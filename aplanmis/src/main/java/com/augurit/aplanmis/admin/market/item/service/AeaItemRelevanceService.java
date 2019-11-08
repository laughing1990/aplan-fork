package com.augurit.aplanmis.admin.market.item.service;

import com.augurit.aplanmis.common.domain.AeaItemRelevance;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
* -Service服务调用接口类
*/
public interface AeaItemRelevanceService {
    public void saveAeaItemRelevance(AeaItemRelevance aeaItemRelevance) throws Exception;
    public void updateAeaItemRelevance(AeaItemRelevance aeaItemRelevance) throws Exception;
    public void deleteAeaItemRelevanceById(String id) throws Exception;
    public PageInfo<AeaItemRelevance> listAeaItemRelevance(AeaItemRelevance aeaItemRelevance, Page page) throws Exception;
    public AeaItemRelevance getAeaItemRelevanceById(String id) throws Exception;
    public List<AeaItemRelevance> listAeaItemRelevance(AeaItemRelevance aeaItemRelevance) throws Exception;

}
