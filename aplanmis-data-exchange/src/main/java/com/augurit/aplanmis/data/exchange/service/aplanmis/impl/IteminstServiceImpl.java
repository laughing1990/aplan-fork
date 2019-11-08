package com.augurit.aplanmis.data.exchange.service.aplanmis.impl;

import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.data.exchange.domain.spgl.SpglXmspsxblxxb;
import com.augurit.aplanmis.data.exchange.mapper.aplanmis.IteminstMapper;
import com.augurit.aplanmis.data.exchange.service.aplanmis.IteminstService;
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
public class IteminstServiceImpl implements IteminstService {

    private static Logger logger = LoggerFactory.getLogger(IteminstServiceImpl.class);

    @Autowired
    private IteminstMapper iteminstMapper;

    @Override
    public PageInfo<SpglXmspsxblxxb> listByTimeRange(Date startTime, Date endTime, Page page) {
        PageHelper.startPage(page);
        List<SpglXmspsxblxxb> list = iteminstMapper.listSpglXmspsxblxxbByTimeRange(startTime, endTime);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<SpglXmspsxblxxb> listSpglXmspsxblxxb(SpglXmspsxblxxb spglXmspsxblxxb, Page page) {
        PageHelper.startPage(page);
        List<SpglXmspsxblxxb> list = iteminstMapper.listSpglXmspsxblxxb(spglXmspsxblxxb);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<SpglXmspsxblxxb>(list);
    }

    @Override
    public SpglXmspsxblxxb getSpglXmspsxblxxbById(String id) {
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return iteminstMapper.getSpglXmspsxblxxbById(id);
    }

    @Override
    public List<SpglXmspsxblxxb> listSpglXmspsxblxxb(SpglXmspsxblxxb spglXmspsxblxxb) {
        List<SpglXmspsxblxxb> list = iteminstMapper.listSpglXmspsxblxxb(spglXmspsxblxxb);
        logger.debug("成功执行查询list！！");
        return list;
    }
}