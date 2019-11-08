package com.augurit.aplanmis.supermarket.apply.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "中介事项申报参数")
public class ImItemApplyData {
    @ApiModelProperty(value = "申请实例ID")
    private String applyinstId;
    @ApiModelProperty(value = "申报来源，网上申报：net、窗口申报：win", required = true, allowableValues = "net, win")
    private String applySource;
    @ApiModelProperty(value = "申报主体 0表示个人，1表示企业", required = true, allowableValues = "0, 1")
    private String applySubject;
    @ApiModelProperty(value = "联系人ID", required = true)
    private String linkmanInfoId;
    @ApiModelProperty(value = "模板ID", hidden = true)
    private String appId;
    @ApiModelProperty(value = "事项版本ID", required = true)
    private String itemVerId;
    //    @ApiModelProperty(value = "分局承办；并行推进事项分局承办，格式为：[{\"itemVerId\":\"111\",\"branchOrg\":\"222\"}]", required = true)
//    private String branchOrgMap;
    @ApiModelProperty(value = "项目ID", required = true)
    private String projInfoId;

    @ApiModelProperty(value = "建设单位ID", required = true)
    String constructionUnitId;//
    @ApiModelProperty(value = "材料实例ID集合", required = true)
    private String[] matinstsIds;
    @ApiModelProperty(value = "办理意见", required = true)
    private String comments;
    @ApiModelProperty(value = "申请联系人ID,", notes = "当申报主体为个人时：必输")
    private String applyLinkmanId;

    @ApiModelProperty(value = "情形ID集合", hidden = true)
    private String[] stateIds;
    @ApiModelProperty(value = "是否为投资审批项目：1 是，0 否")
    String isApproveProj;
//    @ApiModelProperty(value = "采购需求项目ID")
//    String projPurchaseId;

    private String creater;
    private String rootOrgId;


}
