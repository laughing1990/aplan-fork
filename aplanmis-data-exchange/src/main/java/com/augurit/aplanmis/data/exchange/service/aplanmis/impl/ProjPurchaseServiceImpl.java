package com.augurit.aplanmis.data.exchange.service.aplanmis.impl;

import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.data.exchange.domain.spgl.SpglZjfwsxblxxb;
import com.augurit.aplanmis.data.exchange.mapper.aplanmis.ProjPurchaseMapper;
import com.augurit.aplanmis.data.exchange.mapper.spgl.SpglZjfwsxblxxbMapper;
import com.augurit.aplanmis.data.exchange.service.aplanmis.ProjPurchaseService;
import com.augurit.aplanmis.data.exchange.service.spgl.SpglZjfwsxblxxbService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author ylf_i
 * @date 2019/12/17
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
@Slf4j
@Service
@Transactional
public class ProjPurchaseServiceImpl implements ProjPurchaseService {

    @Autowired
    private ProjPurchaseMapper projPurchaseMapper;

    @Override
    public PageInfo<SpglZjfwsxblxxb> listSpglZjfwsxblxxb(SpglZjfwsxblxxb spglZjfwsxblxxb, Page page) {
        PageHelper.startPage(page);
        List<SpglZjfwsxblxxb> list = projPurchaseMapper.listSpglZjfwsxblxxb(spglZjfwsxblxxb);
        return new PageInfo<>(list);
    }

    @Override
    public SpglZjfwsxblxxb getSpglZjfwsxblxxbById(String id) {
        return projPurchaseMapper.getSpglZjfwsxblxxbById(id);
    }

    @Override
    public List<SpglZjfwsxblxxb> listSpglZjfwsxblxxb(SpglZjfwsxblxxb spglZjfwsxblxxb) {
        List<SpglZjfwsxblxxb> list = projPurchaseMapper.listSpglZjfwsxblxxb(spglZjfwsxblxxb);
        return list;
    }

    @Override
    public PageInfo<SpglZjfwsxblxxb> listByTimeRange(Date startTime, Date endTime, Page page) {
        PageHelper.startPage(page);
        List<SpglZjfwsxblxxb> list = projPurchaseMapper.listSpglZjfwsxblxxbByTimeRange(startTime, endTime);
        return new PageInfo<>(list);
    }
}