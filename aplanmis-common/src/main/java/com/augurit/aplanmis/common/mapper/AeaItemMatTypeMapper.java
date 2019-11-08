package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaItemMatType;
import com.augurit.aplanmis.common.mapper.base.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 材料类型表-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaItemMatTypeMapper extends BaseMapper<AeaItemMatType> {


    List<AeaItemMatType> listAeaItemMatType(AeaItemMatType aeaItemMatType);

    Integer checkUniqueTypeCode(@Param("matTypeId") String matTypeId,
                                @Param("typeCode") String typeCode,
                                @Param("rootOrgId")String rootOrgId);

    Long getMaxSortNo(@Param("rootOrgId") String rootOrgId);

    void deleteSelfAndAllChildMatType(@Param("id")String id, @Param("rootOrgId")String rootOrgId);

    List<AeaItemMatType> listOtherMatTypesByMatTypeId(@Param("id")String id, @Param("rootOrgId")String rootOrgId);

    List<AeaItemMatType> listAllRelChildMatType(@Param("id")String id, @Param("rootOrgId")String rootOrgId);
}
