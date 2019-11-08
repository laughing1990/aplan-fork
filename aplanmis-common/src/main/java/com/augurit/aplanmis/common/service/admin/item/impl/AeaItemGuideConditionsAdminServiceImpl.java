package com.augurit.aplanmis.common.service.admin.item.impl;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.common.domain.AeaItemGuideConditions;
import com.augurit.aplanmis.common.mapper.AeaItemGuideConditionsMapper;
import com.augurit.aplanmis.common.service.admin.item.AeaItemGuideConditionsAdminService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@Service
@Transactional
public class AeaItemGuideConditionsAdminServiceImpl implements AeaItemGuideConditionsAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaItemGuideConditionsAdminServiceImpl.class);

    @Autowired
    private AeaItemGuideConditionsMapper aeaItemGuideConditionsMapper;

    @Override
    public void saveAeaItemGuideConditions(AeaItemGuideConditions aeaItemGuideConditions) {

        aeaItemGuideConditions.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaItemGuideConditionsMapper.insertAeaItemGuideConditions(aeaItemGuideConditions);
    }

    @Override
    public void updateAeaItemGuideConditions(AeaItemGuideConditions aeaItemGuideConditions) {

        aeaItemGuideConditionsMapper.updateAeaItemGuideConditions(aeaItemGuideConditions);
    }

    @Override
    public void deleteAeaItemGuideConditionsById(String id) {

        Assert.notNull(id, "id is null.");
        aeaItemGuideConditionsMapper.deleteAeaItemGuideConditions(id);
    }

    @Override
    public void batchDeleteGuideConditionsByItemVerId(String itemVerId, String rootOrgId){

        aeaItemGuideConditionsMapper.batchDeleteGuideConditionsByItemVerId(itemVerId, rootOrgId);
    }

    @Override
    public PageInfo<AeaItemGuideConditions> listAeaItemGuideConditions(AeaItemGuideConditions aeaItemGuideConditions, Page page) {

        aeaItemGuideConditions.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaItemGuideConditions> list = aeaItemGuideConditionsMapper.listAeaItemGuideConditions(aeaItemGuideConditions);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public AeaItemGuideConditions getAeaItemGuideConditionsById(String id) {

        Assert.notNull(id, "id is null.");
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaItemGuideConditionsMapper.getAeaItemGuideConditionsById(id);
    }

    @Override
    public List<AeaItemGuideConditions> listAeaItemGuideConditions(AeaItemGuideConditions aeaItemGuideConditions) {

        aeaItemGuideConditions.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaItemGuideConditions> list = aeaItemGuideConditionsMapper.listAeaItemGuideConditions(aeaItemGuideConditions);
        logger.debug("成功执行查询list！！");
        return list;
    }
}

