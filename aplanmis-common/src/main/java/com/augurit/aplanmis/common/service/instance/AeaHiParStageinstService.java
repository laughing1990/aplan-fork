package com.augurit.aplanmis.common.service.instance;

import com.augurit.aplanmis.common.domain.AeaHiParStageinst;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 并联申报实例
 * @author sam
 */
public interface AeaHiParStageinstService {
    /**
     * 新增并联申报实例
     * @param applyinstId 申请实例ID 必须
     * @param stageId 阶段ID 必须
     * @param themeVerId 主题版本ID 必须
     * @param appinstId 主流程实例ID 可选
     * @param stageinstId 阶段实例ID 可选
     * @return
     * @throws Exception
     */
    public AeaHiParStageinst createAeaHiParStageinst(String applyinstId,String stageId,String themeVerId,String appinstId,String stageinstId) throws Exception;

    /**
     * 更新并联申报实例
     * @param aeaHiParStageinst
     * @throws Exception
     */
    public void updateAeaHiParStageinst(AeaHiParStageinst aeaHiParStageinst) throws Exception;

    /**
     * 获取并联申报实例列表
     * @param aeaHiParStageinst
     * @return
     * @throws Exception
     */
    public List<AeaHiParStageinst> listAeaHiParStageinst(AeaHiParStageinst aeaHiParStageinst) throws Exception;

    /**
     * 获取并联申报实例分页列表
     * @param aeaHiParStageinst
     * @param page
     * @return
     * @throws Exception
     */
    public PageInfo<AeaHiParStageinst> listAeaHiParStageinst(AeaHiParStageinst aeaHiParStageinst, Page page) throws Exception;

    /**
     * 根据主键获取并联申报信息
     * @param stageinstId
     * @return
     * @throws Exception
     */
    public AeaHiParStageinst getAeaHiParStageinstById(String stageinstId) throws Exception;

    /**
     * 根据ID删除并联申报信息
     * @param stageinstId
     * @throws Exception
     */
    public void deleteAeaHiParStageinstById(String stageinstId) throws Exception;

    /**
     * 根据阶段ID获取并联申报信息
     * @param stageId 阶段ID
     * @return
     * @throws Exception
     */
    public List<AeaHiParStageinst> getAeaHiParStageinstByStageId(String stageId) throws Exception;

    /**
     * 根据主题版本ID查询并联申报列表
     * @param themeVerId 主题版本号
     * @return
     * @throws Exception
     */
    public List<AeaHiParStageinst> listAeaHiParStageinstByThemeVerId(String themeVerId) throws Exception;

    /**
     * 根据主流程实例ID查询并联申报信息
     * @param appinstId 流程实例ID
     * @return
     * @throws Exception
     */
    public AeaHiParStageinst getAeaHiParStageinstByAppinstId(String appinstId) throws Exception;

    /**
     * 根据申请ID查询并联申报信息
     * @param applyinstId 申请ID
     * @return
     * @throws Exception
     */
    public AeaHiParStageinst getAeaHiParStageinstByApplyinstId(String applyinstId) throws Exception;

    /**
     * 根据申请ID更新并联申报状态
     * @param applyinstId 申请ID
     * @param stageinstState 并联申报状态
     * @throws Exception
     */
   // public void updateStageinstStateByApplyinstId(String applyinstId,String stageinstState) throws Exception;

    /**
     * 根据申请ID更新并联业务状态
     * @param applyinstId 申请ID
     * @param businessState 并联业务状态
     * @throws Exception
     */
   // public void updateBusinessStateByApplyinstId(String applyinstId,String businessState) throws Exception;
}
