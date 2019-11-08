package com.augurit.aplanmis.data.exchange.service.spgl.impl;

import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.data.exchange.domain.spgl.SpglDfxmsplcjdxxb;
import com.augurit.aplanmis.data.exchange.mapper.spgl.SpglDfxmsplcjdxxbMapper;
import com.augurit.aplanmis.data.exchange.service.spgl.SpglDfxmsplcjdxxbService;
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
public class SpglDfxmsplcjdxxbServiceImpl implements SpglDfxmsplcjdxxbService {

    private static Logger logger = LoggerFactory.getLogger(SpglDfxmsplcjdxxbServiceImpl.class);

    @Autowired
    private SpglDfxmsplcjdxxbMapper spglDfxmsplcjdxxbMapper;

    @Override
    public void updateSpglDfxmsplcjdxxb(SpglDfxmsplcjdxxb spglDfxmsplcjdxxb) {
        spglDfxmsplcjdxxbMapper.updateSpglDfxmsplcjdxxb(spglDfxmsplcjdxxb);
    }

    @Override
    public void deleteSpglDfxmsplcjdxxbById(String id) {
        spglDfxmsplcjdxxbMapper.deleteSpglDfxmsplcjdxxb(id);
    }

    @Override
    public PageInfo<SpglDfxmsplcjdxxb> listSpglDfxmsplcjdxxb(SpglDfxmsplcjdxxb spglDfxmsplcjdxxb, Page page) {
        PageHelper.startPage(page);
        List<SpglDfxmsplcjdxxb> list = spglDfxmsplcjdxxbMapper.listSpglDfxmsplcjdxxb(spglDfxmsplcjdxxb);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<SpglDfxmsplcjdxxb>(list);
    }

    @Override
    public SpglDfxmsplcjdxxb getSpglDfxmsplcjdxxbById(String id) {
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return spglDfxmsplcjdxxbMapper.getSpglDfxmsplcjdxxbById(id);
    }

    @Override
    public List<SpglDfxmsplcjdxxb> listSpglDfxmsplcjdxxb(SpglDfxmsplcjdxxb spglDfxmsplcjdxxb) {
        List<SpglDfxmsplcjdxxb> list = spglDfxmsplcjdxxbMapper.listSpglDfxmsplcjdxxb(spglDfxmsplcjdxxb);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public void insert(SpglDfxmsplcjdxxb spglDfxmsplcjdxxb) {
        spglDfxmsplcjdxxbMapper.insertSpglDfxmsplcjdxxb(spglDfxmsplcjdxxb);
    }

    @Override
    public void batchInsert(List<SpglDfxmsplcjdxxb> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        spglDfxmsplcjdxxbMapper.batchInsertSpglDfxmsplcjdxxb(list);
    }
}