package com.augurit.aplanmis.common.service.admin.par.impl;

import com.augurit.agcloud.bsc.sc.rule.code.service.AutoCodeNumberService;
import com.augurit.agcloud.bsc.util.CommonConstant;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.bsc.domain.MindBaseNode;
import com.augurit.aplanmis.bsc.domain.MindExportData;
import com.augurit.aplanmis.bsc.domain.MindExportObj;
import com.augurit.aplanmis.common.constants.*;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.admin.par.AeaParStateAdminService;
import com.augurit.aplanmis.common.vo.AeaParStateModel;
import com.augurit.aplanmis.common.vo.ItemOfStageState;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 主题情形定义表-Service服务接口实现类
 *
 * @author jjt
 * @date 2019/4/17
 */
@Service
@Transactional
public class AeaParStateAdminServiceImpl implements AeaParStateAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaParStateAdminServiceImpl.class);

    @Autowired
    private AeaParStateMapper aeaParStateMapper;

    @Autowired
    private AeaParStageMapper aeaParStageMapper;

    @Autowired
    private AeaItemMatMapper aeaItemMatMapper;

    @Autowired
    private AeaParInMapper aeaParInMapper;

    @Autowired
    private AeaCertMapper aeaCertMapper;

    @Autowired
    private AeaParStateItemMapper aeaParStateItemMapper;

    @Autowired
    private AeaParStageItemInMapper aeaParStageItemInMapper;

    @Autowired
    private AutoCodeNumberService autoCodeNumberService;

    @Autowired
    private AeaParStateFormMapper aeaParStateFormMapper;

    /**
     * 是否使用递归查询数据库算法
     */
    private final static boolean USE_RECURSION = false;

    @Override
    public void saveAeaParState(AeaParState aeaParState) {

        String userId = SecurityContext.getCurrentUserId();
        String rootOrgId = SecurityContext.getCurrentOrgId();
        String stateSeq = getStateSeq(aeaParState.getParStateId(), aeaParState.getParentStateId());
        Long sortNo = getStateSortNo(rootOrgId);
        aeaParState.setCreater(userId);
        aeaParState.setRootOrgId(rootOrgId);
        aeaParState.setCreateTime(new Date());
        aeaParState.setStateSeq(stateSeq);
        aeaParState.setSortNo(sortNo);
        if (StringUtils.isBlank(aeaParState.getParentStateId())) {
            aeaParState.setParentStateId(null);
        }
        aeaParState.setIsActive(ActiveStatus.ACTIVE.getValue());
        aeaParState.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
        aeaParStateMapper.insertAeaParState(aeaParState);
    }

    @Override
    public void updateAeaParState(AeaParState aeaParState) {

        String userId = SecurityContext.getCurrentUserId();
        String rootOrgId = SecurityContext.getCurrentOrgId();
        String stateSeq = getStateSeq(aeaParState.getParStateId(), aeaParState.getParentStateId());
        aeaParState.setStateSeq(stateSeq);
        aeaParState.setModifier(userId);
        aeaParState.setModifyTime(new Date());
        aeaParStateMapper.updateAeaParState(aeaParState);
        List<AeaParState> allChildStates = aeaParStateMapper.listAllChildStates(aeaParState.getParStateId(), null, rootOrgId);
        if (allChildStates != null && allChildStates.size() > 0) {
            for (AeaParState state : allChildStates) {
                if (StringUtils.isNotBlank(state.getStateSeq())) {
                    String childStateSeq = state.getStateSeq();
                    String itemIds = CommonConstant.SEQ_SEPARATOR + state.getParStateId() + CommonConstant.SEQ_SEPARATOR;
                    int index = childStateSeq.indexOf(itemIds);
                    if (index > -1) {
                        try {
                            childStateSeq = childStateSeq.substring(index + itemIds.length());
                            childStateSeq = stateSeq + childStateSeq;
                            state.setStateSeq(childStateSeq);
                        } catch (Exception e) {
                            System.out.println("此处已经报错...");
                        }
                    } else {
                        state.setStateSeq(stateSeq + state.getParStateId() + CommonConstant.SEQ_SEPARATOR);
                    }
                }
                state.setModifier(SecurityContext.getCurrentUserId());
                state.setModifyTime(new Date());
                aeaParStateMapper.updateAeaParState(state);
            }
        }
    }

    /**
     * 上过上级Id获取当前事项的排序
     * @return
     */
    private Long getStateSortNo(String rootOrgId) {

        if(StringUtils.isBlank(rootOrgId)){
            rootOrgId = SecurityContext.getCurrentOrgId();
        }
        Long sortNo = aeaParStateMapper.getMaxSortNo(rootOrgId);
        if (sortNo == null) {
            sortNo = 1L;
        }
        return sortNo + 1;
    }

    /**
     * 通过上级情形Id获取当前情形的序列
     */
    private String getStateSeq(String stateId, String parentStateId) {

        if (StringUtils.isNotBlank(parentStateId)) {
            //获取上级菜单的序列
            AeaParState parentState = aeaParStateMapper.getAeaParStateById(parentStateId);
            String parentStateSeq = CommonConstant.SEQ_SEPARATOR;
            if (parentState != null && StringUtils.isNotBlank(parentState.getStateSeq())) {
                parentStateSeq = parentState.getStateSeq();
            }
            return parentStateSeq + stateId + CommonConstant.SEQ_SEPARATOR;
        } else {
            return CommonConstant.SEQ_SEPARATOR + stateId + CommonConstant.SEQ_SEPARATOR;
        }
    }

    @Override
    public void deleteAeaParStateById(String id) {

        aeaParStateMapper.deleteAeaParState(id);
        List<AeaParState> allChildStates = aeaParStateMapper.listAllChildStates(id, null, SecurityContext.getCurrentOrgId());
        if (allChildStates != null && allChildStates.size() > 0) {
            for (AeaParState state : allChildStates) {
                aeaParStateMapper.deleteAeaParState(state.getParStateId());
            }
        }
    }

    @Override
    public void batchDeleteAeaParStateByIds(String[] ids) {

        if (ids != null && ids.length > 0) {
            String rootOrgId = SecurityContext.getCurrentOrgId();
            for (String id : ids) {
                aeaParStateMapper.deleteAeaParState(id);
                List<AeaParState> allChildStates = aeaParStateMapper.listAllChildStates(id, null, rootOrgId);
                if (allChildStates != null && allChildStates.size() > 0) {
                    for (AeaParState state : allChildStates) {
                        aeaParStateMapper.deleteAeaParState(state.getParStateId());
                    }
                }
            }
        }
    }

    @Override
    public PageInfo<AeaParState> listAeaParState(AeaParState aeaParState, Page page) {

        aeaParState.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaParState> list = aeaParStateMapper.listAeaParState(aeaParState);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<AeaParState> listAllChildStates(String parStateId, String keyword, String rootOrgId, Page page) {

        if(StringUtils.isBlank(rootOrgId)){
            rootOrgId = SecurityContext.getCurrentOrgId();
        }
        PageHelper.startPage(page);
        List<AeaParState> list = aeaParStateMapper.listAllChildStates(parStateId, keyword, rootOrgId);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public List<AeaParState> listAllChildStates(String parStateId, String keyword, String rootOrgId) {

        if(StringUtils.isBlank(rootOrgId)){
            rootOrgId = SecurityContext.getCurrentOrgId();
        }
        return aeaParStateMapper.listAllChildStates(parStateId, keyword, rootOrgId);
    }

    @Override
    public List<AeaParStateModel> listAeaParStateByStageIdUsedForStateItem(String stageId) {

        if (StringUtils.isNotBlank(stageId)) {
            AeaParStage aeaParStage = aeaParStageMapper.getAeaParStageById(stageId);
            if (aeaParStage != null) {
                String rootOrgId = SecurityContext.getCurrentOrgId();
                List<AeaParState> topStateList = aeaParStateMapper.listRootAeaParStateByStageId(stageId, rootOrgId);
                if (topStateList != null && !topStateList.isEmpty()) {
                    sortAeaParStateBySortNo(topStateList);
                    List<AeaParStateModel> returnList = new ArrayList<>();
                    for (AeaParState topState : topStateList) {
                        AeaParStateModel aeaParStateModel = new AeaParStateModel();
                        BeanUtils.copyProperties(topState, aeaParStateModel);
                        loadAeaParStateModel(aeaParStateModel, stageId, rootOrgId);
                        returnList.add(aeaParStateModel);
                    }
                    return returnList;
                }
            }
        }
        return new ArrayList<>();
    }

    private void loadAeaParStateModel(AeaParStateModel aeaParStateModel, String stageId, String rootOrgId) {

        //获取子情形
        List<AeaParState> stateChildList = aeaParStateMapper.listAeaParStateByParentStateIdAndStageId(stageId, aeaParStateModel.getParStateId(), rootOrgId);
        if (stateChildList != null && !stateChildList.isEmpty()) {
            sortAeaParStateBySortNo(stateChildList);
            //情形下情形子节点
            List<AeaParStateModel> stateNodeList = new ArrayList<>();
            for (AeaParState child : stateChildList) {
                AeaParStateModel parStateModel = new AeaParStateModel();
                BeanUtils.copyProperties(child, parStateModel);
                stateNodeList.add(parStateModel);
                loadAeaParStateModel(parStateModel, stageId, rootOrgId);
            }
            aeaParStateModel.setList(stateNodeList);
        }
    }

    @Override
    public AeaParState getAeaParStateById(String id) {

        Assert.notNull(id, "id is null.");
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaParStateMapper.getAeaParStateById(id);
    }

    @Override
    public List<AeaParState> listAeaParState(AeaParState aeaParState) {

        aeaParState.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaParState> list = aeaParStateMapper.listAeaParState(aeaParState);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public Long getMaxSortNo(String rootOrgId) {

        if(StringUtils.isBlank(rootOrgId)){
            rootOrgId = SecurityContext.getCurrentOrgId();
        }
        Long sortNo = aeaParStateMapper.getMaxSortNo(rootOrgId);
        return sortNo == null ? 1L : (sortNo + 1);
    }

    @Override
    public List<AeaParState> listAeaParStateByStageId(String stageId, String rootOrgId) {

        return aeaParStateMapper.listAeaParStateByStageId(stageId, rootOrgId);
    }

    @Override
    public MindBaseNode listAeaStageStateMindView(String stageId, AeaMindUi aeaMindUi, String rootOrgId) {

        if (StringUtils.isNotBlank(stageId)) {
            // 获取阶段数据
            AeaParStage aeaParStage = aeaParStageMapper.getAeaParStageById(stageId);
            if (aeaParStage != null) {

                // 转换阶段数据成mind节点
                MindBaseNode mindBaseNode = getStageNode(aeaParStage);

                // 声明存储集合
                List<AeaParState> topStateList;
                List<AeaParState> allStateList;
                List<AeaParIn> allMatList = null;
                List<AeaParIn> allCertList = null;
                List<AeaParIn> allMatCommonList = null;
                List<AeaParIn> allCertCommonList = null;
                List<AeaParStateItem> stageStateItemList = null;
                List<AeaParStateForm> stageStateFormList = null;
                List<AeaParStateForm> allStageCommonFormList = null;
                AeaParStateForm form = new AeaParStateForm();

                if (USE_RECURSION) {
                    topStateList = aeaParStateMapper.listRootAeaParStateByStageId(stageId, rootOrgId);
                } else {
                    // 获取阶段情形节点
                    allStateList = aeaParStateMapper.listAeaParStateByStageId(stageId, rootOrgId);
                    if(aeaMindUi!=null&&aeaMindUi.isShowMat()){
                        // 获取阶段情形材料
                        allMatList = aeaParInMapper.listInStateMatByStageId(stageId, null, rootOrgId);
                        // 获取阶段通用材料
                        allMatCommonList = aeaParInMapper.listStageMat(stageId, "", Status.ON , "0", "", rootOrgId);
                    }
                    if(aeaMindUi!=null&&aeaMindUi.isShowCert()){
                        // 获取阶段情形证照
                        allCertList = aeaParInMapper.listInStateCertByStageId(stageId, null, rootOrgId);
                        // 获取阶段通用证照
                        allCertCommonList =  aeaParInMapper.listNoStateInCertByStageId(stageId, null , rootOrgId);
                    }
                    if(aeaMindUi!=null&&aeaMindUi.isShowSituationLinkItem()){
                        // 获取阶段情形事项
                        stageStateItemList = aeaParStateItemMapper.listStageStateItem(null, stageId, rootOrgId);
                    }
                    if(aeaMindUi!=null&&aeaMindUi.isShowForm()){
                        // 获取阶段情形表单数据
                        form.setParStageId(stageId);
                        form.setIsStateForm(Status.ON);
                        stageStateFormList = aeaParStateFormMapper.listStageStateFormWithActStoForm(form);

                        form.setIsStateForm(Status.OFF);
                        allStageCommonFormList = aeaParStateFormMapper.listStageStateFormWithActStoForm(form);
                    }
                    // 获取阶段顶级情形数据
                    topStateList = getTopAeaParStateList(allStateList);
                }

                //阶段下的顶级节点集合
                List<MindBaseNode> topNodeList = new ArrayList<>();
                // 获取阶段顶级情形数据
                if (topStateList != null && !topStateList.isEmpty()) {
                    sortAeaParStateBySortNo(topStateList);
                    for (AeaParState topState : topStateList) {
                        // 转换情形阶段数据为mind节点
                        MindBaseNode mindSituationNode = getChildStateNode(topState);
                        topNodeList.add(mindSituationNode);
                        loadChildStateList(aeaParStage.getStageId(), mindSituationNode, USE_RECURSION, aeaMindUi, allStateList, allMatList, allCertList, stageStateItemList, stageStateFormList, rootOrgId);
                    }
                } else {
                    mindBaseNode.setOpen(MindConst.MIND_NODE_EXPAND_FALSE);
                }
                // 转换通用材料为mind节点
                mindBaseNode.setChilds(listToArray(getChildStageNodeOfCommonMatCert(stageId, topNodeList, allMatCommonList)));
                // 转换通用证照为mind节点
                mindBaseNode.setChilds(listToArray(getChildStageNodeOfCommonMatCert(stageId, topNodeList, allCertCommonList)));
                // 转换通用表单为mind节点
                mindBaseNode.setChilds(listToArray(getChildStageNodeOfCommonForm(stageId, topNodeList, allStageCommonFormList)));
                return mindBaseNode;
            }
        }
        return null;
    }

    private List<MindBaseNode> getChildStageNodeOfCommonMatCert(String stageId, List<MindBaseNode> topNodeList, List<AeaParIn> allMatCommonList){

        if(allMatCommonList!=null&&allMatCommonList.size()>0) {
            for (AeaParIn aeaParIn:allMatCommonList) {

                MindBaseNode mindMatCertNode = new MindBaseNode();
                if(StringUtils.isNotBlank(aeaParIn.getFileType())){
                    if(aeaParIn.getFileType().equals(MindType.MATERIAL.getValue())){
                        mindMatCertNode.setId(aeaParIn.getMatId());
                    }else if(aeaParIn.getFileType().equals(MindType.CERTIFICATE.getValue())){
                        mindMatCertNode.setId(aeaParIn.getCertId());
                    }
                }
                mindMatCertNode.setName(aeaParIn.getAeaMatCertName());
                //父节点id
                mindMatCertNode.setPid(stageId);
                mindMatCertNode.setOpen(MindConst.MIND_NODE_EXPAND_FALSE);
                if(AeaMindConst.MIND_NODE_TYPE_CODE_MAT.equals(aeaParIn.getFileType())){
                    mindMatCertNode.setNodeTypeCode(AeaMindConst.MIND_NODE_TYPE_CODE_MAT);
                    //设置图标（3表示材料）
                    mindMatCertNode.setPriority(AeaMindConst.MIND_NODE_PRIORITY_MAPPING_MAT);
                }else {
                    mindMatCertNode.setNodeTypeCode(AeaMindConst.MIND_NODE_TYPE_CODE_CERT);
                    //设置图标（3表示材料）
                    mindMatCertNode.setPriority(AeaMindConst.MIND_NODE_PRIORITY_MAPPING_CERT);
                }
                topNodeList.add(mindMatCertNode);
            }
        }
        return topNodeList;
    }

    private List<MindBaseNode> getChildStageNodeOfCommonForm(String stageId, List<MindBaseNode> topNodeList, List<AeaParStateForm> allFormCommonList){

        if(allFormCommonList!=null&&allFormCommonList.size()>0) {
            for (AeaParStateForm form : allFormCommonList) {
                MindBaseNode mindFormNode = new MindBaseNode();
                mindFormNode.setId(form.getStateFormId());
                mindFormNode.setName(form.getFormName());
                mindFormNode.setPid(form.getParStateId());
                mindFormNode.setOpen(MindConst.MIND_NODE_EXPAND_FALSE);
                mindFormNode.setNodeTypeCode(AeaMindConst.MIND_NODE_TYPE_CODE_FORM);
                mindFormNode.setPriority(AeaMindConst.MIND_NODE_PRIORITY_MAPPING_FORM);
                Map<String, String> map = new HashMap<>();
                map.put("formId", form.getFormId());
                map.put(AeaMindConst.MIND_NODE_EXTRA_KEY_FORM_PROPERTY, form.getFormProperty());
                mindFormNode.setExtra(map);
                topNodeList.add(mindFormNode);
            }
        }
        return topNodeList;
    }

    private List<AeaParState> getTopAeaParStateList(List<AeaParState> allStateList) {

        List<AeaParState> topAeaParStateList = new ArrayList<>();
        if (allStateList != null && !allStateList.isEmpty()) {
            Iterator<AeaParState> iterator = allStateList.iterator();
            while (iterator.hasNext()) {
                AeaParState aeaParState = iterator.next();
                String parentStateId = aeaParState.getParentStateId();

                if (parentStateId == null || parentStateId.isEmpty()) {
                    topAeaParStateList.add(aeaParState);
                    iterator.remove();
                }
            }
        }
        return topAeaParStateList;
    }

    private List<AeaParState> getChildAeaParStateList(String parentStateId, List<AeaParState> allStateList) {

        List<AeaParState> childAeaParStateList = new ArrayList<>();
        if (StringUtils.isNotBlank(parentStateId) && CollectionUtils.isNotEmpty(allStateList)) {
            Iterator<AeaParState> iterator = allStateList.iterator();
            while (iterator.hasNext()) {
                AeaParState aeaParState = iterator.next();

                if (parentStateId.equals(aeaParState.getParentStateId())) {
                    childAeaParStateList.add(aeaParState);
                    iterator.remove();
                }

            }
        }
        return childAeaParStateList;
    }

    private List<AeaParIn> getChildAeaParInList(String parStateId, List<AeaParIn> allParInList) {

        List<AeaParIn> childParInList = new ArrayList<>();
        if (parStateId != null && allParInList != null && !allParInList.isEmpty()) {
            Iterator<AeaParIn> iterator = allParInList.iterator();
            while (iterator.hasNext()) {
                AeaParIn aeaParIn = iterator.next();

                if (parStateId.equals(aeaParIn.getParStateId())) {
                    childParInList.add(aeaParIn);
                    iterator.remove();
                }
            }
        }
        return childParInList;
    }

    /**
     * 获取子集情形事项
     * @param parStateId
     * @param allStateItemList
     * @return
     */
    private List<AeaParStateItem> getChildAeaParStateItemList(String parStateId, List<AeaParStateItem> allStateItemList) {

        List<AeaParStateItem> childStateItemList = new ArrayList<>();
        if (StringUtils.isNotBlank(parStateId) && allStateItemList != null && !allStateItemList.isEmpty()) {
            Iterator<AeaParStateItem> iterator = allStateItemList.iterator();
            while (iterator.hasNext()) {
                AeaParStateItem stateItem = iterator.next();
                if (parStateId.equals(stateItem.getParStateId())) {
                    childStateItemList.add(stateItem);
                    iterator.remove();
                }
            }
        }
        return childStateItemList;
    }

    private List<AeaParStateForm> getChildAeaParStateFormList(String parStateId, List<AeaParStateForm> allStateFormList) {

        List<AeaParStateForm> childStateItemList = new ArrayList<>();
        if (StringUtils.isNotBlank(parStateId) && allStateFormList != null && !allStateFormList.isEmpty()) {
            Iterator<AeaParStateForm> iterator = allStateFormList.iterator();
            while (iterator.hasNext()) {
                AeaParStateForm stateForm = iterator.next();
                if (StringUtils.isNotBlank(stateForm.getIsStateForm())&&
                    stateForm.getIsStateForm().equals("1")&&
                    parStateId.equals(stateForm.getParStateId())) {
                    childStateItemList.add(stateForm);
                    iterator.remove();
                }
            }
        }
        return childStateItemList;
    }

    private void sortAeaParStateBySortNo(List<AeaParState> list) {

        list.sort(Comparator.comparing(aeaParState -> (aeaParState.getSortNo())));
    }

    private MindBaseNode getStageNode(AeaParStage aeaParStage) {

        MindBaseNode mindBaseNode = new MindBaseNode();
        mindBaseNode.setId(aeaParStage.getStageId());
        mindBaseNode.setName(aeaParStage.getStageName());
        mindBaseNode.setPid(MindConst.MIND_NODE_ROOT);
        mindBaseNode.setOpen(MindConst.MIND_NODE_EXPAND_TRUE);
        mindBaseNode.setNodeTypeCode(AeaMindConst.MIND_NODE_TYPE_CODE_STAGE);
        return mindBaseNode;
    }

    private String getMindViewPriority(AeaParState aeaParState) {

        if (aeaParState != null) {
            if (Status.ON.equals(aeaParState.getIsQuestion()) && "m".equals(aeaParState.getAnswerType())) {
                return AeaMindConst.MIND_NODE_PRIORITY_MAPPING_REQUIRED;
            }
        }
        return null;
    }

    private String getMindViewProgress(AeaParState aeaParState) {

        if (aeaParState != null) {
            if (Status.ON.equals(aeaParState.getIsQuestion()) && Status.ON.equals(aeaParState.getMustAnswer())) {
                return AeaMindConst.MIND_NODE_PROGRESS_MAPPING_REQUIRED;
            }
        }
        return null;
    }

    private void loadChildStateList(String stageId, MindBaseNode node, boolean useRecursion, AeaMindUi aeaMindUi,
                                    List<AeaParState> allStateList, List<AeaParIn> allMatList, List<AeaParIn> allCertList,
                                    List<AeaParStateItem> allStateItemList, List<AeaParStateForm> allStateFormList, String rootOrgId) {

        //情形下所有子节点
        List<MindBaseNode> allChildList = new ArrayList<>();
        List<AeaParState> stateChildList;
        if (useRecursion) {
            stateChildList = aeaParStateMapper.listAeaParStateByParentStateIdAndStageId(stageId, node.getId(), rootOrgId);
        } else {
            stateChildList = getChildAeaParStateList(node.getId(), allStateList);
        }

        if (stateChildList != null && !stateChildList.isEmpty()) {
            // 排序
            sortAeaParStateBySortNo(stateChildList);
            //情形下情形子节点
            List<MindBaseNode> stateNodeList = new ArrayList<>();
            for (AeaParState child : stateChildList) {
                MindBaseNode mindSituationNode = getChildStateNode(child);
                stateNodeList.add(mindSituationNode);
                loadChildStateList(stageId, mindSituationNode, useRecursion, aeaMindUi, allStateList, allMatList, allCertList, allStateItemList, allStateFormList, rootOrgId);
            }
            allChildList.addAll(stateNodeList);
        }

        //情形下材料子节点
        if(aeaMindUi!=null&&aeaMindUi.isShowMat()){
            List<AeaParIn> matInList;
            if (useRecursion) {
                matInList = aeaParInMapper.listInStateMatByStageIdAndStateId(stageId, node.getId(), null, rootOrgId);
            } else {
                matInList = getChildAeaParInList(node.getId(), allMatList);
            }
            if (matInList != null && !matInList.isEmpty()) {
                for (AeaParIn child : matInList) {
                    allChildList.add(getChildParInNode(child, true, useRecursion));
                }
            }
        }

        //情形下证照子节点
        if(aeaMindUi!=null&&aeaMindUi.isShowCert()){
            List<AeaParIn> certInList;
            if (useRecursion) {
                certInList = aeaParInMapper.listInStateCertByStageIdAndStateId(stageId, node.getId(), null, rootOrgId);
            } else {
                certInList = getChildAeaParInList(node.getId(), allCertList);
            }
            if (certInList != null && !certInList.isEmpty()) {
                for (AeaParIn child : certInList) {
                    allChildList.add(getChildParInNode(child, false, useRecursion));
                }
            }
        }

        //情形下显示事项节点
        if(aeaMindUi!=null&&aeaMindUi.isShowSituationLinkItem()){
            List<AeaParStateItem> stateItemList;
            if (useRecursion) {
                stateItemList = aeaParStateItemMapper.listStageStateItem(node.getId(), stageId, rootOrgId);
            } else {
                stateItemList = getChildAeaParStateItemList(node.getId(), allStateItemList);
            }
            if (stateItemList != null && !stateItemList.isEmpty()) {
                for (AeaParStateItem child : stateItemList) {
                    allChildList.add(convertChildStateItemNode(child));
                }
            }
        }

        //情形下显示表单节点
        if(aeaMindUi!=null&&aeaMindUi.isShowForm()){
            List<AeaParStateForm> stateFormList;
            if (useRecursion) {
                AeaParStateForm form = new AeaParStateForm();
                form.setParStageId(stageId);
                form.setParStateId(node.getId());
                form.setIsStateForm("1");
                stateFormList = aeaParStateFormMapper.listStageStateFormWithActStoForm(form);
            } else {
                stateFormList = getChildAeaParStateFormList(node.getId(), allStateFormList);
            }
            if (stateFormList != null && !stateFormList.isEmpty()) {
                for (AeaParStateForm child : stateFormList) {
                    allChildList.add(convertChildStateFormNode(child));
                }
            }
        }

        if (!allChildList.isEmpty()) {
            node.setChilds(listToArray(allChildList));
        } else {
            node.setOpen(MindConst.MIND_NODE_EXPAND_FALSE);
        }
    }

    private void loadItemOfStageState(MindBaseNode mindBaseNode, List<ItemOfStageState> itemOfStageStateList){

        MindBaseNode node = new MindBaseNode();
        List<MindBaseNode> listNode = new ArrayList<>();
        for (ItemOfStageState itemOfStageState:itemOfStageStateList){
            node.setPid(itemOfStageState.getParStateId());
            node.setOpen(MindConst.MIND_NODE_EXPAND_FALSE);
            node.setId(itemOfStageState.getItemVerId());
            node.setName(itemOfStageState.getItemName());
            node.setNodeTypeCode(AeaMindConst.MIND_NODE_TYPE_CODE_ITEM);
            listNode.add(node);
        }
        mindBaseNode.setChilds(listToArray(listNode));
    }

    private MindBaseNode getChildParInNode(AeaParIn aeaParIn, boolean isMat, boolean getStageItemIn) {

        MindBaseNode mindMatNode = new MindBaseNode();
        mindMatNode.setPid(aeaParIn.getParStateId());
        mindMatNode.setName(aeaParIn.getAeaMatCertName());
        mindMatNode.setOpen(MindConst.MIND_NODE_EXPAND_FALSE);
        Map<String, String> map = new HashMap<>();
        map.put("inId", aeaParIn.getInId());
        mindMatNode.setExtra(map);

        if (isMat) {
            mindMatNode.setId(aeaParIn.getMatId());
            mindMatNode.setNodeTypeCode(AeaMindConst.MIND_NODE_TYPE_CODE_MAT);

            if (Status.ON.equals(aeaParIn.getIsGlobalShare())) {
                mindMatNode.setIsGlobal(aeaParIn.getIsGlobalShare());
            } else {
                mindMatNode.setIsGlobal(Status.OFF);
            }
            mindMatNode.setPriority(AeaMindConst.MIND_NODE_PRIORITY_MAPPING_MAT);
        } else {
            mindMatNode.setId(aeaParIn.getCertId());
            mindMatNode.setNodeTypeCode(AeaMindConst.MIND_NODE_TYPE_CODE_CERT);
            mindMatNode.setPriority(AeaMindConst.MIND_NODE_PRIORITY_MAPPING_CERT);

        }
        mindMatNode.setNote(aeaParIn.getAeaMatCertMemo());
        return mindMatNode;
    }

    /**
     * 转换情形事项节点
     *
     * @param stateItem
     * @return
     */
    private MindBaseNode convertChildStateItemNode(AeaParStateItem stateItem) {

        MindBaseNode mindMatNode = new MindBaseNode();
        String itemName = stateItem.getItemName();
        if (StringUtils.isNotBlank(stateItem.getIsCatalog())) {
            // 标准事项
            if(stateItem.getIsCatalog().equals(Status.ON)){
                itemName = ("【标准事项】" + itemName);
            // 实施事项
            }else{
                itemName = ("【实施事项】" + itemName);
            }
        }
        mindMatNode.setId(stateItem.getStateItemId());
        mindMatNode.setName(itemName);
        mindMatNode.setPid(stateItem.getParStateId());
        mindMatNode.setOpen(MindConst.MIND_NODE_EXPAND_FALSE);
        mindMatNode.setNodeTypeCode(AeaMindConst.MIND_NODE_TYPE_CODE_SITUATION_LINK_ITEM);
        mindMatNode.setPriority(AeaMindConst.MIND_NODE_PRIORITY_MAPPING_SITUATION_LINK_ITEM);
        Map<String, String> map = new HashMap<>();
        map.put("itemVerId", stateItem.getItemVerId());
        mindMatNode.setExtra(map);
        return mindMatNode;
    }

    /**
     * 转换情形表单节点
     *
     * @param stateForm
     * @return
     */
    private MindBaseNode convertChildStateFormNode(AeaParStateForm stateForm) {

        MindBaseNode mindMatNode = new MindBaseNode();
        mindMatNode.setId(stateForm.getStateFormId());
        mindMatNode.setName(stateForm.getFormName());
        mindMatNode.setPid(stateForm.getParStateId());
        mindMatNode.setOpen(MindConst.MIND_NODE_EXPAND_FALSE);
        mindMatNode.setNodeTypeCode(AeaMindConst.MIND_NODE_TYPE_CODE_FORM);
        mindMatNode.setPriority(AeaMindConst.MIND_NODE_PRIORITY_MAPPING_FORM);
        Map<String, String> map = new HashMap<>();
        map.put("formId", stateForm.getFormId());
        map.put(AeaMindConst.MIND_NODE_EXTRA_KEY_FORM_PROPERTY, stateForm.getFormProperty());
        mindMatNode.setExtra(map);
        return mindMatNode;
    }

    private MindBaseNode getChildStateNode(AeaParState state) {

        MindBaseNode mindSituationNode = new MindBaseNode();
        mindSituationNode.setId(state.getParStateId());
        mindSituationNode.setName(state.getStateName());
        mindSituationNode.setPid(state.getStageId());
        mindSituationNode.setOpen(MindConst.MIND_NODE_EXPAND_TRUE);
        mindSituationNode.setNodeTypeCode(AeaMindConst.MIND_NODE_TYPE_CODE_SITUATION);
        mindSituationNode.setPriority(getMindViewPriority(state));
        mindSituationNode.setProgress(getMindViewProgress(state));
        mindSituationNode.setNote(state.getStateMemo());
        mindSituationNode.setLinkProcessStart(state.getIsProcStartCond());
        return mindSituationNode;
    }

    private MindBaseNode[] listToArray(List<? extends MindBaseNode> list) {

        if (list != null && !list.isEmpty()) {
            MindBaseNode[] arr = new MindBaseNode[list.size()];
            for (int i = 0; i < list.size(); i++) {
                arr[i] = list.get(i);
            }
            return arr;
        }
        return null;
    }

    @Override
    public void saveOrUpdateAeaStageStateMindView(MindExportObj mindExportObj, String rootOrgId) throws Exception {

        if (mindExportObj != null) {
            if(StringUtils.isBlank(rootOrgId)){
                rootOrgId = SecurityContext.getCurrentOrgId();
            }
            checkMindExportObj(mindExportObj);
            MindExportData root = mindExportObj.getData();
            if (root != null && AeaMindConst.MIND_NODE_TYPE_CODE_STAGE.equals(root.getNodeTypeCode())) {
                AeaParStage aeaParStage = aeaParStageMapper.getAeaParStageById(root.getId());
                if (aeaParStage != null) {
                    List<Object> allDeleteList = new ArrayList<>();
                    loadStageNode(aeaParStage, null, mindExportObj.getChildren(), allDeleteList, rootOrgId);
                    deleteNodeList(allDeleteList, rootOrgId);
                }
            }
        }
    }

    private void deleteNodeList(List<Object> allDeleteList, String rootOgrId) {

        if (CollectionUtils.isNotEmpty(allDeleteList)) {
            for (Object obj : allDeleteList) {
                if (obj instanceof AeaParIn) {
                    AeaParIn parIn = (AeaParIn) obj;
                    if (MindType.MATERIAL.getValue().equals(parIn.getFileType())) {
                        deleteAeaParInMat(parIn);
                    } else {
                        deleteAeaParInCert(parIn);
                    }
                } else if (obj instanceof AeaParState) {
                    AeaParState aeaParState = (AeaParState) obj;
                    aeaParState.setIsDeleted(DeletedStatus.DELETED.getValue());
                    aeaParState.setModifier(SecurityContext.getCurrentUserId());
                    aeaParState.setModifyTime(new Date());
                    aeaParStateMapper.updateAeaParState(aeaParState);
                    aeaParStateItemMapper.deleteAeaParStateItemByStateId(aeaParState.getParStateId(), rootOgrId);
                }
            }
        }
    }

    private void checkMindExportObj(MindExportObj mindExportObj) {

        if (mindExportObj != null) {
            MindExportData mindExportData = mindExportObj.getData();
            if (mindExportData != null) {
                if (mindExportObj.getChildren() != null) {
                    for (MindExportObj child : mindExportObj.getChildren()) {
                        if (child != null && child.getData() != null) {
                            if (MindConst.MIND_NODE_OPERATOR_TAG_NEW.equals(mindExportData.getOperatorTag())) {
                                child.getData().setOperatorTag(MindConst.MIND_NODE_OPERATOR_TAG_NEW);
                            }
                            checkMindExportObj(child);
                        }
                    }
                }
            }
        }
    }


    private void loadStageNode(AeaParStage aeaParStage, AeaParState pAeaParState, MindExportObj[] child,
                               List<Object> allDeleteList, String rootOrgId) throws Exception {

        List<AeaParState> aeaParStateList;
        if (pAeaParState == null) {
            aeaParStateList = aeaParStateMapper.listRootAeaParStateByStageId(aeaParStage.getStageId(), rootOrgId);
        } else {
            aeaParStateList = aeaParStateMapper.listAeaParStateByParentStateIdAndStageId(pAeaParState.getStageId(), pAeaParState.getParStateId(), rootOrgId);
        }

        List<AeaParIn> aeaParInCertList = null;
        List<AeaParIn> aeaParInMatList = null;
        if (pAeaParState != null) {
            aeaParInMatList = aeaParInMapper.listInStateMatByStageIdAndStateId(aeaParStage.getStageId(), pAeaParState.getParStateId(), null, rootOrgId);
            aeaParInCertList = aeaParInMapper.listInStateCertByStageIdAndStateId(aeaParStage.getStageId(), pAeaParState.getParStateId(), null, rootOrgId);
        }

        if (child == null || child.length == 0) {
            addDeleteChildList(aeaParStateList, aeaParInCertList, aeaParInMatList, allDeleteList, rootOrgId);
        } else {
            for (MindExportObj mindExportObj : child) {
                if (mindExportObj != null) {
                    loadStageNode(aeaParStage, pAeaParState, mindExportObj, aeaParStateList, aeaParInCertList, aeaParInMatList, allDeleteList, rootOrgId);
                }
            }
            addDeleteChildList(aeaParStateList, aeaParInCertList, aeaParInMatList, allDeleteList, rootOrgId);
        }

    }

    private void loadStageNode(AeaParStage aeaParStage, AeaParState pAeaParState, MindExportObj mindExportObj, List<AeaParState> aeaParStateList,
                               List<AeaParIn> aeaParInCertList, List<AeaParIn> aeaParInMatList, List<Object> allDeleteList, String rootOrgId) throws Exception {

        if (mindExportObj != null) {
            MindExportData mindExportData = mindExportObj.getData();

            if (mindExportData != null) {

                mindExportData.setName(mindExportData.getText());

                if (AeaMindConst.MIND_NODE_TYPE_CODE_SITUATION.equals(mindExportData.getNodeTypeCode())) {
                    loadStageState(aeaParStage, pAeaParState, mindExportObj, aeaParStateList, allDeleteList, rootOrgId);

                } else if (AeaMindConst.MIND_NODE_TYPE_CODE_MAT.equals(mindExportData.getNodeTypeCode())) {
                    loadStateMat(aeaParStage, pAeaParState, mindExportObj, aeaParInMatList);

                } else if (AeaMindConst.MIND_NODE_TYPE_CODE_CERT.equals(mindExportData.getNodeTypeCode())) {
                    loadStateCert(aeaParStage, pAeaParState, mindExportObj, aeaParInCertList);
                }
            }
        }
    }

    private void loadStageState(AeaParStage aeaParStage, AeaParState pAeaParState, MindExportObj mindExportObj,
                                List<AeaParState> aeaParStateList, List<Object> allDeleteList, String rootOrgId) throws Exception {

        if (mindExportObj != null) {
            MindExportData mindExportData = mindExportObj.getData();

            if (mindExportData != null) {
                AeaParState aeaParState = aeaParStateMapper.getAeaParStateById(mindExportData.getId());

                boolean newNode = MindConst.MIND_NODE_OPERATOR_TAG_NEW.equals(mindExportData.getOperatorTag());
                if (aeaParState != null) {
                    if (newNode) {
                        List<AeaParStateItem> stateItemList = aeaParStateItemMapper.listStateItemByStateId(aeaParState.getParStateId(), rootOrgId);
                        aeaParState = getNewAeaParStateByOldState(aeaParStage.getStageId(), pAeaParState == null ? null : pAeaParState.getParStateId(), mindExportData, aeaParState);
                        saveAeaParState(aeaParState);

                        if (stateItemList != null && !stateItemList.isEmpty()) {
                            addStateItem(aeaParState, stateItemList);
                        }

                        loadStageNode(aeaParStage, aeaParState, mindExportObj.getChildren(), allDeleteList, rootOrgId);
                    } else {
                        updateState(aeaParState, mindExportData, pAeaParState == null ? null : pAeaParState.getParStateId());
                        loadStageNode(aeaParStage, aeaParState, mindExportObj.getChildren(), allDeleteList, rootOrgId);
                    }
                } else {
                    aeaParState = getNewAeaParState(aeaParStage.getStageId(), pAeaParState == null ? null : pAeaParState.getParStateId(), mindExportData);
                    saveAeaParState(aeaParState);
                    loadStageNode(aeaParStage, aeaParState, mindExportObj.getChildren(), allDeleteList, rootOrgId);
                }

                if (aeaParState != null) {
                    removeAeaParState(aeaParStateList, aeaParState.getParStateId());
                }
            }
        }
    }


    private void loadStateMat(AeaParStage aeaParStage, AeaParState pAeaParState, MindExportObj mindExportObj, List<AeaParIn> aeaParInMatList) throws Exception {

        if (mindExportObj != null) {
            MindExportData mindExportData = mindExportObj.getData();

            if (mindExportData != null) {
                AeaItemMat aeaItemMat = aeaItemMatMapper.selectOneById(mindExportData.getId());

                if (aeaItemMat != null) {

                    boolean newNode = MindConst.MIND_NODE_OPERATOR_TAG_NEW.equals(mindExportData.getOperatorTag());

                    if (newNode) {

                        String inId = null;
                        Map<String, String> map = mindExportData.getExtra();
                        if (map != null && map.size() > 0) {
                            inId = map.get("inId");
                        }
                        List<AeaParStageItemIn> stageItemInList = getAeaParStageItemInList(inId);
                        if (Status.ON.equals(aeaItemMat.getIsGlobalShare())) {
                            AeaParIn parIn = insertNewParIn(aeaParStage, pAeaParState, aeaItemMat, mindExportData);
                            addStageItemIn(parIn, stageItemInList);
                        } else {
                            AeaItemMat newAeaItemMat = getNewAeaItemMat(aeaItemMat, mindExportData);
                            aeaItemMatMapper.insertAeaItemMat(newAeaItemMat);
                            AeaParIn parIn = insertNewParIn(aeaParStage, pAeaParState, newAeaItemMat, mindExportData);
                            addStageItemIn(parIn, stageItemInList);
                        }
                    } else {
                        updateParIn(aeaItemMat, mindExportData);
                    }

                    removeAeaParIn(aeaParInMatList, mindExportData.getId(), true);
                }
            }
        }
    }


    private void loadStateCert(AeaParStage aeaParStage, AeaParState pAeaParState, MindExportObj mindExportObj, List<AeaParIn> aeaParInCertList) throws Exception {

        if (mindExportObj != null) {
            MindExportData mindExportData = mindExportObj.getData();
            if (mindExportData != null) {
                AeaCert aeaCert = aeaCertMapper.selectOneById(mindExportData.getId());
                if (aeaCert != null) {
                    boolean newNode = MindConst.MIND_NODE_OPERATOR_TAG_NEW.equals(mindExportData.getOperatorTag());
                    if (newNode) {
                        String inId = null;
                        Map<String, String> map = mindExportData.getExtra();
                        if (map != null && map.size() > 0) {
                            inId = map.get("inId");
                        }
                        List<AeaParStageItemIn> stageItemInList = getAeaParStageItemInList(inId);
                        AeaParIn parIn = insertNewParIn(aeaParStage, pAeaParState, aeaCert, mindExportData);
                        addStageItemIn(parIn, stageItemInList);
                    } else {
                        updateParIn(aeaCert, mindExportData);
                    }
                }
                removeAeaParIn(aeaParInCertList, mindExportData.getId(), false);
            }
        }

    }

    private void addDeleteChildList(List<AeaParState> aeaParStateList, List<AeaParIn> aeaParInCertList,
                                    List<AeaParIn> aeaParInMatList, List<Object> allDeleteList, String rootOrgId) {

        if (aeaParStateList != null && aeaParStateList.size() > 0) {
            for (AeaParState atate : aeaParStateList) {
                addDeleteAeaParStateAndChild(atate, allDeleteList, rootOrgId);
            }
        }
        if (aeaParInMatList != null && aeaParInMatList.size() > 0) {
            allDeleteList.addAll(aeaParInMatList);
        }
        if (aeaParInCertList != null && aeaParInCertList.size() > 0) {
            allDeleteList.addAll(aeaParInCertList);
        }
    }

    private AeaItemMat getNewAeaItemMat(AeaItemMat oldNewAeaItemMat, MindExportData mindExportData) throws Exception {

        String userId = SecurityContext.getCurrentUserId();
        String rootOrgId = SecurityContext.getCurrentOrgId();
        AeaItemMat aeaItemMat = new AeaItemMat();
        BeanUtils.copyProperties(oldNewAeaItemMat, aeaItemMat);
        aeaItemMat.setModifier(null);
        aeaItemMat.setModifyTime(null);
        aeaItemMat.setCreater(userId);
        aeaItemMat.setCreateTime(new Date());
        aeaItemMat.setName(mindExportData.getName());
        aeaItemMat.setMatMemo(mindExportData.getNote());
        aeaItemMat.setMatId(UUID.randomUUID().toString());
        aeaItemMat.setSortNo(getAeaItemMatMaxSortNo(rootOrgId));
        String autoCode = autoCodeNumberService.generate("AEA-ITEM-MAT-CODE", rootOrgId);
        aeaItemMat.setMatCode(autoCode);
        aeaItemMat.setRootOrgId(rootOrgId);
        return aeaItemMat;
    }

    private void updateState(AeaParState aeaParState, MindExportData mindExportData, String parentStateId) {

        boolean needUpdate = false;
        String textName = mindExportData.getName();
        String note = mindExportData.getNote();

        if (textName != null && StringUtils.isNotBlank(textName)) {
            if (!textName.equals(aeaParState.getStateName())) {
                aeaParState.setStateName(textName);
                needUpdate = true;
            }
        }

        if (note != null && StringUtils.isNotBlank(note)) {
            if (!note.equals(aeaParState.getStateMemo())) {
                aeaParState.setStateMemo(note);
                needUpdate = true;
            }
        }
        if ((mindExportData.getProgress() != null && StringUtils.isNotBlank(mindExportData.getProgress())) || (mindExportData.getPriority() != null && StringUtils.isNotBlank(mindExportData.getPriority()))) {
            if (AeaMindConst.MIND_NODE_PROGRESS_MAPPING_REQUIRED.equals(mindExportData.getProgress())) {
                if (!Status.ON.equals(aeaParState.getMustAnswer())) {
                    needUpdate = true;
                }
                aeaParState.setMustAnswer(Status.ON);
            } else {
                if (!Status.OFF.equals(aeaParState.getMustAnswer())) {
                    needUpdate = true;
                }
                aeaParState.setMustAnswer(Status.OFF);
            }

            if (AeaMindConst.MIND_NODE_PRIORITY_MAPPING_REQUIRED.equals(mindExportData.getPriority())) {
                if (!"m".equals(aeaParState.getAnswerType())) {
                    needUpdate = true;
                }
                aeaParState.setAnswerType("m");
            } else {
                if (!"y".equals(aeaParState.getAnswerType())) {
                    needUpdate = true;
                }
                aeaParState.setAnswerType("y");
            }

            if (!Status.ON.equals(aeaParState.getIsQuestion())) {
                aeaParState.setIsQuestion(Status.ON);
                needUpdate = true;
            }
        } else {
            if (Status.ON.equals(aeaParState.getIsQuestion())) {
                needUpdate = true;
                if (StringUtils.isNotBlank(parentStateId)) {
                    AeaParState parentState = aeaParStateMapper.getAeaParStateById(parentStateId);
                    if (parentState != null) {//父情形是问题，则子情形是答案，反之，父情形是答案，则子情形是问题
                        if (Status.OFF.equals(parentState.getIsQuestion())) {
                            aeaParState.setIsQuestion(Status.ON);
                            aeaParState.setAnswerType("s");
                        } else {
                            aeaParState.setIsQuestion( Status.OFF);
                            aeaParState.setAnswerType("");
                        }
                    }
                } else {
                    aeaParState.setIsQuestion(Status.ON);
                    aeaParState.setAnswerType("s");
                }
                aeaParState.setMustAnswer("");
            }
        }

        // 处理是否流程数据
        String linkProcessStart = mindExportData.getLinkProcessStart();
        if (StringUtils.isBlank(linkProcessStart)) {
            linkProcessStart = Status.OFF;
        }
        if (!linkProcessStart.equals(aeaParState.getIsProcStartCond())) {
            aeaParState.setIsProcStartCond(linkProcessStart);
            needUpdate = true;
        }

        if (needUpdate) {

            if(!"2".equals(aeaParState.getIsQuestionOri())){
                aeaParState.setIsQuestionOri(aeaParState.getIsQuestion());
            }

            aeaParState.setModifier(SecurityContext.getCurrentUserId());
            aeaParState.setModifyTime(new Date());
            aeaParState.setIsActive(ActiveStatus.ACTIVE.getValue());
            aeaParState.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
            aeaParStateMapper.updateAeaParState(aeaParState);
        }
    }

    private void updateParIn(AeaItemMat aeaItemMat, MindExportData mindExportData) {
        String textName = mindExportData.getName();
        String note = mindExportData.getNote();
        boolean needUpdate = false;
        if (textName != null && StringUtils.isNotBlank(textName)) {
            if (!aeaItemMat.getMatName().equals(textName)) {
                aeaItemMat.setMatName(textName);
                needUpdate = true;
            }
        }

        if (note != null && StringUtils.isNotBlank(note)) {
            if (!aeaItemMat.getMatMemo().equals(note)) {
                aeaItemMat.setMatMemo(note);
                needUpdate = true;
            }
        }

        if (needUpdate) {
            aeaItemMat.setModifier(SecurityContext.getCurrentUserId());
            aeaItemMat.setModifyTime(new Date());
            aeaItemMat.setIsActive(ActiveStatus.ACTIVE.getValue());
            aeaItemMat.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
            aeaItemMatMapper.updateAeaItemMat(aeaItemMat);
        }
    }

    private void updateParIn(AeaCert aeaCert, MindExportData mindExportData) {
        String textName = mindExportData.getName();
        String note = mindExportData.getNote();
        boolean needUpdate = false;
        if (textName != null && StringUtils.isNotBlank(textName)) {
            if (!aeaCert.getCertName().equals(textName)) {
                aeaCert.setCertName(textName);
                needUpdate = true;
            }
        }

        if (note != null && StringUtils.isNotBlank(note)) {
            if (!aeaCert.getCertMemo().equals(note)) {
                aeaCert.setCertMemo(note);
                needUpdate = true;
            }
        }

        if (needUpdate) {
            aeaCert.setModifier(SecurityContext.getCurrentUserId());
            aeaCert.setModifyTime(LocalDateTime.now());
            aeaCert.setIsDeleted(DeletedStatus.NOT_DELETED);
            aeaCertMapper.updateOne(aeaCert);
        }
    }


    private AeaParIn insertNewParIn(AeaParStage aeaParStage, AeaParState pAeaParState, AeaItemMat aeaItemMat, MindExportData mindExportData) {

        if (aeaItemMat != null) {
            AeaParIn aeaParIn = getNewAeaParIn(aeaParStage, pAeaParState);
            aeaParIn.setFileType("mat");
            aeaParIn.setMatId(aeaItemMat.getMatId());
            aeaParIn.setRootOrgId(SecurityContext.getCurrentOrgId());
            aeaParInMapper.insertAeaParIn(aeaParIn);
            String note = mindExportData.getNote();
            if (note != null && StringUtils.isNotBlank(note)) {
                if (!aeaItemMat.getMatMemo().equals(note)) {
                    aeaItemMat.setMatMemo(note);
                    aeaItemMat.setModifier(SecurityContext.getCurrentUserId());
                    aeaItemMat.setModifyTime(new Date());
                    aeaItemMatMapper.updateAeaItemMat(aeaItemMat);
                }
            }

            return aeaParIn;
        }

        return null;
    }


    private AeaParIn insertNewParIn(AeaParStage aeaParStage, AeaParState pAeaParState, AeaCert aeaCert, MindExportData mindExportData) {

        if (aeaCert != null) {
            AeaParIn aeaParIn = getNewAeaParIn(aeaParStage, pAeaParState);
            aeaParIn.setFileType("cert");
            aeaParIn.setCertId(aeaCert.getCertId());
            aeaParInMapper.insertAeaParIn(aeaParIn);
            String note = mindExportData.getNote();
            if (note != null && StringUtils.isNotBlank(note)) {
                if (!aeaCert.getCertMemo().equals(note)) {
                    aeaCert.setCertMemo(note);
                    aeaCert.setModifier(SecurityContext.getCurrentUserId());
                    aeaCert.setModifyTime(LocalDateTime.now());
                    aeaCertMapper.updateOne(aeaCert);
                }
            }
            return aeaParIn;
        }
        return null;
    }


    private AeaParIn getNewAeaParIn(AeaParStage aeaParStage, AeaParState pAeaParState) {

        String userId = SecurityContext.getCurrentUserId();
        String rootOrgId = SecurityContext.getCurrentOrgId();
        AeaParIn aeaParIn = new AeaParIn();
        aeaParIn.setIsOwner(Status.ON);
        aeaParIn.setCreater(userId);
        aeaParIn.setCreateTime(new Date());
        aeaParIn.setInId(UUID.randomUUID().toString());
        aeaParIn.setStageId(aeaParStage.getStageId());
        if (pAeaParState == null) {
            aeaParIn.setIsStateIn(Status.OFF);
        } else {
            aeaParIn.setIsStateIn(Status.ON);
            aeaParIn.setParStateId(pAeaParState.getParStateId());
        }
        aeaParIn.setIsDeleted(Status.OFF);
        Long sortNo = aeaParInMapper.getMaxSortNoByStageId(aeaParStage.getStageId(), rootOrgId);
        aeaParIn.setSortNo(sortNo==null?1L:(sortNo+1L));
        aeaParIn.setRootOrgId(rootOrgId);
        return aeaParIn;
    }

    private void removeAeaParIn(List<AeaParIn> list, String id, boolean isMat) {

        if (list != null && list.size() > 0) {
            Iterator<AeaParIn> iterator = list.iterator();
            while (iterator.hasNext()) {
                String inId;
                if (isMat) {
                    inId = iterator.next().getMatId();
                } else {
                    inId = iterator.next().getCertId();
                }
                if (inId.equals(id)) {
                    iterator.remove();
                    return;
                }
            }
        }
    }

    private void removeAeaParState(List<AeaParState> list, String stateId) {
        if (list != null && list.size() > 0) {
            Iterator<AeaParState> iterator = list.iterator();

            while (iterator.hasNext()) {
                if (iterator.next().getParStateId().equals(stateId)) {
                    iterator.remove();
                    return;
                }
            }
        }
    }

    private AeaParState getNewAeaParStateByOldState(String stageId, String parentStateId, MindExportData mindExportData, AeaParState oldAeaParState) {

        AeaParState aeaParState = new AeaParState();
        BeanUtils.copyProperties(oldAeaParState, aeaParState);

        aeaParState.setCreateTime(new Date());
        aeaParState.setCreater(SecurityContext.getCurrentUserId());
        aeaParState.setStateName(mindExportData.getName());
        aeaParState.setParentStateId(parentStateId);
        aeaParState.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
        aeaParState.setIsActive(ActiveStatus.ACTIVE.getValue());
        aeaParState.setStageId(stageId);
        aeaParState.setParStateId(UUID.randomUUID().toString());
        aeaParState.setStateMemo(mindExportData.getNote());
        aeaParState.setModifier(null);
        aeaParState.setModifyTime(null);

        initStateQuestionAndAnswer(aeaParState, mindExportData.getPriority(), mindExportData.getProgress(), parentStateId);

        // 处理是否流程数据
        if (Status.ON.equals(mindExportData.getLinkProcessStart())) {
            aeaParState.setIsProcStartCond(Status.ON);
        } else {
            aeaParState.setIsProcStartCond(Status.OFF);
        }
        return aeaParState;
    }

    private AeaParState getNewAeaParState(String stageId, String parentStateId, MindExportData mindExportData) {

        AeaParState aeaParState = new AeaParState();
        aeaParState.setCreateTime(new Date());
        aeaParState.setCreater(SecurityContext.getCurrentUserId());
        aeaParState.setStateName(mindExportData.getName());
        aeaParState.setParentStateId(parentStateId);
        aeaParState.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
        aeaParState.setIsActive(ActiveStatus.ACTIVE.getValue());
        aeaParState.setUseEl(Status.OFF);
        aeaParState.setStageId(stageId);
        aeaParState.setParStateId(UUID.randomUUID().toString());
        aeaParState.setStateMemo(mindExportData.getNote());
        aeaParState.setStateEl("");

        // 处理是否流程数据
        if (Status.ON.equals(mindExportData.getLinkProcessStart())) {
            aeaParState.setIsProcStartCond(Status.ON);
        } else {
            aeaParState.setIsProcStartCond(Status.OFF);
        }

        initStateQuestionAndAnswer(aeaParState, mindExportData.getPriority(), mindExportData.getProgress(), parentStateId);

        return aeaParState;
    }


    private void initStateQuestionAndAnswer(AeaParState aeaParState, String priority, String progress, String parentStateId) {
        boolean isQuestion = false;

        if ((progress != null && StringUtils.isNotBlank(progress)) || (priority != null && StringUtils.isNotBlank(priority))) {
            isQuestion = true;
        }

        if (isQuestion) {
            aeaParState.setIsQuestion(Status.ON);
            if (AeaMindConst.MIND_NODE_PRIORITY_MAPPING_REQUIRED.equals(priority)) {
                aeaParState.setAnswerType("m");
            } else {
                aeaParState.setAnswerType("y");
            }

            if (AeaMindConst.MIND_NODE_PROGRESS_MAPPING_REQUIRED.equals(progress)) {
                aeaParState.setMustAnswer(Status.ON);
            } else {
                aeaParState.setMustAnswer(Status.OFF);
            }
        } else {
            if (StringUtils.isNotBlank(parentStateId)) {
                AeaParState parentState = aeaParStateMapper.getAeaParStateById(parentStateId);
                if (parentState != null) {//父情形是问题，则子情形是答案，反之，父情形是答案，则子情形是问题
                    if (Status.OFF.equals(parentState.getIsQuestion())) {
                        aeaParState.setIsQuestion(Status.ON);
                        aeaParState.setAnswerType("s");
                    } else {
                        aeaParState.setIsQuestion(Status.OFF);
                        aeaParState.setAnswerType("");
                    }
                }
            } else {
                aeaParState.setIsQuestion(Status.ON);
                aeaParState.setAnswerType("s");
            }
        }


        if(!"2".equals(aeaParState.getIsQuestionOri())){
            aeaParState.setIsQuestionOri(aeaParState.getIsQuestion());
        }
    }

    private void addDeleteAeaParStateAndChild(AeaParState aeaParState, List<Object> allDeleteList,String rootOrgId) {

        // 获取情形材料
        List<AeaParIn> matInList = aeaParInMapper.listInStateMatByStageIdAndStateId(aeaParState.getStageId(), aeaParState.getParStateId(), null, rootOrgId);
        if (CollectionUtils.isNotEmpty(matInList)) {
            allDeleteList.addAll(matInList);
        }

        // 获取情形证照
        List<AeaParIn> certInList = aeaParInMapper.listInStateCertByStageIdAndStateId(aeaParState.getStageId(), aeaParState.getParStateId(), null, rootOrgId);
        if (CollectionUtils.isNotEmpty(certInList)) {
            allDeleteList.addAll(certInList);
        }

        //获取子情形
        List<AeaParState> aeaParStateList = aeaParStateMapper.listAeaParStateByParentStateIdAndStageId(aeaParState.getStageId(), aeaParState.getParStateId(), rootOrgId);
        if (CollectionUtils.isNotEmpty(aeaParStateList)) {
            for (AeaParState child : aeaParStateList) {
                addDeleteAeaParStateAndChild(child, allDeleteList, rootOrgId);
            }
        }
        allDeleteList.add(aeaParState);
    }

    private void deleteAeaParInMat(AeaParIn aeaParIn) {

        if (Status.ON.equals(aeaParIn.getIsGlobalShare())) {
            deleteAeaParIn(aeaParIn);
        } else {
            deleteAeaParIn(aeaParIn);
            AeaItemMat aeaItemMat = new AeaItemMat();
            aeaItemMat.setModifier(SecurityContext.getCurrentUserId());
            aeaItemMat.setModifyTime(new Date());
            aeaItemMat.setIsDeleted(DeletedStatus.DELETED.getValue());
            aeaItemMat.setMatId(aeaParIn.getMatId());
            aeaItemMatMapper.updateAeaItemMat(aeaItemMat);
        }
    }

    private void deleteAeaParInCert(AeaParIn aeaParIn) {

        deleteAeaParIn(aeaParIn);
    }

    private void deleteAeaParIn(AeaParIn aeaParIn) {

        aeaParIn.setModifyTime(new Date());
        aeaParIn.setModifier(SecurityContext.getCurrentUserId());
        aeaParIn.setIsDeleted(Status.ON);
        aeaParInMapper.updateAeaParIn(aeaParIn);
        deleteStageItemIn(aeaParIn.getInId());
    }


    private void deleteStageItemIn(String inId) {

        aeaParStageItemInMapper.deleteAeaParStageItemInByInId(inId);
    }

    private void addStateItem(AeaParState aeaParState, List<AeaParStateItem> stateItemList) {

        if (stateItemList != null && !stateItemList.isEmpty()) {
            String userId = SecurityContext.getCurrentUserId();
            String rootOrgId = SecurityContext.getCurrentOrgId();
            for (AeaParStateItem stateItem : stateItemList) {
                AeaParStateItem aeaParStateItem = new AeaParStateItem();
                aeaParStateItem.setStateItemId(UUID.randomUUID().toString());
                aeaParStateItem.setParStateId(aeaParState.getParStateId());
                aeaParStateItem.setSortNo(getStateItemMaxSortNo(rootOrgId));
                aeaParStateItem.setStageItemId(stateItem.getStageItemId());
                aeaParStateItem.setCreater(userId);
                aeaParStateItem.setRootOrgId(rootOrgId);
                aeaParStateItem.setCreateTime(new Date());
                aeaParStateItemMapper.insertAeaParStateItem(aeaParStateItem);
            }
        }
    }

    private Long getStateItemMaxSortNo(String rootOrgId) {

        Long sortNo = aeaParStateItemMapper.getMaxSortNo(rootOrgId);
        return sortNo == null ? 1L : sortNo + 1;
    }


    private Long getStageItemInMaxSortNo() {

        Long sortNo = aeaParStageItemInMapper.getMaxSortNo();
        return sortNo == null ? 1L : sortNo + 1;
    }

    private Long getAeaItemMatMaxSortNo(String rootOrgId) {

        Long sortNo = aeaItemMatMapper.getMaxSortNo(rootOrgId);
        return sortNo == null ? 1L : sortNo + 1;
    }

    private List<AeaParStageItemIn> getAeaParStageItemInList(String inId) {

        if (inId != null) {
            return aeaParStageItemInMapper.listAeaParStageItemInByInId(inId);
        }

        return null;
    }

    private void addStageItemIn(AeaParIn aeaParIn, List<AeaParStageItemIn> parStageItemInList) {

        if (aeaParIn != null && parStageItemInList != null && !parStageItemInList.isEmpty()) {
            for (AeaParStageItemIn parStageItemIn : parStageItemInList) {
                AeaParStageItemIn aeaParStageItemIn = new AeaParStageItemIn();
                aeaParStageItemIn.setItemInId(UUID.randomUUID().toString());
                aeaParStageItemIn.setInId(aeaParIn.getInId());
                aeaParStageItemIn.setStageItemId(parStageItemIn.getStageItemId());
                aeaParStageItemIn.setCreateTime(new Date());
                aeaParStageItemIn.setCreater(SecurityContext.getCurrentUserId());
                aeaParStageItemIn.setSortNo(getStageItemInMaxSortNo());
                aeaParStageItemInMapper.insertAeaParStageItemIn(aeaParStageItemIn);
            }
        }

    }
}

