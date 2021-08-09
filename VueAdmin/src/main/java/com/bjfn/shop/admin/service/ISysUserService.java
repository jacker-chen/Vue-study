package com.bjfn.shop.admin.service;

import com.bjfn.shop.admin.common.dto.SysPage;
import com.bjfn.shop.admin.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

/**
 * <p>
 * 系统用户表 服务类
 * </p>
 *
 * @author bjfn
 * @since 2021-07-18
 */
public interface ISysUserService extends IService<SysUser> {

    List<SysUser> getUserList();
    List<GrantedAuthority> getUserRoleAndAuth(Long user_id);
    SysUser getUserByUsername(String username);

    SysPage getUsers(String userName, Integer curPage, Integer pageSize);

    int add(SysUser sysUser);

    int del(Long[] ids);

    int edit(SysUser sysUser);
}
