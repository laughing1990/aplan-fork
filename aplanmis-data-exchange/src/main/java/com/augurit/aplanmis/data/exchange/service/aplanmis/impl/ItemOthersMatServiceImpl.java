package com.augurit.aplanmis.data.exchange.service.aplanmis.impl;

import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.data.exchange.domain.spgl.SpglXmqtfjxxb;
import com.augurit.aplanmis.data.exchange.mapper.aplanmis.ItemOthersMatMapper;
import com.augurit.aplanmis.data.exchange.service.aplanmis.ItemOthersMatService;
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
public class ItemOthersMatServiceImpl implements ItemOthersMatService {

    private static Logger logger = LoggerFactory.getLogger(ItemOthersMatServiceImpl.class);

    @Autowired
    private ItemOthersMatMapper itemOthersMatMapper;

    @Override
    public PageInfo<SpglXmqtfjxxb> listByTimeRange(Date startTime, Date endTime, Page page) {
        PageHelper.startPage(page);
        List<SpglXmqtfjxxb> list = itemOthersMatMapper.listSpglXmqtfjxxbByTimeRange(startTime, endTime);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<SpglXmqtfjxxb> listSpglXmqtfjxxb(SpglXmqtfjxxb spglXmqtfjxxb, Page page) {
        PageHelper.startPage(page);
        List<SpglXmqtfjxxb> list = itemOthersMatMapper.listSpglXmqtfjxxb(spglXmqtfjxxb);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<SpglXmqtfjxxb>(list);
    }

    @Override
    public SpglXmqtfjxxb getSpglXmqtfjxxbById(String id) {
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return itemOthersMatMapper.getSpglXmqtfjxxbById(id);
    }

    @Override
    public List<SpglXmqtfjxxb> listSpglXmqtfjxxb(SpglXmqtfjxxb spglXmqtfjxxb) {
        List<SpglXmqtfjxxb> list = itemOthersMatMapper.listSpglXmqtfjxxb(spglXmqtfjxxb);
        logger.debug("成功执行查询list！！");
        return list;
    }
}