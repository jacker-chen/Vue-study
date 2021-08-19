package com.bjfn.shop.admin.security.handler;

import cn.hutool.json.JSONUtil;
import com.bjfn.shop.admin.entity.SysUser;
import com.bjfn.shop.admin.service.impl.SysUserServiceImpl;
import com.bjfn.shop.admin.util.JwtUtil;
import com.bjfn.shop.admin.common.util.RedisUtil;
import com.bjfn.shop.admin.common.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * 登录成功处理器
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private SysUserServiceImpl sysUserService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = response.getOutputStream();
        //生成token
        String token = jwtUtil.generateToken(authentication.getName());
        response.setHeader("authorization",token);
        redisUtil.set(authentication.getName(),token,jwtUtil.getExpire());
        SysUser sysUser = sysUserService.getUserByUsername(authentication.getName());
        String authorityStr = sysUserService.getAuthorityStr(sysUser.getId());
        HashMap<String, String> resultMap = new HashMap<>();
        resultMap.put("username",sysUser.getUserName());
        resultMap.put("authorityStr",authorityStr);
        outputStream.write(JSONUtil.toJsonStr(ResultUtil.success(resultMap)).getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}

