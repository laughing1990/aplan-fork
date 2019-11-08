package com.augurit.aplanmis.common.service.guide;

import com.augurit.aplanmis.common.domain.AeaItemGuideCharges;

import java.util.List;

public interface AeaItemGuideChargesService {
    /**
     *  根据事项版本itemVerId查询收费项目
     * @param itemVerId
     * @return
     */
    List<AeaItemGuideCharges> getAeaItemGuideChargesListByItemVerId(String itemVerId) throws Exception;
}
