package com.augurit.aplanmis.front.applyinstCancel.service;

import com.augurit.aplanmis.common.apply.ApplyinstCancelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RestApplyinstCancelService extends ApplyinstCancelService {


    @Override
    public void preCustomEvents(String applyinstId) throws Exception {

    }

    @Override
    public void postCustomEvents(String applyinstId) throws Exception {

    }
}
