package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaItemAgency;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AeaItemAgencyMapper {

    public void insertAeaItemAgency(AeaItemAgency aeaItemAgency);

    public void deleteAeaItemAgency(@Param("agencyId") String agencyId);

    public void batchDeleteAeaItemAgency(@Param("agencyIds") String[] agencyIds);

    public void updateAeaItemAgency(AeaItemAgency aeaItemAgency);

    public List<AeaItemAgency> listAeaItemAgency(AeaItemAgency aeaItemAgency);

    public AeaItemAgency getAeaItemAgencyById(@Param("agencyId") String agencyId);

    public AeaItemAgency getAeaItemAgencyByItemId(@Param("itemId") String itemId);
}
