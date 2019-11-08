package com.augurit.aplanmis.common.event.def;

import com.augurit.aplanmis.common.event.AplanmisEvent;
import com.augurit.aplanmis.common.event.AplanmisEventType;

/**
 * 特程结束 事件
 */
public class ApplySpecificProcEndAplanmisEvent extends AplanmisEvent {

    public ApplySpecificProcEndAplanmisEvent(Object source) {
        super(source);
        eventType = AplanmisEventType.APPLY_SPECIFIC_PROC_END;
    }
}
