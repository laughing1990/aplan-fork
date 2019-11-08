package com.augurit.aplanmis.data.exchange.service.aplanmis;

import com.augurit.aplanmis.data.exchange.domain.aplanmis.EtlErrorLog;
import com.augurit.aplanmis.data.exchange.domain.base.SpglEntity;
import com.augurit.aplanmis.data.exchange.exception.EtlTransException;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author yinlf
 * @date 2019//09/02
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
public interface EtlErrorLogService {

    /**
     * 新增上传错误记录
     *
     * @param etlErrorLog
     */
    void saveEtlErrorLog(EtlErrorLog etlErrorLog);

    void insertEtlErrorLog(String stepName, String tableName, String recordId, Exception e, Long jobLogId);

    void insertEtlErrorLogWithException(SpglEntity spglEntity, Exception e, Long jobLogId);

    void updateEtlErrorLog(EtlErrorLog etlErrorLog);

    void deleteEtlErrorLogById(String id);

    PageInfo<EtlErrorLog> listEtlErrorLog(EtlErrorLog etlErrorLog, Page page);


    /**
     * 分页查询未诊断的错误日志
     *
     * @param page
     * @return
     */
    PageInfo<EtlErrorLog> listUnDiagnoseEtlErrorLog(Page page);

    EtlErrorLog getEtlErrorLogById(String id);

    List<EtlErrorLog> listEtlErrorLog(EtlErrorLog etlErrorLog);

    EtlErrorLog createEtlErrorLog(String stepName, String tableName, String recordId, Exception e, Long jobLogId);

    List<EtlErrorLog> findEtlErrorLongByErrCode(String errCode);

    List<EtlErrorLog> findEtlErrorLongByTableName(String tableName);

    List<EtlErrorLog> findEtlErrorLongByCodeAndTableName(String errCode, String tableName);
}