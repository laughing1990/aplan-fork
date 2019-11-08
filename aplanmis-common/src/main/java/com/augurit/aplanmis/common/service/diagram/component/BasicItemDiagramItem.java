package com.augurit.aplanmis.common.service.diagram.component;

import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("单项事项")
public class BasicItemDiagramItem extends DiagramItem {

    public BasicItemDiagramItem(AeaItemBasic currentItemBasic, AeaHiIteminst aeaHiIteminst) {
        super(currentItemBasic, aeaHiIteminst);
    }
}
