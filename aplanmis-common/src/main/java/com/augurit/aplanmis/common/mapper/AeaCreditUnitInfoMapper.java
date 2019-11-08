package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaCreditUnitInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 单位表-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaCreditUnitInfoMapper {

    void insertAeaCreditUnitInfo(AeaCreditUnitInfo aeaCreditUnitInfo);

    void updateAeaCreditUnitInfo(AeaCreditUnitInfo aeaCreditUnitInfo);

    void deleteAeaCreditUnitInfo(@Param("id") String id, @Param("rootOrgId") String rootOrgId);

    List<AeaCreditUnitInfo> listAeaCreditUnitInfo(AeaCreditUnitInfo aeaCreditUnitInfo);

    List<AeaCreditUnitInfo> listAeaCreditUnitInfoWithGlobalUnit(AeaCreditUnitInfo aeaCreditUnitInfo);

    AeaCreditUnitInfo getAeaCreditUnitInfoById(@Param("id") String id, @Param("rootOrgId") String rootOrgId);

    AeaCreditUnitInfo getAeaCreditUnitInfoWithAeaUnitById(@Param("id") String id, @Param("rootOrgId") String rootOrgId);

    void batchDelSelfAndAllChildById(@Param("id") String id, @Param("rootOrgId") String rootOrgId);
}
