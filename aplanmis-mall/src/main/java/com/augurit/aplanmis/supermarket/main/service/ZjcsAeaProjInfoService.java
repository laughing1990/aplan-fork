package com.augurit.aplanmis.supermarket.main.service;

import com.augurit.aplanmis.common.domain.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface ZjcsAeaProjInfoService {
    AeaProjInfo getAeaProjInfoById(String id);

    List<AeaProjInfo> listAeaProjInfo(AeaProjInfo aeaProjInfo);


    void updateAeaProjInfo(AeaProjInfo aeaProjInfo);

    void insertAeaProjInfo(AeaProjInfo aeaProjInfo);

    //PageInfo<AeaProjInfo> myProjectByPage(String localCode, String projName, String unitInfoId, Page page) throws Exception;
}
