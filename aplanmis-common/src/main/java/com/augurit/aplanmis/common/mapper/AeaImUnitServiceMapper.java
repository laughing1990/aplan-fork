package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaImUnitService;
import com.augurit.aplanmis.common.domain.AgentUnitService;
import com.augurit.aplanmis.common.vo.ServiceMatterVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AeaImUnitServiceMapper {
    AeaImUnitService getAeaImUnitService(AeaImUnitService aeaImUnitService) throws Exception;

    List<AeaImUnitService> listAeaImUnitService(AeaImUnitService aeaImUnitService) throws Exception;

    void insertAeaImUnitService(AeaImUnitService aeaImUnitService) throws Exception;

    void updateAeaImUnitService(AeaImUnitService aeaImUnitService) throws Exception;

    void deleteAeaImUnitService(@Param("id") String id) throws Exception;

    /*AeaImUnitService getAeaImUnitServiceForm(AeaImUnitService aeaImUnitService) throws Exception;*/

    /**
     * 查询单位下中介服务列表
     */
    List<AgentUnitService> listAgentUnitService(@Param("unitInfoId") String unitInfoId) throws Exception;
//



    AeaImUnitService getUnitServiceByUnitInfoIdAndServiceItemId(@Param("unitInfoId") String unitInfoId, @Param("serviceItemId") String serviceItemId)throws Exception;

    List<AeaImUnitService> listUnitServiceByServiceItemId(@Param("serviceItemId") String serviceItemId)throws Exception;

//
    List<ServiceMatterVo> listAeaImUnitServiceVo(ServiceMatterVo serviceMatterVo) throws Exception;

    AeaImUnitService getAeaImUnitServiceDetail(String unitServiceId) throws Exception;

    AeaImUnitService getAeaImUnitServiceByServiceId(@Param("serviceId") String serviceId, @Param("unitInfoId") String unitInfoId) throws Exception;

    List<AeaImUnitService> getAeaImUnitServiceByServiceIds(@Param("serviceId") String serviceId, @Param("unitInfoId") String unitInfoId) throws Exception;


    ServiceMatterVo getServiceMatterVoByUnitserviceId(@Param("unitServiceId") String unitServiceId);
}
