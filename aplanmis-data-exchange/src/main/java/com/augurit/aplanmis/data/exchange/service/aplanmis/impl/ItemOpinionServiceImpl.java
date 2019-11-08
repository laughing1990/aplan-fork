package com.augurit.aplanmis.data.exchange.service.aplanmis.impl;

import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.data.exchange.domain.spgl.SpglXmspsxblxxxxb;
import com.augurit.aplanmis.data.exchange.mapper.aplanmis.ItemOpinionMapper;
import com.augurit.aplanmis.data.exchange.service.aplanmis.ItemOpinionService;
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
 * @author yinlf
 * @date 2019/09/02
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
@Service
@Transactional
public class ItemOpinionServiceImpl implements ItemOpinionService {

    private static Logger logger = LoggerFactory.getLogger(ItemOpinionServiceImpl.class);

    @Autowired
    private ItemOpinionMapper itemOpinionMapper;

    @Override
    public PageInfo<SpglXmspsxblxxxxb> listByTimeRange(Date startTime, Date endTime, Page page) {
        PageHelper.startPage(page);
        List<SpglXmspsxblxxxxb> list = itemOpinionMapper.listSpglXmspsxblxxxxbByTimeRange(startTime, endTime);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<SpglXmspsxblxxxxb> listSpglXmspsxblxxxxb(SpglXmspsxblxxxxb spglXmspsxblxxxxb, Page page) {
        PageHelper.startPage(page);
        List<SpglXmspsxblxxxxb> list = itemOpinionMapper.listSpglXmspsxblxxxxb(spglXmspsxblxxxxb);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<SpglXmspsxblxxxxb>(list);
    }

    @Override
    public SpglXmspsxblxxxxb getSpglXmspsxblxxxxbById(String id) {
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return itemOpinionMapper.getSpglXmspsxblxxxxbById(id);
    }

    @Override
    public List<SpglXmspsxblxxxxb> listSpglXmspsxblxxxxb(SpglXmspsxblxxxxb spglXmspsxblxxxxb) {
        List<SpglXmspsxblxxxxb> list = itemOpinionMapper.listSpglXmspsxblxxxxb(spglXmspsxblxxxxb);
        logger.debug("成功执行查询list！！");
        return list;
    }
}