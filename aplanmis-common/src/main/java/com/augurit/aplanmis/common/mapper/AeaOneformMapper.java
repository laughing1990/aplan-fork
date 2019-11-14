package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaOneform;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AeaOneformMapper {

    void insertAeaOneform(AeaOneform aeaOneform);

    void updateAeaOneform(AeaOneform aeaOneform);

    void deleteAeaOneform(@Param("id") String id);

    void batchDelAeaOneformByIds(@Param("ids") String[] ids);

    AeaOneform getParOneformById(@Param("id") String id);

    List<AeaOneform> listAeaOneform(AeaOneform aeaOneform);

    List<AeaOneform> listAeaOneformNotInStage(AeaOneform aeaOneform);

    Long getMaxSortNo(@Param("rootOrgId") String rootOrgId);

    List<AeaOneform> listAeaOneformByStageId(@Param("stageId") String stageId, @Param("rootOrgId") String rootOrgId);

    void enOrDisableIsActive(@Param("id") String id);

    List<AeaOneform> listAeaOneformNotInItem(AeaOneform aeaOneform);
}
