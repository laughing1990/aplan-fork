package com.augurit.aplanmis.common.event.def;

import com.augurit.aplanmis.common.event.AplanmisEvent;
import com.augurit.aplanmis.common.event.AplanmisEventType;

/**
 * 特程开始 事件
 */
public class ApplySpecificProcStartAplanmisEvent extends AplanmisEvent {

    public ApplySpecificProcStartAplanmisEvent(Object source) {
        super(source);
        eventType = AplanmisEventType.APPLY_SPECIFIC_PROC_END;
    }
}
