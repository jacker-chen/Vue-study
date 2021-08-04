package com.bjfn.shop.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 系统菜单
 * </p>
 *
 * @author bjfn
 * @since 2021-07-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 菜单路径
     */
    private String menuPath;

    /**
     * 菜单图片
     */
    private String menuIcon;

    /**
     * 上级菜单id
     */
    private Integer menuParentId;

    /**
     * 0表示不是叶子，1表示是叶子
     */
    private String isLeaf;

    /**
     * 0表示禁用，1表示启用
     */
    private String status;

    /**
     * 同级中的顺序，0-n，从上到下
     */
    private Integer seq;

    /**
     * 对应前端路由名称
     */
    private String component;

    /**
     * 对应前端路由相对路径
     */
    private String componentDir;

    @TableField(exist = false)
    private List<SysMenu> childMenu;


}
