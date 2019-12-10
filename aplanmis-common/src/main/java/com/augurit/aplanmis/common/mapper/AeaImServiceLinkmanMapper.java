package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaImServiceLinkman;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * -中介服务与联系人关联Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaImServiceLinkmanMapper {

    void insertAeaImServiceLinkman(AeaImServiceLinkman aeaImServiceLinkman) throws Exception;

    void updateAeaImServiceLinkman(AeaImServiceLinkman aeaImServiceLinkman) throws Exception;

    void deleteAeaImServiceLinkman(@Param("id") String id) throws Exception;

    List<AeaImServiceLinkman> listAeaImServiceLinkman(AeaImServiceLinkman aeaImServiceLinkman) throws Exception;

    AeaImServiceLinkman getAeaImServiceLinkmanById(@Param("id") String id) throws Exception;

    AeaImServiceLinkman getAeaImServiceLinkmanByServiceId(@Param("serviceId") String serviceId, @Param("linkmanInfoId") String linkmanInfoId) throws Exception;

    List<AeaLinkmanInfo> listAeaImServiceLinkmanByServiceId(@Param("serviceId") String serviceId) throws Exception;

    List<AeaLinkmanInfo> listAeaImServiceLinkmanByUnitInfoId(@Param("unitInfoId") String unitInfoId, @Param("cardNo") String cardNo, @Param("auditFlag") String auditFlag) throws Exception;

    AeaImServiceLinkman getAeaImServiceLinkmanDetailById(@Param("serviceLinkmanId") String serviceLinkmanId) throws Exception;

    /**
     * 获取单位服务联系人
     *
     * @param unitInfoId 单位ID
     * @param serviceId  服务ID
     * @return List<AeaImServiceLinkman>
     */
    List<AeaImServiceLinkman> listAeaImServiceLinkmanByUnitInfoIdAndServiceId(@Param("unitInfoId") String unitInfoId, @Param("serviceId") String serviceId);
}
