package com.augurit.aplanmis.data.exchange.mapper.spgl;

import com.augurit.aplanmis.data.exchange.domain.spgl.SpglXmydhxjzxxb;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author qhg
 * @date 2019/11/05
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
@Mapper
@Repository
public interface SpglXmydhxjzxxbMapper {

    void insertSpglXmydhxjzxxb(SpglXmydhxjzxxb spglXmydhxjzxxb);

    void updateSpglXmydhxjzxxb(SpglXmydhxjzxxb spglXmydhxjzxxb);

    void deleteSpglXmydhxjzxxb(@Param("id") String id);

    List<SpglXmydhxjzxxb> listSpglXmydhxjzxxb(SpglXmydhxjzxxb spglXmydhxjzxxb);

    SpglXmydhxjzxxb getSpglXmydhxjzxxbById(@Param("id") String id);

    List<SpglXmydhxjzxxb> getSpglXmydhxjzxxbByXzqhdmAndGcdM(@Param("xzqhdm") String xzqhdm, @Param("gcdm") String gcdm);

    void batchInsertSpglXmydhxjzxxb(@Param("list") List<SpglXmydhxjzxxb> list);
}
