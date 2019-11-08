package com.augurit.aplanmis.data.exchange.service.aplanmis.impl;

import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.data.exchange.domain.spgl.SpglDfxmsplcjdsxxxb;
import com.augurit.aplanmis.data.exchange.mapper.aplanmis.StageItemMapper;
import com.augurit.aplanmis.data.exchange.service.aplanmis.StageSubordinateItemService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class StageSubordinateItemServiceImpl implements StageSubordinateItemService {

    private static Logger logger = LoggerFactory.getLogger(StageSubordinateItemServiceImpl.class);

    @Autowired
    private StageItemMapper stageItemMapper;

    @Override
    public PageInfo<SpglDfxmsplcjdsxxxb> listByTimeRange(Date startTime, Date endTime, Page page) {
        PageHelper.startPage(page);
        List<SpglDfxmsplcjdsxxxb> list = stageItemMapper.listSpglDfxmsplcjdfxsxxxbByTimeRange(startTime, endTime);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

}
