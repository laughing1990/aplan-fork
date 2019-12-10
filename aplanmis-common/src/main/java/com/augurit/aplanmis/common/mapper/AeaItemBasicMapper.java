package com.augurit.aplanmis.common.mapper;

import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaItemService;
import com.augurit.aplanmis.common.domain.AgentItemDetail;
import com.augurit.aplanmis.common.dto.AnnounceDataDto;
import com.augurit.aplanmis.common.vo.AeaItemServiceVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * -Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaItemBasicMapper {

    void insertAeaItemBasic(AeaItemBasic aeaItemBasic);

    void updateAeaItemBasic(AeaItemBasic aeaItemBasic);

    void deleteAeaItemBasic(@Param("id") String id) ;

    List<AeaItemBasic> listAeaItemBasic(AeaItemBasic aeaItemBasic) ;

    List<AeaItemBasic> listAeaItemBasicOfMain(AeaItemBasic aeaItemBasic) ;

    AeaItemBasic getAeaItemBasicById(@Param("id") String id) ;

    AeaItemBasic getItemBasicNoRelOtherByItemVerId(@Param("itemVerId") String itemVerId,
                                                   @Param("rootOrgId") String rootOrgId);

    List<AeaItemBasic> getAeaItemBasicListByStageId(@Param("stageId") String stageId,
                                                    @Param("isOptionItem") String isOptionItem,
                                                    @Param("rootOrgId") String rootOrgId);

    AeaItemBasic getAeaItemBasicByItemVerId(@Param("itemVerId") String itemVerId, @Param("rootOrgId") String rootOrgId);

    /**
     * 供后台使用 == 请勿修改
     *
     * @param itemVerId
     * @param rootOrgId
     * @return
     */
    AeaItemBasic getAeaItemBasicByItemVerId2(@Param("itemVerId") String itemVerId, @Param("rootOrgId") String rootOrgId);

    /**
     * 未加限制
     *
     * @param itemVerId
     * @param rootOrgId
     * @return
     */
    AeaItemBasic getOneByItemVerId(@Param("itemVerId") String itemVerId,
                                   @Param("rootOrgId") String rootOrgId);

    List<String> getItemVerIdListByProjInfoId(String projInfoId);

    List<AeaItemBasic> getAeaItemBasicListByStageIdAndStateId(@Param("stageId") String stageId,
                                                              @Param("stateId") String stateId,
                                                              @Param("isOptionItem") String isOptionItem, @Param("rootOrgId") String rootOrgId);

    List<AeaItemBasic> listAeaItemBasicListByStageIds(@Param("stageIds") List<String> stageIds, @Param("rootOrgId") String rootOrgId);

    List<AeaItemBasic> listOptionAeaItemBasicListByStageIds(@Param("stageIds") List<String> stageIds, @Param("isOptionItem") String isOptionItem, @Param("rootOrgId") String rootOrgId);

    List<AeaItemBasic> getAeaItemBasicListByItemVerIds(@Param("itemVerIds") List<String> itemVerIds);

    List<AeaItemBasic> getAeaItemBasicListByOrgId(@Param("orgId") String orgId);

    List<AeaItemBasic> getAeaItemBasicListByOrgIdAndIsCatalog(@Param("orgId") String orgId,@Param("isCatalog")String isCatalog);


    List<AnnounceDataDto> searchAnnounceDataList(@Param("keyword") String keyword, @Param("rootOrgId") String rootOrgId);

    /**
     * @param stateId
     * @return
     */
    List<AeaItemBasic> listAeaParStateItemByStateId(@Param("stateId") String stateId);

    /**
     * 批量查询
     *
     * @param itemVerIds
     * @return
     */
    List<AeaItemBasic> listAeaItemBasicByItemVerIds(@Param("itemVerIds") String[] itemVerIds);

    BscDicCodeItem getItemByTypeCodeAndItemCode(@Param("typeCode") String typeCode, @Param("itemCode") String itemCode);

    /**
     * 获取最新试运行或者发布版本事项
     *
     * @param aeaItemBasic
     * @return
     */
    List<AeaItemBasic> listLatestOkAeaItemBasic(AeaItemBasic aeaItemBasic);

    List<AeaItemBasic> listLatestAeaItemBasic(AeaItemBasic aeaItemBasic);

    /**
     * 通过事项id获取事项基本信息集合
     *
     * @param itemIds
     * @return
     */
    List<AeaItemBasic> listAeaItemBasicByItemIds(@Param("itemIds") String[] itemIds,
                                                 @Param("rootOrgId") String rootOrgId);

    List<AeaItemBasic> listAeaItemBasicWithOrg(AeaItemBasic aeaItemBasic);

    Long getItemAcceptModeByParentId(@Param("parentItemId") String parentItemId);

    List<AeaItemBasic> listAeaItemBasicByOrgIds(@Param("itemBasic") AeaItemBasic aeaItemBasic,
                                                @Param("orgIds") List<String> orgIds);

    /**
     * 查询部门事项以及标准事项
     *
     * @param aeaItemBasic
     * @param orgIds
     * @return
     */
    List<AeaItemBasic> listExtItemByOrgIdsRelStandItem(@Param("itemBasic") AeaItemBasic aeaItemBasic,
                                                       @Param("orgIds") List<String> orgIds);

    AeaItemBasic getAeaItemBasicWithOrgById(@Param("itemBasicId") String itemBasicId);

    List<AeaItemBasic> listAeaItemBasicByThemeVerId(@Param("themeVerId") String themeVerId);

    List<AeaItemBasic> checkUniqueItemCode(@Param("itemCode") String itemCode);

    AeaItemBasic getAeaItemBasicByItemCode(@Param("itemCode") String itemCode);

    List<AeaItemBasic> listAllChildItems(@Param("itemId") String itemId);

    /**
     * 查询中介服务列表
     */
    List<AeaItemService> listAgentItemService(AeaItemService aeaItemService);

    /**
     * 查询中介机构服务事项对应的部门
     */
    List<AeaItemService> listServiceItemOrg();

    /**
     * 查询中介服务事项详情
     */
    AgentItemDetail getAgentItemBasicById(@Param("itemBasicId") String itemBasicId);

    /**
     * 查询单个中介事项对应的行政事项列表
     */
    List<AeaItemBasic> listItemBasicByAgentItemId(@Param("agentItemId") String agentItemId);

    List<AeaItemService> listAgentItemServiceByUnitInfoId(AeaItemService aeaItemService);

    List<AeaItemServiceVo> listItemServiceVo(@Param("keyword") String keyword, @Param("itemVerId") String itemVerId);

    List<AeaItemBasic> listAeaItemBasicAll(@Param("serviceTypeId") String serviceTypeId);

    List<AeaItemBasic> listService(@Param("serviceType") String serviceType);

    List<AeaItemBasic> listItemName(@Param("itemName") String itemName);

    List<AeaItemBasic> listItemByParState(@Param("parStateId") String parStateId);

    List<AeaItemBasic> listAeaItemBasicByStageId(@Param("itemBasic") AeaItemBasic aeaItemBasic, @Param("stageId") String stageId);

    List<AeaItemBasic> listCarryOutAeaItemBasicByParentItemIds(@Param("parentItemIds") List<String> parentItemIds, @Param("rootOrgId") String rootOrgId);

    /**
     * 查询项目并联阶段下核心事项已申报事项列表
     *
     * @param stageId
     * @param projInfoId
     * @param rootOrgId
     * @return
     */
    List<String> getStageItemVerIdListByStageIdAndProjInfoId(@Param("stageId") String stageId, @Param("projInfoId") String projInfoId, @Param("rootOrgId") String rootOrgId);

    List<OpuOmOrg> listOpuOmOrgByAllPropJoinItem(@Param("opuOmOrg") OpuOmOrg opuOmOrg, @Param("rootOrgId") String rootOrgId);

    AeaItemBasic getLatestParentAeaItemBasicByChildItemId(@Param("itemId") String itemId, @Param("rootOrgId") String rootOrgId);

    void syncItemRegion(AeaItemBasic basic);

    List<AeaItemBasic> checkUniqueItemCategoryMark(@Param("itemId") String itemId,
                                                   @Param("isCatalog") String isCatalog,
                                                   @Param("itemCategoryMark") String itemCategoryMark,
                                                   @Param("rootOrgId") String rootOrgId);

    /**
     * 查询所属顶级组织的事项审批部门信息
     *
     * @param rootOrgId
     * @return
     */
    List<AeaItemBasic> listOrgInfoByRootOrgId(@Param("rootOrgId") String rootOrgId);

    /**
     * 根据组织ID查询该组织下发布和试运行的所有实施事项
     *
     * @param orgId
     * @return
     */
    List<AeaItemBasic> listAeaItemBasicByOrgId(@Param("orgId") String orgId);

    /**
     * 根据机构获取最新版本事项
     *
     * @param orgId
     * @param rootOrgId
     * @return
     */
    List<AeaItemBasic> listLatestVerAeaItemBasicByOrgId(@Param("orgId") String orgId, @Param("rootOrgId") String rootOrgId);

    /**
     * 获取自己和子孙下级
     *
     * @param rootOrgId
     * @param isCatalog
     * @param itemIds
     * @return
     */
    List<AeaItemBasic> listSefAndChildLatestOkAeaItemBasic(@Param("rootOrgId") String rootOrgId,
                                                           @Param("isCatalog") String isCatalog,
                                                           @Param("itemIds") List<String> itemIds);

    /**
     * 获取自己和子孙下级
     *
     * @param rootOrgId
     * @param isCatalog
     * @param itemIds
     * @return
     */
    List<String> listSefAndChildLatestOkItemVerIds(@Param("rootOrgId") String rootOrgId,
                                                   @Param("isCatalog") String isCatalog,
                                                   @Param("itemIds") List<String> itemIds);

    /**
     * 通过事项版本集合和顶级部门获取事项数据
     *
     * @param rootOrgId
     * @param itemVerIds
     * @return
     */
    List<AeaItemBasic> listAeaItemBasicByItemVerIdsAndOrgId(@Param("rootOrgId") String rootOrgId,
                                                            @Param("itemVerIds") List<String> itemVerIds);

    List<AeaItemBasic> listAeaItemBasicByIteminstId(String iteminstId);

    List<AeaItemBasic> getAeaItemBasicsListByOutputMatId(@Param("matId") String matId, @Param("rootOrgId") String rootOrgId);

    /**
     * 根据中介事项获取关联的行政事项列表
     *
     * @param itemId
     * @param rootOrgId
     * @return
     */
    List<AeaItemBasic> getAgentParentItem(@Param("itemId") String itemId, @Param("rootOrgId") String rootOrgId);

    List<AeaItemBasic> listUnSelectedParFrontItemBasicByStageId(@Param("stageId") String stageId, @Param("frontItemId") String frontItemId, @Param("rootOrgId") String rootOrgId);

    List<AeaItemBasic> listUnSelectedItemFrontItemBasicByItemVerId(@Param("itemVerId") String itemVerId, @Param("frontItemId") String frontItemId, @Param("rootOrgId") String rootOrgId);

    List<AeaItemBasic> getCompletedItemBasicByProjInfoId(@Param("projInfoId") String projInfoId, @Param("rootOrgId") String rootOrgId);

    /**
     * 查询中介服务事项数量
     *
     * @param startDate
     * @return
     */
    int listAgentItemServiceNum(@Param("startDate") String startDate);

    /**
     * 采购页获取中介事项列表
     *
     * @param keyword
     * @param itemVerId
     * @return
     */
    List<AeaItemServiceVo> listAgentItemServiceVo(@Param("keyword") String keyword, @Param("itemVerId") String itemVerId);
}
