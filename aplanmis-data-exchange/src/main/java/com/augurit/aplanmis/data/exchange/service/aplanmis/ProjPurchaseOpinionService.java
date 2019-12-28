package com.augurit.aplanmis.data.exchange.service.aplanmis;

import com.augurit.aplanmis.data.exchange.domain.spgl.SpglZjfwsxblgcxxb;
import com.augurit.aplanmis.data.exchange.service.spgl.BaseSpglServer;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author ylf_i
 * @date 2019/12/17
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
public interface ProjPurchaseOpinionService extends BaseUploadServer<SpglZjfwsxblgcxxb> {

    SpglZjfwsxblgcxxb getSpglZjfwsxblgcxxbById(String id);

    List<SpglZjfwsxblgcxxb> listSpglZjfwsxblgcxxb(SpglZjfwsxblgcxxb spglZjfwsxblgcxxb);

    PageInfo<SpglZjfwsxblgcxxb> listSpglZjfwsxblgcxxb(SpglZjfwsxblgcxxb spglZjfwsxblgcxxb, Page page);
}