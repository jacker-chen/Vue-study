package com.bjfn.shop.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 资源权限与目录中间表
 * </p>
 *
 * @author bjfn
 * @since 2021-08-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SysAuthDirAuth对象", description="资源权限与目录中间表")
public class SysAuthDirAuth implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主博")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "权限资源id")
    private Integer authId;

    @ApiModelProperty(value = "权限资源目录id")
    private Integer authDirId;


}
