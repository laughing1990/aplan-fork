package com.augurit.aplanmis.common.service.guide;

import com.augurit.aplanmis.common.domain.AeaItemGuideQuestions;

import java.util.List;

public interface AeaItemGuideQuestionsService {
    /**
     * 根据事项版本ID查询常见问题解答
     * @param itemVerId 必须参数 事项版本ID
     * @return
     * @throws Exception
     */
    public List<AeaItemGuideQuestions> listAeaItemGuideQuestions(String itemVerId) throws Exception;
}
