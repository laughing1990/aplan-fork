package com.augurit.aplanmis.common.service.diagram.component;

import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.aplanmis.common.service.diagram.constant.HandleStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ApiModel("并联事项")
public class ParallelStageDiagramItem {

    @ApiModelProperty(value = "里程碑事项数量")
    private int milestoneItemCnt;

    @ApiModelProperty(value = "情形事项", notes = "隶属于阶段情形下的事项")
    protected List<StateStageDiagramItem> stateStageDiagramItems;

    @ApiModelProperty(value = "必选事项", notes = "直接隶属于阶段下的事项")
    protected List<RequiredStageDiagramItem> requiredStageDiagramItems;

    public ParallelStageDiagramItem(List<StateStageDiagramItem> stateStageDiagramItems, List<RequiredStageDiagramItem> requiredStageDiagramItems) {
        this.milestoneItemCnt = 0;
        this.stateStageDiagramItems = new ArrayList<>();
        this.requiredStageDiagramItems = new ArrayList<>();
        if (stateStageDiagramItems != null) {
            this.stateStageDiagramItems.addAll(stateStageDiagramItems);
        }
        if (requiredStageDiagramItems != null) {
            this.requiredStageDiagramItems.addAll(requiredStageDiagramItems);
            requiredStageDiagramItems.forEach(item -> {
                if (item.milestone) {
                    milestoneItemCnt++;
                }
            });
        }
    }

    /**
     * 任一里程碑事项是否办结
     */
    HandleStatus anyMilestoneItemFinished() {
        HandleStatus handleStatus = HandleStatus.UNKNOWN;
        if (CollectionUtils.isNotEmpty(this.requiredStageDiagramItems)) {
            for (StageDiagramItem item : requiredStageDiagramItems) {
                if (item.milestone && (item.acquireHandleStatus() == HandleStatus.FINISHED || item.acquireHandleStatus() == HandleStatus.TOLERENCE_PASS)) {
                    return HandleStatus.FINISHED;
                }
            }
            // 有里程碑事项，但都是未办结
            if (this.milestoneItemCnt > 0) {
                handleStatus = HandleStatus.HANDLING;
            }
        }

        return handleStatus;
    }

    /**
     * 所有里程碑事项办结
     */
    HandleStatus allMilestoneItemFinished() {
        HandleStatus handleStatus = HandleStatus.UNKNOWN;
        int finishedCnt = 0;
        if (CollectionUtils.isNotEmpty(this.requiredStageDiagramItems)) {
            for (StageDiagramItem item : requiredStageDiagramItems) {
                if (item.milestone && item.acquireHandleStatus() == HandleStatus.FINISHED) {
                    finishedCnt++;
                }
            }
            // 已办数于里程碑事项总数相等 -> 办结
            if (milestoneItemCnt > 0 && milestoneItemCnt == finishedCnt) {
                handleStatus = HandleStatus.FINISHED;
            }
        }
        return handleStatus;
    }
}
