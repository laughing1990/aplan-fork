package com.augurit.aplanmis.data.exchange.service.spgl.impl;

import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.data.exchange.domain.spgl.SpglXmspsxblxxb;
import com.augurit.aplanmis.data.exchange.mapper.spgl.SpglXmspsxblxxbMapper;
import com.augurit.aplanmis.data.exchange.service.spgl.SpglXmspsxblxxbService;
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
public class SpglXmspsxblxxbServiceImpl implements SpglXmspsxblxxbService {

    private static Logger logger = LoggerFactory.getLogger(SpglXmspsxblxxbServiceImpl.class);

    @Autowired
    private SpglXmspsxblxxbMapper spglXmspsxblxxbMapper;

    @Override
    public void updateSpglXmspsxblxxb(SpglXmspsxblxxb spglXmspsxblxxb) {
        spglXmspsxblxxbMapper.updateSpglXmspsxblxxb(spglXmspsxblxxb);
    }

    @Override
    public void deleteSpglXmspsxblxxbById(String id) {
        spglXmspsxblxxbMapper.deleteSpglXmspsxblxxb(id);
    }

    @Override
    public PageInfo<SpglXmspsxblxxb> listSpglXmspsxblxxb(SpglXmspsxblxxb spglXmspsxblxxb, Page page) {
        PageHelper.startPage(page);
        List<SpglXmspsxblxxb> list = spglXmspsxblxxbMapper.listSpglXmspsxblxxb(spglXmspsxblxxb);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<SpglXmspsxblxxb>(list);
    }

    @Override
    public SpglXmspsxblxxb getSpglXmspsxblxxbById(String id) {
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return spglXmspsxblxxbMapper.getSpglXmspsxblxxbById(id);
    }

    @Override
    public List<SpglXmspsxblxxb> listSpglXmspsxblxxb(SpglXmspsxblxxb spglXmspsxblxxb) {
        List<SpglXmspsxblxxb> list = spglXmspsxblxxbMapper.listSpglXmspsxblxxb(spglXmspsxblxxb);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public void insert(SpglXmspsxblxxb spglXmspsxblxxb) {
        spglXmspsxblxxbMapper.insertSpglXmspsxblxxb(spglXmspsxblxxb);
    }

    @Override
    public void batchInsert(List<SpglXmspsxblxxb> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        spglXmspsxblxxbMapper.batchInsertSpglXmspsxblxxb(list);
    }
}