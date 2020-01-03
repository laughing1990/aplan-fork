package com.augurit.aplanmis.common.domain;
import java.io.Serializable;

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

    @ApiModelProperty(name = "agentApplyState", value = "代办申请状态 1可代办 2窗口签订中 3 申请人待确认 4已签订")
    private String agentApplyState;

    @ApiModelProperty(name = "windowId", value = "窗口ID")
    private String windowId;

    @ApiModelProperty(name = "startTime", value = "开始时间")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date startTime;

    @ApiModelProperty(name = "endTime", value = "结束时间")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date endTime;

    @ApiModelProperty(name = "creater", value = "创建人")
    private String creater;

    @ApiModelProperty(name = "createTime", value = "创建时间")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date createTime;

    @ApiModelProperty(name = "modifier", value = "修改人")
    private String modifier;

    @ApiModelProperty(name = "modifyTime", value = "修改时间")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date modifyTime;

    @ApiModelProperty(name = "rootOrgId", value = "根组织ID")
    private String rootOrgId;

}
