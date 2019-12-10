package com.augurit.aplanmis.front.approve.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.augurit.agcloud.bpm.common.domain.*;
import com.augurit.agcloud.bpm.common.engine.BpmTaskService;
import com.augurit.agcloud.bpm.common.mapper.ActStoAppinstMapper;
import com.augurit.agcloud.bpm.common.mapper.ActStoAppinstSubflowMapper;
import com.augurit.agcloud.bpm.common.mapper.ActTplAppFlowdefMapper;
import com.augurit.agcloud.bpm.common.mapper.ActTplAppMapper;
import com.augurit.agcloud.bpm.common.service.ActStoAppinstService;
import com.augurit.agcloud.bpm.common.service.ActStoAppinstSubflowService;
import com.augurit.agcloud.bpm.front.service.BpmProcessFrontService;
import com.augurit.agcloud.bpm.front.vo.ExtendBpmHistoryCommentForm;
import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.sc.att.service.IBscAttService;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.domain.OpuOmUser;
import com.augurit.agcloud.opus.common.mapper.OpuOmOrgMapper;
import com.augurit.agcloud.opus.common.mapper.OpuOmUserMapper;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.instance.AeaHiItemCorrectService;
import com.augurit.aplanmis.common.service.instance.AeaLogApplyStateHistService;
import com.augurit.aplanmis.common.service.item.AeaLogItemStateHistService;
import com.augurit.aplanmis.common.vo.MatinstVo;
import com.augurit.aplanmis.common.vo.SupplyOrSpacialCommentVo;
import com.augurit.aplanmis.front.approve.vo.BpmHistoryCommentFormVo;
import com.augurit.aplanmis.front.approve.vo.HistoryCommentsVo;
import org.apache.commons.lang.StringUtils;
import org.flowable.engine.HistoryService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class RestBpmService {
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
    private OpuOmUserMapper opuOmUserMapper;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private ActStoAppinstSubflowMapper actStoAppinstSubflowMapper;
    @Autowired
    private ActStoAppinstSubflowService actStoAppinstSubflowService;
    @Autowired
    private ActStoAppinstService actStoAppinstService;
    @Autowired
    private ApproveService approveService;
    @Autowired
    private ActTplAppFlowdefMapper actTplAppFlowdefService;
    @Autowired
    private ActTplAppMapper actTplAppService;

    @Autowired
    private BpmProcessFrontService bpmProcessFrontService;

    @Autowired
    private AeaLogItemStateHistService aeaLogItemStateHistService;

    @Autowired
    private AeaLogApplyStateHistService aeaLogApplyStateHistService;

    @Autowired
    private AeaHiItemCorrectService aeaHiItemCorrectService;
    @Autowired
    private AeaHiItemCorrectDueIninstMapper aeaHiItemCorrectDueIninstMapper;
    @Autowired
    private AeaHiItemCorrectRealIninstMapper aeaHiItemCorrectRealIninstMapper;
    @Autowired
    private AeaHiItemCorrectStateHistMapper aeaHiItemCorrectStateHistMapper;
    @Autowired
    private AeaHiItemInoutinstMapper aeaHiItemInoutinstMapper;
    @Autowired
    private AeaHiItemMatinstMapper aeaHiItemMatinstMapper;


    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public List<BpmHistoryCommentFormVo> listHistoryCommentByIteminstId(String iteminstId) {
        List<BpmHistoryCommentFormVo> result;
        try {
            AeaHiIteminst aeaHiIteminst = aeaHiIteminstMapper.getAeaHiIteminstById(iteminstId);
            if (aeaHiIteminst != null) {
                String procinstId = aeaHiIteminst.getProcinstId();
                result = listHistoryComment(procinstId, false, null);
            } else {
                throw new RuntimeException("查询不到事项实例");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
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
        String topOrgId = SecurityContext.getCurrentOrgId();
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
                                String iteminstId = approveService.getIteminstIdByTaskId(taskId);
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

    private void addApproverToHistoryComment(List<BpmHistoryCommentFormVo> nodeList) {
        if (nodeList != null) {
            for (BpmHistoryCommentFormVo node : nodeList) {
                if (isPartOfOrg(node.getOrgId())) {
                    node.setIsApprover("1");
                }
                addApproverToHistoryComment(node.getChildNode());
            }
        }
    }

    /**
     * 判断登录用户部门是否属于某个事项部门
     */
    private boolean isPartOfOrg(String orgId) {
        List<OpuOmOrg> opuOmOrgs = opuOmOrgMapper.listBelongOrgByUserId(SecurityContext.getCurrentUserId());
        boolean bsign = false;
        if (opuOmOrgs != null && opuOmOrgs.size() > 0) {
            for (OpuOmOrg opuOmOrg : opuOmOrgs) {
                String secOrgSeq = opuOmOrg.getOrgSeq();
                if (secOrgSeq != null && orgId != null) {
                    //判断登录用户部门是否属于某个事项部门
                    if (secOrgSeq.indexOf(orgId) > 0) {
                        bsign = true;
                        break;
                    }
                }
            }
        }
        return bsign;
    }

    private String getTopParentProcess(String processInstanceId) throws Exception {
        String parentProcessId = this.getParentProcessByProcessInstanceId(processInstanceId);
        if (StringUtils.isNotBlank(parentProcessId)) {
            return getTopParentProcess(parentProcessId);
        } else {
            return processInstanceId;
        }
    }

    private String getParentProcessByProcessInstanceId(String processInstanceId) throws Exception {
        ActStoAppinstSubflow appinstSubflow = actStoAppinstSubflowService.getActStoAppinstSubflowBySubflowProcinstId(processInstanceId);
        if (appinstSubflow != null && appinstSubflow.getParentSubflowId() != null) {
            ActStoAppinstSubflow parentSubflow = actStoAppinstSubflowService.getActStoAppinstSubflowById(appinstSubflow.getParentSubflowId());
            if (parentSubflow != null) {
                return parentSubflow.getSubflowProcinstId();
            }
        } else {
            if (appinstSubflow != null) {
                appinstSubflow.getParentSubflowId();
                ActStoAppinst actStoAppinst = actStoAppinstService.getActStoAppinstById(appinstSubflow.getAppinstId());
                if (actStoAppinst != null) {
                    return actStoAppinst.getProcinstId();
                }
            }
        }
        return null;
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

    public List<BpmHistoryCommentFormVo> listHistoryCommentAll(String processInstanceId, String isUnit, String applyinstId) throws Exception {
        List<BpmHistoryCommentFormVo> treeList = this.listHistoryCommentTree(processInstanceId, true, applyinstId);
        List<BpmHistoryCommentFormVo> resultList = new ArrayList<BpmHistoryCommentFormVo>();
        for (BpmHistoryCommentFormVo bpmHistoryCommentForm : treeList) {
            if (bpmHistoryCommentForm.getChildNode() == null || bpmHistoryCommentForm.getChildNode().size() == 0) {//一级节点
                bpmHistoryCommentForm.setOtherCommentList(this.getSupplyOrSpacialCommentByTaskId(bpmHistoryCommentForm.getTaskId(), applyinstId));
                resultList.add(bpmHistoryCommentForm);
            } else {
                List<BpmHistoryCommentFormVo> childList = bpmHistoryCommentForm.getChildNode();
                // 单项不再仅限于二级，兼容单项并联的多级
                convertListToVo(childList, resultList, bpmHistoryCommentForm);
            }
        }

        /*修改之前意见列表只能显示到三级流程的问题
         * 递归之前结果集
         */
        List resultLast = new ArrayList<>();
        for (BpmHistoryCommentFormVo vo : resultList) {
            if (vo.getChildNode() != null) {
                resultRset(vo, resultLast);
            } else {
                resultLast.add(vo);
            }
        }
        /*修改之前意见列表只能显示到三级流程的问题*/

        return treeList;
    }

    public List<SupplyOrSpacialCommentVo> getSupplyOrSpacialCommentByTaskId(String taskInstId, String applyinstId) {
        List<SupplyOrSpacialCommentVo> list = new ArrayList<>();
        //特殊程序---事项维度
        List<SupplyOrSpacialCommentVo> spacialCommentList = aeaLogItemStateHistService.findSpacialAeaLogItemStateHist(taskInstId, SecurityContext.getCurrentOrgId());
        //补全---申报维度
        List<SupplyOrSpacialCommentVo> applyinstCorrectCommentList = aeaLogApplyStateHistService.findApplyinstCorrectStateHist(applyinstId, taskInstId, SecurityContext.getCurrentOrgId());
        //补正
        List<SupplyOrSpacialCommentVo> itemCorrectCommentList = aeaLogItemStateHistService.findItemCorrectStateHist(taskInstId, SecurityContext.getCurrentOrgId());
        list.addAll(spacialCommentList);
        list.addAll(applyinstCorrectCommentList);
        list.addAll(itemCorrectCommentList);
        return list.size() > 0 ? list.stream().sorted(Comparator.comparing(SupplyOrSpacialCommentVo::getEndDate).reversed()).collect(Collectors.toList()) : list;
    }

    /*
     * 工具，拿list最后一个节点数据,如果登陆用户属于该部门，则需要展示该部门的所有节点
     */
    private void convertListToVo(List<BpmHistoryCommentFormVo> oldList, List<BpmHistoryCommentFormVo> newList, BpmHistoryCommentFormVo copyForm) {
        for (BpmHistoryCommentFormVo form2 : oldList) {//2级菜单
            if (form2.getChildNode() == null || form2.getChildNode().size() == 0) {
                form2.setNodeName(String.format("%s(%s)", copyForm.getNodeName(), form2.getNodeName()));
                form2.setOtherCommentList(this.getSupplyOrSpacialCommentByTaskId(form2.getTaskId(), ""));
                newList.add(form2);
            } else {
                List<BpmHistoryCommentFormVo> childNodeList = form2.getChildNode();
                if (childNodeList != null && childNodeList.size() > 0) {
                    boolean flag = true;
                    // 先根据办理时间排序，然后根据部门名称排序
                    sortDgListHistoryCommentByOrgName(childNodeList);
                    for (BpmHistoryCommentFormVo commentForm : childNodeList) {
                        String orgId = commentForm.getOrgId();
                        boolean bool = isPartOfOrg(orgId);
                        if (bool || commentForm.isDealingTask()) {
                            flag = false;
                            commentForm.setNodeName(String.format("%s(%s)", copyForm.getNodeName(), commentForm.getNodeName()));
                            commentForm.setOtherCommentList(this.getSupplyOrSpacialCommentByTaskId(commentForm.getTaskId(), ""));
                            newList.add(commentForm);
                        }
                    }
                    if (flag) {
                        BpmHistoryCommentFormVo commentForm = childNodeList.get(childNodeList.size() - 1);
                        commentForm.setNodeName(String.format("%s(%s)", copyForm.getNodeName(), commentForm.getNodeName()));
                        commentForm.setOtherCommentList(this.getSupplyOrSpacialCommentByTaskId(commentForm.getTaskId(), ""));
                        newList.add(commentForm);
                    }
                }
            }
        }
    }

    private void sortDgListHistoryCommentByOrgName(List<BpmHistoryCommentFormVo> list) {
        Collections.sort(list, (arg0, arg1) -> {

            if (arg0.getOrgId() != null && arg1.getOrgId() == null) {
                return -1;
            }
            if (arg0.getOrgId() == null && arg1.getOrgId() != null) {
                return 1;
            }
            if (arg0.getOrgId() != null && arg1.getOrgId() != null) {
                return (arg0.getOrgId()).compareTo((arg1.getOrgId()));
            }
            return 0;
        });
    }

    // 修改审批意见列表只能显示到三级流程，四级流程审批人和部门不显示的问题
    public List<BpmHistoryCommentFormVo> resultRset(BpmHistoryCommentFormVo vo, List resultList) {
        List<BpmHistoryCommentFormVo> list = new ArrayList<>();
        if (vo.getChildNode() != null) {
            for (BpmHistoryCommentFormVo bmVo : vo.getChildNode()) {
                resultRset(bmVo, resultList);
            }
            vo.getChildNode();
        } else {
            resultList.add(vo);
        }
        return resultList;
    }

    public Map<String, Object> getActFlowParam(String appFlowdefId) throws Exception {
        ActTplAppFlowdef appFlowdef = actTplAppFlowdefService.getActTplAppFlowdefById(appFlowdefId);
        ActTplApp temp = actTplAppService.getActTplAppById(appFlowdef.getAppId());
        Map<String, Object> map = new HashMap<>();
        map.put("appCode", temp.getAppCode());
        map.put("appComment", temp.getAppComment());
        map.put("procdefKey", appFlowdef.getProcdefKey());
        if ("jointReviewTpl".equals(temp.getAppCode())) {
            map.put("_tableName", "COD_PROJECT_ITEM");
            map.put("_pkName", "ID");
        }
        return map;
    }


    //4.0审批页右边审批意见列表--支持多级-小糊涂 20190926
    public List<HistoryCommentsVo> commentList(String processInstanceId, String applyinstId) throws Exception {

        List<ExtendBpmHistoryCommentForm> historyCommentTree = bpmProcessFrontService.getHistoryCommentTree(processInstanceId, true);
        List<HistoryCommentsVo> voList = new ArrayList<>();
        //一级节点下的所有树叶节点
        List<HistoryCommentsVo> lastNodeList = new ArrayList<>();
        for (ExtendBpmHistoryCommentForm firstNode : historyCommentTree) {
            String nodeName = firstNode.getNodeName();//一级节点名称
            List<ExtendBpmHistoryCommentForm> secondNodeList = firstNode.getChildNode();//二级节点列表
            if (null != secondNodeList && secondNodeList.size() > 0) {
                for (ExtendBpmHistoryCommentForm secondNode : secondNodeList) {
                    getLastLeafList(secondNode, firstNode, secondNode, lastNodeList);

                }
                HistoryCommentsVo vo = new HistoryCommentsVo();
                vo.setNodeName(nodeName);
                vo.setHistoryCommentsVos(lastNodeList);
                voList.add(vo);

            } else {//不存在二级节点
                changeAddFormToVo(voList, firstNode, nodeName);
            }
        }
        List<HistoryCommentsVo> commentsVos = combineSameNode(voList);
        formatSeriesPartList(commentsVos, applyinstId);
        return commentsVos;
    }

    //格式化单项部门审批意见，增加事项名称节点
    private void formatSeriesPartList(List<HistoryCommentsVo> voList, String applyinstId) throws Exception {
        AeaHiApplyinst applyinst = aeaHiApplyinstMapper.getAeaHiApplyinstById(applyinstId);
        if (null != applyinst && "1".equals(applyinst.getIsSeriesApprove())) {
            //获取单事项名称
            List<AeaHiIteminst> aeaHiIteminsts = aeaHiIteminstMapper.listAllAeaHiIteminstByApplyinstId(applyinstId, SecurityContext.getCurrentOrgId());
            if (aeaHiIteminsts.size() > 0) {
                AeaHiIteminst iteminst = aeaHiIteminsts.get(0);
                for (HistoryCommentsVo vo : voList) {
                    String nodeName = vo.getNodeName();
                    if (nodeName.contains("部门审批")) {
                        List<HistoryCommentsVo> historyCommentsVos = new ArrayList<>();

                        List<HistoryCommentsVo.Comment> commentList = vo.getCommentList();

                        HistoryCommentsVo votemp = new HistoryCommentsVo();
                        votemp.setNodeName(iteminst.getIteminstName());
                        votemp.setItem(true);
                        votemp.setCommentList(commentList);
                        historyCommentsVos.add(votemp);
                        vo.setHistoryCommentsVos(historyCommentsVos);
                        vo.setCommentList(null);

                    }
                }
            }
        }
    }

    private List<HistoryCommentsVo> combineSameNode(List<HistoryCommentsVo> oldList) {
        List<HistoryCommentsVo> voList = new ArrayList<>();
        Map<String, List<HistoryCommentsVo.Comment>> map = new HashMap<>();
        Map<String, Boolean> itemMap = new HashMap<>();
        Set<String> set = new HashSet<>();
        for (HistoryCommentsVo vo : oldList) {
            List<HistoryCommentsVo> historyCommentsVos = vo.getHistoryCommentsVos();

            if (null == historyCommentsVos) {
                //只有一级节点
                voList.add(vo);
            } else {

                HistoryCommentsVo newVo = new HistoryCommentsVo();
                String firstNodeName = vo.getNodeName();
                //boolean item = vo.getItem();
                newVo.setNodeName(firstNodeName);
                newVo.setItem(false);
                for (HistoryCommentsVo secondVo : historyCommentsVos) {
                    String nodeName = secondVo.getNodeName();
                    List<HistoryCommentsVo.Comment> commentList = secondVo.getCommentList();
                    if (set.contains(nodeName)) {
                        List<HistoryCommentsVo.Comment> comments = map.get(nodeName);
                        comments.addAll(commentList);
                        map.put(nodeName, comments);
                    } else {//
                        set.add(nodeName);
                        map.put(nodeName, commentList);
                        itemMap.put(nodeName, secondVo.getItem());
                    }
                }
                List<HistoryCommentsVo> historyCommentsVos1 = new ArrayList<>();
                for (Map.Entry<String, List<HistoryCommentsVo.Comment>> entry : map.entrySet()) {
                    String node = entry.getKey();
                    List<HistoryCommentsVo.Comment> comments = entry.getValue();
                    //排序意见---根据时间
                    comments = comments.stream().sorted(Comparator.comparing(HistoryCommentsVo.Comment::getSigeInDate).reversed()).collect(Collectors.toList());

                    HistoryCommentsVo tempVo = new HistoryCommentsVo();
                    tempVo.setItem(itemMap.get(node));
                    tempVo.setNodeName(node);
                    tempVo.setCommentList(comments);
                    historyCommentsVos1.add(tempVo);
                }

                //根据节点类型排序
                historyCommentsVos1 = historyCommentsVos1.stream().sorted((o1, o2) -> {
                    boolean item = o1.getItem();
                    boolean item1 = o2.getItem();
                    if (item ^ item1) {
                        return item ? -1 : 1;
                    } else {
                        return 0;
                    }
                }).collect(Collectors.toList());

                newVo.setHistoryCommentsVos(historyCommentsVos1);
                voList.add(newVo);
            }

        }

        return voList;
    }

    //获取叶子节点数据
    private void getLastLeafList(ExtendBpmHistoryCommentForm secondNode, ExtendBpmHistoryCommentForm parentNode, ExtendBpmHistoryCommentForm currentNode, List<HistoryCommentsVo> lastNodeList) {//从三级级节点开始递归获取，保证二级节点存在
        List<ExtendBpmHistoryCommentForm> childNode = currentNode.getChildNode();

        if (null == childNode || childNode.size() == 0) {//不存在下级节点
            HistoryCommentsVo vo = new HistoryCommentsVo();
            String nodeName = parentNode.getNodeName();
            vo.setNodeName(nodeName);
            if (nodeName.contains("部门审批")) {
                //替换为节点名称
                vo.setItem(false);
                vo.setNodeName(currentNode.getNodeName());
            } else {
                vo.setItem(true);
            }
            HistoryCommentsVo.Comment comment = HistoryCommentsVo.changeToComments(currentNode);

            List<HistoryCommentsVo.Comment> commentList = new ArrayList<>();
            commentList.add(comment);
            vo.setCommentList(commentList);
            lastNodeList.add(vo);
        } else {
            for (ExtendBpmHistoryCommentForm form : childNode) {
                getLastLeafList(secondNode, currentNode, form, lastNodeList);
            }

        }


    }

    private void changeAddFormToVo(List<HistoryCommentsVo> voList, ExtendBpmHistoryCommentForm form, String nodeName) {
        HistoryCommentsVo.Comment comment = new HistoryCommentsVo.Comment();
        BeanUtils.copyProperties(form, comment);
        String isApprover = form.getIsApprover();
        Date sigeInDate = form.getSigeInDate();
        String format = this.format.format(sigeInDate);
        comment.setStartDate(format);
        if (com.augurit.agcloud.framework.util.StringUtils.isNotBlank(isApprover) && "1".equals(isApprover)) {
            comment.setIsPass(true);
        } else {
            comment.setIsPass(false);
        }
        List<HistoryCommentsVo.Comment> commentList = new ArrayList<>();
        commentList.add(comment);
        HistoryCommentsVo vo = new HistoryCommentsVo();
        vo.setCommentList(commentList);
        vo.setNodeName(nodeName);
        voList.add(vo);
    }

    public List itemSupplementRecord(String iteminstId) throws Exception {
        String currentOrgId = SecurityContext.getCurrentOrgId();
        List result = new ArrayList();
        //先查询所有的状态信息
        AeaLogItemStateHist query = new AeaLogItemStateHist();
        query.setIteminstId(iteminstId);
        List<AeaLogItemStateHist> aeaLogItemStateHist = aeaLogItemStateHistService.findAeaLogItemStateHist(query);
        //只遍历补正相关的信息
        for (AeaLogItemStateHist log : aeaLogItemStateHist) {
            if ("6".equals(log.getNewState())) {
                SupplyOrSpacialCommentVo vo = new SupplyOrSpacialCommentVo();
                List matList = new ArrayList();
                //基本信息
                vo.setId(log.getStateHistId());
                vo.setStartDate(format.format(log.getTriggerTime()));
                vo.setCommentMessage(log.getOpsUserOpinion());
                vo.setMan(log.getOpsUserName());
                //节点信息
                if (StringUtils.isNotBlank(log.getTaskinstId())) {
                    HistoricTaskInstance task = historyService.createHistoricTaskInstanceQuery().taskId(log.getTaskinstId()).singleResult();
                    if (task != null) {
                        vo.setNodeName(task.getName());
                    }
                }
                //材料信息
                AeaHiItemCorrectStateHist query2 = new AeaHiItemCorrectStateHist();
                query2.setStateHistId(log.getStateHistId());
                List<AeaHiItemCorrectStateHist> aeaHiItemCorrectStateHists = aeaHiItemCorrectStateHistMapper.listAeaHiItemCorrectStateHist(query2);
                if (aeaHiItemCorrectStateHists.size() > 0) {
                    String correctId = aeaHiItemCorrectStateHists.get(0).getCorrectId();
                    AeaHiItemCorrect aeaHiItemCorrect = aeaHiItemCorrectService.getAeaHiItemCorrectById(correctId);
                    vo.setSignState(swithSignState(aeaHiItemCorrect.getCorrectState()));
                    if (aeaHiItemCorrect.getCorrectEndTime() != null) {
                        vo.setEndDate(format.format(aeaHiItemCorrect.getCorrectEndTime()));
                    }
                    List<AeaHiItemCorrectDueIninst> correctDueIninst = aeaHiItemCorrectDueIninstMapper.getCorrectDueIninstByCorrectId(correctId, currentOrgId);
                    List<AeaHiItemCorrectRealIninst> correctRealIninst = aeaHiItemCorrectRealIninstMapper.getCorrectRealIninstByCorrectId(correctId, currentOrgId);
                    vo.setMatNum(correctDueIninst.size());
                    for (AeaHiItemCorrectDueIninst ininst : correctDueIninst) {
                        AeaHiItemInoutinst aeaHiItemInoutinst = aeaHiItemInoutinstMapper.getAeaHiItemInoutinstById(ininst.getInoutinstId());
                        if (aeaHiItemInoutinst != null) {
                            MatinstVo mvo = new MatinstVo();
                            AeaHiItemMatinst aeaHiItemMatinst = aeaHiItemMatinstMapper.getAeaHiItemMatinstById(aeaHiItemInoutinst.getMatinstId());
                            if (aeaHiItemMatinst != null) {
                                mvo.setMatName(aeaHiItemMatinst.getMatinstName());
                                mvo.setAttMatinstId(aeaHiItemMatinst.getMatinstId());
                                mvo.setDueCopyCount(ininst.getCopyCount());
                                mvo.setDuePaperCount(ininst.getPaperCount());
                                for (AeaHiItemCorrectRealIninst realIninst : correctRealIninst) {
                                    if (realIninst.getInoutinstId().equals(ininst.getInoutinstId())) {
                                        mvo.setRealCopyCount(realIninst.getRealCopyCount());
                                        mvo.setRealPaperCount(realIninst.getRealPaperCount());
                                        mvo.setAttCount(realIninst.getAttCount());
                                    }
                                }
                                matList.add(mvo);
                            }
                        }
                        vo.setMatinstList(matList);
                    }
                    result.add(vo);
                }
            }
        }
        return result;
    }

    private String swithSignState(String state) {
        // 6表示待补正，7表示已补正（待确认），8表示已补正（已确认，材料齐全），5表示不予受理'
        switch (state) {
            case "6":
                return "待补正";
            case "7":
                return "已补正（待确认）";
            case "8":
                return "已补正（已确认，材料齐全）";
            case "5":
                return "不予受理";
            default:
                return state;
        }
    }

    /**
     * 根据父流程的任务实例id，查询当前节点下触发的子流程节点的审批人列表，没有则返回null
     *
     * @param taskId
     * @return
     */
    public List getSubFlowAssignee(String taskId) {
        List result = null;
        try {
            //1、通过taskId查询其触发的子流程
            ActStoAppinstSubflow query = new ActStoAppinstSubflow();
            query.setTriggerTaskinstId(taskId);
            query.setRootOrgId(SecurityContext.getCurrentOrgId());
            List<ActStoAppinstSubflow> actStoAppinstSubflows = actStoAppinstSubflowService.listActStoAppinstSubflow(query);
            if (actStoAppinstSubflows.size() > 0) {
                result = new ArrayList();
                //2、遍历子流程，查询出所有的任务实例
                for (ActStoAppinstSubflow subflow : actStoAppinstSubflows) {
                    List<Task> list = taskService.createTaskQuery().processInstanceId(subflow.getSubflowProcinstId()).list();
                    for (Task task : list) {
                        //3、从任务实例中获取用户登录名，关联查出用户信息
                        String assignee = task.getAssignee();
                        OpuOmUser user = opuOmUserMapper.getUserByLoginName(assignee);
                        if (user != null) {
                            result.add(user);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取任务节点的审批意见，如果包含子流程，则获取子流程的意见，最多两级子流程
     **/
    public List getTaskComment(String taskId){
        List result = new ArrayList();
        HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        if(historicTaskInstance != null){
            String processInstanceId = historicTaskInstance.getProcessInstanceId();
            try {
                //当前节点的意见
                List<BpmHistoryCommentForm> historyComments = bpmTaskService.getHistoryCommentsByTaskId(processInstanceId, taskId);

                //子流程节点意见
                ActStoAppinstSubflow query = new ActStoAppinstSubflow();
                query.setTriggerTaskinstId(taskId);
                List<ActStoAppinstSubflow> actStoAppinstSubflows = actStoAppinstSubflowService.listActStoAppinstSubflow(query);
                if(actStoAppinstSubflows.size() > 0){
                    for(int i=0,len=actStoAppinstSubflows.size(); i<len; i++){
                        ActStoAppinstSubflow actStoAppinstSubflow = actStoAppinstSubflows.get(i);
                        query.setTriggerTaskinstId(null);
                        query.setParentSubflowId(actStoAppinstSubflow.getSubflowId());
                        List<ActStoAppinstSubflow> sSubflows = actStoAppinstSubflowService.listActStoAppinstSubflow(query);
                        if(sSubflows.size() > 0){
                            for(int j=0,lenj=sSubflows.size(); j<lenj; j++){
                                List<BpmHistoryCommentForm> temp = bpmTaskService.getHistoryCommentsByProcessInstanceId(sSubflows.get(j).getSubflowProcinstId());
                                result.addAll(temp);
                            }
                        }else{
                            List<BpmHistoryCommentForm> subHistoryComments = bpmTaskService.getHistoryCommentsByProcessInstanceId(actStoAppinstSubflow.getSubflowProcinstId());
                            result.addAll(subHistoryComments);
                        }
                    }
                }else{
                    result.addAll(historyComments);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 获取任务节点的前一个节点的审批意见，如果包含子流程，则获取子流程的意见，最多两级子流程
     * @param taskId
     * @return
     */
    public List getLastTaskComment(String taskId){
        HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        String taskDefinitionKey = historicTaskInstance.getTaskDefinitionKey();
        if(historicTaskInstance != null) {
            String processInstanceId = historicTaskInstance.getProcessInstanceId();
            List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).list();
            for(int i=list.size()-1; i>=1; i--){
                HistoricTaskInstance temp = list.get(i);
                HistoricTaskInstance temp1 = list.get(i - 1);
                if(temp.getId().equals(taskId) && temp1.getTaskDefinitionKey() != taskDefinitionKey){
                    return getTaskComment(temp1.getId());
                }
            }
        }
        return null;
    }

}
