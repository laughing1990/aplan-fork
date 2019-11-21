package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaImProjPurchase;
import com.augurit.aplanmis.common.domain.AeaImService;
import com.augurit.aplanmis.common.vo.AeaImProjPurchaseDetailVo;
import com.augurit.aplanmis.common.vo.AgentUnitInfoVo;
import com.augurit.aplanmis.common.vo.QueryProjPurchaseVo;
import com.augurit.aplanmis.common.vo.purchase.PurchaseProjVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface AeaImProjPurchaseMapper {
    List<AeaImProjPurchase> listAeaImProjPurchase(AeaImProjPurchase aeaImProjPurchase) throws Exception;

    Integer getPublishedPurchaseCount(@Param("rootOrgId") String rootOrgId) throws Exception;

    void insertAeaImProjPurchase(AeaImProjPurchase aeaImProjPurchase) throws Exception;

    List<AeaImProjPurchase> listAeaImProjPurchaseForMyProj(AeaImProjPurchase aeaImProjPurchase);

    Integer countAeaImProjPurchaseForMyProj(AeaImProjPurchase aeaImProjPurchase);

    AeaImProjPurchase aeaImProjPurchaseForMyProj(AeaImProjPurchase aeaImProjPurchase);


    List<AeaImProjPurchase> listCanjoin();

    AeaImProjPurchase listDetail(String proInfoId);

    AeaImProjPurchase listbid(String projPurchaseId);

    int updateAeaImProjPurchase(AeaImProjPurchase aeaImProjPurchase);

    int deleteAeaImProjPurchaseById(@Param("projPurchaseId") String projPurchaseId);

    List<AeaImProjPurchase> getAeaImProjPurchaseByProjInfoId(@Param("projInfoId") String projInfoId);


    List<AgentUnitInfoVo> listUnitInfoIdByMajorQualRequire(@Param("serviceId") String serviceId, @Param("majorId") String majorId, @Param("qualLevelId") String qualLevelId);

    List<AgentUnitInfoVo> listUnitInfoIdByServiceId(@Param("serviceId") String serviceId);

    List<AeaImProjPurchase> listPublicProjPurchaseByQueryProjPurchaseVo(QueryProjPurchaseVo queryProjPurchaseVo);

    AeaImProjPurchaseDetailVo getAeaImProjPurchaseDetailVoById(String projPurchaseId);

    AeaImProjPurchase getAeaImProjPurchaseByProjPurchaseId(@Param("projPurchaseId") String projPurchaseId);


    List<AeaImProjPurchase> listAuditProjPurchase(AeaImProjPurchase aeaImProjPurchase);

    AeaImProjPurchaseDetailVo getAuditProjPurchaseDetail(String projPurchaseId);

    int updateAeaImProjPurchaseAuditFlag(@Param("aeaImProjPurchase") AeaImProjPurchase aeaImProjPurchase);

    /**
     * 获取中介机构可报名项目以及资质等级要求
     *
     * @return
     */
    List<AeaImProjPurchaseDetailVo> listCanBidProjPurchaseMajorRequire(@Param("unitInfoId") String unitInfoId, @Param("projName") String projName, @Param("itemVerId") String itemVerId, @Param("projPurchaseId") String projPurchaseId);


    List<AeaImProjPurchase> listSelectionNotice(QueryProjPurchaseVo projPurchase);

    AeaImProjPurchase getSelectionNoticeByProjPurchaseId(@Param("projPurchaseId") String projPurchaseId);

    List<AeaImProjPurchase> listProjPurchase(AeaImProjPurchase projPurchase);

    AeaImProjPurchase getProjPurchaseInfoByProjPurchaseId(@Param("projPurchaseId") String projPurchaseId);

    List<AeaImService> getServiceTypeList(@Param("unitInfoId") String unitInfoId);

    AeaImProjPurchase getPublishingInfoByProjPurchaseId(@Param("projPurchaseId") String projPurchaseId, @Param("unitInfoId") String unitInfoId);

    List<AeaImProjPurchase> listProjPurchaseBidding(AeaImProjPurchase aeaImProjPurchase);

    List<AeaImProjPurchase> listContractNotice(QueryProjPurchaseVo projPurchase);

    AeaImProjPurchase getContractNoticeByProjPurchaseId(@Param("projPurchaseId") String projPurchaseId);

    List<Map<String, Object>> queryIntermediaryList(@Param("projPurchaseId") String projPurchaseId);

    void updateIntermediaryWonBidStatus(String unitBiddingId);

    void updateProjPurchaseState(@Param("auditFlag") String auditFlag, @Param("modifier") String modifier, @Param("modifyTime") Date modifyTime, @Param("projPurchaseId") String projPurchaseId);

    /**
     * 已完成采购需求数量
     *
     * @param startDate 查询开始时间
     * @return int
     */
    int listAeaImProjPurchaseNum(@Param("startDate") String startDate);

    /**
     * 已发布未完成的采购数量
     *
     * @param startDate
     * @return
     */
    int listPublicProjPurchaseNum(@Param("startDate") String startDate);

    /**
     * 根据applyinstCode 获取采购项目信息
     *
     * @param applyinstCode 申报流水号
     * @return PurchaseProjVo
     */
    PurchaseProjVo getProjPurchaseInfoByApplyinstCode(@Param("applyinstCode") String applyinstCode);
}
