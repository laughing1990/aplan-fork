package com.augurit.aplanmis.mall.userCenter.service;

import com.augurit.aplanmis.common.domain.AeaItemCond;
import com.augurit.aplanmis.common.domain.AeaItemState;
import com.augurit.aplanmis.mall.main.vo.BaseInfoVo;

import java.util.List;

public interface RestSeriesApplyService {

    /**
     *  根据事项版本ID、项目ID查询基础信息
     * @param itemVerId
     * @param projInfoId
     * @return
     * @throws Exception
     */
    BaseInfoVo getBaseInfoByItemVerIdAndProjInfoId(String itemVerId, String projInfoId)throws Exception;

    /**
     * 查询单事项下前置条件列表，查询前判断是否有前置条件
     * @param itemVerId
     * @return
     * @throws Exception
     */
    List<AeaItemCond> getAeaItemCondTopListByItemVerId(String itemVerId) throws Exception;

    /**
     * 根据事项版本ID查询根情形列表
     * @param itemVerId
     * @return
     * @throws Exception
     */
    List<AeaItemState> listRootAeaItemStateByItemVerId(String itemVerId) throws  Exception;

    List<AeaItemCond> getTreeCondByItemVerId(String itemVerId) throws Exception;
}
