package com.augurit.aplanmis.data.exchange.service.spgl.impl;

import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.data.exchange.domain.spgl.SpglDfxmsplcjdsxxxb;
import com.augurit.aplanmis.data.exchange.mapper.spgl.SpglDfxmsplcjdsxxxbMapper;
import com.augurit.aplanmis.data.exchange.service.spgl.SpglDfxmsplcjdsxxxbService;
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
public class SpglDfxmsplcjdsxxxbServiceImpl implements SpglDfxmsplcjdsxxxbService {

    private static Logger logger = LoggerFactory.getLogger(SpglDfxmsplcjdsxxxbServiceImpl.class);

    @Autowired
    private SpglDfxmsplcjdsxxxbMapper spglDfxmsplcjdsxxxbMapper;

    @Override
    public boolean findActiveSpglDfxmsplcjdsxxxbByUnique(String splcbm, Double splcbbh, String spsxbm, Double spsxbbh) {
        boolean exist = false;
        Integer i = spglDfxmsplcjdsxxxbMapper.findActiveSpglDfxmsplcjdsxxxbByUnique(splcbm, splcbbh, spsxbm, spsxbbh);
        if (i > 0) {
            exist = true;
        }
        return exist;
    }

    @Override
    public void updateSpglDfxmsplcjdsxxxb(SpglDfxmsplcjdsxxxb spglDfxmsplcjdsxxxb) {
        spglDfxmsplcjdsxxxbMapper.updateSpglDfxmsplcjdsxxxb(spglDfxmsplcjdsxxxb);
    }

    @Override
    public void deleteSpglDfxmsplcjdsxxxbById(String id) {
        spglDfxmsplcjdsxxxbMapper.deleteSpglDfxmsplcjdsxxxb(id);
    }

    @Override
    public PageInfo<SpglDfxmsplcjdsxxxb> listSpglDfxmsplcjdsxxxb(SpglDfxmsplcjdsxxxb spglDfxmsplcjdsxxxb, Page page) {
        PageHelper.startPage(page);
        List<SpglDfxmsplcjdsxxxb> list = spglDfxmsplcjdsxxxbMapper.listSpglDfxmsplcjdsxxxb(spglDfxmsplcjdsxxxb);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<SpglDfxmsplcjdsxxxb>(list);
    }

    @Override
    public SpglDfxmsplcjdsxxxb getSpglDfxmsplcjdsxxxbById(String id) {
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return spglDfxmsplcjdsxxxbMapper.getSpglDfxmsplcjdsxxxbById(id);
    }

    @Override
    public List<SpglDfxmsplcjdsxxxb> listSpglDfxmsplcjdsxxxb(SpglDfxmsplcjdsxxxb spglDfxmsplcjdsxxxb) {
        List<SpglDfxmsplcjdsxxxb> list = spglDfxmsplcjdsxxxbMapper.listSpglDfxmsplcjdsxxxb(spglDfxmsplcjdsxxxb);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public void insert(SpglDfxmsplcjdsxxxb spglDfxmsplcjdsxxxb) {
        spglDfxmsplcjdsxxxbMapper.insertSpglDfxmsplcjdsxxxb(spglDfxmsplcjdsxxxb);
    }

    @Override
    public void batchInsert(List<SpglDfxmsplcjdsxxxb> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        spglDfxmsplcjdsxxxbMapper.batchInsertSpglDfxmsplcjdsxxxb(list);
    }
}