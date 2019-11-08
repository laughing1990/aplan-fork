package com.augurit.aplanmis.common.service.guide;

import com.augurit.aplanmis.common.domain.AeaItemServiceWindowRel;

import java.util.List;

public interface AeaItemServiceWindowRelService {

    /**
     *  根据阶段ID查询并联的窗口办理列表
     * @param stageId 必须参数 阶段ID
     * @return
     * @throws Exception
     */
    public List<AeaItemServiceWindowRel> listAeaItemServiceWindowRel(String stageId,String rootOrgId) throws Exception;

    /**
     * 根据事项版本ID查询单项的窗口办理列表
     * @param itemVerId 必须参数 事项版本ID
     * @return
     * @throws Exception
     */
    public List<AeaItemServiceWindowRel> listAeaItemServiceWindowRelByItemVerId(String itemVerId,String rootOrgId) throws Exception;


}
