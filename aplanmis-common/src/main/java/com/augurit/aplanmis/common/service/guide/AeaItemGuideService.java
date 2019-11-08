package com.augurit.aplanmis.common.service.guide;

import com.augurit.aplanmis.common.domain.AeaItemGuide;



public interface AeaItemGuideService {
    /**
     * 根据事项版本ID查找单项的办事指南
     * @param itemVerId
     * @return
     * @throws Exception
     */
    AeaItemGuide getAeaItemGuideListByItemVerId(String itemVerId,String topOrgId) throws Exception;
}
