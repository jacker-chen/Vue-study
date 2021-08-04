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
 * 接口资源
 * </p>
 *
 * @author bjfn
 * @since 2021-08-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SysApi对象", description="接口资源")
public class SysApi implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "资源路径")
    private String path;

    @ApiModelProperty(value = "备注")
    private String remark;


}
