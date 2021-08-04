package com.bjfn.shop.admin.config;

import com.bjfn.shop.admin.security.UserDetailServiceImpl;
import com.bjfn.shop.admin.security.filter.JwtAuthenticationFilter;
import com.bjfn.shop.admin.security.filter.KaptchaFilter;
import com.bjfn.shop.admin.security.handler.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.bcrypt.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //白名单列表
    private final static String[] URL_WHITELIST = {
        "/login", "/sys/logout","/captcha"
    };

    //登录失败处理器
    @Autowired
    private LoginFailureHandler loginFailureHandler;

    //登录成功处理器
    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    //登出成功处理器
    @Autowired
    private JwtLogoutSuccessHandler logoutSuccessHandler;

    //验证码过滤器
    @Autowired
    private KaptchaFilter kaptchaFilter;

    //认证失败处理
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    //权限不足处理
    @Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Autowired
    private UserDetailServiceImpl userDetailService;

    //jwt过滤器
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager());
        return jwtAuthenticationFilter;
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.cors().and().csrf().disable();
        //登录配置
        http.formLogin()
                .successHandler(loginSuccessHandler)//登录成功处理器
                .failureHandler(loginFailureHandler)//登录失败处理器
                .and()
                .logout()
                .logoutUrl("/sys/logout")
                .logoutSuccessHandler(logoutSuccessHandler)
                //禁用session
                .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)//认证失败处理器
                .accessDeniedHandler(jwtAccessDeniedHandler)//权限不足处理器

                .and()
                //配置拦截规则
                    .authorizeRequests()
                    .antMatchers(URL_WHITELIST).permitAll()//白名单
                    .anyRequest().authenticated()//除了白名单，其他都需要登录才可访问
                .and()
                .addFilter(jwtAuthenticationFilter())//添加jwt过滤器，检验jwt
                .addFilterBefore(kaptchaFilter, UsernamePasswordAuthenticationFilter.class)//配置验证码验证过滤器

        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/swagger-ui.html")
                .antMatchers("/webjars/**")
                .antMatchers("/v2/**")
                .antMatchers("/swagger-resources/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
