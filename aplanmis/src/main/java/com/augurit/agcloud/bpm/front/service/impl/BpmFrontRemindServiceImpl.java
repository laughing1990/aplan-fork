package com.augurit.agcloud.bpm.front.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.augurit.agcloud.aoa.domain.AoaMsgRange;
import com.augurit.agcloud.aoa.sc.msg.service.AoaMsgRangeService;
import com.augurit.agcloud.bpm.common.domain.*;
import com.augurit.agcloud.bpm.common.service.*;
import com.augurit.agcloud.bpm.front.domain.ActStoRemindForm;
import com.augurit.agcloud.bpm.front.domain.BpmTaskAndUser;
import com.augurit.agcloud.bpm.front.service.BpmFrontRemindService;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.opus.common.domain.OpuOmUser;
import com.augurit.agcloud.opus.common.service.om.OpuOmUserService;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.event.AplanmisEventPublisher;
import com.augurit.aplanmis.common.event.def.BpmRemindSmsAplanmisEvent;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;
import org.apache.commons.lang.StringUtils;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class BpmFrontRemindServiceImpl implements BpmFrontRemindService {
    @Autowired
    private TaskService taskService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private OpuOmUserService opuOmUserService;
    @Autowired
    private ActStoRemindService actStoRemindService;
    @Autowired
    private ActStoRemindReceiverService actStoRemindReceiverService;
    @Autowired
    private ActStoAppinstService actStoAppinstService;
    @Autowired
    private ActStoAppinstSubflowService actStoAppinstSubflowService;
    @Autowired
    private ActTplAppService actTplAppService;
    @Autowired
    private AoaMsgRangeService aoaMsgRangeService;
    @Autowired
    private AplanmisEventPublisher aplanmisEventPublisher;
    @Autowired
    private AeaHiApplyinstService aeaHiApplyinstService;

    @Override
    public List<BpmTaskAndUser> getTaskUsersByProcessInstanceId(String processInstanceId) throws Exception {
        List<Task> taskList = new ArrayList<>();

        //获取主流程实例
        ActStoAppinst appinst = actStoAppinstService.getActStoAppinstByProcInstId(processInstanceId);
        if(appinst!=null){
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
            if(tasks!=null&&tasks.size()>0)
                taskList.addAll(tasks);

            ActStoAppinstSubflow subflowCondition = new ActStoAppinstSubflow();
            subflowCondition.setAppinstId(appinst.getAppinstId());
            List<ActStoAppinstSubflow> subflows = actStoAppinstSubflowService.listActStoAppinstSubflow(subflowCondition);

            //获取所有子流程的在办任务节点
            if(subflows!=null&&subflows.size()>0){
                for(ActStoAppinstSubflow subflow:subflows){
                    List<Task> subTasks = taskService.createTaskQuery().processInstanceId(subflow.getSubflowProcinstId()).list();
                    if(subTasks!=null&&subTasks.size()>0)
                        taskList.addAll(subTasks);
                }
            }
        }else{//获取子流程实例
            ActStoAppinstSubflow subflow = actStoAppinstSubflowService.getActStoAppinstSubflowBySubflowProcinstId(processInstanceId);
            if(subflow!=null){
                String appinstId = subflow.getAppinstId();

                //获取父节点的在办任务节点
                ActStoAppinst stoAppinst = actStoAppinstService.getActStoAppinstById(appinstId);
                if(stoAppinst!=null) {
                    String parentProcInstId = stoAppinst.getProcinstId();

                    List<Task> parentTasks = taskService.createTaskQuery().processInstanceId(parentProcInstId).list();
                    if(parentTasks!=null&&parentTasks.size()>0)
                        taskList.addAll(parentTasks);
                }

                //获取当前流程的其他子流程在办节点(包括自己本身)
                ActStoAppinstSubflow subflowCondition = new ActStoAppinstSubflow();
                subflowCondition.setAppinstId(appinstId);
                List<ActStoAppinstSubflow> subflows = actStoAppinstSubflowService.listActStoAppinstSubflow(subflowCondition);

                if(subflows!=null&&subflows.size()>0){
                    for(ActStoAppinstSubflow appinstSubflow:subflows){
                        List<Task> subTasks = taskService.createTaskQuery().processInstanceId(appinstSubflow.getSubflowProcinstId()).list();
                        if(subTasks!=null&&subTasks.size()>0)
                            taskList.addAll(subTasks);
                    }
                }
            }
        }

        if(taskList==null||taskList.size()==0)
            return null;

        List<BpmTaskAndUser> users = new ArrayList<>();

        for(Task task:taskList){
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult();
            String procName = "";
            if(processDefinition!=null)
                procName = processDefinition.getName();

            String loginName = task.getAssignee();
            if(loginName!=null&&!"".equals(loginName.trim())) {
                OpuOmUser opuOmUser = opuOmUserService.getUserByLoginName(loginName);
                if (opuOmUser != null) {
                    BpmTaskAndUser taskAndUser = new BpmTaskAndUser();
                    taskAndUser.setTaskId(task.getId());
                    taskAndUser.setTaskName(task.getName());
                    taskAndUser.setUserId(opuOmUser.getUserId());
                    taskAndUser.setLoginName(opuOmUser.getLoginName());
                    taskAndUser.setUserName(opuOmUser.getUserName());
                    taskAndUser.setProcName(procName);
                    taskAndUser.setProcInstId(task.getProcessInstanceId());
                    users.add(taskAndUser);
                }
            }
        }
        return users;
    }

    @Override
    public boolean saveProcessRemind(ActStoRemind actStoRemind,List<ActStoRemindReceiver> receivers) throws Exception {
        if(actStoRemind==null||receivers==null){
            throw new RuntimeException("参数不能为空！");
        }

        String remindId = UUID.randomUUID().toString();
        actStoRemind.setRemindId(remindId);
        actStoRemind.setSendDate(new Date());
        actStoRemind.setSendUserId(SecurityContext.getCurrentUserId());
        actStoRemindService.createActStoRemind(actStoRemind);

        for(ActStoRemindReceiver receiver:receivers){
            if(receiver.getRemindReceiverType()==null||receiver.getReceiverUserId()==null)
                throw new RuntimeException("参数receivers有误，remindReceiverType或receiverUserId为空，请检查！");

            receiver.setRemindId(remindId);
            receiver.setIsRead("0");

            actStoRemindReceiverService.createActStoRemindReceiver(receiver);
        }

        return true;
    }

    @Override
    public boolean saveProcessRemindAndAoaMsg(ActStoRemind actStoRemind, List<ActStoRemindReceiver> receivers,String projName,String applyinstCode) throws Exception {
        if(actStoRemind==null||receivers==null){
            throw new RuntimeException("参数不能为空！");
        }

        String remindId = UUID.randomUUID().toString();
        actStoRemind.setRemindId(remindId);
        actStoRemind.setSendDate(new Date());
        actStoRemind.setSendUserId(SecurityContext.getCurrentUserId());
        actStoRemindService.createActStoRemind(actStoRemind);

        for(ActStoRemindReceiver receiver:receivers){
            if(receiver.getRemindReceiverType()==null||receiver.getReceiverUserId()==null)
                throw new RuntimeException("参数receivers有误，remindReceiverType或receiverUserId为空，请检查！");

            receiver.setRemindId(remindId);
            receiver.setIsRead("0");

            actStoRemindReceiverService.createActStoRemindReceiver(receiver);

            String title = null;
            String projNameStr = null;
            if(projName!=null){
                projNameStr = projName;
            }
            String content = actStoRemind.getRemindContent();;
            if("s".equals(receiver.getRemindReceiverType())){
                title = "关于【"+projNameStr+"】项目审批待办催办信息!";
            }else{
                title = "抄送：关于【"+projNameStr+"】项目审批待办催办信息!";
            }

            //当为应用提醒时才创建发送消息
            if("m".equals(actStoRemind.getRemindType())) {
                if(StringUtils.isNotBlank(applyinstCode)){
                    AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstByCode(applyinstCode);

                    if(aeaHiApplyinst==null)
                        throw new RuntimeException("获取申报实例为空，请检查申报流水号是否正确，当前申报流水号为："+applyinstCode);

                    //触发发送催办短信事件
                    aplanmisEventPublisher.publishEvent(new BpmRemindSmsAplanmisEvent(this,aeaHiApplyinst.getApplyinstId(),receiver.getReceiverUserId()));
                }else{
                    throw new InvalidParameterException("参数applyinstCode不能为空！");
                }
            }

            if("n".equals(actStoRemind.getRemindType())){
                AoaMsgRange aoaMsgRange = new AoaMsgRange();
                aoaMsgRange.setContentTitle(title);
                aoaMsgRange.setContentText(content);
                aoaMsgRange.setIsUserSend("1");
                aoaMsgRange.setUserId(receiver.getReceiverUserId());
                aoaMsgRange.setSendUserId(SecurityContext.getCurrentUserId());
                aoaMsgRangeService.saveMsgContentAndMsgRange(aoaMsgRange);
            }
        }

        return true;
    }

    @Override
    public boolean saveProcessRemindAndAoaMsgs(List<ActStoRemindForm> actStoRemindForms, String projName,String applyinstCode) throws Exception {
        for(ActStoRemindForm actStoRemindForm:actStoRemindForms) {
            if(actStoRemindForm.getUserIdJson()==null)
                break;

            List<ActStoRemindReceiver> receivers = JSONArray.parseArray(actStoRemindForm.getUserIdJson(), ActStoRemindReceiver.class);

            this.saveProcessRemindAndAoaMsg(actStoRemindForm,receivers,projName,applyinstCode);
        }

        return true;
    }

    @Override
    public String getTplAppNameByProcessInstanceId(String procInstId) throws Exception {
        if(StringUtils.isBlank(procInstId))
            throw new InvalidParameterException("参数procInstId为空！");

        String appId = "";

        ActStoAppinst appinst = actStoAppinstService.getActStoAppinstByProcInstId(procInstId);
        if(appinst!=null){
            appId = appinst.getAppId();
        }else{
            ActStoAppinstSubflow subflow = actStoAppinstSubflowService.getActStoAppinstSubflowBySubflowProcinstId(procInstId);
            if(subflow!=null){
                String appinstId = subflow.getAppinstId();
                ActStoAppinst stoAppinst = actStoAppinstService.getActStoAppinstById(appinstId);
                if(stoAppinst!=null) {
                    appId = stoAppinst.getAppId();
                }
            }
        }

        if(StringUtils.isNotBlank(appId)){
            ActTplApp actTplApp = actTplAppService.getActTplAppById(appId);

            return actTplApp.getAppComment();
        }

        return null;
    }
}
