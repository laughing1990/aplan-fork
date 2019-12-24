package com.augurit.aplanmis.data.exchange.service.spgl;

import com.augurit.aplanmis.data.exchange.domain.spgl.SpglZjfwsxblxxb;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author ylf_i
 * @date 2019/12/17
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
public interface SpglZjfwsxblxxbService extends BaseSpglServer<SpglZjfwsxblxxb> {
    void saveSpglZjfwsxblxxb(SpglZjfwsxblxxb spglZjfwsxblxxb);

    void updateSpglZjfwsxblxxb(SpglZjfwsxblxxb spglZjfwsxblxxb);

    void deleteSpglZjfwsxblxxbById(String id);

    PageInfo<SpglZjfwsxblxxb> listSpglZjfwsxblxxb(SpglZjfwsxblxxb spglZjfwsxblxxb, Page page);

    SpglZjfwsxblxxb getSpglZjfwsxblxxbById(String id);

    List<SpglZjfwsxblxxb> listSpglZjfwsxblxxb(SpglZjfwsxblxxb spglZjfwsxblxxb);
}