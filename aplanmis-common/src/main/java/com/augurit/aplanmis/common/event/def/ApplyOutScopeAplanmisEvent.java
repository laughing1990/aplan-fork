package com.augurit.aplanmis.common.event.def;

import com.augurit.aplanmis.common.event.AplanmisEvent;
import com.augurit.aplanmis.common.event.AplanmisEventType;

/**
 * 不予受理 事件
 */
public class ApplyOutScopeAplanmisEvent extends AplanmisEvent {

    public ApplyOutScopeAplanmisEvent(Object source) {
        super(source);
        eventType = AplanmisEventType.APPLY_OUT_SCOPE;
    }
}
