package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaImApplyinstUnitBidding;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 中标单位与申报实例ID关联-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaImApplyinstUnitBiddingMapper {
    /**
     * 根据申请实例ID或竞价ID查询
     *
     * @param applyinstId   申请实例ID
     * @param unitBiddingId 单位竞价ID
     * @return AeaImApplyinstUnitBidding
     * @throws Exception E
     */
    AeaImApplyinstUnitBidding getOneByApplyinstIdOrBiddingId(@Param("applyinstId") String applyinstId, @Param("unitBiddingId") String unitBiddingId) throws Exception;

    /**
     * 通过主键查询单个
     *
     * @param applyinstUnitBiddingId 主键ID
     * @return AeaImApplyinstUnitBidding
     * @throws Exception e
     */
    AeaImApplyinstUnitBidding getOneById(@Param("applyinstUnitBiddingId") String applyinstUnitBiddingId) throws Exception;

    /**
     * 单个插入
     *
     * @param aeaImApplyinstUnitBidding 插入的对象
     * @return 插入的条数
     * @throws Exception e
     */
    int insertOne(AeaImApplyinstUnitBidding aeaImApplyinstUnitBidding) throws Exception;

    /**
     * 更新
     *
     * @param aeaImApplyinstUnitBidding 更新对象
     * @return 更新的条数
     * @throws Exception e
     */
    int updateAeaImApplyinstUnitBidding(AeaImApplyinstUnitBidding aeaImApplyinstUnitBidding) throws Exception;

    int deleteOneById(@Param("id") String id) throws Exception;


}
