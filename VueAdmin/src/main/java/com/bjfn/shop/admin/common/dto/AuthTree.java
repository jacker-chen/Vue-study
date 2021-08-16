package com.bjfn.shop.admin.common.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="统一返回权限树对角", description="权限资源")
public class AuthTree implements Serializable {
    Integer id;
    String name;
    String permission;
    String type;
    String level;
    Integer parentId;
    boolean hasChildren;
    String remark;
    String order;
}
