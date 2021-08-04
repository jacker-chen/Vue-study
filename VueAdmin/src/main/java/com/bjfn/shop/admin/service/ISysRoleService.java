package com.bjfn.shop.admin.service;

import com.bjfn.shop.admin.common.dto.SysPage;
import com.bjfn.shop.admin.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author bjfn
 * @since 2021-07-22
 */
public interface ISysRoleService extends IService<SysRole> {

    SysPage getRoles(String roleName,Integer curPage, Integer pageSize);

    int addRole(SysRole sysRole);

    int delRole(Long[] ids);

    int editRole(SysRole sysRole);

}
