package com.augurit.aplanmis.data.exchange.service.spgl.impl;

import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.data.exchange.domain.spgl.SpglXmqqyjxxb;
import com.augurit.aplanmis.data.exchange.mapper.spgl.SpglXmqqyjxxbMapper;
import com.augurit.aplanmis.data.exchange.service.spgl.SpglXmqqyjxxbService;
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
 * @date 2019/11/05
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
@Service
@Transactional
public class SpglXmqqyjxxbServiceImpl implements SpglXmqqyjxxbService {

    private static Logger logger = LoggerFactory.getLogger(SpglXmqqyjxxbServiceImpl.class);

    @Autowired
    private SpglXmqqyjxxbMapper spglXmqqyjxxbMapper;

    @Override
    public void updateSpglXmqqyjxxb(SpglXmqqyjxxb spglXmqqyjxxb) {
        spglXmqqyjxxbMapper.updateSpglXmqqyjxxb(spglXmqqyjxxb);
    }

    @Override
    public void deleteSpglXmqqyjxxbById(String id) {
        spglXmqqyjxxbMapper.deleteSpglXmqqyjxxb(id);
    }

    @Override
    public PageInfo<SpglXmqqyjxxb> listSpglXmqqyjxxb(SpglXmqqyjxxb spglXmqqyjxxb, Page page) {
        PageHelper.startPage(page);
        List<SpglXmqqyjxxb> list = spglXmqqyjxxbMapper.listSpglXmqqyjxxb(spglXmqqyjxxb);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<SpglXmqqyjxxb>(list);
    }

    @Override
    public SpglXmqqyjxxb getSpglXmqqyjxxbById(String id) {
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return spglXmqqyjxxbMapper.getSpglXmqqyjxxbById(id);
    }

    @Override
    public List<SpglXmqqyjxxb> listSpglXmqqyjxxb(SpglXmqqyjxxb spglXmqqyjxxb) {
        List<SpglXmqqyjxxb> list = spglXmqqyjxxbMapper.listSpglXmqqyjxxb(spglXmqqyjxxb);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public void insert(SpglXmqqyjxxb spglXmqqyjxxb) {
        spglXmqqyjxxbMapper.insertSpglXmqqyjxxb(spglXmqqyjxxb);
    }

    @Override
    public void batchInsert(List<SpglXmqqyjxxb> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        spglXmqqyjxxbMapper.batchInsertSpglXmqqyjxxb(list);
    }
}