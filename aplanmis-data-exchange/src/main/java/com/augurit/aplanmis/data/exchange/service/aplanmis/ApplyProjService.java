package com.augurit.aplanmis.data.exchange.service.aplanmis;

import com.augurit.aplanmis.data.exchange.domain.spgl.SpglXmjbxxb;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author yinlf
 * @date 2019//09/02
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
public interface ApplyProjService extends BaseUploadServer<SpglXmjbxxb> {

    PageInfo<SpglXmjbxxb> listSpglXmjbxxb(SpglXmjbxxb spglXmjbxxb, Page page);

    SpglXmjbxxb getSpglXmjbxxbById(String id);

    List<SpglXmjbxxb> listSpglXmjbxxb(SpglXmjbxxb spglXmjbxxb);
}