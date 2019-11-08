package com.augurit.aplanmis.admin.market.qual.service;

import com.augurit.aplanmis.common.domain.AeaImQualLevel;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
* -Service服务调用接口类
*/
public interface AeaImQualLevelService {
    public void saveAeaImQualLevel(AeaImQualLevel aeaImQualLevel) throws Exception;
    public void updateAeaImQualLevel(AeaImQualLevel aeaImQualLevel) throws Exception;
    public void deleteAeaImQualLevelById(String id) throws Exception;
    public PageInfo<AeaImQualLevel> listAeaImQualLevel(AeaImQualLevel aeaImQualLevel, Page page) throws Exception;
    public AeaImQualLevel getAeaImQualLevelById(String id) throws Exception;
    public List<AeaImQualLevel> listAeaImQualLevel(AeaImQualLevel aeaImQualLevel) throws Exception;
}
