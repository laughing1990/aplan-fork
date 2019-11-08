package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaParNav;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 主题导航定义表-Mapper数据与持久化接口类
 */
@Mapper
public interface AeaParNavMapper {

    void insertAeaParNav(AeaParNav aeaParNav);

    void updateAeaParNav(AeaParNav aeaParNav);

    void deleteAeaParNav(@Param("id") String id, @Param("rootOrgId") String rootOrgId);

    List<AeaParNav> listAeaParNav(AeaParNav aeaParNav);

    AeaParNav getAeaParNavById(@Param("id") String id, @Param("rootOrgId") String rootOrgId);

    Long getMaxSortNo(@Param("rootOrgId") String rootOrgId);

    void changIsActiveState(@Param("id") String id, @Param("rootOrgId") String rootOrgId);
}
