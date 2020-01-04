package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaParFactor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 主题导航因子表-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaParFactorMapper {

    void insertAeaParFactor(AeaParFactor aeaParFactor);

    void updateAeaParFactor(AeaParFactor aeaParFactor);

    void deleteAeaParFactor(@Param("id") String id, @Param("rootOrgId") String rootOrgId);

    List<AeaParFactor> listAeaParFactor(AeaParFactor aeaParFactor);

    List<AeaParFactor> listAeaParFactorWithTheme(AeaParFactor aeaParFactor);

    AeaParFactor getAeaParFactorById(@Param("id") String id, @Param("rootOrgId") String rootOrgId);

    AeaParFactor getAeaParFactorWithThemeById(@Param("id") String id, @Param("rootOrgId") String rootOrgId);

    Long getMaxSortNo(@Param("id") String id, @Param("rootOrgId") String rootOrgId);

    void batchDelSelfAndAllChildFactorById(@Param("id")String id, @Param("rootOrgId") String rootOrgId);

    List<AeaParFactor> listSelfAndAllChildAeaParFactor(@Param("id") String id, @Param("rootOrgId") String rootOrgId);

    AeaParFactor getAeaParFactorByFactorId(@Param("id") String id);
}
