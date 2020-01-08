package com.augurit.aplanmis.common.apply.item;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.aplanmis.common.constants.GuideChangeAction;
import com.augurit.aplanmis.common.constants.GuideDetailType;
import com.augurit.aplanmis.common.domain.AeaHiGuideDetail;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaItemState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 换算后的事项
 */
@Getter
@Setter
@ApiModel(value = "部门辅导事项")
public class GuideComputedItem extends ComputedItem {

    @ApiModelProperty(value = "承诺办结时限单位")
    private String bjType;

    @ApiModelProperty(value = "办理时限，以工作日为单位")
    private Double dueNum;

    @ApiModelProperty(value = "是否智能引导")
    private boolean intelliGuideChoose;

    @ApiModelProperty(value = "申请人选择")
    private boolean applicantChoose;

    @ApiModelProperty(value = "申请人意见")
    private String applicantOpinion;

    @ApiModelProperty(value = "牵头部门选择")
    private boolean leaderDeptChoose;

    @ApiModelProperty(value = "牵头部门意见")
    private String leanderDeptOpinion;

    @ApiModelProperty(value = "审批部门选择")
    private boolean approveDeptChoose;

    @ApiModelProperty(value = "审批部门意见")
    private String approveDeptOpinion;

    @ApiModelProperty(value = "事项情形列表")
    List<AeaItemState> itemStateList;

    protected void mergeCommonField(AeaItemBasic origin) {
        super.mergeCommonField(origin);
        this.bjType = origin.getBjType();
        this.dueNum = origin.getDueNum();
    }

    void fillInfo(AeaHiGuideDetail aeaHiGuideDetail) {
        if (!"0".equals(aeaHiGuideDetail.getDetailState())) {
            GuideDetailType guideDetailType = GuideDetailType.fromValue(aeaHiGuideDetail.getDetailType());
            switch (guideDetailType) {
                case INTELLIGENCE:
                    intelliGuideChoose = true;
                    break;
                case OWNER:
                    applicantChoose = true;
                    applicantOpinion = aeaHiGuideDetail.getGuideOpinion();
                    break;
                case LEADER_DEPT:
                    if (GuideChangeAction.ADD.getValue().equals(aeaHiGuideDetail.getGuideChangeAction())) {
                        leaderDeptChoose = true;
                    }
                    leanderDeptOpinion = aeaHiGuideDetail.getGuideOpinion();
                    break;
                case ITEM_DEPT:
                    if (GuideChangeAction.ADD.getValue().equals(aeaHiGuideDetail.getGuideChangeAction())) {
                        approveDeptChoose = true;
                    }
                    approveDeptOpinion = aeaHiGuideDetail.getGuideOpinion();
                    break;
            }
        }

        // 标准事项需要重新匹配实施事项
        if (Status.ON.equals(this.isCatalog)) {
            for (CarryOutItem co : this.carryOutItems) {
                if (co.getItemVerId().equals(aeaHiGuideDetail.getItemVerId())) {
                    this.currentCarryOutItem = co;
                    break;
                }
            }
        }
    }
}

