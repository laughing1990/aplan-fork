package com.augurit.aplanmis.common.service.mat;

import com.augurit.aplanmis.common.domain.AeaItemMat;

import java.util.List;

/**
 * 事项材料定义表-Service服务调用接口类
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:chenzh</li>
 * <li>创建时间：2019-07-08 16:31:14</li>
 * </ul>
 */
public interface AeaItemMatService {

    /**
     * 根据单项事项ID获取（输入或输出）材料列表
     *
     * @param itemVerId
     * @param isInput        （当 isInput=1时，表示输入材料，isInput=0时，表示输出材料）
     * @param isNeedStateMat (在事项分情形下，isNeedStateMat =1时，会返回事项下的父情形材料，缺省为Null)
     * @return
     * @throws Exception
     */
    List<AeaItemMat> getMatListByItemVerIds(String[] itemVerId, String isInput, String isNeedStateMat) throws Exception;

    /**
     * 获取阶段下某个事项所关联的材料列表
     *
     * @param itemVerId
     * @param stageId
     * @param isNeedStateMat (在事项分情形下，isNeedStateMat =1时，会返回事项下的情形材料，缺省为Null)
     * @return
     * @throws Exception
     */
    List<AeaItemMat> getMatListByItemVerIdAndStageId(String itemVerId, String stageId, String isNeedStateMat, String rootOrgId) throws Exception;

    /**
     * 获取阶段下某几个事项所关联的材料列表
     *
     * @param itemVerIds
     * @param stageId
     * @param isNeedStateMat (在事项分情形下，isNeedStateMat =1时，会返回事项下的情形材料，缺省为Null)
     * @return
     * @throws Exception
     */
    List<AeaItemMat> getMatListByItemVerIdsAndStageId(String[] itemVerIds, String stageId, String isNeedStateMat) throws Exception;

    /**
     * 根据事项实例ID获取（输入或输出）材料列表
     *
     * @param iteminstId
     * @param isInput        （当 isInput=1时，表示输入材料，isInput=0时，表示输出材料）
     * @param isNeedStateMat (在事项分情形下，isNeedStateMat =1时，会返回事项下的父情形材料，缺省为Null)
     * @return
     * @throws Exception
     */
    List<AeaItemMat> getMatListByIteminstId(String iteminstId, String isInput, String isNeedStateMat) throws Exception;

    /**
     * 根据阶段ID获取材料列表
     *
     * @param stageId
     * @param isNeedStateMat (在阶段分情形下，isNeedStateMat =1时，会返回阶段下的父情形材料，缺省为Null)
     * @return
     * @throws Exception
     */
    List<AeaItemMat> getMatListByStageId(String stageId, String isNeedStateMat) throws Exception;

    // 获取直接挂在阶段下的材料
    List<AeaItemMat> listMatListByStageId(String stageId, List<String> itemVerIds) throws Exception;

    /**
     * 根据并联情形ID获取（输入或输出）材料列表
     *
     * @param StageStateId
     * @return
     * @throws Exception
     */
    List<AeaItemMat> getMatListByStageStateIds(String[] StageStateId) throws Exception;

    /**
     * 根据并联情形ID和事项版本ID获取（输入或输出）材料列表
     *
     * @param StageStateId
     * @param itemVerId
     * @return
     * @throws Exception
     */
    List<AeaItemMat> getMatListByStageStateIdsAndItemVerId(String[] StageStateId, String itemVerId) throws Exception;


    /**
     * 根据并联情形ID和材料实例ID获取（输入或输出）材料列表
     *
     * @param StageStateId
     * @param matinstId
     * @return
     * @throws Exception
     */
    List<AeaItemMat> getMatListByStageStateIdsAndMatinstId(String[] StageStateId, String matinstId) throws Exception;

    /**
     * 根据串联情形ID获取（输入或输出）材料列表
     *
     * @param ItemStateId
     * @return
     * @throws Exception
     */
    List<AeaItemMat> getMatListByItemStateIds(String[] ItemStateId) throws Exception;

    /**
     * 根据申请实例ID获取材料列表(不包含材料实例)
     *
     * @param applyinstId 必须参数 申请实例ID
     * @param iteminstId  可选字段 事项实例ID  可根据该字段筛选数据
     * @return
     * @throws Exception
     */
    List<AeaItemMat> getMatListByApplyinstId(String applyinstId, String iteminstId) throws Exception;

    /**
     * 根据申请实例ID获取材料列表(含材料实例)
     *
     * @param applyinstId 必须参数 申请实例ID
     * @param iteminstId  可选字段 事项实例ID  可根据该字段筛选数据
     * @return
     * @throws Exception
     */
    List<AeaItemMat> getMatListByApplyinstIdContainsMatinst(String applyinstId, String iteminstId) throws Exception;

    /**
     * 根据情形ID列表，事项ID列表，阶段ID获取材料列表(含材料实例)
     *
     * @param itemStateIds             事项情形ID列表
     * @param stageStateIds            阶段情形ID列表
     * @param coreItemVerIds           单项/并行事项(实施事项)版本ID列表
     * @param parallelItemVerIds       并联事项(实施标准事项)版本列表
     * @param coreParentItemVerIds     单项/并行事项(标准事项)版本ID列表
     * @param parallelParentItemVerIds 并联事项(标准事项)版本ID列表
     * @param stageId                  阶段ID
     * @param rootOrgId
     * @return
     * @throws Exception
     */
    List<AeaItemMat> getMatListByStateListAndItemListAndStageId(String[] itemStateIds, String[] stageStateIds, String[] coreItemVerIds, String[] parallelItemVerIds, String[] coreParentItemVerIds, String[] parallelParentItemVerIds, String stageId,String rootOrgId) throws Exception;

    /**
     * 获取阶段下事项的共享材料
     *
     * @param stageId
     * @param itemVerIds
     * @return
     * @throws Exception
     */
    List<AeaItemMat> getOfficeMatsByStageItemVerIds(String stageId, String[] itemVerIds) throws Exception;

    /**
     * 查询单项不分情形下材料定义列表-中介超市用
     *
     * @param itemVerId 事项版本ID
     * @return List<AeaItemMat>
     */
    List<AeaItemMat> getSeriesNoStateMatList(String itemVerId) throws Exception;
}
