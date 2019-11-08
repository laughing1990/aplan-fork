package com.augurit.aplanmis.supermarket.certinstMajor.service;

import com.augurit.aplanmis.common.domain.AeaImCertinstMajor;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * -Service服务调用接口类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:thinkpad</li>
 * <li>创建时间：2019-06-12 15:59:06</li>
 * </ul>
 */
public interface AeaImCertinstMajorService {
    public void saveAeaImCertinstMajor(AeaImCertinstMajor aeaImCertinstMajor) throws Exception;

    public void updateAeaImCertinstMajor(AeaImCertinstMajor aeaImCertinstMajor) throws Exception;

    public void deleteAeaImCertinstMajorById(String id) throws Exception;

    public PageInfo<AeaImCertinstMajor> listAeaImCertinstMajor(AeaImCertinstMajor aeaImCertinstMajor, Page page) throws Exception;

    public AeaImCertinstMajor getAeaImCertinstMajorById(String id) throws Exception;

    public List<AeaImCertinstMajor> listAeaImCertinstMajor(AeaImCertinstMajor aeaImCertinstMajor) throws Exception;

}
