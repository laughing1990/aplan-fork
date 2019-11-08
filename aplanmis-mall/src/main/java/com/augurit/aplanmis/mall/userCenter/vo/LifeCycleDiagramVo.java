package com.augurit.aplanmis.mall.userCenter.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/*
  生命周期图VO
 */
@Data
@ApiModel("生命周期图VO")
public class LifeCycleDiagramVo{

    @ApiModelProperty("项目名称")
    private String projName;
    @ApiModelProperty("项目ID")
    private String projInfoId;
    @ApiModelProperty("项目区划ID")
    private String regionId;
    @ApiModelProperty("主题名称(项目类型)")
    private String themeName;
    @ApiModelProperty("主题ID")
    private String themeId;
    @ApiModelProperty("主题类型")
    private String themeType;
    @ApiModelProperty("阶段列表")
    private List<AeaParStageVo> aeaParStages;
    @ApiModelProperty("当前阶段ID")
    private String currentStageId;


}
