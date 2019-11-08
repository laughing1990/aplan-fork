package com.augurit.aplanmis.common.service.admin.item.impl;

import com.augurit.agcloud.bsc.util.CommonConstant;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.bsc.domain.MindBaseNode;
import com.augurit.aplanmis.bsc.domain.MindExportData;
import com.augurit.aplanmis.bsc.domain.MindExportObj;
import com.augurit.aplanmis.common.constants.AeaMindConst;
import com.augurit.aplanmis.common.constants.MindConst;
import com.augurit.aplanmis.common.convert.AeaItemFrontCondStateConvert;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaItemCond;
import com.augurit.aplanmis.common.mapper.AeaItemBasicMapper;
import com.augurit.aplanmis.common.mapper.AeaItemCondMapper;
import com.augurit.aplanmis.common.service.admin.item.AeaItemCondAdminService;
import com.augurit.aplanmis.common.utils.MindViewUtils;
import com.augurit.aplanmis.common.vo.AeaItemFrontCondStateVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 事项前提条件表-Service服务接口实现类
 */
@Service
@Transactional
public class AeaItemCondAdminServiceImpl implements AeaItemCondAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaItemCondAdminServiceImpl.class);

    @Autowired
    private AeaItemCondMapper aeaItemCondMapper;

    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;

    @Override
    public void saveAeaItemCond(AeaItemCond aeaItemCond) {

        String rootOrgId = SecurityContext.getCurrentOrgId();
        String condSeq = getItemCondStateSeq(aeaItemCond.getItemCondId(), aeaItemCond.getParentCondId());
        aeaItemCond.setCondSeq(condSeq);
        aeaItemCond.setSortNo(getMaxSortNo(rootOrgId));
        aeaItemCond.setIsActive(Status.ON);
        aeaItemCond.setRootOrgId(rootOrgId);
        aeaItemCond.setCreater(SecurityContext.getCurrentUserId());
        aeaItemCond.setCreateTime(new Date());
        aeaItemCondMapper.insertAeaItemCond(aeaItemCond);
    }

    @Override
    public void updateAeaItemCond(AeaItemCond aeaItemCond) {

        aeaItemCond.setModifier(SecurityContext.getCurrentUserId());
        aeaItemCond.setModifyTime(new Date());
        aeaItemCondMapper.updateAeaItemCond(aeaItemCond);
    }

    @Override
    public void deleteAeaItemCondById(String id) {

        if (StringUtils.isNotBlank(id)) {
            aeaItemCondMapper.deleteAeaItemCond(id);
        }
    }

    @Override
    public PageInfo<AeaItemCond> listAeaItemCond(AeaItemCond aeaItemCond, Page page) {

        aeaItemCond.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaItemCond> list = aeaItemCondMapper.listAeaItemCond(aeaItemCond);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public List<AeaItemCond> listAeaItemCond(AeaItemCond aeaItemCond){

        aeaItemCond.setRootOrgId(SecurityContext.getCurrentOrgId());
        return aeaItemCondMapper.listAeaItemCond(aeaItemCond);
    }

    @Override
    public AeaItemCond getAeaItemCondById(String id) {

        if (StringUtils.isNotBlank(id)) {
            logger.debug("根据ID获取Form对象，ID为：{}", id);
            return aeaItemCondMapper.getAeaItemCondById(id);
        }
        return null;
    }

    @Override
    public Long getMaxSortNo(String rootOrgId) {

        Long sortNo = aeaItemCondMapper.getMaxSortNo(rootOrgId);
        return sortNo == null ? 1L : (sortNo + 1);
    }

    @Override
    public void batchDeleteAeaItemCond(String[] ids) {

        if (ids != null && ids.length > 0) {
            aeaItemCondMapper.batchDeleteAeaItemCond(ids);
        }
    }

    @Override
    public void changIsActiveState(String id) {

        if (StringUtils.isNotBlank(id)) {
            aeaItemCondMapper.changIsActiveState(id);
        }
    }

    @Override
    public void saveAeaItemCondState(List<AeaItemFrontCondStateVo> stateList, String itemId){

        String userId = SecurityContext.getCurrentUserId();
        String rootOrgId = SecurityContext.getCurrentOrgId();
        AeaItemCond searchCond = new AeaItemCond();
        searchCond.setItemId(itemId);
        searchCond.setRootOrgId(rootOrgId);
        List<AeaItemCond> itemCondStateList = aeaItemCondMapper.listAeaItemCond(searchCond);
        if (stateList != null && stateList.size() > 0) {
            saveAeaItemCondStateChilds(stateList.get(0).getChilds(), "", itemId, itemCondStateList, rootOrgId, userId);
        }
        // 剩下的就是多余的情形,开普已经删除的情形
        if(CollectionUtils.isNotEmpty(itemCondStateList)) {
            for(AeaItemCond entity : itemCondStateList){
                // 删除情形
                aeaItemCondMapper.deleteAeaItemCond(entity.getItemCondId());
            }
        }
    }

    /**
     * 情形数据集的子节点集合
     */
    private void saveAeaItemCondStateChilds(List<AeaItemFrontCondStateVo> stateList, String parentId, String itemId,
                                            List<AeaItemCond> itemCondStateList, String rootOrgId, String userId) {

        for (AeaItemFrontCondStateVo stateVo : stateList) {

            AeaItemCond aeaItemCond = new AeaItemCond();
            aeaItemCond.setRootOrgId(rootOrgId);
            aeaItemCond = AeaItemFrontCondStateConvert.useByGetFrontCondState(aeaItemCond, stateVo);

            //获取已有的情形列表
            AeaItemCond old = new AeaItemCond();
            old.setRootOrgId(rootOrgId);
            old.setItemCondId(aeaItemCond.getItemCondId());
            List<AeaItemCond> oldList = aeaItemCondMapper.listAeaItemCond(old);

            //获取已有的需要更新那条记录
            AeaItemCond needEntity = null;
            if (oldList != null && oldList.size() > 0) {
                needEntity = oldList.get(0);
            }
            AeaItemCond oldEntity = needEntity;
            // 更新
            if (oldEntity != null) {
                // 剔除已经存在的
                if (CollectionUtils.isNotEmpty(itemCondStateList)) {
                    itemCondStateList.removeIf(state -> state.getItemCondId().equals(oldEntity.getItemCondId()));
                }
                if (StringUtils.isNotBlank(parentId)) {
                    aeaItemCond.setParentCondId(parentId);
                }
                aeaItemCond.setItemId(itemId);
                String itemStateSeq = getItemCondStateSeq(aeaItemCond.getItemCondId(), parentId);
                aeaItemCond.setCondSeq(itemStateSeq);
                aeaItemCond.setIsActive(Status.ON);
                aeaItemCond.setModifier(userId);
                aeaItemCond.setModifyTime(new Date());
                aeaItemCondMapper.updateAeaItemCond(aeaItemCond);
            } else { // 新增
                //设置父节点Id
                if (StringUtils.isNotBlank(parentId)) {
                    aeaItemCond.setParentCondId(parentId);
                }
                //设置事项id
                aeaItemCond.setItemId(itemId);
                aeaItemCond.setUseEl("0");
                //获取最大的排列顺序号
                Long maxSortNo = aeaItemCondMapper.getMaxSortNo(rootOrgId);
                if (maxSortNo == null) {
                    maxSortNo = 1L;
                } else {
                    maxSortNo++;
                }
                aeaItemCond.setSortNo(maxSortNo);
                String itemStateSeq = getItemCondStateSeq(aeaItemCond.getItemCondId(), parentId);
                aeaItemCond.setCondSeq(itemStateSeq);
                aeaItemCond.setCreater(userId);
                aeaItemCond.setCreateTime(new Date());
                aeaItemCond.setIsActive("1");
                aeaItemCondMapper.insertAeaItemCond(aeaItemCond);
            }

            //获取子节点的数据
            if (CollectionUtils.isNotEmpty(stateVo.getChilds())) {
                saveAeaItemCondStateChilds(stateVo.getChilds(), stateVo.getId(), itemId, itemCondStateList, rootOrgId, userId);
            }
        }
    }

    private String getItemCondStateSeq(String itemCondId, String parentCondId) {

        if (StringUtils.isNotBlank(parentCondId)) {
            //获取上级菜单的序列
            String parentItemCondSeq = CommonConstant.SEQ_SEPARATOR;
            AeaItemCond parentItemCond = aeaItemCondMapper.getAeaItemCondById(parentCondId);
            if (parentItemCond != null && StringUtils.isNotBlank(parentItemCond.getCondSeq())) {
                parentItemCondSeq = parentItemCond.getCondSeq();
            }
            return parentItemCondSeq + itemCondId + CommonConstant.SEQ_SEPARATOR;
        } else {
            return CommonConstant.SEQ_SEPARATOR + itemCondId + CommonConstant.SEQ_SEPARATOR;
        }
    }

    @Override
    public MindBaseNode listAeaItemCondMindView(String itemVerId){

        if (StringUtils.isNotBlank(itemVerId)) {
            String rootOrgId = SecurityContext.getCurrentOrgId();
            AeaItemBasic aeaItemBasic = aeaItemBasicMapper.getOneByItemVerId(itemVerId, rootOrgId);
            if (aeaItemBasic != null) {
                MindBaseNode mindBaseNode = getRootItemNode(aeaItemBasic);
                AeaItemCond queryAeaItemCond = new AeaItemCond();
                queryAeaItemCond.setRootOrgId(rootOrgId);
                queryAeaItemCond.setItemVerId(itemVerId);
                List<AeaItemCond> allCondList = aeaItemCondMapper.listAeaItemCond(queryAeaItemCond);
                List<AeaItemCond> topCondList = getTopAeaItemCondList(allCondList);
                if (topCondList != null && !topCondList.isEmpty()) {
                    sortAeaItemCondBySortNo(topCondList);
                    List<MindBaseNode> topNodeList = new ArrayList<>();
                    for (AeaItemCond topCond : topCondList) {
                        MindBaseNode mindSituationNode = getTopItemCondNode(topCond);
                        topNodeList.add(mindSituationNode);
                        loadChildItemCondList(itemVerId, mindSituationNode,allCondList);
                    }
                    mindBaseNode.setChilds(MindViewUtils.listToArray(topNodeList));
                } else {
                    mindBaseNode.setOpen(MindConst.MIND_NODE_EXPAND_FALSE);
                }
                return mindBaseNode;
            }
        }
        return null;
    }

    @Override
    public void saveOrUpdateAeaItemCondMindView(MindExportObj mindExportObj) {

        if (mindExportObj != null) {
            MindViewUtils.checkMindExportObj(mindExportObj);
            MindExportData root = mindExportObj.getData();
            if (root != null && AeaMindConst.MIND_NODE_TYPE_CODE_ITEM.equals(root.getNodeTypeCode())) {
                AeaItemBasic itemBasic = aeaItemBasicMapper.getOneByItemVerId(root.getId(), SecurityContext.getCurrentOrgId());
                if (itemBasic != null) {
                    List<Object> allDeleteList = new ArrayList<>();
                    loadItemCondNode(itemBasic, null, mindExportObj.getChildren(), allDeleteList, true);
                    deleteNodeList(allDeleteList);
                }
            }
        }
    }

    private void loadItemCondNode(AeaItemBasic aeaItem, AeaItemCond pAeaItemCond, MindExportObj[] child, List<Object> allDeleteList, boolean isQuestion){

        String rootOrgId = SecurityContext.getCurrentOrgId();
        List<AeaItemCond> aeaItemCondList;
        if (pAeaItemCond == null) {
            aeaItemCondList = aeaItemCondMapper.listItemCondTopListByItemId(aeaItem.getItemVerId(), rootOrgId);
        } else {
            aeaItemCondList = aeaItemCondMapper.listChildAeaItemCondByParentCondId(aeaItem.getItemVerId(), pAeaItemCond.getItemCondId(), rootOrgId);
        }

        if (child == null || child.length == 0) {
            addDeleteChildList(aeaItemCondList, allDeleteList);
        } else {
            for (MindExportObj mindExportObj : child) {
                if (mindExportObj != null) {
                    loadItemCondNode(aeaItem, pAeaItemCond, mindExportObj, aeaItemCondList, allDeleteList, isQuestion);
                }
            }
            addDeleteChildList(aeaItemCondList, allDeleteList);
        }

    }

    private void loadChildItemCondList(String itemVerId, MindBaseNode node, List<AeaItemCond> allCondList) {

        List<MindBaseNode> allChildList = new ArrayList<>();
        List<AeaItemCond> condChildList = getChildItemCondList(node.getId(), allCondList);
        if (condChildList != null && !condChildList.isEmpty()) {
            sortAeaItemCondBySortNo(condChildList);
            List<MindBaseNode> condNodeList = new ArrayList<>();
            for (AeaItemCond child : condChildList) {
                MindBaseNode mindSituationNode = getChildItemCondNode(child);
                condNodeList.add(mindSituationNode);
                loadChildItemCondList(itemVerId, mindSituationNode, allCondList);
            }
            allChildList.addAll(condNodeList);
        }
        if (!allChildList.isEmpty()) {
            node.setChilds(MindViewUtils.listToArray(allChildList));
        } else {
            node.setOpen(MindConst.MIND_NODE_EXPAND_FALSE);
        }
    }

    private void loadItemCondNode(AeaItemBasic aeaItem, AeaItemCond pAeaItemCond, MindExportObj mindExportObj, List<AeaItemCond> aeaItemCondList, List<Object> allDeleteList, Boolean isQuestion) {

        if (mindExportObj != null) {
            MindExportData mindExportData = mindExportObj.getData();
            if (mindExportData != null) {
                mindExportData.setName(mindExportData.getText());
                AeaItemCond aeaItemCond = aeaItemCondMapper.getAeaItemCondById(mindExportData.getId());
                boolean newNode = MindConst.MIND_NODE_OPERATOR_TAG_NEW.equals(mindExportData.getOperatorTag());
                if (aeaItemCond != null) {
                    if (newNode) {
                        aeaItemCond = getNewAeaItemCondByOldItemCond(aeaItem.getItemId(),aeaItem.getItemVerId(), pAeaItemCond == null ? null : pAeaItemCond.getItemCondId(), mindExportData, aeaItemCond,isQuestion);
                        saveAeaItemCond(aeaItemCond);
                        loadItemCondNode(aeaItem, aeaItemCond, mindExportObj.getChildren(), allDeleteList,!isQuestion);
                    } else {
                        updateItemCond(aeaItemCond, mindExportData);
                        loadItemCondNode(aeaItem, aeaItemCond, mindExportObj.getChildren(), allDeleteList,!isQuestion);
                    }
                } else {
                    aeaItemCond = getNewAeaItemCond(aeaItem.getItemId(),aeaItem.getItemVerId(), pAeaItemCond == null ? null : pAeaItemCond.getItemCondId(), mindExportData,isQuestion);
                    saveAeaItemCond(aeaItemCond);
                    loadItemCondNode(aeaItem, aeaItemCond, mindExportObj.getChildren(), allDeleteList,!isQuestion);
                }
                if (aeaItemCond != null) {
                    removeAeaItemCond(aeaItemCondList, aeaItemCond.getItemCondId());
                }

            }
        }
    }

    private MindBaseNode getChildItemCondNode(AeaItemCond itemCond) {
        MindBaseNode mindSituationNode = new MindBaseNode();
        mindSituationNode.setId(itemCond.getItemCondId());
        mindSituationNode.setName(itemCond.getCondName());
        mindSituationNode.setPid(itemCond.getParentCondId());
        mindSituationNode.setOpen(MindConst.MIND_NODE_EXPAND_TRUE);
        mindSituationNode.setNodeTypeCode(AeaMindConst.MIND_NODE_TYPE_CODE_CONDTION);
        mindSituationNode.setNote(itemCond.getCondMemo());
        mindSituationNode.setTerminateSituation(itemCond.getSfzz());
        mindSituationNode.setSituationAnswerNum(itemCond.getMuiltSelect()==null?null:itemCond.getMuiltSelect()+"");
        return mindSituationNode;
    }

    private MindBaseNode getTopItemCondNode(AeaItemCond itemCond){
        MindBaseNode mindSituationNode = getChildItemCondNode(itemCond);
        mindSituationNode.setPid(itemCond.getItemVerId());
        return mindSituationNode;
    }


    private List<AeaItemCond> getChildItemCondList(String parentCondId, List<AeaItemCond> allCondList) {

        List<AeaItemCond> childItemCondList = new ArrayList<>();
        if (parentCondId != null && CollectionUtils.isNotEmpty(allCondList)) {
            Iterator<AeaItemCond> iterator = allCondList.iterator();
            while (iterator.hasNext()) {
                AeaItemCond aeaItemCond = iterator.next();
                if (parentCondId.equals(aeaItemCond.getParentCondId())) {
                    childItemCondList.add(aeaItemCond);
                    iterator.remove();
                }
            }
        }
        return childItemCondList;
    }

    private void sortAeaItemCondBySortNo(List<AeaItemCond> list) {
        list.sort(Comparator.comparing(aeaItemCond -> (aeaItemCond.getSortNo())));
    }

    private List<AeaItemCond> getTopAeaItemCondList(List<AeaItemCond> allCondList) {

        List<AeaItemCond> topAeaItemCondList = new ArrayList<>();
        if (allCondList != null && !allCondList.isEmpty()) {
            Iterator<AeaItemCond> iterator = allCondList.iterator();
            while (iterator.hasNext()) {
                AeaItemCond aeaItemCond = iterator.next();
                String parentCondId = aeaItemCond.getParentCondId();
                if (StringUtils.isBlank(parentCondId)) {
                    topAeaItemCondList.add(aeaItemCond);
                    iterator.remove();
                }
            }
        }
        return topAeaItemCondList;
    }

    private MindBaseNode getRootItemNode(AeaItemBasic aeaItem) {

        MindBaseNode mindBaseNode = new MindBaseNode();
        mindBaseNode.setId(aeaItem.getItemVerId());
        mindBaseNode.setName(aeaItem.getItemName());
        mindBaseNode.setPid(MindConst.MIND_NODE_ROOT);
        mindBaseNode.setOpen(MindConst.MIND_NODE_EXPAND_TRUE);
        mindBaseNode.setNodeTypeCode(AeaMindConst.MIND_NODE_TYPE_CODE_ITEM);
        return mindBaseNode;
    }

    private void addDeleteChildList(List<AeaItemCond> aeaItemCondList, List<Object> allDeleteList) {

        if (aeaItemCondList != null && aeaItemCondList.size() > 0) {
            for (AeaItemCond aeaItemCond : aeaItemCondList) {
                addDeleteAeaItemCondAndChild(aeaItemCond, allDeleteList);
            }
        }
    }


    private void addDeleteAeaItemCondAndChild(AeaItemCond aeaItemCond, List<Object> allDeleteList) {

        List<AeaItemCond> aeaItemCondList = aeaItemCondMapper.listChildAeaItemCondByParentCondId(aeaItemCond.getItemVerId(), aeaItemCond.getItemCondId(), SecurityContext.getCurrentOrgId());
        if (CollectionUtils.isNotEmpty(aeaItemCondList)) {
            for (AeaItemCond child : aeaItemCondList) {
                addDeleteAeaItemCondAndChild(child, allDeleteList);
            }
        }

        allDeleteList.add(aeaItemCond);
    }

    private AeaItemCond getNewAeaItemCondByOldItemCond(String itemId, String itemVerId, String parentCondId, MindExportData mindExportData, AeaItemCond oldAeaItemCond, boolean isQuestion) {

        AeaItemCond aeaItemCond = new AeaItemCond();
        BeanUtils.copyProperties(oldAeaItemCond, aeaItemCond);
        aeaItemCond.setCreateTime(new Date());
        aeaItemCond.setCreater(SecurityContext.getCurrentUserId());
        aeaItemCond.setCondName(mindExportData.getName());
        aeaItemCond.setParentCondId(parentCondId);
        aeaItemCond.setIsActive(Status.ON);
        aeaItemCond.setItemId(itemId);
        aeaItemCond.setItemVerId(itemVerId);
        aeaItemCond.setItemCondId(UUID.randomUUID().toString());
        aeaItemCond.setCondMemo(mindExportData.getNote());
        aeaItemCond.setModifier(null);
        aeaItemCond.setModifyTime(null);
        aeaItemCond.setIsQuestion(isQuestion?"1":"0");
        initAeaItemCond(aeaItemCond, mindExportData);
        return aeaItemCond;

    }

    private void initAeaItemCond(AeaItemCond aeaItemCond, MindExportData mindExportData){

        if(StringUtils.isNotBlank(mindExportData.getTerminateSituation())){
            aeaItemCond.setSfzz(mindExportData.getTerminateSituation());
        }else{
            aeaItemCond.setSfzz(Status.OFF);
        }
        if(StringUtils.isNotBlank(mindExportData.getSituationAnswerNum())){
            aeaItemCond.setMuiltSelect(Long.parseLong(mindExportData.getSituationAnswerNum()));
        }else{
            aeaItemCond.setMuiltSelect(null);
        }
    }


    private AeaItemCond getNewAeaItemCond(String itemId, String itemVerId, String parentCondId, MindExportData mindExportData, boolean isQuestion) {

        AeaItemCond aeaItemCond = new AeaItemCond();
        aeaItemCond.setCreateTime(new Date());
        aeaItemCond.setCreater(SecurityContext.getCurrentUserId());
        aeaItemCond.setCondName(mindExportData.getName());
        aeaItemCond.setParentCondId(parentCondId);
        aeaItemCond.setIsActive(Status.ON);
        aeaItemCond.setUseEl(Status.OFF);
        aeaItemCond.setItemId(itemId);
        aeaItemCond.setItemVerId(itemVerId);
        aeaItemCond.setItemCondId(UUID.randomUUID().toString());
        aeaItemCond.setCondMemo(mindExportData.getNote());
        aeaItemCond.setIsQuestion(isQuestion?"1":"0");

        initAeaItemCond(aeaItemCond,mindExportData);

        return aeaItemCond;
    }

    private void updateItemCond(AeaItemCond aeaItemCond, MindExportData mindExportData) {

        boolean needUpdate = false;
        String textName = mindExportData.getName();
        String note = mindExportData.getNote();

        if (textName != null && StringUtils.isNotBlank(textName)) {
            if (!textName.equals(aeaItemCond.getCondName())) {
                aeaItemCond.setCondName(textName);
                needUpdate = true;
            }
        }

        if (note != null && StringUtils.isNotBlank(note)) {
            if (!note.equals(aeaItemCond.getCondMemo())) {
                aeaItemCond.setCondMemo(note);
                needUpdate = true;
            }
        }

        String sfzz = aeaItemCond.getSfzz();

        if(StringUtils.isNotBlank(mindExportData.getTerminateSituation())){

            if(!mindExportData.getTerminateSituation().equals(sfzz)) {
                aeaItemCond.setSfzz(mindExportData.getTerminateSituation());
                needUpdate = true;
            }
        }else{
            if(!Status.OFF.equals(sfzz)) {
                aeaItemCond.setSfzz(Status.OFF);
                needUpdate = true;
            }
        }

        Long muiltSelect = aeaItemCond.getMuiltSelect();

        if(StringUtils.isNotBlank(mindExportData.getSituationAnswerNum())){

            Long situationAnswerNum = Long.parseLong(mindExportData.getSituationAnswerNum());

            if(!situationAnswerNum.equals(muiltSelect)) {
                aeaItemCond.setMuiltSelect(situationAnswerNum);
                needUpdate = true;
            }
        }else{
            if(muiltSelect!=null) {
                aeaItemCond.setMuiltSelect(null);
                needUpdate = true;
            }
        }

        if (needUpdate) {
            aeaItemCond.setModifier(SecurityContext.getCurrentUserId());
            aeaItemCond.setModifyTime(new Date());
            aeaItemCond.setIsActive(Status.ON);
            aeaItemCondMapper.updateAeaItemCond(aeaItemCond);
        }
    }


    private void removeAeaItemCond(List<AeaItemCond> list, String itemCondId) {

        if(StringUtils.isNotBlank(itemCondId)) {
            if (list != null && list.size() > 0) {
                List<AeaItemCond> needRemoveCond = new ArrayList<>();
                for(AeaItemCond cond : list){
                    if (StringUtils.isNotBlank(cond.getItemCondId())&&cond.getItemCondId().equals(itemCondId)) {
                        needRemoveCond.add(cond);
                        break;
                    }
                }
                if(needRemoveCond!=null&&needRemoveCond.size()>0){
                    list.removeAll(needRemoveCond);
                }
            }
        }
    }

    private void deleteNodeList(List<Object> allDeleteList) {

        if (CollectionUtils.isNotEmpty(allDeleteList)) {
            for (Object obj : allDeleteList) {
                if (obj instanceof AeaItemCond) {
                    AeaItemCond aeaItemCond = (AeaItemCond) obj;
                    aeaItemCondMapper.deleteAeaItemCond(aeaItemCond.getItemCondId());
                }
            }
        }
    }

}

