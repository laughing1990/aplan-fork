package com.augurit.aplanmis.mall.main.service;


import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.domain.AeaParTheme;
import com.augurit.aplanmis.mall.main.vo.AeaRegionVo;
import com.augurit.aplanmis.mall.main.vo.StaticticsVo;
import com.augurit.aplanmis.mall.main.vo.ThemeTypeVo;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface RestMainService {

    /**
     * 主题类别列表
     * @return
     * @throws Exception
     */
    List<ThemeTypeVo> getThemeTypeList(String rootOrgId) throws Exception;

    /**
     * 查询当前主题下最大版本主题的所有阶段列表
     * @param themeId
     * @return
     * @throws Exception
     */
    List<AeaParStage> getStageByThemeId(String themeId,String projInfoId,String rootOrgId,String unitInfoId,String dygjbzfxfw,HttpServletRequest request) throws Exception;

    /**
     * 申报件统计
     * @return
     * @throws Exception
     */
    public StaticticsVo getApplyStatistics(String rootOrgId) throws Exception;

    /**
     * 办件统计
     * @param topOrgId
     * @return
     */
    public StaticticsVo getItemStatistics(String topOrgId) throws Exception;

    /**
     * 获取当前topOrgId下的单位
     * @param topOrgId
     * @return
     */
    AeaRegionVo getAeaRegionVo(String topOrgId) throws Exception;


    /**
     * 首页搜索事项列表接口
     * @param keyword
     * @param topOrgId
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<AeaItemBasic> getItemMainList(String keyword,String topOrgId,int pageNum,int pageSize);

    /**
     * 首页搜索主题列表接口
     * @param keyword
     * @param topOrgId
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<AeaParTheme> getThemeMainList(String keyword,String topOrgId,int pageNum,int pageSize);
}
