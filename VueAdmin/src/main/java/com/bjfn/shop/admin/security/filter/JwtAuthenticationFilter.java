package com.bjfn.shop.admin.security.filter;

import cn.hutool.core.util.StrUtil;
import com.bjfn.shop.admin.common.exception.KaptchaException;
import com.bjfn.shop.admin.entity.SysUser;
import com.bjfn.shop.admin.mapper.SysUserMapper;
import com.bjfn.shop.admin.service.impl.SysUserServiceImpl;
import com.bjfn.shop.admin.util.JwtUtil;
import com.bjfn.shop.admin.util.RedisUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * jwt过滤器，用于对token进行过滤验证
 */

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private SysUserServiceImpl sysUserService;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
       //获取token
        String token = request.getHeader("Authorization");
        if(StrUtil.isBlankOrUndefined(token)){
            chain.doFilter(request,response);
            return;
        }
        token = token.substring(jwtUtil.getHeader().length()).trim();
        Claims claims = jwtUtil.getClaimsFromToken(token);
        if(claims == null){
            throw new JwtException("token 异常");
        }
        String userCode = (String) claims.get("userCode");
        SysUser sysUser = sysUserService.getUserByUsername(userCode);

        String cachToken = (String) redisUtil.get(userCode);
        if(cachToken == null){
            throw new JwtException("token 已过期");
        }
        redisUtil.set(userCode,token,jwtUtil.getExpire());

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userCode, null, sysUserService.getUserRoleAndAuth(sysUser.getId()));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request,response);

    }
}
