package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaCert;
import com.augurit.aplanmis.common.domain.AeaItemInout;
import com.augurit.aplanmis.common.domain.AeaParIn;
import com.augurit.aplanmis.common.mapper.base.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 电子证照定义表-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaCertMapper extends BaseMapper<AeaCert> {

    Long getMaxCertSortNo(@Param("rootOrgId") String rootOrgId);

    List<AeaCert> listAeaCert(AeaCert aeaCert);

    List<AeaCert> listAeaCertByIds(@Param("ids") List<String> ids);

    Integer checkUniqueCertCode(@Param("certId") String certId,
                                @Param("certCode") String certCode,
                                @Param("rootOrgId") String rootOrgId);

    void batchDeleteCertByCertTypeId(@Param("certTypeId") String certTypeId);

    void batchDeleteCertByCertTypeIds(@Param("certTypeIds") String[] certTypeIds);

    void batchDeleteCertByCertIds(@Param("certIds") String[] certIds);

    List<AeaCert> getCertByServiceId(@Param("serviceId") String serviceId);

    List<AeaCert> listStageNoSelectCert(AeaParIn aeaParIn);

    List<AeaCert> listItemNoSelectCert(AeaItemInout inout);

    AeaCert getAeaCertById(@Param("certId") String certId, @Param("rootOrgId") String rootOrgId);

    List<AeaCert> getOutputCertsByItemVerId(@Param("itemVerId") String itemVerId, @Param("rootOrgId") String rootOrgId);

}
