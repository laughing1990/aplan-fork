package com.augurit.aplanmis.data.exchange.mapper.spgl;

import com.augurit.aplanmis.data.exchange.domain.spgl.SpglZjfwsxblxxb;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ylf_i
 * @date 2019/12/17
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
@Mapper
@Repository
public interface SpglZjfwsxblxxbMapper {

    void insertSpglZjfwsxblxxb(SpglZjfwsxblxxb spglZjfwsxblxxb);

    void updateSpglZjfwsxblxxb(SpglZjfwsxblxxb spglZjfwsxblxxb);

    void deleteSpglZjfwsxblxxb(@Param("id") String id);

    List<SpglZjfwsxblxxb> listSpglZjfwsxblxxb(SpglZjfwsxblxxb spglZjfwsxblxxb);

    SpglZjfwsxblxxb getSpglZjfwsxblxxbById(@Param("id") String id);

    void batchInsertSpglZjfwsxblxxb(@Param("list") List<SpglZjfwsxblxxb> list);
}