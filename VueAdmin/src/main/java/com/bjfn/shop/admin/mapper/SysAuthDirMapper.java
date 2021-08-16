package com.bjfn.shop.admin.mapper;

import com.bjfn.shop.admin.common.dto.AuthTree;
import com.bjfn.shop.admin.entity.SysAuthDir;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 资源权限目录表 Mapper 接口
 * </p>
 *
 * @author bjfn
 * @since 2021-08-12
 */
@Mapper
public interface SysAuthDirMapper extends BaseMapper<SysAuthDir> {

    List<AuthTree> getChildrenByParentIdn(Integer parentId);
}
