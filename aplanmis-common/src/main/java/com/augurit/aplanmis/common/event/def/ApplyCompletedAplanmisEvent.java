package com.augurit.aplanmis.common.event.def;

import com.augurit.aplanmis.common.event.AplanmisEvent;
import com.augurit.aplanmis.common.event.AplanmisEventType;

/**
 * 已办结 事件
 */
public class ApplyCompletedAplanmisEvent extends AplanmisEvent {

    public ApplyCompletedAplanmisEvent(Object source) {
        super(source);
        eventType = AplanmisEventType.APPLY_COMPLETED;
    }

    public ApplyCompletedAplanmisEvent(Object source,String applyinstId) {
        super(source);
        eventType = AplanmisEventType.APPLY_COMPLETED;
        this.applyinstId = applyinstId;
    }
}
