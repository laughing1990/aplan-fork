package com.augurit.aplanmis.common.service.admin.item.impl;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaItemFrontItem;
import com.augurit.aplanmis.common.mapper.AeaItemFrontItemMapper;
import com.augurit.aplanmis.common.service.admin.item.AeaItemFrontItemAdminService;
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
* 事项的前置检查事项-Service服务接口实现类
*/
@Service
@Transactional
public class AeaItemFrontItemAdminServiceImpl implements AeaItemFrontItemAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaItemFrontItemAdminServiceImpl.class);

    @Autowired
    private AeaItemFrontItemMapper aeaItemFrontItemMapper;

    @Override
    public List<AeaItemFrontItem> listItemsByItemVerId(String itemVerId){

        String rootOrgId = SecurityContext.getCurrentOrgId();
        List<AeaItemFrontItem> list = aeaItemFrontItemMapper.listItemsByItemVerId(itemVerId, rootOrgId);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public void batchSaveFrontItem(String itemVerId, String[] frontItemVerIds, String[] sortNos){

        if (StringUtils.isNotBlank(itemVerId)) {
            String userId = SecurityContext.getCurrentUserId();
            String rootOrgId = SecurityContext.getCurrentOrgId();
            if(frontItemVerIds!=null&&frontItemVerIds.length>0) {
                // 查找需要删除的
                List<String> needDelIdList = new ArrayList<String>();
                List<AeaItemFrontItem> needDelList = new ArrayList<AeaItemFrontItem>();
                AeaItemFrontItem sfrontItem = new AeaItemFrontItem();
                sfrontItem.setItemVerId(itemVerId);
                sfrontItem.setRootOrgId(rootOrgId);
                List<AeaItemFrontItem> frontItemList = aeaItemFrontItemMapper.listAeaItemFront(sfrontItem);
                if(frontItemList!=null&&frontItemList.size()>0){
                    for(AeaItemFrontItem item : frontItemList){
                        int count=0;
                        for (String frontItemVerId : frontItemVerIds) {
                            if(item.getFrontCkItemVerId().equals(frontItemVerId)){
                                break;
                            }else{
                                count++;
                            }
                        }
                        if(count==frontItemVerIds.length){
                            needDelList.add(item);
                            needDelIdList.add(item.getFrontItemId());
                        }
                    }
                }
                // 先删除
                if(needDelList!=null&&needDelList.size()>0){

                    frontItemList.removeAll(needDelList);
                    aeaItemFrontItemMapper.batchDelAeaItemFront(needDelIdList);
                }

                // 保存
                for (int i=0; i<frontItemVerIds.length;i++) {
                    AeaItemFrontItem updateVo = null;
                    if (frontItemList != null && frontItemList.size() > 0) {
                        for (AeaItemFrontItem item : frontItemList) {
                            if(item.getFrontCkItemVerId().equals(frontItemVerIds[i])){
                                updateVo = item;
                                break;
                            }
                        }
                    }
                    if(updateVo==null){
                        AeaItemFrontItem aeaItemFront = new AeaItemFrontItem();
                        aeaItemFront.setFrontItemId(UUID.randomUUID().toString());
                        aeaItemFront.setItemVerId(itemVerId);
                        aeaItemFront.setFrontCkItemVerId(frontItemVerIds[i]);
                        aeaItemFront.setSortNo(new Long(sortNos[i]));
                        aeaItemFront.setIsActive(Status.ON);
                        aeaItemFront.setCreater(userId);
                        aeaItemFront.setCreateTime(new Date());
                        aeaItemFront.setRootOrgId(rootOrgId);
                        aeaItemFrontItemMapper.insertAeaItemFront(aeaItemFront);
                    }else{
                        updateVo.setModifier(userId);
                        updateVo.setModifyTime(new Date());
                        updateVo.setSortNo(new Long(sortNos[i]));
                        aeaItemFrontItemMapper.updateAeaItemFront(updateVo);
                    }
                }
            }else{
                aeaItemFrontItemMapper.batchDelItemByItemVerId(itemVerId, rootOrgId);
            }
        }
    }

    @Override
    public void batchDelItemByItemVerId(String itemVerId, String rootOrgId){

        if(StringUtils.isNotBlank(itemVerId)){
            aeaItemFrontItemMapper.batchDelItemByItemVerId(itemVerId, rootOrgId);
        }
    }


    @Override
    public void saveAeaItemFrontItem(AeaItemFrontItem aeaItemFrontItem){

        String rootOrgId = SecurityContext.getCurrentOrgId();
        // 先删除
        aeaItemFrontItemMapper.batchDelItemByItemVerId(aeaItemFrontItem.getItemVerId(), rootOrgId);
        // 后创建
        aeaItemFrontItem.setCreateTime(new Date());
        aeaItemFrontItem.setCreater(SecurityContext.getCurrentUserId());
        aeaItemFrontItem.setRootOrgId(rootOrgId);
        aeaItemFrontItemMapper.insertAeaItemFront(aeaItemFrontItem);
    }

    @Override
    public void updateAeaItemFrontItem(AeaItemFrontItem aeaItemFrontItem){

        aeaItemFrontItem.setModifyTime(new Date());
        aeaItemFrontItem.setModifier(SecurityContext.getCurrentUserId());
        aeaItemFrontItemMapper.updateAeaItemFront(aeaItemFrontItem);
    }

    @Override
    public void deleteAeaItemFrontItemById(String id){

        if (StringUtils.isBlank(id)) {
            throw new InvalidParameterException(id);
        }
        String[] ids = id.split(",");
        for(String frontItemId :ids) {
            aeaItemFrontItemMapper.deleteAeaItemFront(frontItemId);
        }
    }

    @Override
    public PageInfo<AeaItemFrontItem> listAeaItemFrontItemByPage(AeaItemFrontItem aeaItemFrontItem, Page page){

        aeaItemFrontItem.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaItemFrontItem> list = aeaItemFrontItemMapper.listAeaItemFrontItem(aeaItemFrontItem);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public List<AeaItemFrontItem> listAeaItemFrontItem(AeaItemFrontItem aeaItemFrontItem){

        aeaItemFrontItem.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaItemFrontItem> list = aeaItemFrontItemMapper.listAeaItemFrontItem(aeaItemFrontItem);
        logger.debug("成功执行分页查询！！");
        return list;
    }

    @Override
    public AeaItemFrontItem getAeaItemFrontItemByFrontItemId(String frontItemId){

        if (StringUtils.isBlank(frontItemId)) {
            throw new InvalidParameterException(frontItemId);
        }
        logger.debug("根据ID获取Form对象，ID为：{}", frontItemId);
        AeaItemFrontItem frontItem = aeaItemFrontItemMapper.getAeaItemFrontItemByFrontItemId(frontItemId);
        return frontItem;
    }

    @Override
    public Long getMaxSortNo(AeaItemFrontItem aeaItemFrontItem){

        Long sortNo = aeaItemFrontItemMapper.getMaxSortNo(aeaItemFrontItem);
        return sortNo==null?1L:(sortNo+1L);
    }

    private void checkSame(AeaItemFrontItem aeaItemFrontItem){

        AeaItemFrontItem queryItemFrontItem = new AeaItemFrontItem();
        queryItemFrontItem.setFrontCkItemVerId(aeaItemFrontItem.getFrontCkItemVerId());
        queryItemFrontItem.setItemVerId(aeaItemFrontItem.getItemVerId());
        List<AeaItemFrontItem> list = aeaItemFrontItemMapper.listAeaItemFront(queryItemFrontItem);
        if(list.size()>0) {
            throw new RuntimeException("已有配置相同的前置事项检测!");
        }
    }

    @Override
    public void changIsActive(String id, String rootOrgId){

        aeaItemFrontItemMapper.changIsActive(id, rootOrgId);
    }

    @Override
    public void batchSaveAeaItemFrontItem(String itemVerId,String frontCkItemVerIds)throws Exception{
        if(StringUtils.isBlank(itemVerId)  || StringUtils.isBlank(frontCkItemVerIds)){
            throw new InvalidParameterException(itemVerId,frontCkItemVerIds);
        }

        String[] frontCkItemVerIdArr = frontCkItemVerIds.split(",");
        if(frontCkItemVerIdArr!=null && frontCkItemVerIdArr.length>0){
            AeaItemFrontItem query = new AeaItemFrontItem();
            query.setItemVerId(itemVerId);
            Long maxSortNo = getMaxSortNo(query);
            for(String frontCkItemVerId:frontCkItemVerIdArr){
                AeaItemFrontItem aeaItemFrontItem = new AeaItemFrontItem();
                aeaItemFrontItem.setItemVerId(itemVerId);
                aeaItemFrontItem.setFrontCkItemVerId(frontCkItemVerId);
                List<AeaItemFrontItem> list = aeaItemFrontItemMapper.listAeaItemFront(aeaItemFrontItem);
                if (list.size() > 0) {
                    continue;
                }
                aeaItemFrontItem.setFrontItemId(UUID.randomUUID().toString());
                aeaItemFrontItem.setCreateTime(new Date());
                aeaItemFrontItem.setCreater(SecurityContext.getCurrentUserId());
                aeaItemFrontItem.setRootOrgId(SecurityContext.getCurrentOrgId());
                aeaItemFrontItem.setSortNo(maxSortNo);
                aeaItemFrontItem.setIsActive("1");
                aeaItemFrontItemMapper.insertAeaItemFront(aeaItemFrontItem);
                maxSortNo++;
            }
        }
    }
}

