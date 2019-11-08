package com.augurit.aplanmis.common.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 审批系统业务事件的父类, 需要实现自定义事件请继承这个
 */
@Getter
public abstract class AplanmisEvent extends ApplicationEvent {
    protected String applyinstId;//申报实例ID
    protected String iteminstId;//事项实例ID
//    protected String procInstId;//流程实例ID
    protected String taskId;//task实例ID

    /**
     * 事件类型
     */
    protected AplanmisEventType eventType;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public AplanmisEvent(Object source) {
        super(source);
    }

}
