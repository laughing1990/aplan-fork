package com.augurit.aplanmis.common.service.admin.item.impl;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaItemOneform;
import com.augurit.aplanmis.common.domain.AeaOneform;
import com.augurit.aplanmis.common.mapper.AeaItemOneformMapper;
import com.augurit.aplanmis.common.mapper.AeaOneformMapper;
import com.augurit.aplanmis.common.service.admin.item.AeaItemOneformAdminService;
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
 * 事项与总表单关联表-Service服务接口实现类
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AeaItemOneformAdminServiceImpl implements AeaItemOneformAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaItemOneformAdminServiceImpl.class);

    @Autowired
    private AeaItemOneformMapper aeaItemOneformMapper;

    @Autowired
    private AeaOneformMapper aeaOneformMapper;

    @Override
    public Double getMaxSortNo(String itemVerId){

        Double sortNo = aeaItemOneformMapper.getMaxSortNo(itemVerId);
        return sortNo==null?1:sortNo+1;
    }

    @Override
    public void addMultiplyItemOneform(String itemVerId, String[] oneformIds) {

        AeaItemOneform aeaItemOneform;
        for (String id:oneformIds){
            if(StringUtils.isNotBlank(id)){
                aeaItemOneform = new AeaItemOneform();
                aeaItemOneform.setItemOneformId(UUID.randomUUID().toString());
                aeaItemOneform.setItemVerId(itemVerId);
                aeaItemOneform.setSortNo(getMaxSortNo(itemVerId));
                aeaItemOneform.setOneformId(id);
                aeaItemOneform.setIsActive(Status.OFF);
                aeaItemOneform.setCreater(SecurityContext.getCurrentUserId());
                aeaItemOneform.setCreateTime(new Date());
                aeaItemOneformMapper.insertAeaItemOneform(aeaItemOneform);
            }
        }
    }



    @Override
    public PageInfo<AeaItemOneform> listAeaItemOneFormByItemVerId(String itemVerId, Page page) {

        PageHelper.startPage(page);
        List<AeaItemOneform> data = aeaItemOneformMapper.listAeaItemOneFormByItemVerId(itemVerId);
        return new PageInfo<>(data);
    }

    @Override
    public List<AeaItemOneform> listAeaItemOneFormByItemVerId(String itemVerId){

        List<AeaItemOneform> list = aeaItemOneformMapper.listAeaItemOneFormByItemVerId(itemVerId);
        return list;
    }

    @Override
    public PageInfo<AeaOneform> listAeaOneFormNotInItem(AeaOneform aeaOneform, Page page) {

        PageHelper.startPage(page);
        List<AeaOneform> data = aeaOneformMapper.listAeaOneformNotInItem(aeaOneform);
        return new PageInfo<>(data);
    }

    @Override
    public void saveAeaItemOneform(AeaItemOneform aeaItemOneform) {
        aeaItemOneform.setCreater(SecurityContext.getCurrentUserId());
        aeaItemOneform.setCreateTime(new Date());
        aeaItemOneformMapper.insertAeaItemOneform(aeaItemOneform);
    }

    @Override
    public void updateAeaItemOneform(AeaItemOneform aeaItemOneform) {
        aeaItemOneform.setModifier(SecurityContext.getCurrentUserId());
        aeaItemOneform.setModifyTime(new Date());
        aeaItemOneformMapper.updateAeaItemOneform(aeaItemOneform);
    }

    @Override
    public void deleteAeaItemOneformById(String id) {

        if (id == null) {
            throw new InvalidParameterException(id);
        }
        aeaItemOneformMapper.deleteAeaItemOneform(id);
    }

    @Override
    public PageInfo<AeaItemOneform> listAeaItemOneform(AeaItemOneform aeaItemOneform, Page page) {

        PageHelper.startPage(page);
        List<AeaItemOneform> list = aeaItemOneformMapper.listAeaItemOneform(aeaItemOneform);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaItemOneform>(list);
    }

    @Override
    public AeaItemOneform getAeaItemOneformById(String id) {

        if (id == null) {
            throw new InvalidParameterException(id);
        }
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaItemOneformMapper.getAeaItemOneformById(id);
    }

    @Override
    public List<AeaItemOneform> listAeaItemOneform(AeaItemOneform aeaItemOneform) {

        List<AeaItemOneform> list = aeaItemOneformMapper.listAeaItemOneform(aeaItemOneform);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public void changIsActiveState(String id){

        if (StringUtils.isBlank(id)) {
            throw new InvalidParameterException("参数id为空!");
        }
        aeaItemOneformMapper.changIsActiveState(id);
    }
}

