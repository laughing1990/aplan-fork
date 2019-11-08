package com.augurit.aplanmis.supermarket.cert.service;

import com.augurit.aplanmis.common.domain.AeaCert;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 电子证照定义表-Service服务调用接口类
 */
public interface AeaCertService {
    public void saveAeaCert(AeaCert aeaCert) throws Exception;

    public void updateAeaCert(AeaCert aeaCert) throws Exception;

    public void deleteAeaCertById(String id) throws Exception;

    public PageInfo<AeaCert> listAeaCert(AeaCert aeaCert, Page page) throws Exception;

    public List<AeaCert> listAeaCert(AeaCert aeaCert) throws Exception;

}
