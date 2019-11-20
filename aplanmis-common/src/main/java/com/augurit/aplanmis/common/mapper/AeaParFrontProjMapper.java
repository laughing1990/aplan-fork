package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaParFrontProj;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 阶段的项目信息前置检测-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaParFrontProjMapper {

    void insertAeaParFrontProj(AeaParFrontProj aeaParFrontProj) ;

    void updateAeaParFrontProj(AeaParFrontProj aeaParFrontProj) ;

    void deleteAeaParFrontProj(@Param("id") String id) ;

    List<AeaParFrontProj> listAeaParFrontProj(AeaParFrontProj aeaParFrontProj) ;

    AeaParFrontProj getAeaParFrontProjById(@Param("id") String id) ;

    Long getMaxSortNo(AeaParFrontProj aeaParFrontProj);

    void changIsActive(@Param("id") String id, @Param("rootOrgId") String rootOrgId);
}
