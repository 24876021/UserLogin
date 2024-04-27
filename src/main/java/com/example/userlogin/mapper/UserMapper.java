package com.example.userlogin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.userlogin.model.SysUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<SysUser> {
}
