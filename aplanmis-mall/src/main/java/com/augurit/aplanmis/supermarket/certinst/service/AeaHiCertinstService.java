package com.augurit.aplanmis.supermarket.certinst.service;

import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.aplanmis.common.domain.AeaHiCertinst;
import com.augurit.aplanmis.common.domain.AeaImService;
import com.augurit.aplanmis.common.domain.AeaImServiceQual;
import com.augurit.aplanmis.common.vo.AeaHiCertinstBVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 电子证照实例表-Service服务调用接口类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:thinkpad</li>
 * <li>创建时间：2019-06-13 19:52:53</li>
 * </ul>
 */
public interface AeaHiCertinstService {
    public void saveAeaHiCertinst(AeaHiCertinstBVo aeaHiCertinst) throws Exception;

    public void updateAeaHiCertinst(AeaHiCertinst aeaHiCertinst) throws Exception;

    public boolean deleteAeaHiCertinstById(String certinstId) throws Exception;

    public boolean deleteContractFile(String detailId) throws Exception;

    public PageInfo<AeaHiCertinst> listAeaHiCertinst(AeaHiCertinst aeaHiCertinst, Page page) throws Exception;

    public AeaHiCertinst getAeaHiCertinstById(String id) throws Exception;

    public List<AeaHiCertinst> listAeaHiCertinst(AeaHiCertinst aeaHiCertinst) throws Exception;

    public PageInfo<AeaHiCertinst> getAeaImMajorLevelAndCertinstByServiceId(String serviceId, String unitInfoId, Page page) throws Exception;

    List<ZtreeNode> getMajorTreeByQualId(String qualId) throws Exception;

    public List<AeaImServiceQual> listAeaImServiceQual(String serviceId) throws Exception;

    public List<AeaImService> listAeaImService() throws Exception;

    public List<AeaHiCertinstBVo> listCertinstLibrary(String type, String typeId, Page page) throws Exception;

    public boolean deleteCertinst(String certinstId) throws Exception;
}
