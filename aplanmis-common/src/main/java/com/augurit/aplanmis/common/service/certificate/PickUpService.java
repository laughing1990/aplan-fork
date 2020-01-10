package com.augurit.aplanmis.common.service.certificate;

import com.augurit.aplanmis.common.vo.conditional.TaskInfo;

import java.util.Map;

/*
取件服务
 */
public interface PickUpService {

    /**
     * 申报的补齐状态
     *
     * @param taskInfos 取件数据
     */
    void complementStatus(Map<String, TaskInfo> taskInfos);

    /**
     * 是否缴费
     *
     * @param taskInfos 取件数据
     */
    void chargeStatus(Map<String, TaskInfo> taskInfos);

    /**
     * 领取状态
     *
     * @param taskInfos 取件数据
     */
    void pickUpStatus(Map<String, TaskInfo> taskInfos);
}
