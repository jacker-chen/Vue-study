package com.bjfn.shop.admin.mapper;

import com.bjfn.shop.admin.entity.SysAuth;
import com.bjfn.shop.admin.entity.SysRole;
import com.bjfn.shop.admin.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 系统用户表 Mapper 接口
 * </p>
 *
 * @author bjfn
 * @since 2021-07-18
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    SysUser getUserByUserCode(String userCode);

    List<SysRole> getRoleByUserId(Long user_id);

    List<SysAuth> getAuthByUserId(Long user_id);
}
