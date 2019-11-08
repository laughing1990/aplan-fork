package com.augurit.aplanmis.data.exchange.service.aplanmis.impl;

import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.data.exchange.domain.spgl.SpglDfxmsplcxxb;
import com.augurit.aplanmis.data.exchange.mapper.aplanmis.ThemeVerMapper;
import com.augurit.aplanmis.data.exchange.service.aplanmis.ThemeVerService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author yinlf
 * @date 2019/09/02
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
@Service
@Transactional
public class ThemeVerServiceImpl implements ThemeVerService {

    private static Logger logger = LoggerFactory.getLogger(ThemeVerServiceImpl.class);

    @Autowired
    private ThemeVerMapper themeVerMapper;

    @Override
    public PageInfo<SpglDfxmsplcxxb> listByTimeRange(Date startTime, Date endTime, Page page) {
        PageHelper.startPage(page);
        List<SpglDfxmsplcxxb> list = themeVerMapper.listSpglDfxmsplcxxbByTimeRange(startTime, endTime);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<SpglDfxmsplcxxb> listSpglDfxmsplcxxb(SpglDfxmsplcxxb spglDfxmsplcxxb, Page page) {
        PageHelper.startPage(page);
        List<SpglDfxmsplcxxb> list = themeVerMapper.listSpglDfxmsplcxxb(spglDfxmsplcxxb);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<SpglDfxmsplcxxb>(list);
    }

    @Override
    public SpglDfxmsplcxxb getSpglDfxmsplcxxbById(String id) {
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return themeVerMapper.getSpglDfxmsplcxxbById(id);
    }

    @Override
    public List<SpglDfxmsplcxxb> listSpglDfxmsplcxxb(SpglDfxmsplcxxb spglDfxmsplcxxb) {
        List<SpglDfxmsplcxxb> list = themeVerMapper.listSpglDfxmsplcxxb(spglDfxmsplcxxb);
        logger.debug("成功执行查询list！！");
        return list;
    }
}