package com.augurit.aplanmis.common.service.instance.impl;

import com.augurit.aplanmis.common.domain.AeaItemInout;
import com.augurit.aplanmis.common.mapper.AeaItemInoutMapper;
import com.augurit.aplanmis.common.service.instance.AeaHiItemInoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AeaHiItemInoutServiceImpl implements AeaHiItemInoutService {

    @Autowired
    private AeaItemInoutMapper aeaItemInoutMapper;

    @Override
    public List<AeaItemInout> getAeaItemInoutMatCertByItemVerId(String itemVerId, String rootOrgId) {
        AeaItemInout aeaItemInout=new AeaItemInout();
        aeaItemInout.setItemVerId(itemVerId);
        aeaItemInout.setIsInput("0");
        aeaItemInout.setIsStateIn("0");
        aeaItemInout.setRootOrgId(rootOrgId);
        List<AeaItemInout> list = aeaItemInoutMapper.listAeaItemInoutMatCert(aeaItemInout);
        return list;
    }
}
