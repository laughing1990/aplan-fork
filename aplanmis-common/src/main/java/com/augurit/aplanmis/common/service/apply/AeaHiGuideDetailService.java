package com.augurit.aplanmis.common.service.apply;

import com.augurit.aplanmis.common.domain.AeaHiGuideDetail;

import java.util.List;

public interface AeaHiGuideDetailService {

    void deleteByGuideDetailId(String guideDetailId);

    void insertAeaHiGuideDetail(AeaHiGuideDetail record);

    AeaHiGuideDetail getAeaHiGuideDetailByGuideDetailId(String guideDetailId);

    void updateAeaHiGuideDetail(AeaHiGuideDetail record);

    void batchInsertAeaHiGuideDetail(List<AeaHiGuideDetail> list);

    List<AeaHiGuideDetail> queryGuideDetailByGuideIdAndDetailType(String guideId, String detailType);

    List<AeaHiGuideDetail> listAeaHiGuideDetail(AeaHiGuideDetail aeaHiGuideDetail);
}
