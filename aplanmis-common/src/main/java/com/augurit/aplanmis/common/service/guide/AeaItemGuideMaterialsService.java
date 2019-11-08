package com.augurit.aplanmis.common.service.guide;

import com.augurit.aplanmis.common.domain.AeaItemGuideMaterials;

import java.util.List;

public interface AeaItemGuideMaterialsService {
    /**
     * 根据事项版本ID查询中介服务
     * @param itemVerId 必须参数 事项版本ID
     * @return
     */
    public List<AeaItemGuideMaterials> listAeaitemGuideMaterials(String itemVerId) throws Exception;
}
