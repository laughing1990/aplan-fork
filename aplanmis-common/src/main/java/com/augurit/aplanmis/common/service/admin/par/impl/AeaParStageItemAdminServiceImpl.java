package com.augurit.aplanmis.common.service.admin.par.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.agcloud.framework.util.JsonUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.mapper.OpuOmOrgMapper;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.AeaItemBasicMapper;
import com.augurit.aplanmis.common.mapper.AeaItemMapper;
import com.augurit.aplanmis.common.mapper.AeaItemVerMapper;
import com.augurit.aplanmis.common.mapper.AeaParStageItemInMapper;
import com.augurit.aplanmis.common.mapper.AeaParStageItemMapper;
import com.augurit.aplanmis.common.mapper.AeaParStageMapper;
import com.augurit.aplanmis.common.mapper.AeaParStateItemMapper;
import com.augurit.aplanmis.common.mapper.AeaProjInfoMapper;
import com.augurit.aplanmis.common.service.admin.par.AeaParStageItemAdminService;
import com.augurit.aplanmis.common.service.admin.par.AeaParThemeVerAdminService;
import com.augurit.aplanmis.common.service.item.AeaItemBasicService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class AeaParStageItemAdminServiceImpl implements AeaParStageItemAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaParStageItemAdminServiceImpl.class);

    @Autowired
    private AeaParStageItemMapper aeaParStageItemMapper;

    @Autowired
    private AeaParStageMapper aeaParStageMapper;

    @Autowired
    private AeaParStageItemInMapper aeaParStageItemInMapper;

    @Autowired
    private AeaParStateItemMapper aeaParStateItemMapper;

    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;

    @Autowired
    private AeaItemBasicService aeaItemBasicService;

    @Autowired
    private AeaItemMapper aeaItemMapper;

    @Autowired
    private AeaItemVerMapper aeaItemVerMapper;

    @Autowired
    private OpuOmOrgMapper opuOmOrgMapper;

    @Autowired
    private AeaProjInfoMapper aeaProjInfoMapper;

    @Autowired
    private AeaParThemeVerAdminService aeaParThemeVerAdminService;

    @Override
    public void saveAeaParStageItem(AeaParStageItem aeaParStageItem) {
        aeaParStageItemMapper.insertAeaParStageItem(aeaParStageItem);
    }

    @Override
    public void updateAeaParStageItem(AeaParStageItem aeaParStageItem) {
        aeaParStageItemMapper.updateAeaParStageItem(aeaParStageItem);
    }

    @Override
    public void deleteAeaParStageItemById(String id) {
        Assert.notNull(id, "id is null.");
        aeaParStageItemMapper.deleteAeaParStageItem(id);
    }

    @Override
    public List<AeaParStageItem> listAeaStageItemCondition(AeaParStageItem aeaParStageItem){

        aeaParStageItem.setRootOrgId(SecurityContext.getCurrentOrgId());
        return aeaParStageItemMapper.listAeaStageItemCondition(aeaParStageItem);
    }

    /**
     * 根据阶段定义id分页查询
     */
    @Override
    public EasyuiPageInfo<AeaParStageItem> listAeaParStageItem(AeaParStageItem aeaParStageItem, Page page) {

        List<AeaParStageItem> list = new ArrayList<>();
        aeaParStageItem.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        if (StringUtils.isNotBlank(aeaParStageItem.getStageId())) {
            list = aeaParStageItemMapper.listAeaStageItemCondition(aeaParStageItem);
        }
        logger.debug("成功执行分页查询！！");
        return PageHelper.toEasyuiPageInfo(new PageInfo<>(list));
    }

    @Override
    public EasyuiPageInfo<AeaParStageItem> listStageAllItem(AeaParStageItem aeaParStageItem, Page page){

        aeaParStageItem.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaParStageItem> list = aeaParStageItemMapper.listStageAllItem(aeaParStageItem);
        logger.debug("成功执行分页查询！！");
        return PageHelper.toEasyuiPageInfo(new PageInfo<>(list));
    }

    /**
     * 根据阶段定义ID和情形ID分页查询
     */
    @Override
    public EasyuiPageInfo<AeaParStageItem> listAeaParStageMatItem(AeaParStageItem aeaParStageItem, Page page) {
        List<AeaParStageItem> list = new ArrayList<>();
        PageHelper.startPage(page);
        if (org.apache.commons.lang3.StringUtils.isNotBlank(aeaParStageItem.getStageId()) && org.apache.commons.lang3.StringUtils.isNotBlank(aeaParStageItem.getParStateId())) {
            list = aeaParStageItemMapper.listAeaStageMatItemCondition(aeaParStageItem);
        }
        logger.debug("成功执行分页查询！！");
        return PageHelper.toEasyuiPageInfo(new PageInfo<>(list));
    }

    /**
     * 根据情形id和材料id分页查询
     */
    @Override
    public EasyuiPageInfo<AeaParStageItem> listAeaParStateItem(AeaParStageItem aeaParStageItem, Page page) {
        return null;
    }

    @Override
    public AeaParStageItem getAeaParStageItemById(String id) {
        Assert.notNull(id, "id is null.");
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaParStageItemMapper.getAeaParStageItemById(id);
    }

    @Override
    public List<AeaParStageItem> listAeaParStageItem(AeaParStageItem aeaParStageItem) {
        List<AeaParStageItem> list = aeaParStageItemMapper.listAeaParStageItem(aeaParStageItem);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public List<AeaParStageItem> listAeaStageItemByStageId(String stageId, String isOptionItem) {

        List<AeaParStageItem> list = aeaParStageItemMapper.listAeaStageItemByStageId(stageId, isOptionItem, SecurityContext.getCurrentOrgId());
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public List<AeaParStageItem> listAeaStageItemInfoByStageId(String stageId) {

        if (StringUtils.isNotBlank(stageId)) {
            AeaParStageItem aeaParStageItem = new AeaParStageItem();
            aeaParStageItem.setStageId(stageId);
            return aeaParStageItemMapper.listAeaStageItemCondition(aeaParStageItem);
        }
        return null;
    }

    @Override
    public void batchDeleteStageItemByStageId(String stageId) {

        AeaParStageItem parStageItem = new AeaParStageItem();
        if (StringUtils.isNotBlank(stageId)) {
            parStageItem.setStageId(stageId);
            List<AeaParStageItem> stageItemList = aeaParStageItemMapper.listAeaParStageItem(parStageItem);
            if (stageItemList != null && stageItemList.size() > 0) {
                for (AeaParStageItem aeaParStageItem : stageItemList) {
                    aeaParStageItemInMapper.deleteAeaParStageItemInByStageItemId(aeaParStageItem.getStageItemId());
                    aeaParStateItemMapper.deleteAeaParStateItem(aeaParStageItem.getStageItemId());
                }
            }
            aeaParStageItemMapper.batchDeleteStageItemByStageId(stageId);
        }
    }

    @Override
    public void batchDeleteOneTypeStageItem(String stageId, String isOptionItem){

        AeaParStageItem parStageItem = new AeaParStageItem();
        if (StringUtils.isNotBlank(stageId)&&StringUtils.isNotBlank(isOptionItem)) {

            AeaParStage queryStage = new AeaParStage();
            queryStage.setStageId(stageId);
            Map<String, Object> map = aeaParThemeVerAdminService.getAeaParThemeVerAndCells(queryStage);
            JSONObject diagram  = null;
            JSONArray cells = null;
            AeaParThemeVer themeVer= null;
            if(map != null && map.get("cells") != null){
                diagram = (JSONObject) map.get("aeaParThemeVerDiagram");
                themeVer = (AeaParThemeVer) map.get("aeaParThemeVer");
                cells = (JSONArray) diagram.get("cells");
            }

            parStageItem.setStageId(stageId);
            parStageItem.setIsOptionItem(isOptionItem);
            List<AeaParStageItem> stageItemList = aeaParStageItemMapper.listAeaParStageItem(parStageItem);
            if (stageItemList != null && stageItemList.size() > 0) {
                for (AeaParStageItem aeaParStageItem : stageItemList) {
                    // 材料事项
                    aeaParStageItemInMapper.deleteAeaParStageItemInByStageItemId(aeaParStageItem.getStageItemId());
                    // 情形事项
                    aeaParStateItemMapper.deleteAeaParStateItem(aeaParStageItem.getStageItemId());
                    // 阶段事项
                    aeaParStageItemMapper.deleteAeaParStageItem(aeaParStageItem.getStageItemId());
                    //从全景图中删除对应的事项
                    aeaParThemeVerAdminService.removeEleFromDiagram(cells, queryStage, aeaParStageItem.getStageItemId(), isOptionItem);
                }

                if(themeVer != null){
                    try{
                        aeaParThemeVerAdminService.ajaustPoolHeightAndReturnMaxHeight(cells,"bpmn.SPool");
                        aeaParThemeVerAdminService.ajaustPoolHeightAndReturnMaxHeight(cells,"bpmn.Pool");
                        aeaParThemeVerAdminService.ajaustPoolHeightAndReturnMaxHeight(cells,"bpmn.HPool");
                        aeaParThemeVerAdminService.setPoolHeightInCommon(cells,"bpmn.HPool");
                        themeVer.setThemeVerDiagram(JsonUtils.toJson(diagram));
                        aeaParThemeVerAdminService.updateAeaParThemeVer(themeVer);
                    }catch (Exception e){
                        logger.error("diagramException:", e);
                    }
                }
            }
        }
    }


    /**
     *
     * @param stageId
     * @param itemIds
     * @param sortNos
     * @param isOptionItem 是否必选事项
     */
    @Override
    public void batchSaveStageItem(String stageId, String[] itemIds, String[] sortNos,String isOptionItem) {

        if (StringUtils.isNotBlank(stageId)) {
            AeaParStage queryStage = new AeaParStage();
            queryStage.setStageId(stageId);
            Map<String, Object> map = aeaParThemeVerAdminService.getAeaParThemeVerAndCells(queryStage);
            AeaParThemeVer themeVer = null;
            JSONObject diagram  = null;
            JSONArray cells = null;
            Map activity = null;
            Map pool = null;
            Map hPool = null;
            if(map != null && map.get("cells") != null){
                themeVer = (AeaParThemeVer) map.get("aeaParThemeVer");
                diagram = (JSONObject) map.get("aeaParThemeVerDiagram");
                cells = (JSONArray) diagram.get("cells");
                activity = (Map) map.get("activity");
                pool = (Map) map.get("pool");
                hPool = (Map) map.get("hPool");
            }


            List<String> itemIdAndVerIdList = new ArrayList<>(Arrays.asList(itemIds));
            List<Map<String,String>> itemMapList = new ArrayList<>();
            itemIdAndVerIdList.forEach(s -> {
                String[] array = s.split("\\*");
                Map<String,String> itemMap = new HashMap<>();
                itemMap.put("itemId",array[0]);
                itemMap.put("itemVerId",array[1]);
                itemMapList.add(itemMap);
            });
            AeaParStageItem stageItem = new AeaParStageItem();
            stageItem.setStageId(stageId);
            stageItem.setIsOptionItem(isOptionItem);
            List<String> oldItemIdList = new ArrayList<>();
            List<AeaParStageItem> oldStageItemList = new ArrayList<>();
            List<AeaParStageItem> stateItemList = aeaParStageItemMapper.listAeaParStageItem(stageItem);
            if (stateItemList != null && stateItemList.size() > 0) {
                for (AeaParStageItem vo : stateItemList) {
                    // 传递参数中不存在旧数据，删除掉
                    if (!itemIdAndVerIdList.contains(vo.getItemId()+"*"+vo.getItemVerId())) {
                        // 材料事项
                        aeaParStageItemInMapper.deleteAeaParStageItemInByStageItemId(vo.getStageItemId());
                        // 情形事项
                        aeaParStateItemMapper.deleteAeaParStateItem(vo.getStageItemId());
                        // 阶段事项
                        aeaParStageItemMapper.deleteAeaParStageItem(vo.getStageItemId());
                        //从全景图中删除对应的事项
                        aeaParThemeVerAdminService.removeEleFromDiagram(cells, queryStage, vo.getStageItemId(), isOptionItem);
                    }else{
                        // 旧的数据
                        oldItemIdList.add(vo.getItemId()+"*"+vo.getItemVerId());
                        oldStageItemList.add(vo);
                    }
                }
            }
            // 保存
            String userId = SecurityContext.getCurrentUserId();
            if (itemMapList!=null&&itemMapList.size() > 0) {
                for (int i = 0; i < itemMapList.size(); i++) {
                    // 新增数据
                    String flag = itemMapList.get(i).get("itemId")+"*"+itemMapList.get(i).get("itemVerId");
                    if (!oldItemIdList.contains(flag)) {

                        AeaParStageItem stageItem1 = new AeaParStageItem();
                        stageItem1.setStageItemId(UUID.randomUUID().toString());
                        stageItem1.setStageId(stageId);
                        stageItem1.setItemId(itemMapList.get(i).get("itemId"));
                        stageItem1.setItemVerId(itemMapList.get(i).get("itemVerId"));
                        stageItem1.setSortNo(new Long(sortNos[i]));
                        stageItem1.setCreater(userId);
                        stageItem1.setCreateTime(new Date());
                        stageItem1.setIsOptionItem(isOptionItem);
                        aeaParStageItemMapper.insertAeaParStageItem(stageItem1);

                        try {
                            AeaItemBasic basic = aeaItemBasicMapper.getOneByItemVerId(stageItem1.getItemVerId(), SecurityContext.getCurrentOrgId());
                            stageItem1.setItemName(basic.getItemName());
                            stageItem1.setDueNum(String.valueOf(basic.getDueNum().intValue()));
                            aeaParThemeVerAdminService.addItemToDiagram(cells, queryStage, stageItem1, isOptionItem, hPool, activity, pool);
                        } catch (Exception e) {
                            logger.error("DiagramException", e);
                            e.printStackTrace();
                        }


                    // 更新数据
                    }else{
                        if(oldStageItemList!=null&&oldStageItemList.size()>0){
                            for(AeaParStageItem oldStageItem : oldStageItemList){
                                if((oldStageItem.getItemId()+"*"+oldStageItem.getItemVerId()).equals(flag)){
                                    oldStageItem.setSortNo(new Long(sortNos[i]));
                                    aeaParStageItemMapper.updateAeaParStageItem(oldStageItem);
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            if(themeVer != null){
                try{
                    aeaParThemeVerAdminService.ajaustPoolHeightAndReturnMaxHeight(cells,"bpmn.SPool");
                    aeaParThemeVerAdminService.ajaustPoolHeightAndReturnMaxHeight(cells,"bpmn.Pool");
                    aeaParThemeVerAdminService.ajaustPoolHeightAndReturnMaxHeight(cells,"bpmn.HPool");
                    aeaParThemeVerAdminService.setPoolHeightInCommon(cells,"bpmn.HPool");
                    themeVer.setThemeVerDiagram(JsonUtils.toJson(diagram));
                    aeaParThemeVerAdminService.updateAeaParThemeVer(themeVer);
                }catch (Exception e){
                    logger.error("diagramException:", e);
                }
            }
        }
    }



    @Override
    public List<ZtreeNode> listStageItemTreeByStageId(String stageId) {

        List<ZtreeNode> allNodes = new ArrayList<>();
        if (StringUtils.isNotBlank(stageId)) {
            String rootOrgId = SecurityContext.getCurrentOrgId();
            AeaParStage stage = aeaParStageMapper.getAeaParStageById(stageId);
            if (stage != null) {
                ZtreeNode rootNode = new ZtreeNode();
                rootNode.setId(stage.getStageId());
                rootNode.setName(stage.getStageName());
                rootNode.setType("stage");
                rootNode.setIsParent(true);
                rootNode.setOpen(true);
                rootNode.setNocheck(true);
                allNodes.add(rootNode);
                List<AeaParStageItem> list = aeaParStageItemMapper.listAeaStageItemByStageId(stageId, "", rootOrgId);
                if (list != null && list.size() > 0) {
                    for (AeaParStageItem stageItem : list) {
                        ZtreeNode noNode = new ZtreeNode();
                        noNode.setId(stageItem.getStageItemId());
                        noNode.setName(stageItem.getItemName());
                        noNode.setType("item");
                        noNode.setpId(stageId);
                        noNode.setIsParent(false);
                        noNode.setOpen(true);
                        noNode.setNocheck(false);
                        allNodes.add(noNode);
                    }
                }
            } else {
                ZtreeNode noNode = getNULLZtreeNode();
                allNodes.add(noNode);
            }
        } else {
            ZtreeNode noNode = getNULLZtreeNode();
            allNodes.add(noNode);
        }
        return allNodes;
    }

    /**
     * 根据阶段id获取绑定的事项树，报错标准事项和实施事项
     * @param stageId
     * @return
     */
    @Override
    public List<ZtreeNode> getStageItemTreeByStageId(String stageId) {
        List<ZtreeNode> allNodes = new ArrayList<>();
        if (StringUtils.isNotBlank(stageId)) {
            String rootOrgId = SecurityContext.getCurrentOrgId();
            AeaParStage stage = aeaParStageMapper.getAeaParStageById(stageId);
            if (stage != null) {
                List<AeaParStageItem> list = aeaParStageItemMapper.listAeaStageItemByStageId(stageId, "", rootOrgId);
                if (list != null && list.size() > 0) {
                    for (AeaParStageItem stageItem : list) {
                        //获取到关联的事项版本id
                        String itemVerId = stageItem.getItemVerId();
                        buildItemTree(itemVerId,null,rootOrgId,allNodes);
                    }
                }
            } else {
                ZtreeNode noNode = getNULLZtreeNode();
                allNodes.add(noNode);
            }
        } else {
            ZtreeNode noNode = getNULLZtreeNode();
            allNodes.add(noNode);
        }
        return allNodes;
    }

    private void buildItemTree(String itemVerId,String pId,String rootOrgId,List<ZtreeNode> allNodes){
        AeaItemBasic aeaItemBasic = aeaItemBasicMapper.getAeaItemBasicByItemVerId(itemVerId, rootOrgId);
        if(aeaItemBasic == null){
            return;
        }
        String nodeName = aeaItemBasic.getItemName();
        OpuOmOrg activeOrg = opuOmOrgMapper.getActiveOrg(aeaItemBasic.getOrgId());
        if (activeOrg != null) {
            nodeName += "【" + activeOrg.getOrgName() + "】";
        }
        //构建树节点
        ZtreeNode noNode = new ZtreeNode();
        noNode.setId(itemVerId);
        noNode.setName(nodeName);
        noNode.setOpen(true);
        noNode.setNocheck(false);
        noNode.setpId(pId);
        if("1".equals(aeaItemBasic.getIsCatalog())){
            noNode.setType("bz_item");
            noNode.setIsParent(true);
            List<AeaItem> aeaItems = aeaItemMapper.getAeaItemByParentItemId(aeaItemBasic.getItemId());
            for(AeaItem item : aeaItems){
                AeaItemVer query = new AeaItemVer();
                query.setItemId(item.getItemId());
                query.setRootOrgId(rootOrgId);
                List<AeaItemVer> aeaItemVers = aeaItemVerMapper.listAeaItemVer(query);
                if(aeaItemVers.size() > 0){
                    buildItemTree(aeaItemVers.get(0).getItemVerId(),itemVerId,rootOrgId,allNodes);
                }
            }
        }else{
            noNode.setType("ss_item");
            noNode.setIsParent(false);
        }
        allNodes.add(noNode);
    }

    private ZtreeNode getNULLZtreeNode() {

        ZtreeNode noNode = new ZtreeNode();
        noNode.setId("noNode");
        noNode.setName("暂无事项");
        noNode.setType("noNode");
        noNode.setIsParent(true);
        noNode.setOpen(true);
        noNode.setNocheck(true);
        return noNode;
    }


    @Override
    public EasyuiPageInfo<AeaParStageItem> listAeaParStageItemWhichBindState(AeaParStageItem aeaParStageItem, Page page) {

        List<AeaParStageItem> list = new ArrayList<>();
        PageHelper.startPage(page);
        if (StringUtils.isNotBlank(aeaParStageItem.getStageId())) {
            list = aeaParStageItemMapper.listAeaParStageItemWhichBindState(aeaParStageItem);
        }
        logger.debug("成功执行分页查询！！");
        return PageHelper.toEasyuiPageInfo(new PageInfo<>(list));
    }

    @Override
    public List<AeaParStageItem> listAeaParStageItemWithoutBindState(String stageId) {

        if (StringUtils.isNotBlank(stageId)) {
            return aeaParStageItemMapper.listAeaStageItemByStageId(stageId, "", SecurityContext.getCurrentOrgId());
        }
        return null;
    }


    @Override
    public List<AeaParStageItem> listStageItemByInId(String StageId, String isOptionItem, String inId) {

        String rootOrgId = SecurityContext.getCurrentOrgId();
        AeaParStageItem sstageItem = new AeaParStageItem();
        sstageItem.setRootOrgId(rootOrgId);
        sstageItem.setStageId(StageId);
        List<AeaParStageItem> stageItemList = aeaParStageItemMapper.listAeaStageItemCondition(sstageItem);
        List<AeaParStageItem> ItemInList = aeaParStageItemMapper.listAeaStageItemByParIn(inId, rootOrgId);
        if (stageItemList != null && stageItemList.size() > 0) {
            for (AeaParStageItem aStageItemList : stageItemList) {
                if (ItemInList != null && ItemInList.size() > 0) {
                    for (AeaParStageItem stageItem : ItemInList) {
                        if (stageItem.getStageItemId().equals(aStageItemList.getStageItemId())) {
                            aStageItemList.setIsCheck(true);
                            break;
                        } else {
                            aStageItemList.setIsCheck(false);
                        }
                    }
                }
            }
        }
        return stageItemList;
    }

    @Override
    public void batchUpdateStageItemByItemVerChange(String themeVerId, String itemId, String itemVerId, String newItemVerId, String rootOrgId){

        aeaParStageItemMapper.batchUpdateStageItemByItemVerChange(themeVerId, itemId, itemVerId, newItemVerId, rootOrgId);
    }

    @Override
    public void handleStageItemsToOnly(String stageId, String isOptionItem){

        // 获取重复事项
        String rootOrgId = SecurityContext.getCurrentOrgId();
        List<AeaParStageItem> list = aeaParStageItemMapper.listRepeatStageItem(stageId, isOptionItem);
        if(list!=null&&list.size()>0){
            for(AeaParStageItem item : list){
                AeaParStageItem s = new AeaParStageItem();
                s.setStageId(stageId);
                s.setIsOptionItem(isOptionItem);
                s.setItemId(item.getItemId());
                s.setItemVerId(item.getItemVerId());
                List<AeaParStageItem> allList = aeaParStageItemMapper.listAeaParStageItem(s);
                if(allList!=null&&allList.size()>0){
                    AeaParStageItem stageItem = allList.get(0);
                    // 必选或可选事项
                    if(isOptionItem.equals(Status.OFF)||isOptionItem.equals(Status.ON)){
                        // 1、处理材料事项
                        List<AeaParStageItemIn> stageItemIns = aeaParStageItemInMapper.listStageItemInByItemAndOption(stageId, isOptionItem, item.getItemId(),item.getItemVerId());
                        if(stageItemIns!=null&&stageItemIns.size()>0){
                            for(AeaParStageItemIn stageItemIn:stageItemIns){
                                stageItemIn.setStageItemId(stageItem.getStageItemId());
                                aeaParStageItemInMapper.updateAeaParStageItemIn(stageItemIn);
                            }
                        }
                        // 2、处理情形事项
                        List<AeaParStateItem> stateItems = aeaParStateItemMapper.listStageStateItemByItemAndOption(stageId, isOptionItem, item.getItemId(),item.getItemVerId(), rootOrgId);
                        if(stateItems!=null&&stateItems.size()>0){
                            for(AeaParStateItem stateItem:stateItems){
                                stateItem.setStageItemId(stageItem.getStageItemId());
                                aeaParStateItemMapper.updateAeaParStateItem(stateItem);
                            }
                        }
                    }
                    // 删除其他重复事项
                    allList.remove(stageItem);
                    for(AeaParStageItem other:allList){
                        aeaParStageItemMapper.deleteAeaParStageItem(other.getStageItemId());
                    }
                }
            }
        }
    }

    @Override
    public Long getMaxSortNoByStageId(String stageId){

        Long sortNo = aeaParStageItemMapper.getMaxSortNoByStageId(stageId);
        return sortNo==null?1L:(sortNo+1L);
    }

    /**
     * 查询配置了指定事项的主题和阶段
     *
     * @param projInfoId 项目projInfoId
     * @param itemVerId  实施事项itemVerId
     */
    @Override
    public List<AeaParStageItem> checkBeforeSeriesFlow(String projInfoId, String itemVerId, String rootOrgId) {
        try {
            AeaProjInfo aeaProjInfo = aeaProjInfoMapper.getAeaProjInfoById(projInfoId);

            List<String> itemVerIds = new ArrayList<>();
            itemVerIds.add(itemVerId);
            AeaItemBasic aeaItemBasic = aeaItemBasicMapper.getAeaItemBasicByItemVerId(itemVerId, rootOrgId);
            // 获取标准事项
            AeaItemBasic catalogItem = aeaItemBasicService.getCatalogItemByCarryOutItemId(aeaItemBasic.getItemId());
            if (catalogItem != null) {
                itemVerIds.add(catalogItem.getItemVerId());
            }
            return aeaParStageItemMapper.checkBeforeSeriesFlow(itemVerIds, aeaProjInfo.getThemeId(), rootOrgId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}