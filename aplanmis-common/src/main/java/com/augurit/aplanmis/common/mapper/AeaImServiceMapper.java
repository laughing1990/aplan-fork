package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaImService;
import com.augurit.aplanmis.common.vo.AeaImServiceVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * -Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaImServiceMapper {

    void insertAeaImService(AeaImService aeaService) throws Exception;

    void updateAeaImService(AeaImService aeaService) throws Exception;

    void deleteAeaImService(@Param("id") String id) throws Exception;

    void deleteAeaImServiceQual(@Param("id") String id) throws Exception;

    List<AeaImService> listAeaImService(AeaImService aeaService) throws Exception;

    AeaImService getAeaImServiceById(@Param("id") String id) throws Exception;

    Integer checkUniqueServiceCode(@Param("serviceId") String serviceId, @Param("serviceCode") String serviceCode);

    void changIsActiveState(@Param("id") String id);

    List<AeaImServiceVo> listAeaImServiceVoByItemVerId(@Param("itemVerId") String itemVerId);

    AeaImService getAeaImServiceByServiceItemId(@Param("serviceItemId") String serviceItemId) throws Exception;

    List<AeaImService> listAeaImServiceNoPageByItemVerId(@Param("itemVerId") String itemVerId);

    List<AeaImService> listAeaImServiceNoPageByProjPurchaseId(@Param("projPurchaseId") String projPurchaseId);

    List<AeaImService> getAeaImServiceAllList() throws Exception;;
}

