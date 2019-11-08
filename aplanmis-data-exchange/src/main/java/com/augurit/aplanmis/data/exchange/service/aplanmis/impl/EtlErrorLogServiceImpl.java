package com.augurit.aplanmis.data.exchange.service.aplanmis.impl;

import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.data.exchange.constant.EtlConstant;
import com.augurit.aplanmis.data.exchange.constant.EtlError;
import com.augurit.aplanmis.data.exchange.domain.aplanmis.EtlErrorLog;
import com.augurit.aplanmis.data.exchange.domain.base.SpglEntity;
import com.augurit.aplanmis.data.exchange.exception.EtlTransException;
import com.augurit.aplanmis.data.exchange.mapper.aplanmis.EtlErrorLogMapper;
import com.augurit.aplanmis.data.exchange.service.aplanmis.EtlErrorLogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author yinlf
 * @date 2019/09/02
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = RuntimeException.class)
public class EtlErrorLogServiceImpl implements EtlErrorLogService {

    private static Logger logger = LoggerFactory.getLogger(EtlErrorLogServiceImpl.class);

    @Autowired
    private EtlErrorLogMapper etlErrorLogMapper;

    @Override
    public void saveEtlErrorLog(EtlErrorLog etlErrorLog) {
        etlErrorLogMapper.insertEtlErrorLog(etlErrorLog);
    }

    @Override
    public void insertEtlErrorLog(String stepName, String tableName, String recordId, Exception e, Long jobLogId) {
        EtlErrorLog log = this.createEtlErrorLog(stepName, tableName, recordId, e, jobLogId);
        etlErrorLogMapper.insertEtlErrorLog(log);
    }

    @Override
    public void insertEtlErrorLogWithException(SpglEntity spglEntity, Exception e, Long jobLogId) {
        EtlErrorLog log = new EtlErrorLog();
        log.setJobLogId(jobLogId);
        log.setJobName(EtlConstant.JOB_NAME_UPLOAD_PROVINCE);
        log.setStepName(spglEntity.getStepName());
        log.setTableName(spglEntity.getTableName());
        log.setRecordId(spglEntity.getDfsjzj());
        if (e instanceof EtlTransException) {
            EtlTransException etlTransException = (EtlTransException) e;
            log.setErrCode(etlTransException.getCode());
            log.setDiagnoseResult(etlTransException.getSolution());
            log.setErrValue(etlTransException.getErrValue());
        } else {
            log.setErrCode(e.getClass().getSimpleName());
        }
        log.setErrDesc(e.getMessage());

        log.setCreateTime(new Date());
        etlErrorLogMapper.insertEtlErrorLog(log);
    }

    @Override
    public void updateEtlErrorLog(EtlErrorLog etlErrorLog) {
        etlErrorLogMapper.updateEtlErrorLog(etlErrorLog);
    }

    @Override
    public void deleteEtlErrorLogById(String id) {
        etlErrorLogMapper.deleteEtlErrorLog(id);
    }

    @Override
    public PageInfo<EtlErrorLog> listEtlErrorLog(EtlErrorLog etlErrorLog, Page page) {
        PageHelper.startPage(page);
        List<EtlErrorLog> list = etlErrorLogMapper.listEtlErrorLog(etlErrorLog);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<EtlErrorLog>(list);
    }

    @Override
    public PageInfo<EtlErrorLog> listUnDiagnoseEtlErrorLog(Page page) {
        PageHelper.startPage(page);
        List<EtlErrorLog> list = etlErrorLogMapper.findUnDiagnoseEtlErrorLog();
        logger.debug("分页查询未诊断的错误日志");
        return new PageInfo<>(list);
    }

    @Override
    public EtlErrorLog getEtlErrorLogById(String id) {
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return etlErrorLogMapper.getEtlErrorLogById(id);
    }

    @Override
    public List<EtlErrorLog> listEtlErrorLog(EtlErrorLog etlErrorLog) {
        List<EtlErrorLog> list = etlErrorLogMapper.listEtlErrorLog(etlErrorLog);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public EtlErrorLog createEtlErrorLog(String stepName, String tableName, String recordId, Exception e, Long jobLogId) {
        EtlErrorLog log = new EtlErrorLog();
        log.setJobLogId(jobLogId);
        log.setJobName(EtlConstant.JOB_NAME_UPLOAD_PROVINCE);
        log.setStepName(stepName);
        log.setTableName(tableName);
        log.setRecordId(recordId);
        log.setErrCode(e.getClass().getSimpleName());
        log.setErrDesc(e.getMessage());
        log.setCreateTime(new Date());
        return log;
    }

    @Override
    public List<EtlErrorLog> findEtlErrorLongByErrCode(String ErrCode) {
        EtlErrorLog etlErrorLog = new EtlErrorLog();
        etlErrorLog.setErrCode(ErrCode);
        return etlErrorLogMapper.listEtlErrorLog(etlErrorLog);
    }

    @Override
    public List<EtlErrorLog> findEtlErrorLongByTableName(String tableName) {
        EtlErrorLog etlErrorLog = new EtlErrorLog();
        etlErrorLog.setTableName(tableName);
        return etlErrorLogMapper.listEtlErrorLog(etlErrorLog);
    }

    @Override
    public List<EtlErrorLog> findEtlErrorLongByCodeAndTableName(String ErrCode, String tableName) {
        EtlErrorLog etlErrorLog = new EtlErrorLog();
        etlErrorLog.setErrCode(ErrCode);
        etlErrorLog.setTableName(tableName);
        return etlErrorLogMapper.listEtlErrorLog(etlErrorLog);
    }
}