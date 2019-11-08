package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaAnaStatisticsRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* 统计数据生成记录表-Mapper数据与持久化接口类
*/
@Mapper
@Repository
public interface AeaAnaStatisticsRecordMapper {
    public void insertAeaAnaStatisticsRecord(AeaAnaStatisticsRecord aeaAnaStatisticsRecord);
    public void updateAeaAnaStatisticsRecord(AeaAnaStatisticsRecord aeaAnaStatisticsRecord);
    public void deleteAeaAnaStatisticsRecord(@Param("id") String id);
    public List <AeaAnaStatisticsRecord> listAeaAnaStatisticsRecord(AeaAnaStatisticsRecord aeaAnaStatisticsRecord);
    public AeaAnaStatisticsRecord getAeaAnaStatisticsRecordById(@Param("id") String id);

    public AeaAnaStatisticsRecord getAeaAnaStatisticsRecord(@Param("reportId") String reportId, @Param("reportName") String reportName, @Param("statisticsStartDate") String statisticsStartDate, @Param("statisticsEndDate") String statisticsEndDate,
                                                            @Param("operateSource") String operateSource, @Param("statisticsType") String statisticsType, @Param("creater") String creater, @Param("rootOrgId") String rootOrgId);
    public List <AeaAnaStatisticsRecord> listStatisticsRecord(@Param("keyword") String keyword);
}
