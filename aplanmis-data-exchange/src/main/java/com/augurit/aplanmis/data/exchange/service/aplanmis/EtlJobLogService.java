package com.augurit.aplanmis.data.exchange.service.aplanmis;

import com.augurit.aplanmis.data.exchange.domain.aplanmis.EtlJobLog;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author yinlf
 * @date 2019//09/02
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
public interface EtlJobLogService {
    void saveEtlJobLog(EtlJobLog etlJobLog);

    void updateEtlJobLog(EtlJobLog etlJobLog);

    void deleteEtlJobLogById(String id);

    PageInfo<EtlJobLog> listEtlJobLog(EtlJobLog etlJobLog, Page page);

    EtlJobLog getEtlJobLogById(String id);

    List<EtlJobLog> listEtlJobLog(EtlJobLog etlJobLog);

    Long getAutoIncrement();

    void batchDeleteEtlJobLogByJobLogIds(String[] jobLogIds);
}