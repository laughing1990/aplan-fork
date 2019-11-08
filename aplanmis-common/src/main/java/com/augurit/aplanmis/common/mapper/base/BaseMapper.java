package com.augurit.aplanmis.common.mapper.base;

import org.apache.ibatis.annotations.Param;

public interface BaseMapper<D> {

    void insertOne(D domain);

    void deleteById(@Param("id") String id);

    void updateOne(D domain);

    D selectOneById(@Param("id") String id);
}
