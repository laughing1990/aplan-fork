package com.augurit.aplanmis.common.service.instance;

import com.augurit.aplanmis.common.domain.AeaItemInout;

import java.util.List;

public interface AeaHiItemInoutService {

    /**
     * 根据事项版本ID查询结果物接口
     * @param itemVerId 事项版本ID
     * @param rootOrgId
     * @return
     */
    List<AeaItemInout> getAeaItemInoutMatCertByItemVerId(String itemVerId, String rootOrgId);
}
