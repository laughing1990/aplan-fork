package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaImUnitBidding;
import com.augurit.aplanmis.common.domain.AeaUnitBiddingAndEvaluation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * -Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaImUnitBiddingMapper {

    void insertAeaImUnitBidding(AeaImUnitBidding aeaImUnitBidding) throws Exception;

    void updateAeaImUnitBidding(AeaImUnitBidding aeaImUnitBidding) throws Exception;

    List<AeaImUnitBidding> listAeaImUnitBidding(AeaImUnitBidding aeaImUnitBidding) throws Exception;

    List<AeaImUnitBidding> listJoining();


    /**
     * 机构中选记录列表
     */
    List<AeaUnitBiddingAndEvaluation> listWinBidService(@Param("unitInfoId") String unitInfoId, @Param("projName") String projName, @Param("serviceId") String serviceId) throws Exception;

    /**
     * 中介机构综合评价
     */
    AeaUnitBiddingAndEvaluation getUnitServiceEvaluation(@Param("unitInfoId") String unitInfoId) throws Exception;

    /**
     * 统计中介机构项目报名综合数据
     *
     * @param unitInfoId
     * @param type       0-已报名项目 1-已中选项目 2-已签约项目 3-服务中项目
     * @return
     */
    Long countUnitBiddingInfo(@Param("unitInfoId") String unitInfoId, @Param("type") String type) throws Exception;

    /**
     * @param aeaImUnitBidding
     * @param type             0-已报名项目 1-已中选项目 2-已签约项目 3-服务中项目
     * @return
     * @throws Exception
     */
    List<AeaImUnitBidding> listUnitBidding(@Param("aeaImUnitBidding") AeaImUnitBidding aeaImUnitBidding, @Param("type") String type) throws Exception;

    AeaImUnitBidding getUnitBiddingAndLinkman(@Param("unitBiddingId") String unitBiddingId) throws Exception;


    List<AeaImUnitBidding> listUnitBiddingByProjPurchaseId(@Param("projPurchaseId") String projPurchaseId) throws Exception;

    /**
     * 更新中标状态
     *
     * @param unitBiddingId
     * @param projPurchaseId
     * @param isWonBid
     * @return
     */
    int updateWinBid(@Param("unitBiddingId") String unitBiddingId, @Param("projPurchaseId") String projPurchaseId, @Param("isWonBid") String isWonBid);

    /**
     * 放弃中标
     *
     * @param unitBiddingId  竞价ID
     * @param projPurchaseId 项目采购ID
     * @return
     */
    int abortWinBid(@Param("unitBiddingId") String unitBiddingId, @Param("projPurchaseId") String projPurchaseId);

    /**
     * 更新 上传合同，服务结果，评价 状态
     *
     * @param unitBiddingId    竞价ID
     * @param isUploadContract 合同是否已上传：1 是，0 否
     * @return
     */
    int updateUploadContract(@Param("unitBiddingId") String unitBiddingId, @Param("isUploadContract") String isUploadContract);

    /**
     * 更新上传服务结果值
     *
     * @param projPurchaseId 项目采购ID
     * @param isUploadResult 服务结果是否已上传：1 是，0 否
     * @return
     */
    int updateUploadResult(@Param("projPurchaseId") String projPurchaseId, @Param("isUploadResult") String isUploadResult);

    /**
     * 获取中标单位竞价ID
     *
     * @param projPurchaseId
     * @return
     */
    List<AeaImUnitBidding> getUnitBiddingByProjPurchase(@Param("projPurchaseId") String projPurchaseId);
}
