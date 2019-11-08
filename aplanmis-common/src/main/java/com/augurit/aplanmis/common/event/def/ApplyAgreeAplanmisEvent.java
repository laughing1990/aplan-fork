package com.augurit.aplanmis.common.event.def;

import com.augurit.aplanmis.common.event.AplanmisEvent;
import com.augurit.aplanmis.common.event.AplanmisEventType;

/**
 * 容缺通过 事件
 */
public class ApplyAgreeAplanmisEvent extends AplanmisEvent {

    public ApplyAgreeAplanmisEvent(Object source) {
        super(source);
        eventType = AplanmisEventType.APPLY_AGREE_TOLERANCE;
    }
}
