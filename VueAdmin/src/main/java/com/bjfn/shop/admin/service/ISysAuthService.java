package com.bjfn.shop.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bjfn.shop.admin.common.dto.AuthTree;
import com.bjfn.shop.admin.common.dto.SysPage;
import com.bjfn.shop.admin.entity.SysAuth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 权限表 服务类
 * </p>
 *
 * @author bjfn
 * @since 2021-07-22
 */
public interface ISysAuthService extends IService<SysAuth> {
    SysPage getList(String authName, Integer curPage, Integer pageSize);

    int del(Long[] ids);

    int add(SysAuth sysAuth);

    SysPage search(Map<String, Object> searchForm);

    SysPage getAuthDirTreeOnOne(Integer curPage, Integer pageSize);

    List<AuthTree> getAuthDirChilderByParentId(Integer parentId);

    HashMap<String, Object> add(AuthTree authTree);
}
