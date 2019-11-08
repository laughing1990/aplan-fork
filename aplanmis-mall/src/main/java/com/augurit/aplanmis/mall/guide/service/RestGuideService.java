package com.augurit.aplanmis.mall.guide.service;


import com.augurit.aplanmis.mall.guide.vo.RestGuideVo;
import com.augurit.aplanmis.mall.guide.vo.RestSingleGuideVo;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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


    }
