package com.augurit.aplanmis.supermarket.dataexchange.service;

import com.augurit.aplanmis.common.domain.AeaImService;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.domain.AeaUnitLinkman;
import com.augurit.aplanmis.common.mapper.AeaImServiceMapper;
import com.augurit.aplanmis.common.mapper.AeaUnitInfoMapper;
import com.augurit.aplanmis.common.mapper.AeaUnitLinkmanMapper;
import com.augurit.aplanmis.supermarket.dataexchange.mapper.DataExchangeMapper;
import com.augurit.aplanmis.supermarket.linkmanInfo.service.AeaLinkmanInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UploadDataService {
    @Autowired
    private DataExchangeMapper dataExchangeMapper;

    public void insertOne() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format1 = format.format(new Date());
        dataExchangeMapper.insertOne(UUID.randomUUID().toString(), "测试多数据-" + format1, new Date());
    }

    public void batchUploadImServiceData(List<AeaImService> imServices) throws Exception {
        if (imServices.size() > 0) {
            dataExchangeMapper.batchInsertAeaImService(imServices);
        }
    }

    public void batchUploadImUnitInfos(List<AeaUnitInfo> unitInfos) throws Exception {
        if (unitInfos.size() > 0) {
            dataExchangeMapper.batchInsertAeaUnitInfo(unitInfos);
        }
    }

    public void batchUploadLinkmanInfos(List<AeaLinkmanInfo> linkmanInfos) throws Exception {
        if (linkmanInfos.size() > 0) {
            dataExchangeMapper.batchInsertAeaLinkmanInfo(linkmanInfos);
        }

    }

    public void batchUploadUnintLinkmanData(List<AeaUnitLinkman> unitLinkmen) throws Exception {
        if (unitLinkmen.size() > 0) {
            dataExchangeMapper.batchInsertAeaUnitLinkman(unitLinkmen);
        }
    }

    public void clearPreDbData() throws Exception {
        dataExchangeMapper.batchDeleteAeaImService();
        dataExchangeMapper.batchDeleteAeaUnitLinkman();
        dataExchangeMapper.batchDeleteAeaUnitInfo();
        dataExchangeMapper.batchDaleteAeaLinkmanInfo();
    }

}
