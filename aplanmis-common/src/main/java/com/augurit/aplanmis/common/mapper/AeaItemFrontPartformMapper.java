package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaItemFrontPartform;
import com.augurit.aplanmis.common.vo.AeaItemFrontPartformVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 事项的前置检查事项-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaItemFrontPartformMapper {

     void insertAeaItemFrontPartform(AeaItemFrontPartform aeaItemFrontPartform) ;
     void updateAeaItemFrontPartform(AeaItemFrontPartform aeaItemFrontPartform) ;
     void deleteAeaItemFrontPartform(@Param("id") String id) ;
     List <AeaItemFrontPartform> listAeaItemFrontPartform(AeaItemFrontPartform aeaItemFrontPartform) ;
     AeaItemFrontPartform getAeaItemFrontPartformById(@Param("id") String id) ;

     List<AeaItemFrontPartformVo> listAeaItemFrontPartformVo(AeaItemFrontPartform aeaItemFrontPartform) ;

     Long getMaxSortNo(AeaItemFrontPartform aeaItemFrontPartform) ;

     List<AeaItemFrontPartformVo> listSelectItemFrontPartform(AeaItemFrontPartform aeaItemFrontPartform) ;

     AeaItemFrontPartformVo getAeaItemFrontPartformVoById(@Param("id") String id) ;
}
