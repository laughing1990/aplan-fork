package com.augurit.aplanmis.common.service.admin.tpl.impl;


import com.augurit.agcloud.bpm.admin.tpl.service.ActTplAppAdminService;
import com.augurit.agcloud.bpm.admin.tpl.vo.AppProcCaseDefVo;
import com.augurit.agcloud.bpm.common.domain.*;
import com.augurit.agcloud.bpm.common.mapper.*;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.mapper.OpuOmOrgMapper;
import com.augurit.aplanmis.common.constants.ActTplAdminConstants;
import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaItemState;
import com.augurit.aplanmis.common.mapper.AeaItemBasicMapper;
import com.augurit.aplanmis.common.service.admin.item.AeaItemAdminService;
import com.augurit.aplanmis.common.service.admin.item.impl.AeaItemStateAdminService;
import com.augurit.aplanmis.common.service.admin.tpl.DgActTplAppAdminService;
import com.augurit.aplanmis.common.vo.AppProcCaseDefPlusAdminVo;
import com.augurit.aplanmis.common.vo.AppProcCaseDefTreeVo;
import org.apache.commons.lang.StringUtils;
import org.flowable.bpmn.model.FlowElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class DgActTplAppAdminServiceImpl implements DgActTplAppAdminService {

    private static Logger logger = LoggerFactory.getLogger(DgActTplAppAdminServiceImpl.class);

    @Autowired
    private ActTplAppMapper actTplAppMapper;
    @Autowired
    private ActTplAppFormMapper actTplAppFormMapper;
    @Autowired
    private ActTplAppViewMapper actTplAppViewMapper;
    @Autowired
    private ActTplAppRangeMapper actTplAppRangeMapper;
    @Autowired
    private ActStoViewMapper actStoViewMapper;
    @Autowired
    private ActStoFormMapper actStoFormMapper;

    @Autowired
    private ActTplAppFlowdefMapper actTplAppFlowdefMapper;

    @Autowired
    private ActTplCategoryMapper actTplCategoryMapper;

    @Autowired
    private ActStoElementMapper actStoElementMapper;

    @Autowired
    private ActTplAppElementMapper actTplAppElementMapper;

    @Autowired
    private ActTplAppAdminService actTplAppAdminService;

    @Autowired
    private DgActTplAppAdminService dgActTplAppAdminService;

    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;

    @Autowired
    private ActTplAppTriggerMapper actTplAppTriggerMapper;

    @Autowired
    private AeaItemStateAdminService aeaItemStateAdminService;

    @Autowired
    private AeaItemAdminService aeaItemAdminService;

    @Autowired
    private ActStoTimeruleMapper actStoTimeruleMapper;

    @Autowired
    private ActStoTimegroupActMapper actStoTimegroupActMapper;

    @Autowired
    private OpuOmOrgMapper opuOmOrgMapper;

    /**
     * @param appName  模板名
     * @param isSeries 1串联  0并联
     */
    @Override
    public ActTplApp createActTplAppAllInfo(String appName, String isSeries) throws Exception {

        ActTplApp actTplApp = new ActTplApp();
        String appId = UUID.randomUUID().toString();
        actTplApp.setAppId(appId);
        actTplApp.setAppOrgId(SecurityContext.getCurrentOrgId());
        actTplApp.setAppCode(UUID.randomUUID().toString());
        actTplApp.setAppComment(appName);
        actTplApp.setCreateTime(new Date());
        actTplApp.setCreater(SecurityContext.getCurrentUserName());
        actTplApp.setAppIsDeleted("0");
        actTplApp.setAppIsActive("1");
        actTplApp.setAppIsPublic("1");
        actTplApp.setFlowModel("proc");
        actTplApp.setAppSortNo(0L);
        //查找流程模板默认分类，不存在则创建一个
        ActTplCategory actTplCategory = new ActTplCategory();
        actTplCategory.setCategoryName("默认分类");
        List<ActTplCategory> categoryList = actTplCategoryMapper.listActTplCategory(actTplCategory);
        if (categoryList.size() > 0) {
            actTplCategory = categoryList.get(0);
        } else {
            actTplCategory.setCategoryId(UUID.randomUUID().toString());
            actTplCategory.setCategoryOrgId(SecurityContext.getCurrentOrgId());
            actTplCategory.setCreater(SecurityContext.getCurrentUserName());
            actTplCategory.setCreateTime(new Date());
            actTplCategory.setCategoryIsLock("0");
            actTplCategory.setCategorySortNo(0L);
            actTplCategoryMapper.insertActTplCategory(actTplCategory);
        }
        actTplApp.setCategoryId(actTplCategory.getCategoryId());
        actTplApp.setCategoryName(actTplCategory.getCategoryName());
        actTplAppMapper.insertActTplApp(actTplApp);
        //添加默认视图
        //addDefaultViewToTplApp(appId);
        //添加基本(待办已办)视图
        addBasicViewToTplApp(appId);
        //添加表单
        addActTplAppFormInfo(appId, isSeries);
        //添加可视范围：默认东莞市
        addActTplAppRangeInfo(appId);
        //添加公共元素
        addCommonElements(appId);

        return actTplApp;
    }

    /**
     * 添加公共元素
     *
     * @param appId 业务流程模板ID
     */
    private void addCommonElements(String appId) {
        String[] elements = ActTplAdminConstants.COMMONELEMENTLIST;
        for (String element : elements) {
            ActStoElement query = new ActStoElement();
            query.setElementCode(element);
            query.setIsActive("1");
            query.setIsDeleted("0");
            query.setElementOrgId("root");
            List<ActStoElement> elementlist = actStoElementMapper.listActStoElement(query);
            if (elementlist != null && elementlist.size() > 0) {
                ActStoElement actStoElement = elementlist.get(0);
                if (actStoElement != null) {
                    ActTplAppElement actTplAppElement = new ActTplAppElement();
                    actTplAppElement.setAppElementId(UUID.randomUUID().toString());
                    actTplAppElement.setAppId(appId);
                    actTplAppElement.setElementId(actStoElement.getElementId());
                    actTplAppElement.setSortNo(0);
                    actTplAppElement.setCreater(SecurityContext.getCurrentUserName());
                    actTplAppElement.setCreateTime(new Date());
                    actTplAppElementMapper.createActTplAppElement(actTplAppElement);
                }
            }
        }
    }

    /**
     * @param appId 业务流程模板ID
     */
    @Override
    public List<ActTplAppFlowdef> getAppFlowDefList(String appId) throws Exception {
        ActTplAppFlowdef query = new ActTplAppFlowdef();
        query.setAppId(appId);
        return actTplAppFlowdefMapper.listActTplAppFlowdef(query);
    }

    @Override
    public List<AppProcCaseDefPlusAdminVo> getAppProcCaseDefVo(String appId, String searchKey) throws Exception {
        List<AppProcCaseDefPlusAdminVo> result = new ArrayList<>();
        List<AppProcCaseDefVo> list=actTplAppAdminService.getTplAppProcdefList(appId,null);
        List<String> record = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (AppProcCaseDefVo vo : list) {
                String defName = vo.getDefName();
                if (StringUtils.isNotBlank(searchKey) && StringUtils.isNotBlank(defName)) {
                    if (!defName.contains(searchKey)) {
                        continue;
                    }
                }
                record.add(vo.getDefKey());
                AppProcCaseDefPlusAdminVo plusVo = new AppProcCaseDefPlusAdminVo();
                BeanUtils.copyProperties(vo, plusVo);
                plusVo.setIsPublic("已发布");
                result.add(plusVo);
            }
        }
        //将未发布的数据也添加进去
        List<ActTplAppFlowdef> defs = dgActTplAppAdminService.getAppFlowDefList(appId);
        if (defs != null && defs.size() > 0) {
            for (ActTplAppFlowdef def : defs) {
                AppProcCaseDefPlusAdminVo vo = new AppProcCaseDefPlusAdminVo();
                String key = def.getProcdefKey();
                if (StringUtils.isNotBlank(key) && !record.contains(key)) {
                    BeanUtils.copyProperties(def, vo);
                    vo.setDefKey(def.getProcdefKey());
                    vo.setId(def.getAppFlowdefId());
                    vo.setFlowModelName("BPMN流程");
                    if (vo.getAppId() != null) {
                        ActTplApp tplApp = actTplAppMapper.getActTplAppById(vo.getAppId());
                        if (tplApp != null) {
                            vo.setAppName(tplApp.getAppComment());
                        }
                    }
                    ActDeModel procDef = actTplAppFlowdefMapper.getActDeModelByKey(key);
                    if (procDef != null) {
                        vo.setDefName(procDef.getName());
                        vo.setModelId(procDef.getId());
                        if (StringUtils.isNotBlank(searchKey) && StringUtils.isNotBlank(procDef.getName())) {
                            if (!procDef.getName().contains(searchKey)) {
                                continue;
                            }
                        }
                    }
                    vo.setIsPublic("未发布");
                    result.add(vo);
                }
            }
        }
        return result;
    }

    @Override
    public List<AppProcCaseDefTreeVo> getAppProcCaseDefTreeVo(String appId, String stateVerId,String searchKey,boolean isPid) throws Exception {
        List<AppProcCaseDefTreeVo> result = new ArrayList<>();
        List<String> record = new ArrayList<>();
        //模糊查询结果
        List<AppProcCaseDefTreeVo> filterList = new ArrayList<>();
        //所有流程查询结果
        List<AppProcCaseDefTreeVo> allList = new ArrayList<>();
        List<AppProcCaseDefVo> list = actTplAppAdminService.getTplAppProcdefList(appId,null);
        if (list != null && list.size() > 0) {
            for (AppProcCaseDefVo vo : list) {
                AppProcCaseDefTreeVo treeVo = new AppProcCaseDefTreeVo();
                BeanUtils.copyProperties(vo,treeVo);
                treeVo.setIsProcess("1");
                allList.add(treeVo);
                String defName = vo.getDefName();
                if (StringUtils.isNotBlank(searchKey) && StringUtils.isNotBlank(defName)) {
                    if (!defName.contains(searchKey)) {
                        continue;
                    }
                }
                record.add(vo.getDefKey());
                filterList.add(treeVo);
            }
        }
        //将未发布的流程数据也添加进去
        List<ActTplAppFlowdef> defs = dgActTplAppAdminService.getAppFlowDefList(appId);
        if (defs != null && defs.size() > 0) {
            for (ActTplAppFlowdef def : defs) {
                AppProcCaseDefTreeVo vo = new AppProcCaseDefTreeVo();
                String key = def.getProcdefKey();
                if (StringUtils.isNotBlank(key) && !record.contains(key)) {
                    BeanUtils.copyProperties(def, vo);
                    vo.setDefKey(def.getProcdefKey());
                    vo.setId(def.getAppFlowdefId());
                    vo.setFlowModelName("BPMN流程");
                    vo.setIsProcess("1");
                    vo.setIsPublic("未发布");
                    allList.add(vo);
                    if (vo.getAppId() != null) {
                        ActTplApp tplApp = actTplAppMapper.getActTplAppById(vo.getAppId());
                        if (tplApp != null) {
                            vo.setAppName(tplApp.getAppComment());
                        }
                    }
                    ActDeModel procDef = actTplAppFlowdefMapper.getActDeModelByKey(key);
                    if (procDef != null) {
                        vo.setDefName(procDef.getName());
                        vo.setModelId(procDef.getId());
                        if (StringUtils.isNotBlank(searchKey) && StringUtils.isNotBlank(procDef.getName())) {
                            if (!procDef.getName().contains(searchKey)) {
                                continue;
                            }
                        }
                    }
                    filterList.add(vo);
                }
            }
        }
        //格式化流程情形
        AeaItemState itemState = new AeaItemState();
        itemState.setStateVerId(stateVerId);
        itemState.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
        List<AeaItemState> itemStateList = aeaItemStateAdminService.listAeaItemState(itemState);
        if (itemStateList != null && itemStateList.size() > 0) {
            for (AppProcCaseDefVo procCaseDefVo : filterList) {
                for (AeaItemState state : itemStateList) {
                    if (procCaseDefVo.getStartEl() != null && procCaseDefVo.getStartEl().contains(state.getItemStateId())) {
                        procCaseDefVo.setStartElName(state.getStateName());
                    } else if (procCaseDefVo.getStartEl() != null && !procCaseDefVo.getStartEl().contains(state.getItemStateId())
                            && procCaseDefVo.getStartElName() == null) {
                        procCaseDefVo.setStartElName(procCaseDefVo.getStartEl());
                    }
                }
            }
        }
        List<String> appFlowdefIds = new ArrayList<>(filterList.size());
        //时限定义数据，格式化到流程时限中
        List<ActStoTimerule> actStoTimerules = actStoTimeruleMapper.listActStoTimerule(null);
        for (AppProcCaseDefTreeVo procCaseDefVo : filterList) {
            appFlowdefIds.add(procCaseDefVo.getAppFlowdefId());
            if(StringUtils.isNotBlank(procCaseDefVo.getTimeruleId())){
                for (ActStoTimerule actStoTimerule : actStoTimerules) {
                    if (procCaseDefVo.getTimeruleId().equals(actStoTimerule.getTimeruleId())) {
                        procCaseDefVo.setTimeLimitUnit(actStoTimerule.getTimeruleUnit());
                    }
                }
            }
        }
        //查询出当前所有流程节点的时限定义数据
        List<ActStoTimegroupAct> actStoTimegroupActs = actStoTimegroupActMapper.listActStoTimegroupActByAppFlowdefId(appFlowdefIds);
        // 获取流程配置的子流程扫描
        ActTplAppTrigger query = new ActTplAppTrigger();
        query.setIsDeleted("0");
        query.setTriggerAppId(appId);
        List<ActTplAppTrigger> actTplAppTriggers = null;
        List<AeaItemBasic> aeaItemBasics = null;
        try {
            actTplAppTriggers = actTplAppTriggerMapper.listActTplAppTrigger(query);
            if (actTplAppTriggers.size() > 0) {
                List<String> itemVerIds = new ArrayList<>();
                for (ActTplAppTrigger trigger : actTplAppTriggers) {
                    if (StringUtils.isNotBlank(trigger.getBusRecordId())) {
                        itemVerIds.add(trigger.getBusRecordId());
                    }
                }
                aeaItemBasics = aeaItemBasicMapper.listAeaItemBasicByItemVerIdsAndOrgId(SecurityContext.getCurrentOrgId(), itemVerIds);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if(isPid){
            for (AppProcCaseDefTreeVo procCaseDefVo : filterList) {
                //主流程模板关联id，从这个流程往下遍历
                if("1".equals(procCaseDefVo.getIsMasterDef())){
                    buildProcTreeByPid(appId,null,procCaseDefVo,result,allList);
                }
            }
            for (AppProcCaseDefTreeVo procCaseDefVo : filterList) {
                if(!"1".equals(procCaseDefVo.getIsMasterDef()) && StringUtils.isBlank(procCaseDefVo.getPid())){
                    buildProcTreeByPid(appId,null,procCaseDefVo,result,allList);
                }
            }
        }else{
            for (AppProcCaseDefTreeVo procCaseDefVo : filterList) {
                //主流程模板关联id，从这个流程往下遍历
                if("1".equals(procCaseDefVo.getIsMasterDef())){
                    buildProcTree(appId, procCaseDefVo, result, allList, actTplAppTriggers, aeaItemBasics, actStoTimerules, actStoTimegroupActs);
                }
            }
            for (AppProcCaseDefTreeVo procCaseDefVo : filterList) {
                if(!"1".equals(procCaseDefVo.getIsMasterDef()) && StringUtils.isBlank(procCaseDefVo.getPid())){
                    buildProcTree(appId, procCaseDefVo, result, allList, actTplAppTriggers, aeaItemBasics, actStoTimerules, actStoTimegroupActs);
                }
            }
        }
        return result;
    }

    /**
     * 递归构建流程树，children 适用elementUI表格树
     * @param appId
     * @param procCaseDefVo
     * @param result
     * @param allList
     */
    private void buildProcTree(String appId, AppProcCaseDefTreeVo procCaseDefVo, List<AppProcCaseDefTreeVo> result,
                               List<AppProcCaseDefTreeVo> allList, List<ActTplAppTrigger> actTplAppTriggers,
                               List<AeaItemBasic> aeaItemBasics, List<ActStoTimerule> actStoTimerules, List<ActStoTimegroupAct> actStoTimegroupActs) {
        if(StringUtils.isBlank(procCaseDefVo.getIsPublic())){
            procCaseDefVo.setIsPublic("已发布");
        }
        result.add(procCaseDefVo);
        String appFlowdefId = procCaseDefVo.getAppFlowdefId();
        //获取流程节点信息
        String defKey = procCaseDefVo.getDefKey();
//        long l = System.currentTimeMillis();
        Collection<FlowElement> taskNodeList = aeaItemAdminService.getTaskNodeList(defKey);
//        System.out.println("加载taskList的时间" + (System.currentTimeMillis() - l));
        List<AppProcCaseDefTreeVo> children = new ArrayList<>();
        String tempId = UUID.randomUUID().toString();
        int index = 0;
        if(taskNodeList!=null&&taskNodeList.size()>0) {
            for (FlowElement flowElement : taskNodeList) {
                AppProcCaseDefTreeVo nodeVo = new AppProcCaseDefTreeVo();
                nodeVo.setDefName(flowElement.getName());
                nodeVo.setId(tempId + ++index);
                nodeVo.setModelId(flowElement.getId());
                nodeVo.setPid(appFlowdefId);
                nodeVo.setIsProcess("0");
                nodeVo.setDefKey(defKey);
                nodeVo.setAppFlowdefId(appFlowdefId);
                //填充节点时限信息
//                fillNodeTimelimit(nodeVo);
                fillNodeInfo(nodeVo, actStoTimerules, actStoTimegroupActs);
                List<AppProcCaseDefTreeVo> grandson = new ArrayList<>();
                for (ActTplAppTrigger trigger : actTplAppTriggers) {
                    if (!trigger.getAppFlowdefId().equals(appFlowdefId)) continue;
                    if (trigger.getIsDeleted().equals("1")) continue;
                    //被触发的子流程
                    String triggerAppFlowdefId = trigger.getTriggerAppFlowdefId();
                    //触发流程的节点
                    String triggerElementId = trigger.getTriggerElementId();
                    if (flowElement.getId().equals(triggerElementId)) {
                        trigger.setIsDeleted("1");//这里暂时使用isDeleted字段用于标志是已经遍历过的trigger，不再重复遍历
                        //查看当前节点是否触发了子流程
                        AppProcCaseDefTreeVo subAppProcCaseDefVo = getFromListByAppFlowdefId(allList, triggerAppFlowdefId);
                        if (subAppProcCaseDefVo != null) {
                            //如果接到触发了子流程，则往下递归，并获取流程关联的事项
//                        fillItemName(subAppProcCaseDefVo, trigger.getBusRecordId());
                            fillItemInfo(subAppProcCaseDefVo, trigger.getBusRecordId(), aeaItemBasics);
                            //如果当前流程已经被挂到某个节点下了，则拷贝一份，挂到当前节点下
                            if (StringUtils.isNotBlank(subAppProcCaseDefVo.getPid())) {
                                AppProcCaseDefTreeVo copy = new AppProcCaseDefTreeVo();
                                BeanUtils.copyProperties(subAppProcCaseDefVo, copy);
                                subAppProcCaseDefVo = copy;
                            }
                            subAppProcCaseDefVo.setPid(nodeVo.getId());
                            buildProcTree(appId, subAppProcCaseDefVo, grandson, allList, actTplAppTriggers, aeaItemBasics, actStoTimerules, actStoTimegroupActs);
                        }
                    }
                }
                nodeVo.setChildren(grandson);
                children.add(nodeVo);
            }
        }
        procCaseDefVo.setId(tempId + ++index);
        procCaseDefVo.setChildren(children);
    }

    private void fillItemInfo(AppProcCaseDefTreeVo subAppProcCaseDefVo, String busRecordId, List<AeaItemBasic> aeaItemBasics) {
        if (aeaItemBasics != null) {
            for (AeaItemBasic aeaItemBasic : aeaItemBasics) {
                if (aeaItemBasic.getItemVerId().equals(busRecordId)) {
                    subAppProcCaseDefVo.setItemApproveOrg(aeaItemBasic.getOrgName());
                    subAppProcCaseDefVo.setItemType(aeaItemBasic.getIsCatalog());
                    subAppProcCaseDefVo.setItemName(aeaItemBasic.getItemName());
                    break;
                }
            }
        }
    }

    private void fillNodeInfo(AppProcCaseDefTreeVo nodeVo, List<ActStoTimerule> actStoTimerules, List<ActStoTimegroupAct> actStoTimegroupActs) {
        for (int i = 0, len = actStoTimegroupActs.size(); i < len; i++) {
            ActStoTimegroupAct temp = actStoTimegroupActs.get(i);
            if (temp.getAppFlowdefId().equals(nodeVo.getAppFlowdefId()) && temp.getActId().equals(nodeVo.getModelId())) {
                nodeVo.setTimeLimit(temp.getTimeLimit().intValue());
                for (ActStoTimerule rule : actStoTimerules) {
                    if (rule.getTimeruleId().equals(temp.getTimeruleId())) {
                        nodeVo.setTimeLimitUnit(rule.getTimeruleUnit());
                        return;
                    }
                }
            }
        }
    }

    /**
     * 递归构建流程树，pId版，适用bootstrap表格树
     * @param appId
     * @param procCaseDefVo
     * @param result
     * @param allList
     */
    private void buildProcTreeByPid(String appId,String pId,AppProcCaseDefTreeVo procCaseDefVo, List<AppProcCaseDefTreeVo> result,List<AppProcCaseDefTreeVo> allList){
        //先初始化流程节点中的树节点信息
        if(StringUtils.isBlank(procCaseDefVo.getIsPublic())){
            procCaseDefVo.setIsPublic("已发布");
        }
        procCaseDefVo.setPid(pId);
        //默认是appFlowdefId作为树节点的id
        if(StringUtils.isBlank(procCaseDefVo.getTreeNodeId())){
            procCaseDefVo.setTreeNodeId(procCaseDefVo.getAppFlowdefId());
        }
        //将当前节点添加到返回结果集
        result.add(procCaseDefVo);
        //获取流程配置的子流程扫描
        String appFlowdefId = procCaseDefVo.getAppFlowdefId();
        ActTplAppTrigger query = new ActTplAppTrigger();
        query.setIsDeleted("0");
        query.setTriggerAppId(appId);
        query.setAppFlowdefId(appFlowdefId);
        List<ActTplAppTrigger> actTplAppTriggers = null;
        try {
            actTplAppTriggers = actTplAppTriggerMapper.listActTplAppTrigger(query);
            if(actTplAppTriggers.size() == 0 && StringUtils.isBlank(procCaseDefVo.getItemName())){
                //如果没有配置子流程则可能已经是事项流程，需要判断是否有父流程触发了当前流程
                query.setAppFlowdefId(pId);
                query.setTriggerAppFlowdefId(appFlowdefId);
                List<ActTplAppTrigger> actTplAppTriggers1 = actTplAppTriggerMapper.listActTplAppTrigger(query);
                if(actTplAppTriggers1.size() > 0){
                    fillItemName(procCaseDefVo,actTplAppTriggers1.get(0).getBusRecordId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //获取流程节点信息
        String defKey = procCaseDefVo.getDefKey();
        Collection<FlowElement> taskNodeList = aeaItemAdminService.getTaskNodeList(defKey);
        if(taskNodeList != null && taskNodeList.size() > 0){
            for(FlowElement flowElement : taskNodeList){
                AppProcCaseDefTreeVo nodeVo = new AppProcCaseDefTreeVo();
                nodeVo.setDefName(flowElement.getName());
                nodeVo.setId(appFlowdefId + "_" + flowElement.getId());
                nodeVo.setTreeNodeId(procCaseDefVo.getTreeNodeId() + "_" + flowElement.getId());
                nodeVo.setModelId(flowElement.getId());//注意：对于节点来说modelId存的是节点的定义id
                nodeVo.setPid(procCaseDefVo.getTreeNodeId());
                nodeVo.setIsProcess("0");
                nodeVo.setDefKey(defKey);
                nodeVo.setAppFlowdefId(appFlowdefId);
                result.add(nodeVo);
                //填充节点时限信息
                fillNodeTimelimit(nodeVo);
                for (ActTplAppTrigger trigger : actTplAppTriggers) {
                    //被触发的子流程
                    String triggerAppFlowdefId = trigger.getTriggerAppFlowdefId();
                    //触发流程的节点
                    String triggerElementId = trigger.getTriggerElementId();
                    if(flowElement.getId().equals(triggerElementId)) {
                        //查看当前节点是否触发了子流程
                        AppProcCaseDefTreeVo subAppProcCaseDefVo = getFromListByAppFlowdefId(allList, triggerAppFlowdefId);
                        if (subAppProcCaseDefVo != null) {
                            //如果接到触发了子流程，则往下递归，并获取流程关联的事项
                            fillItemName(subAppProcCaseDefVo, trigger.getBusRecordId());
                            //如果当前流程已经被挂到某个节点下了，则拷贝一份，挂到当前节点下
                            if(StringUtils.isNotBlank(subAppProcCaseDefVo.getPid())){
                                AppProcCaseDefTreeVo copy = new AppProcCaseDefTreeVo();
                                BeanUtils.copyProperties(subAppProcCaseDefVo,copy);
                                copy.setTreeNodeId(nodeVo.getModelId() + copy.getTreeNodeId());
                                subAppProcCaseDefVo = copy;
                            }else{
                                AppProcCaseDefTreeVo hasVo = getFromListByAppFlowdefId(result, triggerAppFlowdefId);
                                if(hasVo != null){
                                    //如果当前节点已经遍历过了，则 1、添加到当前节点下/ 2、如果是主流程不挂着其他流程节点下，子流程扫描关系没删除。
                                    // 不需要再遍历了 todo 这里可能要优化。
                                    if(!"1".equals(hasVo.getIsMasterDef())){
                                        hasVo.setPid(nodeVo.getTreeNodeId());
                                    }
                                    continue;
                                }
                            }
                            //递归往下遍历
                            buildProcTreeByPid(appId, nodeVo.getTreeNodeId(), subAppProcCaseDefVo, result, allList);
                        }
                    }
                }
            }
        }
    }

    /**
     * 填充节点时限信息
     * @param nodeVo
     */
    private void fillNodeTimelimit(AppProcCaseDefTreeVo nodeVo){
        ActStoTimegroupAct query = new ActStoTimegroupAct();
        query.setAppFlowdefId(nodeVo.getAppFlowdefId());
        query.setActId(nodeVo.getModelId());
        try {
            //查询节点时限配置表
            List<ActStoTimegroupAct> actStoTimegroupActs = actStoTimegroupActMapper.listActStoTimegroupAct(query);
            if(actStoTimegroupActs != null && actStoTimegroupActs.size() > 0){
                ActStoTimegroupAct actStoTimegroupAct = actStoTimegroupActs.get(0);
                nodeVo.setTimeLimit(actStoTimegroupAct.getTimeLimit().intValue());
                if(StringUtils.isNotBlank(actStoTimegroupAct.getTimeruleId())){
                    //查询时限规则
                    ActStoTimerule actStoTimerule = actStoTimeruleMapper.getActStoTimeruleById(actStoTimegroupAct.getTimeruleId());
                    if(actStoTimerule != null){
                        nodeVo.setTimeLimitUnit(actStoTimerule.getTimeruleUnit());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 填充模板流程定义绑定的事项名称
     * @param subAppProcCaseDefVo
     * @param busRecordId
     */
    private void fillItemName(AppProcCaseDefTreeVo subAppProcCaseDefVo, String busRecordId) {
        if(StringUtils.isNotBlank(busRecordId)){
            AeaItemBasic aeaItemBasic = aeaItemBasicMapper.getAeaItemBasicByItemVerId(busRecordId, SecurityContext.getCurrentOrgId());
            if(aeaItemBasic != null){
                if("0".equals(aeaItemBasic.getIsCatalog())) {
                    //实施事项需要查询承办部门
                    OpuOmOrg activeOrg = opuOmOrgMapper.getActiveOrg(aeaItemBasic.getOrgId());
                    if(activeOrg != null){
                        subAppProcCaseDefVo.setItemApproveOrg(activeOrg.getOrgName());
                    }
                }
                subAppProcCaseDefVo.setItemType(aeaItemBasic.getIsCatalog());
                subAppProcCaseDefVo.setItemName(aeaItemBasic.getItemName());
            }
        }
    }

    /**
     * 根据模板流程定义id获取流程定义信息
     * @param list
     * @param appFlowdefId
     * @return
     */
    private AppProcCaseDefTreeVo getFromListByAppFlowdefId(List<AppProcCaseDefTreeVo> list,String appFlowdefId){
        for(AppProcCaseDefTreeVo vo : list){
            if(vo.getAppFlowdefId().equals(appFlowdefId)){
                return vo;
            }
        }
        return null;
    }

    /**
     * 添加基本视图
     */
    private void addBasicViewToTplApp(String appId) {
        ActStoView query = new ActStoView();
        query.setIsAppView("1");
        query.setIsActive("1");
        query.setViewOrgId(SecurityContext.getCurrentOrgId());
        //获取需要关联的视图
        String[] viewList = ActTplAdminConstants.VIEWCODELIST;

        if (viewList != null && viewList.length > 0) {
            for (String code : viewList) {
                query.setViewCode(code);
                List<ActStoView> actStoViews = actStoViewMapper.listActStoView(query);
                if (actStoViews != null && actStoViews.size() > 0) {
                    ActStoView view = actStoViews.get(0);
                    if (view != null) {
                        createActTplAppViewWithStoViewId(appId, view.getViewId());
                    }
                }
            }
        }
    }

    private void createActTplAppViewWithStoViewId(String appId, String stoViewId) {
        ActTplAppView actTplAppView = new ActTplAppView();
        actTplAppView.setAppId(appId);
        actTplAppView.setViewId(stoViewId);
        actTplAppView.setAppViewId(UUID.randomUUID().toString());
        actTplAppView.setCreater(SecurityContext.getCurrentUserId());
        actTplAppView.setCreateTime(new Date());
        actTplAppViewMapper.insertActTplAppView(actTplAppView);
    }


    /**
     * 保存表单（基本信息/申报信息，批文批复）
     *
     * @param isSeries 1表示串联 0表示并联
     */
    private void addActTplAppFormInfo(String appId, String isSeries) {
        String[] formList = ActTplAdminConstants.FORMCODELIST;

        if (formList != null && formList.length > 0) {
            ActStoForm actStoForm = new ActStoForm();
            actStoForm.setFormOrgId(SecurityContext.getCurrentOrgId());
            actStoForm.setIsDeleted("0");
            for (int i = 0; i < formList.length; i++) {
                String code = formList[i];
                actStoForm.setFormCode(code);
                List<ActStoForm> actStoForms = actStoFormMapper.listActStoForm(actStoForm);
                if(actStoForms == null || actStoForms.size() == 0){
                    continue;
                }
                ActStoForm stoForm = actStoForms.get(0);
                if (stoForm != null) {
                    ActTplAppForm tplAppForm = new ActTplAppForm();
                    tplAppForm.setAppFormId(UUID.randomUUID().toString());
                    tplAppForm.setAppId(appId);
                    tplAppForm.setFormId(stoForm.getFormId());
                    if (i == 0) {
                        tplAppForm.setIsMasterForm("1");
                    } else {
                        tplAppForm.setIsMasterForm("0");
                    }
                    tplAppForm.setPriorityOrder(i);
                    tplAppForm.setCreater(SecurityContext.getCurrentUserId());
                    tplAppForm.setCreateTime(new Date());
                    actTplAppFormMapper.insertActTplAppForm(tplAppForm);
                }
            }
        }
    }

    /**
     * 可见范围--关联东莞市
     */
    private void addActTplAppRangeInfo(String appId) throws Exception {
        ActTplAppRange actTplAppRange = new ActTplAppRange();
        actTplAppRange.setAppId(appId);
        actTplAppRange.setAppRangeId(UUID.randomUUID().toString());
        actTplAppRange.setOrgId(ActTplAdminConstants.DGSORGID);
        actTplAppRangeMapper.insertActTplAppRange(actTplAppRange);
    }

    /**
     * 东莞的业务接口
     * 把阶段和事项的办理期限同步到模板流程定义关联表中
     *
     * @param appId     模板id
     * @param limitTime 期限，数字
     * @param timeUnit  期限单位，d表示天，m表示分钟   东莞的事项如果没有字段标明则默认传 d
     * @param isWorkDay 期限是否是工作日 1是0否  东莞的事项如果没有字段标明则默认传 1
     * @return
     * @throws Exception
     */
    @Override
    public boolean syncActTplAppDefLimitTime(String appId, int limitTime, String timeUnit, String isWorkDay) throws Exception {

        //1、先从triger表查询，判断是阶段还是事项
        boolean isItem = true;
        ActTplAppTrigger triQuery = new ActTplAppTrigger();
        triQuery.setTriggerAppId(appId);
        List<ActTplAppTrigger> actTplAppTriggers = actTplAppTriggerMapper.listActTplAppTrigger(triQuery);
        for (ActTplAppTrigger atat : actTplAppTriggers) {
            //包含事项id则是阶段配置
            if (StringUtils.isNotBlank(atat.getBusRecordId())) {
                isItem = false;
                break;
            }
        }
        //2、查询出所有模板流程定义
        String rootOrgId = SecurityContext.getCurrentOrgId();
        ActTplAppFlowdef query = new ActTplAppFlowdef();
        query.setAppId(appId);
        List<ActTplAppFlowdef> actTplAppFlowdefs = actTplAppFlowdefMapper.listActTplAppFlowdef(query);
        if (isItem) {
            for (ActTplAppFlowdef atlf : actTplAppFlowdefs) {
                updateActTplAppDef(limitTime, timeUnit, isWorkDay, atlf);
            }
        } else {
            for (ActTplAppFlowdef atlf : actTplAppFlowdefs) {
                if ("1".equals(atlf.getIsMaster())) {
                    //并联主流程
                    updateActTplAppDef(limitTime, timeUnit, isWorkDay, atlf);
                } else {
                    for (ActTplAppTrigger atat : actTplAppTriggers) {
                        if (atlf.getAppFlowdefId().equals(atat.getTriggerAppFlowdefId()) && StringUtils.isNotBlank(atat.getBusRecordId())) {
                            AeaItemBasic oneByItemVerId = aeaItemBasicMapper.getOneByItemVerId(atat.getBusRecordId(), rootOrgId);
                            //注释，并联沿用阶段时限刷新页面时限又变为事项时限
                            //AeaItem aeaItemById = aeaItemMapper.getAeaItemById(atat.getBusRecordId());
                            if (oneByItemVerId != null) {
//                                updateActTplAppDef(oneByItemVerId.getDueNum().intValue(),"d","1",atlf);
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    private void updateActTplAppDef(int limitTime, String timeUnit, String isWorkDay, ActTplAppFlowdef atlf) throws Exception {
        //atlf.setLimitIsWorkDay(isWorkDay);
        atlf.setTimeLimit(limitTime);
        //atlf.setTimeLimitUnit(timeUnit);
        atlf.setModifier(SecurityContext.getCurrentUserName());
        atlf.setModifyTime(new Date());
        actTplAppFlowdefMapper.updateActTplAppFlowdef(atlf);
    }

}
