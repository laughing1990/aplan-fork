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
import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.mapper.BscDicCodeMapper;
import com.augurit.agcloud.bsc.sc.att.service.IBscAttService;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.mapper.OpuOmOrgMapper;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.mapper.AeaHiApplyinstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiIteminstMapper;
import com.augurit.aplanmis.province.vo.HistoryProcessVo;
import com.augurit.aplanmis.province.vo.ParallelApproveDataVo;
import org.apache.commons.lang.StringUtils;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


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
                return listHistoryCommentAll(procInstIds.get(0), applyinst.getApplyinstId(), applyinst.getIteminstId());
            }
        }
        return new ArrayList<HistoryProcessVo>();
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

    private void sortDgListHistoryCommentBySigeInDate(List<HistoryProcessVo> list) {
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
}
