package com.bjfn.shop.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjfn.shop.admin.common.dto.SysPage;
import com.bjfn.shop.admin.entity.SysRole;
import com.bjfn.shop.admin.entity.SysRoleAuth;
import com.bjfn.shop.admin.entity.SysUserRole;
import com.bjfn.shop.admin.mapper.SysRoleAuthMapper;
import com.bjfn.shop.admin.mapper.SysRoleMapper;
import com.bjfn.shop.admin.mapper.SysUserRoleMapper;
import com.bjfn.shop.admin.service.ISysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author bjfn
 * @since 2021-07-22
 */
@Service
@Slf4j
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SysRoleAuthMapper sysRoleAuthMapper;

    @Override
    public SysPage getRoles(String roleName,Integer curPage, Integer pageSize) {

        Page<SysRole> rolePage = new Page<>(curPage, pageSize);
        if(!StringUtils.isBlank(roleName)){
            rolePage = sysRoleMapper.selectPage(rolePage, new QueryWrapper<SysRole>().lambda().like(SysRole::getRoleName,roleName));
        }else{
            rolePage = sysRoleMapper.selectPage(rolePage, null);
        }
        SysPage sysPage = new SysPage();
        sysPage.setCurrentPage(curPage);
        sysPage.setPageSize(pageSize);
        sysPage.setTotal(rolePage.getTotal());
        sysPage.setTableData(rolePage.getRecords());
        return sysPage;
    }

    @Transactional
    @Override
    public int addRole(SysRole sysRole) {

        List<SysRole> roleList = sysRoleMapper.selectList(new QueryWrapper<SysRole>().lambda().eq(SysRole::getRoleName, sysRole.getRoleName()));
        if(roleList.size()>0){
            return 0;
        }
        return sysRoleMapper.insert(sysRole);
    }
    @Transactional
    @Override
    public int delRole(Long[] ids) {
        int i = sysRoleMapper.deleteBatchIds(Arrays.asList(ids));
        //删除中间表
        sysRoleAuthMapper.delete(new QueryWrapper<SysRoleAuth>().in("role_id",ids));
        sysUserRoleMapper.delete(new QueryWrapper<SysUserRole>().in("role_id",ids));
        return i;

    }

    @Transactional
    @Override
    public int editRole(SysRole sysRole) {
        List<SysRole> roleList = sysRoleMapper.selectList(new QueryWrapper<SysRole>().lambda()
                .eq(SysRole::getRoleName, sysRole.getRoleName())
                .ne(SysRole::getId,sysRole.getId())
        );
        if(roleList.size()>0){
            return 0;
        }
        int i = sysRoleMapper.updateById(sysRole);
        return i;
    }

}
