package com.augurit.aplanmis.supermarket.projPurchase.service;

import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.aplanmis.common.domain.AeaImProjPurchase;
import com.augurit.aplanmis.common.domain.AeaImService;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.vo.*;
import com.augurit.aplanmis.supermarket.projPurchase.vo.OwnerIndexData;
import com.augurit.aplanmis.supermarket.projPurchase.vo.ProjPurchaseStatisticsVo;
import com.augurit.aplanmis.supermarket.projPurchase.vo.SelectedQualMajorRequire;
import com.augurit.aplanmis.supermarket.projPurchase.vo.ShowProjPurchaseVo;
import com.github.pagehelper.Page;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface AeaImProjPurchaseService {
    List<AeaImProjPurchase> getProjPurchaseList(AeaImProjPurchase aeaImProjPurchase, Page page);

    AeaImProjPurchase getProjPurchaseById(String projPurchaseId);

    ProjPurchaseStatisticsVo getStaticDataForProjPurchase();

    List<AeaImProjPurchase> getProjListByLocalCode(String keyword, Page page);

    AeaImProjPurchase saveAeaImProjPurchase(SaveAeaImProjPurchaseVo aeaImProjPurchaseVo, HttpServletRequest request) throws Exception;

    AeaImProjPurchase updateProjPurchase(SaveAeaImProjPurchaseVo aeaImProjPurchaseVo, HttpServletRequest request) throws Exception;

    void updateAeaImProjPurchase(AeaImProjPurchase aeaImProjPurchase) throws Exception;

    void revisedProjPurchase(SaveAeaImProjPurchaseVo saveAeaImProjPurchaseVo, String operateDescribe, List<MultipartFile> files, HttpServletRequest request) throws Exception;

    /**
     * 获取业主未发布的项目列表
     *
     * @param localCode 项目编码
     * @param page      分页参数
     * @param request
     * @return
     */
    List<AeaProjInfo> getUnpublishedProjInfoList(String localCode, Page page, HttpServletRequest request) throws Exception;

    /**
     * 获取项目信息
     *
     * @param projInfoId
     * @return
     */
    ProUnitLinkVo getProUnitLinkInfo(String projInfoId, HttpServletRequest request);


    /**
     * 获取服务事项列表
     *
     * @param keyword
     * @param page
     * @return
     */
    List<AeaItemServiceVo> getAgentServiceItemList(String keyword, Page page);

    /**
     * 根据服务事项获取服务
     *
     * @param itemVerId
     * @return
     */
    List<AeaImServiceVo> getItemServiceListByItemVerId(String itemVerId) throws Exception;


    /**
     * 查询符合条件的中介机构
     *
     * @param queryAgentUnitInfo
     * @return
     * @throws Exception
     */
    List<AgentUnitInfoVo> getAgentUnitInfoList(QueryAgentUnitInfoVo queryAgentUnitInfo) throws Exception;

    /**
     * 根据查询条件查询公示需求列表
     *
     * @param queryProjPurchaseVo
     * @param page
     * @return
     */
    List<AeaImProjPurchase> getPublicProjPurchaseList(QueryProjPurchaseVo queryProjPurchaseVo, Page page);

    AeaImProjPurchaseDetailVo getPublicProjPurchaseDatail(String projPurchaseId) throws Exception;

    List<AeaImService> getAllService() throws Exception;

    List<BscAttForm> getRequireExplainFileList(String requireExplainFile) throws Exception;

    List<BscAttForm> getOfficialRemarkFileList(String officialRemarkFile) throws Exception;

    SelectedQualMajorRequire getSelectedQualMajorRequire(String projPurchaseId) throws Exception;

    List<AeaUnitInfo> getAeaUnitInfoByPage(String keyword, Page page) throws Exception;

    ShowProjPurchaseVo showProjPurchaseByProjPurchaseId(String projPurchaseId) throws Exception;

    void submitProjPurchaseByProjPurchaseId(String projPurchaseId, HttpServletRequest request) throws Exception;

    ContentResultForm listProjPurchase(AeaImProjPurchase aeaImProjPurchase, int pageSize, int pageNum);

    ContentResultForm getProjPurchaseInfoByProjPurchaseId(String projPurchaseId) throws Exception;

    /**
     * 采购项目申请无效
     *
     * @param projPurchaseId 采购需求ID
     * @param memo           申请无效原因
     * @param files          说明文件
     * @param request
     * @throws Exception
     */
    void applyProjPurchaseInvalid(String projPurchaseId, String memo, List<MultipartFile> files, HttpServletRequest request) throws Exception;

    ContentResultForm getPublishingInfoByProjPurchaseId(String projPurchaseId, String unitInfoId) throws Exception;

    AeaImProjPurchase reselectProjPurchase(String projPurchaseId, String memo, List<MultipartFile> files, HttpServletRequest request) throws Exception;

    List<AeaImService> getServiceTypeList(String unitInfoId) throws Exception;

    void applyPostponeService(ApplyPostponeServiceData applyPostponeServiceData, String memo, List<MultipartFile> files, HttpServletRequest request) throws Exception;

    OwnerIndexData showProjPurchaseData(String unitInfoId, String linkmanInfoId) throws Exception;

    AeaProjInfo getProjInfoByLocalCode(String localCode) throws Exception;

    ContentResultForm queryIntermediaryList(String projPurchaseId, int pageSize, int pageNum);

    ContentResultForm updateIntermediaryWonBidStatus(String unitBiddingId, String projPurchaseId) throws Exception;
}
