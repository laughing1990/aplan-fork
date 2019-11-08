package com.augurit.aplanmis.supermarket.projPurchase.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author tiantian
 * @date 2019/6/13
 */
@Data
@ApiModel("业主单位未发布的项目列表")
public class QueryUnpublishedProjInfo {

    @ApiModelProperty(value = "项目代码")
    private String projCode;

    @ApiModelProperty(value = "当前页",required = true)
    private int pageNum;

    @ApiModelProperty(value = "每页记录数",required = true)
    private int pageSize;

    @Override
    public String toString() {
        return "QueryUnpublishedProjInfo{" +
                "projCode='" + projCode + '\'' +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }
}
