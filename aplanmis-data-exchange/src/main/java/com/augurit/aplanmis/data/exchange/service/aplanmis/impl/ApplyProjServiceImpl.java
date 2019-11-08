package com.augurit.aplanmis.data.exchange.service.aplanmis.impl;

import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.data.exchange.domain.spgl.SpglXmjbxxb;
import com.augurit.aplanmis.data.exchange.mapper.aplanmis.ApplyProjMapper;
import com.augurit.aplanmis.data.exchange.service.aplanmis.ApplyProjService;
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
public class ApplyProjServiceImpl implements ApplyProjService {

    private static Logger logger = LoggerFactory.getLogger(ApplyProjServiceImpl.class);

    @Autowired
    private ApplyProjMapper applyProjMapper;

    @Override
    public PageInfo<SpglXmjbxxb> listByTimeRange(Date startTime, Date endTime, Page page) {
        PageHelper.startPage(page);
        List<SpglXmjbxxb> list = applyProjMapper.listSpglXmjbxxbByTimeRange(startTime, endTime);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<SpglXmjbxxb> listSpglXmjbxxb(SpglXmjbxxb spglXmjbxxb, Page page) {
        PageHelper.startPage(page);
        List<SpglXmjbxxb> list = applyProjMapper.listSpglXmjbxxb(spglXmjbxxb);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<SpglXmjbxxb>(list);
    }

    @Override
    public SpglXmjbxxb getSpglXmjbxxbById(String id) {
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return applyProjMapper.getSpglXmjbxxbByDfsjzj(id);
    }

    @Override
    public List<SpglXmjbxxb> listSpglXmjbxxb(SpglXmjbxxb spglXmjbxxb) {
        List<SpglXmjbxxb> list = applyProjMapper.listSpglXmjbxxb(spglXmjbxxb);
        logger.debug("成功执行查询list！！");
        return list;
    }
}