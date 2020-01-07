package com.augurit.aplanmis.supermarket.dataexchange.mapper;

import com.augurit.aplanmis.common.domain.AeaImService;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.domain.AeaUnitLinkman;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Mapper
@Repository
public interface DataExchangeMapper {

    @Insert("insert into aea_unit_info(UNIT_INFO_ID, APPLICANT,CREATE_TIME) values(#{unitInfoId}, #{applicant},#{createTime}) ")
    int insertOne(@Param("unitInfoId") String unitInfoId, @Param("applicant") String applicant, @Param("createTime") Date createTime);

    //清空服务类型表
    int batchDeleteAeaImService();

    //清空单位和联系人关联表
    int batchDeleteAeaUnitLinkman();

    //清空单位信息表
    int batchDeleteAeaUnitInfo();

    //清空联系人表
    int batchDaleteAeaLinkmanInfo();

    //批量插入服务类型
    void batchInsertAeaImService(@Param("list") List<AeaImService> imServices) throws Exception;

    //批量插入单位信息
    int batchInsertAeaUnitInfo(@Param("list") List<AeaUnitInfo> unitInfos);

    //批量插入联系人信息
    int batchInsertAeaLinkmanInfo(@Param("list") List<AeaLinkmanInfo> linkmanInfos);

    //批量插入单位和联系人关联
    int batchInsertAeaUnitLinkman(@Param("list") List<AeaUnitLinkman> unitLinkmen);

    //获取所有服务类型
    List<AeaImService> getAllImServices() throws Exception;

    //获取所有单位信息
    List<AeaUnitInfo> getAllImUnitInfos() throws Exception;

    //获取所有联系人信息
    List<AeaLinkmanInfo> getAllLinkmanInfos() throws Exception;

    //获取所有单位和联系人关联信息
    List<AeaUnitLinkman> getAllUnitLinkmanData() throws Exception;
}
