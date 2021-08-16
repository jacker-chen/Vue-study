package com.bjfn.shop.admin.controller;


import com.bjfn.shop.admin.common.dto.AuthTree;
import com.bjfn.shop.admin.common.lang.Result;
import com.bjfn.shop.admin.entity.SysAuth;
import com.bjfn.shop.admin.entity.SysAuthDir;
import com.bjfn.shop.admin.service.impl.SysAuthServiceImpl;
import com.bjfn.shop.admin.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 权限表 前端控制器
 * </p>
 *
 * @author bjfn
 * @since 2021-07-22
 */
@RestController
@RequestMapping("/sys/auth")
@Api(tags = "权限管理")
@Slf4j
public class SysAuthController {

    @Autowired
    private SysAuthServiceImpl sysAuthService;

    @GetMapping("/getList")
    @ApiOperation(value = "获取权限信息列表")

    public Result getAuthList(@RequestParam(value = "curPage",defaultValue = "1") Integer curPage,
                              @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize){
        return ResultUtil.success(sysAuthService.getList(null,curPage,pageSize));
    }
    @GetMapping("/search")
    @ApiOperation(value = "获取权限信息列表")
    public Result search(@RequestParam Map<String,Object> searchForm){
        return ResultUtil.success(sysAuthService.search(searchForm));
    }

    @ApiOperation("删除权限")
    @DeleteMapping("/del")
    public Result del(@ApiParam("ids为Long数组") @RequestParam(value = "ids") Long[] ids){
        return ResultUtil.success(sysAuthService.del(ids));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增权限")
    public Result add(@RequestBody SysAuth sysAuth){
        return ResultUtil.success(sysAuthService.add(sysAuth));
    }

    @ApiOperation(value = "获取权限资源首级目录树")
    @GetMapping("/getAuthDirTreeOnOne")
    public Result getAuthDirTreeOnOne(@RequestParam(name = "curPage",defaultValue = "1") Integer curPage,@RequestParam(name = "pageSize",defaultValue = "10") Integer pageSize){
        return ResultUtil.success(sysAuthService.getAuthDirTreeOnOne(curPage,pageSize));
    }

    @ApiOperation(value="根据父类id获取其下级权限资源目录（包含资源数据）")
    @GetMapping("/getAuthDirChilderByParentId/{parentId}")
    public Result getAuthDirChilderByParentId(@PathVariable(name = "parentId") Integer parentId){
        return ResultUtil.success(sysAuthService.getAuthDirChilderByParentId(parentId));
    }

    @ApiOperation(value="新增权限资源信息")
    @PostMapping("/addAuth")
    public Result addAuth(@RequestBody AuthTree authTree){
        return ResultUtil.success(sysAuthService.add(authTree));
    }
}
