package com.augurit.aplanmis.data.exchange.service.aplanmis;

import com.augurit.aplanmis.data.exchange.domain.spgl.SpglDfxmsplcjdxxb;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.Date;
import java.util.List;

/**
 * @author yinlf
 * @date 2019//09/02
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
public interface StageService extends BaseUploadServer<SpglDfxmsplcjdxxb> {

    PageInfo<SpglDfxmsplcjdxxb> listSpglDfxmsplcjdxxb(SpglDfxmsplcjdxxb spglDfxmsplcjdxxb, Page page);

    SpglDfxmsplcjdxxb getSpglDfxmsplcjdxxbById(String id);

    List<SpglDfxmsplcjdxxb> listSpglDfxmsplcjdxxb(SpglDfxmsplcjdxxb spglDfxmsplcjdxxb);

    PageInfo<SpglDfxmsplcjdxxb> listSpglDfxmsplcjdxxbByTimeRange(Date startTime, Date endTime, Page page);
}