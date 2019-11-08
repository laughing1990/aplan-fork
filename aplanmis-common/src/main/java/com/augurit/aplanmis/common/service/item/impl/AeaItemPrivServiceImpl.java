package com.augurit.aplanmis.common.service.item.impl;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.service.om.OpuOmOrgService;
import com.augurit.aplanmis.common.constants.ActiveStatus;
import com.augurit.aplanmis.common.constants.AeaItemPrivConstants;
import com.augurit.aplanmis.common.domain.AeaItemPriv;
import com.augurit.aplanmis.common.domain.AeaServiceWindow;
import com.augurit.aplanmis.common.mapper.AeaItemPrivMapper;
import com.augurit.aplanmis.common.service.item.AeaItemPrivService;
import com.augurit.aplanmis.common.service.window.AeaServiceWindowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author yinlf
 * @Date 2019/8/1
 */
@Service
@Transactional
public class AeaItemPrivServiceImpl implements AeaItemPrivService {

    private static Logger LOGGER = LoggerFactory.getLogger(AeaItemPrivServiceImpl.class);

    @Autowired
    AeaServiceWindowService aeaServiceWindowService;

    @Autowired
    AeaItemPrivMapper aeaItemPrivMapper;

    @Autowired
    OpuOmOrgService opuOmOrgService;

    @Override
    public void insertAeaItemPriv(AeaItemPriv aeaItemPriv) {
        LOGGER.debug("新增事项承办");
        aeaItemPriv.setPrivId(UUID.randomUUID().toString());
        aeaItemPriv.setCreater(SecurityContext.getCurrentUserId());
        aeaItemPriv.setCreateTime(new Date());
        aeaItemPrivMapper.insertAeaItemPriv(aeaItemPriv);
    }

    @Override
    public void insertAeaItemPriv(String itemVerId, String regionId, String orgId, String allowManual, String elExpr) {
        LOGGER.debug("新增事项承办");
        AeaItemPriv aeaItemPriv = new AeaItemPriv();
        aeaItemPriv.setPrivId(UUID.randomUUID().toString());
        aeaItemPriv.setItemVerId(itemVerId);
        aeaItemPriv.setRegionId(regionId);
        aeaItemPriv.setOrgId(orgId);
        OpuOmOrg topOrg= opuOmOrgService.getTopOrgByCurOrgId(orgId);
        if(topOrg!=null){
            aeaItemPriv.setRootOrgId(topOrg.getOrgId());
        }
        aeaItemPriv.setAllowManual(allowManual);
        if(StringUtils.isNotBlank(elExpr)){
            aeaItemPriv.setUseEl(AeaItemPrivConstants.USE_EL_TRUE);
            aeaItemPriv.setElExpr(elExpr);
        }else{
            aeaItemPriv.setUseEl(AeaItemPrivConstants.USE_EL_FALSE);
        }
        aeaItemPriv.setIsActive(ActiveStatus.ACTIVE.getValue());
        aeaItemPriv.setCreater(SecurityContext.getCurrentUserId());
        aeaItemPriv.setCreateTime(new Date());
        aeaItemPrivMapper.insertAeaItemPriv(aeaItemPriv);
    }

    @Override
    public void deleteAeaItemPriv(String regionId, String orgId, String itemVerId) {
        LOGGER.debug("删除事项承办");
        aeaItemPrivMapper.deleteAeaItemPrivByItemVerIdAndRegionIdAndOrgId(regionId,orgId,itemVerId);
    }

    @Override
    public List<AeaItemPriv> findCurrentUserItemPriv(String... itemVerId) {
        LOGGER.debug("获取当前用户事项的承办情况");
        //获取当前登录用户所在窗口的行政区划
        AeaServiceWindow currentUserWindow = aeaServiceWindowService.getCurrentUserWindow();
        if(currentUserWindow!=null){
            String regionId = currentUserWindow.getRegionId();
            //根据事项版本ID和行政区划获取办理组织
            List<AeaItemPriv> aeaItemPrivList = this.findItemPrivByRegionIdAndItemVerId(regionId,itemVerId);
            return aeaItemPrivList;
        }
        return null;
    }

    @Override
    public List<AeaItemPriv> findItemPrivByRegionIdAndItemVerId(String regionId,String... itemVerId) {
        LOGGER.debug("某个区域事项的承办情况");
        return aeaItemPrivMapper.findItemPrivByRegionIdAndItemVerId(regionId,itemVerId);
    }
}
