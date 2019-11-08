package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaParStageItem;
import com.augurit.aplanmis.common.mybatisBean.RappidStageItemBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 阶段与事项关联定义表-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaParStageItemMapper {

    void insertAeaParStageItem(AeaParStageItem aeaParStageItem);

    void updateAeaParStageItem(AeaParStageItem aeaParStageItem);

    void deleteAeaParStageItem(@Param("id") String id);

    List<AeaParStageItem> listAeaParStageItem(AeaParStageItem aeaParStageItem);

    AeaParStageItem getAeaParStageItemById(@Param("id") String id);

    /**
     * 根据ids批量查询阶段下事项列表
     *
     * @param parInIds 输入ids
     * @return List<AeaParStageItem>
     */
    List<AeaParStageItem> listAeaStageItemByParInIds(@Param("parInIds") String[] parInIds);

    /**
     * 根据ids批量查询阶段下事项列表
     *
     * @param parInId 输入ids
     * @return List<AeaParStageItem>
     */
    List<AeaParStageItem> listAeaStageItemByParInId(@Param("parInId") String parInId);

    List<AeaParStageItem> listAeaStageItemByStageId(@Param("stageId") String stageId,
                                                    @Param("isOptionItem") String isOptionItem,
                                                    @Param("rootOrgId") String rootOrgId);

    List<AeaParStageItem> listAeaStageItemByStageIdGroupByed(@Param("stageId") String stageId,
                                                             @Param("isOptionItem") String isOptionItem,
                                                             @Param("rootOrgId") String rootOrgId);

    List<AeaParStageItem> listAeaStageItemCondition(AeaParStageItem aeaParStageItem);

    List<AeaParStageItem> listStageAllItem(AeaParStageItem aeaParStageItem);

    List<AeaParStageItem> listAeaStageMatItemCondition(AeaParStageItem aeaParStageItem);

    void batchDeleteStageItemByStageId(@Param("stageId") String stageId);

    List<AeaParStageItem> listAeaParStageItemWhichBindState(AeaParStageItem aeaParStageItem);

    List<AeaParStageItem> listAeaStageItemByParIn(@Param("inId") String inId,
                                                  @Param("rootOrgId") String rootOrgId);

    List<AeaParStageItem> listAeaStageItemByParIns(@Param("inIds") List<String> inIds,
                                                   @Param("rootOrgId") String rootOrgId);

    void batchUpdateStageItemByItemVerChange(@Param("themeVerId") String themeVerId,
                                             @Param("itemId") String itemId,
                                             @Param("itemVerId") String itemVerId,
                                             @Param("newItemVerId") String newItemVerId,
                                             @Param("rootOrgId") String rootOrgId);

    List<AeaParStageItem> listRepeatStageItem(@Param("stageId") String stageId,
                                              @Param("isOptionItem") String isOptionItem);

    Long getMaxSortNoByStageId(String stageId);

    int countStageItemByisOptionItem(@Param("stageId") String stageId,
                                     @Param("isOptionItem") String isOptionItem,
                                     @Param("rootOrgId") String rootOrgId);

    List<RappidStageItemBean> rappidFindStageAllBasicItemGroupByisOptionItem(@Param("stageId") String stageId,
                                                                             @Param("rootOrgId") String rootOrgId);

    List<String> findParentItemByItemds(@Param("parentItemIds") String[] parentItemIds);

    List<AeaParStageItem> listStageItemByThemeVerId(@Param("themeVerId") String themeVerId,
                                                    @Param("isOptionItem") String isOptionItem,
                                                    @Param("rootOrgId") String rootOrgId);


    /**
     * 获取主题版本下的标准/实施事项（不去重）
     *
     * @param themeVerId
     * @param isOptionItem
     * @param isCatalog
     * @param rootOrgId
     * @return
     */
    List<AeaParStageItem> listStageItemRelRepeatByThemeVerId(@Param("themeVerId") String themeVerId,
                                                             @Param("isOptionItem") String isOptionItem,
                                                             @Param("isCatalog") String isCatalog,
                                                             @Param("rootOrgId") String rootOrgId);

    /**
     * 获取主题版本下的标准/实施事项（去重）
     *
     * @param themeVerId
     * @param isOptionItem
     * @param isCatalog
     * @param rootOrgId
     * @return
     */
    List<AeaParStageItem> listStageItemNoRepeatByThemeVerId(@Param("themeVerId") String themeVerId,
                                                            @Param("isOptionItem") String isOptionItem,
                                                            @Param("isCatalog") String isCatalog,
                                                            @Param("rootOrgId") String rootOrgId);

    /**
     * 获取主题版本下的标准/实施事项（去重）,仅携带事项基本信息
     *
     * @param themeVerId
     * @param isOptionItem
     * @param isCatalog
     * @param rootOrgId
     * @return
     */
    List<AeaParStageItem> listStageItemRelInfoNoRepeatByThemeVerId(@Param("themeVerId") String themeVerId,
                                                                   @Param("isOptionItem") String isOptionItem,
                                                                   @Param("isCatalog") String isCatalog,
                                                                   @Param("rootOrgId") String rootOrgId);

    /**
     * 取主题版本下的标准/实施事项itemId（去重）
     *
     * @param themeVerId
     * @param isOptionItem
     * @param isCatalog
     * @param rootOrgId
     * @return
     */
    List<String> listItemIdsNoRepeatByThemeVerId(@Param("themeVerId") String themeVerId,
                                                 @Param("isOptionItem") String isOptionItem,
                                                 @Param("isCatalog") String isCatalog,
                                                 @Param("rootOrgId") String rootOrgId);

    /**
     * 取主题版本下的标准/实施事项itemVerId（去重）
     *
     * @param themeVerId
     * @param isOptionItem
     * @param isCatalog
     * @param rootOrgId
     * @return
     */
    List<String> listItemVerIdsNoRepeatByThemeVerId(@Param("themeVerId") String themeVerId,
                                                    @Param("isOptionItem") String isOptionItem,
                                                    @Param("isCatalog") String isCatalog,
                                                    @Param("rootOrgId") String rootOrgId);

    List<AeaParStageItem> checkBeforeSeriesFlow(@Param("itemVerIds") List<String> itemVerIds, @Param("themeId") String themeId, @Param("rootOrgId") String rootOrgId);
}
