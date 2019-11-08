package com.augurit.aplanmis.common.service.admin.item.impl;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.common.domain.AeaItemGuideMatconditions;
import com.augurit.aplanmis.common.mapper.AeaItemGuideMatconditionsMapper;
import com.augurit.aplanmis.common.service.admin.item.AeaItemGuideMatconditionsAdminService;
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
public class AeaItemGuideMatconditionsAdminServiceImpl implements AeaItemGuideMatconditionsAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaItemGuideMatconditionsAdminServiceImpl.class);

    @Autowired
    private AeaItemGuideMatconditionsMapper aeaItemGuideMatconditionsMapper;

    @Override
    public void saveAeaItemGuideMatconditions(AeaItemGuideMatconditions aeaItemGuideMatconditions) {

        aeaItemGuideMatconditions.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaItemGuideMatconditionsMapper.insertAeaItemGuideMatconditions(aeaItemGuideMatconditions);
    }

    @Override
    public void updateAeaItemGuideMatconditions(AeaItemGuideMatconditions aeaItemGuideMatconditions) {

        aeaItemGuideMatconditionsMapper.updateAeaItemGuideMatconditions(aeaItemGuideMatconditions);
    }

    @Override
    public void deleteAeaItemGuideMatconditionsById(String id) {

        Assert.notNull(id, "id is null.");
        aeaItemGuideMatconditionsMapper.deleteAeaItemGuideMatconditions(id);
    }

    @Override
    public void batchDeleteGuideMatconditionsByItemVerId(String itemVerId, String rootOrgId) {

        aeaItemGuideMatconditionsMapper.batchDeleteGuideMatconditionsByItemVerId(itemVerId, rootOrgId);
    }

    @Override
    public PageInfo<AeaItemGuideMatconditions> listAeaItemGuideMatconditions(AeaItemGuideMatconditions aeaItemGuideMatconditions, Page page) {

        aeaItemGuideMatconditions.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaItemGuideMatconditions> list = aeaItemGuideMatconditionsMapper.listAeaItemGuideMatconditions(aeaItemGuideMatconditions);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public AeaItemGuideMatconditions getAeaItemGuideMatconditionsById(String id) {

        Assert.notNull(id, "id is null.");
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaItemGuideMatconditionsMapper.getAeaItemGuideMatconditionsById(id);
    }

    @Override
    public List<AeaItemGuideMatconditions> listAeaItemGuideMatconditions(AeaItemGuideMatconditions aeaItemGuideMatconditions) {

        aeaItemGuideMatconditions.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaItemGuideMatconditions> list = aeaItemGuideMatconditionsMapper.listAeaItemGuideMatconditions(aeaItemGuideMatconditions);
        logger.debug("成功执行查询list！！");
        return list;
    }
}

