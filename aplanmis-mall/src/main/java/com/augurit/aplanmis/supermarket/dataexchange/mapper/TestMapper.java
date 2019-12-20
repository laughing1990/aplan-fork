package com.augurit.aplanmis.supermarket.dataexchange.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Mapper
@Repository
public interface TestMapper {

    @Insert("insert into aea_unit_info(UNIT_INFO_ID, APPLICANT,CREATE_TIME) values(#{unitInfoId}, #{applicant},#{createTime}) ")
    int insertOne(@Param("unitInfoId") String unitInfoId, @Param("applicant") String applicant, @Param("createTime") Date createTime);
}
