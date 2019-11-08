package com.augurit.aplanmis.admin.market.item.service;

import com.augurit.aplanmis.common.domain.AeaImItemExplain;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
* -Service服务调用接口类
*/
public interface AeaImItemExplainService {
    public void saveAeaImItemExplain(AeaImItemExplain aeaImItemExplain) throws Exception;
    public void updateAeaImItemExplain(AeaImItemExplain aeaImItemExplain) throws Exception;
    public void deleteAeaImItemExplainById(String id) throws Exception;
    public PageInfo<AeaImItemExplain> listAeaImItemExplain(AeaImItemExplain aeaImItemExplain, Page page) throws Exception;
    public AeaImItemExplain getAeaImItemExplainById(String id) throws Exception;
    public List<AeaImItemExplain> listAeaImItemExplain(AeaImItemExplain aeaImItemExplain) throws Exception;

    public AeaImItemExplain getAeaImItemExplainByItemVerId(String itemVerId) throws Exception;

}
