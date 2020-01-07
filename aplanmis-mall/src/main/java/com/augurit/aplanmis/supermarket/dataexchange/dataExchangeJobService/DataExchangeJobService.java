package com.augurit.aplanmis.supermarket.dataexchange.dataExchangeJobService;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.aplanmis.common.domain.AeaImService;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.domain.AeaUnitLinkman;
import com.augurit.aplanmis.common.mapper.AeaImServiceMapper;
import com.augurit.aplanmis.common.mapper.AeaUnitInfoMapper;
import com.augurit.aplanmis.common.mapper.AeaUnitLinkmanMapper;
import com.augurit.aplanmis.supermarket.dataexchange.service.SynchroniseDataService;
import com.augurit.aplanmis.supermarket.dataexchange.service.UploadDataService;
import com.augurit.aplanmis.supermarket.linkmanInfo.service.AeaLinkmanInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataExchangeJobService {

    @Autowired
    private AeaImServiceMapper aeaImServiceMapper;
    @Autowired
    private AeaUnitInfoMapper aeaUnitInfoMapper;
    @Autowired
    private AeaUnitLinkmanMapper aeaUnitLinkmanMapper;
    @Autowired
    private AeaLinkmanInfoService aeaLinkmanInfoService;
    @Autowired
    private UploadDataService uploadDataService;
    @Autowired
    private SynchroniseDataService synchroniseDataService;

    //上传数据到前置库
    public void uploadDataToPreDatabase() throws Exception {
        //先清空前置库数据
        uploadDataService.clearPreDbData();

        //将服务类型同步到前置库
        AeaImService aeaImService = new AeaImService();
        aeaImService.setIsDelete("0");
        aeaImService.setIsActive("1");
        List<AeaImService> imServices = aeaImServiceMapper.listAeaImService(new AeaImService());
        if (imServices.size() > 0) {
            uploadDataService.batchUploadImServiceData(imServices);
        }

        //将通过入驻审核的中介机构同步到前置库
        AeaUnitInfo unitInfo = new AeaUnitInfo();
        unitInfo.setIsImUnit("1");
        unitInfo.setIsDeleted("0");
        unitInfo.setAuditFlag("1");
        List<AeaUnitInfo> unitInfos = aeaUnitInfoMapper.listAeaUnitInfo(unitInfo);
        if (unitInfos.size() > 0) {
            uploadDataService.batchUploadImUnitInfos(unitInfos);
            for (AeaUnitInfo unitInfo1 : unitInfos) {
                //将中介机构的联系人信息和关联信息同步到前置库
                List<AeaLinkmanInfo> linkmanInfos = aeaLinkmanInfoService.getAeaLinkmanInfoByUnitInfoId(unitInfo1.getUnitInfoId(), null);
                if (linkmanInfos.size() > 0) {
                    String linkmanInfoIds = linkmanInfos.stream().map(AeaLinkmanInfo::getLinkmanInfoId).collect(Collectors.joining(","));
                    AeaUnitLinkman aeaUnitLinkman = new AeaUnitLinkman();
                    aeaUnitLinkman.setUnitInfoId(unitInfo1.getUnitInfoId());
                    List<AeaUnitLinkman> unitLinkmen = aeaUnitLinkmanMapper.listAeaUnitLinkman(aeaUnitLinkman);
                    List<AeaUnitLinkman> temp = new ArrayList();
                    for (AeaUnitLinkman unitLinkman : unitLinkmen) {
                        if (linkmanInfoIds.contains(unitLinkman.getLinkmanInfoId())) {
                            temp.add(unitLinkman);
                        }
                    }
                    uploadDataService.batchUploadLinkmanInfos(linkmanInfos);
                    uploadDataService.batchUploadUnintLinkmanData(temp);
                }
            }
        }
    }

    //同步前置库数据
    public void synchroniseDataFromPreJob() throws Exception {

        List<AeaImService> imServices = synchroniseDataService.getAllImServices();
        List<AeaUnitInfo> unitInfos = synchroniseDataService.getAllImUnitInfos();
        List<AeaLinkmanInfo> linkmanInfos = synchroniseDataService.getAllLinkmanInfos();
        List<AeaUnitLinkman> unitLinkmen = synchroniseDataService.getAllUnitLinkmanData();
        if (imServices.size() < 1) {
            //将所有同步的服务类型状态置为删除
            aeaImServiceMapper.batchDeleteAeaImService(null);
        } else {
            String imServiceIds = imServices.stream().map(AeaImService::getServiceId).collect(Collectors.joining(","));
            AeaImService aeaImService = new AeaImService();
            aeaImService.setIsExternal("1");
            aeaImService.setIsDelete("0");
            aeaImService.setRootOrgId(SecurityContext.getCurrentOrgId());
            List<AeaImService> localImService = aeaImServiceMapper.listAeaImService(aeaImService);
            List<String> beDeleteServiceIds = new ArrayList();
            localImService.forEach(service -> {
                if (!imServiceIds.contains(service.getServiceId())) {
                    beDeleteServiceIds.add(service.getServiceId());
                }
            });
            //获取本地所有的外部数据，如果和前置库数据匹配不到就置为删除状态
            if (beDeleteServiceIds.size() > 0) aeaImServiceMapper.batchDeleteAeaImService(beDeleteServiceIds);
            //批量插入前置库的数据
            aeaImServiceMapper.batchInsertAeaImService(imServices);
        }
        if (unitInfos.size() < 1) {
            //将所有同步的单位状态置为删除
            aeaUnitInfoMapper.batchDeleteAeaUnitInfo(null);
            //将所有同步的联系人信息状态置为删除
            aeaLinkmanInfoService.batchDeleteAeaLinkmanInfo(null);
        } else {
            String imunitInfoIds = unitInfos.stream().map(AeaUnitInfo::getUnitInfoId).collect(Collectors.joining(","));
            AeaUnitInfo unitInfo = new AeaUnitInfo();
            unitInfo.setIsExternal("1");
            unitInfo.setIsDeleted("0");
            unitInfo.setRootOrgId(SecurityContext.getCurrentOrgId());
            List<AeaUnitInfo> localUnitInfos = aeaUnitInfoMapper.listAeaUnitInfo(unitInfo);
            List<String> beDeleteUnitInfoids = new ArrayList();
            localUnitInfos.forEach(unit -> {
                if (!imunitInfoIds.contains(unit.getUnitInfoId())) {
                    beDeleteUnitInfoids.add(unit.getUnitInfoId());
                }
            });
            if (beDeleteUnitInfoids.size() > 0) aeaUnitInfoMapper.batchDeleteAeaUnitInfo(beDeleteUnitInfoids);
            aeaUnitInfoMapper.batchInsertAeaUnitInfo(unitInfos);
            if (linkmanInfos.size() < 1) {
                //将所有同步的联系人信息状态置为删除
                aeaLinkmanInfoService.batchDeleteAeaLinkmanInfo(null);
            } else {
                String linkmanInfoids = linkmanInfos.stream().map(AeaLinkmanInfo::getLinkmanInfoId).collect(Collectors.joining(","));
                AeaLinkmanInfo aeaLinkmanInfo = new AeaLinkmanInfo();
                aeaLinkmanInfo.setIsDeleted("0");
                aeaLinkmanInfo.setIsExternal("1");
                aeaLinkmanInfo.setRootOrgId(SecurityContext.getCurrentOrgId());
                List<AeaLinkmanInfo> localLinkmanInfos = aeaLinkmanInfoService.listAeaLinkmanInfo(aeaLinkmanInfo);
                List<String> beDeleleLinkmanInfoIds = new ArrayList();
                localLinkmanInfos.forEach(linkmanInfo -> {
                    if (!linkmanInfoids.contains(linkmanInfo.getLinkmanInfoId())) {
                        beDeleleLinkmanInfoIds.add(linkmanInfo.getLinkmanInfoId());
                    }
                });
                if (beDeleleLinkmanInfoIds.size() > 0)
                    aeaLinkmanInfoService.batchDeleteAeaLinkmanInfo(beDeleleLinkmanInfoIds);
                aeaLinkmanInfoService.batchInsertAeaLinkmanInfo(linkmanInfos);
                if (unitLinkmen.size() > 0)
                    aeaLinkmanInfoService.batchInsertAeaUnintLinkman(unitLinkmen);
            }
        }
    }
}
