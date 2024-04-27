package com.example.userlogin.filter;

import cn.hutool.core.util.StrUtil;
import com.example.userlogin.model.SysUser;
import com.example.userlogin.service.UserService;
import com.example.userlogin.user.UserDetailServiceImpl;
import com.example.userlogin.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * jwt校验器：校验前端发送的token是否正确或过期，若校验成功，则获取用户信息并存入Spring Security以便后续调用
 */

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private UserService userService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jwt = request.getHeader(jwtUtils.getHeader());
        // 这里如果没有jwt，继续往后走，因为后面还有鉴权管理器等去判断是否拥有身份凭证，所以是可以放行的
        // 没有jwt相当于匿名访问，若有一些接口是需要权限的，则不能访问这些接口
        if (StrUtil.isBlankOrUndefined(jwt)) {
            chain.doFilter(request, response);
            return;
        }

        Claims claim = jwtUtils.getClaimsByToken(jwt);
        if (claim == null) {
            throw new JwtException("token 异常");
        }
        if (jwtUtils.isTokenExpired(claim)) {
            throw new JwtException("token 已过期");
        }

        String username = claim.getSubject();


        // 获取用户的权限等信息
        SysUser sysUser = userService.getByUsername(username);


        // 构建UsernamePasswordAuthenticationToken,这里密码为null，是因为提供了正确的JWT,实现自动登录
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, null, userDetailService.getUserAuthority(sysUser.getId()));

        //若JWT验证成功，我们构建了一个UsernamePasswordAuthenticationToken对象，用于保存用户信息，之后将该对象交给SecurityContextHolder，set进它的context中，这样后续我们就能通过调用
        SecurityContextHolder.getContext().setAuthentication(token);
        //SecurityContextHolder.getContext().getAuthentication().getPrincipal()等方法获取到当前登录的用户信息了。

        chain.doFilter(request, response);

    }
}
