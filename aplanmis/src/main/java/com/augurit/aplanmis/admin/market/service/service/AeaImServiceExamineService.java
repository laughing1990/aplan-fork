package com.augurit.aplanmis.admin.market.service.service;

import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.vo.AeaHiCertinstBVo;
import com.augurit.aplanmis.common.vo.ServiceMatterVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * -Service服务调用接口类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:thinkpad</li>
 * <li>创建时间：2019-06-10 13:49:01</li>
 * </ul>
 */
//@Repository
public interface AeaImServiceExamineService {
    PageInfo<ServiceMatterVo> listServiceMatter(ServiceMatterVo serviceMatterVo, Page page) throws Exception;

    ServiceMatterVo getServiceMatterServiceByUnitServiceId(String unitServiceId) throws Exception;

    PageInfo<AeaHiCertinstBVo> listCertinstByUnitServiceid(String unitServiceId, Page page) throws Exception;

    List<AeaHiCertinstBVo> listCertinstByUnitServiceId(String unitServiceId) throws Exception;

    PageInfo<AeaItemBasic> listServiceItemServiceid(String serviceId, Page page);

    //不分页获取中介服务事项
    List<AeaItemBasic> listServiceItemByServiceId(String serviceId);

    void examineService(ServiceMatterVo serviceMatterVo) throws Exception;

    AeaHiCertinstBVo getCertinstById(String certinstId) throws Exception;

    Map getMajorTreeNode(String serviceId, String certinstId) throws Exception;
//    public void saveAeaImServiceQual(AeaImServiceQual aeaImServiceQual) throws Exception;
//    public void updateAeaImServiceQual(AeaImServiceQual aeaImServiceQual) throws Exception;
//    public void deleteAeaImServiceQualById(String id) throws Exception;
//    public void deleteServiceQualByServiceId(String serviceId, String qualId) throws Exception;
//    public PageInfo<AeaImServiceQual> listAeaImServiceQual(AeaImServiceQual aeaImServiceQual, Page page) throws Exception;
//    public AeaImServiceQual getAeaImServiceQualById(String id) throws Exception;
//    public List<AeaImServiceQual> listAeaImServiceQual(AeaImServiceQual aeaImServiceQual) throws Exception;
//    public List<AeaImServiceQual> getAeaImServiceQualListByServiceId(String serviceId) throws Exception;
}
