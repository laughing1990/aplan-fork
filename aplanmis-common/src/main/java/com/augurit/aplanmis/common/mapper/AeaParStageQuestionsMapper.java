package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaParStageQuestions;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* 阶段办事指南常见问题回答-Mapper数据与持久化接口类
*/
@Mapper
@Repository
public interface AeaParStageQuestionsMapper {

     void insertAeaParStageQuestions(AeaParStageQuestions aeaParStageQuestions) ;
     void updateAeaParStageQuestions(AeaParStageQuestions aeaParStageQuestions) ;
     void deleteAeaParStageQuestions(@Param("id") String id) ;
     List <AeaParStageQuestions> listAeaParStageQuestions(AeaParStageQuestions aeaParStageQuestions) ;
     AeaParStageQuestions getAeaParStageQuestionsById(@Param("id") String id) ;
     void batchDelQuestAnswerByIds(@Param("ids") String[] ids);
     Long getMaxSortNo(@Param("stageId") String stageId, @Param("rootOrgId")String rootOrgId);
}
