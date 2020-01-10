package com.augurit.aplanmis.common.service.certificate.impl;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.aplanmis.common.mapper.AeaHiItemCorrectMapper;
import com.augurit.aplanmis.common.mapper.AeaHiSmsSendItemMapper;
import com.augurit.aplanmis.common.service.certificate.PickUpService;
import com.augurit.aplanmis.common.vo.conditional.TaskInfo;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Transactional
public class PickUpServiceImpl implements PickUpService {

    @Autowired
    private AeaHiItemCorrectMapper aeaHiItemCorrectMapper;
    @Autowired
    private AeaHiSmsSendItemMapper aeaHiSmsSendItemMapper;

    @Override
    public void complementStatus(Map<String, TaskInfo> taskInfos) {
        Assert.notNull(taskInfos, "取件列表为空");
        List<String> correctingApplyinstIds = aeaHiItemCorrectMapper.listCorrectingApplyinstId(taskInfos.keySet());
        taskInfos.forEach((applyinstId, info) -> {
            if (correctingApplyinstIds.contains(applyinstId)) {
                info.setComplementionStatus(Status.OFF);
            } else {
                info.setComplementionStatus(Status.ON);
            }
        });
    }

    @Override
    public void chargeStatus(Map<String, TaskInfo> taskInfos) {
        Assert.notNull(taskInfos, "取件列表为空");
        // todo  待对接， 先默认 已缴清
        taskInfos.forEach((applyinstId, info) -> info.setChargeStatus(Status.ON));
    }

    @Override
    public void pickUpStatus(Map<String, TaskInfo> taskInfos) {
        Assert.notNull(taskInfos, "取件列表为空");
        taskInfos.forEach((applyinstId, info) -> {
            int hasSend = aeaHiSmsSendItemMapper.countSendItemByApplyinstId(applyinstId);
            int needSendCount = aeaHiSmsSendItemMapper.getNeedSendCount(applyinstId);
            if (hasSend == needSendCount) {
                info.setPickUpStatus("1");
            } else if (hasSend != 0 && hasSend < needSendCount) {
                info.setPickUpStatus("2");
            } else {
                info.setPickUpStatus("0");
            }
        });

    }
}
