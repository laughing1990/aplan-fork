package com.augurit.aplanmis.data.exchange.service.spgl.impl;

import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.data.exchange.domain.spgl.SpglDfxmsplcxxb;
import com.augurit.aplanmis.data.exchange.mapper.spgl.SpglDfxmsplcxxbMapper;
import com.augurit.aplanmis.data.exchange.service.spgl.SpglDfxmsplcxxbService;
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
public class SpglDfxmsplcxxbServiceImpl implements SpglDfxmsplcxxbService {

    private static Logger logger = LoggerFactory.getLogger(SpglDfxmsplcxxbServiceImpl.class);

    @Autowired
    private SpglDfxmsplcxxbMapper spglDfxmsplcxxbMapper;

    @Override
    public boolean findActiveSpglDfxmsplcxxbByUnique(String splcbm, Double splcbbh) {
        boolean exist = false;
        Integer i = spglDfxmsplcxxbMapper.findActiveSpglDfxmsplcxxbByUnique(splcbm, splcbbh);
        if(i>0){
            exist = true;
        }
        return exist;
    }

    @Override
    public void updateSpglDfxmsplcxxb(SpglDfxmsplcxxb spglDfxmsplcxxb) {
        spglDfxmsplcxxbMapper.updateSpglDfxmsplcxxb(spglDfxmsplcxxb);
    }

    @Override
    public void deleteSpglDfxmsplcxxbById(String id) {
        spglDfxmsplcxxbMapper.deleteSpglDfxmsplcxxb(id);
    }

    @Override
    public PageInfo<SpglDfxmsplcxxb> listSpglDfxmsplcxxb(SpglDfxmsplcxxb spglDfxmsplcxxb, Page page) {
        PageHelper.startPage(page);
        List<SpglDfxmsplcxxb> list = spglDfxmsplcxxbMapper.listSpglDfxmsplcxxb(spglDfxmsplcxxb);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<SpglDfxmsplcxxb>(list);
    }

    @Override
    public SpglDfxmsplcxxb getSpglDfxmsplcxxbById(String id) {
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return spglDfxmsplcxxbMapper.getSpglDfxmsplcxxbById(id);
    }

    @Override
    public List<SpglDfxmsplcxxb> listSpglDfxmsplcxxb(SpglDfxmsplcxxb spglDfxmsplcxxb) {
        List<SpglDfxmsplcxxb> list = spglDfxmsplcxxbMapper.listSpglDfxmsplcxxb(spglDfxmsplcxxb);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public void insert(SpglDfxmsplcxxb spglDfxmsplcxxb) {
        spglDfxmsplcxxbMapper.insertSpglDfxmsplcxxb(spglDfxmsplcxxb);
    }

    @Override
    public void batchInsert(List<SpglDfxmsplcxxb> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        spglDfxmsplcxxbMapper.batchInsertSpglDfxmsplcxxb(list);
    }
}