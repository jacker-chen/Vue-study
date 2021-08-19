package com.bjfn.shop.admin.controller;


import com.bjfn.shop.admin.service.impl.SysMenuServiceImpl;
import com.bjfn.shop.admin.common.lang.Result;
import com.bjfn.shop.admin.common.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 系统菜单 前端控制器
 * </p>
 *
 * @author bjfn
 * @since 2021-07-23
 */
@RestController
@RequestMapping("/sysMenu")
@Api(tags = "菜单管理")
public class SysMenuController {

    @Autowired
    private SysMenuServiceImpl sysMenuService;

    @ApiOperation(value = "获取菜单信息")
    @GetMapping("/getMenuList")
    public Result getMenuList(){
        return ResultUtil.success(sysMenuService.getMenuList());
    }
}
