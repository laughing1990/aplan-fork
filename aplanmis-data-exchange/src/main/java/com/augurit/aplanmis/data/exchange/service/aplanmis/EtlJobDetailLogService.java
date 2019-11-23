package com.augurit.aplanmis.data.exchange.service.aplanmis;

import com.augurit.aplanmis.data.exchange.domain.aplanmis.EtlJobDetailLog;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yinlf
 * @date 2019//09/02
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
public interface EtlJobDetailLogService {

    void saveEtlJobDetailLog(EtlJobDetailLog etlJobDetailLog);

    void updateEtlJobDetailLog(EtlJobDetailLog etlJobDetailLog);

    void deleteEtlJobDetailLogById(String id);

    PageInfo<EtlJobDetailLog> listEtlJobDetailLog(EtlJobDetailLog etlJobDetailLog, Page page);

    EtlJobDetailLog getEtlJobDetailLogById(String id);

    List<EtlJobDetailLog> listEtlJobDetailLog(EtlJobDetailLog etlJobDetailLog);

    List<EtlJobDetailLog> findEtlJobDetailLogByJobLogId(String jobLogId);
}