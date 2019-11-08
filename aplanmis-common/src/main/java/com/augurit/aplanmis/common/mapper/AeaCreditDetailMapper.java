package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaCreditDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 信用管理-信用汇总子表（字段列表）-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaCreditDetailMapper {

     void insertAeaCreditDetail(AeaCreditDetail aeaCreditDetail) ;

     void updateAeaCreditDetail(AeaCreditDetail aeaCreditDetail) ;

     void deleteAeaCreditDetail(@Param("id") String id) ;

     List <AeaCreditDetail> listAeaCreditDetail(AeaCreditDetail aeaCreditDetail) ;

     AeaCreditDetail getAeaCreditDetailById(@Param("id") String id) ;
}
