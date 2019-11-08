package com.augurit.aplanmis.data.exchange.service.spgl;

import com.augurit.aplanmis.data.exchange.domain.spgl.SpglXmqtfjxxb;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author yinlf
 * @date 2019//09/02
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
public interface SpglXmqtfjxxbService extends BaseSpglServer<SpglXmqtfjxxb> {

    void updateSpglXmqtfjxxb(SpglXmqtfjxxb spglXmqtfjxxb);

    void deleteSpglXmqtfjxxbById(String id);

    PageInfo<SpglXmqtfjxxb> listSpglXmqtfjxxb(SpglXmqtfjxxb spglXmqtfjxxb, Page page);

    SpglXmqtfjxxb getSpglXmqtfjxxbById(String id);

    List<SpglXmqtfjxxb> listSpglXmqtfjxxb(SpglXmqtfjxxb spglXmqtfjxxb);
}