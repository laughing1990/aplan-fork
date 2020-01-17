package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaHiItemFillDueIninst;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 事项容缺要求补齐材料实例表-Mapper数据与持久化接口类
 */
@Mapper
public interface AeaHiItemFillDueIninstMapper {

    public void insertAeaHiItemFillDueIninst(AeaHiItemFillDueIninst aeaHiItemFillDueIninst) throws Exception;

    public void batchInsertAeaHiItemFillDueIninst(List<AeaHiItemFillDueIninst> list) throws Exception;

    public void updateAeaHiItemFillDueIninst(AeaHiItemFillDueIninst aeaHiItemFillDueIninst) throws Exception;

    public void deleteAeaHiItemFillDueIninst(@Param("id") String id) throws Exception;

    public List<AeaHiItemFillDueIninst> listAeaHiItemFillDueIninst(AeaHiItemFillDueIninst aeaHiItemFillDueIninst) throws Exception;

    public AeaHiItemFillDueIninst getAeaHiItemFillDueIninstById(@Param("id") String id) throws Exception;

    public List<AeaHiItemFillDueIninst> listAeaHiItemFillDueIninstByFillId(@Param("fillId") String fillId);

    /**
     * 批量更新
     * @param list
     * @return
     */
    int batchUpdateAeaHiItemFillDueIninst(@Param("list") List<AeaHiItemFillDueIninst> list);
}
