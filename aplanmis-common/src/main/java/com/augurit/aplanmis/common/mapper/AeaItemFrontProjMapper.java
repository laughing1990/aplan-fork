package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaItemFrontProj;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* 项目信息前置检测-Mapper数据与持久化接口类
*/
@Mapper
@Repository
public interface AeaItemFrontProjMapper {

     void insertAeaItemFrontProj(AeaItemFrontProj aeaItemFrontProj) ;

     void updateAeaItemFrontProj(AeaItemFrontProj aeaItemFrontProj) ;

     void deleteAeaItemFrontProj(@Param("id") String id) ;

     List <AeaItemFrontProj> listAeaItemFrontProj(AeaItemFrontProj aeaItemFrontProj) ;

     AeaItemFrontProj getAeaItemFrontProjById(@Param("id") String id) ;
}
