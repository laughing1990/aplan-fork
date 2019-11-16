package com.augurit.aplanmis.common.service.analyse.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.common.domain.AeaAnaThemeYearStatistics;
import com.augurit.aplanmis.common.mapper.AeaAnaThemeYearStatisticsMapper;
import com.augurit.aplanmis.common.mapper.AeaHiApplyinstMapper;
import com.augurit.aplanmis.common.service.analyse.AeaAnaThemeYearStatisticsService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 主题年申报统计表（在月申报统计表的基础上统计。历史年数据不变。本年数据每日更新，保存的是本年至昨日的数据，页面显示要加上实时计算今日的数据）-Service服务接口实现类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:mayanhao</li>
 * <li>创建时间：2019-09-05 10:35:36</li>
 * </ul>
 */
@Service
@Transactional
public class AeaAnaThemeYearStatisticsServiceImpl implements AeaAnaThemeYearStatisticsService {

    private static Logger logger = LoggerFactory.getLogger(AeaAnaThemeYearStatisticsServiceImpl.class);

    @Autowired
    private AeaAnaThemeYearStatisticsMapper aeaAnaThemeYearStatisticsMapper;
    @Autowired
    private AeaHiApplyinstMapper applyinstMapper;

    public void saveAeaAnaThemeYearStatistics(AeaAnaThemeYearStatistics aeaAnaThemeYearStatistics) throws Exception {
        aeaAnaThemeYearStatisticsMapper.insertAeaAnaThemeYearStatistics(aeaAnaThemeYearStatistics);
    }

    public void updateAeaAnaThemeYearStatistics(AeaAnaThemeYearStatistics aeaAnaThemeYearStatistics) throws Exception {
        aeaAnaThemeYearStatisticsMapper.updateAeaAnaThemeYearStatistics(aeaAnaThemeYearStatistics);
    }

    public void deleteAeaAnaThemeYearStatisticsById(String id) throws Exception {
        if (id == null)
            throw new InvalidParameterException(id);
        aeaAnaThemeYearStatisticsMapper.deleteAeaAnaThemeYearStatistics(id);
    }

    public PageInfo<AeaAnaThemeYearStatistics> listAeaAnaThemeYearStatistics(AeaAnaThemeYearStatistics aeaAnaThemeYearStatistics, Page page) throws Exception {
        PageHelper.startPage(page);
        List<AeaAnaThemeYearStatistics> list = aeaAnaThemeYearStatisticsMapper.listAeaAnaThemeYearStatistics(aeaAnaThemeYearStatistics);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaAnaThemeYearStatistics>(list);
    }

    public AeaAnaThemeYearStatistics getAeaAnaThemeYearStatisticsById(String id) throws Exception {
        if (id == null)
            throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaAnaThemeYearStatisticsMapper.getAeaAnaThemeYearStatisticsById(id);
    }

    public List<AeaAnaThemeYearStatistics> listAeaAnaThemeYearStatistics(AeaAnaThemeYearStatistics aeaAnaThemeYearStatistics) throws Exception {
        List<AeaAnaThemeYearStatistics> list = aeaAnaThemeYearStatisticsMapper.listAeaAnaThemeYearStatistics(aeaAnaThemeYearStatistics);
        logger.debug("成功执行查询list！！");
        return list;
    }
}

