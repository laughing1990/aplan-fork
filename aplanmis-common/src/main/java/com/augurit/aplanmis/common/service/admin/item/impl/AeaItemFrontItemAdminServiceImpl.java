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
    private AeaItemFrontItemMapper aeaItemFrontMapper;

    @Override
    public void saveAeaItemFront(AeaItemFrontItem aeaItemFront) {

        aeaItemFront.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaItemFront.setIsActive(Status.ON);
        aeaItemFront.setCreater(SecurityContext.getCurrentUserId());
        aeaItemFront.setCreateTime(new Date());
        aeaItemFrontMapper.insertAeaItemFront(aeaItemFront);
    }

    @Override
    public void updateAeaItemFront(AeaItemFrontItem aeaItemFront) {

        aeaItemFrontMapper.updateAeaItemFront(aeaItemFront);
    }

    @Override
    public void deleteAeaItemFrontById(String id) {

        if(StringUtils.isBlank(id)){
            throw new InvalidParameterException("参数id为空!");
        }
        aeaItemFrontMapper.deleteAeaItemFront(id);
    }

    @Override
    public PageInfo<AeaItemFrontItem> listAeaItemFront(AeaItemFrontItem aeaItemFront,Page page) {

        aeaItemFront.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaItemFrontItem> list = aeaItemFrontMapper.listAeaItemFront(aeaItemFront);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaItemFrontItem>(list);
    }

    @Override
    public AeaItemFrontItem getAeaItemFrontById(String id) {

        if(StringUtils.isBlank(id)){
            throw new InvalidParameterException("参数id为空!");
        }
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaItemFrontMapper.getAeaItemFrontById(id);
    }

    @Override
    public List<AeaItemFrontItem> listAeaItemFront(AeaItemFrontItem aeaItemFront) {

        aeaItemFront.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaItemFrontItem> list = aeaItemFrontMapper.listAeaItemFront(aeaItemFront);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public List<AeaItemFrontItem> listItemsByItemVerId(String itemVerId){

        String rootOrgId = SecurityContext.getCurrentOrgId();
        List<AeaItemFrontItem> list = aeaItemFrontMapper.listItemsByItemVerId(itemVerId, rootOrgId);
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
                    aeaItemFrontMapper.insertAeaItemFront(aeaItemFront);
                }
            }
        }
    }

    @Override
    public void batchDelItemByItemVerId(String itemVerId){

        if(StringUtils.isNotBlank(itemVerId)){
            aeaItemFrontMapper.batchDelItemByItemVerId(itemVerId);
        }
    }
}

