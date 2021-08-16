package com.bjfn.shop.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bjfn.shop.admin.common.dto.AuthTree;
import com.bjfn.shop.admin.entity.SysAuth;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 权限表 Mapper 接口
 * </p>
 *
 * @author bjfn
 * @since 2021-07-22
 */
@Mapper
public interface SysAuthMapper extends BaseMapper<SysAuth> {

    List<AuthTree> getAuthByParenId(Integer parentId);
}
