package com.augurit.aplanmis.data.exchange.service.spgl;

import com.augurit.aplanmis.data.exchange.domain.spgl.SpglDfxmsplcxxb;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author yinlf
 * @date 2019//09/02
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
public interface SpglDfxmsplcxxbService extends BaseSpglServer<SpglDfxmsplcxxb> {

    boolean findActiveSpglDfxmsplcxxbByUnique(String splcbm,Double splcbbh);

    void updateSpglDfxmsplcxxb(SpglDfxmsplcxxb spglDfxmsplcxxb);

    void deleteSpglDfxmsplcxxbById(String id);

    PageInfo<SpglDfxmsplcxxb> listSpglDfxmsplcxxb(SpglDfxmsplcxxb spglDfxmsplcxxb, Page page);

    SpglDfxmsplcxxb getSpglDfxmsplcxxbById(String id);

    List<SpglDfxmsplcxxb> listSpglDfxmsplcxxb(SpglDfxmsplcxxb spglDfxmsplcxxb);

}