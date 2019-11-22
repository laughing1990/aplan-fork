package com.augurit.aplanmis.front.approve.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.bsc.domain.BscAttLink;
import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.mapper.BscAttMapper;
import com.augurit.agcloud.bsc.mapper.BscDicCodeMapper;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.domain.OpuOmUser;
import com.augurit.agcloud.opus.common.mapper.OpuOmOrgMapper;
import com.augurit.agcloud.opus.common.mapper.OpuOmUserMapper;
import com.augurit.aplanmis.common.constants.CertinstSource;
import com.augurit.aplanmis.common.constants.UnitType;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiItemInoutinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiItemMatinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiIteminstService;
import com.augurit.aplanmis.common.service.linkman.AeaLinkmanInfoService;
import com.augurit.aplanmis.common.service.mat.AeaItemMatService;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.augurit.aplanmis.front.approve.vo.AeaHiItemInfoVo;
import com.augurit.aplanmis.front.approve.vo.ApplyFormVo;
import com.augurit.aplanmis.front.approve.vo.BpmApproveStateVo;
import com.augurit.aplanmis.front.approve.vo.MatinstVo;
import org.apache.commons.lang.StringUtils;
import org.flowable.engine.HistoryService;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ApproveService {

    @Autowired
    private AeaHiApplyinstMapper aeaHiApplyinstMapper;
    @Autowired
    private BscDicCodeMapper bscDicCodeMapper;
    @Autowired
    private AeaProjInfoService aeaProjInfoService;
    @Autowired
    private AeaHiIteminstMapper aeaHiIteminstMapper;
    @Autowired
    private AeaHiParStageinstMapper aeaHiParStageinstMapper;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private OpuOmOrgMapper opuOmOrgMapper;
    @Autowired
    private AeaParStageMapper aeaParStageMapper;
    @Autowired
    private AeaHiParStateinstMapper aeaHiParStateinstMapper;
    @Autowired
    private AeaParStateMapper aeaParStateMapper;
    @Autowired
    private AeaHiItemStateinstMapper aeaHiItemStateinstMapper;
    @Autowired
    private AeaItemStateMapper aeaItemStateMapper;
    @Autowired
    private AeaHiIteminstService aeaHiIteminstService;
    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;
    @Autowired
    private AeaApplyinstProjMapper aeaApplyinstProjMapper;
    @Autowired
    private AeaProjInfoMapper aeaProjInfoMapper;
    @Autowired
    private AeaLinkmanInfoService aeaLinkmanInfoService;
    @Autowired
    private AeaUnitInfoService aeaUnitInfoService;
    @Autowired
    private AeaLinkmanInfoMapper aeaLinkmanInfoMapper;
    @Autowired
    private AeaItemMatService aeaItemMatService;
    @Autowired
    private FileUtilsService fileUtilsService;
    @Autowired
    private AeaProjLinkmanMapper aeaProjLinkmanMapper;
    @Autowired
    private AeaCreditSummaryMapper aeaCreditSummaryMapper;
    @Autowired
    private AeaHiApplyinstService aeaHiApplyinstService;
    @Autowired
    private AeaUnitProjLinkmanMapper aeaUnitProjLinkmanMapper;
    @Autowired
    private AeaHiItemSpecialMapper aeaHiItemSpecialMapper;
    @Autowired
    private AeaHiItemCorrectMapper aeaHiItemCorrectMapper;
    @Autowired
    private AeaHiItemMatinstMapper aeaHiItemMatinstMapper;
    @Autowired
    private AeaItemMatMapper aeaItemMatMapper;
    @Autowired
    private AeaUnitProjMapper aeaUnitProjMapper;
    @Autowired
    private AeaHiItemInoutinstService aeaHiItemInoutinstService;
    @Autowired
    private AeaHiItemMatinstService aeaHiItemMatinstService;
    @Autowired
    private BscAttMapper bscAttMapper;
    @Autowired
    private OpuOmUserMapper opuOmUserMapper;
    @Autowired
    private AeaHiCertinstMapper aeaHiCertinstMapper;
    @Autowired
    private AeaCertMapper aeaCertMapper;
    @Autowired
    private AeaHiItemInoutinstMapper aeaHiItemInoutinstMapper;

    public BpmApproveStateVo getApplyStylesAndState(String applyinstId, String taskId) throws Exception {
        BpmApproveStateVo bpmApproveStateVo = new BpmApproveStateVo();
        //1、申请状态
        AeaHiApplyinst applyInst = aeaHiApplyinstMapper.getAeaHiApplyinstById(applyinstId);

        if (applyInst == null) {
            throw new RuntimeException("查询不到申请实例");
        }
        bpmApproveStateVo.setApplyStyle(applyInst.getApplyinstSource());
        bpmApproveStateVo.setIsSeriesinst(applyInst.getIsSeriesApprove());

        List<AeaProjInfo> aeaProjInfos = aeaProjInfoService.findApplyProj(applyinstId);
        if (aeaProjInfos.size() > 0) {
            String projName = aeaProjInfos.stream().map(AeaProjInfo::getProjName).collect(Collectors.joining("、"));
            String projCode = aeaProjInfos.stream().map(AeaProjInfo::getLocalCode).collect(Collectors.joining("、"));
            bpmApproveStateVo.setProjName(projName);
            bpmApproveStateVo.setProjCode(projCode);
        }
        String isApprove = applyInst.getIsSeriesApprove();
        //2、审批状态
        List<AeaHiIteminst> iteminstList = aeaHiIteminstMapper.getAeaHiIteminstListByApplyinstId(applyinstId);
        if (iteminstList == null || iteminstList.size() == 0) {
            throw new RuntimeException("iteminstList is null");
        }

        String currentState = "";
        String currentIteminstId = null;
        //串联
        if ("1".equals(isApprove)) {
            AeaHiIteminst iteminst = iteminstList.get(0);
            if (iteminst != null) {
                currentIteminstId = iteminst.getIteminstId();
                bpmApproveStateVo.setStageOrItemName(iteminst.getIteminstName());
                String code = iteminst.getIteminstState();
                if (StringUtils.isNotBlank(code)) {
                    BscDicCodeItem dic = bscDicCodeMapper.getItemByTypeCodeAndItemCodeAndOrgId("ITEMINST_STATE", code, SecurityContext.getCurrentOrgId());
                    if (dic != null) {
                        currentState = dic.getItemName();
                    }
                }
            }

        } else {//并联
            AeaHiParStageinst parStageinst = new AeaHiParStageinst();
            parStageinst.setApplyinstId(applyinstId);
            parStageinst.setRootOrgId(SecurityContext.getCurrentOrgId());
            List<AeaHiParStageinst> list = aeaHiParStageinstMapper.listAeaHiParStageinst(parStageinst);
            if (list != null && list.size() > 0) {
                AeaHiParStageinst aeaHiParStageinst = list.get(0);
                if (aeaHiParStageinst != null) {
                    AeaParStage aeaParStage = aeaParStageMapper.getAeaParStageById(aeaHiParStageinst.getStageId());
                    String stageName = aeaParStage.getStageName();
                    if (!stageName.endsWith("阶段")) {
                        stageName = stageName + "阶段";
                    }
                    bpmApproveStateVo.setStageOrItemName(stageName);
                    bpmApproveStateVo.setIsShowOneForm(aeaParStage.getUseOneForm());
                    String code = "";
                    if (StringUtils.isNotBlank(taskId)) {
                        String iteminstId = this.getIteminstIdByTaskId(taskId);
                        //审批人员
                        currentIteminstId = iteminstId;
                        if (StringUtils.isNotBlank(iteminstId)) {

                            bpmApproveStateVo.setIsApprover("1");
//                            if (StringUtils.isNotBlank(iteminstId)) {
                            AeaHiIteminst iteminst = aeaHiIteminstMapper.getAeaHiIteminstById(iteminstId);
                            if (iteminst != null) {
                                code = iteminst.getIteminstState();
                                bpmApproveStateVo.setCurrentStateValue(iteminst.getIteminstState());
                            }
//                            } else {
//                                code = iteminstList.get(0).getIteminstState();
//                            }
                            if (StringUtils.isNotBlank(code)) {
                                BscDicCodeItem dic = bscDicCodeMapper.getItemByTypeCodeAndItemCodeAndOrgId("ITEMINST_STATE", code, SecurityContext.getCurrentOrgId());
                                if (dic != null) {
                                    currentState = dic.getItemName();
                                }
                            }

                        } else {  //窗口人员
                            String applyinstState = applyInst.getApplyinstState();
                            if (StringUtils.isNotBlank(applyinstState)) {
                                bpmApproveStateVo.setIsApprover("0");
                                bpmApproveStateVo.setCurrentStateValue(applyinstState);
                                BscDicCodeItem dic = bscDicCodeMapper.getItemByTypeCodeAndItemCodeAndOrgId("APPLYINST_STATE", applyinstState, SecurityContext.getCurrentOrgId());
                                if (dic != null) {
                                    currentState = dic.getItemName();
                                }
                            }
                        }

                    }
                    //阶段和协同完成状态判断
                    fillXietongState(bpmApproveStateVo, aeaParStage, aeaProjInfos);
                }
            }
        }
        bpmApproveStateVo.setCurrentState(currentState);
        //增加是否发起补正和特殊程序状态
        if (StringUtils.isNotBlank(currentIteminstId)) {
            AeaHiItemCorrect query = new AeaHiItemCorrect();
            query.setIteminstId(currentIteminstId);
            List<AeaHiItemCorrect> aeaHiItemCorrects = aeaHiItemCorrectMapper.listAeaHiItemCorrect(query);
            if (aeaHiItemCorrects.size() > 0) {
                bpmApproveStateVo.setHasSupply("1");
            }
            List<AeaHiItemSpecial> aeaHiItemSpecials = aeaHiItemSpecialMapper.getAeaHiItemSpecialByIteminstId(currentIteminstId);
            if (aeaHiItemSpecials.size() > 0) {
                bpmApproveStateVo.setHasSpecial("1");
            }
            for (AeaHiItemSpecial special : aeaHiItemSpecials) {
                if ("9".equals(special.getSpecialState())) {
                    bpmApproveStateVo.setShowSpecialBtn("0");
                    break;
                }
            }
        } else {
            AeaHiItemCorrect query = new AeaHiItemCorrect();
            query.setApplyinstId(applyinstId);
            List<AeaHiItemCorrect> aeaHiItemCorrects = aeaHiItemCorrectMapper.listAeaHiItemCorrect(query);
            if (aeaHiItemCorrects.size() > 0) {
                bpmApproveStateVo.setHasSupply("1");
            }
            AeaHiItemSpecial query2 = new AeaHiItemSpecial();
            query2.setApplyinstId(applyinstId);
            List<AeaHiItemSpecial> aeaHiItemSpecials = aeaHiItemSpecialMapper.listAeaHiItemSpecial(query2);
            if (aeaHiItemSpecials.size() > 0) {
                bpmApproveStateVo.setHasSpecial("1");
            }
        }

        return bpmApproveStateVo;
    }

    private void fillXietongState(BpmApproveStateVo bpmApproveStateVo, AeaParStage stage, List<AeaProjInfo> aeaProjInfos) {
        //协同阶段
        List<AeaParStage> aeaParStages = null;
        if ("1".equals(stage.getIsNode())) {
            //当前主线阶段
            AeaParStage query1 = new AeaParStage();
            query1.setParentId(stage.getStageId());
            aeaParStages = aeaParStageMapper.listAeaParStage(query1);
        } else {
            //当前是辅线/技术审查
            AeaParStage query1 = new AeaParStage();
            query1.setParentId(stage.getParentId());
            aeaParStages = aeaParStageMapper.listAeaParStage(query1);
        }
        if (aeaParStages != null && aeaParStages.size() > 0) {
            int index = 0;
            try {
                for (AeaParStage aeaParStage : aeaParStages) {
                    //遍历每个协同阶段的完成情况
                    for (AeaProjInfo projInfo : aeaProjInfos) {
                        AeaApplyinstProj query = new AeaApplyinstProj();
                        query.setProjInfoId(projInfo.getProjInfoId());
                        List<AeaApplyinstProj> aeaApplyinstProjs = aeaApplyinstProjMapper.listAeaApplyinstProj(query);
                        if (aeaApplyinstProjs != null) {
                            for (AeaApplyinstProj aeaApplyinstProj : aeaApplyinstProjs) {
                                AeaHiParStageinst query2 = new AeaHiParStageinst();
                                query2.setApplyinstId(aeaApplyinstProj.getApplyinstId());
                                query2.setStageId(aeaParStage.getStageId());
                                List<AeaHiParStageinst> aeaHiParStageinsts = aeaHiParStageinstMapper.listAeaHiParStageinst(query2);
                                if (aeaHiParStageinsts.size() > 0) {
                                    AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstMapper.getAeaHiApplyinstById(aeaApplyinstProj.getApplyinstId());
                                    if (aeaHiApplyinst != null && "6".equals(aeaHiApplyinst.getBusinessState())) {
                                        index++;
                                    }
                                }
                            }
                        }
                    }
                }
                if (index == aeaParStages.size()) {
                    bpmApproveStateVo.setCoordinationState("已完成");
                } else {
                    bpmApproveStateVo.setCoordinationState("未完成");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getIteminstIdByTaskId(String taskId) {
        AeaHiIteminst iteminst = getIteminstByTaskId(taskId);
        if (iteminst != null) {
            return iteminst.getIteminstId();
        }
        return null;
    }

    public AeaHiIteminst getIteminstByTaskId(String taskId) {
        try {
            HistoricTaskInstance task = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();

            String processInstanceId = task.getProcessInstanceId();

            if (processInstanceId == null)
                return null;

            //20190901 改为根据流程实例id从事项实例表里获取
            AeaHiIteminst aeaHiIteminst = aeaHiIteminstMapper.getAeaHiIteminstByProcinstId(processInstanceId);
            if (aeaHiIteminst != null) {
                return aeaHiIteminst;
            } else {
                //如果为空，尝试流程变量里面获取
                HistoricVariableInstance variableInstance = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).variableName("$BUS_CURRENT_ITEMINST_ID").singleResult();
                if (variableInstance != null) {
                    return aeaHiIteminstMapper.getAeaHiIteminstById(variableInstance.getValue().toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取并联申报已选择的情形列表
     *
     * @param applyinstId
     */
    public List<AeaParState> getHiParStateinstByApplyinstId(String applyinstId) throws Exception {
        //并联
        List<AeaHiParStateinst> aeaHiParStateinsts = aeaHiParStateinstMapper.listAeaHiParStateinstByApplyinstIdOrStageinstId(applyinstId, null);
        if (aeaHiParStateinsts.size() == 0) {
            return new ArrayList<>();
        }
        List<AeaParState> list = new ArrayList<>();

        for (AeaHiParStateinst stateinst : aeaHiParStateinsts) {
            //AeaParState aeaParState=new AeaParState();
            String execStateId = stateinst.getExecStateId();
            //查询选择的情形
            AeaParState aeaParState = aeaParStateMapper.getAeaParStateById(execStateId);
            if (null != aeaParState) {
                //查询父情形
                AeaParState parentState = aeaParStateMapper.getAeaParStateById(aeaParState.getParentStateId());
                List<AeaParState> parStates = new ArrayList<>();
                parStates.add(aeaParState);
                parentState.setAnswerStates(parStates);
                list.add(parentState);
            }

        }
        return list;
    }

    /**
     * 获取单项申报项目的已选择情形信息
     *
     * @param applyinstId 申报id
     */
    public List<AeaItemState> getSelectedStates4Item(String applyinstId) throws Exception {
        List<AeaHiItemStateinst> aeaHiItemStateinsts = aeaHiItemStateinstMapper.listAeaHiItemStateinstByApplyinstIdOrSeriesinstId(applyinstId, null);

        List<AeaItemState> list = new ArrayList<>();
        if (aeaHiItemStateinsts.size() > 0) {
            for (AeaHiItemStateinst stateinst : aeaHiItemStateinsts) {
                String execStateId = stateinst.getExecStateId();
                //查询选择的情形
                AeaItemState aeaItemStateById = aeaItemStateMapper.getAeaItemStateById(execStateId);
                if (aeaItemStateById != null) {
                    String parentStateId = aeaItemStateById.getParentStateId();
                    AeaItemState parentState = aeaItemStateMapper.getAeaItemStateById(parentStateId);
                    if (null != parentState) {
                        List<AeaItemState> stateList = new ArrayList<>();
                        stateList.add(aeaItemStateById);
                        parentState.setAnswerStates(stateList);
                        list.add(parentState);
                    }

                }
            }
        }

        return list;
    }

    /**
     * 根据申请实例查询事项实例
     *
     * @param applyinstId 申请实例ID
     * @param busRecordId 业务主键，
     * @param isItemSeek  是否意见征集，默认false
     */
    public List<AeaHiIteminst> getItemisntList(String applyinstId, String busRecordId, boolean isItemSeek) throws Exception {
        AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstMapper.getAeaHiApplyinstById(applyinstId);
        if (aeaHiApplyinst == null) {
            throw new RuntimeException("查询不到申请信息");
        }
        //单项审批只会查询到一个，不需要过滤
        List<AeaHiIteminst> aeaHiIteminsts = aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(applyinstId);
        if (aeaHiIteminsts.isEmpty()) {
            return aeaHiIteminsts;
        }
        //并联审批
        if ("0".equals(aeaHiApplyinst.getIsSeriesApprove())) {
            if (busRecordId != null && !"undefined".equals(busRecordId)) {
                if (isItemSeek) {
                    aeaHiIteminsts = aeaHiIteminsts.stream().filter(s -> busRecordId.equals(s.getApproveOrgId())).collect(Collectors.toList());
                } else {
                    aeaHiIteminsts = aeaHiIteminsts.stream().filter(s -> busRecordId.equals(s.getIteminstId())).collect(Collectors.toList());
                }
            }
        }
        if (!aeaHiIteminsts.isEmpty()) {
            //先获取数据字典的类型列表，如果没有则不返回事项相关字段类型
            List<BscDicCodeItem> due_unit_type = bscDicCodeMapper.getActiveItemsByTypeCode("DUE_UNIT_TYPE", SecurityContext.getCurrentOrgId());
            List<BscDicCodeItem> item_property = bscDicCodeMapper.getActiveItemsByTypeCode("ITEM_PROPERTY", SecurityContext.getCurrentOrgId());
            for (AeaHiIteminst temp : aeaHiIteminsts) {
                AeaItemBasic itemBasic = aeaItemBasicMapper.getAeaItemBasicByItemVerId(temp.getItemVerId(), SecurityContext.getCurrentOrgId());
                if (itemBasic != null) {
                    OpuOmOrg opuOmOrg = opuOmOrgMapper.getOrg(itemBasic.getOrgId());
                    if (opuOmOrg.getOrgName() != null) {
                        temp.setApproveOrgName(opuOmOrg.getOrgName());
                    }
                    temp.setIteminstName(itemBasic.getItemName());
                    temp.setDueNum(itemBasic.getDueNum());
                    for (BscDicCodeItem item : due_unit_type) {
                        if (item.getItemCode().equals(itemBasic.getBjType())) {
                            temp.setDueNumUnit(item.getItemName());
                            break;
                        }
                    }
                    for (BscDicCodeItem item : item_property) {
                        if (item.getItemCode().equals(itemBasic.getItemProperty())) {
                            temp.setItemProperty(item.getItemName());
                            break;
                        }
                    }
                }
            }
        }
        return aeaHiIteminsts;
    }

    public AeaHiItemInfoVo projectDeclareInfo(String applyinstId, String busRecordId, boolean isItemSeek, String taskId) throws Exception {
        //申报实例
        AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstMapper.getAeaHiApplyinstById(applyinstId);
        if (aeaHiApplyinst != null) {
            AeaHiItemInfoVo aeaHiItemInfoVo = new AeaHiItemInfoVo();
            //申报时间
            aeaHiItemInfoVo.setApplyDate(aeaHiApplyinst.getCreateTime());
            //申报流水号
            aeaHiItemInfoVo.setApplyNo(aeaHiApplyinst.getApplyinstCode());
            //分局承办单位
            String branchOrgName = "";
            if (com.augurit.agcloud.framework.util.StringUtils.isNotBlank(aeaHiApplyinst.getBranchOrg())) {
                JSONArray mapList = JSONObject.parseArray(aeaHiApplyinst.getBranchOrg());
                if (mapList != null && mapList.size() > 0) {
                    Map<String, String> map = (Map<String, String>) mapList.get(0);
                    String branchOrgId = map.get("branchOrg");
                    if (com.augurit.agcloud.framework.util.StringUtils.isNotBlank(branchOrgId)) {
                        OpuOmOrg branchOrg = opuOmOrgMapper.getOrg(branchOrgId);
                        if (branchOrg != null) {
                            branchOrgName = branchOrg.getOrgName();
                        }
                    }
                }
            }
            aeaHiItemInfoVo.setBranchOrgName(branchOrgName);
            //兼容多项目申报
            //20190225 修改 根据申请实例ID 查询申请项目信息
            AeaApplyinstProj aeaApplyinstProj = new AeaApplyinstProj();
            aeaApplyinstProj.setApplyinstId(applyinstId);
            List<AeaApplyinstProj> aeaApplyinstProjs = aeaApplyinstProjMapper.listAeaApplyinstProj(aeaApplyinstProj);
            String[] projInfoIds = null;
            if (aeaApplyinstProjs.size() > 0) {
                projInfoIds = aeaApplyinstProjs.parallelStream().map(AeaApplyinstProj::getProjInfoId).toArray(String[]::new);
                String projId = aeaApplyinstProjs.parallelStream().map(AeaApplyinstProj::getProjInfoId).collect(Collectors.joining(","));
                List<AeaProjInfo> projInfos = aeaProjInfoMapper.listAeaProjInfoByIds(projInfoIds);
                if (projInfos.size() > 0) {
                    String projCodeStr = projInfos.stream().map(AeaProjInfo::getLocalCode).collect(Collectors.joining("，</br>"));
                    String projNameStr = projInfos.stream().map(AeaProjInfo::getProjName).collect(Collectors.joining("，</br>"));
                    aeaHiItemInfoVo.setProjCode(projCodeStr);
                    aeaHiItemInfoVo.setProjName(projNameStr);//项目名称
                    aeaHiItemInfoVo.setProjInfoId(projId);//项目信息ID
                }
            }
            //是否串联,并联
            aeaHiItemInfoVo.setIsSeries(aeaHiApplyinst.getIsSeriesApprove());

            //根据APPLYINSTiD 查询并联或串联 aeaHITEMINST
            List<AeaHiIteminst> aeaHiIteminstListByApplyinstId = aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(applyinstId);
            if (aeaHiIteminstListByApplyinstId.size() == 0) {
                return aeaHiItemInfoVo;
            }
            if ("0".equals(aeaHiApplyinst.getIsSeriesApprove())) {//并联
                if (busRecordId != null && !"undefined".equals(busRecordId)) {
                    if (isItemSeek) {
                        aeaHiIteminstListByApplyinstId = aeaHiIteminstListByApplyinstId.parallelStream().filter(s -> s.getApproveOrgId().equals(busRecordId)).collect(Collectors.toList());
                    } else {
                        aeaHiIteminstListByApplyinstId = aeaHiIteminstListByApplyinstId.parallelStream().filter(s -> s.getIteminstId().equals(busRecordId)).collect(Collectors.toList());
                    }
                } else {
                    if (com.augurit.agcloud.framework.util.StringUtils.isNotBlank(taskId)) {
                        String iteminstId = getIteminstIdByTaskId(taskId);
                        aeaHiIteminstListByApplyinstId = aeaHiIteminstListByApplyinstId.parallelStream().filter(s -> s.getIteminstId().equals(iteminstId)).collect(Collectors.toList());
                    }
                }

            } else {
                //设置单项办件类型
                String itemVerId = aeaHiIteminstListByApplyinstId.get(0).getItemVerId();
                AeaItemBasic oneByItemVerId = aeaItemBasicMapper.getAeaItemBasicByItemVerId(itemVerId, SecurityContext.getCurrentOrgId());
                aeaHiItemInfoVo.setItemProperty(oneByItemVerId.getItemProperty());
            }
            if (aeaHiIteminstListByApplyinstId.size() > 0) {
                //事项ID
                aeaHiItemInfoVo.setItemId(aeaHiIteminstListByApplyinstId.stream().map(AeaHiIteminst::getItemId).collect(Collectors.joining(",")));
                aeaHiItemInfoVo.setItemVerId(aeaHiIteminstListByApplyinstId.stream().map(AeaHiIteminst::getItemVerId).collect(Collectors.joining(",")));
                //事项名称
                aeaHiItemInfoVo.setItemName(aeaHiIteminstListByApplyinstId.stream().map(AeaHiIteminst::getIteminstName).collect(Collectors.joining(",")));
                //事项实例ID
                aeaHiItemInfoVo.setItemInstId(aeaHiIteminstListByApplyinstId.stream().map(AeaHiIteminst::getIteminstId).collect(Collectors.joining(",")));
                //事项编码
                aeaHiItemInfoVo.setIteminstCode(aeaHiIteminstListByApplyinstId.stream().map(AeaHiIteminst::getIteminstCode).collect(Collectors.joining(",")));
                //实施编码
                aeaHiItemInfoVo.setSsCode(aeaHiIteminstListByApplyinstId.stream().map(AeaHiIteminst::getIteminstCode).collect(Collectors.joining(",")));
            }


            //获取建设单位和经办单位信息
            if (projInfoIds != null && projInfoIds.length > 0) {

                if ("0".equals(aeaHiApplyinst.getApplySubject())) {
                    String projInfoId = projInfoIds[0];
                    AeaLinkmanInfo applyProjLinkman = aeaLinkmanInfoService.getApplyProjLinkman(applyinstId, projInfoId);
                    if (applyProjLinkman != null) {
                        aeaHiItemInfoVo.setApplyUnit(applyProjLinkman.getLinkmanName());
                        aeaHiItemInfoVo.setApplyIDCard(applyProjLinkman.getLinkmanCertNo());
                    }
                    AeaLinkmanInfo projLinkman = aeaLinkmanInfoService.getProjLinkman(applyinstId, projInfoId);
                    if (projLinkman != null) {
                        aeaHiItemInfoVo.setContact(projLinkman.getLinkmanName());
                        aeaHiItemInfoVo.setContactIdNo(projLinkman.getLinkmanCertNo());
                        aeaHiItemInfoVo.setContactMobile(projLinkman.getLinkmanMobilePhone());
                        aeaHiItemInfoVo.setContactEmail(projLinkman.getLinkmanMail());
                    }
                } else {
                    //查询联系人信息
                    AeaLinkmanInfo linkmanInfo = aeaLinkmanInfoMapper.getAeaLinkmanInfoById(aeaHiApplyinst.getLinkmanInfoId());
                    //获取建设单位
                    List<AeaUnitInfo> ownerUnit = aeaUnitInfoService.findApplyOwnerUnitProj(applyinstId, projInfoIds[0]);
                    if (null != ownerUnit && ownerUnit.size() > 0) {
                        //获取去重后建设单位 证件 名字
                        String applicant = ownerUnit.parallelStream().distinct().map(AeaUnitInfo::getApplicant).collect(Collectors.joining("<br>"));
                        String idCard = ownerUnit.parallelStream().distinct().map(AeaUnitInfo::getUnifiedSocialCreditCode).collect(Collectors.joining("<br>"));
                        aeaHiItemInfoVo.setApplyUnit(applicant);
                        aeaHiItemInfoVo.setApplyIDCard(idCard);
                        List<AeaLinkmanInfo> linkmanByUnitInfoId = aeaLinkmanInfoService.findAllUnitLinkman(ownerUnit.get(0).getUnitInfoId());
                        if (containsLinkman(linkmanByUnitInfoId, linkmanInfo)) {//经办人不为空
                            setContactValue(linkmanInfo, aeaHiItemInfoVo);
                        } else {
                            setContactValue(linkmanByUnitInfoId.get(0), aeaHiItemInfoVo);
                        }
                    }
                    //获取代办单位
                    List<AeaUnitInfo> daibanUnit = aeaUnitInfoService.findApplyNonOwnerUnitProj(applyinstId, projInfoIds[0]);
                    if (null != daibanUnit && daibanUnit.size() > 0) {
                        //获取去重后单位 证件 名字
                        String agentApplicant = daibanUnit.parallelStream().distinct().filter(u -> com.augurit.agcloud.framework.util.StringUtils.isNotBlank(u.getApplicant())).map(AeaUnitInfo::getApplicant).collect(Collectors.joining("<br>"));
                        String agentIdCard = daibanUnit.parallelStream().distinct().filter(u -> com.augurit.agcloud.framework.util.StringUtils.isNotBlank(u.getUnifiedSocialCreditCode())).map(AeaUnitInfo::getUnifiedSocialCreditCode).collect(Collectors.joining("<br>"));
                        aeaHiItemInfoVo.setAgency(agentApplicant);
                        aeaHiItemInfoVo.setAgencyIDCard(agentIdCard);
                        //设置联系人信息
                        if (linkmanInfo != null) {//经办人不为空
                            setAgencyContactValue(linkmanInfo, aeaHiItemInfoVo);
                        } else {
                            List<AeaLinkmanInfo> linkmanByUnitInfoId = aeaLinkmanInfoService.findAllUnitLinkman(daibanUnit.get(0).getUnitInfoId());
                            setAgencyContactValue(linkmanByUnitInfoId.get(0), aeaHiItemInfoVo);
                        }
                    }
                }
            }
            return aeaHiItemInfoVo;
        }
        return null;
    }

    private void setAgencyContactValue(AeaLinkmanInfo linkmanInfo, AeaHiItemInfoVo aeaHiItemInfoVo) {
        aeaHiItemInfoVo.setAgencyContact(linkmanInfo.getLinkmanName());
        aeaHiItemInfoVo.setAgencyContactIdNo(linkmanInfo.getLinkmanCertNo());
        aeaHiItemInfoVo.setAgencyContactMobile(linkmanInfo.getLinkmanMobilePhone());
        aeaHiItemInfoVo.setAgencyContactEmail(linkmanInfo.getLinkmanMail());

    }

    private void setContactValue(AeaLinkmanInfo linkman, AeaHiItemInfoVo aeaHiItemInfoVo) {
        aeaHiItemInfoVo.setContact(linkman.getLinkmanName());
        aeaHiItemInfoVo.setContactIdNo(linkman.getLinkmanCertNo());
        aeaHiItemInfoVo.setContactMobile(linkman.getLinkmanMobilePhone());
        aeaHiItemInfoVo.setContactEmail(linkman.getLinkmanMail());
    }

    private boolean containsLinkman(List<AeaLinkmanInfo> aeaLinkmanInfos, AeaLinkmanInfo linkmanInfo) {
        boolean bool = false;
        if (linkmanInfo != null && aeaLinkmanInfos.size() > 0) {
            bool = aeaLinkmanInfos.parallelStream().anyMatch(s -> s.getLinkmanInfoId().equals(linkmanInfo.getLinkmanInfoId()));
        }
        return bool;
    }

    /**
     * 获取单项申报材料列表||并联审批单个事项材料
     *
     * @param iteminstId 事项实例ID
     */
    public List<MatinstVo> getSeriesMatinstByIteminstId(String iteminstId, String applyinstId) throws Exception {
        List<MatinstVo> matinstVos = new ArrayList();
        List<AeaItemMat> mats = aeaItemMatService.getMatListByApplyinstIdContainsMatinst(applyinstId, iteminstId);

        String itemVerIds = null;

        if (StringUtils.isNotBlank(iteminstId)) {
            AeaHiIteminst iteminst = aeaHiIteminstService.getAeaHiIteminstById(iteminstId);
            if (iteminst == null) throw new Exception("找不到事项实例！");
            itemVerIds = iteminst.getItemVerId();
        } else if (StringUtils.isNotBlank(applyinstId)) {
            List<AeaHiIteminst> aeaHiIteminsts = aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(applyinstId);
            itemVerIds = aeaHiIteminsts.stream().map(AeaHiIteminst::getItemVerId).collect(Collectors.joining(","));
        }

        for (AeaItemMat mat : mats) {
            MatinstVo matinstVo = new MatinstVo();
            BeanUtils.copyProperties(mat, matinstVo);

            if (mat.getPageMatinstList().size() > 0) {
                AeaHiItemMatinst matinst = mat.getPageMatinstList().get(0);
                matinstVo.setPaperMatinstId(matinst.getMatinstId());
                matinstVo.setRealPaperCount(matinst.getRealPaperCount());
            }

            if (mat.getCopyMatinstList().size() > 0) {
                AeaHiItemMatinst matinst = mat.getCopyMatinstList().get(0);
                matinstVo.setCopyMatinstId(matinst.getMatinstId());
                matinstVo.setRealCopyCount(matinst.getRealCopyCount());
            }

            if (mat.getAttMatinstList().size() > 0) {
                AeaHiItemMatinst matinst = mat.getAttMatinstList().get(0);
                matinstVo.setAttMatinstId(matinst.getMatinstId());
                matinstVo.setAttCount(matinst.getAttCount());
                matinstVo.setFileList(fileUtilsService.getMatAttDetailByMatinstId(matinst.getMatinstId()));
            }


            if (mat.getCertinstList().size() > 0) {

//                AeaHiItemMatinst matinst = mat.getCertinstList().get(0);
                List<Map> certinstList = new ArrayList();
                Map obj = null;
                for (AeaHiItemMatinst certMatinst : mat.getCertinstList()) {
                    obj = new HashMap();
                    //来自于本地系统的证照实例
                    if (CertinstSource.LOCAL.getValue().equals(certMatinst.getCertinstSource())) {
                        obj.put("certFileList", fileUtilsService.getMatAttDetailByMatinstId(certMatinst.getCertinstId()));
                    } else if (CertinstSource.EXTERNAL.getValue().equals(certMatinst.getCertinstSource())) {//来自于外部系统的证照实例
                        OpuOmUser user = opuOmUserMapper.getUserByUserId(certMatinst.getCreater());
                        List<BscAttFileAndDir> bscAttFileAndDirs = new ArrayList();
                        BscAttFileAndDir fileAndDir = new BscAttFileAndDir();
                        fileAndDir.setFileId(certMatinst.getAuthCode());
                        fileAndDir.setFileName(certMatinst.getCertisntName());
                        fileAndDir.setCreaterName(user != null ? user.getUserName() : certMatinst.getCreater());
                        fileAndDir.setUpdateTime(certMatinst.getCreateTime());
                        bscAttFileAndDirs.add(fileAndDir);
                        obj.put("certFileList", bscAttFileAndDirs);
                    }

                    obj.put("certMatinstId", certMatinst.getMatinstId());
                    obj.put("certinstSource", certMatinst.getCertinstSource());
                    certinstList.add(obj);
                }
                matinstVo.setCertinstList(certinstList);
            }
            matinstVo.setItemVerIds(itemVerIds);
            if (mat.getForminstList().size() > 0) {
                matinstVo.setForminstId(mat.getForminstList().get(0).getStoForminstId());
            }

            matinstVos.add(matinstVo);
        }

        return matinstVos;
    }

    /**
     * 业务审批 --> 申报表单基本信息
     *
     * @param taskId      任务ID
     * @param applyinstId 申请实例ID
     * @param isItemSeek  是否意见征集
     * @return ApplyFormVo
     * @throws Exception
     */
    public ApplyFormVo getBaseApplyFormInfo(String taskId, String applyinstId, boolean isItemSeek) throws Exception {
        ApplyFormVo vo = new ApplyFormVo();
        AeaHiApplyinst applyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);
        if (null == applyinst) return vo;
        //项目信息
        List<AeaProjInfo> projInfos = aeaProjInfoService.findApplyProj(applyinstId);
        String iteminstId = this.getIteminstIdByTaskId(taskId);
        List<AeaHiIteminst> itemisntList = this.getItemisntList(applyinstId, iteminstId, isItemSeek);
        vo.setProjInfoList(projInfos);
        ApplyFormVo.ApplyInfoVo applyInfoVo = vo.changeApply2vo(applyinst);
        if ("0".equals(applyinst.getIsSeriesApprove())) {
            //并联申报需要查询出申报的主题和阶段名称
            AeaHiParStageinst aeaHiParStageinst = aeaHiParStageinstMapper.getAeaHiParStageinstByApplyinstId(applyinstId);
            if (aeaHiParStageinst != null) {
                applyInfoVo.setStageName(aeaHiParStageinst.getStageName());
                applyInfoVo.setThemeName(aeaHiParStageinst.getThemeName());
            }
        }
        vo.setApplyInfoVo(applyInfoVo);
        vo.setIteminstList(itemisntList);

        String applySubject = applyinst.getApplySubject();
        vo.setApplySubject(applySubject);
        if ("1".equals(applySubject)) {//unit
            //单位信息
            List<AeaUnitInfo> unitInfos = aeaUnitInfoService.findApplyUnitProj(applyinstId, projInfos.get(0).getProjInfoId());
            //增加查询信用类型
            List<ApplyFormVo.UnitInfoVo> unitVoList = new ArrayList<>();
            for (AeaUnitInfo unitInfo : unitInfos) {
                ApplyFormVo.UnitInfoVo unitInfoVo = new ApplyFormVo.UnitInfoVo();
                BeanUtils.copyProperties(unitInfo, unitInfoVo);
                //20191018 增加项目单位
                List<AeaUnitProjLinkman> aeaUnitProjLinkmen = aeaUnitProjLinkmanMapper.queryByUnitProjIdAndlinkType(unitInfo.getUnitProjId(), null, null);
                vo.setLinkmanTypes(unitInfoVo, aeaUnitProjLinkmen);
                String unifiedSocialCreditCode = unitInfoVo.getUnifiedSocialCreditCode();//信用代码
                List<AeaCreditSummary> aeaCreditSummaries = aeaCreditSummaryMapper.listAeaCreditSummaryByUnifiedSocialCreditCode(unifiedSocialCreditCode);
                unitInfoVo.setCreditType(true);
                if (aeaCreditSummaries.size() > 0) {
                    boolean bad = aeaCreditSummaries.stream().noneMatch(aeaCreditSummary -> aeaCreditSummary.getCreditType().equals("bad"));
                    unitInfoVo.setCreditType(bad);
                }
                //查询联系人信息，默认取第一个
                List<AeaLinkmanInfo> linkmanInfos = aeaLinkmanInfoMapper.getAeaLinkmanInfoByUnitInfoId(unitInfo.getUnitInfoId(), null);
                if (linkmanInfos.size() > 0) {
                    AeaLinkmanInfo linkmanInfo = linkmanInfos.get(0);
                    unitInfoVo.setLinkmanName(linkmanInfo.getLinkmanName());
                    unitInfoVo.setLinkmanCertNo(linkmanInfo.getLinkmanCertNo());
                    unitInfoVo.setLinkmanMail(linkmanInfo.getLinkmanMail());
                    unitInfoVo.setLinkmanMobilePhone(linkmanInfo.getLinkmanMobilePhone());
                    unitInfoVo.setLinkmanFax(linkmanInfo.getLinkmanFax());
                }
                unitVoList.add(unitInfoVo);
            }
            vo.setUnitInfoVoList(unitVoList);
            //联系人信息
            AeaLinkmanInfo linkmanInfo = aeaLinkmanInfoService.getAeaLinkmanInfoByLinkmanInfoId(applyinst.getLinkmanInfoId());

            vo.setLinkmanInfoVo(vo.changeLinkman2vo(linkmanInfo));
        } else if ("0".equals(applySubject)) {//personal
            List<AeaProjLinkman> projLinkmanList = aeaProjLinkmanMapper.getAeaProjLinkmanByApplyinstId(applyinstId, null);
            vo.setLinkmanVoList(vo.changeProjLinkman2vo(projLinkmanList));
        }
        return vo;
    }

    /**
     * 修改或新增Matinst实例和certinst的关联
     *
     * @throws Exception
     */
    public String updateOrInsertMatCertinst(AeaHiCertinst aeaHiCertinst) throws Exception {

        StringBuffer message = new StringBuffer();
        String certinstId = null;
        String unitInfoId = null;

        AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstById(aeaHiCertinst.getApplyinstId());
        if (aeaHiApplyinst == null) return message.append("找不到申报实例！").toString();

        List<AeaApplyinstProj> aeaApplyinstProjs = aeaApplyinstProjMapper.getAeaApplyinstProjByApplyinstId(aeaHiCertinst.getApplyinstId());
        if (aeaApplyinstProjs.size() < 1) return message.append("找不到项目信息！").toString();
        String projInfoId = aeaApplyinstProjs.get(0).getProjInfoId();

        if (StringUtils.isBlank(aeaHiCertinst.getCertinstId())) {

            List<AeaHiCertinst> certinsts = aeaHiItemMatinstService.getHistoryCertinst(aeaHiCertinst.getCertinstCode(), SecurityContext.getCurrentOrgId());
            if (certinsts.size() < 1) {

                AeaCert aeaCert = aeaCertMapper.getAeaCertById(aeaHiCertinst.getCertId(), SecurityContext.getCurrentOrgId());
                if (aeaCert == null) return message.append("证照定义不存在！").toString();

                aeaHiCertinst.setCertinstId(certinstId);
                aeaHiCertinst.setRootOrgId(SecurityContext.getCurrentOrgId());
                aeaHiCertinst.setCreater(SecurityContext.getCurrentUserId());
                aeaHiCertinst.setCertinstSource(CertinstSource.EXTERNAL.getValue());
                aeaHiCertinst.setCreateTime(new Date());
                aeaHiCertinst.setProjInfoId(projInfoId);

                if ("u".equals(aeaCert.getCertHolder())) {
                    aeaHiCertinst.setLinkmanInfoId(aeaHiApplyinst.getLinkmanInfoId());
                } else {
                    List<AeaUnitInfo> aeaUnitInfos = aeaUnitProjMapper.findUnitInfoByProjIdAndUnitType(projInfoId, UnitType.DEVELOPMENT_UNIT.getValue());
                    if (aeaUnitInfos.size() > 0) {
                        unitInfoId = aeaUnitInfos.get(0).getUnitInfoId();
                        aeaHiCertinst.setUnitInfoId(unitInfoId);
                    }
                }
                aeaHiCertinstMapper.insertAeaHiCertinst(aeaHiCertinst);
            } else {
                certinstId = certinsts.get(0).getCertinstId();
            }
        } else {
            certinstId = aeaHiCertinst.getCertinstId();
        }

        if (StringUtils.isBlank(aeaHiCertinst.getMatinstId())) {
            if (StringUtils.isBlank(aeaHiCertinst.getCertId()) || StringUtils.isBlank(aeaHiCertinst.getMatId()) || StringUtils.isBlank(aeaHiCertinst.getApplyinstId()))
                return message.append("缺少参数！").toString();
            AeaItemMat aeaItemMat = aeaItemMatMapper.getAeaItemMatById(aeaHiCertinst.getMatId());
            if (aeaItemMat != null) {

                if ("c".equals(aeaItemMat.getMatProp())) {
                    AeaHiItemMatinst aeaHiItemMatinst = new AeaHiItemMatinst();
                    aeaHiItemMatinst.setMatinstId(UUID.randomUUID().toString());
                    aeaHiItemMatinst.setCertinstId(certinstId);
                    aeaHiItemMatinst.setMatId(aeaHiCertinst.getMatId());
                    aeaHiItemMatinst.setCreateTime(new Date());
                    aeaHiItemMatinst.setCreater(SecurityContext.getCurrentUserId());
                    aeaHiItemMatinst.setMatinstCode(aeaItemMat.getMatCode());
                    aeaHiItemMatinst.setMatinstName(aeaItemMat.getMatName());
                    aeaHiItemMatinst.setMatProp(aeaItemMat.getMatProp());
                    aeaHiItemMatinst.setRootOrgId(SecurityContext.getCurrentOrgId());

                    if ("1".equals(aeaHiApplyinst.getApplySubject())) {
                        aeaHiItemMatinst.setMatinstSource("u");

                        if (StringUtils.isBlank(unitInfoId)) {
                            List<AeaUnitInfo> aeaUnitInfos = aeaUnitProjMapper.findUnitInfoByProjIdAndUnitType(projInfoId, UnitType.DEVELOPMENT_UNIT.getValue());
                            if (aeaUnitInfos.size() < 1) return message.append("找不到建设单位！").toString();
                            unitInfoId = aeaUnitInfos.get(0).getUnitInfoId();
                        }
                        aeaHiItemMatinst.setUnitInfoId(unitInfoId);
                    } else {
                        aeaHiItemMatinst.setMatinstSource("l");
                        aeaHiItemMatinst.setLinkmanInfoId(aeaHiApplyinst.getLinkmanInfoId());
                    }
                    aeaHiItemMatinst.setProjInfoId(projInfoId);
                    aeaHiItemMatinstMapper.insertAeaHiItemMatinst(aeaHiItemMatinst);

                    //创建该申报实例下所有需要用到该材料实例的关联关系
                    aeaHiItemInoutinstService.batchInsertAeaHiItemInoutinst(new String[]{aeaHiItemMatinst.getMatinstId()}, aeaHiCertinst.getApplyinstId(), SecurityContext.getCurrentUserId());

                } else {
                    message.append("该材料不是证照类型！");
                }

            } else {
                message.append("找不到材料定义！");
            }

        } else {

            AeaHiItemMatinst aeaHiItemMatinst = aeaHiItemMatinstMapper.getAeaHiItemMatinstById(aeaHiCertinst.getMatinstId());
            if (aeaHiItemMatinst == null) return message.append("找不到材料实例！").toString();
            aeaHiItemMatinst.setCertinstId(certinstId);
            aeaHiItemMatinst.setModifyTime(new Date());
            aeaHiItemMatinst.setModifier(SecurityContext.getCurrentUserId());
            aeaHiItemMatinstMapper.updateAeaHiItemMatinst(aeaHiItemMatinst);
        }


        return message.toString();
    }

    /**
     * 上传电子附件
     *
     * @throws Exception
     */
    public String uploadAtt(String matinstId, String applyinstId, String matId, HttpServletRequest request) throws Exception {

        StringBuffer message = new StringBuffer();
        request.setCharacterEncoding("utf-8");//设置编码，防止附件名称乱码

        if (StringUtils.isNotBlank(matinstId)) {

            AeaHiItemMatinst aeaHiItemMatinst = aeaHiItemMatinstService.getAeaHiItemMatinstById(matinstId);
            if (aeaHiItemMatinst == null) return message.append("找不到材料实例！").toString();
            aeaHiItemMatinstService.setAttCount(aeaHiItemMatinst, request);
            aeaHiItemMatinst.setModifier(SecurityContext.getCurrentUserName());
            aeaHiItemMatinst.setModifyTime(new Date());
            aeaHiItemMatinstMapper.updateAeaHiItemMatinst(aeaHiItemMatinst);

        } else {

            if (StringUtils.isBlank(applyinstId) || StringUtils.isBlank(matId))
                return message.append("缺少参数！").toString();

            AeaItemMat aeaItemMat = aeaItemMatMapper.getAeaItemMatById(matId);

            if (aeaItemMat != null) {

                AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);
                if (aeaHiApplyinst == null) return message.append("找不到申报实例！").toString();

                AeaHiItemMatinst aeaHiItemMatinst = new AeaHiItemMatinst();
                aeaHiItemMatinst.setMatinstId(UUID.randomUUID().toString());
                aeaHiItemMatinst.setMatId(matId);
                aeaHiItemMatinst.setCreateTime(new Date());
                aeaHiItemMatinst.setCreater(SecurityContext.getCurrentUserId());
                aeaHiItemMatinst.setMatinstCode(aeaItemMat.getMatCode());
                aeaHiItemMatinst.setMatinstName(aeaItemMat.getMatName());
                aeaHiItemMatinst.setMatProp(aeaItemMat.getMatProp());
                aeaHiItemMatinst.setRootOrgId(SecurityContext.getCurrentOrgId());

                List<AeaApplyinstProj> aeaApplyinstProjs = aeaApplyinstProjMapper.getAeaApplyinstProjByApplyinstId(applyinstId);
                if (aeaApplyinstProjs.size() < 1) return message.append("找不到项目信息！").toString();
                String projInfoId = aeaApplyinstProjs.get(0).getProjInfoId();

                if ("1".equals(aeaHiApplyinst.getApplySubject())) {
                    aeaHiItemMatinst.setMatinstSource("u");
                    List<AeaUnitInfo> aeaUnitInfos = aeaUnitProjMapper.findUnitInfoByProjIdAndUnitType(projInfoId, UnitType.DEVELOPMENT_UNIT.getValue());
                    if (aeaUnitInfos.size() < 1) return message.append("找不到建设单位！").toString();
                    aeaHiItemMatinst.setUnitInfoId(aeaUnitInfos.get(0).getUnitInfoId());
                } else {
                    aeaHiItemMatinst.setLinkmanInfoId(aeaHiApplyinst.getLinkmanInfoId());
                    aeaHiItemMatinst.setMatinstSource("l");
                }
                aeaHiItemMatinstService.setAttCount(aeaHiItemMatinst, request);
                aeaHiItemMatinst.setProjInfoId(projInfoId);
                aeaHiItemMatinstMapper.insertAeaHiItemMatinst(aeaHiItemMatinst);

                //创建该申报实例下所有需要用到该材料实例的关联关系
                aeaHiItemInoutinstService.batchInsertAeaHiItemInoutinst(new String[]{aeaHiItemMatinst.getMatinstId()}, applyinstId, SecurityContext.getCurrentUserId());

            } else {
                message.append("找不到材料定义！");
            }
        }
        return message.toString();
    }

    /**
     * 新增材料实例和电子附件关联
     *
     * @throws Exception
     */
    public String createMatinstAndFileLink(String matinstId, String fileIds, String applyinstId, String matId) throws Exception {

        StringBuffer message = new StringBuffer();

        if (StringUtils.isBlank(fileIds)) return message.append("电子附件ID为空！").toString();
        String[] detailIds = fileIds.split(",");
        if (detailIds.length < 1) return message.append("电子附件ID为空！").toString();

        if (StringUtils.isBlank(matinstId)) {
            if (StringUtils.isBlank(applyinstId) || StringUtils.isBlank(matId))
                return message.append("缺少参数！").toString();

            AeaItemMat aeaItemMat = aeaItemMatMapper.getAeaItemMatById(matId);

            if (aeaItemMat != null) {

                AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);
                if (aeaHiApplyinst == null) return message.append("找不到申报实例！").toString();

                matinstId = UUID.randomUUID().toString();

                AeaHiItemMatinst aeaHiItemMatinst = new AeaHiItemMatinst();
                aeaHiItemMatinst.setMatinstId(matinstId);
                aeaHiItemMatinst.setMatId(matId);
                aeaHiItemMatinst.setCreateTime(new Date());
                aeaHiItemMatinst.setCreater(SecurityContext.getCurrentUserId());
                aeaHiItemMatinst.setMatinstCode(aeaItemMat.getMatCode());
                aeaHiItemMatinst.setMatinstName(aeaItemMat.getMatName());
                aeaHiItemMatinst.setMatProp(aeaItemMat.getMatProp());
                aeaHiItemMatinst.setRootOrgId(SecurityContext.getCurrentOrgId());

                List<AeaApplyinstProj> aeaApplyinstProjs = aeaApplyinstProjMapper.getAeaApplyinstProjByApplyinstId(applyinstId);
                if (aeaApplyinstProjs.size() < 1) return message.append("找不到项目信息！").toString();
                String projInfoId = aeaApplyinstProjs.get(0).getProjInfoId();

                if ("1".equals(aeaHiApplyinst.getApplySubject())) {
                    List<AeaUnitInfo> aeaUnitInfos = aeaUnitProjMapper.findUnitInfoByProjIdAndUnitType(projInfoId, UnitType.DEVELOPMENT_UNIT.getValue());
                    if (aeaUnitInfos.size() < 1) return message.append("找不到建设单位！").toString();
                    aeaHiItemMatinst.setUnitInfoId(aeaUnitInfos.get(0).getUnitInfoId());
                    aeaHiItemMatinst.setMatinstSource("u");
                } else {
                    aeaHiItemMatinst.setLinkmanInfoId(aeaHiApplyinst.getLinkmanInfoId());
                    aeaHiItemMatinst.setMatinstSource("l");
                }

                for (String detailId : detailIds) {
                    //复制电子件关联关系
                    BscAttLink attLink = new BscAttLink();
                    attLink.setLinkId(UUID.randomUUID().toString());
                    attLink.setTableName("AEA_HI_ITEM_MATINST");
                    attLink.setPkName("MATINST_ID");
                    attLink.setDetailId(detailId);
                    attLink.setRecordId(matinstId);
                    bscAttMapper.insertLink(attLink);
                }

                aeaHiItemMatinst.setAttCount(Long.valueOf(fileUtilsService.getAttachmentsByRecordId(new String[]{matinstId}, "AEA_HI_ITEM_MATINST", "MATINST_ID").size()));
                aeaHiItemMatinst.setProjInfoId(projInfoId);
                aeaHiItemMatinstMapper.insertAeaHiItemMatinst(aeaHiItemMatinst);

                //创建该申报实例下所有需要用到该材料实例的关联关系
                aeaHiItemInoutinstService.batchInsertAeaHiItemInoutinst(new String[]{matinstId}, applyinstId, SecurityContext.getCurrentUserId());
            } else {
                message.append("找不到材料定义！");
            }

        } else {

            AeaHiItemMatinst aeaHiItemMatinst = aeaHiItemMatinstService.getAeaHiItemMatinstById(matinstId);
            if (aeaHiItemMatinst == null) return message.append("找不到材料实例！").toString();

            for (String detailId : detailIds) {
                //复制电子件关联关系
                BscAttLink attLink = new BscAttLink();
                attLink.setLinkId(UUID.randomUUID().toString());
                attLink.setTableName("AEA_HI_ITEM_MATINST");
                attLink.setPkName("MATINST_ID");
                attLink.setRecordId(matinstId);
                attLink.setDetailId(detailId);
                bscAttMapper.insertLink(attLink);
            }

            aeaHiItemMatinst.setAttCount(Long.valueOf(fileUtilsService.getAttachmentsByRecordId(new String[]{aeaHiItemMatinst.getMatinstId()}, "AEA_HI_ITEM_MATINST", "MATINST_ID").size()));
            aeaHiItemMatinst.setModifier(SecurityContext.getCurrentUserName());
            aeaHiItemMatinst.setModifyTime(new Date());
            aeaHiItemMatinstMapper.updateAeaHiItemMatinst(aeaHiItemMatinst);
        }

        return message.toString();
    }

    /**
     * 获取电子附件材料库
     *
     * @return
     * @throws Exception
     */
    public List<BscAttFileAndDir> matinstFileLibrary(String matId, String applyinstId) throws Exception {

        List<BscAttFileAndDir> files = new ArrayList();
        if (StringUtils.isNotBlank(matId) && StringUtils.isNotBlank(applyinstId)) {

            AeaItemMat aeaItemMat = aeaItemMatMapper.getAeaItemMatById(matId);
            if (aeaItemMat != null) {

                List<AeaHiItemMatinst> matinsts = new ArrayList();

                //个人材料
                if ("l".equals(aeaItemMat.getMatHolder())) {
                    AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);
                    if (aeaHiApplyinst != null) {
                        matinsts.addAll(aeaHiItemMatinstMapper.listUnitAttMatinst(aeaItemMat.getMatCode(), null, aeaHiApplyinst.getLinkmanInfoId()));
                    }
                } else if ("p".equals(aeaItemMat.getMatHolder())) {

                    //项目材料
                    List<AeaApplyinstProj> aeaApplyinstProjs = aeaApplyinstProjMapper.getAeaApplyinstProjByApplyinstId(applyinstId);
                    if (aeaApplyinstProjs.size() > 0) {
                        String projInfoId = aeaApplyinstProjs.get(0).getProjInfoId();
                        matinsts.addAll(aeaHiItemMatinstMapper.listProjAeaHiItemMatinst(new String[]{aeaItemMat.getMatCode()}, new String[]{projInfoId}, null));
                    }
                } else {

                    //单位材料
                    List<AeaUnitInfo> aeaUnitInfos = aeaUnitProjMapper.findUnitInfoByApplyinstIdAndUnitType(applyinstId, UnitType.DEVELOPMENT_UNIT.getValue());
                    if (aeaUnitInfos.size() > 0) {
                        String[] unitInIds = aeaUnitInfos.stream().map(AeaUnitInfo::getUnitInfoId).toArray(String[]::new);
                        matinsts.addAll(aeaHiItemMatinstMapper.listProjAeaHiItemMatinst(new String[]{aeaItemMat.getMatCode()}, null, unitInIds));
                    }
                }

                for (AeaHiItemMatinst matinst : matinsts) {
                    files.addAll(fileUtilsService.getMatAttDetailByMatinstId(matinst.getMatinstId()));
                }
            }
        }
        return files;
    }

    public List<AeaHiCertinst> certinstLibrary(String certId, String applyinstId) throws Exception {

        List<AeaHiCertinst> certinsts = new ArrayList();

        if (StringUtils.isNotBlank(certId) && StringUtils.isNotBlank(applyinstId)) {
            AeaCert cert = aeaCertMapper.getAeaCertById(certId, SecurityContext.getCurrentOrgId());
            if (cert != null) {
                //个人材料
                if ("u".equals(cert.getCertHolder())) {
                    AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);
                    if (aeaHiApplyinst != null) {
                        certinsts.addAll(aeaHiCertinstMapper.getCertinsts(certId, aeaHiApplyinst.getLinkmanInfoId(), null, null, SecurityContext.getCurrentOrgId()));
                    }
                } else if ("p".equals(cert.getCertHolder())) {
                    //项目材料
                    List<AeaApplyinstProj> aeaApplyinstProjs = aeaApplyinstProjMapper.getAeaApplyinstProjByApplyinstId(applyinstId);
                    if (aeaApplyinstProjs.size() > 0) {
                        String projInfoId = aeaApplyinstProjs.get(0).getProjInfoId();
                        certinsts.addAll(aeaHiCertinstMapper.getCertinsts(certId, null, projInfoId, null, SecurityContext.getCurrentOrgId()));
                    }
                } else {
                    //单位材料
                    List<AeaUnitInfo> aeaUnitInfos = aeaUnitProjMapper.findUnitInfoByApplyinstIdAndUnitType(applyinstId, UnitType.DEVELOPMENT_UNIT.getValue());
                    if (aeaUnitInfos.size() > 0) {
                        String[] unitInIds = aeaUnitInfos.stream().map(AeaUnitInfo::getUnitInfoId).toArray(String[]::new);
                        certinsts.addAll(aeaHiCertinstMapper.getCertinsts(certId, null, null, unitInIds, SecurityContext.getCurrentOrgId()));
                    }
                }

                for (AeaHiCertinst certinst : certinsts) {
                    certinst.setBscAttDetails(aeaHiCertinstMapper.getFilesByRecordIds("AEA_HI_CERTINST", "CERTINST_ID", null, new String[]{certinst.getCertinstId()}));
                }
            }
        }

        return certinsts;
    }

    public void unbindCertinst(String matinstId) throws Exception {
        if (StringUtils.isNotBlank(matinstId)) {
            aeaHiItemInoutinstMapper.getAeaHiItemInoutinstByMatinstId(matinstId);
            aeaHiItemMatinstMapper.deleteAeaHiItemMatinst(matinstId);
        }
    }
}
