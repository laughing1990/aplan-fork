package com.augurit.aplanmis.data.exchange.service.aplanmis;

import com.augurit.aplanmis.data.exchange.domain.spgl.SpglXmspsxbltbcxxxb;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.Date;
import java.util.List;

/**
 * @author ylf_i
 * @date 2019/12/17
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
public interface ItemSpecialService extends BaseUploadServer<SpglXmspsxbltbcxxxb> {

    SpglXmspsxbltbcxxxb getSpglXmspsxbltbcxxxbById(String id);

    List<SpglXmspsxbltbcxxxb> listSpglXmspsxbltbcxxxb(SpglXmspsxbltbcxxxb spglXmspsxbltbcxxxb);

    PageInfo<SpglXmspsxbltbcxxxb> listSpglXmspsxbltbcxxxb(SpglXmspsxbltbcxxxb spglXmspsxbltbcxxxb, Page page);
}
