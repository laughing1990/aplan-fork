package com.augurit.aplanmis.common.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author tiantian
 * @date 2019/6/13
 */
@Data
@ApiModel("符合采购项目条件")
public class QueryProjPurchaseVo {

    @ApiModelProperty(value = "采购项目名称")
    private String projName;

    @ApiModelProperty(value = "服务id")
    private String serviceId;

    @ApiModelProperty(value = "企业id")
    private String unitInfoId;

    @ApiModelProperty(value = "项目业主名称")
    private String applicant;

    @ApiModelProperty(value = "竞价类型：1 随机中标，2 自主选择")
    private String biddingType;//竞价类型：1 随机中标，2 自主选择

    @ApiModelProperty(value = "开始时间,yyyy-MM-dd")
    private String startTime;

    @ApiModelProperty(value = "结束时间,yyyy-MM-dd")
    private String endTime;

    @ApiModelProperty(value = "当前页")
    private int pageNum;

    @ApiModelProperty(value = "每页记录数")
    private int pageSize;

    private List serviceIds;// 用于参数转换
}
