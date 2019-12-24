package com.augurit.aplanmis.data.exchange.service.spgl.impl;

import com.augurit.aplanmis.data.exchange.domain.spgl.SpglZjfwsxblxxb;
import com.augurit.aplanmis.data.exchange.mapper.spgl.SpglZjfwsxblxxbMapper;
import com.augurit.aplanmis.data.exchange.service.spgl.SpglZjfwsxblxxbService;
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
public class SpglZjfwsxblxxbServiceImpl implements SpglZjfwsxblxxbService {

    private static Logger logger = LoggerFactory.getLogger(SpglZjfwsxblxxbServiceImpl.class);

    @Autowired
    private SpglZjfwsxblxxbMapper spglZjfwsxblxxbMapper;

    @Override
    public void saveSpglZjfwsxblxxb(SpglZjfwsxblxxb spglZjfwsxblxxb) {
        spglZjfwsxblxxbMapper.insertSpglZjfwsxblxxb(spglZjfwsxblxxb);
    }

    @Override
    public void updateSpglZjfwsxblxxb(SpglZjfwsxblxxb spglZjfwsxblxxb) {
        spglZjfwsxblxxbMapper.updateSpglZjfwsxblxxb(spglZjfwsxblxxb);
    }

    @Override
    public void deleteSpglZjfwsxblxxbById(String id) {
        spglZjfwsxblxxbMapper.deleteSpglZjfwsxblxxb(id);
    }

    @Override
    public PageInfo<SpglZjfwsxblxxb> listSpglZjfwsxblxxb(SpglZjfwsxblxxb spglZjfwsxblxxb, Page page) {
        PageHelper.startPage(page);
        List<SpglZjfwsxblxxb> list = spglZjfwsxblxxbMapper.listSpglZjfwsxblxxb(spglZjfwsxblxxb);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<SpglZjfwsxblxxb>(list);
    }

    @Override
    public SpglZjfwsxblxxb getSpglZjfwsxblxxbById(String id) {
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return spglZjfwsxblxxbMapper.getSpglZjfwsxblxxbById(id);
    }

    @Override
    public List<SpglZjfwsxblxxb> listSpglZjfwsxblxxb(SpglZjfwsxblxxb spglZjfwsxblxxb) {
        List<SpglZjfwsxblxxb> list = spglZjfwsxblxxbMapper.listSpglZjfwsxblxxb(spglZjfwsxblxxb);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public void insert(SpglZjfwsxblxxb spglZjfwsxblxxb) {
        spglZjfwsxblxxbMapper.insertSpglZjfwsxblxxb(spglZjfwsxblxxb);
    }

    @Override
    public void batchInsert(List<SpglZjfwsxblxxb> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        spglZjfwsxblxxbMapper.batchInsertSpglZjfwsxblxxb(list);
    }
}