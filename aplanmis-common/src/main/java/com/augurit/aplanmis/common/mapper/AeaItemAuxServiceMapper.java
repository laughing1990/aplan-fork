package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaItemAuxService;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AeaItemAuxServiceMapper {

    public void insertAeaItemAuxService(AeaItemAuxService aeaItemAuxService);

    public void deleteAeaItemAuxService(@Param("auxServiceId") String auxServiceId);

    public void updateAeaItemAuxService(AeaItemAuxService aeaItemAuxService);

    public List<AeaItemAuxService> listAeaItemAuxService(AeaItemAuxService aeaItemAuxService);

    public AeaItemAuxService getAeaItemAuxServiceById(@Param("auxServiceId") String auxServiceId);
}
