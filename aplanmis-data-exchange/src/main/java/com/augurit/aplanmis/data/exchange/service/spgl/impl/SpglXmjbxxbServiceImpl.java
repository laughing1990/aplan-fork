package com.augurit.aplanmis.data.exchange.service.spgl.impl;

import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.data.exchange.domain.spgl.SpglXmjbxxb;
import com.augurit.aplanmis.data.exchange.mapper.spgl.SpglXmjbxxbMapper;
import com.augurit.aplanmis.data.exchange.service.spgl.SpglXmjbxxbService;
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
public class SpglXmjbxxbServiceImpl implements SpglXmjbxxbService {

    private static Logger logger = LoggerFactory.getLogger(SpglXmjbxxbServiceImpl.class);

    @Autowired
    private SpglXmjbxxbMapper spglXmjbxxbMapper;

    @Override
    public void updateSpglXmjbxxb(SpglXmjbxxb spglXmjbxxb) {
        spglXmjbxxbMapper.updateSpglXmjbxxb(spglXmjbxxb);
    }

    @Override
    public void deleteSpglXmjbxxbById(String id) {
        spglXmjbxxbMapper.deleteSpglXmjbxxb(id);
    }

    @Override
    public PageInfo<SpglXmjbxxb> listSpglXmjbxxb(SpglXmjbxxb spglXmjbxxb, Page page) {
        PageHelper.startPage(page);
        List<SpglXmjbxxb> list = spglXmjbxxbMapper.listSpglXmjbxxb(spglXmjbxxb);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<SpglXmjbxxb>(list);
    }

    @Override
    public SpglXmjbxxb getSpglXmjbxxbById(String id) {
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return spglXmjbxxbMapper.getSpglXmjbxxbById(id);
    }

    @Override
    public List<SpglXmjbxxb> listSpglXmjbxxb(SpglXmjbxxb spglXmjbxxb) {
        List<SpglXmjbxxb> list = spglXmjbxxbMapper.listSpglXmjbxxb(spglXmjbxxb);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public void insert(SpglXmjbxxb spglXmjbxxb) {
        spglXmjbxxbMapper.insertSpglXmjbxxb(spglXmjbxxb);
    }

    @Override
    public void batchInsert(List<SpglXmjbxxb> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        spglXmjbxxbMapper.batchInsertSpglXmjbxxb(list);
    }
}