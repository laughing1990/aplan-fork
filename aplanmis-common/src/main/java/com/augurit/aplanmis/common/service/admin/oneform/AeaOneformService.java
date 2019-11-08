package com.augurit.aplanmis.common.service.admin.oneform;

import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.aplanmis.common.domain.AeaOneform;
import com.github.pagehelper.Page;

import java.util.List;

public interface AeaOneformService {

    /**
     * 保存
     * @param aeaOneform
     */
    void saveAeaOneform(AeaOneform aeaOneform);

    /**
     * 更新
     * @param aeaOneform
     */
    void updateAeaOneform(AeaOneform aeaOneform);

    /**
     * 删除
     * @param id
     */
    void deleteAeaOneformById(String id);

    /**
     * 批量删除
     *
     * @param ids
     */
    void batchDelAeaOneformByIds(String[] ids);

    /**
     * 分页获取
     *
     * @param AeaOneform
     * @param page
     * @return
     */
    EasyuiPageInfo<AeaOneform> getAeaOneformList(AeaOneform aeaOneform, Page page);

    /**
     * 通过id获取
     *
     * @param id
     * @return
     */
    AeaOneform getAeaOneformById(String id);

    /**
     * 获取最大排序号
     *
     * @param rootOrgId
     * @return
     */
    Long getMaxSortNo(String rootOrgId);

    List<AeaOneform> findAllStageOneFormsByStageId(String stageId, String rootOrgId);

    void enOrDisableIsActive(String id);
}
