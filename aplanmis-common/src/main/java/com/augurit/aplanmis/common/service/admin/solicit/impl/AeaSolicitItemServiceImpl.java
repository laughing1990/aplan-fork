package com.augurit.aplanmis.common.service.admin.solicit.impl;

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
    public void batchSaveSolicitItem(String[] itemIds){

        String userId = SecurityContext.getCurrentUserId();
        String rootOrgId = SecurityContext.getCurrentOrgId();
        if(itemIds!=null&&itemIds.length>0) {
            // 查找需要删除的
            List<String> needDelIdList = new ArrayList<String>();
            List<AeaSolicitItem> needDelList = new ArrayList<AeaSolicitItem>();
            AeaSolicitItem sSolicitOrg = new AeaSolicitItem();
            sSolicitOrg.setRootOrgId(rootOrgId);
            List<AeaSolicitItem> itemList = aeaSolicitItemMapper.listAeaSolicitItem(sSolicitOrg);
            if(itemList!=null&&itemList.size()>0){
                for(AeaSolicitItem item : itemList){
                    int count=0;
                    for (String itemId : itemIds) {
                        if((item.getItemId()+"*"+item.getItemVerId()).equals(itemId)){
                            break;
                        }else{
                            count++;
                        }
                    }
                    if(count==itemIds.length){
                        needDelList.add(item);
                        needDelIdList.add(item.getSolicitItemId());
                    }
                }
            }
            // 先删除
            if(needDelList!=null&&needDelList.size()>0){

                itemList.removeAll(needDelList);
                String[] needDelIdArr = new String[needDelIdList.size()];
                needDelIdList.toArray(needDelIdArr);
                aeaSolicitItemMapper.batchDelSolicitItemByIds(needDelIdArr);
            }

            // 保存
            for (int i=0; i<itemIds.length;i++) {
                AeaSolicitItem updateVo = null;
                if (itemList != null && itemList.size() > 0) {
                    for (AeaSolicitItem item : itemList) {
                        if((item.getItemId()+"*"+item.getItemVerId()).equals(itemIds[i])){
                            updateVo = item;
                            break;
                        }
                    }
                }
                if(updateVo==null){
                    AeaSolicitItem solicitOrg = new AeaSolicitItem();
                    solicitOrg.setSolicitItemId(UUID.randomUUID().toString());
                    String[] array = itemIds[i].split("\\*");
                    solicitOrg.setItemId(array[0]);
                    solicitOrg.setItemVerId(array[1]);
                    solicitOrg.setBusType("YJZQ");
                    solicitOrg.setSolicitType("0");
                    solicitOrg.setCreater(userId);
                    solicitOrg.setCreateTime(new Date());
                    solicitOrg.setRootOrgId(rootOrgId);
                    aeaSolicitItemMapper.insertAeaSolicitItem(solicitOrg);
                }else{
                    updateVo.setModifier(userId);
                    updateVo.setModifyTime(new Date());
                    aeaSolicitItemMapper.updateAeaSolicitItem(updateVo);
                }
            }
        }else{
            batchDelSolicitItemByRootOrgId(rootOrgId);
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
        ZtreeNode rootNode = new ZtreeNode();
        rootNode.setId("root");
        rootNode.setName("征求事项");
        rootNode.setType("root");
        rootNode.setOpen(true);
        rootNode.setIsParent(true);
        rootNode.setNocheck(true);
        allNodes.add(rootNode);
        AeaSolicitItem sSolicitItem = new AeaSolicitItem();
        sSolicitItem.setRootOrgId(rootOrgId);
        List<AeaSolicitItem> list = aeaSolicitItemMapper.listAeaSolicitItemRelInfo(sSolicitItem);
        if(list!=null&&list.size()>0){
            for(AeaSolicitItem item : list){
                allNodes.add(convertSolicitItemNode(item));
            }
        }
        return allNodes;
    }

    private ZtreeNode convertSolicitItemNode(AeaSolicitItem item){

        ZtreeNode node = new ZtreeNode();
        node.setId(item.getSolicitItemId());
        if (StringUtils.isNotBlank(item.getIsCatalog())) {
            // 标准事项
            if (item.getIsCatalog().equals(Status.ON)) {
                item.setItemName("【标准事项】" + item.getItemName());
                if (StringUtils.isNotBlank(item.getGuideOrgName())) {
                    item.setItemName(item.getItemName() + "【" + item.getGuideOrgName() + "】");
                }
            // 实施事项
            } else {
                item.setItemName("【实施事项】" + item.getItemName());
                if (StringUtils.isNotBlank(item.getOrgName())) {
                    item.setItemName(item.getItemName() + "【" + item.getOrgName() + "】");
                }
            }
        }
        node.setName(item.getItemName());
        node.setpId("root");
        node.setType("item");
        node.setOpen(true);
        node.setIsParent(false);
        node.setNocheck(false);
        return node;
    }
}

