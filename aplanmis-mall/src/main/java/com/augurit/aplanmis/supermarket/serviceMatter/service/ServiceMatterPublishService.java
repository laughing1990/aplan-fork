package com.augurit.aplanmis.supermarket.serviceMatter.service;

import com.alibaba.fastjson.JSONObject;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.aplanmis.common.domain.AeaCert;
import com.augurit.aplanmis.common.domain.AeaImServiceMajor;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.vo.AeaHiCertinstBVo;
import com.augurit.aplanmis.common.vo.ServiceMatterVo;
import com.augurit.aplanmis.supermarket.vo.ServiceQualVo;
import com.github.pagehelper.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author chenjw
 * @version v1.0.0
 * @description
 * @date Created in 2019/6/3/003 16:21
 */

public interface ServiceMatterPublishService {


    List<AeaImServiceMajor> getMajorList() throws Exception;

    List<ZtreeNode> getQualLevelList()throws Exception;

    void saveServiceMatter(ServiceMatterVo serviceMatterVo) throws Exception;

    JSONObject getBasicInfo() throws Exception;

    List<AeaLinkmanInfo> listLinkmanByServiceUnitInfoId(String serviceId, String unitInfoId) throws Exception;

    List<ServiceMatterVo> listServiceMatter(ServiceMatterVo serviceMatterVo, Page page) throws Exception;

    AeaHiCertinstBVo saveCertificateInfo(AeaHiCertinstBVo certinstBVo, HttpServletRequest req) throws Exception;

    void updateServiceMatter(ServiceMatterVo serviceMatterVo) throws  Exception;

    AeaHiCertinstBVo updateAeaHicertinstBvo(AeaHiCertinstBVo certinstBVo, HttpServletRequest req) throws  Exception;

    void deleteCertificateInfo(String certinstId) throws Exception;

    List<AeaItemBasic> listItemBasicByServiceId(String serviceId, Page page) throws  Exception;

    List<AeaCert> getCertListByserviceId(String serviceId);

    List<ServiceQualVo>  getMajorTreeNode(String serviceId) throws Exception;

    void deleteCertinstAtt(String attLinkId);

    List<AeaLinkmanInfo> getLinkManList(String unitInfoId, String serviceId) throws Exception;

    List<AeaItemBasic> listItemBasicByServiceIdNoPage(String serviceId)throws Exception;
}
