package com.bjfn.shop.admin.security;

import com.bjfn.shop.admin.entity.SysUser;
import com.bjfn.shop.admin.service.impl.SysUserServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailServiceImpl implements UserDetailsService {


    @Autowired
    private SysUserServiceImpl sysUserService;

    @Override
    public UserDetails loadUserByUsername(String userCode) throws UsernameNotFoundException {
        SysUser sysUser = sysUserService.getUserByUsername(userCode);
        if(sysUser == null){
            throw new InternalAuthenticationServiceException("用户不存在");
        }
        SecurityUserDetails securityUserDetails = new SecurityUserDetails();
        BeanUtils.copyProperties(sysUser, securityUserDetails);
        //写入角色与权限资源
        securityUserDetails.setAuthorities(sysUserService.getUserRoleAndAuth(sysUser.getId()));
        return securityUserDetails;
    }

}
