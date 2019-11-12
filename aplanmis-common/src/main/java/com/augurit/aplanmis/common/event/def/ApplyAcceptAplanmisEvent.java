package com.augurit.aplanmis.common.event.def;

import com.augurit.aplanmis.common.event.AplanmisEvent;
import com.augurit.aplanmis.common.event.AplanmisEventType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 申报受理事件
 * @author yinlf
 * @Date 2019/9/3
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ApplyAcceptAplanmisEvent extends AplanmisEvent {

    private String stageId;
    private String userId;
    private String[] projInfoIds;

    public ApplyAcceptAplanmisEvent(Object source, String applyinstId, String userId) {
        super(source);
        this.applyinstId = applyinstId;
        this.userId = userId;
        eventType = AplanmisEventType.Apply_ACCEPT_DEAL;
    }

    public ApplyAcceptAplanmisEvent(Object source, String stageId, String[] projInfoIds, String userId) {
        super(source);
        this.stageId = stageId;
        this.userId = userId;
        this.projInfoIds = projInfoIds;
        eventType = AplanmisEventType.Apply_ACCEPT_DEAL;
    }

    public ApplyAcceptAplanmisEvent(Object source, String stageId, String[] projInfoIds, String userId,String applyinstId) {
        super(source);
        this.stageId = stageId;
        this.userId = userId;
        this.projInfoIds = projInfoIds;
        this.applyinstId = applyinstId;
        eventType = AplanmisEventType.Apply_ACCEPT_DEAL;
    }
}
