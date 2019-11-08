package com.augurit.aplanmis.data.exchange.service.aplanmis.impl;

import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.data.exchange.domain.aplanmis.EtlJob;
import com.augurit.aplanmis.data.exchange.mapper.aplanmis.EtlJobMapper;
import com.augurit.aplanmis.data.exchange.service.aplanmis.EtlJobService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author yinlf
 * @date 2019/09/02
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
@Service
@Transactional
public class EtlJobServiceImpl implements EtlJobService {

    private static Logger logger = LoggerFactory.getLogger(EtlJobServiceImpl.class);

    @Autowired
    private EtlJobMapper etlJobMapper;

    @Override
    public void saveEtlJob(EtlJob etlJob) {
        etlJobMapper.insertEtlJob(etlJob);
    }

    @Override
    public void updateEtlJob(EtlJob etlJob) {
        etlJobMapper.updateEtlJob(etlJob);
    }

    @Override
    public void deleteEtlJobById(String id) {
        etlJobMapper.deleteEtlJob(id);
    }

    @Override
    public PageInfo<EtlJob> listEtlJob(EtlJob etlJob, Page page) {
        PageHelper.startPage(page);
        List<EtlJob> list = etlJobMapper.listEtlJob(etlJob);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<EtlJob>(list);
    }

    @Override
    public EtlJob getEtlJobById(String id) {
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return etlJobMapper.getEtlJobById(id);
    }

    @Override
    public List<EtlJob> listEtlJob(EtlJob etlJob) {
        List<EtlJob> list = etlJobMapper.listEtlJob(etlJob);
        logger.debug("成功执行查询list！！");
        return list;
    }
}