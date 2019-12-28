package com.augurit.aplanmis.data.exchange.service.spgl;

import com.augurit.aplanmis.data.exchange.domain.spgl.SpglXmspsxbltbcxxxb;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author ylf_i
 * @date 2019/12/17
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
public interface SpglXmspsxbltbcxxxbService extends BaseSpglServer<SpglXmspsxbltbcxxxb> {
    void saveSpglXmspsxbltbcxxxb(SpglXmspsxbltbcxxxb spglXmspsxbltbcxxxb);

    void updateSpglXmspsxbltbcxxxb(SpglXmspsxbltbcxxxb spglXmspsxbltbcxxxb);

    void deleteSpglXmspsxbltbcxxxbById(String id);

    PageInfo<SpglXmspsxbltbcxxxb> listSpglXmspsxbltbcxxxb(SpglXmspsxbltbcxxxb spglXmspsxbltbcxxxb, Page page);

    SpglXmspsxbltbcxxxb getSpglXmspsxbltbcxxxbById(String id);

    List<SpglXmspsxbltbcxxxb> listSpglXmspsxbltbcxxxb(SpglXmspsxbltbcxxxb spglXmspsxbltbcxxxb);
}