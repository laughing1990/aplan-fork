package com.augurit.aplanmis.common.event.vo;

import lombok.Data;

// 申报事件vo
@Data
public class ApplyEventVo {

    // 申报实例
    private String applyinstId;

    // 流程模板实例
    private String appinstId;

    // 任务实例
    private String taskinstId;

    // 申报实例状态
    private String applyinstState;

    // 状态更改之前的申报状态
    private String oldApplyinstState;

    // 服务窗口
    private String opuWindowId;

    // 是否抛出报错信息
    private boolean showException = false;

    public ApplyEventVo(String applyinstId, String appinstId, String taskinstId, String applyinstState, String oldApplyinstState, String opuWindowId) {
        this.applyinstId = applyinstId;
        this.appinstId = appinstId;
        this.taskinstId = taskinstId;
        this.applyinstState = applyinstState;
        this.oldApplyinstState = oldApplyinstState;
        this.opuWindowId = opuWindowId;
    }

    public ApplyEventVo(String applyinstId, String appinstId, String taskinstId, String applyinstState, String oldApplyinstState, String opuWindowId,boolean showException) {
        this(applyinstId, appinstId, taskinstId, applyinstState, oldApplyinstState, opuWindowId);
        this.showException = showException;
    }
}
