package com.augurit.aplanmis.data.exchange.service.spgl;

import com.augurit.aplanmis.data.exchange.domain.spgl.SpglXmspsxblxxxxb;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author yinlf
 * @date 2019//09/02
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
public interface SpglXmspsxblxxxxbService extends BaseSpglServer<SpglXmspsxblxxxxb> {

    void updateSpglXmspsxblxxxxb(SpglXmspsxblxxxxb spglXmspsxblxxxxb);

    void deleteSpglXmspsxblxxxxbById(String id);

    PageInfo<SpglXmspsxblxxxxb> listSpglXmspsxblxxxxb(SpglXmspsxblxxxxb spglXmspsxblxxxxb, Page page);

    SpglXmspsxblxxxxb getSpglXmspsxblxxxxbById(String id);

    List<SpglXmspsxblxxxxb> listSpglXmspsxblxxxxb(SpglXmspsxblxxxxb spglXmspsxblxxxxb);
}