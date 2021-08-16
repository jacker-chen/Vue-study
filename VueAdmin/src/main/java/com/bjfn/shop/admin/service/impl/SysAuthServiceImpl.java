package com.bjfn.shop.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjfn.shop.admin.common.dto.AuthTree;
import com.bjfn.shop.admin.common.dto.SysPage;
import com.bjfn.shop.admin.entity.SysAuth;
import com.bjfn.shop.admin.entity.SysAuthDir;
import com.bjfn.shop.admin.entity.SysAuthDirAuth;
import com.bjfn.shop.admin.entity.SysRoleAuth;
import com.bjfn.shop.admin.mapper.SysAuthDirAuthMapper;
import com.bjfn.shop.admin.mapper.SysAuthDirMapper;
import com.bjfn.shop.admin.mapper.SysAuthMapper;
import com.bjfn.shop.admin.mapper.SysRoleAuthMapper;
import com.bjfn.shop.admin.service.ISysAuthService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author bjfn
 * @since 2021-07-22
 */
@Service
public class SysAuthServiceImpl extends ServiceImpl<SysAuthMapper, SysAuth> implements ISysAuthService {

    @Autowired
    private SysAuthMapper sysAuthMapper;

    @Autowired
    private SysRoleAuthMapper sysRoleAuthMapper;

    @Autowired
    private SysAuthDirMapper sysAuthDirMapper;

    @Autowired
    private SysAuthDirAuthMapper sysAuthDirAuthMapper;

    @Override
    public SysPage getList(String authName, Integer curPage, Integer pageSize) {
        Page<SysAuth> sysAuthPage = new Page<>();
        sysAuthPage.setCurrent(curPage);
        sysAuthPage.setSize(pageSize);
        if(StringUtils.isAnyBlank(authName)){
            sysAuthPage = sysAuthMapper.selectPage(sysAuthPage, null);
        }else{
            sysAuthPage = sysAuthMapper.selectPage(sysAuthPage, new QueryWrapper<SysAuth>().lambda().like(SysAuth::getName,authName));
        }
        return new SysPage(curPage,pageSize,sysAuthPage.getTotal(),sysAuthPage.getRecords());
    }

    @Transactional
    @Override
    public int del(Long[] ids) {
        sysRoleAuthMapper.delete(new QueryWrapper<SysRoleAuth>().in("auth_id",ids));
        int i = sysAuthMapper.deleteBatchIds(Arrays.asList(ids));
        return i;
    }

    @Transactional
    @Override
    public int add(SysAuth sysAuth) {
        Integer count = sysAuthMapper.selectCount(new QueryWrapper<SysAuth>().eq("permission", sysAuth.getPermission().trim()));
        if(count>0){
            return 0;
        }
        return sysAuthMapper.insert(sysAuth);
    }

    @Override
    public SysPage search(Map<String, Object> searchForm) {
        Integer curPage =  Integer.parseInt(searchForm.get("curPage").toString());
        Integer pageSize = Integer.parseInt(searchForm.get("pageSize").toString());
        Page<SysAuth> objectPage = new Page<SysAuth>(curPage, pageSize);
        QueryWrapper<SysAuth> sysAuthQueryWrapper = new QueryWrapper<>();
        searchForm.forEach((key,value)->{

            if(key.startsWith("searchFrom") && StringUtils.isNoneBlank(value.toString())){
                String column = key.substring(key.indexOf("[") + 1, key.indexOf("]"));
                sysAuthQueryWrapper.like(column,value);
            }
        });
        Page<SysAuth> sysAuthPage = sysAuthMapper.selectPage(objectPage, sysAuthQueryWrapper);
        return new SysPage(curPage,pageSize,sysAuthPage.getTotal(),sysAuthPage.getRecords());
    }

    @Override
    public SysPage getAuthDirTreeOnOne(Integer curPage, Integer pageSize) {
        Page<SysAuthDir> sysAuthDirPage = new Page<>();
        sysAuthDirPage.setCurrent(curPage);
        sysAuthDirPage.setPages(pageSize);
        Page<SysAuthDir> parentDirList = sysAuthDirMapper.selectPage(sysAuthDirPage, new QueryWrapper<SysAuthDir>().isNull("parent_id").orderByAsc("dir_order"));
        List<SysAuthDir> listRecords = parentDirList.getRecords();
        Collection<AuthTree> trees = listRecords.stream().map(r -> new AuthTree(r.getId(), r.getDirName(), "", "0", r.getDirLev(), null,true,"","")).collect(Collectors.toList());

        SysPage sysPage = new SysPage();
        sysPage.setTotal(parentDirList.getTotal());
        sysPage.setPageSize(pageSize);
        sysPage.setCurrentPage(curPage);
        sysPage.setTableData(trees);
        return sysPage;
    }

    @Override
    public List<AuthTree> getAuthDirChilderByParentId(Integer parentId) {
        List<AuthTree> authTrees = sysAuthDirMapper.getChildrenByParentIdn(parentId);
        if(authTrees.size()==0){
            authTrees = sysAuthMapper.getAuthByParenId(parentId);
        }
        return authTrees;
    }

    @Transactional
    @Override
    public HashMap<String, Object> add(AuthTree authTree) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("flag",false);
        if("0".equals(authTree.getType())){//目录
            if(dirIsExists(authTree.getParentId(),authTree.getName())){
                resultMap.put("msg","已存在该目录");
                return resultMap;
            }
            SysAuthDir paretnDir = sysAuthDirMapper.selectOne(new QueryWrapper<SysAuthDir>().lambda().eq(SysAuthDir::getId, authTree.getParentId()));
            SysAuthDir sysAuthDir = new SysAuthDir();
            Integer lev = Integer.valueOf(paretnDir.getDirLev()) + 1;
            sysAuthDir.setDirLev(lev.toString());
            sysAuthDir.setDirName(authTree.getName());
            sysAuthDir.setRemark(authTree.getRemark());
            sysAuthDir.setParentId(authTree.getParentId());
            sysAuthDirMapper.insert(sysAuthDir);
        }else{//资源
            if(permissionIsExists(authTree.getPermission())){
                resultMap.put("msg","已存在当前资源数据");
                return resultMap;
            }
            SysAuth sysAuth = new SysAuth();
            sysAuth.setName(authTree.getName());
            sysAuth.setPermission(authTree.getPermission());
            sysAuthMapper.insert(sysAuth);
            SysAuthDirAuth sysAuthDirAuth = new SysAuthDirAuth();
            sysAuthDirAuth.setAuthDirId(authTree.getParentId());
            sysAuthDirAuth.setAuthId(sysAuthMapper.selectOne(new QueryWrapper<SysAuth>().lambda().eq(SysAuth::getPermission,authTree.getPermission())).getId());
            sysAuthDirAuthMapper.insert(sysAuthDirAuth);
        }
        resultMap.put("flag",true);
        resultMap.put("msg","操作成功");
        return resultMap;
    }

    /**
     * 检验当前资源编码是否存在
     * @param permission
     * @return
     */
    public boolean permissionIsExists(String permission){
        Integer selectCount = sysAuthMapper.selectCount(new QueryWrapper<SysAuth>().lambda().eq(SysAuth::getPermission,permission));
        if(selectCount == 0){
            return false;
        }
        return true;
    }

    /**
     * 检验当前子级目录下是否存在资源目录
     * @param parentId
     * @param dirName
     * @return
     */
    public boolean dirIsExists(Integer parentId,String dirName){
        Integer selectCount = sysAuthDirMapper.selectCount(new QueryWrapper<SysAuthDir>().lambda().eq(SysAuthDir::getDirName,dirName).eq(SysAuthDir::getParentId,parentId));
        if(selectCount == 0){
            return false;
        }
        return true;
    }
}
