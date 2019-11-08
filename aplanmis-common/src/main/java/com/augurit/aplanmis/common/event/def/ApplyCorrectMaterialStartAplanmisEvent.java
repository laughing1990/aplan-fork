package com.augurit.aplanmis.common.event.def;

import com.augurit.aplanmis.common.event.AplanmisEvent;
import com.augurit.aplanmis.common.event.AplanmisEventType;

/**
 * 补正开始 事件
 */
public class ApplyCorrectMaterialStartAplanmisEvent extends AplanmisEvent {

    public ApplyCorrectMaterialStartAplanmisEvent(Object source) {
        super(source);
        eventType = AplanmisEventType.APPLY_CORRECT_MATERIAL_START;
    }

    public ApplyCorrectMaterialStartAplanmisEvent(Object source,String iteminstId) {
        super(source);
        eventType = AplanmisEventType.APPLY_CORRECT_MATERIAL_START;
        this.iteminstId = iteminstId;
    }
}
