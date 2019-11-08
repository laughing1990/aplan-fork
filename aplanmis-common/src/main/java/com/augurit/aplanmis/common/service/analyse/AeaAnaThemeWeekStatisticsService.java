package com.augurit.aplanmis.common.service.analyse;

import com.augurit.aplanmis.common.domain.AeaAnaThemeWeekStatistics;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 主题周申报统计表（在日申报统计表的基础上统计。历史周数据不变。本周数据每日更新，保存的是本周至昨日的数据，页面显示要加上实时计算今日的数据）-Service服务调用接口类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:mayanhao</li>
 * <li>创建时间：2019-09-05 10:19:04</li>
 * </ul>
 */
public interface AeaAnaThemeWeekStatisticsService {
    public void saveAeaAnaThemeWeekStatistics(AeaAnaThemeWeekStatistics aeaAnaThemeWeekStatistics) throws Exception;

    public void updateAeaAnaThemeWeekStatistics(AeaAnaThemeWeekStatistics aeaAnaThemeWeekStatistics) throws Exception;

    public void deleteAeaAnaThemeWeekStatisticsById(String id) throws Exception;

    public PageInfo<AeaAnaThemeWeekStatistics> listAeaAnaThemeWeekStatistics(AeaAnaThemeWeekStatistics aeaAnaThemeWeekStatistics, Page page) throws Exception;

    public AeaAnaThemeWeekStatistics getAeaAnaThemeWeekStatisticsById(String id) throws Exception;

    public List<AeaAnaThemeWeekStatistics> listAeaAnaThemeWeekStatistics(AeaAnaThemeWeekStatistics aeaAnaThemeWeekStatistics) throws Exception;

    public void analyThemeWeekStatistics(String startTime, String endTime) throws Exception;
}
