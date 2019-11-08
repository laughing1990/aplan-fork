package com.augurit.aplanmis.common.domain;

import lombok.Data;

/**
 * @author yinlf
 * @Date 2019/8/13
 */
@Data
public class TaskCountForm {
    /**
     * 运行流程节点实例ID
     */
    private String taskId;
    /**
     * 办理期限
     */
    private String dueNum;
    /**
     * 办理用时
     */
    private String useWorkDay;
    /**
     * 节点定义
     */
    private String taskDefKey;

    private Double suspensionState;
}
