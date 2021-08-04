package com.bjfn.shop.admin.service.impl;

import com.bjfn.shop.admin.entity.SysMenu;
import com.bjfn.shop.admin.mapper.SysMenuMapper;
import com.bjfn.shop.admin.service.ISysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统菜单 服务实现类
 * </p>
 *
 * @author bjfn
 * @since 2021-07-23
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Autowired
    private SysMenuMapper menuMapper;

    @Override
    public List<SysMenu> getMenuList() {
        List<SysMenu> allMenuList = menuMapper.selectList(null);
        Integer menuParentId = allMenuList.get(0).getMenuParentId();
        //过滤一级菜单
        List<SysMenu> parentMenuList = allMenuList.stream().filter(e -> e.getMenuParentId() == null).sorted(Comparator.comparing(SysMenu::getSeq)).collect(Collectors.toList());
        //  递归调用，为所有一级菜单设置子菜单
        parentMenuList.stream().forEach(menu -> {
            menu.setChildMenu(getChild(menu.getId(), allMenuList));
        });
        return parentMenuList;
    }

    /**
     * 递归查找子菜单
     *
     * @param id          当前菜单ID
     * @param allMenuList 查找菜单列表
     * @return
     */
    public static List<SysMenu> getChild(Integer id, List<SysMenu> allMenuList) {
        //  子菜单
        List<SysMenu> childList = new ArrayList<>();
        for (SysMenu menu : allMenuList) {
            if (id.equals(menu.getMenuParentId())) {
                childList.add(menu);
            }
        }
        //  为子菜单设置子菜单
        for (SysMenu nav : childList) {
            nav.setChildMenu(getChild(nav.getId(), allMenuList));
        }

        //  排序
        childList = childList.stream().sorted(Comparator.comparing(SysMenu::getSeq)).collect(Collectors.toList());

        if (childList.size() == 0) {
            return new ArrayList<>();
        }
        return childList;
    }
}
