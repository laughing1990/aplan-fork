package com.augurit.aplanmis.common.listener;

import com.augurit.agcloud.bpm.common.domain.*;
import com.augurit.agcloud.bpm.common.mapper.ActStoAppinstSubflowMapper;
import com.augurit.agcloud.bpm.common.mapper.ActStoTimeruleInstMapper;
import com.augurit.agcloud.bpm.common.mapper.ActTplAppFlowdefMapper;
import com.augurit.agcloud.bpm.common.mapper.ActTplAppTriggerMapper;
import com.augurit.agcloud.bpm.common.service.ActStoAppinstService;
import com.augurit.agcloud.bsc.domain.BscJobRemind;
import com.augurit.agcloud.bsc.sc.job.service.BscJobRemindService;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.opus.common.domain.OpuOmUserInfo;
import com.augurit.agcloud.opus.common.service.om.OpuOmUserInfoService;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.event.def.*;
import com.augurit.aplanmis.common.exception.SmsException;
import com.augurit.aplanmis.common.listener.builder.BscJobRemindBuilder;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;
import com.augurit.aplanmis.common.shortMessage.AplanmisSmsConfigProperties;
import com.augurit.aplanmis.common.shortMessage.converter.SendSmsRemindContentConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.Process;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 发送短信监听器
 * 异步处理
 * author:sam
 */
@Component
@Slf4j
public class SmsAplanmisListener {
    @Autowired
    private BscJobRemindService bscJobRemindService;
    @Autowired
    private BscJobRemindBuilder bscJobRemindBuilder;
    @Autowired
    private SendSmsRemindContentConverter sendSmsTemplateJsonConverter;
    @Autowired
    private AeaHiApplyinstService aeaHiApplyinstService;
    @Autowired
    private AeaApplyinstProjMapper aeaApplyinstProjMapper;
    @Autowired
    private ActStoAppinstService actStoAppinstService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private OpuOmUserInfoService opuOmUserInfoService;
    @Autowired
    private AeaLinkmanInfoMapper aeaLinkmanInfoMapper;
    @Autowired
    private ActStoAppinstSubflowMapper actStoAppinstSubflowMapper;
    @Autowired
    private ActTplAppTriggerMapper actTplAppTriggerMapper;
    @Autowired
    private ActTplAppFlowdefMapper actTplAppFlowdefMapper;
    @Autowired
    private AeaHiSmsInfoMapper aeaHiSmsInfoMapper;
    @Autowired
    private AeaHiIteminstMapper aeaHiIteminstMapper;
    @Autowired
    private AeaHiSeriesinstMapper aeaHiSeriesinstMapper;
    @Autowired
    private AeaHiParStageinstMapper aeaHiParStageinstMapper;
    @Autowired
    private ActStoTimeruleInstMapper actStoTimeruleInstMapper;
    @Autowired
    private AplanmisSmsConfigProperties smsConfigProperties;
    @Autowired
    private RepositoryService repositoryService;

    //Todo 申报成功短信发送：发送业主申报成功通知（网厅使用）
    @EventListener
    public void applyStartAplanmisEvent(ApplyStartAplanmisEvent applyStartAplanmisEvent){
        log.info("申报成功短信发送：发送业主申报成功通知（网厅使用）");

        if(!smsConfigProperties.isEnable())
            return;

        String applyinstId = applyStartAplanmisEvent.getApplyinstId();

        if(StringUtils.isBlank(applyinstId))
            return ;

        try {
            AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);
            if(aeaHiApplyinst==null)
                throw new RuntimeException("申报实例获取为空！");

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String applyDate = simpleDateFormat.format(aeaHiApplyinst.getAcceptTime());
            String applyinstCode = aeaHiApplyinst.getApplyinstCode();
            String linkmanInfoId = aeaHiApplyinst.getLinkmanInfoId();

            List<AeaApplyinstProj> applyinstProjList = aeaApplyinstProjMapper.getAeaApplyinstProjCascadeProjByApplyinstId(applyinstId);
            if(applyinstProjList==null||applyinstProjList.size()==0)
                throw new RuntimeException("无法获取到申报项目信息！");

            String projName = applyinstProjList.get(0).getProjName();

            String phoneNum = "";
            if(StringUtils.isNotBlank(linkmanInfoId)) {
                //获取联系人电话
                AeaLinkmanInfo aeaLinkmanInfo = aeaLinkmanInfoMapper.getAeaLinkmanInfoById(linkmanInfoId);

                if(aeaLinkmanInfo!=null)
                    phoneNum = aeaLinkmanInfo.getLinkmanMobilePhone();
            }

            //目前仅网厅需要发送申报成功提醒
            if("net".equals(aeaHiApplyinst.getApplyinstSource())){
                //网厅受理提醒（提醒业主）
                String remindContent = sendSmsTemplateJsonConverter.getNetApplyJobRemindContent(applyDate,projName,applyinstCode,phoneNum);
                if (remindContent != null)
                    this.createSendSmsJobRemind(phoneNum,remindContent,applyinstId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SmsException(e.getMessage());

        }
    }

    //Todo 申报受理短信发送：发送业主受理通知
    @EventListener
    public void applyAcceptAplanmisEvent(ApplyAcceptAplanmisEvent applyAcceptAplanmisEvent){
        log.info("申报受理短信发送：发送业主受理通知及事项审批通知监听");

        if(!smsConfigProperties.isEnable())
            return;

        String applyinstId = applyAcceptAplanmisEvent.getApplyinstId();

        if(StringUtils.isBlank(applyinstId))
            return ;

        try {
            AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);
            if(aeaHiApplyinst==null)
                throw new RuntimeException("申报实例获取为空！");

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String applyDate = simpleDateFormat.format(aeaHiApplyinst.getAcceptTime());
            String applyinstCode = aeaHiApplyinst.getApplyinstCode();
            String linkmanInfoId = aeaHiApplyinst.getLinkmanInfoId();

            List<AeaApplyinstProj> applyinstProjList = aeaApplyinstProjMapper.getAeaApplyinstProjCascadeProjByApplyinstId(applyinstId);
            if(applyinstProjList==null||applyinstProjList.size()==0)
                throw new RuntimeException("无法获取到申报项目信息！");

            String projName = applyinstProjList.get(0).getProjName();

            ActStoAppinst appinst = new ActStoAppinst();
            appinst.setMasterRecordId(applyinstId);
            List<ActStoAppinst> appinstList = actStoAppinstService.listActStoAppinst(appinst);
            if(appinstList==null||appinstList.size()==0)
                throw new RuntimeException("无法获取申报主流程信息！");

            ActStoAppinst masterAppinst = appinstList.get(0);
            String procinstId = masterAppinst.getProcinstId();

            String taskName = "";

            List<Task> list = taskService.createTaskQuery().processInstanceId(procinstId).list();
            if(list != null && list.size() > 0){
                taskName = list.get(0).getName();
            }

            String phoneNum = "";
            if(StringUtils.isNotBlank(linkmanInfoId)) {
                //获取联系人电话
                AeaLinkmanInfo aeaLinkmanInfo = aeaLinkmanInfoMapper.getAeaLinkmanInfoById(linkmanInfoId);

                if(aeaLinkmanInfo!=null)
                    phoneNum = aeaLinkmanInfo.getLinkmanMobilePhone();
            }

            //窗口受理提醒（提醒业主）
            String winAcceptRemindContent = sendSmsTemplateJsonConverter.getWinAcceptJobRemindContent(applyDate,projName,applyinstCode,taskName,phoneNum);
            if (winAcceptRemindContent != null)
                this.createSendSmsJobRemind(phoneNum,winAcceptRemindContent,applyinstId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SmsException(e.getMessage());
        }
    }

    //Todo 申报受理短信提醒：提醒审批人员
    @EventListener
    public void applyAcceptBacklogAplanmisEvent(ApplyAcceptAplanmisEvent applyAcceptAplanmisEvent){
        log.info("申报受理短信提醒：提醒审批人员");

        if(!smsConfigProperties.isEnable())
            return;

        String applyinstId = applyAcceptAplanmisEvent.getApplyinstId();
        if(StringUtils.isBlank(applyinstId))
            return ;

        try {
            AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);
            if (aeaHiApplyinst == null)
                throw new RuntimeException("申报实例获取为空！");

            String applyinstCode = aeaHiApplyinst.getApplyinstCode();

            List<AeaApplyinstProj> applyinstProjList = aeaApplyinstProjMapper.getAeaApplyinstProjCascadeProjByApplyinstId(applyinstId);
            if(applyinstProjList==null||applyinstProjList.size()==0)
                throw new RuntimeException("无法获取到申报项目信息！");

            String projName = applyinstProjList.get(0).getProjName();

            ActStoAppinst appinst = new ActStoAppinst();
            appinst.setMasterRecordId(applyinstId);
            List<ActStoAppinst> appinstList = actStoAppinstService.listActStoAppinst(appinst);
            if(appinstList==null||appinstList.size()==0)
                throw new RuntimeException("无法获取申报主流程信息！");

            String procinstId = appinstList.get(0).getProcinstId();

            List<Task> tasks = this.getAllRuTasksByMasterProcinstId(procinstId);

            if(tasks!=null&&tasks.size()>0){
                for(Task task:tasks){
                    if(StringUtils.isNotBlank(task.getAssignee())){//受理人不为空时才发送短信
                        String phoneNum = "";
                        OpuOmUserInfo user = opuOmUserInfoService.getOpuOmUserInfoByloginName(task.getAssignee());
                        if(user!=null)
                            phoneNum = user.getUserMobile();

                        //剩余天数
                        String dueNum = this.getDueNum(task.getProcessInstanceId());

                        String backlogJobRemindContent = sendSmsTemplateJsonConverter.getBacklogJobRemindContent(applyinstCode,projName,task.getName(),dueNum,phoneNum);
                        if (backlogJobRemindContent != null)
                            this.createSendSmsJobRemind(phoneNum,backlogJobRemindContent,applyinstId);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new SmsException(e.getMessage());
        }
    }

    //Todo 申报办结短信提醒（仅限窗口领件短信提醒，快递取件的放在统一取件录入快递号之后）
    @EventListener
    public void applyCompleteAplanmisEvent(ApplyCompletedAplanmisEvent applyCompletedAplanmisEvent){
        log.info("申报办结短信提醒（仅限窗口领件短信提醒，快递取件的放在统一取件录入快递号之后）");

        if(!smsConfigProperties.isEnable())
            return;

        String applyinstId = applyCompletedAplanmisEvent.getApplyinstId();
        if(StringUtils.isBlank(applyinstId))
            return ;

        try {
            AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);
            if (aeaHiApplyinst == null)
                throw new RuntimeException("申报实例获取为空！");

            String applyinstCode = aeaHiApplyinst.getApplyinstCode();
            String linkmanInfoId = aeaHiApplyinst.getLinkmanInfoId();

            List<AeaApplyinstProj> applyinstProjList = aeaApplyinstProjMapper.getAeaApplyinstProjCascadeProjByApplyinstId(applyinstId);
            if(applyinstProjList==null||applyinstProjList.size()==0)
                throw new RuntimeException("无法获取到申报项目信息！");

            String projName = applyinstProjList.get(0).getProjName();

            AeaHiSmsInfo aeaHiSmsInfo = aeaHiSmsInfoMapper.getAeaHiSmsInfoByApplyinstId(applyinstId);
            if(aeaHiSmsInfo!=null){
                String receiveMode = aeaHiSmsInfo.getReceiveMode();//领取模式：1 窗口取证  0 邮政快递

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String overDate = simpleDateFormat.format(new Date());

                String phoneNum = "";
                if(StringUtils.isNotBlank(linkmanInfoId)) {
                    //获取联系人电话
                    AeaLinkmanInfo aeaLinkmanInfo = aeaLinkmanInfoMapper.getAeaLinkmanInfoById(linkmanInfoId);

                    if(aeaLinkmanInfo!=null)
                        phoneNum = aeaLinkmanInfo.getLinkmanMobilePhone();
                }

                if("1".equals(receiveMode)){//窗口取证
                    String remindContent = sendSmsTemplateJsonConverter.getNodeEndWinJobRemindContent(overDate,projName,applyinstCode,phoneNum);
                    if (remindContent != null)
                        this.createSendSmsJobRemind(phoneNum,remindContent,applyinstId);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new SmsException(e.getMessage());
        }
    }

    //Todo 材料补正开始短信通知，通知业主补正材料
    @EventListener
    public void applyCorrectMaterialStartAplanmisEvent(ApplyCorrectMaterialStartAplanmisEvent applyCorrectMaterialStartAplanmisEvent){
        log.info("材料补正开始短信通知，通知业主补正材料");

        if(!smsConfigProperties.isEnable())
            return;

        String iteminstId = applyCorrectMaterialStartAplanmisEvent.getIteminstId();
        if(StringUtils.isBlank(iteminstId))
            return ;

        try {
            AeaHiIteminst aeaHiIteminst = aeaHiIteminstMapper.getAeaHiIteminstById(iteminstId);

            String applyinstId = "";
            if(aeaHiIteminst!=null){
                if(aeaHiIteminst.getSeriesinstId()!=null){
                    AeaHiSeriesinst aeaHiSeriesinst = aeaHiSeriesinstMapper.getAeaHiSeriesinstById(aeaHiIteminst.getSeriesinstId());
                    if(aeaHiSeriesinst!=null)
                        applyinstId = aeaHiSeriesinst.getApplyinstId();
                }else{
                    AeaHiParStageinst aeaHiParStageinst = aeaHiParStageinstMapper.getAeaHiParStageinstById(aeaHiIteminst.getStageinstId());
                    if(aeaHiParStageinst!=null)
                        applyinstId = aeaHiParStageinst.getApplyinstId();
                }

                if(StringUtils.isBlank(applyinstId))
                    return ;

                AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);
                if(aeaHiApplyinst==null)
                    throw new RuntimeException("申报实例获取为空！");

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String applyDate = simpleDateFormat.format(aeaHiApplyinst.getAcceptTime());
                String applyinstCode = aeaHiApplyinst.getApplyinstCode();
                String linkmanInfoId = aeaHiApplyinst.getLinkmanInfoId();

                List<AeaApplyinstProj> applyinstProjList = aeaApplyinstProjMapper.getAeaApplyinstProjCascadeProjByApplyinstId(applyinstId);
                if(applyinstProjList==null||applyinstProjList.size()==0)
                    throw new RuntimeException("无法获取到申报项目信息！");

                String projName = applyinstProjList.get(0).getProjName();

                String phoneNum = "";
                if(StringUtils.isNotBlank(linkmanInfoId)) {
                    //获取联系人电话
                    AeaLinkmanInfo aeaLinkmanInfo = aeaLinkmanInfoMapper.getAeaLinkmanInfoById(linkmanInfoId);

                    if(aeaLinkmanInfo!=null)
                        phoneNum = aeaLinkmanInfo.getLinkmanMobilePhone();
                }

                String remindContent = sendSmsTemplateJsonConverter.getBuzhengJobRemindContent(applyDate,projName,applyinstCode,phoneNum);
                if (remindContent != null)
                    this.createSendSmsJobRemind(phoneNum,remindContent,applyinstId);
            }

        }catch (Exception e){
            e.printStackTrace();
            throw new SmsException(e.getMessage());
        }
    }

    //Todo 流程发送时，通知下一个流程节点人员审批待办短信通知（人工发送时触发）
    @EventListener
    public void bpmTaskApproveAplanmisEvent(BpmNodeSendAplanmisEvent bpmNodeSendEvent) {
        log.info("流程发送时，通知下一个流程节点人员审批待办短信通知");

        if(!smsConfigProperties.isEnable())
            return;

        BpmTaskSendObject sendObject = bpmNodeSendEvent.getTaskSendObject();
        if(sendObject==null)
            return ;
        try {
            String taskId = sendObject.getTaskId();
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            String procInstId = task.getProcessInstanceId();

            List<BpmTaskSendConfig> sendConfigs = sendObject.getSendConfigs();
            if(sendConfigs!=null&&sendConfigs.size()>0){
                ActStoAppinst actStoAppinst = actStoAppinstService.getActStoAppinstByProcInstId(procInstId);
                if(actStoAppinst==null){
                    ActStoAppinstSubflow actStoAppinstSubflow = actStoAppinstSubflowMapper.getActStoAppinstSubflowBySubflowProcinstId(procInstId);
                    if(actStoAppinstSubflow!=null){
                        actStoAppinst = actStoAppinstService.getActStoAppinstById(actStoAppinstSubflow.getAppinstId());
                    }
                }

                String applyinstId = actStoAppinst.getMasterRecordId();

                AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);
                if(aeaHiApplyinst==null)
                    throw new RuntimeException("申报实例获取为空！");

                String applyinstCode = aeaHiApplyinst.getApplyinstCode();

                List<AeaApplyinstProj> applyinstProjList = aeaApplyinstProjMapper.getAeaApplyinstProjCascadeProjByApplyinstId(applyinstId);
                if(applyinstProjList==null||applyinstProjList.size()==0)
                    throw new RuntimeException("无法获取到申报项目信息！");

                String projName = applyinstProjList.get(0).getProjName();

                for(BpmTaskSendConfig sendConfig:sendConfigs){
                    String assignees = sendConfig.getAssignees();

                    String destActId = sendConfig.getDestActId();
                    BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
                    Process process = (Process)bpmnModel.getProcesses().get(0);
                    FlowElement flowElement = process.getFlowElement(destActId);

                    List<String> assigneeList = new ArrayList<>();
                    String taskName = "";
                    if(sendConfig.isUserTask()&&StringUtils.isNotBlank(assignees)){
                        UserTask currTaskElement = (UserTask)flowElement;
                        taskName = currTaskElement.getName();

                        if(assignees.indexOf(",")!=-1){
                            assigneeList.addAll(Arrays.asList(assignees.split(",")));
                        }else{
                            assigneeList.add(assignees);
                        }
                    }else if(!sendConfig.isUserTask()){//适配下一个节点为非人工节点时，判断是否为路由节点，如果是，向下找一级(目前只查找了下一级为用户节点并且配置为具体的受理人或者受理人范围时生效)
                        if(flowElement instanceof Gateway){
                            Gateway gateway = (Gateway)flowElement;
                            List<SequenceFlow> outSeqs = gateway.getOutgoingFlows();
                            if(outSeqs!=null&&outSeqs.size()>0){
                                for(SequenceFlow flow:outSeqs){
                                    FlowElement targetFlowElement = flow.getTargetFlowElement();
                                    if(targetFlowElement instanceof UserTask){
                                        taskName = ((UserTask)targetFlowElement).getName();

                                        String assigneeStr = ((UserTask)targetFlowElement).getAssignee();
                                        if(StringUtils.isBlank(assigneeStr)){
                                            assigneeStr = ((UserTask)targetFlowElement).getAssigneeRange();
                                        }

                                        if(StringUtils.isNotBlank(assigneeStr)){
                                            if(assigneeStr.indexOf(",")>-1){
                                                if(assigneeStr.indexOf(",")!=-1){
                                                    assigneeList.addAll(Arrays.asList(assigneeStr.split(",")));
                                                }else{
                                                    assigneeList.add(assigneeStr);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if(assigneeList.size()>0){
                        for(String assignee:assigneeList){
                            if (StringUtils.isNotBlank(assignee)) {//有受理人才发送短信通知
                                String phoneNum = "";
                                OpuOmUserInfo user = opuOmUserInfoService.getOpuOmUserInfoByloginName(assignee);
                                if(user!=null)
                                    phoneNum = user.getUserMobile();

                                //剩余天数
                                String dueNum = this.getDueNum(task.getProcessInstanceId());

                                String remindContent = sendSmsTemplateJsonConverter.getBacklogJobRemindContent(applyinstCode, projName,taskName, dueNum, phoneNum);
                                if (remindContent != null)
                                    this.createSendSmsJobRemind(phoneNum, remindContent, applyinstId);
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new SmsException(e.getMessage());
        }
    }

    //Todo 审批超时短信提醒
    @EventListener
    public void approveOverTimeAplanmisEvent(ApproveOverTimeAplanmisEvent approveOverTimeAplanmisEvent){
        log.info("审批超时短信提醒");

        if(!smsConfigProperties.isEnable())
            return;

        String taskId = approveOverTimeAplanmisEvent.getTaskId();
        if(StringUtils.isBlank(taskId))
            return ;

        try {
            Task currTask = taskService.createTaskQuery().taskId(taskId).singleResult();
            if(currTask==null)
                return;

            String procInstId = currTask.getProcessInstanceId();

            ActStoAppinst actStoAppinst = actStoAppinstService.getActStoAppinstByProcInstId(procInstId);
            if(actStoAppinst==null){
                ActStoAppinstSubflow actStoAppinstSubflow = actStoAppinstSubflowMapper.getActStoAppinstSubflowBySubflowProcinstId(procInstId);
                if(actStoAppinstSubflow!=null){
                    actStoAppinst = actStoAppinstService.getActStoAppinstById(actStoAppinstSubflow.getAppinstId());
                }
            }

            String applyinstId = actStoAppinst.getMasterRecordId();

            AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);
            if (aeaHiApplyinst == null)
                throw new RuntimeException("申报实例获取为空！");

            String applyinstCode = aeaHiApplyinst.getApplyinstCode();

            List<AeaApplyinstProj> applyinstProjList = aeaApplyinstProjMapper.getAeaApplyinstProjCascadeProjByApplyinstId(applyinstId);
            if(applyinstProjList==null||applyinstProjList.size()==0)
                throw new RuntimeException("无法获取到申报项目信息！");

            String projName = applyinstProjList.get(0).getProjName();

            if(StringUtils.isNotBlank(currTask.getAssignee())){//受理人不为空时才发送短信
                String phoneNum = "";
                OpuOmUserInfo user = opuOmUserInfoService.getOpuOmUserInfoByloginName(currTask.getAssignee());
                if(user!=null)
                    phoneNum = user.getUserMobile();

                String dueNum = "";//剩余天数

                ActStoTimeruleInst timeruleInst = new ActStoTimeruleInst();
                timeruleInst.setOrgId(SecurityContext.getCurrentOrgId());
                timeruleInst.setProcInstId(currTask.getProcessInstanceId());
                List<ActStoTimeruleInst> timeruleInstList = actStoTimeruleInstMapper.listActStoTimeruleInst(timeruleInst);
                //先查时限实例表，如果存在已计算的时限实例则直接读取
                if(timeruleInstList!=null&&timeruleInstList.size()>0){
                    if(timeruleInstList.get(0).getOverdueTime()!=null)
                        dueNum = timeruleInstList.get(0).getOverdueTime().toString();
                }

                String remindContent = sendSmsTemplateJsonConverter.getOverTimeJobRemindContent(dueNum,projName,applyinstCode,currTask.getName(),phoneNum);
                if (remindContent != null)
                    this.createSendSmsJobRemind(phoneNum,remindContent,applyinstId);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new SmsException(e.getMessage());
        }
    }

    //Todo 办件预警短信提醒（未实现，暂注释）
    //@EventListener
    public void warningTaskAplanmisEvent(WarningTaskAplanmisEvent warningTaskAplanmisEvent){
        log.info("办件预警短信提醒");

        if(!smsConfigProperties.isEnable())
            return;

        String taskId = warningTaskAplanmisEvent.getTaskId();
        if(StringUtils.isBlank(taskId))
            return ;

        try {
            Task currTask = taskService.createTaskQuery().taskId(taskId).singleResult();
            if(currTask==null)
                return;

            String procInstId = currTask.getProcessInstanceId();

            ActStoAppinst actStoAppinst = actStoAppinstService.getActStoAppinstByProcInstId(procInstId);
            if(actStoAppinst==null){
                ActStoAppinstSubflow actStoAppinstSubflow = actStoAppinstSubflowMapper.getActStoAppinstSubflowBySubflowProcinstId(procInstId);
                if(actStoAppinstSubflow!=null){
                    actStoAppinst = actStoAppinstService.getActStoAppinstById(actStoAppinstSubflow.getAppinstId());
                }
            }

            String applyinstId = actStoAppinst.getMasterRecordId();

            AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);
            if (aeaHiApplyinst == null)
                throw new RuntimeException("申报实例获取为空！");

            String applyinstCode = aeaHiApplyinst.getApplyinstCode();

            List<AeaApplyinstProj> applyinstProjList = aeaApplyinstProjMapper.getAeaApplyinstProjCascadeProjByApplyinstId(applyinstId);
            if(applyinstProjList==null||applyinstProjList.size()==0)
                throw new RuntimeException("无法获取到申报项目信息！");

            String projName = applyinstProjList.get(0).getProjName();

            if(StringUtils.isNotBlank(currTask.getAssignee())){//受理人不为空时才发送短信
                String phoneNum = "";
                OpuOmUserInfo user = opuOmUserInfoService.getOpuOmUserInfoByloginName(currTask.getAssignee());
                if(user!=null)
                    phoneNum = user.getUserMobile();

                String dueNum = "";//剩余天数

                ActStoTimeruleInst timeruleInst = new ActStoTimeruleInst();
                timeruleInst.setOrgId(SecurityContext.getCurrentOrgId());
                timeruleInst.setProcInstId(currTask.getProcessInstanceId());
                List<ActStoTimeruleInst> timeruleInstList = actStoTimeruleInstMapper.listActStoTimeruleInst(timeruleInst);
                //先查时限实例表，如果存在已计算的时限实例则直接读取
                if(timeruleInstList!=null&&timeruleInstList.size()>0){
                    if(timeruleInstList.get(0).getRemainingTime()!=null)
                        dueNum = timeruleInstList.get(0).getRemainingTime().toString();
                }

                String remindContent = sendSmsTemplateJsonConverter.getWaringJobRemindContent(dueNum,projName,applyinstCode,currTask.getName(),phoneNum);
                //暂未实现，暂时注释
                //if (remindContent != null)
                //this.createSendSmsJobRemind(phoneNum,remindContent,applyinstId);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new SmsException(e.getMessage());
        }
    }

    //Todo 催办短信提醒
    @EventListener
    public void bpmRemindSmsAplanmisEvent(BpmRemindSmsAplanmisEvent bpmRemindSmsAplanmisEvent){
        log.info("催办短信提醒");

        if(!smsConfigProperties.isEnable())
            return;

        String applyinstId = bpmRemindSmsAplanmisEvent.getApplyinstId();
        if(StringUtils.isBlank(applyinstId))
            return ;

        String userId = bpmRemindSmsAplanmisEvent.getUserId();
        if(StringUtils.isBlank(userId))
            return ;
        try {
            String phoneNum = "";
            String projName = null;
            String projCode = null;
            String applyinstCode = null;

            OpuOmUserInfo user = opuOmUserInfoService.getOpuOmUserInfoByUserId(userId);
            if (user != null)
                phoneNum = user.getUserMobile();

            AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);
            if(aeaHiApplyinst!=null)
                applyinstCode = aeaHiApplyinst.getApplyinstCode();

            List<AeaApplyinstProj> applyinstProjList = aeaApplyinstProjMapper.getAeaApplyinstProjCascadeProjByApplyinstId(applyinstId);
            if(applyinstProjList==null||applyinstProjList.size()==0)
                throw new RuntimeException("无法获取到申报项目信息！");

            projName = applyinstProjList.get(0).getProjName();
            projCode = applyinstProjList.get(0).getLocalCode();

            String remindContent = sendSmsTemplateJsonConverter.getBpmRemindSmsContent(projName, projCode, applyinstCode, phoneNum);
            if (remindContent != null)
                this.createSendSmsJobRemind(phoneNum, remindContent, applyinstId);
        }catch (Exception e){
            e.printStackTrace();
            throw new SmsException(e.getMessage());
        }
    }

    //Todo 邮寄方式取件短信提醒（快递取件的放在统一取件录入快递号之后）
    @EventListener
    public void applyMailQujianAplanmisEvent(ApplyMailQujianAplanmisEvent applyMailQujianAplanmisEvent){
        log.info("申报办结短信提醒（仅限窗口领件短信提醒，快递取件的放在统一取件录入快递号之后）");

        if(!smsConfigProperties.isEnable())
            return;

        String applyinstId = applyMailQujianAplanmisEvent.getApplyinstId();
        if(StringUtils.isBlank(applyinstId))
            return ;

        try {
            AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);
            if (aeaHiApplyinst == null)
                throw new RuntimeException("申报实例获取为空！");

            String applyinstCode = aeaHiApplyinst.getApplyinstCode();
            String linkmanInfoId = aeaHiApplyinst.getLinkmanInfoId();

            List<AeaApplyinstProj> applyinstProjList = aeaApplyinstProjMapper.getAeaApplyinstProjCascadeProjByApplyinstId(applyinstId);
            if(applyinstProjList==null||applyinstProjList.size()==0)
                throw new RuntimeException("无法获取到申报项目信息！");

            String projName = applyinstProjList.get(0).getProjName();

            AeaHiSmsInfo aeaHiSmsInfo = aeaHiSmsInfoMapper.getAeaHiSmsInfoByApplyinstId(applyinstId);
            if(aeaHiSmsInfo!=null){
                String receiveMode = aeaHiSmsInfo.getReceiveMode();//领取模式：1 窗口取证  0 邮政快递

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String overDate = simpleDateFormat.format(new Date());

                String phoneNum = "";
                if(StringUtils.isNotBlank(linkmanInfoId)) {
                    //获取联系人电话
                    AeaLinkmanInfo aeaLinkmanInfo = aeaLinkmanInfoMapper.getAeaLinkmanInfoById(linkmanInfoId);

                    if(aeaLinkmanInfo!=null)
                        phoneNum = aeaLinkmanInfo.getLinkmanMobilePhone();
                }

                String expressNum = applyMailQujianAplanmisEvent.getExpressNum();

                if("0".equals(receiveMode)){//快递取证
                    String remindContent = sendSmsTemplateJsonConverter.getNodeEndMailJobRemindContent(overDate,projName,applyinstCode,expressNum,phoneNum);
                    if (remindContent != null)
                        this.createSendSmsJobRemind(phoneNum,remindContent,applyinstId);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new SmsException(e.getMessage());
        }
    }

    /**
     * 根据流程实例ID获取当前流程的时限
     * @param procInstId 流程实例ID
     * @return
     * @throws Exception
     */
    private String getDueNum(String procInstId) throws Exception{
        String dueNum = "";

        ActStoTimeruleInst timeruleInst = new ActStoTimeruleInst();
        timeruleInst.setOrgId(SecurityContext.getCurrentOrgId());
        timeruleInst.setProcInstId(procInstId);
        List<ActStoTimeruleInst> timeruleInstList = actStoTimeruleInstMapper.listActStoTimeruleInst(timeruleInst);
        //先查时限实例表，如果存在已计算的时限实例则直接读取
        if(timeruleInstList!=null&&timeruleInstList.size()>0){
            if(timeruleInstList.get(0).getRemainingTime()!=null)
                dueNum = timeruleInstList.get(0).getRemainingTime().toString();
        }

        if(StringUtils.isBlank(dueNum)){//没有时限计算实例，则实时查询时限定义的天数
            ActStoAppinstSubflow appinstSubflow = actStoAppinstSubflowMapper.getActStoAppinstSubflowBySubflowProcinstId(procInstId);
            if(appinstSubflow!=null){
                String triggerId = appinstSubflow.getTriggerId();

                ActTplAppTrigger trigger = actTplAppTriggerMapper.getActTplAppTriggerById(triggerId);
                if(trigger!=null){
                    ActTplAppFlowdef flowdef = actTplAppFlowdefMapper.getActTplAppFlowdefById(trigger.getAppFlowdefId());
                    if(flowdef!=null)
                        dueNum = flowdef.getTimeLimit().toString();
                }
            }
        }
        return dueNum;
    }

    /**
     * 根据主流程实例ID获取所有的子流程的运行任务实例集合（包含主流程的运行任务实例）
     * @param masterProcinstId 主流程实例ID
     * @return
     * @throws Exception
     */
    private List<Task> getAllRuTasksByMasterProcinstId(String masterProcinstId) throws Exception {
        List<Task> list = new ArrayList<>();

        List<Task> currProcTasks = taskService.createTaskQuery().processInstanceId(masterProcinstId).list();
        if(currProcTasks!=null&&currProcTasks.size()>0)
            list.addAll(currProcTasks);

        ActStoAppinst masterAppinst = actStoAppinstService.getActStoAppinstByProcInstId(masterProcinstId);

        ActStoAppinstSubflow actStoAppinstSubflow = new ActStoAppinstSubflow();
        actStoAppinstSubflow.setAppinstId(masterAppinst.getAppinstId());
        List<ActStoAppinstSubflow> subflowAppinsts = actStoAppinstSubflowMapper.listActStoAppinstSubflow(actStoAppinstSubflow);

        if(subflowAppinsts!=null&&subflowAppinsts.size()>0){
            for(ActStoAppinstSubflow subflow:subflowAppinsts){
                List<Task> subTasks = taskService.createTaskQuery().processInstanceId(subflow.getSubflowProcinstId()).list();
                if(subTasks!=null&&subTasks.size()>0)
                    list.addAll(subTasks);
            }
        }
        return list;
    }

    /**
     * 创建短信通知信息对象并保存
     * @param phoneNum
     * @param remindContent
     * @param applyinstId
     */
    private void createSendSmsJobRemind(String phoneNum,String remindContent,String applyinstId){
        BscJobRemind acceptJobRemind = bscJobRemindBuilder.build();
        acceptJobRemind.setRemindUserIdList(phoneNum);
        acceptJobRemind.setRemindContent(remindContent);
        acceptJobRemind.setRecordId(applyinstId);

        try {
            bscJobRemindService.saveBscJobRemind(acceptJobRemind);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("保存短信提醒jobRemind信息失败！");
            throw new SmsException(e.getMessage());
        }
    }
}
