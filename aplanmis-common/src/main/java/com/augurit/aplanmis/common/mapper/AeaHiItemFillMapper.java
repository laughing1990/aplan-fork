package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaHiItemFill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 事项容缺补齐实例表-Mapper数据与持久化接口类
 */
@Mapper
public interface AeaHiItemFillMapper {

    public void insertAeaHiItemFill(AeaHiItemFill aeaHiItemFill) throws Exception;

    public void updateAeaHiItemFill(AeaHiItemFill aeaHiItemFill) throws Exception;

    public void deleteAeaHiItemFill(@Param("id") String id) throws Exception;

    public List<AeaHiItemFill> listAeaHiItemFill(AeaHiItemFill aeaHiItemFill) throws Exception;

    public AeaHiItemFill getAeaHiItemFillById(@Param("id") String id) throws Exception;

    public List<AeaHiItemFill> listAeaHiItemFillsByCondition(AeaHiItemFill aeaHiItemFill);

    public AeaHiItemFill getAeaHiItemFillDetail(@Param("fillId") String fillId);
}
