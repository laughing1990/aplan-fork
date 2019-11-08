
package com.augurit.aplanmis.common.service.admin.item.impl;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.common.domain.AeaItemGuideMaterials;
import com.augurit.aplanmis.common.mapper.AeaItemGuideMaterialsMapper;
import com.augurit.aplanmis.common.service.admin.item.AeaItemGuideMaterialsAdminService;
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
public class AeaItemGuideMaterialsAdminServiceImpl implements AeaItemGuideMaterialsAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaItemGuideMaterialsAdminServiceImpl.class);

    @Autowired
    private AeaItemGuideMaterialsMapper aeaItemGuideMaterialsMapper;

    @Override
    public void saveAeaItemGuideMaterials(AeaItemGuideMaterials aeaItemGuideMaterials) {

        aeaItemGuideMaterialsMapper.insertAeaItemGuideMaterials(aeaItemGuideMaterials);
    }

    @Override
    public void updateAeaItemGuideMaterials(AeaItemGuideMaterials aeaItemGuideMaterials) {

        aeaItemGuideMaterialsMapper.updateAeaItemGuideMaterials(aeaItemGuideMaterials);
    }

    @Override
    public void deleteAeaItemGuideMaterialsById(String id) {

        Assert.notNull(id, "id is null.");
        aeaItemGuideMaterialsMapper.deleteAeaItemGuideMaterials(id);
    }

    @Override
    public void batchDeleteGuideMaterialsByItemVerId(String itemVerId, String rootOrgId) {

        aeaItemGuideMaterialsMapper.batchDeleteGuideMaterialsByItemVerId(itemVerId, rootOrgId);
    }

    @Override
    public PageInfo<AeaItemGuideMaterials> listAeaItemGuideMaterials(AeaItemGuideMaterials aeaItemGuideMaterials, Page page) {

        aeaItemGuideMaterials.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaItemGuideMaterials> list = aeaItemGuideMaterialsMapper.listAeaItemGuideMaterials(aeaItemGuideMaterials);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public AeaItemGuideMaterials getAeaItemGuideMaterialsById(String id) {

        Assert.notNull(id, "id is null.");
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaItemGuideMaterialsMapper.getAeaItemGuideMaterialsById(id);
    }

    @Override
    public List<AeaItemGuideMaterials> listAeaItemGuideMaterials(AeaItemGuideMaterials aeaItemGuideMaterials) {

        aeaItemGuideMaterials.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaItemGuideMaterials> list = aeaItemGuideMaterialsMapper.listAeaItemGuideMaterials(aeaItemGuideMaterials);
        logger.debug("成功执行查询list！！");
        return list;
    }
}


