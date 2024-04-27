package com.example.userlogin.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.userlogin.mapper.UserMapper2;
import com.example.userlogin.model.User;
import com.example.userlogin.service.UserService2;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl2 extends ServiceImpl<UserMapper2, User> implements UserService2 {
}
