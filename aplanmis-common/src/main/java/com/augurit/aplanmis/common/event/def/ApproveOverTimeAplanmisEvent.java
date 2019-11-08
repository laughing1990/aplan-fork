package com.augurit.aplanmis.common.event.def;

import com.augurit.aplanmis.common.event.AplanmisEvent;
import com.augurit.aplanmis.common.event.AplanmisEventType;

/**
 * 审批超时事件
 */
public class ApproveOverTimeAplanmisEvent extends AplanmisEvent {

    public ApproveOverTimeAplanmisEvent(Object source) {
        super(source);
        eventType = AplanmisEventType.APPROVE_OVER_TIME;
    }

    public ApproveOverTimeAplanmisEvent(Object source,String taskId) {
        super(source);
        eventType = AplanmisEventType.APPROVE_OVER_TIME;
        this.taskId = taskId;
    }
}
