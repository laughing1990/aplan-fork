package com.augurit.aplanmis.data.exchange.service.aplanmis.impl;

import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.data.exchange.domain.spgl.SpglXmspsxpfwjxxb;
import com.augurit.aplanmis.data.exchange.mapper.aplanmis.OfficialDocMapper;
import com.augurit.aplanmis.data.exchange.service.aplanmis.OfficialDocService;
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
public class OfficialDocServiceImpl implements OfficialDocService {

    private static Logger logger = LoggerFactory.getLogger(OfficialDocServiceImpl.class);

    @Autowired
    private OfficialDocMapper officialDocMapper;

    @Override
    public PageInfo<SpglXmspsxpfwjxxb> listByTimeRange(Date startTime, Date endTime, Page page) {
        PageHelper.startPage(page);
        List<SpglXmspsxpfwjxxb> list = officialDocMapper.listSpglXmspsxpfwjxxbByTimeRange(startTime, endTime);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<SpglXmspsxpfwjxxb> listSpglXmspsxpfwjxxb(SpglXmspsxpfwjxxb spglXmspsxpfwjxxb, Page page) {
        PageHelper.startPage(page);
        List<SpglXmspsxpfwjxxb> list = officialDocMapper.listSpglXmspsxpfwjxxb(spglXmspsxpfwjxxb);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<SpglXmspsxpfwjxxb>(list);
    }

    @Override
    public SpglXmspsxpfwjxxb getSpglXmspsxpfwjxxbById(String id) {
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return officialDocMapper.getSpglXmspsxpfwjxxbById(id);
    }

    @Override
    public List<SpglXmspsxpfwjxxb> listSpglXmspsxpfwjxxb(SpglXmspsxpfwjxxb spglXmspsxpfwjxxb) {
        List<SpglXmspsxpfwjxxb> list = officialDocMapper.listSpglXmspsxpfwjxxb(spglXmspsxpfwjxxb);
        logger.debug("成功执行查询list！！");
        return list;
    }
}