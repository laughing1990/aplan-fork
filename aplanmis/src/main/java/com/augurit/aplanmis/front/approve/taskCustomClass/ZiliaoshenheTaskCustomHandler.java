package com.augurit.aplanmis.front.approve.taskCustomClass;

import com.augurit.agcloud.bpm.common.domain.ActStoAppinst;
import com.augurit.agcloud.bpm.common.domain.ActTplAppFlowdef;
import com.augurit.agcloud.bpm.common.domain.ActTplAppTrigger;
import com.augurit.agcloud.bpm.common.engine.handler.AssigneeRangeHandler;
import com.augurit.agcloud.bpm.common.mapper.ActTplAppFlowdefMapper;
import com.augurit.agcloud.bpm.common.mapper.ActTplAppTriggerMapper;
import com.augurit.agcloud.bpm.common.service.ActStoAppinstService;
import com.augurit.agcloud.bpm.common.utils.SpringContextHolder;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiIteminstService;
import com.augurit.aplanmis.common.service.instance.AeaHiParStageinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiParStateinstService;
import com.augurit.aplanmis.common.service.item.AeaItemBasicService;
import com.augurit.aplanmis.front.apply.service.AeaParStageService;
import com.augurit.aplanmis.integration.guangdong.shuguang.rest.config.GDCatalogItemSyncConfig;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.Process;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.*;

/**
 *  联合验收的资料审核节点自定义处理类（江门使用）
 */
public class ZiliaoshenheTaskCustomHandler implements AssigneeRangeHandler {
    private static Logger logger = LoggerFactory.getLogger(AssigneeRangeHandler.class);
    @Override
    public List<String> assign(UserTask userTask, String processIntanceId) throws Exception {
        logger.debug("===================资料审核自定义逻辑类开始=======================");
        ActStoAppinstService actStoAppinstService = SpringContextHolder.getBean(ActStoAppinstService.class);
        AeaHiApplyinstService aeaHiApplyinstService = SpringContextHolder.getBean(AeaHiApplyinstService.class);
        AeaHiParStateinstMapper aeaHiParStateinstMapper = SpringContextHolder.getBean(AeaHiParStateinstMapper.class);
        AeaHiParStageinstService aeaHiParStageinstService = SpringContextHolder.getBean(AeaHiParStageinstService.class);
        AeaParStateMapper aeaParStateMapper = SpringContextHolder.getBean(AeaParStateMapper.class);
        AeaItemBasicService aeaItemBasicService =SpringContextHolder.getBean(AeaItemBasicService.class);
        AeaItemVerMapper aeaItemVerMapper = SpringContextHolder.getBean(AeaItemVerMapper.class);
        ActTplAppTriggerMapper actTplAppTriggerMapper = SpringContextHolder.getBean(ActTplAppTriggerMapper.class);
        ActTplAppFlowdefMapper actTplAppFlowdefMapper = SpringContextHolder.getBean(ActTplAppFlowdefMapper.class);
        HistoryService historyService = SpringContextHolder.getBean(HistoryService.class);
        RepositoryService repositoryService = SpringContextHolder.getBean(RepositoryService.class);
        List<String> loginNameList = new ArrayList<>();

        ActStoAppinst actStoAppinst = actStoAppinstService.getActStoAppinstByProcInstId(processIntanceId);
        String applyinstId = actStoAppinst.getMasterRecordId();
        AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);//拿到申报实例对象
        //绑定的申报阶段信息
        AeaHiParStageinst aeaHiParStageinst = aeaHiParStageinstService.getAeaHiParStageinstByApplyinstId(aeaHiApplyinst.getApplyinstId());

        //获取阶段下并联审批事项列表
        AeaParStageItemMapper aeaParStageItemMapper =SpringContextHolder.getBean(AeaParStageItemMapper.class);
        AeaParStateItemMapper aeaParStateItemMapper =SpringContextHolder.getBean(AeaParStateItemMapper.class);
        AeaItemBasicMapper aeaItemBasicMapper =SpringContextHolder.getBean(AeaItemBasicMapper.class);
        List<AeaParStageItem> aeaParStageItems = aeaParStageItemMapper.listStageItems(aeaHiParStageinst.getStageId(),"0");
        //去除事项列表中绑定了情形的数据
        List<AeaItemBasic> aeaItemBasicss = new ArrayList<>();
        if(aeaParStageItems!=null&&aeaParStageItems.size()>0){
            for (int i = 0; i < aeaParStageItems.size(); i++){
                AeaParStateItem aeaParStateItem =aeaParStateItemMapper.getAeaParStateItemByStageItemId(aeaParStageItems.get(i).getStageItemId());
                if(aeaParStateItem!=null){
                    aeaParStageItems.remove(i);
                    i--;
                }
            }
            for (AeaParStageItem aeaParStageItem:aeaParStageItems){
                AeaItemBasic aeaItemBasic = new AeaItemBasic();
                aeaItemBasic.setItemId(aeaParStageItem.getItemId());
                aeaItemBasic.setItemVerId(aeaParStageItem.getItemVerId());
                List<AeaItemBasic> aeaItemBasics = aeaItemBasicMapper.listAeaItemBasic(aeaItemBasic);
                if(aeaItemBasics!=null && aeaItemBasics.size()>0){
                    for (int i = 0; i < aeaItemBasics.size(); i++) {
                        aeaItemBasicss.add(aeaItemBasics.get(i));
                    }
                }
            }
        }
        AeaHiParStateinst aeaHiParStateinst = new AeaHiParStateinst();
        aeaHiParStateinst.setStageinstId(aeaHiParStageinst.getStageinstId());
        //查看阶段绑定的情形
        List<AeaHiParStateinst> aeaHiParStateinsts = aeaHiParStateinstMapper.listAeaHiParStateinst(aeaHiParStateinst);
        //获取选择的情形答案
        List<AeaParState> aeaParStates = new ArrayList<>();
        for (AeaHiParStateinst aeaHiParStateinst1:aeaHiParStateinsts){
            AeaParState aeaParState = aeaParStateMapper.getAeaParStateById(aeaHiParStateinst1.getExecStateId());
            aeaParStates.add(aeaParState);
        }
        //父情形绑定的事项
        for (int i = 0; i < aeaParStates.size(); i++) {
            List<AeaItemBasic> aeaItemBasics = aeaItemBasicService.listAeaParStateItemByStateId(aeaParStates.get(i).getParStateId());
            for (AeaItemBasic aeaItemBasic:aeaItemBasics){
                aeaItemBasicss.add(aeaItemBasic);
            }
        }
//        for (String procinstId1:procinstIds){
//            List<HistoricTaskInstance> task = historyService.createHistoricTaskInstanceQuery().processInstanceId(procinstId1).list();
//            if(task!=null&&task.size()>0){
//                for (HistoricTaskInstance tasks:task)
//                    names.add(tasks.getAssignee());
//            }
//        }
        List<ActTplAppTrigger> actTplAppTriggers = new ArrayList<>();
        //查询事项流程   部门审批受理节点的审批人
        for (AeaItemBasic aeaItemBasic2:aeaItemBasicss){
            List<AeaItemVer> aeaItemVers = aeaItemVerMapper.queryAeaItemVerByItemId(aeaItemBasic2.getItemId());
            List<ActTplAppTrigger> actTplAppTriggerss = new ArrayList<>();
            if(aeaItemVers!=null&&aeaItemVers.size()>0){
                for (AeaItemVer aeaItemVer:aeaItemVers){
                    ActTplAppTrigger ActTplAppTrigger = new ActTplAppTrigger();
                    ActTplAppTrigger.setTriggerAppId(aeaItemBasic2.getAppId());
                    ActTplAppTrigger.setBusRecordId(aeaItemVer.getItemVerId());
                    actTplAppTriggerss.add(ActTplAppTrigger);
                }
            }
            //根据事项appid以及事项版本item_ver_id查出流程定义
            if(actTplAppTriggerss!=null && actTplAppTriggerss.size()>0){
                for (ActTplAppTrigger actTplAppTrigger1:actTplAppTriggerss){
                    List<ActTplAppTrigger> actTplAppTrigger = actTplAppTriggerMapper.listActTplAppTrigger(actTplAppTrigger1);
                    if(actTplAppTrigger!=null&&actTplAppTrigger.size()>0){
                        for (ActTplAppTrigger actTplAppTrigger2:actTplAppTrigger){
                            actTplAppTriggers.add(actTplAppTrigger2);
                        }
                    }
                }
            }
        }
        List<ActTplAppFlowdef> actTplAppFlowdefs = new ArrayList<>();
        if(actTplAppTriggers!=null&&actTplAppTriggers.size()>0){
            for (ActTplAppTrigger actTplAppTrigger1:actTplAppTriggers){
                ActTplAppFlowdef actTplAppFlowdef = actTplAppFlowdefMapper.getActTplAppFlowdefById(actTplAppTrigger1.getTriggerAppFlowdefId());
                logger.debug("========"+actTplAppFlowdef.toString());
                actTplAppFlowdefs.add(actTplAppFlowdef);
            }
        }

        if(actTplAppFlowdefs!=null&&actTplAppFlowdefs.size()>0){
            for (ActTplAppFlowdef appFlowdef:actTplAppFlowdefs){
                String procdefKey = appFlowdef.getProcdefKey();
                ProcessDefinition def = repositoryService.createProcessDefinitionQuery().processDefinitionKey(procdefKey).latestVersion().singleResult();
                if(def!=null){
                    BpmnModel bpmnModel = repositoryService.getBpmnModel(def.getId());
                    if(bpmnModel!=null){
                        List<Process> processList = bpmnModel.getProcesses();
                        if(processList!=null&&processList.size()>0){
                            Process process = processList.get(0);

                            Collection<FlowElement> flowElements = process.getFlowElements();

                            List<FlowElement> elements = new ArrayList<>();
                            if(flowElements.size()!=process.getFlowElementMap().size()){
                                Set<String> keys = process.getFlowElementMap().keySet();
                                for(String key:keys){
                                    elements.add(process.getFlowElementMap().get(key));
                                }

                                flowElements.addAll(elements);
                            }

                            if(flowElements!=null&&flowElements.size()>0){
                                for(FlowElement flowElement:flowElements){
                                    if(flowElement instanceof StartEvent){
                                        StartEvent startEvent = (StartEvent)flowElement;
                                        List<SequenceFlow> sequenceFlowList = startEvent.getOutgoingFlows();
                                        if(sequenceFlowList!=null&&sequenceFlowList.size()>0){
                                            for(SequenceFlow sequenceFlow:sequenceFlowList){
                                                FlowElement targetElememt = sequenceFlow.getTargetFlowElement();
                                                if(targetElememt instanceof UserTask){
                                                    UserTask currUserTask = (UserTask)targetElememt;
                                                    String assigneeStr = currUserTask.getAssignee();
                                                    if(StringUtils.isBlank(assigneeStr)){
                                                        assigneeStr = currUserTask.getAssigneeRange();
                                                    }

                                                    if(StringUtils.isNotBlank(assigneeStr)&&assigneeStr.indexOf(",")>-1){
                                                        String[] assignees = assigneeStr.split(",");

                                                        if(assignees!=null&&assignees.length>0){
                                                            for(String assignee:assignees){
                                                                if(StringUtils.isNotBlank(assignee)&&!assignee.contains("ORG")
                                                                        &&!assignee.contains("POS")&&!assignee.contains("ROLE"))
                                                                    loginNameList.add(assignee);
                                                            }
                                                        }

                                                    }else if(StringUtils.isNotBlank(assigneeStr)){
                                                        loginNameList.add(assigneeStr);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        //利用hashSet去重
        HashSet h = new HashSet(loginNameList);
        loginNameList.clear();
        loginNameList.addAll(h);
        //根据流程实例id查询task
        logger.debug("===================资料审核自定义逻辑类结束=======================");
        return loginNameList;
    }
}
