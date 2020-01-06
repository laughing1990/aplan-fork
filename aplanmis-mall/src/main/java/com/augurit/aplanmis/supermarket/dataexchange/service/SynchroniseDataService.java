package com.augurit.aplanmis.supermarket.dataexchange.service;

import com.augurit.aplanmis.common.domain.AeaImService;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.domain.AeaUnitLinkman;
import com.augurit.aplanmis.common.mapper.AeaImServiceMapper;
import com.augurit.aplanmis.common.mapper.AeaLinkmanInfoMapper;
import com.augurit.aplanmis.common.mapper.AeaUnitInfoMapper;
import com.augurit.aplanmis.supermarket.dataexchange.mapper.DataExchangeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SynchroniseDataService {

    @Autowired
    private DataExchangeMapper dataExchangeMapper;

    //获取所有服务类型
    public List<AeaImService> getAllImServices() throws Exception {
        return dataExchangeMapper.getAllImServices();
    }

    //获取所有单位信息
    public List<AeaUnitInfo> getAllImUnitInfos() throws Exception {
        return dataExchangeMapper.getAllImUnitInfos();
    }

    //获取所有联系人信息
    public List<AeaLinkmanInfo> getAllLinkmanInfos() throws Exception {
        return dataExchangeMapper.getAllLinkmanInfos();
    }

    //获取所有单位和联系人关联信息
    public List<AeaUnitLinkman> getAllUnitLinkmanData() throws Exception {
        return dataExchangeMapper.getAllUnitLinkmanData();
    }

}
