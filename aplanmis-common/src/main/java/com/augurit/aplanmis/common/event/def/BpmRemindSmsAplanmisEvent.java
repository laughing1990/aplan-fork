package com.augurit.aplanmis.common.event.def;

import com.augurit.aplanmis.common.event.AplanmisEvent;
import com.augurit.aplanmis.common.event.AplanmisEventType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 催办短信事件
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BpmRemindSmsAplanmisEvent extends AplanmisEvent {
    private String userId;//被催办提醒用户ID

    public BpmRemindSmsAplanmisEvent(Object source,String applyinstId,String userId) {
        super(source);
        eventType = AplanmisEventType.BPM_REMIND_SEND;
        this.applyinstId = applyinstId;
        this.userId = userId;
    }
}
