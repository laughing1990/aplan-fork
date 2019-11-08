package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaServiceWindow;
import com.augurit.aplanmis.common.domain.AeaServiceWindowItem;
import com.augurit.aplanmis.common.domain.AeaServiceWindowStage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 服务窗口-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaServiceWindowMapper {

    void insertAeaServiceWindow(AeaServiceWindow aeaServiceWindow);

    void updateAeaServiceWindow(AeaServiceWindow aeaServiceWindow);

    void deleteAeaServiceWindow(@Param("id") String id);

    List<AeaServiceWindow> listAeaServiceWindow(AeaServiceWindow aeaServiceWindow);

    AeaServiceWindow getAeaServiceWindowById(@Param("windowId") String windowId);

    void batchDeleteAeaServiceWindow(@Param("windowIds") String[] windowIds, @Param("rootOrgId") String rootOrgId);

    List<AeaServiceWindow> findAllAeaServiceWindow();

    List<AeaServiceWindow> findAeaServiceWindowByRegionId(@Param("regionId") String regionId);

    List<AeaServiceWindow> findWindowByItemVerId(@Param("itemVerId") String itemVerId);

    List<AeaServiceWindow> findAeaServiceWindowByUserId(@Param("userId") String userId);

    Long getMaxSortNo(@Param("rootOrgId")String rootOrgId);

    List<AeaServiceWindow> listItemUnselectedWindow(AeaServiceWindowItem windowItem);

    List<AeaServiceWindow> listStageUnselectedWindow(AeaServiceWindowStage windowStage);
}
