package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaItemUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* 用户事项管理-Mapper数据与持久化接口类
*/
@Mapper
@Repository
public interface AeaItemUserMapper {

     void insertAeaItemUser(AeaItemUser aeaItemUser);

     void updateAeaItemUser(AeaItemUser aeaItemUser);

     void deleteAeaItemUser(@Param("id") String id);

     void batchDelItemUserByArrIds(@Param("ids") String[] ids);

     void batchDelItemUserByListIds(@Param("ids") List<String> ids);

     List <AeaItemUser> listAeaItemUser(AeaItemUser aeaItemUser);

     List<AeaItemUser> listUserItemRelItemInfo(AeaItemUser aeaItemUser);

     AeaItemUser getAeaItemUserById(@Param("id") String id);

     void batchDelItemByUserId(@Param("userId") String userId, @Param("rootOrgId") String rootOrgId);

     Long getMaxSortNo(@Param("rootOrgId") String rootOrgId);

     void changIsActive(@Param("id")String id, @Param("rootOrgId")String rootOrgId);
}
