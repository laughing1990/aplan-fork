package com.augurit.aplanmis.common.service.instance;

import com.augurit.aplanmis.common.domain.AeaHiSeriesinst;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.Date;
import java.util.List;

/**
 * 单项申报信息
 * @author sam
 */
public interface AeaHiSeriesinstService {
    /**
     * 新增单项实例
     * @param applyinstId 申请实例ID
     * @param appinstId 流程模板实例ID
     * @param isParallel 是否并行推进事项。0表示否，1表示是
     * @param stageId 所属阶段定义(是并行推进事项时为必须)
     * @return
     * @throws Exception
     */
    public AeaHiSeriesinst createAeaHiSeriesinst(String applyinstId,String appinstId,String isParallel,String stageId) throws Exception;

    /**
     * 更新单项信息
     * @param aeaHiSeriesinst
     * @return
     * @throws Exception
     */
    public void updateAeaHiSeriesinst(AeaHiSeriesinst aeaHiSeriesinst) throws Exception;

    /**
     * 获取单项信息列表
     * @param aeaHiSeriesinst
     * @return
     * @throws Exception
     */
    public List<AeaHiSeriesinst> listAeaHiSeriesinst(AeaHiSeriesinst aeaHiSeriesinst) throws Exception;

    /**
     * 获取单项信息分页列表
     * @param aeaHiSeriesinst
     * @param page
     * @return
     * @throws Exception
     */
    public PageInfo<AeaHiSeriesinst> listAeaHiSeriesinst(AeaHiSeriesinst aeaHiSeriesinst, Page page) throws Exception;

    /**
     * 根据主键删除单项信息
     * @param seriesinstId
     * @throws Exception
     */
    public void deleteAeaHiSeriesinst(String seriesinstId) throws Exception;

    /**
     * 根据主键查询单项申报信息
     * @param seriesinstId
     * @throws Exception
     */
    public AeaHiSeriesinst getAeaHiSeriesinstById(String seriesinstId) throws Exception;

    /**
     * 根据申报ID查询单项信息
     * @param applyinstId 申报实例ID
     * @throws Exception
     */
    public AeaHiSeriesinst getAeaHiSeriesinstByApplyinstId(String applyinstId) throws Exception;

    /**
     * 根据流程实例ID查询单项信息
     * @param appinstId 流程实例ID
     * @throws Exception
     */
    public AeaHiSeriesinst getAeaHiSeriesinstByAppinstId(String appinstId) throws Exception;

    /**
     * 根据ID更新单项申报状态
     * @param seriesinstId
     * @param state 需要更为为的状态
     * @throws Exception
     */
    public void updateSeriesinstStateById(String seriesinstId,String state) throws Exception;

    /**
     * 根据ID更新单项申报结束时间
     * @param seriesinstId
     * @param endTime 结束时间
     * @throws Exception
     */
    public void updateEndTimeById(String seriesinstId, Date endTime) throws Exception;
}
