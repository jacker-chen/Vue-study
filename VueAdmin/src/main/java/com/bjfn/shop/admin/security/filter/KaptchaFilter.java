package com.bjfn.shop.admin.security.filter;

import com.bjfn.shop.admin.common.exception.KaptchaException;
import com.bjfn.shop.admin.security.handler.LoginFailureHandler;
import com.bjfn.shop.admin.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



/**
 * 验证码过滤器
 */
@Component
public class KaptchaFilter extends OncePerRequestFilter {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private LoginFailureHandler loginFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        //只对login请求并是post请求过滤
        if("/login".equals(uri) && "POST".equals(request.getMethod())){
            try{
                //检验验证码
                validate(request);
            }catch (KaptchaException e){//验证失败交由登录验证失败处理器处理
                loginFailureHandler.onAuthenticationFailure(request,response,e);
            }
        }
        filterChain.doFilter(request,response);
    }
    //检验验证码
    private void validate(HttpServletRequest request) {
        String code = request.getParameter("code");
        String key = request.getParameter("key");
        if(StringUtils.isEmpty(code) || StringUtils.isEmpty(key)){
            throw new KaptchaException("验证码不能为空");
        }
        Object o = redisUtil.get(key);
        if(StringUtils.isEmpty(o)){
            throw new KaptchaException("验证码已过期");
        }
        if(!o.equals(code)){
            throw new KaptchaException("验证码错误");
        }
        //一次性验证码
        redisUtil.del(key);
    }
}
