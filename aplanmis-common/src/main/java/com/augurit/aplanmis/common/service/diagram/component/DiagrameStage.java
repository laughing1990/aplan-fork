package com.augurit.aplanmis.common.service.diagram.component;

import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaHiParStageinst;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.service.diagram.constant.HandleStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@ApiModel("阶段")
public class DiagrameStage {

    @ApiModelProperty(value = "阶段实体", notes = "表中的具体阶段")
    private AeaParStage currentStage;

    @ApiModelProperty(value = "阶段实例实体")
    private AeaHiParStageinst aeaHiParStageinst;

    @ApiModelProperty(value = "申报实例实体")
    private AeaHiApplyinst aeaHiApplyinst;

    @ApiModelProperty(value = "并联申报事项")
    private ParallelStageDiagramItem parallelStageDiagramItem;

    @ApiModelProperty(value = "并行申报事项")
    private OptionStageDiagramItem optionStageDiagramItem;

    @ApiModelProperty(value = "办结依据", dataType = "string", notes = "1:属于该审批阶段的所有里程碑事项办结，该审批阶段才算办结;2:属于该审批阶段的任一项里程碑事项办结，该审批阶段就算办结")
    private String finishedDependency;

    @ApiModelProperty(value = "办理状态", notes = "详情查看 HandleStatus 枚举")
    protected HandleStatus finished;

    @ApiModelProperty("阶段办理开始时间")
    private Date stageStartTime;

    @ApiModelProperty("阶段办理结束时间")
    private Date stageEndTime;

    @ApiModelProperty(value = "阶段办理时长", notes = "以 天 为单位")
    private Double duringTime;

    public DiagrameStage(AeaParStage currentStage, ParallelStageDiagramItem parallelStageDiagramItem, OptionStageDiagramItem optionStageDiagramItem) {
        this.currentStage = currentStage;
        this.finishedDependency = currentStage.getLcbsxlx();
        this.parallelStageDiagramItem = parallelStageDiagramItem;
        this.optionStageDiagramItem = optionStageDiagramItem;
    }

    /// 使用龙飞提供的计算逻辑设置阶段办理状态
    /**
     * 前提：阶段的办结是根据并联事项来判断的
     * <p>
     * 阶段的办结依赖于 里程碑事项是否办结
     *
     * @return 阶段的办结状态
     */
   /* public HandleStatus acquireHandleStatus() {
        this.finished = HandleStatus.UN_FINISHED;

        // 没有申报实例 谈何办结
        if (aeaHiApplyinst == null) return finished;

        switch (this.finishedDependency) {
            // 属于该审批阶段的所有里程碑事项办结，该审批阶段才算办结
            case "1":
                finished = allMilestoneItemFinished();
                break;
            // 属于该审批阶段的任一项里程碑事项办结，该审批阶段就算办结
            case "2":
                finished = anyMilestoneItemFinished();
                break;
            // 根据申报实例的状态来计算
            default:
                finished = defaultHandStatusByAeaHiApplyinst();
        }
        if (finished == HandleStatus.UNKNOWN) {
            finished = defaultHandStatusByAeaHiApplyinst();
        }
        return finished;
    }

    private HandleStatus defaultHandStatusByAeaHiApplyinst() {
        String applyState = this.aeaHiApplyinst.getApplyinstState();
        if (ApplyState.COMPLETED.getValue().equals(applyState)) {
            return HandleStatus.FINISHED;
        } else if (ApplyState.OUT_SCOPE.getValue().equals(applyState)) {
            return HandleStatus.UN_FINISHED;
        }
        return HandleStatus.HANDLING;
    }

    *//**
     * 任一里程碑事项是否办结
     *//*
    private HandleStatus anyMilestoneItemFinished() {
        return parallelStageDiagramItem.anyMilestoneItemFinished();
    }

    *//**
     * 所有里程碑事项办结
     *//*
    private HandleStatus allMilestoneItemFinished() {
        return parallelStageDiagramItem.allMilestoneItemFinished();
    }*/
}
