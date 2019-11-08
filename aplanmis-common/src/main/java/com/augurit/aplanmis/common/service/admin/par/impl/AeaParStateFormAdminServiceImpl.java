package com.augurit.aplanmis.common.service.admin.par.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaParStateForm;
import com.augurit.aplanmis.common.mapper.AeaParStateFormMapper;
import com.augurit.aplanmis.common.service.admin.par.AeaParInAdminService;
import com.augurit.aplanmis.common.service.admin.par.AeaParStateFormAdminService;
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
 * @author jjt
 * @date 2019/4/16
 *
 * 情形与表单关联定义表-Service服务调用接口类
 */
@Service
@Transactional
public class AeaParStateFormAdminServiceImpl implements AeaParStateFormAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaParStateFormAdminServiceImpl.class);

    @Autowired
    private AeaParStateFormMapper aeaParStateFormMapper;

    @Autowired
    private AeaParInAdminService aeaParInAdminService;

    /**
     *  保存
     *
     * @param form
     */
    @Override
    public void saveAeaParStateForm(AeaParStateForm form){

        if(form.getSortNo()==null){
            form.setSortNo(getMaxSortNo(form.getParStageId()));
        }
        form.setCreater(SecurityContext.getCurrentUserId());
        form.setCreateTime(new Date());
        aeaParStateFormMapper.insertAeaParStateForm(form);
    }

    /**
     * 更新
     *
     * @param form
     */
    @Override
    public void updateAeaParStateForm(AeaParStateForm form){

        aeaParStateFormMapper.updateAeaParStateForm(form);
    }

    /**
     *  删除
     *
     * @param id
     */
    @Override
    public void deleteAeaParStateFormById(String id){

        if(StringUtils.isBlank(id)){
            throw new InvalidParameterException(id);
        }
        aeaParStateFormMapper.deleteAeaParStateForm(id);
    }

    @Override
    public void batchDelAeaParStateFormByIds(String[] ids){

        if(ids==null){
            throw new InvalidParameterException("参数ids为空!");
        }
        if(ids!=null&&ids.length==0){
            throw new InvalidParameterException("参数ids为空!");
        }
        aeaParStateFormMapper.batchDelAeaParStateFormByIds(ids);
    }

    /**
     *  获取
     *
     * @param id
     * @return
     */
    @Override
    public AeaParStateForm getAeaParStateFormById(String id){

        if(StringUtils.isNotBlank(id)){
            throw new InvalidParameterException(id);
        }
        return aeaParStateFormMapper.getAeaParStateFormById(id);
    }

    /**
     * 不带分页获取
     *
     * @param form
     * @return
     */
    @Override
    public List<AeaParStateForm> listAeaParStateForm(AeaParStateForm form){

        logger.debug("成功执行查询list！！");
        return aeaParStateFormMapper.listAeaParStateForm(form);
    }

    /**
     *  分页获取
     *
     * @param form
     * @param page
     * @return
     */
    @Override
    public PageInfo<AeaParStateForm> listAeaParStateForm(AeaParStateForm form, Page page){

        PageHelper.startPage(page);
        List <AeaParStateForm> list = aeaParStateFormMapper.listAeaParStateForm(form);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaParStateForm>(list);
    }

    @Override
    public Long getMaxSortNo(String stageId){

        Long sortNo = aeaParStateFormMapper.getMaxSortNo(stageId);
        return sortNo==null?new Long(1) : sortNo+1;
    }

    @Override
    public PageInfo<AeaParStateForm> listFormAndStateFormByPage(AeaParStateForm aeaParStateForm, Page page){

        PageHelper.startPage(page);
        aeaParStateForm.setFormOrgId(SecurityContext.getCurrentOrgId());
        List<AeaParStateForm> list = aeaParStateFormMapper.listCurOrgForm(aeaParStateForm);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaParStateForm>(list);
    }

    @Override
    public void saveAeaParStateForms(String parStageId, String parStateId, String isStateForm, String[] formIds){

        AeaParStateForm form = new AeaParStateForm();
        form.setParStageId(parStageId);
        form.setIsStateForm(isStateForm);
        form.setParStateId(parStateId);
        aeaParStateFormMapper.deleteAeaParStateFormByCond(form);
        saveAeaParStateFormsAndNotDelOld(parStageId, parStateId, isStateForm, formIds);
    }

    @Override
    public void saveAeaParStateFormsAndNotDelOld(String parStageId, String parStateId, String isStateForm, String[] formIds){

        if(formIds!=null && formIds.length>0){
            String userId = SecurityContext.getCurrentUserId();
            for(String id : formIds){
                if(StringUtils.isNotBlank(id)){
                    AeaParStateForm aeaParStateForm = new AeaParStateForm();
                    aeaParStateForm.setStateFormId(UUID.randomUUID().toString());
                    aeaParStateForm.setFormId(id);
                    aeaParStateForm.setParStageId(parStageId);
                    aeaParStateForm.setParStateId(parStateId);
                    aeaParStateForm.setIsStateForm(isStateForm);
                    if(aeaParStateForm.getSortNo()==null){
                        Long formSortNo = getMaxSortNo(parStageId);
                        Long inSortNo = aeaParInAdminService.getMaxSortNoByStageId(parStageId);
                        aeaParStateForm.setSortNo(formSortNo>=inSortNo?formSortNo:inSortNo);
                    }
                    aeaParStateForm.setCreater(userId);
                    aeaParStateForm.setCreateTime(new Date());
                    aeaParStateFormMapper.insertAeaParStateForm(aeaParStateForm);
                }
            }
        }
    }

    @Override
    public List<AeaParStateForm> listStageNoSelectForm(AeaParStateForm form){

        return aeaParStateFormMapper.listStageNoSelectForm(form);
    }

    @Override
    public PageInfo<AeaParStateForm> listStageNoSelectFormByPage(AeaParStateForm form, Page page){

        PageHelper.startPage(page);
        form.setFormOrgId(SecurityContext.getCurrentOrgId());
        List<AeaParStateForm> list = aeaParStateFormMapper.listStageNoSelectForm(form);
        return new PageInfo<>(list);
    }
}
