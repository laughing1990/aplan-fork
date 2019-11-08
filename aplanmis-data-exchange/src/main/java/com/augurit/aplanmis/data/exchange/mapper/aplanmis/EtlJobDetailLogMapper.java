package com.augurit.aplanmis.data.exchange.mapper.aplanmis;

import com.augurit.aplanmis.data.exchange.domain.aplanmis.EtlJobDetailLog;
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
public interface EtlJobDetailLogMapper {

    void insertEtlJobDetailLog(EtlJobDetailLog etlJobDetailLog);

    void updateEtlJobDetailLog(EtlJobDetailLog etlJobDetailLog);

    void deleteEtlJobDetailLog(@Param("id") String id);

    List<EtlJobDetailLog> listEtlJobDetailLog(EtlJobDetailLog etlJobDetailLog);

    EtlJobDetailLog getEtlJobDetailLogById(@Param("id") String id);
}