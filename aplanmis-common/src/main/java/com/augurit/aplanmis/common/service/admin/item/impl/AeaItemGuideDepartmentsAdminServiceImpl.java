package com.augurit.aplanmis.common.service.admin.item.impl;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.common.domain.AeaItemGuideDepartments;
import com.augurit.aplanmis.common.mapper.AeaItemGuideDepartmentsMapper;
import com.augurit.aplanmis.common.service.admin.item.AeaItemGuideDepartmentsAdminService;
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
public class AeaItemGuideDepartmentsAdminServiceImpl implements AeaItemGuideDepartmentsAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaItemGuideDepartmentsAdminServiceImpl.class);

    @Autowired
    private AeaItemGuideDepartmentsMapper aeaItemGuideDepartmentsMapper;

    @Override
    public void saveAeaItemGuideDepartments(AeaItemGuideDepartments aeaItemGuideDepartments) {

        aeaItemGuideDepartments.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaItemGuideDepartmentsMapper.insertAeaItemGuideDepartments(aeaItemGuideDepartments);
    }

    @Override
    public void updateAeaItemGuideDepartments(AeaItemGuideDepartments aeaItemGuideDepartments) {

        aeaItemGuideDepartmentsMapper.updateAeaItemGuideDepartments(aeaItemGuideDepartments);
    }

    @Override
    public void deleteAeaItemGuideDepartmentsById(String id) {

        Assert.notNull(id, "id is null.");
        aeaItemGuideDepartmentsMapper.deleteAeaItemGuideDepartments(id);
    }

    @Override
    public void batchDeleteGuideDepartmentsByItemVerId(String itemVerId, String rootOrgId){

        aeaItemGuideDepartmentsMapper.batchDeleteGuideDepartmentsByItemVerId(itemVerId, rootOrgId);
    }

    @Override
    public PageInfo<AeaItemGuideDepartments> listAeaItemGuideDepartments(AeaItemGuideDepartments aeaItemGuideDepartments, Page page) {

        aeaItemGuideDepartments.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaItemGuideDepartments> list = aeaItemGuideDepartmentsMapper.listAeaItemGuideDepartments(aeaItemGuideDepartments);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public AeaItemGuideDepartments getAeaItemGuideDepartmentsById(String id) {

        Assert.notNull(id, "id is null.");
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaItemGuideDepartmentsMapper.getAeaItemGuideDepartmentsById(id);
    }

    @Override
    public List<AeaItemGuideDepartments> listAeaItemGuideDepartments(AeaItemGuideDepartments aeaItemGuideDepartments) {

        aeaItemGuideDepartments.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaItemGuideDepartments> list = aeaItemGuideDepartmentsMapper.listAeaItemGuideDepartments(aeaItemGuideDepartments);
        logger.debug("成功执行查询list！！");
        return list;
    }
}

