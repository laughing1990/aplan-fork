package com.augurit.aplanmis.front.apply.vo.guide;

import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.aplanmis.common.domain.AeaHiGuideDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@ApiModel("辅导vo")
@Getter
@Setter
public class GuideVo {

    @ApiModelProperty(value = "部门辅导id")
    private String guideId;

    @ApiModelProperty(value = "部门辅导明细类型", notes = "s表示智能引导，o表示业主，l表示牵头部门，i表示事项部门，r表示最终结果")
    private String detailType;

    @ApiModelProperty(value = "主题ID")
    private String themeId;

    @ApiModelProperty(value = "主题版本ID")
    private String themeVerId;

    @ApiModelProperty(value = "阶段ID")
    private String stageId;

    @ApiModelProperty(value = "辅导明细状态：0表示未开始，1表示辅导中，2表示已完成")
    private String detailState;

    @ApiModelProperty(value = "辅导的事项明细")
    private List<GuideActionVo> guideActionVos;

    @ApiModel("辅导明细vo")
    @Getter
    @Setter
    private static class GuideActionVo {

        @ApiModelProperty(value = "辅导变更操作，c表示change，a表示add，d表示delete")
        private String guideChangeAction;

        @ApiModelProperty(value = "实施事项ID")
        private String itemId;

        @ApiModelProperty(value = "实施事项版本")
        private String itemVerId;

        @ApiModelProperty(value = "标准事项版本id")
        private String catalogItemVerId;

        @ApiModelProperty(value = "辅导意见")
        private String guideOpinion;

        @ApiModelProperty(value = "辅导部门id", notes = "如果是牵头部门，则从aeaHiGuide拿; 如果是审批部门, 则从carryOutItem中拿")
        private String orgId;
    }

    public List<AeaHiGuideDetail> toAeaHiGuideDetails(String guideUserId) {
        Assert.hasText(this.guideId, "辅导主记录id不能为空");
        Assert.hasText(this.detailType, "辅导类型detailType不能为空");
        Assert.hasText(this.themeId, "项目类型themeId不能为空");
        Assert.hasText(this.themeVerId, "项目类型版本themeVerId不能为空");
        Assert.hasText(this.stageId, "阶段stageId不能为空");
        Assert.hasText(this.detailState, "辅导明细状态detailState不能为空");
        Assert.isTrue(CollectionUtils.isNotEmpty(guideActionVos), "辅导明细不能为空");

        List<AeaHiGuideDetail> aeaHiGuideDetails = new ArrayList<>();
        guideActionVos.forEach(vo -> {
            Assert.hasText(vo.getGuideChangeAction(), "操作类型不能为空");
            Assert.hasText(vo.getOrgId(), "辅导部门不能为空");
            Assert.hasText(vo.getItemVerId(), "实施事项版本id不能为空");
            Assert.hasText(vo.getItemId(), "实施事项id不能为空");

            AeaHiGuideDetail aeaHiGuideDetail = new AeaHiGuideDetail();
            aeaHiGuideDetail.setGuideId(this.guideId);
            aeaHiGuideDetail.setDetailType(this.detailType);
            aeaHiGuideDetail.setThemeId(this.themeId);
            aeaHiGuideDetail.setThemeVerId(this.themeVerId);
            aeaHiGuideDetail.setStageId(this.stageId);
            aeaHiGuideDetail.setDetailState(this.detailState);
            aeaHiGuideDetail.setGuideChangeAction(vo.getGuideChangeAction());
            aeaHiGuideDetail.setItemId(vo.getItemId());
            aeaHiGuideDetail.setItemVerId(vo.getItemVerId());
            aeaHiGuideDetail.setCatalogItemVerId(vo.getCatalogItemVerId());
            aeaHiGuideDetail.setGuideOrgId(vo.getOrgId());
            aeaHiGuideDetail.setGuideUserId(guideUserId);
            aeaHiGuideDetail.setGuideOpinion(vo.getGuideOpinion());
            aeaHiGuideDetails.add(aeaHiGuideDetail);
        });
        return aeaHiGuideDetails;
    }
}
