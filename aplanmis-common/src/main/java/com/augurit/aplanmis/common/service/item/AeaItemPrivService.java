package com.augurit.aplanmis.common.service.item;

import com.augurit.aplanmis.common.domain.AeaItemPriv;

import java.util.List;

/**
 * @author yinlf
 * @Date 2019/7/25
 */
public interface AeaItemPrivService {

    /**
     * 新增事项承办
     *
     * @param aeaItemPriv
     */
    void insertAeaItemPriv(AeaItemPriv aeaItemPriv);

    /**
     * 设置事项在某区域内承办组织
     *
     * @param itemVerId   事项版本ID
     * @param regionId    行政区划ID
     * @param orgId       组织ID
     * @param allowManual 是否允许人工选择下级承办组织，0表示禁止，1表示允许
     * @param elExpr      启用EL表达式时填写EL表达式，不启用时填NULL
     */
    void insertAeaItemPriv(String itemVerId, String regionId, String orgId, String allowManual, String elExpr);

    /**
     * 删除事项承办
     *
     * @param regionId 行政区划ID
     * @param orgId 组织ID
     * @param itemVerId 事项版本ID
     */
    void deleteAeaItemPriv(String regionId, String orgId, String itemVerId);

    /**
     * 获取当前用户事项的承办情况
     *
     * @param itemVerId 事项版本ID
     * @return 承办情况
     */
    List<AeaItemPriv> findCurrentUserItemPriv(String... itemVerId);

    /**
     * 某个区域事项的承办情况
     *
     * @param regionId  行政区划ID
     * @param itemVerId 事项版本ID
     * @return
     */
    List<AeaItemPriv> findItemPrivByRegionIdAndItemVerId(String regionId, String... itemVerId);
}
