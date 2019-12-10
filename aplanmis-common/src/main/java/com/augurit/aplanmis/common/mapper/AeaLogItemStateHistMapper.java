package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaLogItemStateHist;
import com.augurit.aplanmis.common.vo.SupplyOrSpacialCommentVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yinlf
 */
@Mapper
@Repository
public interface AeaLogItemStateHistMapper {

    /**
     * 新增事项状态变更记录
     *
     * @param aeaLogItemStateHist
     */
    void insertAeaLogItemStateHist(AeaLogItemStateHist aeaLogItemStateHist);

    /**
     * 更新事项状态变更记录
     *
     * @param aeaLogItemStateHist
     */
    void updateAeaLogItemStateHist(AeaLogItemStateHist aeaLogItemStateHist);

    /**
     * @param id
     */
    void deleteAeaLogItemStateHist(@Param("id") String id);

    void batchDeleteAeaLogItemStateHist(@Param("ids") List<String> ids);

    /**
     * 查询事项状态变更记录
     *
     * @param aeaLogItemStateHist
     * @return
     */
    List<AeaLogItemStateHist> listAeaLogItemStateHist(AeaLogItemStateHist aeaLogItemStateHist);

    /**
     * 记录ID查询事项状态变更记录
     *
     * @param id
     * @return
     */
    AeaLogItemStateHist getAeaLogItemStateHistById(@Param("id") String id);

    AeaLogItemStateHist getAeaLogItemStateHistByCorrectId(@Param("correctId") String correctId);

    /**
     * 批量新增事项状态变更记录
     *
     * @param aeaLogItemStateHistList
     */
    void batchInsertAeaLogItemStateHist(@Param("aeaLogItemStateHistList") List<AeaLogItemStateHist> aeaLogItemStateHistList);

    AeaLogItemStateHist getInitStateAeaLogItemStateHist(@Param("iteminstId") String iteminstId, @Param("appinstId") String appinstId);

    List<SupplyOrSpacialCommentVo> findSpacialAeaLogItemStateHist(@Param("taskInstId") String taskInstId, @Param("rootOrgId") String rootOrgId);

    List<SupplyOrSpacialCommentVo> findItemCorrectStateHist(@Param("taskInstId") String taskInstId, @Param("rootOrgId") String rootOrgId);

    List<AeaLogItemStateHist> listAeaLogItemStateHistByIteminstIds(@Param("iteminstIds") List<String> iteminstIds);

}
