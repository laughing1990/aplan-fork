package com.augurit.aplanmis.common.service.stage;

import com.augurit.aplanmis.common.domain.AeaParStage;

import java.util.List;

/**
 * @author 小糊涂
 */
public interface AeaParStageService {
    /**
     * 根据主题ID或主题版本ID查询主题下的阶段列表
     * 1 themeId!=null && themeVerId==null 查询主题下最新已发布或试运行下的阶段列表
     * 2 themeVerId!=null 查询指定主题版本的阶段列表
     *
     * @param themeId    主题ID
     * @param themeVerId 主题版本ID
     * @return List<AeaParStage>
     */
    List<AeaParStage> listAeaParStageByThemeIdOrThemeVerId(String themeId, String themeVerId, String topOrgId) throws Exception;

    /**
     * 根据 项目ID 或 编码（localCode||gcbm)
     *
     * @param projInfoId 项目ID
     * @param projCode   编码（localCode||gcbm)
     * @return List<AeaParStage>
     */
    List<AeaParStage> listAeaParStageByProjIdOrProjCode(String projInfoId, String projCode) throws Exception;

    /**
     * 根据阶段ID查询阶段信息
     *
     * @param id
     * @return
     * @throws Exception
     */
    AeaParStage getAeaParStageById(String id) throws Exception;

    /**
     * 根据事项实例ID查找所属阶段信息
     *
     * @param iteminstId 事项实例ID
     * @return
     * @throws Exception
     */
    AeaParStage getAeaParStageByIteminstId(String iteminstId) throws Exception;

    /**
     * 根据申请实例ID查找所属阶段信息
     *
     * @param applyinstId 申请实例ID
     * @return
     * @throws Exception
     */
    AeaParStage getAeaParStageByApplyinstId(String applyinstId) throws Exception;

    /**
     * 根据阶段ID、项目ID查询当前阶段的申请实例状态
     *
     * @param stageId    阶段ID
     * @param projInfoId 项目ID
     * @return
     * @throws Exception
     */
    List<String> getApplyInstStatusByProjInfoIdAndStageId(String stageId, String projInfoId, String unitInfoId, String linkmanInfoId) throws Exception;

    /**
     * 根据项目ID获取已经办结或办结容缺通过的阶段信息
     *
     * @param projInfoId
     * @return
     */
    List<AeaParStage> getCompletedStageByProjInfoId(String projInfoId);
}
