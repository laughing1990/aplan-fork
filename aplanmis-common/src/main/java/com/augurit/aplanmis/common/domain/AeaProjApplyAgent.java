package com.augurit.aplanmis.common.domain;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
/**
* 项目代办申请表-模型
*/
@Data
public class AeaProjApplyAgent implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "applyAgentId", value = "主键")
    private String applyAgentId;

    @ApiModelProperty(name = "agentCode", value = "代办申请编号")
    private String agentCode;

    @ApiModelProperty(name = "projInfoId", value = "项目ID")
    private String projInfoId;

    @ApiModelProperty(name = "unitInfoId", value = "单位ID")
    private String unitInfoId;

    @ApiModelProperty(name = "applyUserId", value = "申请人ID")
    private String applyUserId;

    @ApiModelProperty(name = "agentStageState", value = "委托代办阶段：1立项用地规划许可阶段 2 工程建设许可阶段 3施工许可阶段 4竣工验收阶段  多选时逗号拼接")
    private String agentStageState;

    @ApiModelProperty(name = "agentApplyState", value = "代办申请状态 1待签订 2窗口签订中 3 待申请人签章 4已签订（代办中） 5拒绝代办 6窗口终止 7代办终止 8窗口办结 9代办结束")
    private String agentApplyState;

    @ApiModelProperty(name = "windowId", value = "窗口ID")
    private String windowId;

    @ApiModelProperty(name = "agreementCode", value = "协议编号")
    private String agreementCode;

    @ApiModelProperty(name = "agreementSignTime", value = "协议签订时间")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
    private java.util.Date agreementSignTime;

    @ApiModelProperty(name = "agentUserId", value = "代办中心的代办人ID")
    private java.lang.String agentUserId;

    @ApiModelProperty(name = "agentUserName", value = "代办人的姓名")
    private java.lang.String agentUserName;

    @ApiModelProperty(name = "agentUserMobile", value = "代办人的手机号")
    private java.lang.String agentUserMobile;

    @ApiModelProperty(name = "agentStopAgreementFileId", value = "代办终止协议文件ID，存detailId")
    private java.lang.String agentStopAgreementFileId;

    @ApiModelProperty(name = "agentEndAgreementFileId", value = "代办结束办结单文件ID，存detailId")
    private java.lang.String agentEndAgreementFileId;

    @ApiModelProperty(name = "startTime", value = "开始时间")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", locale = "zh", timezone = "GMT+8")
    private java.util.Date startTime;

    @ApiModelProperty(name = "endTime", value = "结束时间")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", locale = "zh", timezone = "GMT+8")
    private java.util.Date endTime;

    @ApiModelProperty(name = "creater", value = "创建人")
    private String creater;

    @ApiModelProperty(name = "createTime", value = "创建时间")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", locale = "zh", timezone = "GMT+8")
    private java.util.Date createTime;

    @ApiModelProperty(name = "modifier", value = "修改人")
    private String modifier;

    @ApiModelProperty(name = "modifyTime", value = "修改时间")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", locale = "zh", timezone = "GMT+8")
    private java.util.Date modifyTime;

    @ApiModelProperty(name = "rootOrgId", value = "根组织ID")
    private String rootOrgId;

    //非表字段
    @ApiModelProperty(name = "localCode", value = "项目代码")
    private String localCode;

    @ApiModelProperty(name = "gcbm", value = "工程代码")
    private String gcbm;

    @ApiModelProperty(name = "projName", value = "项目/工程名称")
    private String projName;

    @ApiModelProperty(name = "themeName", value = "项目类型")
    private String themeName;

    @ApiModelProperty(name = "keyword", value = "查询关键字")
    private String keyword;

    @ApiModelProperty(name = "windowIds", value = "窗口ID数组，查询使用", hidden = true)
    private String[] windowIds;

    @ApiModelProperty(name = "minStartTime",value = "申请时间开始，查询使用")
    private String minStartTime;
    @ApiModelProperty(name = "maxStartTime",value = "申请时间结束，查询使用")
    private String maxStartTime;

    @ApiModelProperty(name = "themeId", value = "主题ID")
    private String themeId;

    @ApiModelProperty(name = "currentUserId", value = "当前登录用户ID")
    private java.lang.String currentUserId;

    @ApiModelProperty(name = "currentUserName", value = "当前登录用户名称")
    private java.lang.String currentUserName;

    @ApiModelProperty(name = "currentUserMobile", value = "当前登录用户手机号")
    private java.lang.String currentUserMobile;

    @ApiModelProperty(name = "unitName", value = "申请单位（甲方）")
    private String unitName;
    @ApiModelProperty(name = "windowName", value = "代办中心名称（乙方）")
    private String windowName;
    @ApiModelProperty(name = "agentStageName", value = "委托代办阶段名称，多个时逗号拼接")
    private String agentStageName;
    @ApiModelProperty(name = "applyUserName", value = "申请人姓名")
    private String applyUserName;
    @ApiModelProperty(name = "applyUserPhone", value = "申请人手机号")
    private String applyUserPhone;

    @ApiModelProperty(name = "agreementFileName", value = "生成的代办协议文件名称")
    private String agreementFileName;
    @ApiModelProperty(name = "detailId", value = "生成的代办协议文件预览和下载的ID")
    private String detailId;

    @ApiModelProperty(name = "agreementStopReason", value = "代办终止单终止原因")
    private String agreementStopReason;

    @ApiModelProperty(name = "agreementStopTime", value = "代办终止单终止日期")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
    private java.util.Date agreementStopTime;

    @ApiModelProperty(name = "agreementEndTime", value = "代办办结单办结日期")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
    private java.util.Date agreementEndTime;
}
