package com.augurit.aplanmis.common.event.def;

import com.augurit.agcloud.bpm.common.domain.BpmTaskSendObject;
import com.augurit.aplanmis.common.event.AplanmisEvent;
import com.augurit.aplanmis.common.event.AplanmisEventType;
import lombok.Data;

/**
 * 流程发送事件定义
 */
@Data
public class BpmNodeSendAplanmisEvent extends AplanmisEvent {
    private BpmTaskSendObject taskSendObject;

    public BpmNodeSendAplanmisEvent(Object source) {
        super(source);
        eventType = AplanmisEventType.BPM_NODE_SEND;
    }

    public BpmNodeSendAplanmisEvent(Object source, BpmTaskSendObject taskSendObject) {
        super(source);
        eventType = AplanmisEventType.BPM_NODE_SEND;
        this.taskSendObject = taskSendObject;
    }
}
