package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaParFrontPartform;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 阶段的扩展表单前置检测表-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaParFrontPartformMapper {

    void insertAeaParFrontPartform(AeaParFrontPartform aeaParFrontPartform) ;

    void updateAeaParFrontPartform(AeaParFrontPartform aeaParFrontPartform) ;

    void deleteAeaParFrontPartform(@Param("id") String id) ;

    List<AeaParFrontPartform> listAeaParFrontPartform(AeaParFrontPartform aeaParFrontPartform) ;

    AeaParFrontPartform getAeaParFrontPartformById(@Param("id") String id) ;

    List<AeaParFrontPartform> listAeaParFrontPartformVo(AeaParFrontPartform aeaParFrontPartform) ;

    Long getMaxSortNo(@Param("stageId") String stageId, @Param("rootOrgId") String rootOrgId);

    List<AeaParFrontPartform> listSelectParFrontPartform(AeaParFrontPartform aeaParFrontPartform) ;

    AeaParFrontPartform getAeaParFrontPartformVoById(@Param("id") String id) ;

    List<AeaParFrontPartform> getAeaParFrontPartformVoByStageId(@Param("stageId") String stageId, @Param("rootOrgId") String rootOrgId);

    void changIsActive(@Param("id") String id, @Param("rootOrgId") String rootOrgId);
}
