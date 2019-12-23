package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaSolicitOrg;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* 按组织征求配置表-Mapper数据与持久化接口类
*/
@Mapper
@Repository
public interface AeaSolicitOrgMapper {

     void insertAeaSolicitOrg(AeaSolicitOrg aeaSolicitOrg) ;
     void updateAeaSolicitOrg(AeaSolicitOrg aeaSolicitOrg) ;
     void deleteAeaSolicitOrg(@Param("id") String id) ;
     List <AeaSolicitOrg> listAeaSolicitOrg(AeaSolicitOrg aeaSolicitOrg) ;
     AeaSolicitOrg getAeaSolicitOrgById(@Param("id") String id) ;
}
