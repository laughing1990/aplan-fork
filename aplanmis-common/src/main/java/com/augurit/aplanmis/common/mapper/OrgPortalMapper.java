package com.augurit.aplanmis.common.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface OrgPortalMapper {

    long countShiXianJian(@Param("currentOrgId") String currentOrgId, @Param("loginName")String loginName, @Param("instState")String instState);
}
