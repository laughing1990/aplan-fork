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
            // 先删除
            batchDelItemByItemVerId(itemVerId);

            // 保存
            if(frontItemVerIds!=null&&frontItemVerIds.length>0) {
                String userId = SecurityContext.getCurrentUserId();
                String rootOrgId = SecurityContext.getCurrentOrgId();
                for (int i=0; i<frontItemVerIds.length;i++) {
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
                }
            }
        }
    }

    @Override
    public void batchDelItemByItemVerId(String itemVerId){

        if(StringUtils.isNotBlank(itemVerId)){
            aeaItemFrontItemMapper.batchDelItemByItemVerId(itemVerId);
        }
    }


    @Override
    public void saveAeaItemFrontItem(AeaItemFrontItem aeaItemFrontItem){
        checkSame(aeaItemFrontItem);

        aeaItemFrontItem.setCreateTime(new Date());
        aeaItemFrontItem.setCreater(SecurityContext.getCurrentUserId());
        aeaItemFrontItem.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaItemFrontItemMapper.insertAeaItemFront(aeaItemFrontItem);
    }

    @Override
    public void updateAeaItemFrontItem(AeaItemFrontItem aeaItemFrontItem){
//        checkSame(aeaItemFrontItem);

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
        PageHelper.startPage(page);
        List<AeaItemFrontItem> list = aeaItemFrontItemMapper.listAeaItemFrontItem(aeaItemFrontItem);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public AeaItemFrontItem getAeaItemFrontItemByFrontItemId(String frontItemId){
        if (StringUtils.isBlank(frontItemId)) {
            throw new InvalidParameterException(frontItemId);
        }
        logger.debug("根据ID获取Form对象，ID为：{}", frontItemId);
        return aeaItemFrontItemMapper.getAeaItemFrontItemByFrontItemId(frontItemId);
    }

    @Override
    public Long getMaxSortNo(AeaItemFrontItem aeaItemFrontItem){
        Long sortNo = aeaItemFrontItemMapper.getMaxSortNo(aeaItemFrontItem);
        if(sortNo==null){
            sortNo = 1l;
        }else{
            sortNo = sortNo + 1;
        }

        return sortNo;
    }

    private void checkSame(AeaItemFrontItem aeaItemFrontItem){
        AeaItemFrontItem queryItemFrontItem = new AeaItemFrontItem();
        queryItemFrontItem.setFrontCkItemVerId(aeaItemFrontItem.getFrontCkItemVerId());
        queryItemFrontItem.setItemVerId(aeaItemFrontItem.getItemVerId());
        List<AeaItemFrontItem> list = aeaItemFrontItemMapper.listAeaItemFront(queryItemFrontItem);
        if(list.size()>0){
            throw new RuntimeException("已有配置相同的前置事项检测!");
        }

    }
}

