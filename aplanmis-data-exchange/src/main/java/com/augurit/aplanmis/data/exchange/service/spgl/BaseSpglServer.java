package com.augurit.aplanmis.data.exchange.service.spgl;

import com.augurit.aplanmis.data.exchange.domain.base.SpglEntity;

import java.util.List;

/**
 * @author yinlf
 * @Date 2019/9/29
 */
public interface BaseSpglServer<T extends SpglEntity> {

    void insert(T t);

    void batchInsert(List<T> list);
}
