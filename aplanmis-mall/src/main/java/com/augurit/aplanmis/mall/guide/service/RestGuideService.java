package com.augurit.aplanmis.mall.guide.service;


import com.augurit.aplanmis.mall.guide.vo.RestGuideVo;
import com.augurit.aplanmis.mall.guide.vo.RestSingleGuideVo;
import com.augurit.aplanmis.mall.guide.vo.RestStageAndItemVo;
import com.augurit.aplanmis.mall.main.vo.ThemeTypeVo;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface RestGuideService {

    /**
     * 根据stageId阶段id查询当前阶段办事指南信息
     * @param stageId
     * @return
     * @throws Exception
     */
    RestGuideVo getGuideByStageId(String stageId,String rootOrgId)throws Exception;

    RestSingleGuideVo getGuideByItemVerId(String itemVerId,String rootOrgId)throws Exception;

    /**
     * 材料预览接口
     * @param detailId
     * @param request
     * @param response
     * @param redirectAttributes
     * @return
     * @throws Exception
     */
    ModelAndView preview(String detailId, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) throws Exception;

    /**
     * 办事指南-根据主题id查询阶段及事项列表
     * @param themeId
     * @throws Exception
     */
    RestStageAndItemVo getStageAndItemByThemeId(String themeId)throws Exception;

    /**
     * 办事指南页根据搜索关键字查符合条件的主题、阶段、事项
     * @param keyword
     * @return
     * @throws Exception
     */
    List<ThemeTypeVo> searchThemeAndStageAndItemByKeyword(String keyword)throws Exception;


    /**
     * 办事指南-根据主题id查询阶段及事项列表
     * @param themeId
     * @throws Exception
     */
    RestStageAndItemVo searchStageAndItemByKeywordAndThemeId(String themeId,String keyword)throws Exception;
    }
