package com.bjfn.shop.admin.security.handler;

import cn.hutool.json.JSONUtil;
import com.bjfn.shop.admin.common.lang.Result;
import com.bjfn.shop.admin.util.JwtUtil;
import com.bjfn.shop.admin.util.RedisUtil;
import com.bjfn.shop.admin.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 退出处理器
 */
@Component
@Slf4j
public class JwtLogoutSuccessHandler implements LogoutSuccessHandler {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private JwtUtil jwtUtil;


    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = response.getOutputStream();
        if(authentication != null){//手工退出
            new SecurityContextLogoutHandler().logout(request,response,authentication);
        }
        String token = request.getHeader("Authorization").substring(jwtUtil.getHeader().length()).trim();
        String username = jwtUtil.getUserCodeFromToken(token);
        log.info(token);
        log.info(username+"，已登出系统");
        response.setHeader("authorization","");
        redisUtil.del(username);
        redisUtil.del("GrantedAuthority:"+username);

        Result<String> success = ResultUtil.success("");
        outputStream.write(JSONUtil.toJsonStr(success).getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}
