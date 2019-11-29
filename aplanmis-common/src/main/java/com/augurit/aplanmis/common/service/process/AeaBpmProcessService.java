package com.augurit.aplanmis.common.service.process;


import com.augurit.agcloud.bpm.common.engine.form.BpmProcessInstance;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import org.flowable.task.api.Task;

import java.util.List;

public interface AeaBpmProcessService {
    /**
     * 根据流程模板ID和申请实例，启动流程
     *
     * @param appId          流程模板ID
     * @param aeaHiApplyinst 申请实例对象
     * @return
     * @throws Exception
     */
    public BpmProcessInstance startFlow(String appId, String appinstId, AeaHiApplyinst aeaHiApplyinst) throws Exception;

    /**
     * 根据流程当前节点，获取下一个节点信息，并动态设置下一个节点的办理人
     *
     * @param currentTask 当前节点对象
     * @param assignees   办理人列表
     */
    public void fillNextTaskAssignee(Task currentTask, List<String> assignees);

    BpmProcessInstance startFlow(String appId, String appinstId, AeaHiApplyinst aeaHiApplyinst, String userName) throws Exception;
}