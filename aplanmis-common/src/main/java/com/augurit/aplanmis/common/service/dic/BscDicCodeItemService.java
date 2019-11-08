package com.augurit.aplanmis.common.service.dic;

import com.augurit.agcloud.bsc.domain.BscDicCodeItem;

import java.util.List;

public interface BscDicCodeItemService {
    /**
     *  获取数据字典的值
     * @param typeCode  必须参数
     * @param itemCode  必须参数
     * @param orgId      非必须参数
     * @return
     * @throws Exception
     */
    BscDicCodeItem getItemByTypeCodeAndItemCodeAndOrgId(String typeCode, String itemCode, String orgId) throws Exception;

    /**
     * 获取数据字典列表
     * @param typeCode 必须参数
     * @param orgId 非必须参数
     * @return
     * @throws Exception
     */
    List<BscDicCodeItem> getActiveItemsByTypeCode(String typeCode, String orgId) throws Exception;


    /**
     * 获取所有数据字典列表
     * @param orgId 非必须参数
     * @return
     * @throws Exception
     */
    List<BscDicCodeItem> getAllActiveItemsByTypeCode( String orgId) throws Exception;
}
