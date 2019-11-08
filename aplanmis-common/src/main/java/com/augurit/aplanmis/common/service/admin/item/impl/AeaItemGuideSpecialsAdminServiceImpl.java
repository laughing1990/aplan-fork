package com.augurit.aplanmis.common.service.admin.item.impl;

import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaItemGuideSpecials;
import com.augurit.aplanmis.common.mapper.AeaItemGuideSpecialsMapper;
import com.augurit.aplanmis.common.service.admin.item.AeaItemGuideSpecialsAdminService;
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
public class AeaItemGuideSpecialsAdminServiceImpl implements AeaItemGuideSpecialsAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaItemGuideSpecialsAdminServiceImpl.class);

    @Autowired
    private AeaItemGuideSpecialsMapper aeaItemGuideSpecialsMapper;

    @Override
    public void saveAeaItemGuideSpecials(AeaItemGuideSpecials aeaItemGuideSpecials) {
        aeaItemGuideSpecialsMapper.insertAeaItemGuideSpecials(aeaItemGuideSpecials);
    }

    @Override
    public void updateAeaItemGuideSpecials(AeaItemGuideSpecials aeaItemGuideSpecials) {
        aeaItemGuideSpecialsMapper.updateAeaItemGuideSpecials(aeaItemGuideSpecials);
    }

    @Override
    public void deleteAeaItemGuideSpecialsById(String id) {
        Assert.notNull(id, "id is null.");
        aeaItemGuideSpecialsMapper.deleteAeaItemGuideSpecials(id);
    }

    @Override
    public void batchDeleteGuideSpecialsByItemVerId(String itemVerId, String rootOrgId) {

        if (StringUtils.isNotBlank(itemVerId)) {
            aeaItemGuideSpecialsMapper.batchDeleteGuideSpecialsByItemVerId(itemVerId, rootOrgId);
        }
    }

    @Override
    public PageInfo<AeaItemGuideSpecials> listAeaItemGuideSpecials(AeaItemGuideSpecials aeaItemGuideSpecials, Page page) {
        PageHelper.startPage(page);
        List<AeaItemGuideSpecials> list = aeaItemGuideSpecialsMapper.listAeaItemGuideSpecials(aeaItemGuideSpecials);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public AeaItemGuideSpecials getAeaItemGuideSpecialsById(String id) {
        Assert.notNull(id, "id is null.");
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaItemGuideSpecialsMapper.getAeaItemGuideSpecialsById(id);
    }

    @Override
    public List<AeaItemGuideSpecials> listAeaItemGuideSpecials(AeaItemGuideSpecials aeaItemGuideSpecials) {
        List<AeaItemGuideSpecials> list = aeaItemGuideSpecialsMapper.listAeaItemGuideSpecials(aeaItemGuideSpecials);
        logger.debug("成功执行查询list！！");
        return list;
    }
}

