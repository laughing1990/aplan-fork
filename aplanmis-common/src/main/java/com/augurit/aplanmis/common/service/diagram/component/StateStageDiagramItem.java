package com.augurit.aplanmis.common.service.diagram.component;

import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaHiParStageinst;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.service.diagram.constant.HandleStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("情形事项, 隶属于阶段情形下的事项")
public class StateStageDiagramItem extends StageDiagramItem {

    @ApiModelProperty(value = "情形id", dataType = "string", notes = "aea_par_stage_state主键")
    private String parStateId;

    public StateStageDiagramItem(AeaItemBasic currentItemBasic, AeaHiIteminst aeaHiIteminst) {
        super(currentItemBasic, aeaHiIteminst);
    }

    public StateStageDiagramItem(AeaItemBasic currentItemBasic, AeaHiParStageinst aeaHiParStageinst, AeaHiIteminst aeaHiIteminst) {
        super(currentItemBasic, aeaHiIteminst);
        this.aeaHiParStageinst = aeaHiParStageinst;
    }

    /**
     * 情形事项可能存在不用办理状态， 即对应的情形没有选中，那么申报时就不会出现情形事项
     */
    @Override
    public HandleStatus acquireHandleStatus() {
        // 有阶段实例但没有事项实例，说明申报时没有选中情形, 办理状态即为 不用办理
        if (this.aeaHiParStageinst != null && this.aeaHiIteminst == null) {
            finished = HandleStatus.NEED_NOT_HANDLE;
            return finished;
        }
        return super.acquireHandleStatus();
    }
}
