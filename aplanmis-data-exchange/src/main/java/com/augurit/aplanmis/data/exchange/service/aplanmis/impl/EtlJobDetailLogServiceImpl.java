package com.augurit.aplanmis.data.exchange.service.aplanmis.impl;

import com.augurit.aplanmis.data.exchange.domain.aplanmis.EtlJobDetailLog;
import com.augurit.aplanmis.data.exchange.mapper.aplanmis.EtlJobDetailLogMapper;
import com.augurit.aplanmis.data.exchange.service.aplanmis.EtlJobDetailLogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

/**
 * @author yinlf
 * @date 2019/09/02
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
@Service
@Transactional
public class EtlJobDetailLogServiceImpl implements EtlJobDetailLogService {

    private static Logger logger = LoggerFactory.getLogger(EtlJobDetailLogServiceImpl.class);

    @Autowired
    private EtlJobDetailLogMapper etlJobDetailLogMapper;

    @Override
    public void saveEtlJobDetailLog(EtlJobDetailLog etlJobDetailLog) {
        etlJobDetailLogMapper.insertEtlJobDetailLog(etlJobDetailLog);
    }

    @Override
    public void updateEtlJobDetailLog(EtlJobDetailLog etlJobDetailLog) {
        etlJobDetailLogMapper.updateEtlJobDetailLog(etlJobDetailLog);
    }

    @Override
    public void deleteEtlJobDetailLogById(String id) {
        etlJobDetailLogMapper.deleteEtlJobDetailLog(id);
    }

    @Override
    public PageInfo<EtlJobDetailLog> listEtlJobDetailLog(EtlJobDetailLog etlJobDetailLog, Page page) {
        PageHelper.startPage(page);
        List<EtlJobDetailLog> list = etlJobDetailLogMapper.listEtlJobDetailLog(etlJobDetailLog);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<EtlJobDetailLog>(list);
    }

    @Override
    public EtlJobDetailLog getEtlJobDetailLogById(String id) {
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return etlJobDetailLogMapper.getEtlJobDetailLogById(id);
    }

    @Override
    public List<EtlJobDetailLog> listEtlJobDetailLog(EtlJobDetailLog etlJobDetailLog) {
        List<EtlJobDetailLog> list = etlJobDetailLogMapper.listEtlJobDetailLog(etlJobDetailLog);
        logger.debug("成功执行查询list！！");
        return list;
    }
}