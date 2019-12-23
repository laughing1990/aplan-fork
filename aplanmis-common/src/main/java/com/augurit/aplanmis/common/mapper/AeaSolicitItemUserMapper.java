package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaSolicitItemUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* 按事项征求的人员名单表-Mapper数据与持久化接口类
*/
@Mapper
@Repository
public interface AeaSolicitItemUserMapper {

     void insertAeaSolicitItemUser(AeaSolicitItemUser aeaSolicitItemUser) ;
     void updateAeaSolicitItemUser(AeaSolicitItemUser aeaSolicitItemUser) ;
     void deleteAeaSolicitItemUser(@Param("id") String id) ;
     List<AeaSolicitItemUser> listAeaSolicitItemUser(AeaSolicitItemUser aeaSolicitItemUser) ;
     AeaSolicitItemUser getAeaSolicitItemUserById(@Param("id") String id) ;

     /**
      * 通过事项版本id获取关联的用户表数据
      *
      * @param itemVerId
      * @param rootOrgId
      * @return
      */
     List<AeaSolicitItemUser> listSolicitItemUserByItemVerId(@Param("itemVerId") String itemVerId, @Param("rootOrgId") String rootOrgId) ;
}
