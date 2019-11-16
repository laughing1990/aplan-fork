package com.augurit.aplanmis.common.service.analyse.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.common.domain.AeaAnaThemeMonthStatistics;
import com.augurit.aplanmis.common.mapper.AeaAnaThemeDayStatisticsMapper;
import com.augurit.aplanmis.common.mapper.AeaAnaThemeMonthStatisticsMapper;
import com.augurit.aplanmis.common.service.analyse.AeaAnaThemeMonthStatisticsService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 主题月申报统计表（在日申报统计表的基础上统计。历史月数据不变。本月数据每日更新，保存的是本月至昨日的数据，页面显示要加上实时计算今日的数据）-Service服务接口实现类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:mayanhao</li>
 * <li>创建时间：2019-09-05 10:35:28</li>
 * </ul>
 */
@Service
@Transactional
public class AeaAnaThemeMonthStatisticsServiceImpl implements AeaAnaThemeMonthStatisticsService {

    private static Logger logger = LoggerFactory.getLogger(AeaAnaThemeMonthStatisticsServiceImpl.class);

    @Autowired
    private AeaAnaThemeMonthStatisticsMapper aeaAnaThemeMonthStatisticsMapper;
    @Autowired
    private AeaAnaThemeDayStatisticsMapper aeaAnaThemeDayStatisticsMapper;

    public void saveAeaAnaThemeMonthStatistics(AeaAnaThemeMonthStatistics aeaAnaThemeMonthStatistics) throws Exception {
        aeaAnaThemeMonthStatisticsMapper.insertAeaAnaThemeMonthStatistics(aeaAnaThemeMonthStatistics);
    }

    public void updateAeaAnaThemeMonthStatistics(AeaAnaThemeMonthStatistics aeaAnaThemeMonthStatistics) throws Exception {
        aeaAnaThemeMonthStatisticsMapper.updateAeaAnaThemeMonthStatistics(aeaAnaThemeMonthStatistics);
    }

    public void deleteAeaAnaThemeMonthStatisticsById(String id) throws Exception {
        if (id == null)
            throw new InvalidParameterException(id);
        aeaAnaThemeMonthStatisticsMapper.deleteAeaAnaThemeMonthStatistics(id);
    }

    public PageInfo<AeaAnaThemeMonthStatistics> listAeaAnaThemeMonthStatistics(AeaAnaThemeMonthStatistics aeaAnaThemeMonthStatistics, Page page) throws Exception {
        PageHelper.startPage(page);
        List<AeaAnaThemeMonthStatistics> list = aeaAnaThemeMonthStatisticsMapper.listAeaAnaThemeMonthStatistics(aeaAnaThemeMonthStatistics);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaAnaThemeMonthStatistics>(list);
    }

    public AeaAnaThemeMonthStatistics getAeaAnaThemeMonthStatisticsById(String id) throws Exception {
        if (id == null)
            throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaAnaThemeMonthStatisticsMapper.getAeaAnaThemeMonthStatisticsById(id);
    }

    public List<AeaAnaThemeMonthStatistics> listAeaAnaThemeMonthStatistics(AeaAnaThemeMonthStatistics aeaAnaThemeMonthStatistics) throws Exception {
        List<AeaAnaThemeMonthStatistics> list = aeaAnaThemeMonthStatisticsMapper.listAeaAnaThemeMonthStatistics(aeaAnaThemeMonthStatistics);
        logger.debug("成功执行查询list！！");
        return list;
    }
}

