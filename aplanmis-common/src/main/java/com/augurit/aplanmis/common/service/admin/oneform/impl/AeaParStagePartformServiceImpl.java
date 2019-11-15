package com.augurit.aplanmis.common.service.admin.oneform.impl;

import com.augurit.agcloud.bpm.common.domain.ActStoForm;
import com.augurit.agcloud.bpm.common.mapper.ActStoFormMapper;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.common.domain.AeaParStagePartform;
import com.augurit.aplanmis.common.mapper.AeaParStagePartformMapper;
import com.augurit.aplanmis.common.service.admin.oneform.AeaParStagePartformService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AeaParStagePartformServiceImpl implements AeaParStagePartformService {

    private static Logger logger = LoggerFactory.getLogger(AeaParStagePartformServiceImpl.class);

    @Autowired
    private AeaParStagePartformMapper aeaParStagePartformMapper;

    @Autowired
    private ActStoFormMapper actStoFormMapper;

    @Override
    public void saveStagePartform(AeaParStagePartform partform) {

        partform.setSortNo(getMaxSortNo(partform.getStageId()));
        partform.setCreater(SecurityContext.getCurrentUserId());
        partform.setCreateTime(new Date());
        aeaParStagePartformMapper.insertStagePartform(partform);
    }

    @Override
    public void updateStagePartform(AeaParStagePartform partform) {

        aeaParStagePartformMapper.updateStagePartform(partform);
    }

    @Override
    public void deleteStagePartformById(String id) {

        if (StringUtils.isBlank(id)) {
            throw new InvalidParameterException("参数id为空!");
        }
        aeaParStagePartformMapper.delStagePartformById(id);
    }

    @Override
    public void batchDelStagePartformByIds(String[] ids) {

        if (ids != null && ids.length > 0) {
            aeaParStagePartformMapper.batchDelStagePartformByIds(ids);
        } else {
            throw new InvalidParameterException("参数ids为空!");
        }
    }

    @Override
    public PageInfo<AeaParStagePartform> listStagePartform(AeaParStagePartform partform, Page page) {

        PageHelper.startPage(page);
        List<AeaParStagePartform> list = aeaParStagePartformMapper.listStagePartform(partform);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaParStagePartform>(list);
    }

    @Override
    public AeaParStagePartform getStagePartformById(String id) {

        if (StringUtils.isBlank(id)) {
            throw new InvalidParameterException("参数id为空!");
        }
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaParStagePartformMapper.getStagePartformById(id);
    }

    @Override
    public List<AeaParStagePartform> listStagePartform(AeaParStagePartform partform) {

        List<AeaParStagePartform> list = aeaParStagePartformMapper.listStagePartform(partform);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public List<AeaParStagePartform> listStagePartformRelForm(AeaParStagePartform partform) {

        List<AeaParStagePartform> list = aeaParStagePartformMapper.listStagePartformRelFormInfo(partform);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public PageInfo<AeaParStagePartform> listStagePartformRelForm(AeaParStagePartform partform, Page page) {

        PageHelper.startPage(page);
        List<AeaParStagePartform> list = aeaParStagePartformMapper.listStagePartformRelFormInfo(partform);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaParStagePartform>(list);
    }

    @Override
    public Long getMaxSortNo(String stageId) {

        Long sortNo = aeaParStagePartformMapper.getMaxSortNo(stageId);
        return sortNo == null ? 1L : (sortNo + 1L);
    }

    @Override
    public List<AeaParStagePartform> listPartFormNoSelectForm(AeaParStagePartform partform) {

        partform.setFormOrgId(SecurityContext.getCurrentOrgId());
        return aeaParStagePartformMapper.listPartFormNoSelectForm(partform);
    }

    @Override
    public PageInfo<AeaParStagePartform> listPartFormNoSelectFormByPage(AeaParStagePartform partform, Page page) {

        partform.setFormOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaParStagePartform> list = aeaParStagePartformMapper.listPartFormNoSelectForm(partform);
        return new PageInfo<>(list);
    }

    @Override
    public void createAndUpdateDevForm(String formCode, String formName, String formLoadUrl, String formId, String stagePartformId) throws Exception {

        if (StringUtils.isBlank(formId)) {
            ActStoForm actStoForm = new ActStoForm();
            actStoForm.setFormOrgId(SecurityContext.getCurrentOrgId());
            actStoForm.setFormCode(formCode);
            actStoForm.setFormId(UUID.randomUUID().toString());
            actStoForm.setFormName(formName);
            actStoForm.setFormLoadUrl(formLoadUrl);
            actStoForm.setFormProperty("dev-biz");
            actStoForm.setIsDeleted("0");
            actStoForm.setCreater(SecurityContext.getCurrentUserId());
            actStoForm.setCreateTime(new Date());
            actStoForm.setFormTmnId("1");
            actStoForm.setIsAutoBuildForm("0");
            actStoForm.setIsAutoBuildTable("0");
            actStoForm.setIsMetaDbTable("0");
            actStoForm.setFormVersion(1l);
            actStoForm.setIsInnerForm("0");
            actStoForm.setIsLock("0");
            actStoFormMapper.insertActStoForm(actStoForm);

            AeaParStagePartform aeaParStagePartform = aeaParStagePartformMapper.getStagePartformById(stagePartformId);
            if (aeaParStagePartform == null) throw new Exception("aeaParStagePartform is null");
            aeaParStagePartform.setStoFormId(actStoForm.getFormId());
            aeaParStagePartformMapper.updateStagePartform(aeaParStagePartform);

        } else {

            ActStoForm actStoForm = actStoFormMapper.getActStoFormById(formId);
            if (actStoForm == null) throw new Exception("actStoForm is null");
            actStoForm.setFormCode(formCode);
            actStoForm.setFormName(formName);
            actStoForm.setFormLoadUrl(formLoadUrl);
            actStoForm.setModifier(SecurityContext.getCurrentUserId());
            actStoForm.setModifyTime(new Date());
            actStoFormMapper.updateActStoForm(actStoForm);
        }
    }
}

