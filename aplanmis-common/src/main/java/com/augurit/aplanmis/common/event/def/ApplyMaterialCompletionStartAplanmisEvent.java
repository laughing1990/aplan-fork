package com.augurit.aplanmis.common.event.def;

import com.augurit.aplanmis.common.event.AplanmisEvent;
import com.augurit.aplanmis.common.event.AplanmisEventType;

/**
 * 补全开始 事件
 */
public class ApplyMaterialCompletionStartAplanmisEvent extends AplanmisEvent {

    public ApplyMaterialCompletionStartAplanmisEvent(Object source) {
        super(source);
        eventType = AplanmisEventType.APPLY_MATERIAL_COMPLETION_START;
    }

    public ApplyMaterialCompletionStartAplanmisEvent(Object source, String applyinstId) {
        super(source);
        eventType = AplanmisEventType.APPLY_MATERIAL_COMPLETION_START;
        this.applyinstId = applyinstId;
    }
}
