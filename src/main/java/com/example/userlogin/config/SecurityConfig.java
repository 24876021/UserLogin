package com.example.userlogin.config;

import com.example.userlogin.filter.CaptchaFilter;
import com.example.userlogin.filter.CustomAuthenticationFilter;
import com.example.userlogin.filter.JwtAuthenticationFilter;
import com.example.userlogin.handler.LoginFailureHandler;
import com.example.userlogin.handler.LoginSuccessHandler;
import com.example.userlogin.handler.jwt.JWTLogoutSuccessHandler;
import com.example.userlogin.handler.jwt.JwtAccessDeniedHandler;
import com.example.userlogin.handler.jwt.JwtAuthenticationEntryPoint;
import com.example.userlogin.handler.password.PasswordEncoder;
import com.example.userlogin.user.UserDetailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 整合所有组件，进行Spring Security全局配置
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true) //可在方法前后以注解的方式进行权限检查
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    LoginFailureHandler loginFailureHandler;

    @Autowired
    LoginSuccessHandler loginSuccessHandler;

    @Autowired
    CaptchaFilter captchaFilter;

    @Autowired
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Autowired
    UserDetailServiceImpl userDetailService;

    @Autowired
    JWTLogoutSuccessHandler jwtLogoutSuccessHandler;

    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager());
        return jwtAuthenticationFilter;
    }

//接口认证白名单，以下接口不需要权限即可访问
    private static final String[] URL_WHITELIST = {
      "/login",         //登录
      "/logout",        //登出
      "/captcha",        //验证码
      "/user/register", //注册
    };

    /**
     * 使用 Spring Security 自带的密码加密器
     */
    @Bean
    PasswordEncoder PasswordEncoder() {
        return new PasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .csrf().disable()

                // 登录配置
                .formLogin()
                //.successHandler(loginSuccessHandler) 使用自定义配置登录拦截器，这里无需配置
                //.failureHandler(loginFailureHandler) 使用自定义配置登录拦截器，这里无需配置

                .and()
                .logout()
                .logoutSuccessHandler(jwtLogoutSuccessHandler)

                // 禁用session
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // 配置拦截规则
                .and()
                .authorizeRequests()
                .antMatchers(URL_WHITELIST).permitAll()
                .anyRequest().authenticated()
                // 异常处理器
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                // 配置自定义的过滤器
                .and()
                .addFilter(jwtAuthenticationFilter())

                //自定义登录拦截 用customAuthenticationFilter 替换 UsernamePasswordAuthenticationFilter
                .addFilterAt(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)

                // 验证码过滤器放在UsernamePassword过滤器之前
                .addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class)
                ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService);
    }

    /**
     * 登录配置，使用自定义的用户名密码验证过滤器
     */
    @Bean
    CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(loginSuccessHandler);
        filter.setAuthenticationFailureHandler(loginFailureHandler);
        // 可自定义登录接口请求路径
        // filter.setFilterProcessesUrl("");
        filter.setAuthenticationManager(authenticationManagerBean());

        return filter;
    }
}
