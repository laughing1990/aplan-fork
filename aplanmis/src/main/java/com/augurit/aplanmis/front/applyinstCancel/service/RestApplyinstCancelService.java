package com.augurit.aplanmis.front.applyinstCancel.service;

import com.augurit.aplanmis.common.apply.ApplyinstCancelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RestApplyinstCancelService extends ApplyinstCancelService {

    //撤件受理时触发的外部事件
    @Override
    public void preCustomEvents(String applyinstId) throws Exception {

    }

    //撤件完成部门审批后触发的外部事件
    @Override
    public void postCustomEvents(String applyinstId) throws Exception {

    }

    //业主提交撤件时触发的外部事件
    @Override
    public void ApplySubmittedEvents(String applyinstId) throws Exception {

    }
}
