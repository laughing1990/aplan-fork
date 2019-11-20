package com.augurit.aplanmis.common.service.admin.par.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaParFrontProj;
import com.augurit.aplanmis.common.mapper.AeaParFrontProjMapper;
import com.augurit.aplanmis.common.service.admin.par.AeaParFrontProjService;
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
 * 阶段的项目信息前置检测-Service服务接口实现类
 */
@Service
@Transactional
public class AeaParFrontProjServiceImpl implements AeaParFrontProjService {

    private static Logger logger = LoggerFactory.getLogger(AeaParFrontProjServiceImpl.class);

    @Autowired
    private AeaParFrontProjMapper aeaParFrontProjMapper;

    @Override
    public void saveAeaParFrontProj(AeaParFrontProj aeaParFrontProj) throws Exception {

//        checkSame(aeaParFrontProj);

        aeaParFrontProj.setCreateTime(new Date());
        aeaParFrontProj.setCreater(SecurityContext.getCurrentUserId());
        aeaParFrontProj.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaParFrontProjMapper.insertAeaParFrontProj(aeaParFrontProj);
    }

    @Override
    public void updateAeaParFrontProj(AeaParFrontProj aeaParFrontProj) throws Exception {

        aeaParFrontProj.setModifyTime(new Date());
        aeaParFrontProj.setModifier(SecurityContext.getCurrentUserId());
        aeaParFrontProjMapper.updateAeaParFrontProj(aeaParFrontProj);
    }

    @Override
    public void deleteAeaParFrontProjById(String id) throws Exception {
        if (StringUtils.isBlank(id)) {
            throw new InvalidParameterException(id);
        }
        String[] ids = id.split(",");
        for(String frontProjId :ids) {
            aeaParFrontProjMapper.deleteAeaParFrontProj(frontProjId);
        }
    }

    @Override
    public PageInfo<AeaParFrontProj> listAeaParFrontProj(AeaParFrontProj aeaParFrontProj, Page page) throws Exception {

        PageHelper.startPage(page);
        List<AeaParFrontProj> list = aeaParFrontProjMapper.listAeaParFrontProj(aeaParFrontProj);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public AeaParFrontProj getAeaParFrontProjById(String id) throws Exception {
        if (StringUtils.isBlank(id)) {
            throw new InvalidParameterException(id);
        }
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaParFrontProjMapper.getAeaParFrontProjById(id);
    }

    @Override
    public List<AeaParFrontProj> listAeaParFrontProj(AeaParFrontProj aeaParFrontProj) throws Exception {
        List<AeaParFrontProj> list = aeaParFrontProjMapper.listAeaParFrontProj(aeaParFrontProj);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public Long getMaxSortNo(AeaParFrontProj aeaParFrontProj)throws Exception{

        Long sortNo = aeaParFrontProjMapper.getMaxSortNo(aeaParFrontProj);
        return sortNo==null?1L:(sortNo+1L);
    }

    private void checkSame(AeaParFrontProj aeaParFrontProj) throws Exception{

        AeaParFrontProj queryParFrontProj = new AeaParFrontProj();
        queryParFrontProj.setStageId(aeaParFrontProj.getStageId());
        queryParFrontProj.setRuleName(aeaParFrontProj.getRuleName());
        List<AeaParFrontProj> list = aeaParFrontProjMapper.listAeaParFrontProj(queryParFrontProj);
        if(list.size()>0){
            throw new RuntimeException("已有相同规则名称的项目前置检测!");
        }

        queryParFrontProj.setRuleName(null);
        queryParFrontProj.setRuleEl(aeaParFrontProj.getRuleEl());
        list = aeaParFrontProjMapper.listAeaParFrontProj(queryParFrontProj);
        if(list.size()>0){
            throw new RuntimeException("已有相同规则的项目前置检测!");
        }
    }

    @Override
    public void changIsActive(String id, String rootOrgId){

        aeaParFrontProjMapper.changIsActive(id, rootOrgId);
    }
}

