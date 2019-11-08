package com.augurit.aplanmis.common.service.admin.par;

import com.augurit.aplanmis.common.domain.AeaParStageQuestions;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
* 阶段办事指南常见问题回答-Service服务调用接口类
*/
public interface AeaParStageQuestionsAdminService {

     void saveAeaParStageQuestions(AeaParStageQuestions aeaParStageQuestions);
     void updateAeaParStageQuestions(AeaParStageQuestions aeaParStageQuestions);
     void deleteAeaParStageQuestionsById(String id);
     PageInfo<AeaParStageQuestions> listAeaParStageQuestions(AeaParStageQuestions aeaParStageQuestions, Page page);
     AeaParStageQuestions getAeaParStageQuestionsById(String id);
     List<AeaParStageQuestions> listAeaParStageQuestions(AeaParStageQuestions aeaParStageQuestions);
     void batchDelQuestAnswerByIds(String[] ids);
     Long getMaxSortNo(String stageId, String rootOrgId);
}
