package com.augurit.aplanmis.common.event.def;

import com.augurit.aplanmis.common.event.AplanmisEvent;
import com.augurit.aplanmis.common.event.AplanmisEventType;
import lombok.Data;

/**
 * @author yinlf
 * @Date 2019/9/3
 */
@Data
public class ItemCompletedEvent extends AplanmisEvent {

    private String iteminstId;
    private String orgId;
    private String userId;

    public ItemCompletedEvent(Object source, String iteminstId, String orgId, String userId) {
        super(source);
        this.iteminstId = iteminstId;
        this.orgId = orgId;
        this.userId = userId;
        eventType = AplanmisEventType.STATE_ITEM_COMPLETED;
    }
}
