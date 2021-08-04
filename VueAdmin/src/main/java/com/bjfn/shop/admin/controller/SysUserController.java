package com.bjfn.shop.admin.controller;


import com.bjfn.shop.admin.common.lang.Result;
import com.bjfn.shop.admin.entity.SysUser;
import com.bjfn.shop.admin.service.ISysUserService;
import com.bjfn.shop.admin.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
        sysUserService.getUsers(null,curPage,pageSize);
        return ResultUtil.success("");
    }
}
