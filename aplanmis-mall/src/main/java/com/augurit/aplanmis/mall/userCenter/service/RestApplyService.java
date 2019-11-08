package com.augurit.aplanmis.mall.userCenter.service;


import com.augurit.aplanmis.common.vo.AeaUnitInfoVo;
import com.augurit.aplanmis.mall.userCenter.vo.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface RestApplyService {


    /**
     * 获取当前申报主体
     * @param request
     * @return
     */
    UserInfoVo getApplyObject(HttpServletRequest request, String projInfoId) throws Exception;

    ParallelApplyResultVo startStageProcess(StageApplyDataPageVo stageApplyDataPageVo) throws Exception;

    SeriesApplyResultVo startSeriesFlow(SeriesApplyDataPageVo seriesApplyDataPageVo) throws Exception;

    List<AeaUnitInfoVo> getAeaUnitInfosByProjInfoId(String projInfoId, HttpServletRequest request) throws Exception;

    }
