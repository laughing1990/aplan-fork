package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaItemServiceCharge;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 办理收费-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaItemServiceChargeMapper {

    public void insertAeaItemServiceCharge(AeaItemServiceCharge aeaItemServiceCharge) throws Exception;

    public void updateAeaItemServiceCharge(AeaItemServiceCharge aeaItemServiceCharge) throws Exception;

    public void deleteAeaItemServiceCharge(@Param("id") String id) throws Exception;

    public List<AeaItemServiceCharge> listAeaItemServiceCharge(AeaItemServiceCharge aeaItemServiceCharge) throws Exception;

    public AeaItemServiceCharge getAeaItemServiceChargeById(@Param("id") String id) throws Exception;

    public AeaItemServiceCharge getAeaItemServiceChargeByItemId(@Param("itemId") String itemId);
}
