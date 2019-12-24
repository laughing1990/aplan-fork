package com.augurit.aplanmis.data.exchange.service.aplanmis.impl;

import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.data.exchange.domain.spgl.SpglZjfwsxblgcxxb;
import com.augurit.aplanmis.data.exchange.mapper.aplanmis.ProjPurchaseOpinionMapper;
import com.augurit.aplanmis.data.exchange.service.aplanmis.ProjPurchaseOpinionService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
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
public class ProjPurchaseOponionServiceImpl implements ProjPurchaseOpinionService {

    @Autowired
    private ProjPurchaseOpinionMapper projPurchaseOpinionMapper;

    @Override
    public PageInfo<SpglZjfwsxblgcxxb> listSpglZjfwsxblgcxxb(SpglZjfwsxblgcxxb spglZjfwsxblgcxxb, Page page) {
        PageHelper.startPage(page);
        List<SpglZjfwsxblgcxxb> list = projPurchaseOpinionMapper.listSpglZjfwsxblgcxxb(spglZjfwsxblgcxxb);
        return new PageInfo<>(list);
    }

    @Override
    public SpglZjfwsxblgcxxb getSpglZjfwsxblgcxxbById(String id) {
        return projPurchaseOpinionMapper.getSpglZjfwsxblgcxxbById(id);
    }

    @Override
    public List<SpglZjfwsxblgcxxb> listSpglZjfwsxblgcxxb(SpglZjfwsxblgcxxb spglZjfwsxblgcxxb) {
        List<SpglZjfwsxblgcxxb> list = projPurchaseOpinionMapper.listSpglZjfwsxblgcxxb(spglZjfwsxblgcxxb);
        return list;
    }

    @Override
    public PageInfo<SpglZjfwsxblgcxxb> listByTimeRange(Date startTime, Date endTime, Page page) {
        PageHelper.startPage(page);
        List<SpglZjfwsxblgcxxb> list = projPurchaseOpinionMapper.listSpglZjfwsxblgcxxbByTimeRange(startTime, endTime);
        return new PageInfo<>(list);
    }
}