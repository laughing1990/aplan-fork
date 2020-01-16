package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaHiItemFillRealIninst;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 事项容缺实际补齐材料实例表-Mapper数据与持久化接口类
 */
@Mapper
public interface AeaHiItemFillRealIninstMapper {

    public void insertAeaHiItemFillRealIninst(AeaHiItemFillRealIninst aeaHiItemFillRealIninst) throws Exception;

    public void updateAeaHiItemFillRealIninst(AeaHiItemFillRealIninst aeaHiItemFillRealIninst) throws Exception;

    public void deleteAeaHiItemFillRealIninst(@Param("id") String id) throws Exception;

    public List<AeaHiItemFillRealIninst> listAeaHiItemFillRealIninst(AeaHiItemFillRealIninst aeaHiItemFillRealIninst) throws Exception;

    public AeaHiItemFillRealIninst getAeaHiItemFillRealIninstById(@Param("id") String id) throws Exception;

    public void batchInsertAeaHiItemFillRealIninst(List<AeaHiItemFillRealIninst> list) throws Exception;

}
