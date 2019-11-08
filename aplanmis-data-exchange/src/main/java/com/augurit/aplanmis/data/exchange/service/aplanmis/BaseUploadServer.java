package com.augurit.aplanmis.data.exchange.service.aplanmis;

import com.augurit.aplanmis.data.exchange.domain.base.SpglEntity;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.Date;

/**
 * @author yinlf
 * @Date 2019/9/29
 */
public interface BaseUploadServer<T extends SpglEntity> {
    PageInfo<T> listByTimeRange(Date startTime, Date endTime, Page page);
}
