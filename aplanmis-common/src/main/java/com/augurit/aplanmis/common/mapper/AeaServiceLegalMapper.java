package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaServiceLegal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 法律法规-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaServiceLegalMapper {

     void insertAeaServiceLegal(AeaServiceLegal aeaServiceLegal) ;

     void updateAeaServiceLegal(AeaServiceLegal aeaServiceLegal) ;

     void deleteAeaServiceLegal(@Param("id") String id) ;

     List<AeaServiceLegal> listAeaServiceLegal(AeaServiceLegal aeaServiceLegal) ;

     AeaServiceLegal getAeaServiceLegalById(@Param("id") String id) ;

     List<AeaServiceLegal> listOtherLegalByLegalId(@Param("id") String id, @Param("rootOrgId") String rootOrgId);

     List<AeaServiceLegal> listAllRelChildLegal(@Param("id")String id, @Param("rootOrgId")String rootOrgId);
}
