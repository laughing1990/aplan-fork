package com.augurit.aplanmis.mall.userCenter.vo;

import com.augurit.aplanmis.common.apply.item.GuideComputedItem;
import com.augurit.aplanmis.common.vo.guide.GuideDetailVo;
import com.augurit.aplanmis.mall.main.vo.ParallelApproveItemVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("并联审批事项确认返回结果VO")
public class ApplyIteminstConfirmVo {
    @ApiModelProperty(value = "主题ID")
    private String themeId;
    @ApiModelProperty(value = "主题名称")
    private String themeName;
    @ApiModelProperty(value = "旧主题ID")
    private String oldThemeId;
    @ApiModelProperty(value = "旧主题版本ID")
    private String oldThemeName;
    @ApiModelProperty(value = "阶段ID")
    private String stageId;
    @ApiModelProperty(value = "阶段名称")
    private String stageName;
    @ApiModelProperty(value = "并行审批事项列表")
    private List<GuideComputedItem> coreIteminstList;
    @ApiModelProperty(value = "并联推进事项列表")
    private List<GuideComputedItem> parallelIteminstList;
    @ApiModelProperty(value = "部门意见")
    private String deptComments;
    @ApiModelProperty(value = "是否变更主题  1是 0否")
    private String isThemeChange;

    public static ApplyIteminstConfirmVo formatGuide(GuideDetailVo detail) {
        ApplyIteminstConfirmVo vo=new ApplyIteminstConfirmVo();
        vo.setCoreIteminstList(detail.getOptionItems());
        vo.setParallelIteminstList(detail.getParallelItems());
        vo.setThemeId(detail.getNewThemeId());
        vo.setStageId(detail.getNewStageId());
        vo.setIsThemeChange(detail.isThemeChanged()?"1":"0");
        vo.setDeptComments(detail.getAeaHiGuide().getLeaderOrgOpinion());
        vo.setOldThemeId(detail.getAeaHiGuide().getThemeId());
        vo.setOldThemeName(detail.getAeaHiGuide().getThemeName());
        vo.setStageName(detail.getAeaHiGuide().getStageName());
        return vo;
    }
}
