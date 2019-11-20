package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaParFrontItemform;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 阶段事项表单前置检测表-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaParFrontItemformMapper {

    void insertAeaParFrontItemform(AeaParFrontItemform aeaParFrontItemform) ;

    void updateAeaParFrontItemform(AeaParFrontItemform aeaParFrontItemform) ;

    void deleteAeaParFrontItemform(@Param("id") String id) ;

    List<AeaParFrontItemform> listAeaParFrontItemform(AeaParFrontItemform aeaParFrontItemform) ;

    AeaParFrontItemform getAeaParFrontItemformById(@Param("id") String id) ;

    List<AeaParFrontItemform> listAeaParFrontItemformVo(AeaParFrontItemform aeaParFrontItemform) ;

    Long getMaxSortNo(@Param("stageId") String stageId, @Param("rootOrgId") String rootOrgId);

    List<AeaParFrontItemform> listSelectParFrontItemform(AeaParFrontItemform aeaParFrontItemform) ;

    AeaParFrontItemform getAeaParFrontItemformVoById(@Param("id") String id) ;

    List<AeaParFrontItemform> getAeaParFrontItemformVoByStageId(@Param("stageId") String stageId, @Param("rootOrgId") String rootOrgId);

    void changIsActive(@Param("id") String id, @Param("rootOrgId") String rootOrgId);
}
