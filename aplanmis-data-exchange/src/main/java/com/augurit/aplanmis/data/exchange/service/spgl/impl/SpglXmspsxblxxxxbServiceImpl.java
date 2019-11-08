package com.augurit.aplanmis.data.exchange.service.spgl.impl;

import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.data.exchange.domain.spgl.SpglXmspsxblxxxxb;
import com.augurit.aplanmis.data.exchange.mapper.spgl.SpglXmspsxblxxxxbMapper;
import com.augurit.aplanmis.data.exchange.service.spgl.SpglXmspsxblxxxxbService;
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
public class SpglXmspsxblxxxxbServiceImpl implements SpglXmspsxblxxxxbService {

    private static Logger logger = LoggerFactory.getLogger(SpglXmspsxblxxxxbServiceImpl.class);

    @Autowired
    private SpglXmspsxblxxxxbMapper spglXmspsxblxxxxbMapper;

    @Override
    public void updateSpglXmspsxblxxxxb(SpglXmspsxblxxxxb spglXmspsxblxxxxb) {
        spglXmspsxblxxxxbMapper.updateSpglXmspsxblxxxxb(spglXmspsxblxxxxb);
    }

    @Override
    public void deleteSpglXmspsxblxxxxbById(String id) {
        spglXmspsxblxxxxbMapper.deleteSpglXmspsxblxxxxb(id);
    }

    @Override
    public PageInfo<SpglXmspsxblxxxxb> listSpglXmspsxblxxxxb(SpglXmspsxblxxxxb spglXmspsxblxxxxb, Page page) {
        PageHelper.startPage(page);
        List<SpglXmspsxblxxxxb> list = spglXmspsxblxxxxbMapper.listSpglXmspsxblxxxxb(spglXmspsxblxxxxb);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<SpglXmspsxblxxxxb>(list);
    }

    @Override
    public SpglXmspsxblxxxxb getSpglXmspsxblxxxxbById(String id) {
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return spglXmspsxblxxxxbMapper.getSpglXmspsxblxxxxbById(id);
    }

    @Override
    public List<SpglXmspsxblxxxxb> listSpglXmspsxblxxxxb(SpglXmspsxblxxxxb spglXmspsxblxxxxb) {
        List<SpglXmspsxblxxxxb> list = spglXmspsxblxxxxbMapper.listSpglXmspsxblxxxxb(spglXmspsxblxxxxb);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public void insert(SpglXmspsxblxxxxb spglXmspsxblxxxxb) {
        spglXmspsxblxxxxbMapper.insertSpglXmspsxblxxxxb(spglXmspsxblxxxxb);
    }

    @Override
    public void batchInsert(List<SpglXmspsxblxxxxb> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        spglXmspsxblxxxxbMapper.batchInsertSpglXmspsxblxxxxb(list);
    }
}