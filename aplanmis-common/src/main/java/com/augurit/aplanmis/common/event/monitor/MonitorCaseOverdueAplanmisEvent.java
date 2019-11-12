package com.augurit.aplanmis.common.event.monitor;

import com.augurit.aplanmis.common.event.AplanmisEvent;
import com.augurit.aplanmis.common.event.AplanmisEventType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/*
办件逾期事件
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MonitorCaseOverdueAplanmisEvent extends AplanmisEvent {

    /*
    剩余天数
     */
    protected String dueNumLeft;

    /*
    项目名称
     */
    protected String projName;

    /*
    申请编号
     */
    protected String applyinstCode;

    /*
    申请实例id
     */
    protected String applyinstId;

    public MonitorCaseOverdueAplanmisEvent(Object source, String dueNumLeft, String projName, String applyinstCode, String applyinstId) {
        this(source);
        this.dueNumLeft = dueNumLeft;
        this.projName = projName;
        this.applyinstCode = applyinstCode;
        this.applyinstId = applyinstId;
    }

    public MonitorCaseOverdueAplanmisEvent(Object source) {
        super(source);
        eventType = AplanmisEventType.MONITOR_CASE_OVERDUE;
    }
}
