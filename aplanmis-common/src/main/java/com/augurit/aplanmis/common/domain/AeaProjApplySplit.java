package com.augurit.aplanmis.common.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
/**
* 项目拆分申请表-模型
*/
@Data
public class AeaProjApplySplit implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "applySplitId", value = "主键")
    private String applySplitId;

    @ApiModelProperty(name = "projInfoId", value = "项目ID")
    private String projInfoId;

    @ApiModelProperty(name = "unitInfoId", value = "单位ID")
    private String unitInfoId;

    @ApiModelProperty(name = "applyUserId", value = "申请人ID")
    private String applyUserId;

    @ApiModelProperty(name = "applyAccountUid", value = "统一认证登录账号uid")
    private String applyAccountUid;

    @ApiModelProperty(name = "stageNo", value = "所处阶段编号 1 工程建设许可阶段  2 施工许可阶段")
    private String stageNo;

    @ApiModelProperty(name = "stageId", value = "阶段ID")
    private String stageId;

    @ApiModelProperty(name = "frontStageProjInfoId", value = "前阶段项目ID")
    private String frontStageProjInfoId;

    @ApiModelProperty(name = "leaderOrgId", value = "牵头部门组织ID")
    private String leaderOrgId;

    @ApiModelProperty(name = "leaderUserId", value = "牵头部门用户ID")
    private String leaderUserId;

    @ApiModelProperty(name = "applyState", value = "申请状态 1未发起  2待审核  3审核通过  4 审核不通过")
    private String applyState;

    @ApiModelProperty(name = "startTime", value = "开始时间")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date startTime;

    @ApiModelProperty(name = "endTime", value = "结束时间")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date endTime;

    @ApiModelProperty(name = "splitMemo", value = "备注说明")
    private String splitMemo;

    @ApiModelProperty(name = "isDeleted", value = "是否逻辑删除，0表示未删除，1表示已删除")
    private String isDeleted;

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

    // 扩展字段
    @ApiModelProperty(value = "阶段名称")
    private String stageName;

    @ApiModelProperty(value = "前阶段工程编码")
    private String frontStageGcbm;

    @ApiModelProperty(value = "工程名称")
    private String projName;

}
