package com.augurit.aplanmis.common.mapper;

import com.augurit.agcloud.bpm.common.domain.ActStoForm;
import com.augurit.aplanmis.common.domain.AeaParStageItem;
import com.augurit.aplanmis.common.domain.AeaParStageOneform;
import com.augurit.aplanmis.common.vo.ActStoFormVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AeaParStageOneformMapper {

    void insertAeaParStageOneform(AeaParStageOneform aeaParStageOneform);

    void updateAeaParStageOneform(AeaParStageOneform aeaParStageOneform);

    void deleteAeaParStageOneform(@Param("id") String id);

    List<AeaParStageOneform> listAeaParStageOneform(@Param("parStageId") String parStageId);

    List<AeaParStageItem> listAeaStageItem(@Param("parStageId") String parStageId, @Param("rootOrgId") String rootOrgId);

    ActStoForm getActStoForm(@Param("stageId") String stageId,@Param("itemVerId") String itemVerId, @Param("formId") String formId);

    AeaParStageOneform getAeaParStageOneformById(@Param("id") String id);

    List<ActStoFormVo> listActStoFromNotInStageItem(ActStoForm aeaParOneform);

    List<AeaParStageOneform> listAeaParStageOneformNoRel(AeaParStageOneform oneform);
}
