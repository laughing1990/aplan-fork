package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaImBiddingEmployees;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 企业报价联系人表-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaImBiddingEmployeesMapper {

    void insertAeaImBiddingEmployeesList(List<AeaImBiddingEmployees> aeaImBiddingEmployeesList);

    List<AeaImBiddingEmployees> listAeaImBiddingEmployeesByUnitBiddingId(@Param("unitBiddingId") String unitBiddingId);
}
