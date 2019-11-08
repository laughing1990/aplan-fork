package com.augurit.aplanmis.admin.market.unit.service;

import com.augurit.aplanmis.common.domain.AeaImUnitRequire;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
* -Service服务调用接口类
*/
public interface AeaImUnitRequireService {

    public void saveAeaImUnitRequire(AeaImUnitRequire aeaImUnitRequire) throws Exception;
    public void updateAeaImUnitRequire(AeaImUnitRequire aeaImUnitRequire) throws Exception;
    public void deleteAeaImUnitRequireById(String id) throws Exception;
    public PageInfo<AeaImUnitRequire> listAeaImUnitRequire(AeaImUnitRequire aeaImUnitRequire, Page page) throws Exception;
    public AeaImUnitRequire getAeaImUnitRequireById(String id) throws Exception;
    public List<AeaImUnitRequire> listAeaImUnitRequire(AeaImUnitRequire aeaImUnitRequire) throws Exception;

    public AeaImUnitRequire getAeaImUnitRequireByItemVerId(String itemVerId) throws Exception;
}
