package com.augurit.aplanmis.common.service.admin.item.impl;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.common.domain.AeaItemGuideAttr;
import com.augurit.aplanmis.common.mapper.AeaItemGuideAttrMapper;
import com.augurit.aplanmis.common.service.admin.item.AeaItemGuideAttrAdminService;
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
public class AeaItemGuideAttrAdminServiceImpl implements AeaItemGuideAttrAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaItemGuideAttrAdminServiceImpl.class);

    @Autowired
    private AeaItemGuideAttrMapper aeaItemGuideAttrMapper;

    @Override
    public void saveAeaItemGuideAttr(AeaItemGuideAttr aeaItemGuideAttr) {

        aeaItemGuideAttr.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaItemGuideAttrMapper.insertAeaItemGuideAttr(aeaItemGuideAttr);
    }

    @Override
    public void updateAeaItemGuideAttr(AeaItemGuideAttr aeaItemGuideAttr) {

        aeaItemGuideAttrMapper.updateAeaItemGuideAttr(aeaItemGuideAttr);
    }

    @Override
    public void deleteAeaItemGuideAttrById(String id) {

        Assert.notNull(id, "id is null.");
        aeaItemGuideAttrMapper.deleteAeaItemGuideAttr(id);
    }

    @Override
    public PageInfo<AeaItemGuideAttr> listAeaItemGuideAttr(AeaItemGuideAttr aeaItemGuideAttr, Page page) {

        aeaItemGuideAttr.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaItemGuideAttr> list = aeaItemGuideAttrMapper.listAeaItemGuideAttr(aeaItemGuideAttr);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public AeaItemGuideAttr getAeaItemGuideAttrById(String id) {

        Assert.notNull(id, "id is null.");
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaItemGuideAttrMapper.getAeaItemGuideAttrById(id);
    }

    @Override
    public void batchDeleteItemGuideAttr(AeaItemGuideAttr aeaItemGuideAttr) {

        aeaItemGuideAttr.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaItemGuideAttrMapper.batchDeleteItemGuideAttr(aeaItemGuideAttr);
    }
}

