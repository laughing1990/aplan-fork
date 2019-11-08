package com.augurit.aplanmis.common.service.state;

import com.augurit.aplanmis.common.domain.AeaItemState;

import java.util.List;

/**
 * @author xiaohutu
 */
public interface AeaItemStateService {
    /**
     * 根据事项版本ID获取情形列表
     *
     * @param itemVerId  事项版本
     * @param stateVerId 情形版本（非必输，null==默认查询试运行或已发布的最大版本）
     * @return List<AeaItemState>
     * @throws Exception E
     */
    List<AeaItemState> listAeaItemStateByItemVerId(String itemVerId, String stateVerId) throws Exception;

    /**
     * 根据事项版本ID获取情形列表树
     *
     * @param itemVerId  事项版本
     * @param stateVerId 情形版本==null 默认查询已发布最大版本情形
     * @return List<AeaItemState>
     * @throws Exception E
     */
    List<AeaItemState> listTreeAeaItemStateByItemVerId(String itemVerId, String stateVerId) throws Exception;

    /**
     * 根据情形版本ID获取情形列表
     *
     * @param stateVerId 情形版本ID
     * @return List<AeaItemState>
     * @throws Exception E
     */
    List<AeaItemState> listAeaItemStateByStateVerId(String stateVerId) throws Exception;

    /**
     * 根据事项版本ID获取指定情形版本root情形列表
     * if(itemVerId==null && stateVerId==null) parentId!=null&& !=ROOT && !=root
     * if(parentId!=null&& !=ROOT && !=root) itemVerId!=null
     *
     * @param itemVerId  事项版本
     * @param parentId   父ID  ==ROOT||null||root 查询root情形
     * @param rootOrgId  顶级机构ID
     * @param stateVerId 情形版本---null==查询最新已发布或试运行
     * @return List<AeaItemState>
     * @throws Exception E
     */
    List<AeaItemState> listAeaItemStateByParentId(String itemVerId, String stateVerId, String parentId, String rootOrgId) throws Exception;

    /**
     * 根据情形ID获取所有的情形列表
     *
     * @param itemStateId 情形ID
     * @return List<AeaItemState>
     * @throws Exception E
     */
    List<AeaItemState> listAeaItemStateById(String itemStateId) throws Exception;

    /**
     * 根据事项实例ID获取所有的情形列表
     *
     * @param iteminstId 事项实例ID
     * @return List<AeaItemState>
     * @throws Exception E
     */
    List<AeaItemState> listAeaItemStateByIteminstId(String iteminstId) throws Exception;

    int deleteAeaItemState(String id) throws Exception;
}
