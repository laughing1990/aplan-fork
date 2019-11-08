package com.augurit.aplanmis.data.exchange.mapper.aplanmis;

import com.augurit.aplanmis.data.exchange.domain.aplanmis.EtlJobLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yinlf
 * @date 2019/09/02
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
@Mapper
@Repository
public interface EtlJobLogMapper {

    void insertEtlJobLog(EtlJobLog etlJobLog);

    void updateEtlJobLog(EtlJobLog etlJobLog);

    void deleteEtlJobLog(@Param("id") String id);

    List<EtlJobLog> listEtlJobLog(EtlJobLog etlJobLog);

    EtlJobLog getEtlJobLogById(@Param("id") String id);

    Long getAutoIncrement(@Param("dbName") String dbName);
}