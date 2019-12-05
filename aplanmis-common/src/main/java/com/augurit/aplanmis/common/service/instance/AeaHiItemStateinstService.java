package com.augurit.aplanmis.common.service.instance;

import com.augurit.aplanmis.common.domain.AeaItemState;

import java.util.List;
import java.util.Map;

/**
 * @author 小糊涂
 */
public interface AeaHiItemStateinstService {
    /**
     * 批量插入选择的情形实例
     *
     * @param applyinstId  申请实例ID
     * @param seriesinstId 单项实例ID
     * @param itemStateIds 选择的情形ID
     * @param creater 创建人
     * @return 插入的条数
     */
    int batchInsertAeaHiItemStateinst(String applyinstId, String seriesinstId, String stageinstId, String[] itemStateIds, String creater) throws Exception;

    /**
     *根据事情实例或单项实例ID查询已选择的情形列表
     * @param applyinstId 申请实例ID
     * @param seriesinstId 单项实例ID
     * @return List<Map<String,String>>
     * @throws Exception e
     */
    List<Map<String,String>> listSelectedAeaItemStateinstBySeriesinstIdOrApplyinstId(String applyinstId, String seriesinstId) throws Exception;

    /**
     * 根据申请实例ID或单项实例ID查询已选择的情形列表
     * @param applyinstId
     * @param seriesinstId
     * @return
     * @throws Exception
     */
    List<AeaItemState> listAeaItemStateByApplyinstIdOrSeriesinstId(String applyinstId, String seriesinstId) throws Exception;

    /**
     * 批量删除事项情形
     * @param itemStateIds
     */
    void batchDeleteAeaItemState(String[] itemStateIds);
}
