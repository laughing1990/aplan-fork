package com.augurit.aplanmis.province.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.augurit.agcloud.bpm.common.domain.ActStoAppinst;
import com.augurit.agcloud.bpm.common.domain.ActStoAppinstSubflow;
import com.augurit.agcloud.bpm.common.domain.ActTplAppTrigger;
import com.augurit.agcloud.bpm.common.domain.BpmHistoryCommentForm;
import com.augurit.agcloud.bpm.common.engine.BpmTaskService;
import com.augurit.agcloud.bpm.common.mapper.ActStoAppinstMapper;
import com.augurit.agcloud.bpm.common.mapper.ActStoAppinstSubflowMapper;
import com.augurit.agcloud.bpm.common.mapper.ActTplAppTriggerMapper;
import com.augurit.agcloud.bpm.common.service.ActStoAppinstService;
import com.augurit.agcloud.bpm.common.service.ActStoAppinstSubflowService;
import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.mapper.BscDicCodeMapper;
import com.augurit.agcloud.bsc.sc.att.service.IBscAttService;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.mapper.OpuOmOrgMapper;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.mapper.AeaHiApplyinstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiIteminstMapper;
import com.augurit.aplanmis.province.vo.BpmHistoryCommentFormVo;
import com.augurit.aplanmis.province.vo.HistoryProcessVo;
import com.augurit.aplanmis.province.vo.ParallelApproveDataVo;
import org.apache.commons.lang.StringUtils;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class ApproveProcessService {
    @Autowired
    private ActStoAppinstMapper actStoAppinstMapper;
    @Autowired
    private IBscAttService bscAttService;
    @Autowired
    private BpmTaskService bpmTaskService;
    @Autowired
    private AeaHiApplyinstMapper aeaHiApplyinstMapper;
    @Autowired
    private AeaHiIteminstMapper aeaHiIteminstMapper;
    @Autowired
    private OpuOmOrgMapper opuOmOrgMapper;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private ActStoAppinstSubflowMapper actStoAppinstSubflowMapper;
    @Autowired
    private ActStoAppinstSubflowService actStoAppinstSubflowService;
    @Autowired
    private ActStoAppinstService actStoAppinstService;
    @Autowired
    private ApproveCommonService approveCommonService;
    @Autowired
    private BscDicCodeMapper bscDicCodeMapper;
    @Autowired
    private ActTplAppTriggerMapper actTplAppTriggerMapper;

    @Value("${dg.sso.access.platform.org.top-org-id:0368948a-1cdf-4bf8-a828-71d796ba89f6}")
    private String topOrgId;

    public List<HistoryProcessVo> listHistoryApproveProcess(String proj_code, String item_instance_code) throws Exception {
        AeaHiApplyinst applyinst = approveCommonService.getApplyinstByProjCodeAndIteminstId(proj_code, item_instance_code);
        if (applyinst != null) {
            List<String> procInstIds = approveCommonService.
                    getProcInstIdByApplyinstIdOrApplyinstCode(applyinst.getApplyinstId(), null);
            if (procInstIds.size() > 0) {
                List<HistoryProcessVo> historyProcessVos = listHistoryCommentAll(procInstIds.get(0), applyinst.getApplyinstId(), applyinst.getIteminstId());
                return historyProcessVos;
            }
        }
        return new ArrayList<HistoryProcessVo>();
    }

    public List<BpmHistoryCommentFormVo> listHistoryCommentTree(String processInstanceId, boolean isNeedQueryDetail, String applyinstId) throws Exception {
        // 保证processInstanceId属于一级流程节点
        processInstanceId = getTopParentProcess(processInstanceId);
        List<BpmHistoryCommentFormVo> parentNodeList = listHistoryComment(processInstanceId, isNeedQueryDetail, applyinstId);
        List<BpmHistoryCommentFormVo> resultNodeList = new ArrayList<>();
        //先过滤一级流程
        filterNode(parentNodeList, resultNodeList);
        convertToTree(resultNodeList, applyinstId);
        addApproverToHistoryComment(resultNodeList);
        return resultNodeList;
    }

    public List<ParallelApproveDataVo> listParallelApproveData(String project_code, String item_instance_code) throws Exception {
        List<ParallelApproveDataVo> parallelApproveDataVos = new ArrayList<>();
        List<AeaHiApplyinst> applyinsts = approveCommonService.getParalleApproveDataByIteminstIdAndProjCode(project_code, item_instance_code);
        if (applyinsts.size() > 0) {
            for (AeaHiApplyinst aeaHiApplyinst : applyinsts) {
                ParallelApproveDataVo parallelApproveDataVo = new ParallelApproveDataVo();
                parallelApproveDataVo.setApplyinstId(aeaHiApplyinst.getApplyinstId());
                parallelApproveDataVo.setIteminstId(aeaHiApplyinst.getIteminstId());
                parallelApproveDataVo.setProjCode(StringUtils.isNotBlank(aeaHiApplyinst.getCentralCode()) ? aeaHiApplyinst.getCentralCode() : aeaHiApplyinst.getLocalCode());
                BscDicCodeItem dic = bscDicCodeMapper.getItemByTypeCodeAndItemCodeAndOrgId("BUSINESS_STATE", aeaHiApplyinst.getBusinessState(), topOrgId);
                parallelApproveDataVo.setCurrentState(dic == null ? "" : dic.getItemName());
                parallelApproveDataVo.setFileList(approveCommonService.getMatlist(new String[]{aeaHiApplyinst.getIteminstId()}, "1"));
                parallelApproveDataVo.setItemName(aeaHiApplyinst.getItemName());
                OpuOmOrg opuOrg = opuOmOrgMapper.getOrg(aeaHiApplyinst.getApproveOrgId());
                parallelApproveDataVo.setApproveOrgName(opuOrg == null ? "" : opuOrg.getOrgName());
                parallelApproveDataVos.add(parallelApproveDataVo);
            }
        }
        return parallelApproveDataVos;
    }

    public String getIteminstIdByTaskId(String taskId) throws Exception {
        String key = "$BUS_CURRENT_ITEMINST_ID";
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = null;
        if (task != null) {
            processInstanceId = task.getProcessInstanceId();
        } else {
            HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
            if (historicTaskInstance != null) {
                processInstanceId = historicTaskInstance.getProcessInstanceId();
            }
        }
        if (StringUtils.isBlank(processInstanceId)) {
            return null;
        }
        String iteminstId = null;
        HistoricVariableInstance historicVariableInstance = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).variableName(key).singleResult();
        if (historicVariableInstance != null) {
            iteminstId = (String) historicVariableInstance.getValue();
        }
        if (StringUtils.isBlank(iteminstId)) {
            iteminstId = (String) runtimeService.getVariable(processInstanceId, key);
        }
        return iteminstId;
    }

    public List<HistoryProcessVo> listHistoryComment(String processInstanceId, String applyinstId) throws Exception {
        //获取流程历史意见列表
        List<BpmHistoryCommentForm> historyComments = bpmTaskService.getHistoryCommentsByProcessInstanceId(processInstanceId);
        List<HistoryProcessVo> results = new ArrayList<>();
        //获取流程当前所处节点
        List<Task> list = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        if (historyComments != null && historyComments.size() > 0) {
            for (BpmHistoryCommentForm temp : historyComments) {
                if (5 == temp.getTaskState()) {
                    continue;
                }
                if (list.size() > 0 && temp.getTaskId().equals(list.get(0).getId())) {
                    continue;
                }
                OpuOmOrg org = getOpuOmOrgByApplyinstBranchOrg(applyinstId, list.size() > 0 ? list.get(0).getId() : "");
                if (org == null) org = opuOmOrgMapper.getOrg(temp.getOrgId());
                String orgName = org != null ? org.getOrgName() : "";
                HistoryProcessVo tempObj = new HistoryProcessVo();
                BeanUtils.copyProperties(temp, tempObj);
                if (StringUtils.isNotBlank(orgName)) temp.setOrgName(orgName);
                tempObj.setFileList(
                        bscAttService.listAttLinkAndDetailNoPage(
                                "AEA_HI_TASK",
                                "ID_",
                                tempObj.getTaskId(),
                                null,
                                topOrgId,
                                null));
                //临时处理
                if ("收件".equals(temp.getNodeName())) {
                    tempObj.setReceiveList(approveCommonService.getAeaHiReceiveByApplyinstId(applyinstId));
                }
                results.add(tempObj);
            }
        }
        sortDgListHistoryCommentBySigeInDate_old(results);
        return results;
    }

    public List<BpmHistoryCommentFormVo> listHistoryComment(String processInstanceId, boolean isNeedQueryDetail, String applyinstId) throws Exception {
        // 获取流程历史意见列表
        List<BpmHistoryCommentForm> historyComments = bpmTaskService.getHistoryCommentsByProcessInstanceId(processInstanceId);
        List<BpmHistoryCommentFormVo> results = new ArrayList<>();
        // 获取流程当前所处节点
        List<Task> list = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        // 是否存在活动节点
        boolean isExistNode = false;
        if (list != null && list.size() > 0) {
            isExistNode = true;
        }
//        String topOrgId = SecurityContext.getCurrentOrgId();
        if (historyComments != null && historyComments.size() > 0) {
            for (BpmHistoryCommentForm temp : historyComments) {
                // 去掉【已终止】状态的意见
                if (5 == temp.getTaskState()) {
                    continue;
                }
                OpuOmOrg org = null;
                if (StringUtils.isNotBlank(applyinstId)) {
                    AeaHiApplyinst applyinst = aeaHiApplyinstMapper.getAeaHiApplyinstById(applyinstId);
                    if (applyinst != null) {
                        String branchOrgStr = applyinst.getBranchOrg();
                        if (StringUtils.isNotBlank(branchOrgStr)) {
                            String itemVerId = "";
                            String taskId = list.size() > 0 ? list.get(0).getId() : "";
                            if (StringUtils.isNotBlank(taskId)) {
//                                String iteminstId = approveService.getIteminstIdByTaskId(taskId);
                                String iteminstId = this.getIteminstIdByTaskId(taskId);
                                if (StringUtils.isNotBlank(iteminstId)) {
                                    AeaHiIteminst iteminst = aeaHiIteminstMapper.getAeaHiIteminstById(iteminstId);
                                    itemVerId = iteminst != null ? iteminst.getItemVerId() : "";

                                    JSONArray mapList = JSONObject.parseArray(branchOrgStr);
                                    if (mapList != null && mapList.size() > 0) {
                                        for (int i = 0; i < mapList.size(); i++) {
                                            Map<String, String> map = (Map<String, String>) mapList.get(i);
                                            String branchOrgId = map.get("branchOrg");
                                            String branchItemVerId = map.get("itemVerId");
                                            if (StringUtils.isNotBlank(branchOrgId) && StringUtils.isNotBlank(branchItemVerId) && branchItemVerId.equals(itemVerId)) {
                                                org = opuOmOrgMapper.getOrg(branchOrgId);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (org == null) {
                    String orgId = temp.getOrgId();
                    org = opuOmOrgMapper.getOrg(orgId);
                }
                String firstOrgShortName = "";
                String secondOrgShortName = "";
                String orgName = "";
                if (org != null) {
                    firstOrgShortName = org.getOrgShortName1();
                    secondOrgShortName = org.getOrgShortName2();
                    orgName = org.getOrgName();
                }
                BpmHistoryCommentFormVo tempObj = new BpmHistoryCommentFormVo();
                BeanUtils.copyProperties(temp, tempObj);
                if (StringUtils.isNotBlank(orgName)) {
                    tempObj.setOrgName(orgName);
                }
                tempObj.setFirstOrgShortName(firstOrgShortName);
                tempObj.setSecondOrgShortName(secondOrgShortName);
                if (list.stream().anyMatch(temp1 -> tempObj.getTaskId().equals(temp1.getId()))) {
                    tempObj.setDealingTask(isExistNode);
                }
                if (isNeedQueryDetail) {
                    List<BscAttForm> bscAttForms = bscAttService.listAttLinkAndDetailNoPage("AEA_HI_TASK", "ID_", tempObj.getTaskId(), null, topOrgId, null);
                    if (bscAttForms != null) {
                        tempObj.setAttDetailNum(bscAttForms.size());
                    }
                }
                results.add(tempObj);
            }
        }
        sortDgListHistoryCommentBySigeInDate(results);
        return results;
    }

    /**
     * 根据申请实例上的branchOrg查询审批部门
     *
     * @param applyinstId 申请实例
     * @return
     */
    public OpuOmOrg getOpuOmOrgByApplyinstBranchOrg(String applyinstId, String taskId) throws Exception {
        if (StringUtils.isBlank(applyinstId) || StringUtils.isBlank(taskId)) return null;
        AeaHiApplyinst applyinst = aeaHiApplyinstMapper.getAeaHiApplyinstById(applyinstId);
        if (applyinst == null) return null;
        String branchOrgStr = applyinst.getBranchOrg();
        if (StringUtils.isBlank(branchOrgStr)) return null;
        String iteminstId = getIteminstIdByTaskId(taskId);
        if (StringUtils.isNotBlank(iteminstId)) {
            AeaHiIteminst iteminst = aeaHiIteminstMapper.getAeaHiIteminstById(iteminstId);
            String itemVerId = iteminst != null ? iteminst.getItemVerId() : "";
            JSONArray mapList = JSONObject.parseArray(branchOrgStr);
            if (mapList != null && mapList.size() > 0) {
                for (int i = 0; i < mapList.size(); i++) {
                    Map<String, String> map = (Map<String, String>) mapList.get(i);
                    String branchOrgId = map.get("branchOrg");
                    String branchItemVerId = map.get("itemVerId");
                    if (StringUtils.isNotBlank(branchOrgId) && StringUtils.isNotBlank(branchItemVerId) && branchItemVerId.equals(itemVerId)) {
                        return opuOmOrgMapper.getOrg(branchOrgId);
                    }
                }
            }
        }
        return null;
    }

    private void sortDgListHistoryCommentBySigeInDate_old(List<HistoryProcessVo> list) {
        Collections.sort(list, new Comparator<HistoryProcessVo>() {
            @Override
            public int compare(HistoryProcessVo arg0, HistoryProcessVo arg1) {
                if (arg0.getEndDate() != null && arg1.getEndDate() == null) {
                    return -1;
                }
                if (arg0.getEndDate() == null && arg1.getEndDate() != null) {
                    return 1;
                }
                if (arg0.getEndDate() != null && arg1.getEndDate() != null) {
                    return (arg0.getEndDate()).compareTo((arg1.getEndDate()));
                }
                return (arg0.getSigeInDate()).compareTo((arg1.getSigeInDate()));
            }
        });
    }

    private String getTopParentProcess(String processInstanceId) throws Exception {
        String parentProcessId = this.getParentProcessByProcessInstanceId(processInstanceId);
        if (StringUtils.isNotBlank(parentProcessId)) {
            return getTopParentProcess(parentProcessId);
        } else {
            return processInstanceId;
        }
    }

    public String getParentProcessByProcessInstanceId(String processInstanceId) throws Exception {

        ActStoAppinstSubflow appinstSubflow = actStoAppinstSubflowService.getActStoAppinstSubflowBySubflowProcinstId(processInstanceId);

        if (appinstSubflow != null && appinstSubflow.getParentSubflowId() != null) {
            ActStoAppinstSubflow parentSubflow = actStoAppinstSubflowService.getActStoAppinstSubflowById(appinstSubflow.getParentSubflowId());
            if (parentSubflow != null) {
                return parentSubflow.getSubflowProcinstId();
            }
        } else if (appinstSubflow != null && appinstSubflow.getParentSubflowId() == null) {
            ActStoAppinst actStoAppinst = actStoAppinstService.getActStoAppinstById(appinstSubflow.getAppinstId());
            if (actStoAppinst != null) {
                return actStoAppinst.getProcinstId();
            }
        }
        return null;
    }

    public List<HistoryProcessVo> listHistoryCommentTree(String processInstanceId, String applyinstId, String iteminstId) throws Exception {
        processInstanceId = getTopParentProcess(processInstanceId);//保证processInstanceId属于一级流程节点
        List<HistoryProcessVo> parentNodeList = listHistoryComment(processInstanceId, applyinstId);
        AeaHiIteminst iteminst = aeaHiIteminstMapper.getAeaHiIteminstById(iteminstId);
        String itemVerId = iteminst == null ? "" : iteminst.getItemVerId();
        convertToTree(parentNodeList, applyinstId, itemVerId);
        return parentNodeList;
    }

    private void convertToTree(List<HistoryProcessVo> parentNodeList, String applyinstId, String itemVerId) throws Exception {
        if (parentNodeList != null && parentNodeList.size() > 0) {
            for (HistoryProcessVo historyProcessVo : parentNodeList) {
                List<HistoryProcessVo> childNodeList =
                        listHistoryCommentByTaskId(historyProcessVo.getTaskId(), historyProcessVo.isDealingTask(), applyinstId, itemVerId);
                if (childNodeList != null && childNodeList.size() > 0) {
                    historyProcessVo.setChildNode(childNodeList);
                    convertToTree(childNodeList, applyinstId, itemVerId);
                }
            }
        }
    }

    public List<HistoryProcessVo> listHistoryCommentByTaskId(String taskId, boolean isDealing, String applyinstId, String itemVerId) throws Exception {
        String processInstanceId = null;
        HistoricTaskInstance historicTaskInstance = null;
        List<HistoryProcessVo> results = new ArrayList<>();
        Task task = null;
        if (isDealing) {
            //父节点正在办理的情况
           /* task = taskService.createTaskQuery().taskId(taskId).singleResult();
            if (task != null) {
                processInstanceId = task.getProcessInstanceId();
            }*/
            return results;
        } else {
            //父节点已经办理完成的情况
            historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
            if (historicTaskInstance == null) {
                return null;
            }
            processInstanceId = historicTaskInstance.getProcessInstanceId();
        }
        //根据父流程的流程变量获取子流程实例id
        String subProcessInstanceId = null;
        ActStoAppinst actStoAppinst = actStoAppinstMapper.getActStoAppinstByProcInstId(processInstanceId);
        AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstMapper.getAeaHiApplyinstById(applyinstId);
        //说明当前是一级流程，去找二级流程的procinstId
        if (actStoAppinst != null) {
            String appinstId = actStoAppinst.getAppinstId();
            if (StringUtils.isNotBlank(appinstId)) {
                ActStoAppinstSubflow query = new ActStoAppinstSubflow();
                query.setAppinstId(appinstId);
                query.setParentSubflowId(null);
                query.setTriggerTaskinstId(taskId);
                List<ActStoAppinstSubflow> actStoAppinstSubflows = actStoAppinstSubflowMapper.listActStoAppinstSubflow(query);
                if (actStoAppinstSubflows != null && actStoAppinstSubflows.size() > 0) {
                    subProcessInstanceId = actStoAppinstSubflows.get(0).getSubflowProcinstId();
                    if (StringUtils.isNotBlank(subProcessInstanceId)) {
                        results = listHistoryComment(subProcessInstanceId, applyinstId);
                    }
                }
            }
        } else {//当前是二级子流程，去找三级流程的procinstId
            ActStoAppinstSubflow actStoAppinstSubflow = actStoAppinstSubflowMapper.getActStoAppinstSubflowBySubflowProcinstId(processInstanceId);
            if (actStoAppinstSubflow != null) {
                if (aeaHiApplyinst != null) {
                    ActStoAppinstSubflow query = new ActStoAppinstSubflow();
                    query.setTriggerTaskinstId(taskId);
                    List<ActStoAppinstSubflow> appinstSubflows = actStoAppinstSubflowMapper.listActStoAppinstSubflow(query);
                    if (appinstSubflows != null && appinstSubflows.size() > 0) {
                        ActTplAppTrigger trigger = actTplAppTriggerMapper.getActTplAppTriggerById(appinstSubflows.get(0).getTriggerId());
                        String procinstId = appinstSubflows.get(0).getSubflowProcinstId();
                        if (StringUtils.isNotBlank(procinstId) && trigger != null && itemVerId.equals(trigger.getBusRecordId())) {
                            results = listHistoryComment(procinstId, applyinstId);
                        }
                    }
                }
            }
        }
        return results;
    }

    public List<HistoryProcessVo> listHistoryCommentAll(String processInstanceId, String applyinstId, String iteminstId) throws Exception {
        List<HistoryProcessVo> treeList = this.listHistoryCommentTree(processInstanceId, applyinstId, iteminstId);
        List<HistoryProcessVo> resultList = new ArrayList<HistoryProcessVo>();
        for (HistoryProcessVo bpmHistoryCommentForm : treeList) {
            if (bpmHistoryCommentForm.getChildNode() == null || bpmHistoryCommentForm.getChildNode().size() == 0) {//一级节点
                resultList.add(bpmHistoryCommentForm);
            } else {
                List<HistoryProcessVo> childList = bpmHistoryCommentForm.getChildNode();
                convertListToVo(childList, resultList, bpmHistoryCommentForm);
            }
        }
        return resultList;
    }


    public List<BpmHistoryCommentFormVo> listHistoryCommentByTaskId(String applyinstId, BpmHistoryCommentFormVo parentNode) throws Exception {
        String processInstanceId = parentNode.getProcessInstanceId();
        String taskId = parentNode.getTaskId();
        List<BpmHistoryCommentFormVo> childNodes = null;
        List<BpmHistoryCommentFormVo> results = new ArrayList<>();
        String itemProcInstId = null;//事项关联的流程实例id
        //根据父流程的流程变量获取子流程实例id
        String subProcessInstanceId;
        ActStoAppinst actStoAppinst = actStoAppinstMapper.getActStoAppinstByProcInstId(processInstanceId);
        AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstMapper.getAeaHiApplyinstById(applyinstId);
        //说明当前是一级流程，去找二级流程的procinstId
        if (actStoAppinst != null) {
            String appinstId = actStoAppinst.getAppinstId();
            if (StringUtils.isNotBlank(appinstId)) {
                ActStoAppinstSubflow query = new ActStoAppinstSubflow();
                query.setAppinstId(appinstId);
                query.setParentSubflowId(null);
                query.setTriggerTaskinstId(taskId);
                List<ActStoAppinstSubflow> actStoAppinstSubflows = actStoAppinstSubflowMapper.listActStoAppinstSubflow(query);
                if (actStoAppinstSubflows != null && actStoAppinstSubflows.size() > 0) {
                    subProcessInstanceId = actStoAppinstSubflows.get(0).getSubflowProcinstId();
                    if (StringUtils.isNotBlank(subProcessInstanceId)) {
                        itemProcInstId = subProcessInstanceId;
                        results = listHistoryComment(subProcessInstanceId, true, applyinstId);
                    }
                }
            }
        } else {
            // 当前是二级子流程，去找三级流程的procinstId
            ActStoAppinstSubflow actStoAppinstSubflow = actStoAppinstSubflowMapper.getActStoAppinstSubflowBySubflowProcinstId(processInstanceId);
            // parentSubflowId  = null  表示二级流程
            if (actStoAppinstSubflow != null && actStoAppinstSubflow.getParentSubflowId() == null) {

                // 并联审批才有三级流程，2019-04-26修改为单项和并联都允许三级流程
                if (aeaHiApplyinst != null) {
                    ActStoAppinstSubflow query = new ActStoAppinstSubflow();
                    query.setTriggerTaskinstId(taskId);
                    List<ActStoAppinstSubflow> appinstSubflows = actStoAppinstSubflowMapper.listActStoAppinstSubflow(query);
                    if (appinstSubflows != null && appinstSubflows.size() > 0) {
                        String procinstId = appinstSubflows.get(0).getSubflowProcinstId();
                        if (StringUtils.isNotBlank(procinstId)) {
                            itemProcInstId = procinstId;
                            results = listHistoryComment(procinstId, true, applyinstId);
                        }
                    } else {
                        // 如果查询不到子流程的（也就是在父子流程配置时，对接外部的节点不需要配置子流程），尝试查询外部对接同步的审批数据，使用 taskId，作为关联到同步表中查询出必要数据组装。
                        // this.splitJointExternalComments(aeaHiApplyinst.getApplyinstCode(),taskId,results);
                    }
                }
            } else if (actStoAppinstSubflow != null && actStoAppinstSubflow.getParentSubflowId() != null) {
                if (aeaHiApplyinst != null) {
                    ActStoAppinstSubflow query = new ActStoAppinstSubflow();
                    query.setTriggerTaskinstId(taskId);
                    List<ActStoAppinstSubflow> appinstSubflows = actStoAppinstSubflowMapper.listActStoAppinstSubflow(query);
                    if (appinstSubflows != null && appinstSubflows.size() > 0) {
                        String procinstId = appinstSubflows.get(0).getSubflowProcinstId();
                        if (StringUtils.isNotBlank(procinstId)) {
                            itemProcInstId = procinstId;
                            results = listHistoryComment(procinstId, true, applyinstId);
                        }
                    }
                }
            }
        }
        //事项节点名称显示为事项名称
        String nodeName = parentNode.getNodeName();
        String isItemNode = "0";
        String iteminstId = null;
        String approveOrgName = null;
        if (StringUtils.isNotBlank(itemProcInstId)) {
            //这里通过流程实例获取事项实例的方式有点问题，如果出现多次触发同一事项的子流程则会出现覆盖和数据丢失的问题，所以原则上，同一个事项实例只能对应一个事项审批流程
            AeaHiIteminst aeaHiIteminst = aeaHiIteminstMapper.getAeaHiIteminstByProcinstId(itemProcInstId);
            if (aeaHiIteminst == null) {
                //如果为空，尝试流程变量里面获取
                HistoricVariableInstance variableInstance = historyService.createHistoricVariableInstanceQuery().processInstanceId(itemProcInstId).variableName("$BUS_CURRENT_ITEMINST_ID").singleResult();
                if (variableInstance != null) {
                    aeaHiIteminst = aeaHiIteminstMapper.getAeaHiIteminstById(variableInstance.getValue().toString());
                }
            }
            if (aeaHiIteminst != null) {
                nodeName = aeaHiIteminst.getIteminstName();
                isItemNode = "1";
                approveOrgName = aeaHiIteminst.getApproveOrgName();
            }
        }
        if (aeaHiApplyinst != null && "1".equals(aeaHiApplyinst.getIsSeriesApprove()) && "部门审批".equals(parentNode.getNodeName()) && StringUtils.isNotBlank(itemProcInstId)) {
            BpmHistoryCommentFormVo itemNode = new BpmHistoryCommentFormVo();
            itemNode.setNodeName(nodeName);
            itemNode.setIsItemNode("1");
            itemNode.setDealingTask(parentNode.isDealingTask());
            itemNode.setChildNode(results);
            itemNode.setIteminstId(iteminstId);
            itemNode.setOrgName(approveOrgName);
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(itemProcInstId).singleResult();
            if (historicProcessInstance != null && historicProcessInstance.getEndTime() != null) {
                itemNode.setEndDate(historicProcessInstance.getEndTime());
            }
            childNodes = new ArrayList<>();
            childNodes.add(itemNode);
        } else {
            childNodes = results;
            parentNode.setNodeName(nodeName);
            parentNode.setIsItemNode(isItemNode);
            parentNode.setIteminstId(iteminstId);
            if (StringUtils.isNotBlank(approveOrgName)) {
                parentNode.setOrgName(approveOrgName);
            }
        }
        //过滤重复的节点，即多工作项合并
        List<BpmHistoryCommentFormVo> temp = new ArrayList<>();
        filterNode(childNodes, temp);
        parentNode.setChildNode(temp);
        return temp;
    }


    private void convertListToVo(List<HistoryProcessVo> oldList, List<HistoryProcessVo> newList, HistoryProcessVo copyForm) {
        for (HistoryProcessVo form2 : oldList) {//2级菜单
            if (form2.getChildNode() == null || form2.getChildNode().size() == 0) {
                form2.setNodeName(String.format("%s(%s)", copyForm.getNodeName(), form2.getNodeName()));
                if (StringUtils.isNotBlank(form2.getTaskAssignee())) newList.add(form2);
            } else {
                List<HistoryProcessVo> childNodeList = form2.getChildNode();
                for (HistoryProcessVo commentForm : childNodeList) {
                    if (!commentForm.isDealingTask()) {
                        commentForm.setNodeName(String.format("%s(%s)", copyForm.getNodeName(), commentForm.getNodeName()));
                        if (StringUtils.isNotBlank(commentForm.getTaskAssignee())) newList.add(commentForm);
                    }
                }
                convertListToVo(newList, new ArrayList<>(), form2);
            }
        }
    }

    /**
     * 过滤重复的节点，合并多工作项
     *
     * @param nodeList
     * @param resultNodeList
     */
    private void filterNode(List<BpmHistoryCommentFormVo> nodeList, List<BpmHistoryCommentFormVo> resultNodeList) {
        if (nodeList.size() > 1) {
            for (int i = 0; i < nodeList.size() - 1; ) {
                List<BpmHistoryCommentFormVo> childNode = new ArrayList<>();
                BpmHistoryCommentFormVo temp = new BpmHistoryCommentFormVo();
                BpmHistoryCommentFormVo voi = nodeList.get(i);
                BeanUtils.copyProperties(voi, temp);
                resultNodeList.add(temp);
                for (int j = i + 1; j < nodeList.size(); j++) {
                    i = j;
                    BpmHistoryCommentFormVo voj = nodeList.get(j);
                    if (voi.getNodeName().equals(voj.getNodeName())) {
                        childNode.add(voj);
                    } else {
                        if (i == nodeList.size() - 1) {
                            resultNodeList.add(voj);
                        }
                        break;
                    }
                }
                if (childNode.size() > 0) {
                    childNode.add(voi);
                    temp.setChildNode(childNode);
                    temp.setIsMultiTaskNode("1");
                }
            }
        } else if (nodeList.size() > 0) {
            resultNodeList.add(nodeList.get(0));
        }
    }

    private void convertToTree(List<BpmHistoryCommentFormVo> parentNodeList, String applyinstId) throws Exception {
        if (parentNodeList != null && parentNodeList.size() > 0) {
            for (BpmHistoryCommentFormVo bpmHistoryCommentFormVo : parentNodeList) {
                if (bpmHistoryCommentFormVo.getChildNode() == null) {
                    List<BpmHistoryCommentFormVo> childNodeList = listHistoryCommentByTaskId(applyinstId, bpmHistoryCommentFormVo);
                    if (childNodeList != null && childNodeList.size() > 0) {
                        convertToTree(childNodeList, applyinstId);
                    }
                }
            }
        }
    }

    private void addApproverToHistoryComment(List<BpmHistoryCommentFormVo> nodeList) {
        if (nodeList != null) {
            for (BpmHistoryCommentFormVo node : nodeList) {
                /*if (isPartOfOrg(node.getOrgId())) {
                    node.setIsApprover("1");
                }*/
                addApproverToHistoryComment(node.getChildNode());
            }
        }
    }

    private void sortDgListHistoryCommentBySigeInDate(List<BpmHistoryCommentFormVo> list) {
        Collections.sort(list, (arg0, arg1) -> {
            if (arg0.getEndDate() != null && arg1.getEndDate() == null) {
                return -1;
            }
            if (arg0.getEndDate() == null && arg1.getEndDate() != null) {
                return 1;
            }
            if (arg0.getEndDate() != null && arg1.getEndDate() != null) {
                return (arg0.getEndDate()).compareTo((arg1.getEndDate()));
            }
            return (arg0.getSigeInDate()).compareTo((arg1.getSigeInDate()));
        });
    }
}
