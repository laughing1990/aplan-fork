package com.augurit.aplanmis.common.service.instance;

import com.augurit.agcloud.bpm.common.domain.*;
import com.augurit.agcloud.bpm.common.mapper.ActStoAppinstMapper;
import com.augurit.agcloud.bpm.common.mapper.ActStoAppinstSubflowMapper;
import com.augurit.agcloud.bpm.common.mapper.ActStoTimegroupActMapper;
import com.augurit.agcloud.bpm.common.mapper.ActTplAppTriggerMapper;
import com.augurit.agcloud.bpm.common.service.ActStoTimeruleInstService;
import com.augurit.agcloud.bpm.common.service.ActStoTimeruleService;
import com.augurit.agcloud.bpm.common.service.ActTplAppFlowdefService;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class RestTimeruleinstServiceImpl implements RestTimeruleinstService {


    @Autowired
    private ActStoTimeruleInstService actStoTimeruleInstService;

    @Autowired
    private ActStoTimeruleService actStoTimeruleService;

    @Autowired
    private ActStoTimegroupActMapper actStoTimegroupActMapper;

    @Autowired
    private ActTplAppFlowdefService actTplAppFlowdefService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ActStoAppinstMapper actStoAppinstMapper;

    @Autowired
    private ActStoAppinstSubflowMapper actStoAppinstSubflowMapper;

    @Autowired
    private ActTplAppTriggerMapper actTplAppTriggerMapper;

    @Override
    public void createTimeruleinstByProcinst(String appId, String procinstId, String procDefKey) throws Exception {

        if (StringUtils.isBlank(procinstId) || StringUtils.isBlank(procDefKey) || StringUtils.isBlank(appId)) return;
        ActTplAppFlowdef actTplAppFlowdef = new ActTplAppFlowdef();
        actTplAppFlowdef.setAppId(appId);
        actTplAppFlowdef.setProcdefKey(procDefKey);
        actTplAppFlowdef.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<ActTplAppFlowdef> appFlowdefList = actTplAppFlowdefService.listActTplAppFlowdef(actTplAppFlowdef);
        if (appFlowdefList.size() < 1) return;

        ActTplAppFlowdef appFlowdef = appFlowdefList.get(0);
        if (StringUtils.isBlank(appFlowdef.getTimeruleId())) return;
        ActStoTimerule actStoTimerule = actStoTimeruleService.getActStoTimeruleById(appFlowdef.getTimeruleId());

        if (actStoTimerule == null) return;
        ActStoTimeruleInst actStoTimeruleInst = new ActStoTimeruleInst();
        actStoTimeruleInst.setProcInstId(procinstId);
        actStoTimeruleInst.setAppFlowdefId(appFlowdef.getAppFlowdefId());
        actStoTimeruleInst.setTimeruleId(actStoTimerule.getTimeruleId());
        actStoTimeruleInst.setOrgId(SecurityContext.getCurrentOrgId());
        List<ActStoTimeruleInst> timeruleInsts = actStoTimeruleInstService.listActStoTimeruleInst(actStoTimeruleInst);
        if (timeruleInsts.size() > 0) return;

        //如果时限为NULL,则默认为1天
        actStoTimeruleInst.setTimeLimit(Long.valueOf(appFlowdef.getTimeLimit() == null ? 1 : appFlowdef.getTimeLimit()));
        actStoTimeruleInst.setTimeruleInstType("1");
        actStoTimeruleInst.setCreateTime(new Date());
        actStoTimeruleInst.setIsConcluding("0");
        actStoTimeruleInst.setTimeruleUnit(actStoTimerule.getTimeruleUnit());
        actStoTimeruleInst.setCreater(SecurityContext.getCurrentUserName());
        actStoTimeruleInst.setTimeruleInstId(UUID.randomUUID().toString());
        actStoTimeruleInst.setUseLimitTime(0d);
        actStoTimeruleInst.setOverdueTime(0d);
        actStoTimeruleInst.setInstState("1");
        actStoTimeruleInst.setRemainingTime(Double.valueOf(appFlowdef.getTimeLimit() == null ? 1 : appFlowdef.getTimeLimit()));
        actStoTimeruleInstService.saveActStoTimeruleInst(actStoTimeruleInst);
    }

    @Override
    public void createTimeruleinstByTaskinst(String taskId) throws Exception {
        if (StringUtils.isBlank(taskId)) return;
        //查询出task实例
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if(task != null){
            String currentOrgId = SecurityContext.getCurrentOrgId();
            String appFlowdefId = null;
            String actId = task.getTaskDefinitionId();
            String processInstanceId = task.getProcessInstanceId();
            //查询是否已经存在实例
            ActStoTimeruleInst query1 = new ActStoTimeruleInst();
            query1.setTaskinstId(taskId);
            query1.setTimeruleInstType("2");
            query1.setOrgId(currentOrgId);
            List<ActStoTimeruleInst> actStoTimeruleInsts = actStoTimeruleInstService.listActStoTimeruleInst(query1);
            //已经创建了节点时限实例则不重复创建
            if(actStoTimeruleInsts.size() > 0) return;

            //查询appFlowdefId 用于关联出节点时限配置
            ActStoAppinst actStoAppinst = actStoAppinstMapper.getActStoAppinstByProcInstId(processInstanceId);
            //查的到则是一级流程节点
            if(actStoAppinst != null){
                appFlowdefId = actStoAppinst.getAppFlowdefId();
            }else{
                //查询子流程
                ActStoAppinstSubflow actStoAppinstSubflow = actStoAppinstSubflowMapper.getActStoAppinstSubflowBySubflowProcinstId(processInstanceId);
                if(actStoAppinstSubflow != null){
                    ActTplAppTrigger trigger = actTplAppTriggerMapper.getAllActTplAppTriggerById(actStoAppinstSubflow.getTriggerId());
                    if(trigger != null){
                        appFlowdefId = trigger.getTriggerAppFlowdefId();
                    }
                }
            }
            if(StringUtils.isNotBlank(appFlowdefId)){
                ActStoTimegroupAct query = new ActStoTimegroupAct();
                query.setAppFlowdefId(appFlowdefId);
                query.setActId(actId);
                List<ActStoTimegroupAct> actStoTimegroupActs = actStoTimegroupActMapper.listActStoTimegroupAct(query);
                if(actStoTimegroupActs.size() > 0){
                    List<ActStoTimerule> actStoTimerules = actStoTimeruleService.listActStoTimerule(new ActStoTimerule());
                    if (actStoTimerules.size() == 0) return;
                    String currentUserName = SecurityContext.getCurrentUserName();
                    for(int i=0,leni = actStoTimegroupActs.size(); i<leni; i++){
                        ActStoTimegroupAct actStoTimegroupAct = actStoTimegroupActs.get(i);
                        String unit = "WD";
                        for(int j=0,lenj=actStoTimerules.size(); j<lenj; j++){
                            ActStoTimerule timerule = actStoTimerules.get(j);
                            if(actStoTimegroupAct.getTimeruleId().equals(timerule.getTimeruleId())){
                                unit = timerule.getTimeruleUnit();
                            }
                        }
                        ActStoTimeruleInst actStoTimeruleInst = new ActStoTimeruleInst();
                        actStoTimeruleInst.setProcInstId(processInstanceId);
                        actStoTimeruleInst.setAppFlowdefId(appFlowdefId);
                        actStoTimeruleInst.setTimeruleId(actStoTimegroupAct.getTimeruleId());
                        actStoTimeruleInst.setOrgId(currentOrgId);
                        //如果时限为NULL,则默认为1天
                        actStoTimeruleInst.setTimeLimit(Long.valueOf(actStoTimegroupAct.getTimeLimit() == null ? 1 : actStoTimegroupAct.getTimeLimit()));
                        if(StringUtils.isNotBlank(actStoTimegroupAct.getTimegroupId())){
                            actStoTimeruleInst.setTimeruleInstType("3");
                            actStoTimeruleInst.setTimegroupId(actStoTimegroupAct.getTimegroupId());
                        }else{
                            actStoTimeruleInst.setTimeruleInstType("2");
                        }
                        actStoTimeruleInst.setTimegroupActId(actStoTimegroupAct.getTimegroupActId());
                        actStoTimeruleInst.setCreateTime(new Date());
                        actStoTimeruleInst.setIsConcluding("0");
                        actStoTimeruleInst.setTimeruleUnit(unit);
                        actStoTimeruleInst.setCreater(currentUserName);
                        actStoTimeruleInst.setTimeruleInstId(UUID.randomUUID().toString());
                        actStoTimeruleInst.setUseLimitTime(0d);
                        actStoTimeruleInst.setOverdueTime(0d);
                        actStoTimeruleInst.setInstState("1");
                        actStoTimeruleInst.setRemainingTime(Double.valueOf(actStoTimegroupAct.getTimeLimit() == null ? 1 : actStoTimegroupAct.getTimeLimit()));
                        actStoTimeruleInstService.saveActStoTimeruleInst(actStoTimeruleInst);
                    }
                }
            }
        }
    }
}
