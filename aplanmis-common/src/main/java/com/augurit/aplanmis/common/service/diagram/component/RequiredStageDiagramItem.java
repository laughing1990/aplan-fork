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
@ApiModel("必选事项, 隶属于阶段下事项")
public class RequiredStageDiagramItem extends StageDiagramItem {

    @ApiModelProperty(value = "阶段id", dataType = "string", notes = "aea_par_stage主键")
    private String stageId;

    public RequiredStageDiagramItem(AeaItemBasic currentItemBasic, AeaHiParStageinst aeaHiParStageinst, AeaHiIteminst aeaHiIteminst) {
        super(currentItemBasic, aeaHiIteminst);
        this.aeaHiParStageinst = aeaHiParStageinst;
    }

}
