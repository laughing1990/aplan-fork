package com.augurit.aplanmis.supermarket.main.service.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.supermarket.main.service.ZjcsAeaProjInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Transactional
@Service
public class ZjcsAeaProjInfoServicelmpl implements ZjcsAeaProjInfoService {

    @Autowired
    AeaUnitProjMapper aeaUnitProjMapper;
    @Autowired
    AeaUnitInfoMapper aeaUnitInfoMapper;
    @Autowired
    private AeaProjInfoMapper aeaProjInfoMapper;
    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;

    @Value("${dg.sso.access.platform.org.top-org-id}")
    protected String topOrgId;

    @Override
    public List<AeaProjInfo> listAeaProjInfo(AeaProjInfo aeaProjInfo) {
        List<AeaProjInfo> list = aeaProjInfoMapper.listAeaProjInfo(aeaProjInfo);
        return list;
    }

    @Override
    public AeaProjInfo getAeaProjInfoById(String id) {
        if (StringUtils.isBlank(id)) {
            throw new InvalidParameterException(id);
        }
        return aeaProjInfoMapper.getAeaProjInfoById(id);
    }


    @Override
    public void insertAeaProjInfo(AeaProjInfo aeaProjInfo){
        aeaProjInfo.setRootOrgId(topOrgId);
        aeaProjInfoMapper.insertAeaProjInfo(aeaProjInfo);
    }

    @Override
    public void updateAeaProjInfo(AeaProjInfo aeaProjInfo) {
        aeaProjInfoMapper.updateAeaProjInfo(aeaProjInfo);
    }

/*    @Override
    public PageInfo<AeaProjInfo>  myProjectByPage(String localCode, String projName, String unitInfoId, Page page) throws Exception{
        List<AeaProjInfo> list = new ArrayList<AeaProjInfo>();
        PageHelper.startPage(page);
        list = aeaProjInfoMapper.myProjectByPage(localCode, projName, unitInfoId);
        page.setTotal(list.size());
        return new PageInfo<AeaProjInfo>(list);
    }*/
}
