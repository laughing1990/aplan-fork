package com.augurit.aplanmis.mall.userCenter.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("部门辅导申请参数")
public class AeaGuideApplyVo {
    @ApiModelProperty("申报来源")
    private String applySource;
    @ApiModelProperty("申报主体类型 1单位 0个人")
    private String applySubject;
    @ApiModelProperty("联系人ID")
    private String  linkmanInfoId;
    @ApiModelProperty("单位ID")
    private String  unitInfoId;
    @ApiModelProperty("申请实例ID")
    private String applyinstId;
    @ApiModelProperty("主题ID")
    private String themeId;
    @ApiModelProperty("主题版本ID")
    private String themeVerId;
    @ApiModelProperty("阶段ID")
    private String stageId;
    @ApiModelProperty("项目ID")
    private String projInfoId;
    @ApiModelProperty("事项列表")
    private List<AeaGuideItemVo> itemList;

    @ApiModelProperty("智能引导事项列表")
    private List<AeaGuideItemVo> itItemList;
    @ApiModelProperty("是否智能引导 1是 0否")
    private String isItGuide;

    @ApiModelProperty(value = "情形ID集合")
    private String[] stateIds;
    @ApiModelProperty(value = "单位项目关联ID", required = true)
    private List<String> projUnitIds;

    @ApiModelProperty(value = "智能引导主题ID")
    private String itThemeId;
    @ApiModelProperty(value = "智能引导主题版本ID", required = true)
    private String itThemeVerId;
    @ApiModelProperty(value = "智能引导阶段ID", required = true)
    private String itStageId;

}
