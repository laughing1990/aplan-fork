package com.augurit.aplanmis.data.exchange.service.aplanmis;

import com.augurit.aplanmis.data.exchange.domain.spgl.SpglXmdwxxb;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author yinlf
 * @date 2019//09/02
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
public interface ApplyProjUnitService extends BaseUploadServer<SpglXmdwxxb> {

    PageInfo<SpglXmdwxxb> listSpglXmdwxxb(SpglXmdwxxb spglXmdwxxb, Page page);

    SpglXmdwxxb getSpglXmdwxxbById(String id);

    List<SpglXmdwxxb> listSpglXmdwxxb(SpglXmdwxxb spglXmdwxxb);
}