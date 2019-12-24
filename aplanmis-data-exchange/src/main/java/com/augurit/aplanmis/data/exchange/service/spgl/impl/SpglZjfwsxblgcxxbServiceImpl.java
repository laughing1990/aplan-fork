package com.augurit.aplanmis.data.exchange.service.spgl.impl;

import com.augurit.aplanmis.data.exchange.domain.spgl.SpglZjfwsxblgcxxb;
import com.augurit.aplanmis.data.exchange.mapper.spgl.SpglZjfwsxblgcxxbMapper;
import com.augurit.aplanmis.data.exchange.service.spgl.SpglZjfwsxblgcxxbService;
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
public class SpglZjfwsxblgcxxbServiceImpl implements SpglZjfwsxblgcxxbService {

    private static Logger logger = LoggerFactory.getLogger(SpglZjfwsxblgcxxbServiceImpl.class);

    @Autowired
    private SpglZjfwsxblgcxxbMapper spglZjfwsxblgcxxbMapper;

    @Override
    public void saveSpglZjfwsxblgcxxb(SpglZjfwsxblgcxxb spglZjfwsxblgcxxb) {
        spglZjfwsxblgcxxbMapper.insertSpglZjfwsxblgcxxb(spglZjfwsxblgcxxb);
    }

    @Override
    public void updateSpglZjfwsxblgcxxb(SpglZjfwsxblgcxxb spglZjfwsxblgcxxb) {
        spglZjfwsxblgcxxbMapper.updateSpglZjfwsxblgcxxb(spglZjfwsxblgcxxb);
    }

    @Override
    public void deleteSpglZjfwsxblgcxxbById(String id) {
        spglZjfwsxblgcxxbMapper.deleteSpglZjfwsxblgcxxb(id);
    }

    @Override
    public PageInfo<SpglZjfwsxblgcxxb> listSpglZjfwsxblgcxxb(SpglZjfwsxblgcxxb spglZjfwsxblgcxxb, Page page) {
        PageHelper.startPage(page);
        List<SpglZjfwsxblgcxxb> list = spglZjfwsxblgcxxbMapper.listSpglZjfwsxblgcxxb(spglZjfwsxblgcxxb);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<SpglZjfwsxblgcxxb>(list);
    }

    @Override
    public SpglZjfwsxblgcxxb getSpglZjfwsxblgcxxbById(String id) {
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return spglZjfwsxblgcxxbMapper.getSpglZjfwsxblgcxxbById(id);
    }

    @Override
    public List<SpglZjfwsxblgcxxb> listSpglZjfwsxblgcxxb(SpglZjfwsxblgcxxb spglZjfwsxblgcxxb) {
        List<SpglZjfwsxblgcxxb> list = spglZjfwsxblgcxxbMapper.listSpglZjfwsxblgcxxb(spglZjfwsxblgcxxb);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public void insert(SpglZjfwsxblgcxxb spglZjfwsxblgcxxb) {
        spglZjfwsxblgcxxbMapper.insertSpglZjfwsxblgcxxb(spglZjfwsxblgcxxb);
    }

    @Override
    public void batchInsert(List<SpglZjfwsxblgcxxb> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        spglZjfwsxblgcxxbMapper.batchInsertSpglZjfwsxblgcxxb(list);
    }
}