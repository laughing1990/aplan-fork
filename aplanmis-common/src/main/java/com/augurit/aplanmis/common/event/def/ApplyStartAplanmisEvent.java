package com.augurit.aplanmis.common.event.def;

import com.augurit.aplanmis.common.event.AplanmisEvent;
import com.augurit.aplanmis.common.event.AplanmisEventType;

/**
 * 申报开始 事件（目前仅用网厅申报件发送短信）
 */
public class ApplyStartAplanmisEvent extends AplanmisEvent {

    public ApplyStartAplanmisEvent(Object source) {
        super(source);
        eventType = AplanmisEventType.APPLY_START;
    }

    public ApplyStartAplanmisEvent(Object source,String applyinstId) {
        super(source);
        eventType = AplanmisEventType.APPLY_START;
        this.applyinstId = applyinstId;
    }
}
