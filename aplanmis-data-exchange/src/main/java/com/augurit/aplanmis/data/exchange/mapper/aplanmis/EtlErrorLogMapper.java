package com.augurit.aplanmis.data.exchange.mapper.aplanmis;

import com.augurit.aplanmis.data.exchange.domain.aplanmis.EtlErrorLog;
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
public interface EtlErrorLogMapper {

    void insertEtlErrorLog(EtlErrorLog etlErrorLog);

    void updateEtlErrorLog(EtlErrorLog etlErrorLog);

    void deleteEtlErrorLog(@Param("id") String id);

    List<EtlErrorLog> listEtlErrorLog(EtlErrorLog etlErrorLog);

    EtlErrorLog getEtlErrorLogById(@Param("id") String id);

    List<EtlErrorLog> findUnDiagnoseEtlErrorLog();
}