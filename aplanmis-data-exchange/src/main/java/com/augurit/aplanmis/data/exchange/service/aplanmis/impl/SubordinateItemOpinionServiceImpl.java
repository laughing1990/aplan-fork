package com.augurit.aplanmis.data.exchange.service.aplanmis.impl;

import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.data.exchange.domain.spgl.SpglXmspsxblxxxxb;
import com.augurit.aplanmis.data.exchange.mapper.aplanmis.ItemOpinionMapper;
import com.augurit.aplanmis.data.exchange.service.aplanmis.SubordinateItemOpinionService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class SubordinateItemOpinionServiceImpl implements SubordinateItemOpinionService {

    @Autowired
    private ItemOpinionMapper itemOpinionMapper;

    @Override
    public PageInfo<SpglXmspsxblxxxxb> listByTimeRange(Date startTime, Date endTime, Page page) {
        PageHelper.startPage(page);
        List<SpglXmspsxblxxxxb> list = itemOpinionMapper.listSpglXmspfssxblxxxxbByTimeRange(startTime, endTime);
        return new PageInfo<>(list);
    }

}