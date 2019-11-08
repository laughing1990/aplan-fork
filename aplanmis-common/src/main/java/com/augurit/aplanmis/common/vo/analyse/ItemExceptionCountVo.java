package com.augurit.aplanmis.common.vo.analyse;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Ma Yanhao
 * @date 2019/9/1 10:48
 */
@Data
@ApiModel(value = "事项办理异常统计实体")
public class ItemExceptionCountVo {
    @ApiModelProperty(value = "排名")
    private int sortNo;
    @ApiModelProperty(value = "事项版本ID")
    private String itemVerId;
    @ApiModelProperty(value = "事项名称")
    private String itemName;
    @ApiModelProperty(value = "受理部门")
    private String orgName;
    @ApiModelProperty(value = "受理数")
    private Integer totalCount;
    @ApiModelProperty(value = "不予受理数")
    private Integer noAcceptCount;
    @ApiModelProperty(value = "逾期数")
    private Integer overdueCount;
    @ApiModelProperty(value = "异常数")
    private Integer exceptionCount;
    @ApiModelProperty(value = "不予受理率")
    private Double noAcceptRate;
    @ApiModelProperty(value = "逾期率")
    private Double overdueRate;
    @ApiModelProperty(value = "异常率")
    private Double exceptionRate;
}
