package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaItemAgencyAux;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AeaItemAgencyAuxMapper {

    public void insertAeaItemAgencyAux(AeaItemAgencyAux aeaItemAgencyAux);

    public void deleteAeaItemAgencyAux(@Param("agencyAuxId") String agencyAuxId);

    public void batchDeleteAeaItemAgencyAux(@Param("agencyAuxIds") String[] agencyAuxIds);

    public void updateAeaItemAgencyAux(AeaItemAgencyAux aeaItemAgencyAux);

    public List<AeaItemAgencyAux> listAeaItemAgencyAux(AeaItemAgencyAux aeaItemAgencyAux);

    public AeaItemAgencyAux getAeaItemAgencyAuxById(@Param("agencyAuxId") String agencyAuxId);

    public List<AeaItemAgencyAux> getAeaItemAgencyAuxByAgencyId(@Param("agencyId") String agencyId);
}
