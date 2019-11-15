package com.augurit.aplanmis.data.exchange.service.spgl.impl;

import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.data.exchange.domain.spgl.SpglDfghkzxxxb;
import com.augurit.aplanmis.data.exchange.mapper.spgl.SpglDfghkzxxxbMapper;
import com.augurit.aplanmis.data.exchange.service.spgl.SpglDfghkzxxxbService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author qhg
 * @date 2019/11/07
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
@Service
@Transactional
public class SpglDfghkzxxxbServiceImpl implements SpglDfghkzxxxbService {

    private static Logger logger = LoggerFactory.getLogger(SpglDfghkzxxxbServiceImpl.class);

    @Autowired
    private SpglDfghkzxxxbMapper spglDfghkzxxxbMapper;

    @Override
    public void updateSpglDfghkzxxxb(SpglDfghkzxxxb spglDfghkzxxxb) {
        spglDfghkzxxxbMapper.updateSpglDfghkzxxxb(spglDfghkzxxxb);
    }

    @Override
    public void deleteSpglDfghkzxxxbById(String id) {
        spglDfghkzxxxbMapper.deleteSpglDfghkzxxxb(id);
    }

    @Override
    public PageInfo<SpglDfghkzxxxb> listSpglDfghkzxxxb(SpglDfghkzxxxb spglDfghkzxxxb, Page page) {
        PageHelper.startPage(page);
        List<SpglDfghkzxxxb> list = spglDfghkzxxxbMapper.listSpglDfghkzxxxb(spglDfghkzxxxb);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<SpglDfghkzxxxb>(list);
    }

    @Override
    public SpglDfghkzxxxb getSpglDfghkzxxxbById(String id) {
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return spglDfghkzxxxbMapper.getSpglDfghkzxxxbById(id);
    }

    @Override
    public List<SpglDfghkzxxxb> listSpglDfghkzxxxb(SpglDfghkzxxxb spglDfghkzxxxb) {
        List<SpglDfghkzxxxb> list = spglDfghkzxxxbMapper.listSpglDfghkzxxxb(spglDfghkzxxxb);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public void insert(SpglDfghkzxxxb spglDfghkzxxxb) {
        spglDfghkzxxxbMapper.insertSpglDfghkzxxxb(spglDfghkzxxxb);
    }

    @Override
    public void batchInsert(List<SpglDfghkzxxxb> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        spglDfghkzxxxbMapper.batchInsertSpglDfghkzxxxb(list);
    }
}