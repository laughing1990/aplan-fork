package com.augurit.aplanmis.data.exchange.service.aplanmis.impl;

import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.data.exchange.domain.spgl.SpglXmdwxxb;
import com.augurit.aplanmis.data.exchange.mapper.aplanmis.ApplyProjUnitMapper;
import com.augurit.aplanmis.data.exchange.service.aplanmis.ApplyProjUnitService;
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
public class ApplyProjUnitServiceImpl implements ApplyProjUnitService {

    private static Logger logger = LoggerFactory.getLogger(ApplyProjUnitServiceImpl.class);

    @Autowired
    private ApplyProjUnitMapper applyProjUnitMapper;

    @Override
    public PageInfo<SpglXmdwxxb> listByTimeRange(Date startTime, Date endTime, Page page) {
        PageHelper.startPage(page);
        List<SpglXmdwxxb> list = applyProjUnitMapper.listSpglXmdwxxbByTimeRange(startTime, endTime);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<SpglXmdwxxb> listSpglXmdwxxb(SpglXmdwxxb spglXmdwxxb, Page page) {
        PageHelper.startPage(page);
        List<SpglXmdwxxb> list = applyProjUnitMapper.listSpglXmdwxxb(spglXmdwxxb);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<SpglXmdwxxb>(list);
    }

    @Override
    public SpglXmdwxxb getSpglXmdwxxbById(String id) {
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return applyProjUnitMapper.getSpglXmdwxxbById(id);
    }

    @Override
    public List<SpglXmdwxxb> listSpglXmdwxxb(SpglXmdwxxb spglXmdwxxb) {
        List<SpglXmdwxxb> list = applyProjUnitMapper.listSpglXmdwxxb(spglXmdwxxb);
        logger.debug("成功执行查询list！！");
        return list;
    }
}