package com.augurit.aplanmis.common.service.admin.par.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.MindType;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.admin.par.AeaParStateItemAdminService;
import com.augurit.aplanmis.common.vo.ParStateAdminVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
* 情形与事项关联定义表-Service服务接口实现类
*/
@Service
@Transactional
public class AeaParStateItemAdminServiceImpl implements AeaParStateItemAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaParStateItemAdminServiceImpl.class);

    @Autowired
    private AeaParStateItemMapper aeaParStateItemMapper;

    @Autowired
    private AeaParStateMapper aeaParStateMapper;

    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;

    @Autowired
    private AeaItemMatMapper aeaItemMatMapper;

    @Autowired
    private AeaParStageItemInMapper aeaParStageItemInMapper;

    @Autowired
    private AeaParInMapper aeaParInMapper;

    @Override
    public void saveAeaParStateItem(AeaParStateItem aeaParStateItem) {

        aeaParStateItem.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaParStateItem.setCreater(SecurityContext.getCurrentUserId());
        aeaParStateItem.setCreateTime(new Date());
        aeaParStateItemMapper.insertAeaParStateItem(aeaParStateItem);
    }

    @Override
    public void saveStateItemRel(String parStateId, String stageItemId){

        if(StringUtils.isNotBlank(parStateId)&&StringUtils.isNotBlank(stageItemId)){
            AeaParStateItem stateItem = new AeaParStateItem();
            stateItem.setRootOrgId(SecurityContext.getCurrentOrgId());
            stateItem.setParStateId(parStateId);
            List<AeaParStateItem> list = aeaParStateItemMapper.listAeaParStateItem(stateItem);
            if(list!=null&&list.size()>0){
                for(AeaParStateItem item:list){
                    aeaParStateItemMapper.deleteAeaParStateItem(item.getStateItemId());
                }
            }
            stateItem.setStateItemId(UUID.randomUUID().toString());
            stateItem.setStageItemId(stageItemId);
            stateItem.setSortNo(getMaxSortNo());
            saveAeaParStateItem(stateItem);
        }
    }

    @Override
    public void updateAeaParStateItem(AeaParStateItem aeaParStateItem) {

        aeaParStateItemMapper.updateAeaParStateItem(aeaParStateItem);
    }

    @Override
    public void deleteAeaParStateItemById(String id) {

        if(StringUtils.isBlank(id)) {
            throw new InvalidParameterException(id);
        }
        aeaParStateItemMapper.deleteAeaParStateItem(id);
    }

    @Override
    public PageInfo<AeaParStateItem> listAeaParStateItem(AeaParStateItem aeaParStateItem, Page page) {

        aeaParStateItem.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaParStateItem> list = aeaParStateItemMapper.listAeaParStateItem(aeaParStateItem);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaParStateItem>(list);
    }

    @Override
    public AeaParStateItem getAeaParStateItemById(String id) {

        if(StringUtils.isBlank(id)) {
            throw new InvalidParameterException(id);
        }
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaParStateItemMapper.getAeaParStateItemById(id, SecurityContext.getCurrentOrgId());
    }

    @Override
    public List<AeaParStateItem> listAeaParStateItem(AeaParStateItem aeaParStateItem) {

        aeaParStateItem.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaParStateItem> list = aeaParStateItemMapper.listAeaParStateItem(aeaParStateItem);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public List<AeaParStateItem> listStateItemByStateId(String parStateId) {

        if(StringUtils.isNotBlank(parStateId)){
            return aeaParStateItemMapper.listStateItemByStateId(parStateId, SecurityContext.getCurrentOrgId());
        }
        return null;
    }

    @Override
    public Long getMaxSortNo(){

        String rootOrgId = SecurityContext.getCurrentOrgId();
        Long sortNo = aeaParStateItemMapper.getMaxSortNo(rootOrgId);
        return sortNo==null?new Long(1):sortNo+1;
    }

    @Override
    public void saveAeaParStateItemByStateIds(String stageItemId,String stateIds){

        if(StringUtils.isNotBlank(stateIds) && StringUtils.isNotBlank(stageItemId)){
            aeaParStateItemMapper.deleteAeaParStateItem(stageItemId);
            String[] ids = stateIds.split(",");
            if(ids!=null && ids.length>0){
                for(String id : ids){
                    AeaParStateItem aeaParStateItem = new AeaParStateItem();
                    aeaParStateItem.setStateItemId(UUID.randomUUID().toString());
                    aeaParStateItem.setParStateId(id);
                    aeaParStateItem.setSortNo(getMaxSortNo());
                    aeaParStateItem.setStageItemId(stageItemId);
                    saveAeaParStateItem(aeaParStateItem);
                }
            }

        }
    }

    @Override
    public Map<String, Object> getStageMatList(String stageId, String parentId, String level) throws Exception {

        Map<String, Object> resultmap = new HashMap<>();
        List<ParStateAdminVo> resultMats = new ArrayList<>();
        String rootOrgId = SecurityContext.getCurrentOrgId();
        AeaParState parState = new AeaParState();
        parState.setRootOrgId(rootOrgId);
        parState.setStageId(stageId);
        List<AeaParState> stateList = aeaParStateMapper.listAeaParState(parState);
        AeaParState parentQuestionState;
        if ("root".equals(parentId)) {
            parentQuestionState = new AeaParState();
        } else {
            parentQuestionState = aeaParStateMapper.getAeaParStateById(parentId);
        }
        if(stateList!=null&&stateList.size()>0){
            for (int i = 0; i < stateList.size(); i++) {
                AeaParState statetemp = stateList.get(i);
                boolean sign = false;
                if ("root".equals(parentId)) {
                    if (StringUtils.isBlank(statetemp.getParentStateId())) {
                        sign = true;
                    }
                } else {
                    if (parentId.equals(statetemp.getParentStateId())) {
                        sign = true;
                    }
                }
                if (sign) {
                    ParStateAdminVo psmtemp = new ParStateAdminVo();
                    List<AeaParState> resultAnswer = new ArrayList<>();
                    List<AeaParIn> resultParIn = new ArrayList<>();
                    if ("1".equals(statetemp.getIsQuestion())) {
                        AeaParState paramState = new AeaParState();
                        paramState.setRootOrgId(rootOrgId);
                        paramState.setParentStateId(statetemp.getParStateId());
                        List<AeaParState> answerList = aeaParStateMapper.listAeaParState(paramState);
                        for (int j = 0; j < answerList.size(); j++) {
                            resultAnswer.add(answerList.get(j));
                        }
                    }
                    psmtemp.setQuestionState(statetemp);
                    psmtemp.setAnswerList(resultAnswer);
                    if ("root".equals(parentId)) {
                        psmtemp.setParentQuestionStateId("root");
                    } else {
                        psmtemp.setParentQuestionStateId(parentQuestionState.getParentStateId());
                    }
                    resultMats.add(psmtemp);
                }
            }
        }
        resultmap.put("stateList", resultMats);
        if ("root".equals(parentId)) {
            List<AeaItemMat> matList = aeaItemMatMapper.listAeaItemMatByParInNoState(stageId);
            Map<String, Object> maptemp = new HashMap<>();
            maptemp.put("stateId", "root");
            maptemp.put("matsList", matList);
            resultmap.put("mats", maptemp);
            Map<String, Object> bindItemMap = new HashMap<>();
            bindItemMap.put("divid", "root");
            bindItemMap.put("stateid", "root");
            List<AeaItem> bindItemList = new ArrayList<>();
            bindItemMap.put("list", bindItemList);
            resultmap.put("bindItemMap", bindItemMap);
        } else {
            List<AeaItemMat> matList = aeaItemMatMapper.listAeaItemMatByParIn(stageId, parentQuestionState.getParStateId());
            Map<String, Object> maptemp = new HashMap<>();
            maptemp.put("stateId", parentQuestionState.getParStateId());
            maptemp.put("matsList", matList);
            resultmap.put("mats", maptemp);
            //查询该情形下关联的事项
            Map<String, Object> bindItemMap = new HashMap<>();
            bindItemMap.put("divid", parentQuestionState.getParentStateId());
            bindItemMap.put("stateid", parentId);
            List<AeaItemBasic> bindItemList = aeaItemBasicMapper.listItemByParState(parentId);
            bindItemMap.put("list", bindItemList);
            resultmap.put("bindItemMap", bindItemMap);
        }
        resultmap.put("showLevel", level);
        return resultmap;
    }


    @Override
    public Map<String, Object> getRemoveStateIds(String stateId, String stageId) throws Exception {

        Map<String, Object> resultMap = new HashMap<>();
        StringBuffer resultstr = new StringBuffer();
        List<Map<String, Object>> removeMatList = new ArrayList<>();
        AeaParState parState = new AeaParState();
        parState.setStageId(stageId);
        List<AeaParState> stateList = aeaParStateMapper.listAeaParState(parState);
        //当情形是多个答案的单选时，删除的情形会有多个
        String[] stateIdarr = stateId.split(",");
        for (int i = 0; i < stateIdarr.length; i++) {
            List<AeaParState> rlist = getRemoveState(stateList, stateIdarr[i]);
            for (int j = 0; j < rlist.size(); j++) {
                AeaParState statetemp = rlist.get(j);
                resultstr.append(statetemp.getParStateId()+",");
                if ("0".equals(statetemp.getIsQuestion())) {
                    List<AeaItemMat> mattemp = aeaItemMatMapper.listAeaItemMatByParIn(statetemp.getStageId(), statetemp.getParStateId());
                    Map<String, Object> removeMap = new HashMap<>();
                    removeMap.put("stateId", statetemp.getParStateId());
                    removeMap.put("matsList", mattemp);
                    removeMatList.add(removeMap);
                }
            }
        }
        String str = "";
        if (resultstr.toString().contains(",")) {
            str = resultstr.toString().substring(0, resultstr.toString().length() - 1);
        } else {
            str = resultstr.toString();
        }
        resultMap.put("removeIds", str);
        //删除材料时要删除所有子情形的材料和自己的材料
        List<AeaItemMat> ownmat = aeaItemMatMapper.listAeaItemMatByParIn(stageId, stateId);
        Map<String, Object> ownMap = new HashMap<>();
        ownMap.put("stateId", stateId);
        ownMap.put("matsList", ownmat);
        removeMatList.add(ownMap);
        resultMap.put("removeMat", removeMatList);
        return resultMap;
    }


    private List<AeaParState> getRemoveState(List<AeaParState> stateList, String stateId) {

        List<AeaParState> resultlist = new ArrayList<>();
        for (int i = 0; i < stateList.size(); i++) {
            AeaParState statetemp = stateList.get(i);
            if (stateId.equals(statetemp.getParentStateId())) {
                resultlist.add(statetemp);
                List<AeaParState> listtemp = getRemoveState(stateList, statetemp.getParStateId());
                for (int j = 0; j < listtemp.size(); j++) {
                    resultlist.add(listtemp.get(j));
                }
            }
        }
        return resultlist;
    }

    @Override
    public void deleteStateItemByStageItemId(String stageItemId){
        if(stageItemId == null) {
            throw new InvalidParameterException(stageItemId);
        }
        aeaParStateItemMapper.deleteAeaParStateItem(stageItemId);
    }

    @Override
    public void saveAeaParStateItemByStageItemIds(String stateId,String stageItemIds,String stageId){

        String rootOrgId = SecurityContext.getCurrentOrgId();
        aeaParStateItemMapper.deleteAeaParStateItemByStateId(stateId, rootOrgId);
        String[] ids = null;
        if(StringUtils.isNotBlank(stageItemIds)) {
            ids = stageItemIds.split(",");
        }
        //级联删除--删除表AEA_PAR_STAGE_ITEM_IN中不属于事项数组的数据
        if(ids!=null && ids.length>0){
            for(String id : ids){
                if(StringUtils.isNotBlank(id)){
                    AeaParStateItem aeaParStateItem = new AeaParStateItem();
                    aeaParStateItem.setStateItemId(UUID.randomUUID().toString());
                    aeaParStateItem.setParStateId(stateId);
                    aeaParStateItem.setSortNo(getMaxSortNo());
                    aeaParStateItem.setStageItemId(id);
                    saveAeaParStateItem(aeaParStateItem);
                }
            }
        }

        // 满足前端特殊情况处理
        AeaParState state = aeaParStateMapper.getAeaParStateById(stateId);
        AeaParState parentState = null;
        if(state!=null&&StringUtils.isNotBlank(state.getParentStateId())){
            parentState = aeaParStateMapper.getAeaParStateById(state.getParentStateId());
        }
        if(parentState!=null){
            List<AeaParStateItem> stateItemList = aeaParStateItemMapper.listStateItemByParentStateId(parentState.getParStateId(), rootOrgId);
            if(CollectionUtils.isNotEmpty(stateItemList)){
                parentState.setIsQuestionOri("2");
            }else{
                parentState.setIsQuestionOri(parentState.getIsQuestion());
            }
            parentState.setModifier(SecurityContext.getCurrentUserId());
            parentState.setModifyTime(new Date());
            aeaParStateMapper.updateAeaParState(parentState);
        }
    }

    @Override
    public void saveAeaParStateItemByStageMatItemIds(String stateId, String stageItemIds, String matCertId, String stageId, String fileType) {

        if(StringUtils.isBlank(stageId)){
            throw new InvalidParameterException("参数stageId为空!");
        }
        if(StringUtils.isBlank(fileType)){
            throw new InvalidParameterException("参数fileType为空!");
        }
        if(StringUtils.isBlank(matCertId)){
            throw new InvalidParameterException("参数matCertId为空!");
        }
        String rootOrgId = SecurityContext.getCurrentOrgId();
        AeaParIn aeaParIn = new AeaParIn();
        aeaParIn.setStageId(stageId);
        aeaParIn.setRootOrgId(rootOrgId);
        if(MindType.CERTIFICATE.getValue().equals(fileType)) {
            aeaParIn.setFileType(MindType.CERTIFICATE.getValue());
            aeaParIn.setCertId(matCertId);
        }else{
            aeaParIn.setFileType(MindType.MATERIAL.getValue());
            aeaParIn.setMatId(matCertId);
        }
        if(StringUtils.isNotBlank(stateId)) {
            aeaParIn.setIsStateIn("1");
            aeaParIn.setParStateId(stateId);
        }else{
            aeaParIn.setIsStateIn("0");
        }
        List<AeaParIn> aeaParIns = aeaParInMapper.listAeaParIn(aeaParIn);
        if(aeaParIns!=null&&aeaParIns.size()>0){
            AeaParIn in = aeaParIns.get(0);
            String inId = in.getInId();
            // 先删除关联
            aeaParStageItemInMapper.deleteAeaParStageItemInByInId(inId);
            // 再建立关联
            String[] ids = stageItemIds.split(",");
            if(ids!=null && ids.length>0){
                for(String id : ids){
                    if(id!=null && StringUtils.isNotBlank(id)) {
                        AeaParStageItemIn aeaParStageItemIn = new AeaParStageItemIn();
                        aeaParStageItemIn.setItemInId(UUID.randomUUID().toString());
                        aeaParStageItemIn.setStageItemId(id);
                        aeaParStageItemIn.setInId(inId);
                        aeaParStageItemIn.setCreater(SecurityContext.getCurrentUserId());
                        aeaParStageItemIn.setCreateTime(new Date());
                        aeaParStageItemInMapper.insertAeaParStageItemIn(aeaParStageItemIn);
                    }
                }
            }
        }
    }

    @Override
    public void saveAeaParStateMatItemByStageItemIds(String stateId, String stageItemIds) {

        if(StringUtils.isBlank(stateId)) {
            throw new InvalidParameterException("参数stateId为空!");
        }
        if(StringUtils.isNotBlank(stageItemIds)){
            String rootOrgId = SecurityContext.getCurrentOrgId();
            aeaParStateItemMapper.deleteAeaParStateItemByStateId(stateId, rootOrgId);
            String[] ids = stageItemIds.split(",");
            if(ids!=null && ids.length>0){
                for(String id : ids){
                    AeaParStateItem aeaParStateItem = new AeaParStateItem();
                    aeaParStateItem.setStateItemId(UUID.randomUUID().toString());
                    aeaParStateItem.setParStateId(stateId);
                    aeaParStateItem.setSortNo(getMaxSortNo());
                    aeaParStateItem.setStageItemId(id);
                    saveAeaParStateItem(aeaParStateItem);
                }
            }

        }
    }
}

