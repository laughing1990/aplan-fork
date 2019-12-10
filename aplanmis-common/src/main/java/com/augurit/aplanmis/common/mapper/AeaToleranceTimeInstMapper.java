package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaToleranceTimeInst;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface AeaToleranceTimeInstMapper {

    public void insertAeaToleranceTimeInst(AeaToleranceTimeInst aeaToleranceTimeInst) throws Exception;

    public void updateAeaToleranceTimeInst(AeaToleranceTimeInst aeaToleranceTimeInst) throws Exception;

    public void deleteAeaToleranceTimeInst(@Param("id") String id) throws Exception;

    public List<AeaToleranceTimeInst> listAeaToleranceTimeInst(AeaToleranceTimeInst aeaToleranceTimeInst) throws Exception;

    public AeaToleranceTimeInst getAeaToleranceTimeInstById(@Param("id") String id) throws Exception;

    public List<AeaToleranceTimeInst> getUnCompletedToleranceTimeinstsByJobTimerId(@Param("orgId") String orgId,@Param("jobTimerId") String jobTimerId) throws Exception;

    void batchUpdateAeaToleranceTimeInst(@Param("list") List<AeaToleranceTimeInst> list) throws Exception;

}
