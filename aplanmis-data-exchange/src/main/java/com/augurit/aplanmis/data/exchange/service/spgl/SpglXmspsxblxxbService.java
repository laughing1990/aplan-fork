package com.augurit.aplanmis.data.exchange.service.spgl;

import com.augurit.aplanmis.data.exchange.domain.spgl.SpglXmspsxblxxb;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author yinlf
 * @date 2019//09/02
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
public interface SpglXmspsxblxxbService extends BaseSpglServer<SpglXmspsxblxxb> {

    void updateSpglXmspsxblxxb(SpglXmspsxblxxb spglXmspsxblxxb);

    void deleteSpglXmspsxblxxbById(String id);

    PageInfo<SpglXmspsxblxxb> listSpglXmspsxblxxb(SpglXmspsxblxxb spglXmspsxblxxb, Page page);

    SpglXmspsxblxxb getSpglXmspsxblxxbById(String id);

    List<SpglXmspsxblxxb> listSpglXmspsxblxxb(SpglXmspsxblxxb spglXmspsxblxxb);
}