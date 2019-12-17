package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaStdmatType;
import com.augurit.aplanmis.common.mapper.base.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 材料类型表-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaStdmatTypeMapper extends BaseMapper<AeaStdmatType> {

     /**
      * 查询
      *
      * @param aeaStdmatType
      * @return
      */
     List<AeaStdmatType> listAeaStdmatType(AeaStdmatType aeaStdmatType);

     /**
      * 获取最大排序号
      *
      * @param rootOrgId
      * @return
      */
     Long getMaxSortNo(@Param("rootOrgId") String rootOrgId);

     /**
      * 批量删除自己和子集数据
      *
      * @param id
      * @param rootOrgId
      */
     void deleteSelfAndAllChildById(@Param("id")String id, @Param("rootOrgId")String rootOrgId);

     /**
      * 获取除当前分类下的所有其他分类
      *
      * @param id
      * @param rootOrgId
      * @return
      */
     List<AeaStdmatType> listOtherStdMatTypesById(@Param("id")String id, @Param("rootOrgId")String rootOrgId);

     /**
      * 获取当前分类下的所有子集
      *
      * @param id
      * @param rootOrgId
      * @return
      */
     List<AeaStdmatType> listAllRelChildStdMatType(@Param("id")String id, @Param("rootOrgId")String rootOrgId);

     /**
      * 校验唯一编号
      *
      * @param id
      * @param typeCode
      * @param rootOrgId
      * @return
      */
     Long checkUniqueTypeCode(@Param("id")String id, @Param("typeCode") String typeCode, @Param("rootOrgId")String rootOrgId);
}
