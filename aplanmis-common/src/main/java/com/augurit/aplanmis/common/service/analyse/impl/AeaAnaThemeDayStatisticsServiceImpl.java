package com.augurit.aplanmis.common.service.analyse.impl;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.aplanmis.common.domain.AeaAnaThemeDayStatistics;
import com.augurit.aplanmis.common.mapper.AeaAnaThemeDayStatisticsMapper;
import com.augurit.aplanmis.common.service.analyse.AeaAnaThemeDayStatisticsService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
/**
* 主题日申报统计表-Service服务接口实现类
<ul>
    <li>项目名：奥格erp3.0--第一期建设项目</li>
    <li>版本信息：v1.0</li>
    <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
    <li>创建人:chenjw</li>
    <li>创建时间：2019-09-03 13:41:57</li>
</ul>
*/
@Service
@Transactional
public class AeaAnaThemeDayStatisticsServiceImpl implements AeaAnaThemeDayStatisticsService {

    private static Logger logger = LoggerFactory.getLogger(AeaAnaThemeDayStatisticsServiceImpl.class);

    @Autowired
    private AeaAnaThemeDayStatisticsMapper aeaAnaThemeDayStatisticsMapper;

    public void saveAeaAnaThemeDayStatistics(AeaAnaThemeDayStatistics aeaAnaThemeDayStatistics) throws Exception{
        aeaAnaThemeDayStatisticsMapper.insertAeaAnaThemeDayStatistics(aeaAnaThemeDayStatistics);
    }
    public void updateAeaAnaThemeDayStatistics(AeaAnaThemeDayStatistics aeaAnaThemeDayStatistics) throws Exception{
        aeaAnaThemeDayStatisticsMapper.updateAeaAnaThemeDayStatistics(aeaAnaThemeDayStatistics);
    }
    public void deleteAeaAnaThemeDayStatisticsById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        aeaAnaThemeDayStatisticsMapper.deleteAeaAnaThemeDayStatistics(id);
    }
    public PageInfo<AeaAnaThemeDayStatistics> listAeaAnaThemeDayStatistics(AeaAnaThemeDayStatistics aeaAnaThemeDayStatistics,Page page) throws Exception{
        PageHelper.startPage(page);
        List<AeaAnaThemeDayStatistics> list = aeaAnaThemeDayStatisticsMapper.listAeaAnaThemeDayStatistics(aeaAnaThemeDayStatistics);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaAnaThemeDayStatistics>(list);
    }
    public AeaAnaThemeDayStatistics getAeaAnaThemeDayStatisticsById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaAnaThemeDayStatisticsMapper.getAeaAnaThemeDayStatisticsById(id);
    }
    public List<AeaAnaThemeDayStatistics> listAeaAnaThemeDayStatistics(AeaAnaThemeDayStatistics aeaAnaThemeDayStatistics) throws Exception{
        List<AeaAnaThemeDayStatistics> list = aeaAnaThemeDayStatisticsMapper.listAeaAnaThemeDayStatistics(aeaAnaThemeDayStatistics);
        logger.debug("成功执行查询list！！");
        return list;
    }

    /**
     * 批量插入
     * @param aeaAnaThemeDayStatistics
     * @throws Exception
     */
    public void batchInsertAeaAnaThemeDayStatistics(List<AeaAnaThemeDayStatistics> aeaAnaThemeDayStatistics) throws Exception {
        aeaAnaThemeDayStatisticsMapper.batchInsertAeaAnaThemeDayStatistics(aeaAnaThemeDayStatistics);
    }
}

