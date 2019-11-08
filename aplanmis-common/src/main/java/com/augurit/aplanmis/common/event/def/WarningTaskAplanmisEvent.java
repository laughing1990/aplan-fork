package com.augurit.aplanmis.common.event.def;

import com.augurit.aplanmis.common.event.AplanmisEvent;
import com.augurit.aplanmis.common.event.AplanmisEventType;

/**
 * 办件预警事件
 */
public class WarningTaskAplanmisEvent extends AplanmisEvent {

    public WarningTaskAplanmisEvent(Object source,String taskId) {
        super(source);
        eventType = AplanmisEventType.WARNING_TASK;
        this.taskId = taskId;
    }
}
