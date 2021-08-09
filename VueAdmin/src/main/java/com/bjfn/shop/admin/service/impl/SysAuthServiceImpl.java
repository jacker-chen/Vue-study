package com.bjfn.shop.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjfn.shop.admin.common.dto.SysPage;
import com.bjfn.shop.admin.common.lang.Result;
import com.bjfn.shop.admin.entity.SysAuth;
import com.bjfn.shop.admin.entity.SysRoleAuth;
import com.bjfn.shop.admin.mapper.SysAuthMapper;
import com.bjfn.shop.admin.mapper.SysRoleAuthMapper;
import com.bjfn.shop.admin.service.ISysAuthService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjfn.shop.admin.util.ResultUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Arrays;
import java.util.Map;

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
}
