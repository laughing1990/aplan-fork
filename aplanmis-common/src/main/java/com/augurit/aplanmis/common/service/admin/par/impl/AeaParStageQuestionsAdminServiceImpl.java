package com.augurit.aplanmis.common.service.admin.par.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaParStageQuestions;
import com.augurit.aplanmis.common.mapper.AeaParStageQuestionsMapper;
import com.augurit.aplanmis.common.service.admin.par.AeaParStageQuestionsAdminService;
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
* 阶段办事指南常见问题回答-Service服务接口实现类
*/
@Service
@Transactional
public class AeaParStageQuestionsAdminServiceImpl implements AeaParStageQuestionsAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaParStageQuestionsAdminServiceImpl.class);

    @Autowired
    private AeaParStageQuestionsMapper aeaParStageQuestionsMapper;

    @Override
    public void saveAeaParStageQuestions(AeaParStageQuestions aeaParStageQuestions) {

        aeaParStageQuestions.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaParStageQuestions.setCreater(SecurityContext.getCurrentUserId());
        aeaParStageQuestions.setCreateTime(new Date());
        aeaParStageQuestionsMapper.insertAeaParStageQuestions(aeaParStageQuestions);
    }

    @Override
    public void updateAeaParStageQuestions(AeaParStageQuestions aeaParStageQuestions) {

        aeaParStageQuestions.setModifier(SecurityContext.getCurrentUserId());
        aeaParStageQuestions.setModifyTime(new Date());
        aeaParStageQuestionsMapper.updateAeaParStageQuestions(aeaParStageQuestions);
    }

    @Override
    public void deleteAeaParStageQuestionsById(String id) {

        if(StringUtils.isBlank(id)){
            throw new InvalidParameterException("参数id为空!");
        }
        aeaParStageQuestionsMapper.deleteAeaParStageQuestions(id);
    }

    @Override
    public void batchDelQuestAnswerByIds(String[] ids){

        if(ids!=null && ids.length>0) {
            aeaParStageQuestionsMapper.batchDelQuestAnswerByIds(ids);
        }else{
            throw new InvalidParameterException("参数ids为空!");
        }
    }

    @Override
    public PageInfo<AeaParStageQuestions> listAeaParStageQuestions(AeaParStageQuestions aeaParStageQuestions, Page page) {

        aeaParStageQuestions.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaParStageQuestions> list = aeaParStageQuestionsMapper.listAeaParStageQuestions(aeaParStageQuestions);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaParStageQuestions>(list);
    }

    @Override
    public AeaParStageQuestions getAeaParStageQuestionsById(String id) {

        if(StringUtils.isBlank(id)){
            throw new InvalidParameterException("参数id为空!");
        }
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaParStageQuestionsMapper.getAeaParStageQuestionsById(id);
    }

    @Override
    public List<AeaParStageQuestions> listAeaParStageQuestions(AeaParStageQuestions aeaParStageQuestions) {

        aeaParStageQuestions.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaParStageQuestions> list = aeaParStageQuestionsMapper.listAeaParStageQuestions(aeaParStageQuestions);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public Long getMaxSortNo(String stageId, String rootOrgId){

        Long sortNo = aeaParStageQuestionsMapper.getMaxSortNo(stageId, rootOrgId);
        return sortNo==null?1L:(sortNo+1);
    }
}

