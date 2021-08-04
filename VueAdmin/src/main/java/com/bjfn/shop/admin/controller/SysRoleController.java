package com.bjfn.shop.admin.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjfn.shop.admin.common.lang.Result;
import com.bjfn.shop.admin.entity.SysRole;
import com.bjfn.shop.admin.service.ISysRoleService;
import com.bjfn.shop.admin.service.impl.SysRoleServiceImpl;
import com.bjfn.shop.admin.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author bjfn
 * @since 2021-07-22
 */

@Api(tags = "角色管理")
@RestController
@RequestMapping("/sys/role")
@Slf4j
public class SysRoleController {

    @Autowired
    private SysRoleServiceImpl sysRoleService;


    @ApiOperation(value = "获取角色信息")
    @GetMapping("/getRoles")
    @PreAuthorize("hasAuthority('sys:role:info')")
    public Result getRoles(@RequestParam(value = "curPage",defaultValue = "1") Integer curPage,@RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        log.info("查询角色{}"+curPage+","+pageSize);
        return ResultUtil.success(sysRoleService.getRoles(null,curPage, pageSize));
    }

    @ApiOperation(value = "新增角色")
    @PutMapping("/add")
    public Result addRole(@RequestBody SysRole sysRole){
        log.info("新增角色：{}",sysRole.toString());
        return ResultUtil.success(sysRoleService.addRole(sysRole));
    }

    @ApiOperation(value = "修改角色")
    @PutMapping("/edit")
    public Result editRole(@RequestBody SysRole sysRole){
        log.info("修改角色：{}",sysRole.toString());
        return ResultUtil.success(sysRoleService.editRole(sysRole));
    }

    @ApiOperation(value = "删除角色")
    @DeleteMapping("/del")
    public Result delRole(@ApiParam("id为数组")@RequestParam(value = "ids") Long[] ids){
        log.info("删除角色id为：{}", Arrays.toString(ids));
        return ResultUtil.success(sysRoleService.delRole(ids));
    }

    @ApiOperation(value = "根据角色名称查询角色")
    @GetMapping("/search/{roleName}")
    public Result searchRole(@PathVariable("roleName") String roleName,@RequestParam(value = "curPage",defaultValue = "1") Integer curPage,@RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        return ResultUtil.success(sysRoleService.getRoles(roleName,curPage,pageSize));
    }
}
