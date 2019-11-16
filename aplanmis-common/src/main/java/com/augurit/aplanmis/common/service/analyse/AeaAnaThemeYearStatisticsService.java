package com.augurit.aplanmis.common.service.analyse;

import com.augurit.aplanmis.common.domain.AeaAnaThemeYearStatistics;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 主题年申报统计表（在月申报统计表的基础上统计。历史年数据不变。本年数据每日更新，保存的是本年至昨日的数据，页面显示要加上实时计算今日的数据）-Service服务调用接口类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:mayanhao</li>
 * <li>创建时间：2019-09-05 10:35:36</li>
 * </ul>
 */
public interface AeaAnaThemeYearStatisticsService {
    public void saveAeaAnaThemeYearStatistics(AeaAnaThemeYearStatistics aeaAnaThemeYearStatistics) throws Exception;

    public void updateAeaAnaThemeYearStatistics(AeaAnaThemeYearStatistics aeaAnaThemeYearStatistics) throws Exception;

    public void deleteAeaAnaThemeYearStatisticsById(String id) throws Exception;

    public PageInfo<AeaAnaThemeYearStatistics> listAeaAnaThemeYearStatistics(AeaAnaThemeYearStatistics aeaAnaThemeYearStatistics, Page page) throws Exception;

    public AeaAnaThemeYearStatistics getAeaAnaThemeYearStatisticsById(String id) throws Exception;

    public List<AeaAnaThemeYearStatistics> listAeaAnaThemeYearStatistics(AeaAnaThemeYearStatistics aeaAnaThemeYearStatistics) throws Exception;
}
