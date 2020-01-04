package com.augurit.aplanmis.front.approve.taskCustomClass;

import com.augurit.agcloud.bpm.common.domain.ActStoAppinst;
import com.augurit.agcloud.bpm.common.domain.ActStoAppinstSubflow;
import com.augurit.agcloud.bpm.common.engine.handler.AssigneeRangeHandler;
import com.augurit.agcloud.bpm.common.service.ActStoAppinstService;
import com.augurit.agcloud.bpm.common.service.ActStoAppinstSubflowService;
import com.augurit.agcloud.bpm.common.utils.SpringContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.task.api.history.HistoricTaskInstance;

import java.util.ArrayList;
import java.util.List;

/**
 * 竣工验收阶段，待申请人复验节点审批人自定义处理类
 * @author：sam
 */
public class ProjCheckReviewTaskHandler implements AssigneeRangeHandler {

    @Override
    public List<String> assign(UserTask userTask, String processIntanceId) throws Exception {
        ActStoAppinstService actStoAppinstService = SpringContextHolder.getBean(ActStoAppinstService.class);
        ActStoAppinstSubflowService actStoAppinstSubflowService = SpringContextHolder.getBean(ActStoAppinstSubflowService.class);
        RuntimeService runtimeService = SpringContextHolder.getBean(RuntimeService.class);

        String appinstId = null;

        ActStoAppinst actStoAppinst = actStoAppinstService.getActStoAppinstByProcInstId(processIntanceId);
        if(actStoAppinst==null){
            ActStoAppinstSubflow actStoAppinstSubflow = actStoAppinstSubflowService.getActStoAppinstSubflowBySubflowProcinstId(processIntanceId);
            if(actStoAppinstSubflow!=null){
                appinstId = actStoAppinstSubflow.getAppinstId();
                actStoAppinst = actStoAppinstService.getActStoAppinstById(appinstId);
            }
        }else{
            appinstId = actStoAppinst.getAppinstId();
        }

        if(actStoAppinst!=null){
            String masterProcInstId = actStoAppinst.getProcinstId();

            String assignee = (String)runtimeService.getVariable(masterProcInstId,"$INITIATOR");

            List<String> assignees = new ArrayList<>();
            assignees.add(assignee);

            return assignees;
        }

        return null;
    }
}
