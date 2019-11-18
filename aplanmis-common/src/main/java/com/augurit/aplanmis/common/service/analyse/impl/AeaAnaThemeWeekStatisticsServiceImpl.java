package com.augurit.aplanmis.common.service.analyse.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.common.domain.AeaAnaThemeWeekStatistics;
import com.augurit.aplanmis.common.mapper.AeaAnaThemeDayStatisticsMapper;
import com.augurit.aplanmis.common.mapper.AeaAnaThemeWeekStatisticsMapper;
import com.augurit.aplanmis.common.service.analyse.AeaAnaThemeWeekStatisticsService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 主题周申报统计表（在日申报统计表的基础上统计。历史周数据不变。本周数据每日更新，保存的是本周至昨日的数据，页面显示要加上实时计算今日的数据）-Service服务接口实现类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:mayanhao</li>
 * <li>创建时间：2019-09-05 10:19:04</li>
 * </ul>
 */
@Service
@Transactional
public class AeaAnaThemeWeekStatisticsServiceImpl implements AeaAnaThemeWeekStatisticsService {

    private static Logger logger = LoggerFactory.getLogger(AeaAnaThemeWeekStatisticsServiceImpl.class);

    @Autowired
    private AeaAnaThemeWeekStatisticsMapper aeaAnaThemeWeekStatisticsMapper;
    @Autowired
    private AeaAnaThemeDayStatisticsMapper aeaAnaThemeDayStatisticsMapper;

    public void saveAeaAnaThemeWeekStatistics(AeaAnaThemeWeekStatistics aeaAnaThemeWeekStatistics) throws Exception {
        aeaAnaThemeWeekStatisticsMapper.insertAeaAnaThemeWeekStatistics(aeaAnaThemeWeekStatistics);
    }

    public void updateAeaAnaThemeWeekStatistics(AeaAnaThemeWeekStatistics aeaAnaThemeWeekStatistics) throws Exception {
        aeaAnaThemeWeekStatisticsMapper.updateAeaAnaThemeWeekStatistics(aeaAnaThemeWeekStatistics);
    }

    public void deleteAeaAnaThemeWeekStatisticsById(String id) throws Exception {
        if (id == null)
            throw new InvalidParameterException(id);
        aeaAnaThemeWeekStatisticsMapper.deleteAeaAnaThemeWeekStatistics(id);
    }

    public PageInfo<AeaAnaThemeWeekStatistics> listAeaAnaThemeWeekStatistics(AeaAnaThemeWeekStatistics aeaAnaThemeWeekStatistics, Page page) throws Exception {
        PageHelper.startPage(page);
        List<AeaAnaThemeWeekStatistics> list = aeaAnaThemeWeekStatisticsMapper.listAeaAnaThemeWeekStatistics(aeaAnaThemeWeekStatistics);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaAnaThemeWeekStatistics>(list);
    }

    public AeaAnaThemeWeekStatistics getAeaAnaThemeWeekStatisticsById(String id) throws Exception {
        if (id == null)
            throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaAnaThemeWeekStatisticsMapper.getAeaAnaThemeWeekStatisticsById(id);
    }

    public List<AeaAnaThemeWeekStatistics> listAeaAnaThemeWeekStatistics(AeaAnaThemeWeekStatistics aeaAnaThemeWeekStatistics) throws Exception {
        List<AeaAnaThemeWeekStatistics> list = aeaAnaThemeWeekStatisticsMapper.listAeaAnaThemeWeekStatistics(aeaAnaThemeWeekStatistics);
        logger.debug("成功执行查询list！！");
        return list;
    }
}

