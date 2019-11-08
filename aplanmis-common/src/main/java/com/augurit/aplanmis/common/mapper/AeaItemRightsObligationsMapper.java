package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaItemRightsObligations;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AeaItemRightsObligationsMapper {

    public void insertAeaItemRightsObligations(AeaItemRightsObligations aeaItemRightsObligations);

    public void deleteAeaItemRightsObligations(@Param("rightObliId") String rightObliId);

    public void batchDeleteAeaItemRightsObligations(@Param("rightObliIds") String[] rightObliIds);

    public void updateAeaItemRightsObligations(AeaItemRightsObligations aeaItemRightsObligations);

    public List<AeaItemRightsObligations> listAeaItemRightsObligations(AeaItemRightsObligations aeaItemRightsObligations);

    public List<AeaItemRightsObligations> getAeaItemRightsObligationsByItemId(String itemId);
}
