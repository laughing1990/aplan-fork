package com.augurit.aplanmis.common.service.admin.item.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaItemGuideCharges;
import com.augurit.aplanmis.common.mapper.AeaItemGuideChargesMapper;
import com.augurit.aplanmis.common.service.admin.item.AeaItemGuideChargesAdminService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* 收费项目信息-Service服务接口实现类
 *
 * @author jjt
 * @date 2019/05/07
*/
@Service
@Transactional
public class AeaItemGuideChargesServiceAdminImpl implements AeaItemGuideChargesAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaItemGuideChargesServiceAdminImpl.class);

    @Autowired
    private AeaItemGuideChargesMapper aeaItemGuideChargesMapper;

    @Override
    public void saveAeaItemGuideCharges(AeaItemGuideCharges charges) {

        String rootOrgId = SecurityContext.getCurrentOrgId();
        charges.setOrdernum(getMaxSortNoByItemVerId(charges.getItemVerId(), rootOrgId));
        charges.setRootOrgId(rootOrgId);
        aeaItemGuideChargesMapper.insertAeaItemGuideCharges(charges);
    }

    @Override
    public void updateAeaItemGuideCharges(AeaItemGuideCharges aeaItemGuideCharges) {

        aeaItemGuideChargesMapper.updateAeaItemGuideCharges(aeaItemGuideCharges);
    }

    @Override
    public void deleteAeaItemGuideChargesById(String id) {

        if(StringUtils.isBlank(id)) {
            throw new InvalidParameterException(id);
        }
        aeaItemGuideChargesMapper.deleteAeaItemGuideChargesById(id);
    }

    @Override
    public PageInfo<AeaItemGuideCharges> listAeaItemGuideCharges(AeaItemGuideCharges aeaItemGuideCharges, Page page) {

        aeaItemGuideCharges.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaItemGuideCharges> list = aeaItemGuideChargesMapper.listAeaItemGuideCharges(aeaItemGuideCharges);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaItemGuideCharges>(list);
    }

    @Override
    public AeaItemGuideCharges getAeaItemGuideChargesById(String id) {

        if(StringUtils.isBlank(id)) {
            throw new InvalidParameterException(id);
        }
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaItemGuideChargesMapper.getAeaItemGuideChargesById(id);
    }

    @Override
    public List<AeaItemGuideCharges> listAeaItemGuideCharges(AeaItemGuideCharges aeaItemGuideCharges) {

        aeaItemGuideCharges.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaItemGuideCharges> list = aeaItemGuideChargesMapper.listAeaItemGuideCharges(aeaItemGuideCharges);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public void batchDeleteGuideChargesByItemVerId(String itemVerId, String rootOrgId){

        aeaItemGuideChargesMapper.batchDelGuideChargesByItemVerId(itemVerId, rootOrgId);
    }

    @Override
    public void batchDelAeaItemGuideChargesByIds(String[] ids){

        aeaItemGuideChargesMapper.batchDelAeaItemGuideChargesByIds(ids);
    }

    @Override
    public Long getMaxSortNoByItemVerId(String itemVerId, String rootOrgId){

        Long num = aeaItemGuideChargesMapper.getMaxSortNoByItemVerId(itemVerId, rootOrgId);
        return num==null?1:(num+1);
    }
}

