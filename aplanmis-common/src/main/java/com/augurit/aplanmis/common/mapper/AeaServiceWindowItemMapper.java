package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaServiceWindowItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AeaServiceWindowItemMapper {

    void insertAeaServiceWindowItem(AeaServiceWindowItem aeaServiceWindowItem);

    void updateAeaServiceWindowItem(AeaServiceWindowItem aeaServiceWindowItem);

    void deleteAeaServiceWindowItemById(@Param("id") String id);

    /**
     * 批量删除
     *
     * @param ids
     */
    void batchDelAeaServiceWindowItemByIds(@Param("ids") String[] ids);

    List<AeaServiceWindowItem> listAeaServiceWindowItem(AeaServiceWindowItem aeaServiceWindowItem);

    AeaServiceWindowItem getAeaServiceWindowItemById(@Param("id") String id);

    void deleteAeaServiceWindowItemByWindowIdAndItemVerId(@Param("windowId") String windowId,
                                                          @Param("itemVerId") String itemVerId, @Param("rootOrgId") String rootOrgId);

    void enableWindowItem(@Param("windowId") String windowId, @Param("itemVerId") String itemVerId);

    void disableWindowItem(@Param("windowId") String windowId, @Param("itemVerId") String itemVerId);

    List<AeaItemBasic> findWindowItemByUserId(@Param("userId") String userId);

    List<AeaItemBasic> findWindowItemByWindowIdAndKeywordAndIsActive(@Param("windowId") String windowId, @Param("keyword") String keyword,@Param("isActive")String isActive, @Param("rootOrgId") String rootOrgId);

    void batchInsertAeaServiceWindowItem(@Param("aeaServiceWindowItemList") List<AeaServiceWindowItem> aeaServiceWindowItemList);

    void batchDeleteWindowItemByWindowIdAndItemVerId(@Param("windowId")String windowId,@Param("itemVerIds") String[] itemVerIds, @Param("rootOrgId") String rootOrgId);

    /**
     * 获取窗口关联事项
     *
     * @param windowId
     * @param keyword
     * @param rootOrgId
     * @return
     */
    List<AeaServiceWindowItem> listWindowItemByWindowId(@Param("windowId")String windowId, @Param("keyword")String keyword,@Param("rootOrgId") String rootOrgId);

    /**
     * 批量删除
     *
     * @param windowId
     * @param itemVerIds
     */
    void delAeaServiceWindowItemByWindowAndItemIds(@Param("windowId")String windowId, @Param("itemVerIds")String[] itemVerIds,@Param("rootOrgId")String rootOrgId);

    /**
     * 批量删除
     *
     * @param windowIds
     * @param rootOrgId
     */
    void delAeaServiceWindowItemByWindowIds(@Param("windowIds") String[] windowIds, @Param("rootOrgId") String rootOrgId);

}