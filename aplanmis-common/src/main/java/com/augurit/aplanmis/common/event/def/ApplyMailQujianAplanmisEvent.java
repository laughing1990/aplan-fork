package com.augurit.aplanmis.common.event.def;

import com.augurit.aplanmis.common.event.AplanmisEvent;
import com.augurit.aplanmis.common.event.AplanmisEventType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 邮寄方式取件触发事件
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ApplyMailQujianAplanmisEvent extends AplanmisEvent {
    private String expressNum;//快递号

    public ApplyMailQujianAplanmisEvent(Object source,String applyinstId,String expressNum) {
        super(source);
        this.eventType = AplanmisEventType.Apply_MAIL_QU_JIAN;
        this.applyinstId = applyinstId;
        this.expressNum = expressNum;
    }
}
