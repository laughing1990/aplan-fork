package com.augurit.aplanmis.admin.market.buscert.service;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.aplanmis.common.domain.AeaBusCert;
import com.augurit.aplanmis.common.domain.AeaCert;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
* -Service服务调用接口类
*/
public interface AeaBusCertService {
    public void saveAeaBusCert(AeaBusCert aeaBusCert) throws Exception;
    public void updateAeaBusCert(AeaBusCert aeaBusCert) throws Exception;
    public void deleteAeaBusCertById(String id) throws Exception;
    public PageInfo<AeaBusCert> listAeaBusCert(AeaBusCert aeaBusCert, Page page) throws Exception;
    public AeaBusCert getAeaBusCertById(String id) throws Exception;
    public List<AeaBusCert> listAeaBusCert(AeaBusCert aeaBusCert) throws Exception;

    PageInfo<AeaCert> listAeaCert(AeaCert aeaCert, Page page, String tableName)throws Exception;

    public void saveRelation(String[] saveCertIds, String busRecordId, String tableName) throws Exception;

    public void cancelRelation(String[] cancelCertIds, String busRecordId, String tableName) throws Exception;

    public ContentResultForm<List> getSavedCert(AeaBusCert aeaBusCert, String tableName) throws Exception;
}
