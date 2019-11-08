package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaCertType;
import com.augurit.aplanmis.common.mapper.base.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 电子证照类型表-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaCertTypeMapper extends BaseMapper<AeaCertType> {


    List<AeaCertType> listAeaCertType(AeaCertType aeaCertType);

    Long getMaxSortNo(@Param("parentTypeId") String parentTypeId,
                      @Param("rootOrgId") String rootOrgId);

    Integer checkUniqueCertTypeCode(@Param("certTypeId") String certTypeId,
                                    @Param("typeCode") String typeCode ,
                                    @Param("rootOrgId") String rootOrgId);

    void batchDeleteCertType(@Param("ids") String[] ids);

    List<AeaCertType> listSelfAndChildCertTypeById(@Param("id") String id);

    void changIsActiveState(@Param("id") String id);

    List<AeaCertType> listOtherCertTypesByCertTypeId(@Param("id")String id, @Param("rootOrgId")String rootOrgId);

    List<AeaCertType> listAllRelChildCertType(@Param("id")String id, @Param("rootOrgId")String rootOrgId);
}
