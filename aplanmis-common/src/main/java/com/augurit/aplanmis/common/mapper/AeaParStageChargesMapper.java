package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaParStageCharges;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 阶段办事指南收费项目信息-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaParStageChargesMapper {

    void insertAeaParStageCharges(AeaParStageCharges aeaParStageCharges);

    void updateAeaParStageCharges(AeaParStageCharges aeaParStageCharges);

    void deleteAeaParStageCharges(@Param("id") String id);

    void batchDelChargesByIds(@Param("ids") String[] ids);

    List<AeaParStageCharges> listAeaParStageCharges(AeaParStageCharges aeaParStageCharges);

    AeaParStageCharges getAeaParStageChargesById(@Param("id") String id);

    Long getMaxSortNo(@Param("stageId") String stageId,
                      @Param("rootOrgId")String rootOrgId);
}
