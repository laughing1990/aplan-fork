package com.augurit.aplanmis.data.exchange.service.aplanmis;

import com.augurit.aplanmis.data.exchange.domain.spgl.SpglDfxmsplcjdsxxxb;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author yinlf
 * @Date 2019/9/21
 */
public interface StageItemService extends BaseUploadServer<SpglDfxmsplcjdsxxxb> {

    /**
     * 查询项目基本信息
     *
     * @param spglDfxmsplcjdsxxxb
     * @return
     */
    List<SpglDfxmsplcjdsxxxb> findSpglDfxmsplcjdsxxxb(SpglDfxmsplcjdsxxxb spglDfxmsplcjdsxxxb);

    PageInfo<SpglDfxmsplcjdsxxxb> listSpglDfxmsplcjdsxxxb(SpglDfxmsplcjdsxxxb spglDfxmsplcjdsxxxb, Page page);
}
