package com.augurit.aplanmis.common.service.admin.item.impl;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaItemGuideAccordings;
import com.augurit.aplanmis.common.mapper.AeaItemGuideAccordingsMapper;
import com.augurit.aplanmis.common.service.admin.item.AeaItemGuideAccordingsAdminService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.jsonwebtoken.lang.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AeaItemGuideAccordingsAdminServiceImpl implements AeaItemGuideAccordingsAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaItemGuideAccordingsAdminServiceImpl.class);

    @Autowired
    private AeaItemGuideAccordingsMapper aeaItemGuideAccordingsMapper;

    @Override
    public void saveAeaItemGuideAccordings(AeaItemGuideAccordings aeaItemGuideAccordings) {

        aeaItemGuideAccordings.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaItemGuideAccordingsMapper.insertAeaItemGuideAccordings(aeaItemGuideAccordings);
    }

    @Override
    public void updateAeaItemGuideAccordings(AeaItemGuideAccordings aeaItemGuideAccordings) {

        aeaItemGuideAccordingsMapper.updateAeaItemGuideAccordings(aeaItemGuideAccordings);
    }

    @Override
    public void deleteAeaItemGuideAccordingsById(String id) {

        Assert.notNull(id, "id is null.");
        aeaItemGuideAccordingsMapper.deleteAeaItemGuideAccordings(id);
    }

    @Override
    public void batchDeleteAeaItemGuideAccordingsByItemVerId(String itemVerId, String rootOrgId){

        if(StringUtils.isBlank(rootOrgId)){
            rootOrgId = SecurityContext.getCurrentOrgId();
        }
        if (StringUtils.isNotBlank(itemVerId)) {
            aeaItemGuideAccordingsMapper.batchDeleteGuideAccordingsByItemVerId(itemVerId, rootOrgId);
        }
    }

    @Override
    public PageInfo<AeaItemGuideAccordings> listAeaItemGuideAccordings(AeaItemGuideAccordings aeaItemGuideAccordings, Page page) {

        aeaItemGuideAccordings.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaItemGuideAccordings> list = aeaItemGuideAccordingsMapper.listAeaItemGuideAccordings(aeaItemGuideAccordings);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public AeaItemGuideAccordings getAeaItemGuideAccordingsById(String id) {

        Assert.notNull(id, "id is null.");
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaItemGuideAccordingsMapper.getAeaItemGuideAccordingsById(id);
    }

    @Override
    public List<AeaItemGuideAccordings> listAeaItemGuideAccordings(AeaItemGuideAccordings aeaItemGuideAccordings) {

        aeaItemGuideAccordings.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaItemGuideAccordings> list = aeaItemGuideAccordingsMapper.listAeaItemGuideAccordings(aeaItemGuideAccordings);
        logger.debug("成功执行查询list！！");
        return list;
    }
}

