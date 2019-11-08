package com.augurit.aplanmis.data.exchange.service.spgl.impl;

import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.data.exchange.domain.spgl.SpglXmqtfjxxb;
import com.augurit.aplanmis.data.exchange.mapper.spgl.SpglXmqtfjxxbMapper;
import com.augurit.aplanmis.data.exchange.service.spgl.SpglXmqtfjxxbService;
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
public class SpglXmqtfjxxbServiceImpl implements SpglXmqtfjxxbService {

    private static Logger logger = LoggerFactory.getLogger(SpglXmqtfjxxbServiceImpl.class);

    @Autowired
    private SpglXmqtfjxxbMapper spglXmqtfjxxbMapper;

    @Override
    public void updateSpglXmqtfjxxb(SpglXmqtfjxxb spglXmqtfjxxb) {
        spglXmqtfjxxbMapper.updateSpglXmqtfjxxb(spglXmqtfjxxb);
    }

    @Override
    public void deleteSpglXmqtfjxxbById(String id) {
        spglXmqtfjxxbMapper.deleteSpglXmqtfjxxb(id);
    }

    @Override
    public PageInfo<SpglXmqtfjxxb> listSpglXmqtfjxxb(SpglXmqtfjxxb spglXmqtfjxxb, Page page) {
        PageHelper.startPage(page);
        List<SpglXmqtfjxxb> list = spglXmqtfjxxbMapper.listSpglXmqtfjxxb(spglXmqtfjxxb);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<SpglXmqtfjxxb>(list);
    }

    @Override
    public SpglXmqtfjxxb getSpglXmqtfjxxbById(String id) {
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return spglXmqtfjxxbMapper.getSpglXmqtfjxxbById(id);
    }

    @Override
    public List<SpglXmqtfjxxb> listSpglXmqtfjxxb(SpglXmqtfjxxb spglXmqtfjxxb) {
        List<SpglXmqtfjxxb> list = spglXmqtfjxxbMapper.listSpglXmqtfjxxb(spglXmqtfjxxb);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public void insert(SpglXmqtfjxxb spglXmqtfjxxb) {
        spglXmqtfjxxbMapper.insertSpglXmqtfjxxb(spglXmqtfjxxb);
    }

    @Override
    public void batchInsert(List<SpglXmqtfjxxb> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        spglXmqtfjxxbMapper.batchInsertSpglXmqtfjxxb(list);
    }
}