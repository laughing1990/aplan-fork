package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaSolicitOrgUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* 按组织征求的人员名单表-Mapper数据与持久化接口类
*/
@Mapper
@Repository
public interface AeaSolicitOrgUserMapper {

     void insertAeaSolicitOrgUser(AeaSolicitOrgUser aeaSolicitOrgUser) ;

     void updateAeaSolicitOrgUser(AeaSolicitOrgUser aeaSolicitOrgUser) ;

     void deleteAeaSolicitOrgUser(@Param("id") String id) ;

     void batchDelSolicitOrgUserByIds(@Param("ids") String[] ids);

     void batchDelSolicitOrgUserBySolicitOrgId(@Param("solicitOrgId") String solicitOrgId);

     AeaSolicitOrgUser getAeaSolicitOrgUserById(@Param("id") String id) ;

     List<AeaSolicitOrgUser> listAeaSolicitOrgUser(AeaSolicitOrgUser aeaSolicitOrgUser) ;

     List<AeaSolicitOrgUser> listAeaSolicitOrgUserRelInfo(AeaSolicitOrgUser aeaSolicitOrgUser) ;

     List<AeaSolicitOrgUser> listAeaSolicitOrgUserByOrgId(@Param("orgId") String orgId, @Param("rootOrgId") String rootOrgId) ;

     Long getMaxSortNo();

     void changeIsActive(@Param("id") String id) ;
}
