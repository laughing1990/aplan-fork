package com.augurit.aplanmis.front.approve.taskCustomClass;

import com.augurit.agcloud.bpm.common.domain.ActStoAppinst;
import com.augurit.agcloud.bpm.common.engine.handler.AssigneeRangeHandler;
import com.augurit.agcloud.bpm.common.service.ActStoAppinstService;
import com.augurit.agcloud.bpm.common.utils.SpringContextHolder;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.opus.common.domain.OpuOmUser;
import com.augurit.aplanmis.common.domain.AeaApplyinstProj;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.mapper.AeaApplyinstProjMapper;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.service.window.AeaServiceWindowUserService;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.HistoryService;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.task.api.history.HistoricTaskInstance;

import java.util.ArrayList;
import java.util.List;

/**
 * 窗口流程办结节点办理人自定义处理类
 *
 * @author：sam
 */
public class WinConfirmTaskCustomHandler implements AssigneeRangeHandler {

    @Override
    public List<String> assign(UserTask userTask, String processIntanceId) throws Exception {
        HistoryService historyService = null;
        if (CommandContextUtil.getProcessEngineConfiguration() != null) {
            historyService = CommandContextUtil.getProcessEngineConfiguration().getHistoryService();
        } else {
            historyService = SpringContextHolder.getBean(HistoryService.class);
        }

        AeaServiceWindowUserService aeaServiceWindowUserService = SpringContextHolder.getBean(AeaServiceWindowUserService.class);
        ActStoAppinstService actStoAppinstService = SpringContextHolder.getBean(ActStoAppinstService.class);
        AeaHiApplyinstService aeaHiApplyinstService = SpringContextHolder.getBean(AeaHiApplyinstService.class);
        AeaApplyinstProjMapper aeaApplyinstProjMapper = SpringContextHolder.getBean(AeaApplyinstProjMapper.class);
        AeaProjInfoService aeaProjInfoService = SpringContextHolder.getBean(AeaProjInfoService.class);

        ActStoAppinst actStoAppinst = actStoAppinstService.getActStoAppinstByProcInstId(processIntanceId);
        if (actStoAppinst == null)
            throw new RuntimeException("获取当前主流程实例失败！");

        String applyinstId = actStoAppinst.getMasterRecordId();
        AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);
        if (aeaHiApplyinst == null)
            throw new RuntimeException("获取当前申报实例失败！");

        String applySource = aeaHiApplyinst.getApplyinstSource();

        List<HistoricTaskInstance> list = new ArrayList<>();

        List<String> assignees = new ArrayList<>();

        if ("win".equals(applySource)) {
            List<HistoricTaskInstance> taskInstanceList = historyService.createHistoricTaskInstanceQuery().processInstanceId(processIntanceId).taskDefinitionKey("shoujian").list();
            if (taskInstanceList != null && taskInstanceList.size() > 0)
                list.addAll(taskInstanceList);

            if (list.size() > 0) {
                for (HistoricTaskInstance taskInstance : list) {
                    //这里要过滤掉多工作项中被删除的任务实例
                    if (StringUtils.isBlank(taskInstance.getDeleteReason()) && StringUtils.isNotBlank(taskInstance.getAssignee()))
                        assignees.add(taskInstance.getAssignee());
                }
            }
        } else {
            List<AeaApplyinstProj> aeaApplyinstProjs = aeaApplyinstProjMapper.getAeaApplyinstProjByApplyinstId(applyinstId);
            AeaApplyinstProj aeaApplyinstProj = aeaApplyinstProjs.get(0);

            AeaProjInfo aeaProjInfo = aeaProjInfoService.getAeaProjInfoByProjInfoId(aeaApplyinstProj.getProjInfoId());

            List<OpuOmUser> users = aeaServiceWindowUserService.findWindowUserByRegionIdAndAllItemUser(aeaProjInfo.getRegionalism(), SecurityContext.getCurrentOrgId());
            users.forEach(a -> {
                assignees.add(a.getLoginName());
            });
        }

        return assignees;

        /*List<HistoricTaskInstance> taskInstanceList = historyService.createHistoricTaskInstanceQuery().processInstanceId(processIntanceId).taskDefinitionKey("shoujian").list();
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

        return null;*/
    }
}
