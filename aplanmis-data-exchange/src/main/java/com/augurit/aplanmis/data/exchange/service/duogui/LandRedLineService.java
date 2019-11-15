package com.augurit.aplanmis.data.exchange.service.duogui;

import com.augurit.aplanmis.data.exchange.domain.spgl.SpglXmydhxjzxxb;
import com.augurit.aplanmis.data.exchange.service.aplanmis.BaseUploadServer;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author qhg
 * @date 2019/11/05
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
public interface LandRedLineService extends BaseUploadServer<SpglXmydhxjzxxb> {

    PageInfo<SpglXmydhxjzxxb> listSpglXmydhxjzxxb(SpglXmydhxjzxxb spglXmydhxjzxxb, Page page);

    SpglXmydhxjzxxb getSpglXmydhxjzxxbById(String id);

    List<SpglXmydhxjzxxb> listSpglXmydhxjzxxb(SpglXmydhxjzxxb spglXmydhxjzxxb);
}