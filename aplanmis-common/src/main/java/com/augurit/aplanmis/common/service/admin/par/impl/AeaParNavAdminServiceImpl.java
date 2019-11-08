package com.augurit.aplanmis.common.service.admin.par.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.domain.AeaParNav;
import com.augurit.aplanmis.common.mapper.AeaParNavMapper;
import com.augurit.aplanmis.common.service.admin.par.AeaParFactorAdminService;
import com.augurit.aplanmis.common.service.admin.par.AeaParNavAdminService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author ZhangXinhui
 * @date 2019/8/29 029 19:28
 * @desc
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class AeaParNavAdminServiceImpl implements AeaParNavAdminService {

    private static final Logger log = LoggerFactory.getLogger(AeaParNavAdminServiceImpl.class);

    @Autowired
    private AeaParNavMapper aeaParNavMapper;

    @Autowired
    private AeaParFactorAdminService aeaParFactorService;

    @Override
    public PageInfo<AeaParNav> listAeaParNavPage(AeaParNav aeaParNav, Page page) {

        PageHelper.startPage(page);
        List<AeaParNav> list = aeaParNavMapper.listAeaParNav(aeaParNav);
        return new PageInfo<AeaParNav>(list);

    }

    @Override
    public List<AeaParNav> listAeaParNavPage(AeaParNav aeaParNav) {

        return aeaParNavMapper.listAeaParNav(aeaParNav);
    }

    @Override
    public Long getMaxSortNo(String rootOrgId) {

        Long sortNo =  aeaParNavMapper.getMaxSortNo(rootOrgId);
        return sortNo==null?1L:(sortNo+1L);
    }

    @Override
    public AeaParNav getAeaParNavById(String navId, String rootOrgId) {

        return aeaParNavMapper.getAeaParNavById(navId, rootOrgId);
    }

    @Override
    public void insertAeaParNav(AeaParNav aeaParNav) {

        aeaParNav.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaParNav.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
        aeaParNav.setCreater(SecurityContext.getCurrentUserId());
        aeaParNav.setCreateTime(new Date());
        aeaParNavMapper.insertAeaParNav(aeaParNav);
    }

    @Override
    public void updateAeaParNav(AeaParNav aeaParNav) {

        aeaParNav.setModifier(SecurityContext.getCurrentUserId());
        aeaParNav.setModifyTime(new Date());
        aeaParNavMapper.updateAeaParNav(aeaParNav);
    }

    @Override
    public void deleteAeaParNav(String navId, String rootOrgId) {

        aeaParFactorService.deleteAeaParFactorByNavId(navId, rootOrgId);
        aeaParNavMapper.deleteAeaParNav(navId, rootOrgId);
    }

    @Override
    public void batchDelAeaParNavByIds(String[] ids, String rootOrgId){

        if(ids==null){
            throw  new InvalidParameterException("参数ids为空!");
        }
        if(ids!=null&&ids.length==0){
            throw  new InvalidParameterException("参数ids为空!");
        }
        for(String navId:ids){
            aeaParFactorService.deleteAeaParFactorByNavId(navId, rootOrgId);
            aeaParNavMapper.deleteAeaParNav(navId, rootOrgId);
        }
    }

    @Override
    public void changIsActiveState(String navId, String rootOrgId){

        if (StringUtils.isBlank(navId)) {
            throw new InvalidParameterException("参数navId为空!");
        }
        aeaParNavMapper.changIsActiveState(navId, rootOrgId);
    }
}
