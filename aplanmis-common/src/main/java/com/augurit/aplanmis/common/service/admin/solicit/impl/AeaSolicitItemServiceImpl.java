package com.augurit.aplanmis.common.service.admin.solicit.impl;

import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.mapper.BscDicCodeMapper;
import com.augurit.agcloud.bsc.sc.dic.code.service.BscDicCodeService;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaSolicitItem;
import com.augurit.aplanmis.common.mapper.AeaSolicitItemMapper;
import com.augurit.aplanmis.common.service.admin.solicit.AeaSolicitItemService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 按事项征求配置表-Service服务接口实现类
 */
@Service
@Transactional
public class AeaSolicitItemServiceImpl implements AeaSolicitItemService {

    private static Logger logger = LoggerFactory.getLogger(AeaSolicitItemServiceImpl.class);

    @Autowired
    private BscDicCodeMapper bscDicCodeMapper;

    @Autowired
    private AeaSolicitItemMapper aeaSolicitItemMapper;

    @Override
    public void saveAeaSolicitItem(AeaSolicitItem AeaSolicitItem) {

        AeaSolicitItem.setRootOrgId(SecurityContext.getCurrentOrgId());
        AeaSolicitItem.setCreater(SecurityContext.getCurrentUserId());
        AeaSolicitItem.setCreateTime(new Date());
        aeaSolicitItemMapper.insertAeaSolicitItem(AeaSolicitItem);
    }

    @Override
    public void updateAeaSolicitItem(AeaSolicitItem AeaSolicitItem) {

        AeaSolicitItem.setModifier(SecurityContext.getCurrentUserId());
        AeaSolicitItem.setModifyTime(new Date());
        aeaSolicitItemMapper.updateAeaSolicitItem(AeaSolicitItem);
    }

    @Override
    public void deleteAeaSolicitItemById(String id) {

        if(StringUtils.isBlank(id)){
            throw new InvalidParameterException("参数id为空!");
        }
        aeaSolicitItemMapper.deleteAeaSolicitItem(id);
    }

    @Override
    public void batchDelSolicitItemByIds(String[] ids){

        if(ids!=null&&ids.length>0){
            aeaSolicitItemMapper.batchDelSolicitItemByIds(ids);
        }else{
            throw new InvalidParameterException("参数ids为空!");
        }
    }

    @Override
    public PageInfo<AeaSolicitItem> listAeaSolicitItem(AeaSolicitItem AeaSolicitItem, Page page) {

        AeaSolicitItem.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaSolicitItem> list = aeaSolicitItemMapper.listAeaSolicitItem(AeaSolicitItem);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaSolicitItem>(list);
    }

    @Override
    public AeaSolicitItem getAeaSolicitItemById(String id) {

        if(StringUtils.isBlank(id)){
            throw new InvalidParameterException("参数id为空!");
        }
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaSolicitItemMapper.getAeaSolicitItemById(id);
    }

    @Override
    public AeaSolicitItem getAeaSolicitItemRelInfoById(String id){

        if(StringUtils.isBlank(id)){
            throw new InvalidParameterException("参数id为空!");
        }
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaSolicitItemMapper.getAeaSolicitItemRelInfoById(id);
    }

    @Override
    public List<AeaSolicitItem> listAeaSolicitItem(AeaSolicitItem AeaSolicitItem) {

        AeaSolicitItem.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaSolicitItem> list = aeaSolicitItemMapper.listAeaSolicitItem(AeaSolicitItem);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public PageInfo<AeaSolicitItem> listAeaSolicitItemRelInfo(AeaSolicitItem AeaSolicitItem, Page page) {

        AeaSolicitItem.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaSolicitItem> list = aeaSolicitItemMapper.listAeaSolicitItemRelInfo(AeaSolicitItem);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaSolicitItem>(list);
    }

    @Override
    public List<AeaSolicitItem> listAeaSolicitItemRelInfo(AeaSolicitItem AeaSolicitItem) {

        AeaSolicitItem.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaSolicitItem> list = aeaSolicitItemMapper.listAeaSolicitItemRelInfo(AeaSolicitItem);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public void batchSaveSolicitItem(String busType, String solicitType, String[] itemIds){

        String userId = SecurityContext.getCurrentUserId();
        String rootOrgId = SecurityContext.getCurrentOrgId();
        if(itemIds!=null&&itemIds.length>0) {
            AeaSolicitItem sSolicitItem = new AeaSolicitItem();
            sSolicitItem.setRootOrgId(rootOrgId);
            for (String itemId:itemIds) {
                String[] array = itemId.split("\\*");
                sSolicitItem.setItemId(array[0]);
                sSolicitItem.setItemVerId(array[1]);
                sSolicitItem.setBusType(busType);
                List<AeaSolicitItem> itemList = aeaSolicitItemMapper.listAeaSolicitItem(sSolicitItem);
                if(itemList!=null&&itemList.size()>0){
                    for(AeaSolicitItem item:itemList){
                        item.setSolicitType(solicitType);
                        item.setModifier(userId);
                        item.setModifyTime(new Date());
                        aeaSolicitItemMapper.updateAeaSolicitItem(item);
                    }
                }else{
                    sSolicitItem.setSolicitItemId(UUID.randomUUID().toString());
                    sSolicitItem.setSolicitType(solicitType);
                    sSolicitItem.setCreater(userId);
                    sSolicitItem.setCreateTime(new Date());
                    sSolicitItem.setRootOrgId(rootOrgId);
                    aeaSolicitItemMapper.insertAeaSolicitItem(sSolicitItem);
                }
            }
        }
    }

    @Override
    public void batchDelSolicitItemByRootOrgId(String rootOrgId){

        if(StringUtils.isNotBlank(rootOrgId)){
            aeaSolicitItemMapper.batchDelSolicitItemByRootOrgId(rootOrgId);
        }
    }

    @Override
    public List<ZtreeNode> gtreeSolicitItem(String rootOrgId){

        if(StringUtils.isBlank(rootOrgId)){
            rootOrgId = SecurityContext.getCurrentOrgId();
        }
        List<ZtreeNode> allNodes = new ArrayList<>();
        // 添加根节点
        ZtreeNode rootNode = new ZtreeNode();
        rootNode.setId("root");
        rootNode.setName("征求事项");
        rootNode.setType("root");
        rootNode.setOpen(true);
        rootNode.setIsParent(true);
        rootNode.setNocheck(true);
        allNodes.add(rootNode);
        // 征求业务类型
        List<BscDicCodeItem> codeItemList = bscDicCodeMapper.getActiveItemsByTypeCode("SOLICIT_BUS_TYPE", rootOrgId);
        if(codeItemList!=null&&codeItemList.size()>0){
            // 征求事项节点
            AeaSolicitItem sSolicitItem = new AeaSolicitItem();
            sSolicitItem.setRootOrgId(rootOrgId);
            List<AeaSolicitItem> solicitItemList = aeaSolicitItemMapper.listAeaSolicitItemRelInfo(sSolicitItem);
            if(solicitItemList!=null&&solicitItemList.size()>0){
                for (BscDicCodeItem codeItem : codeItemList) {
                    List<AeaSolicitItem> needRemoveList = new ArrayList<>();
                    for (AeaSolicitItem item : solicitItemList) {
                        if (StringUtils.isNotBlank(item.getBusType()) && item.getBusType().equals(codeItem.getItemCode())) {
                            needRemoveList.add(item);
                            ZtreeNode node = convertSolicitItemNode(item, null);
                            node.setpId(codeItem.getItemId());
                            allNodes.add(node);
                        }
                    }
                    if (needRemoveList != null && needRemoveList.size() > 0) {
                        ZtreeNode itemCodeNode = new ZtreeNode();
                        itemCodeNode.setId(codeItem.getItemId());
                        itemCodeNode.setName(codeItem.getItemName());
                        itemCodeNode.setpId("root");
                        itemCodeNode.setType("itemCode");
                        itemCodeNode.setOpen(true);
                        itemCodeNode.setIsParent(true);
                        itemCodeNode.setNocheck(true);
                        allNodes.add(itemCodeNode);
                    }
                    solicitItemList.removeAll(needRemoveList);
                }
            }
        }
        return allNodes;
    }

    private ZtreeNode convertSolicitItemNode(AeaSolicitItem item, List<BscDicCodeItem> codeItemList){

        ZtreeNode node = new ZtreeNode();
        node.setId(item.getSolicitItemId());
        if (StringUtils.isNotBlank(item.getIsCatalog())) {
            // 标准事项
            if (item.getIsCatalog().equals(Status.ON)) {
                if (StringUtils.isNotBlank(item.getGuideOrgName())) {
                    item.setItemName("【标准事项】" + item.getItemName() + "【" + item.getGuideOrgName() + "】");
                }
                // 实施事项
            } else {
                if (StringUtils.isNotBlank(item.getOrgName())) {
                    item.setItemName("【实施事项】" + item.getItemName() + "【" + item.getOrgName() + "】");
                }
            }
        }
        if(codeItemList!=null&&codeItemList.size()>0){
            for(BscDicCodeItem codeItem:codeItemList){
                if(codeItem.getItemCode().equals(item.getBusType())){
                    item.setItemName(item.getItemName() + "【" + codeItem.getItemName() + "】");
                }
            }
        }
        node.setName(item.getItemName());
        if(StringUtils.isNotBlank(item.getSolicitType())){
            if(item.getSolicitType().equals(Status.OFF)){
                node.setName(node.getName()+"【多人征求模式】");
            }else{
                node.setName(node.getName()+"【单人征求模式】");
            }
        }
        node.setpId("root");
        node.setType("item");
        node.setOpen(true);
        node.setIsParent(false);
        node.setNocheck(false);
        return node;
    }

    @Override
    public Long getCountNotRelSelf(AeaSolicitItem aeaSolicitItem){

       return aeaSolicitItemMapper.getCountNotRelSelf(aeaSolicitItem);
    }
}