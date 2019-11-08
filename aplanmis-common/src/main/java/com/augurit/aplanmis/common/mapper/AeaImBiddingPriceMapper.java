package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaImBiddingPrice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 企业报价金额表-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaImBiddingPriceMapper {

    List<AeaImBiddingPrice> listAeaImBiddingPrice(AeaImBiddingPrice aeaImBiddingPrice) throws Exception;

    void insertAeaImBiddingPrice(AeaImBiddingPrice aeaImBiddingPrice) throws Exception;

    void updateAeaImBiddingPrice(AeaImBiddingPrice aeaImBiddingPrice) throws Exception;

    Map getBiddingPriceByProjPurchaseId(@Param("unitBiddingId") String unitBiddingId, @Param("projPurchaseId") String projPurchaseId);

    /**
     * 竞价选取
     * @param projPurchaseId
     * @param type 1-随机选取 2-最高价格
     * @return
     */
    AeaImBiddingPrice getBiddingPrice(@Param("projPurchaseId") String projPurchaseId, @Param("type") String type);
}
