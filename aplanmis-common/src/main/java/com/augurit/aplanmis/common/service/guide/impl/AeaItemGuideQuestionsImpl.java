package com.augurit.aplanmis.common.service.guide.impl;

import com.augurit.aplanmis.common.domain.AeaItemGuideQuestions;
import com.augurit.aplanmis.common.mapper.AeaItemGuideQuestionsMapper;
import com.augurit.aplanmis.common.service.guide.AeaItemGuideQuestionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.List;

@Service
public class AeaItemGuideQuestionsImpl implements AeaItemGuideQuestionsService {
    @Autowired
    private AeaItemGuideQuestionsMapper aeaItemGuideQuestionsMapper;

    public List<AeaItemGuideQuestions> listAeaItemGuideQuestions(String itemVerId) throws Exception {
        if(org.apache.commons.lang.StringUtils.isBlank(itemVerId)) throw new InvalidParameterException("参数itemVerId为空！");
        AeaItemGuideQuestions questions = new AeaItemGuideQuestions();
        questions.setItemVerId(itemVerId);
        List<AeaItemGuideQuestions> questionsList = aeaItemGuideQuestionsMapper.listAeaItemGuideQuestions(questions);
        return questionsList;
    }
}
