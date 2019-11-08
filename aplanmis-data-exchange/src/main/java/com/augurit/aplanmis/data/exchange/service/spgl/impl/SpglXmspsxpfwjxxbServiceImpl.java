package com.augurit.aplanmis.data.exchange.service.spgl.impl;

import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.data.exchange.domain.spgl.SpglXmspsxpfwjxxb;
import com.augurit.aplanmis.data.exchange.mapper.spgl.SpglXmspsxpfwjxxbMapper;
import com.augurit.aplanmis.data.exchange.service.spgl.SpglXmspsxpfwjxxbService;
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
public class SpglXmspsxpfwjxxbServiceImpl implements SpglXmspsxpfwjxxbService {

    private static Logger logger = LoggerFactory.getLogger(SpglXmspsxpfwjxxbServiceImpl.class);

    @Autowired
    private SpglXmspsxpfwjxxbMapper spglXmspsxpfwjxxbMapper;

    @Override
    public void updateSpglXmspsxpfwjxxb(SpglXmspsxpfwjxxb spglXmspsxpfwjxxb) {
        spglXmspsxpfwjxxbMapper.updateSpglXmspsxpfwjxxb(spglXmspsxpfwjxxb);
    }

    @Override
    public void deleteSpglXmspsxpfwjxxbById(String id) {
        spglXmspsxpfwjxxbMapper.deleteSpglXmspsxpfwjxxb(id);
    }

    @Override
    public PageInfo<SpglXmspsxpfwjxxb> listSpglXmspsxpfwjxxb(SpglXmspsxpfwjxxb spglXmspsxpfwjxxb, Page page) {
        PageHelper.startPage(page);
        List<SpglXmspsxpfwjxxb> list = spglXmspsxpfwjxxbMapper.listSpglXmspsxpfwjxxb(spglXmspsxpfwjxxb);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<SpglXmspsxpfwjxxb>(list);
    }

    @Override
    public SpglXmspsxpfwjxxb getSpglXmspsxpfwjxxbById(String id) {
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return spglXmspsxpfwjxxbMapper.getSpglXmspsxpfwjxxbById(id);
    }

    @Override
    public List<SpglXmspsxpfwjxxb> listSpglXmspsxpfwjxxb(SpglXmspsxpfwjxxb spglXmspsxpfwjxxb) {
        List<SpglXmspsxpfwjxxb> list = spglXmspsxpfwjxxbMapper.listSpglXmspsxpfwjxxb(spglXmspsxpfwjxxb);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public void insert(SpglXmspsxpfwjxxb spglXmspsxpfwjxxb) {
        spglXmspsxpfwjxxbMapper.insertSpglXmspsxpfwjxxb(spglXmspsxpfwjxxb);
    }

    @Override
    public void batchInsert(List<SpglXmspsxpfwjxxb> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        spglXmspsxpfwjxxbMapper.batchInsertSpglXmspsxpfwjxxb(list);
    }
}