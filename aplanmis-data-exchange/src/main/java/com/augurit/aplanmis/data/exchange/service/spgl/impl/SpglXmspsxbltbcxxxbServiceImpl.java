package com.augurit.aplanmis.data.exchange.service.spgl.impl;

import com.augurit.aplanmis.data.exchange.domain.spgl.SpglXmspsxbltbcxxxb;
import com.augurit.aplanmis.data.exchange.mapper.spgl.SpglXmspsxbltbcxxxbMapper;
import com.augurit.aplanmis.data.exchange.service.spgl.SpglXmspsxbltbcxxxbService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

/**
 * @author ylf_i
 * @date 2019/12/17
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
@Service
@Transactional
public class SpglXmspsxbltbcxxxbServiceImpl implements SpglXmspsxbltbcxxxbService {

    private static Logger logger = LoggerFactory.getLogger(SpglXmspsxbltbcxxxbServiceImpl.class);

    @Autowired
    private SpglXmspsxbltbcxxxbMapper spglXmspsxbltbcxxxbMapper;

    @Override
    public void saveSpglXmspsxbltbcxxxb(SpglXmspsxbltbcxxxb spglXmspsxbltbcxxxb) {
        spglXmspsxbltbcxxxbMapper.insertSpglXmspsxbltbcxxxb(spglXmspsxbltbcxxxb);
    }

    @Override
    public void updateSpglXmspsxbltbcxxxb(SpglXmspsxbltbcxxxb spglXmspsxbltbcxxxb) {
        spglXmspsxbltbcxxxbMapper.updateSpglXmspsxbltbcxxxb(spglXmspsxbltbcxxxb);
    }

    @Override
    public void deleteSpglXmspsxbltbcxxxbById(String id) {
        spglXmspsxbltbcxxxbMapper.deleteSpglXmspsxbltbcxxxb(id);
    }

    @Override
    public PageInfo<SpglXmspsxbltbcxxxb> listSpglXmspsxbltbcxxxb(SpglXmspsxbltbcxxxb spglXmspsxbltbcxxxb, Page page) {
        PageHelper.startPage(page);
        List<SpglXmspsxbltbcxxxb> list = spglXmspsxbltbcxxxbMapper.listSpglXmspsxbltbcxxxb(spglXmspsxbltbcxxxb);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<SpglXmspsxbltbcxxxb>(list);
    }

    @Override
    public SpglXmspsxbltbcxxxb getSpglXmspsxbltbcxxxbById(String id) {
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return spglXmspsxbltbcxxxbMapper.getSpglXmspsxbltbcxxxbById(id);
    }

    @Override
    public List<SpglXmspsxbltbcxxxb> listSpglXmspsxbltbcxxxb(SpglXmspsxbltbcxxxb spglXmspsxbltbcxxxb) {
        List<SpglXmspsxbltbcxxxb> list = spglXmspsxbltbcxxxbMapper.listSpglXmspsxbltbcxxxb(spglXmspsxbltbcxxxb);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public void insert(SpglXmspsxbltbcxxxb spglXmspsxbltbcxxxb) {
        spglXmspsxbltbcxxxbMapper.insertSpglXmspsxbltbcxxxb(spglXmspsxbltbcxxxb);
    }

    @Override
    public void batchInsert(List<SpglXmspsxbltbcxxxb> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        spglXmspsxbltbcxxxbMapper.batchInsertSpglXmspsxbltbcxxxb(list);
    }
}