package com.example.userlogin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.userlogin.model.SysUser;

/**
 *
 */
public interface UserService extends IService<SysUser> {
    SysUser getByUsername(String username);

    String getUserAuthorityInfo(Long userId);//定义从数据表的Authority字段中获取权限信息的方法

    SysUser getByAccount(String account);
}
