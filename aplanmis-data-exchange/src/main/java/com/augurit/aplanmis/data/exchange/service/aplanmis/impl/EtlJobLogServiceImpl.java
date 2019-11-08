package com.augurit.aplanmis.data.exchange.service.aplanmis.impl;

import com.augurit.agcloud.bsc.util.SpringUtil;
import com.augurit.aplanmis.data.exchange.domain.aplanmis.EtlJobLog;
import com.augurit.aplanmis.data.exchange.mapper.aplanmis.EtlJobLogMapper;
import com.augurit.aplanmis.data.exchange.service.aplanmis.EtlJobLogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.mysql.jdbc.JDBC4Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

/**
 * @author yinlf
 * @date 2019/09/02
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
@Service
@Transactional
public class EtlJobLogServiceImpl implements EtlJobLogService {

    private static Logger logger = LoggerFactory.getLogger(EtlJobLogServiceImpl.class);

    @Autowired
    private EtlJobLogMapper etlJobLogMapper;

    @Value("${spring.datasource.aplanmis.jdbc-url}")
    private String aplanmisJdbcUrl;

    @Override
    public void saveEtlJobLog(EtlJobLog etlJobLog) {
        etlJobLogMapper.insertEtlJobLog(etlJobLog);
    }

    @Override
    public void updateEtlJobLog(EtlJobLog etlJobLog) {
        etlJobLogMapper.updateEtlJobLog(etlJobLog);
    }

    @Override
    public void deleteEtlJobLogById(String id) {
        etlJobLogMapper.deleteEtlJobLog(id);
    }

    @Override
    public PageInfo<EtlJobLog> listEtlJobLog(EtlJobLog etlJobLog, Page page) {
        PageHelper.startPage(page);
        List<EtlJobLog> list = etlJobLogMapper.listEtlJobLog(etlJobLog);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<EtlJobLog>(list);
    }

    @Override
    public EtlJobLog getEtlJobLogById(String id) {
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return etlJobLogMapper.getEtlJobLogById(id);
    }

    @Override
    public List<EtlJobLog> listEtlJobLog(EtlJobLog etlJobLog) {
        List<EtlJobLog> list = etlJobLogMapper.listEtlJobLog(etlJobLog);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public Long getAutoIncrement() {
        try {
           String dbName =  aplanmisJdbcUrl.split("/")[3].split("\\?")[0];
           return etlJobLogMapper.getAutoIncrement(dbName);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}