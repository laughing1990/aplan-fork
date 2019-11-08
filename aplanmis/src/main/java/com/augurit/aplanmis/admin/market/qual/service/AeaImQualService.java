package com.augurit.aplanmis.admin.market.qual.service;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.domain.AeaImQual;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
* -Service服务调用接口类
*/
public interface AeaImQualService {
    public void saveAeaImQual(AeaImQual aeaImQual) throws Exception;
    public void updateAeaImQual(AeaImQual aeaImQual) throws Exception;
    public void deleteAeaImQualById(String id) throws Exception;
    public PageInfo<AeaImQual> listAeaImQual(AeaImQual aeaImQual, Page page) throws Exception;
    //查询不在指定资质Id范围的资质数据列表
    public PageInfo<AeaImQual> listAeaImNotInQual(AeaImQual aeaImQual, List<String> qualId, Page page) throws Exception;
    //查询指定资质Id范围的资质数据列表
    public PageInfo<AeaImQual> listAeaImInQual(AeaImQual aeaImQual, List<String> qualId, Page page) throws Exception;
    public AeaImQual getAeaImQualById(String id) throws Exception;
    public List<AeaImQual> listAeaImQual(AeaImQual aeaImQual) throws Exception;

    boolean checkUniqueQualCode(String qualId, String qualCode);
    void batchDeleteQual(String[] ids) throws Exception;

    ResultForm saveQualLevelCfg(String qualId, String qualLevelId) throws Exception;

    ContentResultForm getQualIdsByItemVerId(String itemVerId) throws Exception;

    List<AeaImQual> getQualNamesByIds(String idSeq) throws Exception;
}
