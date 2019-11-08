package com.augurit.aplanmis.common.service.analyse;

import com.augurit.aplanmis.common.domain.AeaAnaThemeMonthStatistics;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 主题月申报统计表（在日申报统计表的基础上统计。历史月数据不变。本月数据每日更新，保存的是本月至昨日的数据，页面显示要加上实时计算今日的数据）-Service服务调用接口类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:mayanhao</li>
 * <li>创建时间：2019-09-05 10:35:28</li>
 * </ul>
 */
public interface AeaAnaThemeMonthStatisticsService {
    public void saveAeaAnaThemeMonthStatistics(AeaAnaThemeMonthStatistics aeaAnaThemeMonthStatistics) throws Exception;

    public void updateAeaAnaThemeMonthStatistics(AeaAnaThemeMonthStatistics aeaAnaThemeMonthStatistics) throws Exception;

    public void deleteAeaAnaThemeMonthStatisticsById(String id) throws Exception;

    public PageInfo<AeaAnaThemeMonthStatistics> listAeaAnaThemeMonthStatistics(AeaAnaThemeMonthStatistics aeaAnaThemeMonthStatistics, Page page) throws Exception;

    public AeaAnaThemeMonthStatistics getAeaAnaThemeMonthStatisticsById(String id) throws Exception;

    public List<AeaAnaThemeMonthStatistics> listAeaAnaThemeMonthStatistics(AeaAnaThemeMonthStatistics aeaAnaThemeMonthStatistics) throws Exception;

    public void analyThemeMonthStatistics(String startTime, String endTime) throws Exception;
}
