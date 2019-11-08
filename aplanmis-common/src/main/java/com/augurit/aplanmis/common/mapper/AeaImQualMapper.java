package com.augurit.aplanmis.common.mapper;
import com.augurit.aplanmis.common.domain.AeaImQual;
import com.augurit.aplanmis.common.vo.AeaImQualVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* -Mapper数据与持久化接口类
*/
@Mapper
@Repository
public interface AeaImQualMapper {

    public void insertAeaImQual(AeaImQual aeaImQual) throws Exception;
    public void updateAeaImQual(AeaImQual aeaImQual) throws Exception;
    public void deleteAeaImQual(@Param("id") String id) throws Exception;
    public List <AeaImQual> listAeaImQual(AeaImQual aeaImQual) throws Exception;
    Integer checkUniqueQualCode(@Param("qualId") String qualId, @Param("qualCode") String qualCode);
    public AeaImQual getAeaImQualById(@Param("id") String id) throws Exception;
    List<AeaImQualVo> listAeaImQualVoByServiceId(@Param("serviceId") String serviceId);
    public List <AeaImQual> listAeaImNotInQual(@Param("qual") AeaImQual aeaImQual, @Param("qualIds") List<String> qualIds) throws Exception;
    public List <AeaImQual> listAeaImInQual(AeaImQual aeaImQual, @Param("qualIds") List<String> qualIds) throws Exception;

    List<AeaImQual> listAeaImQualByQualIds(@Param("qualIds") String[] qualIds);

    List<AeaImQual> listAeaImQualByProjPurchaseId(@Param("projPurchaseId") String projPurchaseId);

}


