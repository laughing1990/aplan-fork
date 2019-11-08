package com.augurit.aplanmis.common.event.def;

import com.augurit.aplanmis.common.event.AplanmisEvent;
import com.augurit.aplanmis.common.event.AplanmisEventType;

/**
 * 审批通过 事件
 */
public class ApplyAgreeToleranceAplanmisEvent extends AplanmisEvent {

    public ApplyAgreeToleranceAplanmisEvent(Object source) {
        super(source);
        eventType = AplanmisEventType.APPLY_AGREE;
    }
}
