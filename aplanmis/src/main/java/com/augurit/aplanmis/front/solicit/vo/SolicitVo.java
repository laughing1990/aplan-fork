package com.augurit.aplanmis.front.solicit.vo;

import com.augurit.aplanmis.common.domain.AeaHiSolicit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author:chendx
 * @date: 2019-12-24
 * @time: 11:04
 */
@Data
@ApiModel("意见征求模型")
public class SolicitVo {

    @ApiModelProperty(value = "主键", required = false, dataType="string")
    private String solicitId;

    @ApiModelProperty(value = "征求意见类型：i表示按事项征求，d表示按部门征求", required = true, dataType="string")
    private String solicitType;

    @ApiModelProperty(value = "征求业务类型，一次征询、意见征求、部门辅导", required = true, dataType="string")
    private String busType;

    @ApiModelProperty(value = "征求详细信息，包括事项或审批部门信息，格式[{\"itemId\":\"123\",\"itemVerId\":\"123\",\"orgId\":\"123\",\"orgName\":\"123\",\"opinion\":\"123\"}]", required = true, dataType="string")
    private String detailInfo;

    @ApiModelProperty(value = "按组织征求配置ID【当SOLICIT_TYPE=d】", required = false, dataType="string")
    private String solicitOrgId;

    @ApiModelProperty(value = "按事项征求配置ID【当SOLICIT_TYPE=i】", required = false, dataType="string")
    private String solicitItemId;

    @ApiModelProperty(value = "发起征求的申报ID", required = true, dataType="string")
    private String applyinstId;

    @ApiModelProperty(value = "发起征求的流程实例ID", required = true, dataType="string")
    private String procinstId;

    @ApiModelProperty(value = "发起征求的历史任务实例ID", required = true, dataType="string")
    private String hiTaskinstId;

    @ApiModelProperty(value = "意见征求流水号", required = false, dataType="string")
    private String solicitCode;

    @ApiModelProperty(value = "意见征求主题", required = true, dataType="string")
    private String solicitTopic;

    @ApiModelProperty(value = "征求意见内容", required = false, dataType="string")
    private String solicitContent;

    @ApiModelProperty(value = "意见征求承诺时限，例如：5个工作日，那该字段为5", required = true, dataType="string")
    private Long solicitDueDays;

    @ApiModelProperty(value = "意见征求实际时限，例如：5个工作日，那该字段为5", required = false, dataType="string")
    private Long solicitRealDays;

    @ApiModelProperty(value = "意见征求时限单位，z表示自然日，g表示工作日", required = false, dataType="string")
    private String solicitDaysUnit;

    @ApiModelProperty(value = "意见征求开始时间", required = false, dataType="string")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date solicitStartTime;

    @ApiModelProperty(value = "意见征求结束时间", required = false, dataType="string")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date solicitEndTime;

    @ApiModelProperty(value = "是否纳入计时,1是0否", required = false, dataType="string")
    private String isCalcTimerule;

    @ApiModelProperty(value = "意见征求计算策略表ID", required = false, dataType="string")
    private String solicitTimeruleId;

    @ApiModelProperty(value = "办结结论标志位，0表示不通过，1表示通过", required = false, dataType="string")
    private String conclusionFlag;

    @ApiModelProperty(value = "办结结论描述", required = false, dataType="string")
    private String conclusionDesc;

    @ApiModelProperty(value = "填写结论时间", required = false, dataType="string")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date conclusionTime;

    @ApiModelProperty(value = "填写结论用户ID", required = false, dataType="string")
    private String conclusionUserId;

    @ApiModelProperty(value = "填写结论用户姓名", required = false, dataType="string")
    private String conclusionUserName;

    @ApiModelProperty(value = "意见征求发起组织ID", required = false, dataType="string")
    private String initiatorOrgId;

    @ApiModelProperty(value = "意见征求发起组织名称", required = false, dataType="string")
    private String initiatorOrgName;

    @ApiModelProperty(value = "意见征求发起用户ID", required = false, dataType="string")
    private String initiatorUserId;

    @ApiModelProperty(value = "意见征求发起用户姓名", required = false, dataType="string")
    private String initiatorUserName;

    @ApiModelProperty(value = "办理次数限制，0表示无限制，1或以上表示限制次数", required = false, dataType="string")
    private Long countLimit;

    @ApiModelProperty(value = "征求意见状态：0表示未开始，1表示征求中，2表示已完成，3表示已终止", required = false, dataType="string")
    private String solicitState;

    @ApiModelProperty(value = "创建人", required = false, dataType="string")
    private String creater;

    @ApiModelProperty(value = "创建时间", required = false, dataType="string")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime;

    @ApiModelProperty(value = "修改人", required = false, dataType="string")
    private String modifier;

    @ApiModelProperty(value = "修改时间", required = false, dataType="string")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime;

    @ApiModelProperty(value = "根组织ID", required = false, dataType="string")
    private String rootOrgId;

    public AeaHiSolicit convertToAeaHiSolicit(){
        AeaHiSolicit aeaHiSolicit = new AeaHiSolicit();
        BeanUtils.copyProperties(this,aeaHiSolicit);
        return aeaHiSolicit;
    }
}
