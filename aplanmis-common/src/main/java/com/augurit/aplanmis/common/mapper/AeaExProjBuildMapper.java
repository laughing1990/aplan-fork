package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaExProjBuild;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AeaExProjBuildMapper {
    int insertAeaExProjBuild(AeaExProjBuild aeaExProjBuild);
    int updateAeaExProjBuild(AeaExProjBuild aeaExProjBuild);
    AeaExProjBuild getAeaExProjBuildByProjId(@Param("projInfoId") String projInfoId);
}
