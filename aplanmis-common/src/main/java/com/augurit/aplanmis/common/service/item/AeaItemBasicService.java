package com.augurit.aplanmis.common.service.item;


import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.vo.PreItemCheckResultVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface AeaItemBasicService {

    /**
     * 1.根据阶段ID查询事项定义列表
     *
     * @param stageId      必须参数  阶段ID
     * @param isOptionItem 可选参数 是否可选事项 0表示并联审批事项 1表示并行推进事项 2前置检查事项
     * @param projInfoId   可选参数  项目ID 用于判断改事项是否已办
     * @param rootOrgId   必选参数  顶级机构ID 多租户
     * @return
     * @throws Exception
     */
    List<AeaItemBasic> getAeaItemBasicListByStageId(String stageId, String isOptionItem, String projInfoId, String rootOrgId) throws Exception;

    /**
     * 2.根据阶段ID和情形ID查询事项定义列表
     *
     * @param stageId      必须参数  阶段ID
     * @param stateId      非必须参数  情形ID  为null时，查询的是所有情形下的事项
     * @param isOptionItem 可选参数 是否可选事项 0表示并联审批事项 1表示并行推进事项 2前置检查事项
     * @param rootOrgId   必选参数  顶级机构ID 多租户
     * @return
     * @throws Exception
     */
    List<AeaItemBasic> getAeaItemBasicListByStageIdAndStateId(String stageId, String stateId, String isOptionItem,String rootOrgId) throws Exception;

    /**
     * 3.根据事项VerId查询事项信息
     *
     * @param itemVerId 必须参数  事项版本ID
     * @return
     * @throws Exception
     */
    AeaItemBasic getAeaItemBasicByItemVerId(String itemVerId) throws Exception;

    /**
     * 5.根据部门ID查询事项定义列表
     *
     * @param orgId 非必须 部门ID 为空时，查询所有事项
     * @return
     * @throws Exception
     */
    List<AeaItemBasic> getAeaItemBasicListByOrgId(String orgId) throws Exception;

    /**
     * 6.新增/修改事项定义
     *
     * @param aeaItemBasic 事项实体
     *                     根据ID是否有值判断是新增还是修改
     * @throws Exception
     */
    public void saveAeaItemBasic(AeaItemBasic aeaItemBasic) throws Exception;

    /**
     * 7.根据ID删除事项定义
     *
     * @param id
     * @throws Exception
     */
    public void deleteAeaItemBasic(String id) throws Exception;

    /**
     * 8.查询事项定义列表
     *
     * @param aeaItemBasic
     * @return
     * @throws Exception
     */
    public List<AeaItemBasic> listAeaItemBasic(AeaItemBasic aeaItemBasic) throws Exception;

    /**
     * 9.根据ID查询事项定义
     *
     * @param id
     * @return
     * @throws Exception
     */
    public AeaItemBasic getAeaItemBasicById(String id) throws Exception;

    /**
     * 10.分页查询事项定义列表
     *
     * @param aeaItemBasic
     * @return
     * @throws Exception
     */
    PageInfo<AeaItemBasic> listAeaItemBasic(AeaItemBasic aeaItemBasic, int pageNum, int pageSize,String rootOrgId) throws Exception;

    /**
     * 11.根据事项过滤组织机构列表
     */
    public List<OpuOmOrg> listOpuOmOrgByAeaItemBasic(String rootOrgId) throws Exception;

    /**
     * 12.分页根据orgid查询事项定义列表
     *
     * @param orgId
     * @param pageNum
     * @param pageSize
     * @return
     * @throws Exception
     */
    PageInfo<AeaItemBasic> getAeaItemBasicListByOrgId(String orgId, int pageNum, int pageSize) throws Exception;


    /**
     * 12.分页根据orgid查询事项定义列表
     *
     * @param orgId
     * @param isCatalog
     * @param pageNum
     * @param pageSize
     * @return
     * @throws Exception
     */
    PageInfo<AeaItemBasic> getAeaItemBasicListByOrgId(String orgId,String isCatalog, int pageNum, int pageSize) throws Exception;

    /**
     * xiaohutu----2019-07-19新增
     * 根据情形ID插叙绑定的事项列表
     *
     * @param stateId 情形ID
     * @return
     */
    public List<AeaItemBasic> listAeaParStateItemByStateId(String stateId) throws Exception;

    /**
     * 根据标准事项ID和区划ID查询实施事项列表
     * @param itemId 事项ID
     * @param regionalism 区划ID
     * @return
     */
    public List<AeaItemBasic> getSssxByItemIdAndRegionalism(String itemId, String regionalism, String[] projectAddressArr, String rootOrgId);

    /**
     * 将事项列表中的标准事项转化成实施事项的信息
     * @param itemList
     * @param regionalism 区划ID
     */
    void conversionBasicItemToSssx(List<AeaItemBasic> itemList, String regionalism, String projectAddress, String rootOrgId);

    /**
     * 根据实施事项查找标准事项
     *
     * @param itemId
     * @return
     */
    AeaItemBasic getCatalogItemByCarryOutItemId(String itemId,String rootOrgId);

    List<OpuOmOrg> listOpuOmOrgByAeaItemBasic1(String s) throws Exception;

    /**
     * 单项申报前置事项检查
     *
     * @param itemVerId  实施事项版本id
     * @param projInfoId 项目id
     * @return 检查结果
     */
    PreItemCheckResultVo checkPreItemsPassed(String itemVerId, String projInfoId);

    /**
     * 检查前置事项是否已办(已办或不存在前置事项返回空数组，存在返回未办的前置事项)
     *
     * @param itemVerId  实施事项版本id
     * @param projInfoId 项目id
     * @return
     */
    List<AeaItemBasic> frontItemIsDone(String itemVerId, String projInfoId);

    /**
     * 根据部门ID查询标准或实施事项定义列表
     *
     * @param orgId 非必须 部门ID 为空时，查询所有事项
     * @return
     * @throws Exception
     */
    List<AeaItemBasic> getAeaItemBasicListByOrgId(String orgId,String isCatalog) throws Exception;
}
