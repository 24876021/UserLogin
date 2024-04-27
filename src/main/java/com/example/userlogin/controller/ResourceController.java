package com.example.userlogin.controller;

import com.example.userlogin.model.User;
import com.example.userlogin.service.UserService2;
import com.example.userlogin.model.Result2;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@Api(tags = "用户管理相关接口")
public class ResourceController {
    @Autowired
    UserService2 userService;
    /**
     * 查询所有用户
     * @return
     */
    @PreAuthorize("hasAuthority('resource:get')")
    @GetMapping("/AllUsers")
    @ApiOperation("查询所有用户")
    public Result2 getAllUsers(){
        List<User> userList = userService.list();
        if (!userList.isEmpty()){
            return Result2.success(userList);
        }else {
            return Result2.error("查询所有用户失败!");
        }
    }
    /**
     * 根据 id 查询指定用户
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('resource:get')")
    @GetMapping("/user/{id}")
    @ApiOperation(("根据 id 查询指定用户"))
    public Result2 getUserById(@PathVariable("id") Integer id){
        User user = userService.getById(id);
        return Result2.success(user);
    }
    /**
     * 插入用户
     * @param user
     * @return
     */
    @PreAuthorize("hasAuthority('resource:set')")
    @PostMapping("/user")
    @ApiOperation(("插入用户"))
    public Result2 addUser(@RequestBody User user){
        if (userService.save(user)){
            return Result2.success("插入用户成功！");
        }
        else return Result2.error("插入用户失败");
    }
    /**
     * 修改用户信息
     * @param user
     * @return
     */
    @PreAuthorize("hasAuthority('resource:set')")
    @PutMapping("/user")
    @ApiOperation(("修改用户"))
    public Result2 updateUser(@RequestBody User user){
        if (userService.updateById(user)){
            return Result2.success("更新用户成功！");
        }
        else return Result2.error("更新用户失败");
    }
    /**
     * 删除用户
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('resource:remove1')")
    @DeleteMapping("/user")
    @ApiOperation(("删除用户"))
    public Result2 deleteUserById(Integer id){
        if (userService.removeById(id)){
            return Result2.success("删除用户成功！");
        }
        else return Result2.error("删除用户失败！");
    }
}


