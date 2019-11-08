package com.augurit.aplanmis.front.approve.taskCustomClass;

import com.augurit.agcloud.bpm.common.engine.handler.AssigneeRangeHandler;
import com.augurit.agcloud.bpm.common.utils.SpringContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.HistoryService;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.task.api.history.HistoricTaskInstance;

import java.util.ArrayList;
import java.util.List;

/**
 * 窗口流程办结节点办理人自定义处理类
 * @author：sam
 */
public class WinBanjieTaskCustomHandler implements AssigneeRangeHandler {

    @Override
    public List<String> assign(UserTask userTask, String processIntanceId) throws Exception {
        HistoryService historyService = null;
        if(CommandContextUtil.getProcessEngineConfiguration()!=null){
            historyService = CommandContextUtil.getProcessEngineConfiguration().getHistoryService();
        }else{
            historyService = SpringContextHolder.getBean(HistoryService.class);
        }

        List<HistoricTaskInstance> list = new ArrayList<>();

        List<HistoricTaskInstance> taskInstanceList = historyService.createHistoricTaskInstanceQuery().processInstanceId(processIntanceId).taskDefinitionKey("shoujian").list();
        if(taskInstanceList!=null&&taskInstanceList.size()>0)
            list.addAll(taskInstanceList);

        taskInstanceList = historyService.createHistoricTaskInstanceQuery().processInstanceId(processIntanceId).taskDefinitionKey("wangshangshouli").list();
        if(taskInstanceList!=null&&taskInstanceList.size()>0)
            list.addAll(taskInstanceList);

        if(list.size()>0){
            List<String> assignees = new ArrayList<>();
            for(HistoricTaskInstance taskInstance:list){
                //这里要过滤掉多工作项中被删除的任务实例
                if (StringUtils.isBlank(taskInstance.getDeleteReason()) && StringUtils.isNotBlank(taskInstance.getAssignee()))
                    assignees.add(taskInstance.getAssignee());
            }

            return assignees;
        }

        return null;
    }
}
