package com.augurit.aplanmis.data.exchange.service.spgl;

import com.augurit.aplanmis.data.exchange.domain.spgl.SpglXmspsxpfwjxxb;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author yinlf
 * @date 2019//09/02
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
public interface SpglXmspsxpfwjxxbService extends BaseSpglServer<SpglXmspsxpfwjxxb> {

    void updateSpglXmspsxpfwjxxb(SpglXmspsxpfwjxxb spglXmspsxpfwjxxb);

    void deleteSpglXmspsxpfwjxxbById(String id);

    PageInfo<SpglXmspsxpfwjxxb> listSpglXmspsxpfwjxxb(SpglXmspsxpfwjxxb spglXmspsxpfwjxxb, Page page);

    SpglXmspsxpfwjxxb getSpglXmspsxpfwjxxbById(String id);

    List<SpglXmspsxpfwjxxb> listSpglXmspsxpfwjxxb(SpglXmspsxpfwjxxb spglXmspsxpfwjxxb);
}