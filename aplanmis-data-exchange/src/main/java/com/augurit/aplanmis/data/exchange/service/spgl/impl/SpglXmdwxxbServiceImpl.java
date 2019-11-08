package com.augurit.aplanmis.data.exchange.service.spgl.impl;

import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.data.exchange.domain.spgl.SpglXmdwxxb;
import com.augurit.aplanmis.data.exchange.mapper.spgl.SpglXmdwxxbMapper;
import com.augurit.aplanmis.data.exchange.service.spgl.SpglXmdwxxbService;
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
public class SpglXmdwxxbServiceImpl implements SpglXmdwxxbService {

    private static Logger logger = LoggerFactory.getLogger(SpglXmdwxxbServiceImpl.class);

    @Autowired
    private SpglXmdwxxbMapper spglXmdwxxbMapper;

    @Override
    public void updateSpglXmdwxxb(SpglXmdwxxb spglXmdwxxb) {
        spglXmdwxxbMapper.updateSpglXmdwxxb(spglXmdwxxb);
    }

    @Override
    public void deleteSpglXmdwxxbById(String id) {
        spglXmdwxxbMapper.deleteSpglXmdwxxb(id);
    }

    @Override
    public PageInfo<SpglXmdwxxb> listSpglXmdwxxb(SpglXmdwxxb spglXmdwxxb, Page page) {
        PageHelper.startPage(page);
        List<SpglXmdwxxb> list = spglXmdwxxbMapper.listSpglXmdwxxb(spglXmdwxxb);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<SpglXmdwxxb>(list);
    }

    @Override
    public SpglXmdwxxb getSpglXmdwxxbById(String id) {
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return spglXmdwxxbMapper.getSpglXmdwxxbById(id);
    }

    @Override
    public List<SpglXmdwxxb> listSpglXmdwxxb(SpglXmdwxxb spglXmdwxxb) {
        List<SpglXmdwxxb> list = spglXmdwxxbMapper.listSpglXmdwxxb(spglXmdwxxb);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public void insert(SpglXmdwxxb spglXmdwxxb) {
        spglXmdwxxbMapper.insertSpglXmdwxxb(spglXmdwxxb);
    }

    @Override
    public void batchInsert(List<SpglXmdwxxb> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        spglXmdwxxbMapper.batchInsertSpglXmdwxxb(list);
    }
}