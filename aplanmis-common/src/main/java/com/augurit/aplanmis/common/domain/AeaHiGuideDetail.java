package com.augurit.aplanmis.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

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

    @ApiModelProperty(value = "辅导变更操作，c表示change，a表示add，d表示delete")
    private String guideChangeAction;

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

    @ApiModelProperty(value = "标准事项版本id")
    private String catalogItemVerId;

    @ApiModelProperty(value = "辅导部门ID")
    private String guideOrgId;

    @ApiModelProperty(value = "辅导用户ID")
    private String guideUserId;

    @ApiModelProperty(value = "辅导意见")
    private String guideOpinion;

    @ApiModelProperty(value = "辅导明细状态：0表示未开始，1表示辅导中，2表示已完成")
    private String detailState;

    @ApiModelProperty(value = "辅导开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date detailStartTime;

    @ApiModelProperty(value = "辅导结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date detailEndTime;

    @ApiModelProperty(value = "创建人")
    private String creater;

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    private String modifier;

    @ApiModelProperty(value = "修改时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;

    // 扩展字段

    private String themeName;
    private String itemName;
}