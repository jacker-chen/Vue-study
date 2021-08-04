package com.bjfn.shop.admin;

import com.bjfn.shop.admin.security.UserDetailServiceImpl;
import com.bjfn.shop.admin.service.impl.SysUserServiceImpl;
import com.bjfn.shop.admin.util.JwtUtil;
import com.bjfn.shop.admin.util.RedisUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;


@SpringBootTest
@Slf4j
public class AuthTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private SysUserServiceImpl sysUserService;

    @Test
    public void test(){
        Claims claimsByToken = jwtUtil.getClaimsFromToken("eyJhbGciOiJIUzUxMiJ9.eyJjcmVhdGVkIjoxNjI3NjA3NTQ4MzY2LCJleHAiOjE2Mjc2MTM1NDgsInVzZXJuYW1lIjoiYWRtaW4ifQ.ev-IKNjjtZ4_BA0VTCbkm6DREX0eJWCBFli_kMVIqaZXuPf6nDyAqcZyGMfo2OCScjY6ZxHTMM-C6FXwCQ105A");
        log.info(claimsByToken.toString());
    }


    @Test
    public void getToken(){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("admin", null, sysUserService.getUserRoleAndAuth(1L));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        String token = jwtUtil.generateToken(authenticationToken.getName());
        redisUtil.set(authenticationToken.getName(),token,jwtUtil.getExpire());
        log.info(token);
    }
}
