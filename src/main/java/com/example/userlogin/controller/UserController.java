package com.example.userlogin.controller;

import com.example.userlogin.constant.RsaProperties;
import com.example.userlogin.model.Result;
import com.example.userlogin.model.SysUser;
import com.example.userlogin.handler.password.PasswordEncoder;
import com.example.userlogin.service.UserService;
import com.example.userlogin.utils.RSAUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 用户控制器
 * @author tian
 *
 * login 和 logout 由框架管理
 */
@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/register")
    @ApiOperation("用户注册")
    public Result register(@RequestBody SysUser sysUser){
        ReentrantLock lock = new ReentrantLock();
        lock.lock();
        try {
            //查询当前账号是否存在
            SysUser sysUser1 = userService.getByAccount(sysUser.getAccount());
            if (sysUser1 !=null){
                return Result.fail("当前账号已经存在，请更换账号");
            }
            //REA解密
            String pwd = RSAUtils.decryptByPrivate(sysUser.getPassword(), RsaProperties.privateKey);
            //SpringSecurity 密码编码
            String encodePwd = passwordEncoder.encode(pwd);
            sysUser.setPassword(encodePwd);
            userService.save(sysUser);
            return Result.succ("注册成功！");

        }finally {
            lock.unlock();
        }

    }
}
