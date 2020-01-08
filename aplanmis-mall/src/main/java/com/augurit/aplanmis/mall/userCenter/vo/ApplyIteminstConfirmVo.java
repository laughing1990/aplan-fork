package com.augurit.aplanmis.mall.userCenter.vo;

import com.augurit.agcloud.framework.util.StringUtils;
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
//    @ApiModelProperty(value = "是否变更主题  1是 0否")
//    private String isThemeChange;

    @ApiModelProperty(value = "牵头部门选择主题ID")
    private String leaderThemeId;
    @ApiModelProperty(value = "牵头部门选择主题名称")
    private String leaderThemeName;

    @ApiModelProperty(value = "智能引导选择主题名称")
    private String itThemeName;

    @ApiModelProperty(value = "申请人选择主题ID")
    private String applyThemeId;
    @ApiModelProperty(value = "申请人选择主题名称")
    private String applyThemeName;

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

    @ApiModelProperty(value = "项目ID")
    private String projInfoId;
    @ApiModelProperty(value = "项目名称")
    private String projName;
    @ApiModelProperty(value = "项目/工程代码")
    private String gcbm;

    @ApiModelProperty(value = "是否智能引导 1 是 0否")
    private String isItSel;


    public static ApplyIteminstConfirmVo formatGuide(GuideDetailVo detail) {
        ApplyIteminstConfirmVo vo=new ApplyIteminstConfirmVo();
        vo.setCoreIteminstList(detail.getOptionItems());
        vo.setParallelIteminstList(detail.getParallelItems());

        vo.setApplyThemeId(detail.getAeaHiGuide().getThemeId());
        vo.setApplyThemeName(detail.getAeaHiGuide().getThemeName());

        vo.setLeaderThemeId(StringUtils.isNotBlank(detail.getNewThemeId())?detail.getNewThemeId():detail.getAeaHiGuide().getThemeId());
        vo.setLeaderThemeName(StringUtils.isNotBlank(detail.getNewThemeName())?detail.getNewThemeName():detail.getAeaHiGuide().getThemeName());

        vo.setStageId(StringUtils.isNotBlank(detail.getNewStageId())?detail.getNewStageId():detail.getAeaHiGuide().getStageId());
        vo.setStageName(detail.getAeaHiGuide().getStageName());
        vo.setDeptComments(detail.getAeaHiGuide().getLeaderOrgOpinion());
        return vo;
    }
}
