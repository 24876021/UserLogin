package com.example.userlogin.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.userlogin.model.SysUser;
import com.example.userlogin.mapper.UserMapper;
import com.example.userlogin.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, SysUser> implements UserService {
    @Override
    public SysUser getByUsername(String username) {
        return this.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getName,username));
    }

    @Override
    public String getUserAuthorityInfo(Long userId) {
        SysUser sysUser = getById(userId);
        return sysUser.getAuthority();
    }//实现从数据表的Authority字段中获取权限信息的方法

    @Override
    public SysUser getByAccount(String account) {
        return this.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getAccount,account));
    }
}
