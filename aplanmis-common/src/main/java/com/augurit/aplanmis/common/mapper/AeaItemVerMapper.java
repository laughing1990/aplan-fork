package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaItemVer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 事项最大版本号表-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaItemVerMapper {

    void insertAeaItemVer(AeaItemVer aeaItemVer) ;

    void updateAeaItemVer(AeaItemVer aeaItemVer) ;

    AeaItemVer getAeaItemVerById(@Param("id") String id) ;

    AeaItemVer getAeaItemVerWithItemBasicById(@Param("id") String id);

    void deleteAeaItemVer(@Param("id") String id) ;

    void deleteAeaItemVerByItemId(@Param("itemId") String itemId, @Param("rootOrgId")String rootOrgId);

    List<AeaItemVer> listAeaItemVer(AeaItemVer aeaItemVer);

    List<AeaItemVer> listAeaItemVerWithItemBasic(AeaItemVer aeaItemVer);

    List<AeaItemVer> listTestRunOrPublishedItemVer(@Param("itemId") String itemId,
                                                   @Param("rootOrgId")String rootOrgId);

    void deprecateAllTestRunAndPublishedVersion(@Param("itemId") String itemId,
                                                @Param("itemVerId")String itemVerId,
                                                @Param("rootOrgId")String rootOrgId);

    //根据事项ID集合查询当前事项的所有实施事项的事项版本集合
    List<AeaItemVer> getAllShiShiItemVerIdsByParentItemIds(@Param("itemIds")List<String> itemIds);

    //根据事项id集合查询事项版本集合
    List<AeaItemVer> getAeaItemVersByItemIds(@Param("itemIds") List<String> itemIds);

    /**
     * 根据标准事项ID，查询所有的实施事项ID集合
     * @param itemId 标准事项ID
     * @return
     */
    List<String> getShiShiItemByBiaozhunItemId(@Param("itemId") String itemId);
}
