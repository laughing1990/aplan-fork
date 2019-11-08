package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaCreditRedblack;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* 信用管理-红黑名单管理-Mapper数据与持久化接口类
*/
@Mapper
@Repository
public interface AeaCreditRedblackMapper {

     void insertAeaCreditRedblack(AeaCreditRedblack aeaCreditRedblack);

     void updateAeaCreditRedblack(AeaCreditRedblack aeaCreditRedblack);

     AeaCreditRedblack getAeaCreditRedblackById(@Param("id") String id, @Param("rootOrgId") String rootOrgId);

     AeaCreditRedblack getAeaCreditRedblackRelInfoById(@Param("id") String id, @Param("rootOrgId") String rootOrgId);

     List<AeaCreditRedblack> listAeaCreditRedblack(AeaCreditRedblack aeaCreditRedblack);

     List<AeaCreditRedblack> listAeaCreditRedblackRelInfo(AeaCreditRedblack aeaCreditRedblack);

     void deleteAeaCreditRedblack(@Param("id") String id, @Param("rootOrgId") String rootOrgId);

     void enOrDisableIsValid(@Param("id") String id);
}
