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
 * 资源权限目录表
 * </p>
 *
 * @author bjfn
 * @since 2021-08-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SysAuthDir对象", description="资源权限目录表")
public class SysAuthDir implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "权限目录名称")
    private String dirName;

    @ApiModelProperty(value = "目录级别")
    private String dirLev;

    @ApiModelProperty(value = "排序")
    private String dirOrder;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "父级目录id")
    private Integer parentId;


}
