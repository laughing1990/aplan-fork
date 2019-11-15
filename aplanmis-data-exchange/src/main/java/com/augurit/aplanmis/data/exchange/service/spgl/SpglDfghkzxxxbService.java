package com.augurit.aplanmis.data.exchange.service.spgl;

import com.augurit.aplanmis.data.exchange.domain.spgl.SpglDfghkzxxxb;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author qhg
 * @date 2019//11/06
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
public interface SpglDfghkzxxxbService extends BaseSpglServer<SpglDfghkzxxxb> {

    void updateSpglDfghkzxxxb(SpglDfghkzxxxb spglDfghkzxxxb);

    void deleteSpglDfghkzxxxbById(String id);

    PageInfo<SpglDfghkzxxxb> listSpglDfghkzxxxb(SpglDfghkzxxxb spglDfghkzxxxb, Page page);

    SpglDfghkzxxxb getSpglDfghkzxxxbById(String id);

    List<SpglDfghkzxxxb> listSpglDfghkzxxxb(SpglDfghkzxxxb spglDfghkzxxxb);
}