package com.augurit.aplanmis.data.exchange.service.duogui.impl;

import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.data.exchange.domain.spgl.SpglXmydhxjzxxb;
import com.augurit.aplanmis.data.exchange.mapper.duogui.LandRedLineMapper;
import com.augurit.aplanmis.data.exchange.service.duogui.LandRedLineService;
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
 * @author qhg
 * @date 2019/11/05
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
@Service
@Transactional
public class LandRedLineServiceImpl implements LandRedLineService {

    private static Logger logger = LoggerFactory.getLogger(LandRedLineServiceImpl.class);

    @Autowired
    private LandRedLineMapper landRedLineMapper;

    @Override
    public PageInfo<SpglXmydhxjzxxb> listByTimeRange(Date startTime, Date endTime, Page page) {
        PageHelper.startPage(page);
        List<SpglXmydhxjzxxb> list = landRedLineMapper.listSpglXmydhxjzxxbByTimeRange(startTime, endTime);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<SpglXmydhxjzxxb> listSpglXmydhxjzxxb(SpglXmydhxjzxxb spglXmydhxjzxxb, Page page) {
        PageHelper.startPage(page);
        List<SpglXmydhxjzxxb> list = landRedLineMapper.listSpglXmydhxjzxxb(spglXmydhxjzxxb);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<SpglXmydhxjzxxb>(list);
    }

    @Override
    public SpglXmydhxjzxxb getSpglXmydhxjzxxbById(String id) {
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return landRedLineMapper.getSpglXmydhxjzxxbById(id);
    }

    @Override
    public List<SpglXmydhxjzxxb> listSpglXmydhxjzxxb(SpglXmydhxjzxxb spglXmydhxjzxxb) {
        List<SpglXmydhxjzxxb> list = landRedLineMapper.listSpglXmydhxjzxxb(spglXmydhxjzxxb);
        logger.debug("成功执行查询list！！");
        return list;
    }
}