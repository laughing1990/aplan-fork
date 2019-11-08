package com.augurit.aplanmis.data.exchange.service.aplanmis.impl;

import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.data.exchange.domain.spgl.SpglDfxmsplcjdsxxxb;
import com.augurit.aplanmis.data.exchange.mapper.aplanmis.StageItemMapper;
import com.augurit.aplanmis.data.exchange.service.aplanmis.StageItemService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author yinlf
 * @Date 2019/9/21
 */
@Service
public class StageItemServiceImpl implements StageItemService {

    private static Logger logger = LoggerFactory.getLogger(StageItemServiceImpl.class);

    @Autowired
    private StageItemMapper stageItemMapper;

    @Override
    public PageInfo<SpglDfxmsplcjdsxxxb> listByTimeRange(Date startTime, Date endTime, Page page) {
        PageHelper.startPage(page);
        List<SpglDfxmsplcjdsxxxb> list = stageItemMapper.listSpglDfxmsplcjdsxxxbByTimeRange(startTime, endTime);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public List<SpglDfxmsplcjdsxxxb> findSpglDfxmsplcjdsxxxb(SpglDfxmsplcjdsxxxb spglDfxmsplcjdsxxxb) {
        return stageItemMapper.listSpglDfxmsplcjdsxxxb(spglDfxmsplcjdsxxxb);
    }

    @Override
    public PageInfo<SpglDfxmsplcjdsxxxb> listSpglDfxmsplcjdsxxxb(SpglDfxmsplcjdsxxxb spglDfxmsplcjdsxxxb, Page page) {
        PageHelper.startPage(page);
        List<SpglDfxmsplcjdsxxxb> spglDfxmsplcjdsxxxbs = stageItemMapper.listSpglDfxmsplcjdsxxxb(spglDfxmsplcjdsxxxb);
        return new PageInfo(spglDfxmsplcjdsxxxbs);
    }


}
