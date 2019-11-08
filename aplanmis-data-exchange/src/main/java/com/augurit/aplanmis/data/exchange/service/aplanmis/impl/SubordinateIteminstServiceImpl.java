package com.augurit.aplanmis.data.exchange.service.aplanmis.impl;

import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.data.exchange.domain.spgl.SpglXmspsxblxxb;
import com.augurit.aplanmis.data.exchange.mapper.aplanmis.IteminstMapper;
import com.augurit.aplanmis.data.exchange.service.aplanmis.SubordinateIteminstService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class SubordinateIteminstServiceImpl implements SubordinateIteminstService {

    @Autowired
    private IteminstMapper iteminstMapper;

    @Override
    public PageInfo<SpglXmspsxblxxb> listByTimeRange(Date startTime, Date endTime, Page page) {
        PageHelper.startPage(page);
        List<SpglXmspsxblxxb> list = iteminstMapper.listSpglXmspfxsxblxxbByTimeRange(startTime, endTime);
        return new PageInfo<>(list);
    }

}