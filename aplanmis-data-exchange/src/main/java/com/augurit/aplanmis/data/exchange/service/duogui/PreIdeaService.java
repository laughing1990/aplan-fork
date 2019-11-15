package com.augurit.aplanmis.data.exchange.service.duogui;

import com.augurit.aplanmis.data.exchange.domain.spgl.SpglXmqqyjxxb;
import com.augurit.aplanmis.data.exchange.service.aplanmis.BaseUploadServer;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author qhg
 * @date 2019/11/05
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
public interface PreIdeaService extends BaseUploadServer<SpglXmqqyjxxb> {

    PageInfo<SpglXmqqyjxxb> listSpglXmqqyjxxb(SpglXmqqyjxxb spglXmqqyjxxb, Page page);

    SpglXmqqyjxxb getSpglXmqqyjxxbById(String id);

    List<SpglXmqqyjxxb> listSpglXmqqyjxxb(SpglXmqqyjxxb spglXmqqyjxxb);
}