package com.augurit.aplanmis.common.vo.analyse;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("委办局部门统计事项详情实体vo")
public class ItemDetailFormVo implements Serializable {
    //公共部分
    @ApiModelProperty(value = "部门名称")
    private String orgName;
    @ApiModelProperty("部门id")
    private String orgId;
    @ApiModelProperty("地区区id")
    private String regionId;
    @ApiModelProperty("地区名称")
    private String regionName;
    //查询返回字段
    @ApiModelProperty(value = "事项实例状态")
    private String iteminstState;
    @ApiModelProperty("事项实例id")
    private String iteminstId;
    @ApiModelProperty("事项id")
    private String itemId;
    @ApiModelProperty("事项名称")
    private String itemName;
    //返回结果部分
    @ApiModelProperty("排名")
    private int sortNo;
    @ApiModelProperty("接件事项数量")
    private int receiptCount;
    @ApiModelProperty("受理事项数量")
    private int acceptCount;
    @ApiModelProperty("不予受理事项数量")
    private int notAcceptCount;
    @ApiModelProperty("结事项数量")
    private int completedCount;
    @ApiModelProperty("办结率")
    private Double completedRate;
    @ApiModelProperty("逾期事项数量")
    private int overdueCount;
    @ApiModelProperty("逾期率")
    private Double overdueRate;

    @ApiModelProperty("来源")
    private String applyinstSource;
    @ApiModelProperty("受理率")
    private Double acceptanceRate;
    @ApiModelProperty("窗口申报数")
    private int winApplyCount;
    @ApiModelProperty("网上申报数")
    private int netApplyCount;
    @ApiModelProperty("时限状态")
    private String instState;

}
