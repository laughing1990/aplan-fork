package com.augurit.aplanmis.supermarket.serviceResult.service;

import com.augurit.aplanmis.common.domain.AeaImServiceResult;
import com.augurit.aplanmis.common.vo.MatinstVo;
import com.augurit.aplanmis.common.vo.ServiceProjInfoVo;
import com.augurit.aplanmis.supermarket.serviceResult.vo.ServiceResultVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * -Service服务调用接口类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:tiantian</li>
 * <li>创建时间：2019-06-20 09:10:47</li>
 * </ul>
 */
public interface AeaImServiceResultService {
    void saveAeaImServiceResult(AeaImServiceResult aeaImServiceResult) throws Exception;

    void updateAeaImServiceResult(AeaImServiceResult aeaImServiceResult) throws Exception;


    void deleteAeaImServiceResultById(String id) throws Exception;


    PageInfo<AeaImServiceResult> listAeaImServiceResult(AeaImServiceResult aeaImServiceResult, Page page) throws Exception;

    AeaImServiceResult getAeaImServiceResultById(String serviceResultId) throws Exception;

    List<AeaImServiceResult> listAeaImServiceResult(AeaImServiceResult aeaImServiceResult) throws Exception;

    List<ServiceProjInfoVo> getServiceProjInfoList(String keyword, String auditFlag, Page page, HttpServletRequest request) throws Exception;

    AeaImServiceResult saveAeaImServiceResult(AeaImServiceResult aeaImServiceResult, List<MultipartFile> files, HttpServletRequest request) throws Exception;

    AeaImServiceResult updateAeaImServiceResult(AeaImServiceResult aeaImServiceResult, List<MultipartFile> files, HttpServletRequest request) throws Exception;

    public boolean dealAeaImServiceResult(AeaImServiceResult aeaImServiceResult, HttpServletRequest request) throws Exception;//业主处理服务结果


    boolean deleteServiceResultFile(String detailId) throws Exception;

    /**
     * 保存或修改服务结果---20191122
     *
     * @param matinstIds
     * @param serviceResultVo
     */
    void saveOrUpdateSerivceResult(String[] matinstIds, ServiceResultVo serviceResultVo) throws Exception;

    /**
     * 查询采购项目材料及实例列表
     *
     * @param projPurchaseId 采购项目需求ID
     * @return List<MatinstVo>
     */
    List<MatinstVo> getProjPurchaseMatinstList(String projPurchaseId) throws Exception;

    /**
     * 中介超市上传服务电子件
     *
     * @param matId          材料ID
     * @param matinstId      材料实例ID
     * @param projPurchaseId 采购项目ID
     * @param request        附件对象
     * @return matinstId
     * @throws Exception e
     */
    String uploadServiceResultAtt(String matId, String matinstId, String projPurchaseId, HttpServletRequest request) throws Exception;
}
