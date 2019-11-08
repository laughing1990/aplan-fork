package com.augurit.aplanmis.data.exchange.service.spgl;

import com.augurit.aplanmis.data.exchange.domain.spgl.SpglDfxmsplcjdsxxxb;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author yinlf
 * @date 2019//09/02
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
public interface SpglDfxmsplcjdsxxxbService extends BaseSpglServer<SpglDfxmsplcjdsxxxb> {

    boolean findActiveSpglDfxmsplcjdsxxxbByUnique(String splcbm,Double splcbbh,String spsxbm,Double spsxbbh);

    void updateSpglDfxmsplcjdsxxxb(SpglDfxmsplcjdsxxxb spglDfxmsplcjdsxxxb);

    void deleteSpglDfxmsplcjdsxxxbById(String id);

    PageInfo<SpglDfxmsplcjdsxxxb> listSpglDfxmsplcjdsxxxb(SpglDfxmsplcjdsxxxb spglDfxmsplcjdsxxxb, Page page);

    SpglDfxmsplcjdsxxxb getSpglDfxmsplcjdsxxxbById(String id);

    List<SpglDfxmsplcjdsxxxb> listSpglDfxmsplcjdsxxxb(SpglDfxmsplcjdsxxxb spglDfxmsplcjdsxxxb);
}