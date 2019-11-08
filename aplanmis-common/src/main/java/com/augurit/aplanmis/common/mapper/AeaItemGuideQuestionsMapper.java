package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaItemGuideQuestions;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AeaItemGuideQuestionsMapper {

    void insertAeaItemGuideQuestions(AeaItemGuideQuestions aeaItemGuideQuestions);

    void updateAeaItemGuideQuestions(AeaItemGuideQuestions aeaItemGuideQuestions);

    void deleteAeaItemGuideQuestions(@Param("id") String id);

    List<AeaItemGuideQuestions> listAeaItemGuideQuestions(AeaItemGuideQuestions aeaItemGuideQuestions);

    AeaItemGuideQuestions getAeaItemGuideQuestionsById(@Param("id") String id);

    void batchDeleteGuideQuestionsByItemVerId(@Param("itemVerId") String itemVerId,
                                              @Param("rootOrgId")String rootOrgId);

    void batchDelQuestAnswerByIds(@Param("ids")String[] ids);

    Long getMaxSortNoByItemVerId(@Param("itemVerId") String itemVerId,
                                 @Param("rootOrgId")String rootOrgId);
}
