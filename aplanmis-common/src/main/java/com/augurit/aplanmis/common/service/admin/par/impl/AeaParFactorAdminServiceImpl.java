package com.augurit.aplanmis.common.service.admin.par.impl;

import com.augurit.agcloud.bsc.util.CommonConstant;
import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ActiveStatus;
import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.domain.AeaParFactor;
import com.augurit.aplanmis.common.domain.AeaParFactorTheme;
import com.augurit.aplanmis.common.mapper.AeaParFactorMapper;
import com.augurit.aplanmis.common.mapper.AeaParFactorThemeMapper;
import com.augurit.aplanmis.common.service.admin.par.AeaParFactorAdminService;
import com.augurit.aplanmis.common.utils.EuiFactorUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author ZhangXinhui
 * @date 2019/8/30 030 19:31
 * @desc
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class AeaParFactorAdminServiceImpl implements AeaParFactorAdminService {

    @Autowired
    private AeaParFactorMapper aeaParFactorMapper;

    @Autowired
    private AeaParFactorThemeMapper aeaParFactorThemeMapper;

    @Override
    public PageInfo<AeaParFactor> listAeaParFactorPage(AeaParFactor aeaParFactor, Page page) {

        PageHelper.startPage(page);
        List data = aeaParFactorMapper.listAeaParFactorWithTheme(aeaParFactor);
        return new PageInfo<>(data);
    }

    @Override
    public List<AeaParFactor> listAeaParFactor(AeaParFactor aeaParFactor) {

        return  aeaParFactorMapper.listAeaParFactorWithTheme(aeaParFactor);
    }

    @Override
    public List<AeaParFactor> gtreeAeaParFactor(AeaParFactor aeaParFactor){

        List<AeaParFactor> list = aeaParFactorMapper.listAeaParFactorWithTheme(aeaParFactor);
        if(list!=null&&list.size()>0){
            if(StringUtils.isBlank(aeaParFactor.getKeyword())){
                list =  EuiFactorUtils.buildTree(list);
            }
        }
        return list;
    }

    @Override
    public Long getMaxSortNo(String navId, String rootOrgId) {

        Long sortNo = aeaParFactorMapper.getMaxSortNo(navId, rootOrgId);
        return sortNo==null?1L:(sortNo+1L);
    }

    @Override
    public AeaParFactor getAeaParFactorWithThemeById(String factorId, String rootOrgId) throws Exception {

        if (StringUtils.isBlank(factorId)) {
            throw new Exception("缺少factorId");
        }
        return aeaParFactorMapper.getAeaParFactorWithThemeById(factorId, rootOrgId);
    }


    @Override
    public void deleteAeaParFactorById(String factorId, String rootOrgId) {

        List<AeaParFactor> factorList = aeaParFactorMapper.listSelfAndAllChildAeaParFactor(factorId, rootOrgId);
        if(factorList!=null&&factorList.size()>0){
           for(AeaParFactor factor : factorList){
               aeaParFactorThemeMapper.deleteAeaParFactorThemeByFactorId(factor.getFactorId());
           }
        }
        aeaParFactorMapper.batchDelSelfAndAllChildFactorById(factorId, rootOrgId);
    }

    @Override
    public void batchDelAeaParFactorByIds(String[] factorIds, String rootOrgId) {

        if(factorIds==null){
            throw new InvalidParameterException("参数factorIds为空!");
        }
        if(factorIds!=null&&factorIds.length==0){
            throw new InvalidParameterException("参数factorIds为空!");
        }
        for(String factorId:factorIds){
            List<AeaParFactor> factorList = aeaParFactorMapper.listSelfAndAllChildAeaParFactor(factorId, rootOrgId);
            if(factorList!=null&&factorList.size()>0){
                for(AeaParFactor factor : factorList){
                    aeaParFactorThemeMapper.deleteAeaParFactorThemeByFactorId(factor.getFactorId());
                }
            }
            aeaParFactorMapper.batchDelSelfAndAllChildFactorById(factorId, rootOrgId);
        }
    }

    @Override
    public void deleteAeaParFactorByNavId(String navId, String rootOrgId) {

        AeaParFactor aeaParFactor = new AeaParFactor();
        aeaParFactor.setRootOrgId(rootOrgId);
        aeaParFactor.setNavId(navId);
        List<AeaParFactor> list = aeaParFactorMapper.listAeaParFactor(aeaParFactor);
        if (list != null) {
            list.forEach(item -> {
                deleteAeaParFactorById(item.getFactorId(), rootOrgId);
            });
        }

    }

    @Override
    public void saveAeaParFactor(AeaParFactor aeaParFactor) {

        aeaParFactor.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaParFactor.setIsActive(ActiveStatus.ACTIVE.getValue());
        aeaParFactor.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
        aeaParFactor.setFactorSeq(getTypeSeq(aeaParFactor));
        aeaParFactor.setCreater(SecurityContext.getCurrentUserId());
        aeaParFactor.setCreateTime(new Date());
        aeaParFactorMapper.insertAeaParFactor(aeaParFactor);
        if (StringUtils.isBlank(aeaParFactor.getThemeId())) {
            return;
        }
        bindTheme(aeaParFactor);
    }

    @Override
    public void updateAeaParFactor(AeaParFactor aeaParFactor) {

        aeaParFactor.setModifier(SecurityContext.getCurrentUserId());
        aeaParFactor.setModifyTime(new Date());
        aeaParFactorMapper.updateAeaParFactor(aeaParFactor);
        if (StringUtils.isBlank(aeaParFactor.getThemeId())) {
            return;
        }
        unbindTheme(aeaParFactor.getFactorId());
        bindTheme(aeaParFactor);
    }

    @Override
    public void unbindTheme(String factorId){

        aeaParFactorThemeMapper.deleteAeaParFactorThemeByFactorId(factorId);
    }

    private void bindTheme(AeaParFactor aeaParFactor){
        AeaParFactorTheme aeaParFactorTheme = new AeaParFactorTheme();
        aeaParFactorTheme.setGuideThemeId(UuidUtil.generateUuid());
        aeaParFactorTheme.setCreater(SecurityContext.getCurrentUserId());
        aeaParFactorTheme.setCreateTime(new Date());
        aeaParFactorTheme.setFactorId(aeaParFactor.getFactorId());
        aeaParFactorTheme.setThemeId(aeaParFactor.getThemeId());
        aeaParFactorThemeMapper.insertAeaParFactorTheme(aeaParFactorTheme);
    }

    private String getTypeSeq(AeaParFactor aeaParFactor) {

        String factorId = aeaParFactor.getFactorId();
        String parentFactorId = aeaParFactor.getParentFactorId();
        String rootOrgId = aeaParFactor.getRootOrgId();
        String suffix = factorId + CommonConstant.SEQ_SEPARATOR;
        if (StringUtils.isBlank(parentFactorId)) {
            return CommonConstant.SEQ_SEPARATOR + suffix;
        }
        //获取上级的序列
        AeaParFactor apf = aeaParFactorMapper.getAeaParFactorById(parentFactorId, rootOrgId);
        if (apf == null) {
            return CommonConstant.SEQ_SEPARATOR + suffix;
        }
        String parentSeq = apf.getFactorSeq();
        return StringUtils.isBlank(parentSeq) ? CommonConstant.SEQ_SEPARATOR + suffix : parentSeq + suffix;

    }
}
