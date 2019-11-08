package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaParStageItemIn;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 阶段事项与输入关联定义表-Mapper数据与持久化接口类
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:chenzh</li>
 * <li>创建时间：2019-07-04 16:18:35</li>
 * </ul>
 */
@Mapper
@Repository
public interface AeaParStageItemInMapper {

    public void insertAeaParStageItemIn(AeaParStageItemIn aeaParStageItemIn);

    public void updateAeaParStageItemIn(AeaParStageItemIn aeaParStageItemIn);

    public void deleteAeaParStageItemIn(@Param("id") String id);

    public List<AeaParStageItemIn> listAeaParStageItemIn(AeaParStageItemIn aeaParStageItemIn);

    public AeaParStageItemIn getAeaParStageItemInById(@Param("id") String id);

    void deleteAeaParStageItemInByStageItemId(@Param("stageItemId") String stageItemId);

    void deleteAeaParStageItemInByInId(@Param("inId") String inId);

    Long getMaxSortNo();

    /**
     * 通过事项获取材料事项
     *
     * @param stageId
     * @param isOptionItem
     * @param itemId
     * @param itemVerId
     * @return
     */
    List<AeaParStageItemIn> listStageItemInByItemAndOption(@Param("stageId") String stageId,
                                                           @Param("isOptionItem") String isOptionItem,
                                                           @Param("itemId") String itemId,
                                                           @Param("itemVerId") String itemVerId);

    List<AeaParStageItemIn> listStageItemInByStageIdAndOption(@Param("stageId") String stageId,
                                                              @Param("isOptionItem") String isOptionItem);

    List<AeaParStageItemIn> listAeaParStageItemInByInId(@Param("inId") String inId);
}
