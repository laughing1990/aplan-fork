package com.augurit.aplanmis.supermarket.notice.service;

import com.augurit.aplanmis.common.domain.AeaImProjPurchase;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class SelectionNoticeVo {

    @ApiModelProperty(value = "主键ID")
    private String projPurchaseId;

    @ApiModelProperty(value = "服务项目ID")
    private String projInfoId;

    @ApiModelProperty(value = "业主单位ID")
    private String publishUnitInfoId;
    @ApiModelProperty(value = "个人申报时采购人ID")
    private String publishLinkmanInfoId;

    @ApiModelProperty(value = "服务类型和中介服务事项关联ID")
    private String serviceItemId;

    @ApiModelProperty(value = "最低价格（万元）")
    private String basePrice;

    @ApiModelProperty(value = "采购需求状态：0：未提交，1：服务中，2：服务完成，3：服务中止，4：审核中，5：退回，6：报名中，7：选取中，8：选取开始，9：已选取，10：无效，11：待选取，12：已过时")
    private String auditFlag;

    @ApiModelProperty(value = "竞价天数")
    private String bidNum;

    @ApiModelProperty(value = "竞价时间单位")
    private String bjType;

    @ApiModelProperty(value = "是否删除：1 已删除，0 未删除")
    private String isDelete;

    @ApiModelProperty(value = "是否启用：1 已启用，0 未启用")
    private String isActive;

    @ApiModelProperty(value = "竞价类型：1 随机中标，2 自主选择 3 竞价选取")
    private String biddingType;

    @ApiModelProperty(value = "备注")
    private String memo;

    @ApiModelProperty(value = "最高价格（万元）")
    private String highestPrice;

    @ApiModelProperty(value = "服务时间说明")
    private String serviceTimeExplain;

    @ApiModelProperty(value = "服务内容")
    private String serviceContent;

    @ApiModelProperty(value = "业主联系人")
    private String contacts;

    @ApiModelProperty(value = "联系电话")
    private String moblie;

    @ApiModelProperty(value = "发布时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date publishTime;

    @ApiModelProperty(value = "金额说明")
    private String amountExplain;

    @ApiModelProperty(value = "资质备案说明")
    private String qualRecordRequire;

    @ApiModelProperty(value = "是否为投资审批项目：1 是，0 否")
    private String isApproveProj;

    @ApiModelProperty(value = "关联的审批流水号")
    private String applyinstCode;

    @ApiModelProperty(value = "是否公示中选机构： 1 是， 0 否")
    private String isDiscloseIm;

    @ApiModelProperty(value = "是否公示中标公告：1 是， 0 否")
    private String isDiscloseBidding;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "审核时间")
    private java.util.Date auditTime;

    @ApiModelProperty(value = "审核意见")
    private String auditOpinion;

    @ApiModelProperty(value = "业主投诉电话")
    private String ownerComplaintPhone;

    @ApiModelProperty(value = "中介机构要求信息ID")
    private String unitRequireId;

    @ApiModelProperty(value = "服务选择条件 1 多个服务具备其一，0 多个服务都具备")
    private String selectCondition;

    @ApiModelProperty(value = "是否确认金额，1 是 0 否")
    private String isDefineAmount;

    @ApiModelProperty(value = "截止日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date expirationDate;

    @ApiModelProperty(value = "业主委托人信息ID")
    private String linkmanInfoId;

    @ApiModelProperty(value = "选取中介时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date choiceImunitTime;

    @ApiModelProperty(value = "报价方式,0 金额 1 下浮率")
    private String quoteType;

    @ApiModelProperty(value = "是否有企业回避：1 是，0 否")
    private String isAvoid;

    @ApiModelProperty(value = "【当IS_AVOID=1时必填】回避原因")
    private String avoidReason;
    @ApiModelProperty(value = "发布单位或个人名字")
    private String applicant;
    //=======================中标单位信息
    private String selectedEndTime;//选取结束时间
    private String selectedApplicant;//中选机构名称
    private String selectedUnitInfoId;//中选机构id
    private String selectedAppAddr;//中选机构地址
    //============中介服务事项信息 ======================

    //======================采购项目信息
    private String projName;
    private String itemName;
    private String itemBasicId;
    private String localCode;
    @ApiModelProperty("采购项目规模")
    private String projScale;
    @ApiModelProperty("采购项目规模内容")
    private java.lang.String projScaleContent;

    @ApiModelProperty(value = "服务名称")
    private String serviceName;

    @ApiModelProperty("投资审批项目")
    private ApproveProjInfo approveProjInfo;

    public SelectionNoticeVo() {
    }

    public SelectionNoticeVo(AeaImProjPurchase projPurchase) {
        BeanUtils.copyProperties(projPurchase, this);
        this.approveProjInfo = new ApproveProjInfo();
    }

    public void change2proj(AeaProjInfo projInfo) {
        BeanUtils.copyProperties(projInfo, this.approveProjInfo);
    }

    @Data
    @ApiModel("投资审批项目信息")
    public class ApproveProjInfo {
        @ApiModelProperty("主键ID")
        private java.lang.String projInfoId; // (主键)
        @ApiModelProperty("地方编码")
        private java.lang.String localCode; // (地方编码)
        @ApiModelProperty("项目名称")
        private java.lang.String projName; // (项目名称)
    }
}
