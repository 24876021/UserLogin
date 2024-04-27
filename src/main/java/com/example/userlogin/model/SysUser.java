package com.example.userlogin.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author tian
 */
@Data
public class SysUser {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String account;

    private String password;

    /**
     * 权限字段
     */
    private String authority;

    private String name;

}
