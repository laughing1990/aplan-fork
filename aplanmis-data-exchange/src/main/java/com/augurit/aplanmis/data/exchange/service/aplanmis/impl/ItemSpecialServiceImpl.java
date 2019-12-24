package com.augurit.aplanmis.data.exchange.service.aplanmis.impl;

import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.data.exchange.domain.spgl.SpglXmspsxbltbcxxxb;
import com.augurit.aplanmis.data.exchange.mapper.aplanmis.ItemSpecialMapper;
import com.augurit.aplanmis.data.exchange.service.aplanmis.ItemSpecialService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@Transactional
public class ItemSpecialServiceImpl implements ItemSpecialService {

    @Autowired
    private ItemSpecialMapper itemSpecialMapper;

    @Override
    public SpglXmspsxbltbcxxxb getSpglXmspsxbltbcxxxbById(String id) {
        return itemSpecialMapper.getSpglXmspsxbltbcxxxbById(id);
    }

    @Override
    public List<SpglXmspsxbltbcxxxb> listSpglXmspsxbltbcxxxb(SpglXmspsxbltbcxxxb spglXmspsxbltbcxxxb) {
        List<SpglXmspsxbltbcxxxb> list = itemSpecialMapper.listSpglXmspsxbltbcxxxb(spglXmspsxbltbcxxxb);
        return list;
    }

    @Override
    public PageInfo<SpglXmspsxbltbcxxxb> listSpglXmspsxbltbcxxxb(SpglXmspsxbltbcxxxb spglXmspsxbltbcxxxb, Page page) {
        PageHelper.startPage(page);
        List<SpglXmspsxbltbcxxxb> list = itemSpecialMapper.listSpglXmspsxbltbcxxxb(spglXmspsxbltbcxxxb);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<SpglXmspsxbltbcxxxb> listByTimeRange(Date startTime, Date endTime, Page page) {
        PageHelper.startPage(page);
        List<SpglXmspsxbltbcxxxb> list = itemSpecialMapper.listSpglXmspsxbltbcxxxbByTimeRange(startTime, endTime);
        return new PageInfo<>(list);
    }
}
