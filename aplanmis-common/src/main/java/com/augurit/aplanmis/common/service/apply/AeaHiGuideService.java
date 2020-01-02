package com.augurit.aplanmis.common.service.apply;

import com.augurit.aplanmis.common.domain.AeaHiGuide;

import java.util.List;

public interface AeaHiGuideService {


    void deleteAeaHiGuideByGuideId(String guideId);

    void insertAeaHiGuide(AeaHiGuide record);

    AeaHiGuide getAeaHiGuideByGuideId(String guideId);

    void updateAeaHiGuide(AeaHiGuide record);

    void batchInsertAeaHiGuide(List<AeaHiGuide> list);

}
