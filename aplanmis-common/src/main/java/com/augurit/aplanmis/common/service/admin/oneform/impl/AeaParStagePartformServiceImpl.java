package com.augurit.aplanmis.common.service.admin.oneform.impl;

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

@Service
@Transactional
public class AeaParStagePartformServiceImpl implements AeaParStagePartformService {

    private static Logger logger = LoggerFactory.getLogger(AeaParStagePartformServiceImpl.class);

    @Autowired
    private AeaParStagePartformMapper aeaParStagePartformMapper;

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

        if(StringUtils.isBlank(id)){
            throw new InvalidParameterException("参数id为空!");
        }
        aeaParStagePartformMapper.delStagePartformById(id);
    }

    @Override
    public void batchDelStagePartformByIds(String[] ids){

        if(ids!=null&&ids.length>0) {
            aeaParStagePartformMapper.batchDelStagePartformByIds(ids);
        }else{
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

        if(StringUtils.isBlank(id)){
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
    public List<AeaParStagePartform> listStagePartformRelForm(AeaParStagePartform partform){

        List<AeaParStagePartform> list = aeaParStagePartformMapper.listStagePartformRelFormInfo(partform);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public PageInfo<AeaParStagePartform> listStagePartformRelForm(AeaParStagePartform partform, Page page){

        PageHelper.startPage(page);
        List<AeaParStagePartform> list = aeaParStagePartformMapper.listStagePartformRelFormInfo(partform);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaParStagePartform>(list);
    }

    @Override
    public Long getMaxSortNo(String stageId){

        Long sortNo = aeaParStagePartformMapper.getMaxSortNo(stageId);
        return sortNo==null?1L : (sortNo+1L);
    }

    @Override
    public List<AeaParStagePartform> listPartFormNoSelectForm(AeaParStagePartform partform){

        partform.setFormOrgId(SecurityContext.getCurrentOrgId());
        return aeaParStagePartformMapper.listPartFormNoSelectForm(partform);
    }

    @Override
    public PageInfo<AeaParStagePartform> listPartFormNoSelectFormByPage(AeaParStagePartform partform, Page page){

        partform.setFormOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaParStagePartform> list = aeaParStagePartformMapper.listPartFormNoSelectForm(partform);
        return new PageInfo<>(list);
    }
}

