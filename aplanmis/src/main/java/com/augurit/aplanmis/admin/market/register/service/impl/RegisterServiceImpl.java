package com.augurit.aplanmis.admin.market.register.service.impl;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.mapper.BscAttDetailMapper;
import com.augurit.agcloud.bsc.mapper.BscAttMapper;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.admin.market.register.service.RegisterService;
import com.augurit.aplanmis.admin.market.register.vo.*;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.dic.BscDicCodeItemService;
import com.augurit.aplanmis.common.service.linkman.AeaLinkmanInfoService;
import com.augurit.aplanmis.common.vo.AeaHiCertinstBVo;
import com.augurit.aplanmis.common.vo.ServiceMatterVo;
import com.github.pagehelper.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private AeaUnitInfoMapper aeaUnitInfoMapper;
    @Autowired
    private AeaLinkmanInfoMapper aeaLinkmanInfoMapper;

    @Autowired
    private BscAttMapper bscAttMapper;

    @Autowired
    private AeaImUnitServiceMapper aeaImUnitServiceMapper;

    @Autowired
    private AeaHiCertinstMapper aeaHiCertinstMapper;

    @Autowired
    private AeaImCertinstMajorMapper aeaImCertinstMajorMapper;

    @Autowired
    private BscAttDetailMapper bscAttDetailMapper;

    @Autowired
    private AeaImServiceLinkmanMapper aeaImServiceLinkmanMapper;

    @Autowired
    private AeaUnitLinkmanMapper aeaUnitLinkmanMapper;

    @Autowired
    private AeaImClientServiceMapper aeaImClientServiceMapper;

    @Autowired
    private AeaLinkmanInfoService aeaLinkmanInfoService;

    @Autowired
    BscDicCodeItemService bscDicCodeItemService;

    @Autowired
    private AeaImUnitServiceMapper unitServiceMapper;

    @Autowired
    private AeaBusCertinstMapper aeaBusCertinstMapper;

    @Override
    public List<AeaUnitInfo> getRegisterUnitList(RegisterSearch registerSearch, Page page) throws Exception {
        PageHelper.startPage(page);
        AeaUnitInfo aeaUnitInfo = new AeaUnitInfo();
        BeanUtils.copyProperties(registerSearch, aeaUnitInfo);
        if (StringUtils.isNotBlank(registerSearch.getRegisterType())) {
            //RegisterType 1中介机构，2业主单位
            if ("1".equals(registerSearch.getRegisterType())) {
                aeaUnitInfo.setIsImUnit("1");
            } else if ("2".equals(registerSearch.getRegisterType())) {
                aeaUnitInfo.setIsOwnerUnit("1");
            }
        }

        return aeaUnitInfoMapper.getRegisterUnitList(aeaUnitInfo);
    }

    @Override
    public RegisterResultVo getRegisterUnitDetail(String unitInfoId) throws Exception {
        RegisterResultVo agentRegisterVo = new RegisterResultVo();
        AeaUnitInfo aeaUnitInfo = aeaUnitInfoMapper.getAeaUnitInfoById(unitInfoId);
        if (aeaUnitInfo != null) {
            BscDicCodeItem idTypeItem = bscDicCodeItemService.getItemByTypeCodeAndItemCodeAndOrgId("IDTYPE", aeaUnitInfo.getIdtype(), SecurityContext.getCurrentOrgId());
            aeaUnitInfo.setIdtype(idTypeItem.getItemName());
            aeaUnitInfo.setUnitNature("1".equals(aeaUnitInfo.getUnitNature()) ? "企业" : "2".equals(aeaUnitInfo.getUnitNature()) ? "事业单位" : "3".equals(aeaUnitInfo.getUnitNature()) ? "社会组织" : "");

            //联系人列表：包括授权人列表，执业人员列表
            List<AeaLinkmanInfo> contactManList = aeaLinkmanInfoMapper.getAeaLinkmanInfoByUnitInfoId(unitInfoId, 0);

            //单位附件
            //List<BscAttForm> unitFileList = bscAttMapper.listAttLinkAndDetail("AEA_UNIT_INFO", "UNIT_INFO_ID", aeaUnitInfo.getUnitInfoId(), null, SecurityContext.getCurrentOrgId(), null);

            List<BscAttFileAndDir> unitFileList = bscAttDetailMapper.searchFileAndDirsSimple(null, SecurityContext.getCurrentOrgId(), "AEA_UNIT_INFO", "UNIT_INFO_ID", new String[]{aeaUnitInfo.getUnitInfoId()});
            /// aeaHiCertinstBVo.setCertinstDetail(attInfoList);

            //服务信息
            ServiceMatterVo serviceMatterVo = new ServiceMatterVo();
            serviceMatterVo.setUnitInfoId(aeaUnitInfo.getUnitInfoId());

            AeaImUnitService aeaImUnitService = new AeaImUnitService();
            AeaHiCertinstBVo aeaHiCertinstBVo = new AeaHiCertinstBVo();
            RegisterServiceAndQualVo registerServiceAndQualVo = new RegisterServiceAndQualVo();
            List<AeaHiCertinstBVo> aeaHiCertinstBVoList = new ArrayList<AeaHiCertinstBVo>();
            List<ServiceMatterVo> matterVoList = aeaImUnitServiceMapper.listAeaImUnitServiceVo(serviceMatterVo);
            if (CollectionUtils.isNotEmpty(matterVoList)) {
                ServiceMatterVo matterVo = matterVoList.get(0);
                BeanUtils.copyProperties(matterVo, aeaImUnitService);
                registerServiceAndQualVo.setAeaImUnitService(aeaImUnitService);

                AeaBusCertinst aeaBusCertinst = new AeaBusCertinst();
                aeaBusCertinst.setBusRecordId(matterVo.getUnitServiceId());
                aeaBusCertinst.setBusTableName("AEA_IM_UNIT_SERVICE");
                List<AeaHiCertinstBVo> certinstBVoList = aeaHiCertinstMapper.getAeaHiCertinstByBusCertinst(aeaBusCertinst);
                if (CollectionUtils.isNotEmpty(certinstBVoList)) {
                    aeaHiCertinstBVo = certinstBVoList.get(0);

                    //查询major-id
                    String certinstId = aeaHiCertinstBVo.getCertinstId();
                    AeaImCertinstMajor certinstMajor = new AeaImCertinstMajor();
                    certinstMajor.setCertinstId(certinstId);
                    List<AeaImCertinstMajor> majors = aeaImCertinstMajorMapper.listAeaImCertinstMajor(certinstMajor);
                    if (CollectionUtils.isNotEmpty(majors)) {
                        List<String> majorList = majors.stream().map(AeaImCertinstMajor::getMajorId).collect(Collectors.toList());
                        aeaHiCertinstBVo.setMajorId(majorList);
                    }

                    List<BscAttFileAndDir> attInfoList = bscAttDetailMapper.searchFileAndDirsSimple(null, SecurityContext.getCurrentOrgId(), "AEA_HI_CERTINST", "CERTINST_ID", new String[]{certinstId});
                    aeaHiCertinstBVo.setCertinstDetail(attInfoList);
                    matterVo.setCertinstBVos(certinstBVoList);
                    //服务附件
                    List<BscAttForm> serviceFileList = bscAttMapper.listAttLinkAndDetail("AEA_HI_CERTINST", "CERTINST_ID", certinstId, null, SecurityContext.getCurrentOrgId(), null);

                    aeaHiCertinstBVoList.add(aeaHiCertinstBVo);
                    registerServiceAndQualVo.setAeaHiCertinstBVo(aeaHiCertinstBVoList);
                    agentRegisterVo.setServiceFileList(serviceFileList);
                }
            }

            //授权人列表
            List<AeaLinkmanInfo> authorManList = aeaLinkmanInfoMapper.listBindLinkmanByUnitId(aeaUnitInfo.getUnitInfoId(), null, "1", null);


            //执业人员
            List<AeaLinkmanInfo> practiceManList = aeaImServiceLinkmanMapper.listAeaImServiceLinkmanByUnitInfoId(unitInfoId, null, null);
            List<BscAttForm> practiceManFileList = null;
            AeaLinkmanInfo practiceManInfo = null;
            List<BscAttForm> practiceManCertFileList = new ArrayList<BscAttForm>();
            if (CollectionUtils.isNotEmpty(practiceManList)) {
                practiceManInfo = practiceManList.get(0);
                AeaBusCertinst aeaBusCertinst = new AeaBusCertinst();
                aeaBusCertinst.setBusRecordId(practiceManInfo.getServiceLinkmanId());
                aeaBusCertinst.setBusTableName("aea_im_service_linkman");
                List<AeaHiCertinstBVo> certinsts = aeaHiCertinstMapper.getAeaHiCertinstByBusCertinst(aeaBusCertinst);
                for (AeaHiCertinstBVo certinstBVo : certinsts) {
                    List<BscAttFileAndDir> attInfoList = bscAttDetailMapper.searchFileAndDirsSimple(null, SecurityContext.getCurrentOrgId(), "AEA_HI_CERTINST", "CERTINST_ID", new String[]{certinstBVo.getCertinstId()});
                    certinstBVo.setCertinstDetail(attInfoList);

                    //执行人员证书附件
                    List<BscAttForm> certFileList = bscAttMapper.listAttLinkAndDetail("aea_hi_certinst", "CERTINST_ID", certinstBVo.getCertinstId(), null, SecurityContext.getCurrentOrgId(), null);
                    practiceManCertFileList.addAll(certFileList);
                }
                practiceManInfo.setCertinsts(certinsts);


                //执行人员附件
                practiceManFileList = bscAttMapper.listAttLinkAndDetail("AEA_LINKMAN_INFO", "LINKMAN_INFO_ID", practiceManInfo.getLinkmanInfoId(), null, SecurityContext.getCurrentOrgId(), null);


            }

            agentRegisterVo.setPracticeManInfo(practiceManInfo);
            agentRegisterVo.setPracticeManCertFileList(practiceManCertFileList);
            agentRegisterVo.setPracticeManFileList(practiceManFileList);
            agentRegisterVo.setAuthorManList(authorManList);
            agentRegisterVo.setRegisterServiceAndQualVo(registerServiceAndQualVo);
            agentRegisterVo.setUnitFileList(unitFileList);
            agentRegisterVo.setContactManList(contactManList);
            agentRegisterVo.setUnitInfo(aeaUnitInfo);
        }
        return agentRegisterVo;
    }


    /**
     * 审批入驻机构
     *
     * @param registerAuditVo
     * @throws Exception
     */
    @Override
    public void examineService(RegisterAuditVo registerAuditVo) throws Exception {
        if (registerAuditVo != null && StringUtils.isNotBlank(registerAuditVo.getUnitInfoId())) {
            AeaUnitInfo aeaUnitInfo = aeaUnitInfoMapper.getAeaUnitInfoById(registerAuditVo.getUnitInfoId());
            aeaUnitInfo.setAuditFlag(registerAuditVo.getAuditFlag());
            aeaUnitInfo.setUnitInfoId(registerAuditVo.getUnitInfoId());
            aeaUnitInfoMapper.updateAeaUnitInfo(aeaUnitInfo);


            ServiceMatterVo serviceMatterVo = new ServiceMatterVo();
            serviceMatterVo.setUnitInfoId(aeaUnitInfo.getUnitInfoId());
            serviceMatterVo.setAuditFlag("2");
            List<ServiceMatterVo> matterVoList = aeaImUnitServiceMapper.listAeaImUnitServiceVo(serviceMatterVo);
            for (ServiceMatterVo resultVo : matterVoList) {
                AeaImUnitService unitService = new AeaImUnitService();
                unitService.setUnitServiceId(resultVo.getUnitServiceId());
                unitService.setMemo(registerAuditVo.getMemo());
                unitService.setAuditFlag(registerAuditVo.getAuditFlag());
                unitService.setAuditTime(new Date());
                unitServiceMapper.updateAeaImUnitService(unitService);

                AeaBusCertinst aeaBusCertinst = new AeaBusCertinst();
                aeaBusCertinst.setBusRecordId(resultVo.getUnitServiceId());
                aeaBusCertinst.setBusTableName("AEA_IM_UNIT_SERVICE");
                aeaBusCertinst.setAuditFlag(registerAuditVo.getAuditFlag());
                aeaBusCertinst.setMemo(registerAuditVo.getMemo());
                aeaBusCertinstMapper.updateAeaBusCertinstByAudit(aeaBusCertinst);
            }

            List<AeaLinkmanInfo> practiceManList = aeaImServiceLinkmanMapper.listAeaImServiceLinkmanByUnitInfoId(registerAuditVo.getUnitInfoId(), null, "2");
            for (AeaLinkmanInfo resultVo : practiceManList) {
                AeaImServiceLinkman aeaImServiceLinkman = new AeaImServiceLinkman();
                aeaImServiceLinkman.setAuditFlag(registerAuditVo.getAuditFlag());
                aeaImServiceLinkman.setMemo(registerAuditVo.getMemo());
                aeaImServiceLinkman.setAuditTime(new Date());
                aeaImServiceLinkman.setServiceLinkmanId(resultVo.getServiceLinkmanId());
                aeaImServiceLinkmanMapper.updateAeaImServiceLinkman(aeaImServiceLinkman);

                AeaBusCertinst aeaBusCertinst = new AeaBusCertinst();
                aeaBusCertinst.setBusRecordId(resultVo.getServiceLinkmanId());
                aeaBusCertinst.setBusTableName("aea_im_service_linkman");
                aeaBusCertinst.setAuditFlag(registerAuditVo.getAuditFlag());
                aeaBusCertinst.setMemo(registerAuditVo.getMemo());
                aeaBusCertinstMapper.updateAeaBusCertinstByAudit(aeaBusCertinst);
            }

        }
    }

    @Override
    public OwnerRegisterResultVo getOwnerRegisterDetail(String unitInfoId) throws Exception {
        OwnerRegisterResultVo ownerRegisterResultVo = new OwnerRegisterResultVo();
        AeaUnitInfo aeaUnitInfo = aeaUnitInfoMapper.getAeaUnitInfoById(unitInfoId);
        if (aeaUnitInfo != null) {
            BscDicCodeItem idTypeItem = bscDicCodeItemService.getItemByTypeCodeAndItemCodeAndOrgId("IDTYPE", aeaUnitInfo.getIdtype(), SecurityContext.getCurrentOrgId());
            aeaUnitInfo.setIdtype(idTypeItem.getItemName());
            aeaUnitInfo.setUnitNature("1".equals(aeaUnitInfo.getUnitNature()) ? "企业" : "2".equals(aeaUnitInfo.getUnitNature()) ? "事业单位" : "3".equals(aeaUnitInfo.getUnitNature()) ? "社会组织" : "");

            //联系人列表：包括授权人列表，执业人员列表
            List<AeaLinkmanInfo> contactManList = aeaLinkmanInfoMapper.getAeaLinkmanInfoByUnitInfoId(unitInfoId, 0);

            List<BscAttFileAndDir> unitFileList = bscAttDetailMapper.searchFileAndDirsSimple(null, SecurityContext.getCurrentOrgId(), "AEA_UNIT_INFO", "UNIT_INFO_ID", new String[]{aeaUnitInfo.getUnitInfoId()});

            //授权人列表
            List<AeaLinkmanInfo> authorManList = aeaLinkmanInfoMapper.listBindLinkmanByUnitId(aeaUnitInfo.getUnitInfoId(), null, "1", null);
            if (CollectionUtils.isNotEmpty(authorManList)) {
                AeaLinkmanInfo authorMan = authorManList.get(0);
                //授权人附件
                List<BscAttFileAndDir> authorManFiles = bscAttDetailMapper.searchFileAndDirsSimple(null, SecurityContext.getCurrentOrgId(), "AEA_LINKMAN_INFO", "LINKMAN_INFO_ID", new String[]{authorMan.getLinkmanInfoId()});
                ownerRegisterResultVo.setAuthorManInfo(authorMan);
                ownerRegisterResultVo.setAuthorManFileList(authorManFiles);
            }
            ownerRegisterResultVo.setContactManList(contactManList);
            ownerRegisterResultVo.setUnitFileList(unitFileList);
        }
        ownerRegisterResultVo.setUnitInfo(aeaUnitInfo);
        return ownerRegisterResultVo;
    }

    @Override
    public void examineOwnerUnitService(RegisterAuditVo registerAuditVo) throws Exception {
        if (registerAuditVo != null && StringUtils.isNotBlank(registerAuditVo.getUnitInfoId())) {
            AeaUnitInfo aeaUnitInfo = aeaUnitInfoMapper.getAeaUnitInfoById(registerAuditVo.getUnitInfoId());
            aeaUnitInfo.setAuditFlag(registerAuditVo.getAuditFlag());
            aeaUnitInfo.setUnitInfoId(registerAuditVo.getUnitInfoId());
            aeaUnitInfoMapper.updateAeaUnitInfo(aeaUnitInfo);
        }
    }

    /**
     * 删除||启用入住机构
     *
     * @param unitInfoId 机构ID
     * @param isDeleted
     */
    @Override
    public void deleteOrEnableAgentUnit(String unitInfoId, String isDeleted) throws Exception {
        if (StringUtils.isEmpty(unitInfoId)) return;
        AeaUnitInfo unitInfo = aeaUnitInfoMapper.getAeaUnitIncludeDeleteById(unitInfoId);
        if (null == unitInfo) return;
        String modifier = SecurityContext.getCurrentUserName();
        //更新单位
        aeaUnitInfoMapper.deleteOrEnableAeaUnitInfo(unitInfoId, modifier, isDeleted);
        //更新单位联系人
        aeaLinkmanInfoMapper.updateAllUnitLinkman(unitInfoId, modifier, isDeleted);
        String isImUnit = unitInfo.getIsImUnit();
        if (StringUtils.isNotBlank(isImUnit) && "1".equals(isImUnit)) {
            //中介机构

            List<AeaImUnitService> unitServiceList = aeaImUnitServiceMapper.listAgentUnitService(unitInfoId);
            if (!unitServiceList.isEmpty()) {
                // 更新中介服务aea_im_unit_servcie
                aeaImUnitServiceMapper.deleteOrEnableAllUnitServiceByUnitInfoId(unitInfoId, modifier, isDeleted);
                // 更新服务执业人 aea_im_service_linkman
                aeaImServiceLinkmanMapper.deleteOrEnableAllServiceLinkman(unitInfoId, isDeleted);
                // 更新服务委托人 aea_im_client_service
                String[] unitServiceIds = unitServiceList.stream().map(AeaImUnitService::getUnitServiceId).toArray(String[]::new);
                aeaImClientServiceMapper.deleteOrEnableAllUnitClientService(unitServiceIds, isDeleted);
            }
        }
    }


}
