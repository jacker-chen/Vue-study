package com.bjfn.shop.admin.controller;


import com.bjfn.shop.admin.entity.SysUser;
import com.bjfn.shop.admin.service.ISysUserService;
import com.bjfn.shop.admin.common.lang.Result;
import com.bjfn.shop.admin.common.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 系统用户表 前端控制器
 * </p>
 *
 * @author bjfn
 * @since 2021-07-18
 */
@RestController
@RequestMapping("/sysUser")
@Api(tags = "用户管理")
@Validated
@Slf4j
public class SysUserController {
    @Autowired
    private ISysUserService sysUserService;

    @ApiOperation("获取所用用户信息")
    @GetMapping("/getUserList")
    public Result getUserList(){
        return ResultUtil.success(sysUserService.getUserList());
    }

    @ApiOperation(value = "分页获取用户信息")
    @GetMapping("/getUsers")
    public Result getUsers(@RequestParam(value = "curPage",defaultValue = "1") Integer curPage, @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        return ResultUtil.success(sysUserService.getUsers(null,curPage,pageSize));
    }

    @ApiOperation(value = "根据用户名查询用户列表信息")
    @GetMapping("/search/{userName}")
    public Result searchUsers(@PathVariable("userName") String userName,@RequestParam(value = "curPage",defaultValue = "1") Integer curPage, @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        return ResultUtil.success(sysUserService.getUsers(userName,curPage,pageSize));
    }

    @ApiOperation(value="新增用户")
    @PostMapping("/add")
    public Result add(@RequestBody SysUser sysUser){
        return ResultUtil.success(sysUserService.add(sysUser));
    }

    @ApiOperation(value="删除用户")
    @DeleteMapping("/del")
    public Result del(@ApiParam("id为数组")@RequestParam(value = "ids") Long[] ids){
        return ResultUtil.success(sysUserService.del(ids));
    }

    @ApiOperation(value = "修改用户")
    @PostMapping("/edit")
    public Result edit(@RequestBody SysUser sysUser){
        return ResultUtil.success(sysUserService.edit(sysUser));
    }
    @ApiOperation(value = "分配角色")
    @PutMapping("/allotRole")
    public Result allotRole(@RequestParam(value="userId") Long userId,
                            @RequestParam(value = "roleIds")Long[] roleIds){
        return ResultUtil.success(sysUserService.allotRole(userId,roleIds));
    }

    @ApiOperation(value="根据用户id查询用户拥有的角色与还没拥有的角色")
    @GetMapping("/getUserRoleById/{userId}")
    public Result getUserRoleById(@PathVariable(name = "userId") Long userId){
        Map<String,Object> result = sysUserService.getUserRoleById(userId);
        return ResultUtil.success(result);
    }
}
