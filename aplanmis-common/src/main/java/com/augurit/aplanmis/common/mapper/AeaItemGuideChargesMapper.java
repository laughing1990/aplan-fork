package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaItemGuideCharges;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AeaItemGuideChargesMapper {

    /**
     * 插入
     *
     * @param aeaItemGuideCharges
     */
    void insertAeaItemGuideCharges(AeaItemGuideCharges aeaItemGuideCharges);

    /**
     * 更新
     *
     * @param aeaItemGuideCharges
     */
    void updateAeaItemGuideCharges(AeaItemGuideCharges aeaItemGuideCharges);

    /**
     * 获取
     *
     * @param id
     * @return
     */
    AeaItemGuideCharges getAeaItemGuideChargesById(@Param("id") String id);

    /**
     * 获取列表
     *
     * @param aeaItemGuideCharges
     * @return
     */
    List<AeaItemGuideCharges> listAeaItemGuideCharges(AeaItemGuideCharges aeaItemGuideCharges);

    /**
     * 删除
     *
     * @param id
     */
    void deleteAeaItemGuideChargesById(@Param("id") String id);

    /**
     * 批量删除
     *
     * @param ids
     */
    void batchDelAeaItemGuideChargesByIds(@Param("ids") String[] ids);

    /**
     * 通过事项版本ID删除
     *
     * @param itemVerId
     */
    void batchDelGuideChargesByItemVerId(@Param("itemVerId") String itemVerId,
                                         @Param("rootOrgId") String rootOrgId);

    /**
     * 获取某版本数据下最大的排序
     *
     * @param itemVerId
     * @return
     */
    Long getMaxSortNoByItemVerId(@Param("itemVerId") String itemVerId,
                                 @Param("rootOrgId") String rootOrgId);
}
