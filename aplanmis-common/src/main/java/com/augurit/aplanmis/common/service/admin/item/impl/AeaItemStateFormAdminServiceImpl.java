package com.augurit.aplanmis.common.service.admin.item.impl;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaItemStateForm;
import com.augurit.aplanmis.common.mapper.AeaItemStateFormMapper;
import com.augurit.aplanmis.common.service.admin.item.AeaItemInoutAdminService;
import com.augurit.aplanmis.common.service.admin.item.AeaItemStateFormAdminService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * AeaItemStateFormAdminServiceImpl class
 *
 * @author jjt
 * @date 2019/4/21
 */
@Service
@Transactional
public class AeaItemStateFormAdminServiceImpl implements AeaItemStateFormAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaItemStateFormAdminServiceImpl.class);

    @Autowired
    private AeaItemStateFormMapper aeaItemStateFormMapper;

    @Autowired
    private AeaItemInoutAdminService aeaItemInoutAdminService;

    @Override
    public void saveAeaItemStateForm(AeaItemStateForm form) {

        if(form.getSortNo()==null){
            Long inSortNo = aeaItemInoutAdminService.getMaxSortNo(form.getItemVerId(), form.getItemStateVerId(), Status.ON, SecurityContext.getCurrentOrgId());
            Long formSortNo = getMaxSortNo(form.getItemVerId(),form.getItemStateVerId());
            form.setSortNo(formSortNo>=inSortNo?formSortNo:inSortNo);
        }
        form.setCreater(SecurityContext.getCurrentUserId());
        form.setCreateTime(new Date());
        aeaItemStateFormMapper.insertAeaItemStateForm(form);
    }

    @Override
    public void updateAeaItemStateForm(AeaItemStateForm form) {

        aeaItemStateFormMapper.updateAeaItemStateForm(form);
    }

    @Override
    public void deleteAeaItemStateFormById(String id) {

        if(StringUtils.isBlank(id)){
            throw new InvalidParameterException("参数id为空!");
        }
        aeaItemStateFormMapper.deleteAeaItemStateFormById(id);
    }

    @Override
    public void batchDelAeaItemStateFormByIds(String[] ids){

        if(ids==null){
            throw new InvalidParameterException("参数ids为空!");
        }
        if(ids!=null&&ids.length==0){
            throw new InvalidParameterException("参数ids为空!");
        }
        aeaItemStateFormMapper.batchDelAeaItemStateFormByIds(ids);
    }


    @Override
    public AeaItemStateForm getAeaItemStateFormById(String id) {

        if(StringUtils.isBlank(id)){
            throw new InvalidParameterException("参数id为空!");
        }
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaItemStateFormMapper.getAeaItemStateFormById(id);
    }

    @Override
    public List<AeaItemStateForm> listAeaItemStateForm(AeaItemStateForm aeaItemStateForm) {

        List<AeaItemStateForm> list = aeaItemStateFormMapper.listAeaItemStateForm(aeaItemStateForm);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public PageInfo<AeaItemStateForm> listAeaItemStateForm(AeaItemStateForm aeaItemStateForm, Page page) {

        PageHelper.startPage(page);
        List<AeaItemStateForm> list = aeaItemStateFormMapper.listAeaItemStateForm(aeaItemStateForm);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaItemStateForm>(list);
    }

    @Override
    public PageInfo<AeaItemStateForm> listCurOrgFormByPage(AeaItemStateForm form, Page page){

        PageHelper.startPage(page);
        form.setFormOrgId(SecurityContext.getCurrentOrgId());
        List<AeaItemStateForm> list = aeaItemStateFormMapper.listCurOrgForm(form);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaItemStateForm>(list);

    }

    @Override
    public Long getMaxSortNo(String itemVerId, String itemStateVerId){

        Long sortNo = aeaItemStateFormMapper.getMaxSortNo(itemVerId, itemStateVerId);
        return sortNo==null?new Long(1) : sortNo+1;
    }

    @Override
    public void saveAeaItemStateForms(String itemVerId, String itemStateVerId, String isStateForm, String itemStateId, String[] formIds){

        AeaItemStateForm form = new AeaItemStateForm();
        form.setItemVerId(itemVerId);
        form.setItemStateVerId(itemStateVerId);
        form.setIsStateForm(isStateForm);
        form.setItemStateId(itemStateId);
        aeaItemStateFormMapper.deleteAeaItemStateFormByCond(form);
        saveAeaItemStateFormsAndNotDelOld(itemVerId, itemStateVerId, isStateForm, itemStateId, formIds);
    }

    @Override
    public void saveAeaItemStateFormsAndNotDelOld(String itemVerId, String itemStateVerId, String isStateForm, String itemStateId, String[] formIds){

        if(formIds!=null && formIds.length>0){
            String userId = SecurityContext.getCurrentUserId();
            String rootOrgId = SecurityContext.getCurrentOrgId();
            for(String id : formIds){
                if(StringUtils.isNotBlank(id)){
                    AeaItemStateForm stateform = new AeaItemStateForm();
                    stateform.setItemStateFormId(UUID.randomUUID().toString());
                    stateform.setItemVerId(itemVerId);
                    stateform.setItemStateVerId(itemStateVerId);
                    stateform.setIsStateForm(isStateForm);
                    stateform.setItemStateId(itemStateId);
                    stateform.setFormId(id);
                    if(stateform.getSortNo()==null){
                        Long inSortNo = aeaItemInoutAdminService.getMaxSortNo(stateform.getItemVerId(), stateform.getItemStateVerId(), Status.ON, rootOrgId);
                        Long formSortNo = getMaxSortNo(stateform.getItemVerId(), stateform.getItemStateVerId());
                        stateform.setSortNo(formSortNo>=inSortNo?formSortNo:inSortNo);
                    }
                    stateform.setCreater(userId);
                    stateform.setCreateTime(new Date());
                    aeaItemStateFormMapper.insertAeaItemStateForm(stateform);
                }
            }
        }
    }

    @Override
    public List<AeaItemStateForm> listItemNoSelectForm(AeaItemStateForm form){

        return aeaItemStateFormMapper.listItemNoSelectForm(form);
    }

    @Override
    public PageInfo<AeaItemStateForm> listItemNoSelectFormByPage(AeaItemStateForm form, Page page){

        PageHelper.startPage(page);
        form.setFormOrgId(SecurityContext.getCurrentOrgId());
        List<AeaItemStateForm> list = aeaItemStateFormMapper.listItemNoSelectForm(form);
        return new PageInfo<>(list);
    }
}

