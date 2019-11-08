package com.augurit.aplanmis.common.event.def;

import com.augurit.aplanmis.common.event.AplanmisEvent;
import com.augurit.aplanmis.common.event.AplanmisEventType;

/**
 * 补正结束 事件
 */
public class ApplyCorrectMaterialEndAplanmisEvent extends AplanmisEvent {

    public ApplyCorrectMaterialEndAplanmisEvent(Object source) {
        super(source);
        eventType = AplanmisEventType.APPLY_CORRECT_MATERIAL_END;
    }
}
