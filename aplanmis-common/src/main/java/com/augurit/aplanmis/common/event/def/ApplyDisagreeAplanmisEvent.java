package com.augurit.aplanmis.common.event.def;

import com.augurit.aplanmis.common.event.AplanmisEvent;
import com.augurit.aplanmis.common.event.AplanmisEventType;

/**
 * 审批不通过 事件
 */
public class ApplyDisagreeAplanmisEvent extends AplanmisEvent {

    public ApplyDisagreeAplanmisEvent(Object source) {
        super(source);
        eventType = AplanmisEventType.APPLY_DISAGREE;
    }
}
