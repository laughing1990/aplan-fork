package com.augurit.aplanmis.common.service.analyse;
import com.augurit.aplanmis.common.domain.AeaAnaThemeDayStatistics;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import java.util.List;
/**
* 主题日申报统计表-Service服务调用接口类
<ul>
    <li>项目名：奥格erp3.0--第一期建设项目</li>
    <li>版本信息：v1.0</li>
    <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
    <li>创建人:chenjw</li>
    <li>创建时间：2019-09-03 13:41:57</li>
</ul>
*/
public interface AeaAnaThemeDayStatisticsService {
    public void saveAeaAnaThemeDayStatistics(AeaAnaThemeDayStatistics aeaAnaThemeDayStatistics) throws Exception;
    public void updateAeaAnaThemeDayStatistics(AeaAnaThemeDayStatistics aeaAnaThemeDayStatistics) throws Exception;
    public void deleteAeaAnaThemeDayStatisticsById(String id) throws Exception;
    public PageInfo<AeaAnaThemeDayStatistics> listAeaAnaThemeDayStatistics(AeaAnaThemeDayStatistics aeaAnaThemeDayStatistics, Page page) throws Exception;
    public AeaAnaThemeDayStatistics getAeaAnaThemeDayStatisticsById(String id) throws Exception;
    public List<AeaAnaThemeDayStatistics> listAeaAnaThemeDayStatistics(AeaAnaThemeDayStatistics aeaAnaThemeDayStatistics) throws Exception;

    public void batchInsertAeaAnaThemeDayStatistics(List<AeaAnaThemeDayStatistics> aeaAnaThemeDayStatistics) throws Exception;
}
