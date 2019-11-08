package com.augurit.aplanmis.rest.userCenter.service;


import com.augurit.aplanmis.rest.userCenter.vo.ParallelApplyResultVo;
import com.augurit.aplanmis.rest.userCenter.vo.SeriesApplyDataPageVo;
import com.augurit.aplanmis.rest.userCenter.vo.SeriesApplyResultVo;
import com.augurit.aplanmis.rest.userCenter.vo.StageApplyDataPageVo;
import com.augurit.aplanmis.rest.userCenter.vo.UserInfoVo;

import javax.servlet.http.HttpServletRequest;

public interface RestApplyService {


    /**
     * 获取当前申报主体
     *
     * @param request
     * @return
     */
    UserInfoVo getApplyObject(HttpServletRequest request) throws Exception;

    ParallelApplyResultVo startStageProcess(StageApplyDataPageVo stageApplyDataPageVo) throws Exception;

    SeriesApplyResultVo startSeriesFlow(SeriesApplyDataPageVo seriesApplyDataPageVo) throws Exception;

}
