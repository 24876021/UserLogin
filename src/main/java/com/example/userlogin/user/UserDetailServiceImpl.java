package com.example.userlogin.user;

import com.example.userlogin.model.SysUser;
import com.example.userlogin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 从数据库中验证账户名、密码验证的过程将由框架帮我们完成，封装隐藏了
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {

        SysUser sysUser = userService.getByAccount(account);
        if (sysUser == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }


        return new AccountUser(sysUser.getId(), sysUser.getName(), sysUser.getPassword(), getUserAuthority(sysUser.getId()));

    }

    /**
     * 获取用户权限信息（角色、菜单权限）
     * @param userId
     * @return
     */
    public List<GrantedAuthority> getUserAuthority(Long userId) {

        String authority = userService.getUserAuthorityInfo(userId);

        return AuthorityUtils.commaSeparatedStringToAuthorityList(authority);
    }
}
