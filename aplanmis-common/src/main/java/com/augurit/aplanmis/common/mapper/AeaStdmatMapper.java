package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaStdmat;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 标准材料定义表-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaStdmatMapper {

     void insertAeaStdmat(AeaStdmat aeaStdmat) ;

     void updateAeaStdmat(AeaStdmat aeaStdmat) ;

     void deleteAeaStdmat(@Param("id") String id) ;

     List <AeaStdmat> listAeaStdmat(AeaStdmat aeaStdmat) ;

     AeaStdmat getAeaStdmatById(@Param("id") String id) ;

     /**
      * 获取最大排序号
      *
      * @param rootOrgId
      * @return
      */
     Long getMaxSortNo(@Param("rootOrgId") String rootOrgId);

     /**
      * 校验唯一编号
      *
      * @param id
      * @param stdmatCode
      * @param rootOrgId
      * @return
      */
     Long checkUniqueCode(@Param("id")String id, @Param("stdmatCode") String stdmatCode, @Param("rootOrgId")String rootOrgId);
}
