package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaParStagePartform;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AeaParStagePartformMapper {

     /**
      * 插入
      *
      * @param stagePartform
      */
     void insertStagePartform(AeaParStagePartform stagePartform) ;

     /**
      * 更新
      *
      * @param stagePartform
      */
     void updateStagePartform(AeaParStagePartform stagePartform) ;

     /**
      * 单条获取
      *
      * @param id
      * @return
      */
     AeaParStagePartform getStagePartformById(@Param("id") String id) ;

     /**
      * 通过id删除
      *
      * @param id
      */
     void delStagePartformById(@Param("id") String id) ;

     /**
      * 批量删除
      *
      * @param ids
      * @return
      */
     void batchDelStagePartformByIds(@Param("ids") String[] ids) ;

     /**
      * 获取多条
      *
      * @param stagePartform
      * @return
      */
     List<AeaParStagePartform> listStagePartform(AeaParStagePartform stagePartform) ;

     /**
      * 获取多条
      *
      * @param stagePartform
      * @return
      */
     List<AeaParStagePartform> listStagePartformRelFormInfo(AeaParStagePartform stagePartform) ;

     /**
      * 获取最大排序
      *
      * @param stageId
      * @return
      */
     Long getMaxSortNo(@Param("stageId") String stageId);

     /**
      * 获取未选择的表单
      *
      * @return
      */
     List<AeaParStagePartform> listPartFormNoSelectForm(AeaParStagePartform stagePartform);
}
