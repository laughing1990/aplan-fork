package com.augurit.aplanmis.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@ApiModel(value = "部门辅导详情")
@Getter
@Setter
@ToString
public class AeaHiGuideDetail {

    @ApiModelProperty(value = "主键")
    private String guideDetailId;

    @ApiModelProperty(value = "部门辅导信息表ID")
    private String guideId;

    @ApiModelProperty(value = "部门辅导明细类型，包括：s表示智能引导，o表示业主，l表示牵头部门，i表示事项部门，r表示最终结果")
    private String detailType;

    @ApiModelProperty(value = "主题ID")
    private String themeId;

    @ApiModelProperty(value = "主题版本ID")
    private String themeVerId;

    @ApiModelProperty(value = "阶段ID")
    private String stageId;

    @ApiModelProperty(value = "实施事项ID")
    private String itemId;

    @ApiModelProperty(value = "实施事项版本")
    private String itemVerId;

    @ApiModelProperty(value = "辅导部门ID")
    private String guideOrgId;

    @ApiModelProperty(value = "辅导用户ID")
    private String guideUserId;

    @ApiModelProperty(value = "辅导意见")
    private String guideOpinion;

    @ApiModelProperty(value = "辅导明细状态：0表示未开始，1表示辅导中，2表示已完成")
    private String detailState;

    @ApiModelProperty(value = "辅导开始时间")
    private Date detailStartTime;

    @ApiModelProperty(value = "辅导结束时间")
    private Date detailEndTime;

    @ApiModelProperty(value = "创建人")
    private String creater;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    private String modifier;

    @ApiModelProperty(value = "修改时间")
    private Date modifyTime;
}