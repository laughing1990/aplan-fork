package com.augurit.aplanmis.common.service.analyse;


import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.aplanmis.common.vo.analyse.ApplyThemeExceptionVo;
import com.augurit.aplanmis.common.vo.analyse.ItemExceptionCountVo;
import com.augurit.aplanmis.common.vo.analyse.ItemStatusCountVo;

import java.util.List;
import java.util.Map;

public interface EfficiencySupervisionService {

    public ItemStatusCountVo listItemStatusCount()throws Exception;

    public ContentResultForm queryApplyStatistics();

    public ContentResultForm queryThemeApplyStatistics(String startTime, String endTime);

    public ContentResultForm queryThemeTypeApplyStatistics(String startTime, String endTime);

    public ContentResultForm queryStageApplyStatistics(String startTime, String endTime);

    public  Map<String, Object> queryApplyStatisticsByTime(String startTime, String endTime) throws Exception;

    public List<Map<String, Object>> staticsticsByApplySource() throws Exception;

    public List<Map<String, Object>> staticsticsByApplyType() throws Exception;

    Map<String, Object> queryApplyStatisticsByRegion(String startTime,String endTime) throws Exception;

    public ContentResultForm queryMonthlyApplyStatistics(String startTime, String endTime);


    List<ApplyThemeExceptionVo> queryApplyThemeExceptionStatistics() throws Exception;

    Map<String, Object> queryItemStatisticsByTime(String startTime, String endTime) throws Exception;

    Map<String, Object> queryItemStatisticsByRegion(String startTime, String endTime) throws Exception;

    List<ItemExceptionCountVo> queryItemExceptionStatistics() throws Exception;

    List<Map<String, Object>> staticsticsIteminstByApplyType() throws Exception;

}
