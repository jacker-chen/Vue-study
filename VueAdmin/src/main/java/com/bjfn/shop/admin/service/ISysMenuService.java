package com.bjfn.shop.admin.service;

import com.bjfn.shop.admin.entity.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 系统菜单 服务类
 * </p>
 *
 * @author bjfn
 * @since 2021-07-23
 */
public interface ISysMenuService extends IService<SysMenu> {

    List<SysMenu> getMenuList();
}
