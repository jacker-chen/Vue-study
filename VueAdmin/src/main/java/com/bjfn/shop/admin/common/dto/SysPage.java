package com.bjfn.shop.admin.common.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value="统一返回分页对象", description="接口资源")
public class SysPage implements Serializable {
    private Integer currentPage;
    private Integer pageSize;
    private Long total;
    private Object tableData;
}
