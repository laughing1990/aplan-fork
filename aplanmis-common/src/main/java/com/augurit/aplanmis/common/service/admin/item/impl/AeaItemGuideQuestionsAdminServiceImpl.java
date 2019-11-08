package com.augurit.aplanmis.common.service.admin.item.impl;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaItemGuideQuestions;
import com.augurit.aplanmis.common.mapper.AeaItemGuideQuestionsMapper;
import com.augurit.aplanmis.common.service.admin.item.AeaItemGuideQuestionsAdminService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@Service
@Transactional
public class AeaItemGuideQuestionsAdminServiceImpl implements AeaItemGuideQuestionsAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaItemGuideQuestionsAdminServiceImpl.class);

    @Autowired
    private AeaItemGuideQuestionsMapper aeaItemGuideQuestionsMapper;

    @Override
    public void saveAeaItemGuideQuestions(AeaItemGuideQuestions questions) {

        String rootOrgId = SecurityContext.getCurrentOrgId();
        questions.setOrdernum(getMaxSortNoByItemVerId(questions.getItemVerId(), rootOrgId));
        questions.setRootOrgId(rootOrgId);
        aeaItemGuideQuestionsMapper.insertAeaItemGuideQuestions(questions);
    }

    @Override
    public void updateAeaItemGuideQuestions(AeaItemGuideQuestions aeaItemGuideQuestions) {

        aeaItemGuideQuestionsMapper.updateAeaItemGuideQuestions(aeaItemGuideQuestions);
    }

    @Override
    public Long getMaxSortNoByItemVerId(String itemVerId, String rootOrgId){

        Long num = aeaItemGuideQuestionsMapper.getMaxSortNoByItemVerId(itemVerId, rootOrgId);
        return num==null?1 : num+1;
    }

    @Override
    public void deleteAeaItemGuideQuestionsById(String id) {

        Assert.notNull(id, "id is null.");
        aeaItemGuideQuestionsMapper.deleteAeaItemGuideQuestions(id);
    }

    @Override
    public void batchDeleteGuideQuestionsByItemVerId(String itemVerId, String rootOrgId){

        if(StringUtils.isBlank(rootOrgId)){
            rootOrgId = SecurityContext.getCurrentOrgId();
        }
        if (StringUtils.isNotBlank(itemVerId)) {
            aeaItemGuideQuestionsMapper.batchDeleteGuideQuestionsByItemVerId(itemVerId, rootOrgId);
        }
    }

    @Override
    public PageInfo<AeaItemGuideQuestions> listAeaItemGuideQuestions(AeaItemGuideQuestions aeaItemGuideQuestions, Page page) {

        aeaItemGuideQuestions.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaItemGuideQuestions> list = aeaItemGuideQuestionsMapper.listAeaItemGuideQuestions(aeaItemGuideQuestions);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public AeaItemGuideQuestions getAeaItemGuideQuestionsById(String id) {

        Assert.notNull(id, "id is null.");
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaItemGuideQuestionsMapper.getAeaItemGuideQuestionsById(id);
    }

    @Override
    public List<AeaItemGuideQuestions> listAeaItemGuideQuestions(AeaItemGuideQuestions aeaItemGuideQuestions) {

        aeaItemGuideQuestions.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaItemGuideQuestions> list = aeaItemGuideQuestionsMapper.listAeaItemGuideQuestions(aeaItemGuideQuestions);
        logger.debug("成功执行查询list！！");
        return list;
    }

    /**
     * 批量删除
     *
     * @param ids
     */
    @Override
    public void batchDelQuestAnswerByIds(String[] ids){

        aeaItemGuideQuestionsMapper.batchDelQuestAnswerByIds(ids);
    }
}

