package com.augurit.aplanmis.front.apply.vo.guide;

import com.augurit.aplanmis.common.constants.GuideApplyState;
import com.augurit.aplanmis.common.domain.AeaHiGuide;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@ApiModel("部门辅导结束vo")
public class GuideFinishVo {

    @ApiModelProperty(value = "部门辅导id")
    private String guideId;

    @ApiModelProperty(value = "牵头部门汇总意见")
    private String leaderOrgOpinion;

    public AeaHiGuide toAeaHiGuide(String currentUserId) {
        AeaHiGuide aeaHiGuide = new AeaHiGuide();
        aeaHiGuide.setGuideId(guideId);
        aeaHiGuide.setApplyState(GuideApplyState.APPLICANT_CONFIRMING.getValue());
        aeaHiGuide.setGuideEndTime(new Date());
        aeaHiGuide.setModifyTime(new Date());
        aeaHiGuide.setModifier(currentUserId);
        aeaHiGuide.setLeaderOrgOpinion(leaderOrgOpinion);
        return aeaHiGuide;
    }
}
