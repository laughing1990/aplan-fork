package com.augurit.aplanmis.common.service.guide;

import com.augurit.aplanmis.common.domain.AeaItemGuideExtend;

public interface AeaItemGuideExtendService {
    /**
     *  根据事项版本ID查询扩展字段信息
     * @param itemVerId 必须参数 事项版本ID
     * @return
     */
    public AeaItemGuideExtend getAeaItemGuideExtendByItemVerId(String itemVerId) throws Exception;
}
