package com.augurit.aplanmis.common.event.def;

import com.augurit.aplanmis.common.event.AplanmisEvent;
import com.augurit.aplanmis.common.event.AplanmisEventType;

/**
 * 超期默许事件（仅用于唐山）
 */
public class TacitlyAplanmisEvent extends AplanmisEvent {

    public TacitlyAplanmisEvent(Object source) {
        super(source);
        eventType = AplanmisEventType.TACITLY;
    }
}
