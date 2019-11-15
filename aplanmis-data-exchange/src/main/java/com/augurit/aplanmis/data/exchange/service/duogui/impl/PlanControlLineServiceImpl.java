package com.augurit.aplanmis.data.exchange.service.duogui.impl;

import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.data.exchange.domain.spgl.SpglDfghkzxxxb;
import com.augurit.aplanmis.data.exchange.mapper.duogui.PlanControlLineMapper;
import com.augurit.aplanmis.data.exchange.service.duogui.PlanControlLineService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author qhg
 * @date 2019/11/05
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
@Service
@Transactional
public class PlanControlLineServiceImpl implements PlanControlLineService {

    private static Logger logger = LoggerFactory.getLogger(PlanControlLineServiceImpl.class);

    @Autowired
    private PlanControlLineMapper planControlLineMapper;

    @Override
    public PageInfo<SpglDfghkzxxxb> listByTimeRange(Date startTime, Date endTime, Page page) {
        PageHelper.startPage(page);
        List<SpglDfghkzxxxb> list = planControlLineMapper.listSpglDfghkzxxxbByTimeRange(startTime, endTime);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<SpglDfghkzxxxb> listSpglDfghkzxxxb(SpglDfghkzxxxb spglDfghkzxxxb, Page page) {
        PageHelper.startPage(page);
        List<SpglDfghkzxxxb> list = planControlLineMapper.listSpglDfghkzxxxb(spglDfghkzxxxb);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<SpglDfghkzxxxb>(list);
    }

    @Override
    public SpglDfghkzxxxb getSpglDfghkzxxxbById(String id) {
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return planControlLineMapper.getSpglDfghkzxxxbById(id);
    }

    @Override
    public List<SpglDfghkzxxxb> listSpglDfghkzxxxb(SpglDfghkzxxxb spglDfghkzxxxb) {
        List<SpglDfghkzxxxb> list = planControlLineMapper.listSpglDfghkzxxxb(spglDfghkzxxxb);
        logger.debug("成功执行查询list！！");
        return list;
    }
}