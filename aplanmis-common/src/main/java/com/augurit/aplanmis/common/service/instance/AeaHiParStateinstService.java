package com.augurit.aplanmis.common.service.instance;

import com.augurit.aplanmis.common.domain.AeaHiParStateinst;
import com.augurit.aplanmis.common.domain.AeaItemState;

import java.util.List;
import java.util.Map;

/**
 * @author 小糊涂
 */
public interface AeaHiParStateinstService {

    /**
     * 根据申请实例ID或阶段实例ID查询已选择的情形实例ID
     *
     * @param applyinstId 申请实例ID
     * @param stageinstId 阶段实例ID
     * @return
     */
    List<AeaHiParStateinst> listAeaHiParStateinstByApplyinstIdOrStageinstId(String applyinstId, String stageinstId) throws Exception;

    /**
     * 批量插入选择的情形实例
     *
     * @param applyinstId 申请实例ID
     * @param stageinstId 阶段实例ID
     * @param stateIds    选择的情形ID
     * @param creater     创建人
     * @return 插入的条数
     */
    int batchInsertAeaHiParStateinst(String applyinstId, String stageinstId, String[] stateIds, String creater) throws Exception;

    /**
     *根据事情实例或阶段实例ID查询已选择的情形列表
     * @param applyinstId 申请实例ID
     * @param stageinstId 阶段实例ID
     * @return List<Map<String,String>>
     * @throws Exception e
     */
    List<Map<String,String>> listSelectedParStateinstByStageinstIdOrApplyinstId( String applyinstId, String stageinstId) throws Exception;

    /**
     * 根据申请实例ID或并联实例ID查询已选择的事项情形列表
     * @param applyinstId
     * @param stageinstId
     * @return
     * @throws Exception
     */
    List<AeaItemState> listAeaItemStateByApplyinstIdOrSeriesinstId(String applyinstId, String stageinstId) throws Exception;
}
