package com.augurit.aplanmis.rest.index.service;


import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.rest.index.service.vo.AeaRegionVo;
import com.augurit.aplanmis.rest.index.service.vo.StaticticsVo;
import com.augurit.aplanmis.rest.index.service.vo.ThemeTypeVo;

import java.util.List;

public interface RestMainService {

    /**
     * 主题类别列表
     *
     * @return
     * @throws Exception
     */
    List<ThemeTypeVo> getThemeTypeList(String rootOrgId) throws Exception;

    /**
     * 查询当前主题下最大版本主题的所有阶段列表
     *
     * @param themeId
     * @return
     * @throws Exception
     */
    List<AeaParStage> getStageByThemeId(String themeId, String projInfoId, String rootOrgId, String unitInfoId) throws Exception;

    /**
     * 申报件统计
     *
     * @return
     * @throws Exception
     */
    public StaticticsVo getApplyStatistics(String rootOrgId) throws Exception;

    /**
     * 办件统计
     *
     * @param topOrgId
     * @return
     */
    public StaticticsVo getItemStatistics(String topOrgId) throws Exception;

    /**
     * 获取当前topOrgId下的单位
     *
     * @param topOrgId
     * @return
     */
    AeaRegionVo getAeaRegionVo(String topOrgId) throws Exception;
}
