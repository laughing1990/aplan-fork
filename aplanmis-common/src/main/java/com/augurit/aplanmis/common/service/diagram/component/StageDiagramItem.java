package com.augurit.aplanmis.common.service.diagram.component;

import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaHiParStageinst;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("阶段事项")
public abstract class StageDiagramItem extends DiagramItem {

    @ApiModelProperty(value = "阶段id", dataType = "string", notes = "aea_par_stage中的主键")
    protected String stageId;

    @ApiModelProperty(value = "阶段实例")
    protected AeaHiParStageinst aeaHiParStageinst;

    public StageDiagramItem(AeaItemBasic currentItemBasic, AeaHiIteminst aeaHiIteminst) {
        super(currentItemBasic, aeaHiIteminst);
    }

}
