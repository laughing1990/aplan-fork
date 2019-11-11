package com.augurit.aplanmis.front.form.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import freemarker.ext.beans.HashAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class RestExSJUnitFormService {
    @Autowired
    private AeaExProjBuildMapper aeaExProjBuildMapper;
    @Autowired
    private AeaUnitProjMapper aeaUnitProjMapper;
    @Autowired
    private AeaUnitInfoMapper aeaUnitInfoMapper;
    @Autowired
    private AeaUnitProjLinkmanMapper aeaUnitProjLinkmanMapper;
    @Autowired
    private AeaLinkmanInfoMapper aeaLinkmanInfoMapper;
    @Autowired
    private AeaHiCertinstMapper aeaHiCertinstMapper;
    @Autowired
    private AeaImQualLevelMapper aeaImQualLevelMapper;


    public void saveOrUpdateSJUnitInfo(AeaExProjBuild aeaExProjBuild) throws Exception {
        if (aeaExProjBuild != null){
            if(aeaExProjBuild.getBuildId()==null){
                aeaExProjBuild.setBuildId(UuidUtil.generateUuid());
                aeaExProjBuild.setCreateTime(new Date());
                aeaExProjBuild.setRootOrgId(SecurityContext.getCurrentOrgId());
                int i = aeaExProjBuildMapper.insertAeaExProjBuild(aeaExProjBuild);
            }else {
                aeaExProjBuildMapper.updateAeaExProjBuild(aeaExProjBuild);
            }
        }
        String aeaExProjBuildUnitInfo = aeaExProjBuild.getAeaExProjBuildUnitInfo();
        List<AeaExProjBuildUnitInfo> list = JSON.parseArray(aeaExProjBuildUnitInfo,AeaExProjBuildUnitInfo.class);
        for (AeaExProjBuildUnitInfo exProjBuildUnitInfo : list) {
            AeaUnitProj aeaUnitProj = new AeaUnitProj();

            AeaHiCertinst aeaHiCertinst = new AeaHiCertinst();//证书编号
            aeaHiCertinst.setCertinstId(exProjBuildUnitInfo.getCertinstId());
            aeaHiCertinst.setCertinstCode(exProjBuildUnitInfo.getCertinstCode());
            if(aeaHiCertinst.getCertinstId()!=null&&aeaHiCertinst.getCertinstId()!=""){
                aeaHiCertinstMapper.updateAeaHiCertinst(aeaHiCertinst);
            }else {
                if(exProjBuildUnitInfo.getCertinstCode()!=null){
                    String s = UuidUtil.generateUuid();
                    aeaHiCertinst.setCertinstId(s);
                    exProjBuildUnitInfo.setCertinstId(s);
                    aeaHiCertinst.setCreateTime(new Date());
                    aeaHiCertinst.setRootOrgId(SecurityContext.getCurrentOrgId());
                    aeaHiCertinst.setCreater(SecurityContext.getCurrentUserName());
                    aeaHiCertinst.setCertId(UuidUtil.generateUuid());
                    aeaHiCertinstMapper.insertAeaHiCertinst(aeaHiCertinst);
                }
            }

            aeaUnitProj.setUnitProjId(exProjBuildUnitInfo.getUnitProjId());
            aeaUnitProj.setProjInfoId(aeaExProjBuild.getProjInfoId());
            aeaUnitProj.setUnitInfoId(exProjBuildUnitInfo.getUnitInfoId());
            aeaUnitProj.setUnitType(exProjBuildUnitInfo.getUnitType());
            aeaUnitProj.setLinkmanInfoId(exProjBuildUnitInfo.getLinkmanInfoId());
            aeaUnitProj.setQualLevelId(exProjBuildUnitInfo.getQualLevelId());//to
            aeaUnitProj.setCertinstId(exProjBuildUnitInfo.getCertinstId());
            aeaUnitProj.setSafeLicenceNum(exProjBuildUnitInfo.getUnitSafeLicenceNum());
            if(aeaUnitProj.getLinkmanInfoId() == null && exProjBuildUnitInfo.getLinkmanName()!=null){
                AeaLinkmanInfo linkman = new AeaLinkmanInfo();
                String linkmanId = UuidUtil.generateUuid();
                linkman.setLinkmanInfoId(linkmanId);
                linkman.setLinkmanName(exProjBuildUnitInfo.getLinkmanName());
                linkman.setLinkmanMobilePhone(exProjBuildUnitInfo.getLinkmanMobilePhone());
                linkman.setLinkmanType("u");
                linkman.setIsActive("1");
                linkman.setIsDeleted("0");
                linkman.setCreater(SecurityContext.getCurrentUserName());
                linkman.setCreateTime(new Date());
                linkman.setRootOrgId(SecurityContext.getCurrentOrgId());
                aeaUnitProj.setLinkmanInfoId(linkmanId);
                exProjBuildUnitInfo.setLinkmanInfoId(linkmanId);
                aeaLinkmanInfoMapper.insertAeaLinkmanInfo(linkman);
            }
            if(aeaUnitProj.getUnitProjId() != null){
                int i = aeaUnitProjMapper.updateAeaUnitProj(aeaUnitProj);
            }else {
                aeaUnitProj.setUnitProjId(UuidUtil.generateUuid());
                aeaUnitProj.setCreateTime(new Date());
                aeaUnitProj.setCreater(SecurityContext.getCurrentUserName());
                aeaUnitProj.setIsOwner("0");
                aeaUnitProj.setIsDeleted("0");
                int i = aeaUnitProjMapper.insertAeaUnitProj(aeaUnitProj);
            }

            AeaUnitInfo aeaUnitInfo = new AeaUnitInfo();
            aeaUnitInfo.setUnitInfoId(exProjBuildUnitInfo.getUnitInfoId());
            aeaUnitInfo.setApplicant(exProjBuildUnitInfo.getApplicant());
            aeaUnitInfo.setIdrepresentative(exProjBuildUnitInfo.getIdrepresentative());
            aeaUnitInfo.setIdmobile(exProjBuildUnitInfo.getIdmobile());
            aeaUnitInfo.setOrganizationalCode(exProjBuildUnitInfo.getOrganizationalCode());
            aeaUnitInfo.setUnifiedSocialCreditCode(exProjBuildUnitInfo.getUnifiedSocialCreditCode());
            if(exProjBuildUnitInfo.getUnitInfoId() !=null){
                aeaUnitInfoMapper.updateAeaUnitInfo(aeaUnitInfo);
            }else {
                aeaUnitInfo.setUnitInfoId(UuidUtil.generateUuid());
                aeaUnitInfo.setCreateTime(new Date());
                aeaUnitInfo.setRootOrgId(SecurityContext.getCurrentOrgId());
                aeaUnitInfo.setCreater(SecurityContext.getCurrentUserName());
                aeaUnitInfoMapper.insertAeaUnitInfo(aeaUnitInfo);
            }

            AeaLinkmanInfo aeaLinkmanInfo = new AeaLinkmanInfo();//负责人信息
            aeaLinkmanInfo.setLinkmanInfoId(exProjBuildUnitInfo.getLinkmanInfoId());
            aeaLinkmanInfo.setLinkmanName(exProjBuildUnitInfo.getLinkmanName());
            aeaLinkmanInfo.setLinkmanMobilePhone(exProjBuildUnitInfo.getLinkmanMobilePhone());
            if(aeaLinkmanInfo.getLinkmanInfoId()!=null && aeaLinkmanInfo.getLinkmanInfoId()!=""){
                aeaLinkmanInfoMapper.updateAeaLinkmanInfo(aeaLinkmanInfo);
            }else {
                if(exProjBuildUnitInfo.getLinkmanName()!=null){
                    if(aeaLinkmanInfo.getLinkmanInfoId()==null){
                        aeaLinkmanInfo.setLinkmanInfoId(aeaUnitProj.getLinkmanInfoId());
                        exProjBuildUnitInfo.setLinkmanInfoId(aeaUnitProj.getLinkmanInfoId());
                    }
                    String linkmaninfoId = UuidUtil.generateUuid();
                    aeaLinkmanInfo.setLinkmanInfoId(linkmaninfoId);
                    exProjBuildUnitInfo.setLinkmanInfoId(linkmaninfoId);
                    aeaLinkmanInfo.setCreateTime(new Date());
                    aeaLinkmanInfo.setRootOrgId(SecurityContext.getCurrentOrgId());
                    aeaLinkmanInfo.setCreater(SecurityContext.getCurrentUserName());
                    aeaLinkmanInfo.setLinkmanType("u");
                    aeaLinkmanInfo.setIsActive("0");
                    aeaLinkmanInfo.setIsDeleted("0");
                    aeaLinkmanInfoMapper.insertAeaLinkmanInfo(aeaLinkmanInfo);
                }
            }

            AeaUnitProjLinkman manager = new AeaUnitProjLinkman();//负责人信息
            manager.setProjLinkmanId(exProjBuildUnitInfo.getProjLinkmanId());
            manager.setRegisterNum(exProjBuildUnitInfo.getRegisterNum());
            manager.setSafeLicenceNum(exProjBuildUnitInfo.getPersonSafeLicenceNum());
            manager.setProfessionSealNum(exProjBuildUnitInfo.getProfessionSealNum());
            manager.setTitleGrade(exProjBuildUnitInfo.getTitleGrade());
            manager.setTitleCertNum(exProjBuildUnitInfo.getTitleCertNum());
            manager.setLinkmanType(exProjBuildUnitInfo.getLinkmanType());
            manager.setUnitProjId(exProjBuildUnitInfo.getUnitProjId());
            manager.setLinkmanInfoId(exProjBuildUnitInfo.getLinkmanInfoId());
            if(manager.getProjLinkmanId()!=null &&manager.getProjLinkmanId()!=""){
                aeaUnitProjLinkmanMapper.updateAeaUnitProjLinkman(manager);
            }else {
                if(manager.getUnitProjId()!=null){
                    if(manager.getLinkmanInfoId() == null){
                        manager.setLinkmanInfoId(aeaLinkmanInfo.getLinkmanInfoId());
                    }
                    manager.setProjLinkmanId(UuidUtil.generateUuid());
                    manager.setCreateTime(new Date());
                    manager.setCreater(SecurityContext.getCurrentUserName());
                    aeaUnitProjLinkmanMapper.insertAeaUnitProjLinkman(manager);
                }
            }

            String personSettings = exProjBuildUnitInfo.getPersonSetting();
            List<PersonSetting> listpersonSettings = JSON.parseArray(personSettings, PersonSetting.class);
            if(listpersonSettings!=null&&listpersonSettings.size()>0){
                for (PersonSetting person : listpersonSettings) {
                    if(person.getLinkmanName()!=null){
                        AeaUnitProjLinkman aeaUnitProjLinkman = new AeaUnitProjLinkman();
                        aeaUnitProjLinkman.setProjLinkmanId(person.getProjLinkmanId());
                        aeaUnitProjLinkman.setLinkmanInfoId(person.getLinkmanInfoId());
                        aeaUnitProjLinkman.setLinkmanType(person.getLinkmanType());
                        aeaUnitProjLinkman.setProfessionCertType(person.getProfessionCertType());
                        aeaUnitProjLinkman.setProfessionSealNum(person.getSafeLicenceNum());
                        aeaUnitProjLinkman.setTitleCertNum(person.getTitleCertNum());
                        aeaUnitProjLinkman.setSafeLicenceNum(person.getSafeLicenceNum());
                        aeaUnitProjLinkman.setUnitProjId(exProjBuildUnitInfo.getUnitProjId());
                        if(aeaUnitProjLinkman.getProjLinkmanId()!=null){
                            aeaUnitProjLinkmanMapper.updateAeaUnitProjLinkman(aeaUnitProjLinkman);
                        }else {
                            aeaUnitProjLinkman.setProjLinkmanId(UuidUtil.generateUuid());
                            aeaUnitProjLinkman.setCreateTime(new Date());
                            aeaUnitProjLinkman.setCreater(SecurityContext.getCurrentUserName());
                            aeaUnitProjLinkmanMapper.insertAeaUnitProjLinkman(aeaUnitProjLinkman);
                        }
                    }
                }
            }
        }
    }

    public Map<String,Object> initExSJUnit(String projInfoId) throws Exception {
        AeaExProjBuild aeaExProjBuildByProjId = aeaExProjBuildMapper.getAeaExProjBuildByProjId(projInfoId);

        Map<String,Object> map = new HashMap<>();
        map.put("unitInfoShowFrom",aeaExProjBuildByProjId);

        AeaUnitProj aeaUnitProj = new AeaUnitProj();
        aeaUnitProj.setProjInfoId(projInfoId);
        List<AeaUnitProj> aeaUnitProjs = aeaUnitProjMapper.listAeaUnitProj(aeaUnitProj);
        if(aeaUnitProjs!=null && aeaUnitProjs.size()>0) {
            for (Iterator<AeaUnitProj> iterator = aeaUnitProjs.iterator(); iterator.hasNext(); ) {
                AeaUnitProj next = iterator.next();
                AeaUnitInfo aeaUnitInfoById = aeaUnitInfoMapper.getAeaUnitInfoById(next.getUnitInfoId());//单位信息
                AeaLinkmanInfo aeaLinkmanInfoById = aeaLinkmanInfoMapper.getAeaLinkmanInfoById(next.getLinkmanInfoId());//负责人
                AeaUnitProjLinkman managerInfo = new AeaUnitProjLinkman();//负责人相关信息
                managerInfo.setUnitProjId(next.getUnitProjId());
                managerInfo.setLinkmanInfoId(next.getLinkmanInfoId());
                if(next.getUnitType().equals("9") || next.getUnitType().equals("10")){
                    managerInfo.setLinkmanType("104001");
                }else if(next.getUnitType().equals("6") || next.getUnitType().equals("8")){
                    managerInfo.setLinkmanType("104002");
                }else if(next.getUnitType().equals("19")){
                    managerInfo.setLinkmanType("105001");
                }
                List<AeaUnitProjLinkman> managerInfos = aeaUnitProjLinkmanMapper.listAeaUnitProjLinkman(managerInfo);
                AeaImQualLevel aeaImQualLevelById = aeaImQualLevelMapper.getAeaImQualLevelById(next.getQualLevelId());
                AeaHiCertinst aeaHiCertinstById = aeaHiCertinstMapper.getAeaHiCertinstById(next.getCertinstId());
                //aeaExProjBuildUnitInfo start
                ExSJUnitFromDetails aeaExProjBuildUnitInfo = new ExSJUnitFromDetails();
                aeaExProjBuildUnitInfo.setUnitInfoId(next.getUnitInfoId());
                aeaExProjBuildUnitInfo.setUnitProjId(next.getUnitProjId());
                aeaExProjBuildUnitInfo.setUnitType(next.getUnitType());
                if(aeaUnitInfoById!=null){
                    aeaExProjBuildUnitInfo.setApplicant(aeaUnitInfoById.getApplicant());
                    aeaExProjBuildUnitInfo.setIdrepresentative(aeaUnitInfoById.getIdrepresentative());
                    aeaExProjBuildUnitInfo.setIdmobile(aeaUnitInfoById.getIdmobile());
                    aeaExProjBuildUnitInfo.setLinkmanInfoId(aeaUnitInfoById.getLinkmanInfoId());
                }
                if (aeaLinkmanInfoById != null) {
                    aeaExProjBuildUnitInfo.setLinkmanName(aeaLinkmanInfoById.getLinkmanName());
                    aeaExProjBuildUnitInfo.setLinkmanMobilePhone(aeaLinkmanInfoById.getLinkmanMobilePhone());
                }
                if (managerInfos != null && managerInfos.size()>0) {
                    aeaExProjBuildUnitInfo.setProjLinkmanId(managerInfos.get(0).getProjLinkmanId());
                    aeaExProjBuildUnitInfo.setRegisterNum(managerInfos.get(0).getRegisterNum());
                    aeaExProjBuildUnitInfo.setPersonSafeLicenceNum(managerInfos.get(0).getSafeLicenceNum());
                    aeaExProjBuildUnitInfo.setProfessionCertType(managerInfos.get(0).getProfessionCertType());
                    aeaExProjBuildUnitInfo.setProfessionSealNum(managerInfos.get(0).getProfessionSealNum());
                    aeaExProjBuildUnitInfo.setTitleGrade(managerInfos.get(0).getTitleGrade());
                    aeaExProjBuildUnitInfo.setTitleCertNum(managerInfos.get(0).getTitleCertNum());
                    aeaExProjBuildUnitInfo.setLinkmanType(managerInfos.get(0).getLinkmanType());
                }
                aeaExProjBuildUnitInfo.setQualLevelId(next.getQualLevelId());
                if(aeaImQualLevelById!=null){
                    aeaExProjBuildUnitInfo.setQualLevelName(aeaImQualLevelById.getQualLevelName());
                    aeaExProjBuildUnitInfo.setQualLevelCode(aeaImQualLevelById.getQualLevelCode());
                }
                aeaExProjBuildUnitInfo.setCertinstId(next.getCertinstId());
                if(aeaHiCertinstById!=null){
                    aeaExProjBuildUnitInfo.setCertinstCode(aeaHiCertinstById.getCertinstCode());
                }
                aeaExProjBuildUnitInfo.setUnitSafeLicenceNum(next.getSafeLicenceNum());
                if(aeaUnitInfoById!=null){
                    aeaExProjBuildUnitInfo.setOrganizationalCode(aeaUnitInfoById.getOrganizationalCode());
                    aeaExProjBuildUnitInfo.setUnifiedSocialCreditCode(aeaUnitInfoById.getUnifiedSocialCreditCode());
                }
                //aeaExProjBuildUnitInfo end
                //人员设置
                AeaUnitProjLinkman aeaUnitProjLinkman = new AeaUnitProjLinkman();
                aeaUnitProjLinkman.setUnitProjId(next.getUnitProjId());
                List<AeaUnitProjLinkman> aeaUnitProjLinkmen = aeaUnitProjLinkmanMapper.listAeaUnitProjLinkman(aeaUnitProjLinkman);
                List<PersonSetting> listperson = new ArrayList<>();
                if(aeaUnitProjLinkmen!=null&&aeaUnitProjLinkmen.size()>0){
                    for (Iterator<AeaUnitProjLinkman> aeaUnitProjLinkmanIterator = aeaUnitProjLinkmen.iterator(); aeaUnitProjLinkmanIterator.hasNext(); ) {
                        AeaUnitProjLinkman unitProjLinkman = aeaUnitProjLinkmanIterator.next();
                        AeaLinkmanInfo personSetting = aeaLinkmanInfoMapper.getAeaLinkmanInfoById(unitProjLinkman.getLinkmanInfoId());
                        //personsetting start
                        PersonSetting person = new PersonSetting();
                        if(unitProjLinkman!=null){
                            person.setProjLinkmanId(unitProjLinkman.getProjLinkmanId());
                            person.setLinkmanType(unitProjLinkman.getLinkmanType());
                            person.setLinkmanInfoId(unitProjLinkman.getLinkmanInfoId());
                            person.setSafeLicenceNum(unitProjLinkman.getSafeLicenceNum());
                            person.setProfessionCertType(unitProjLinkman.getProfessionCertType());
                            person.setTitleCertNum(unitProjLinkman.getTitleCertNum());
                        }
                        if(personSetting!=null){
                            person.setLinkmanName(personSetting.getLinkmanName());
                            person.setLinkmanCertNo(personSetting.getLinkmanCertNo());
                        }
                        person.setUnitProjId(next.getUnitProjId());
                        listperson.add(person);
                    }
                }
                aeaExProjBuildUnitInfo.setPersonSetting(listperson);
                String unitType = aeaExProjBuildUnitInfo.getUnitType();
                if (unitType.equals("9")) {
                    map.put("gongchengzongFrom", aeaExProjBuildUnitInfo);
                } else if (unitType.equals("10")) {
                    map.put("applyShigongzongFrom", aeaExProjBuildUnitInfo);
                } else if (unitType.equals("6")) {
                    map.put("applyShigongzhuanyefenbaoFrom", aeaExProjBuildUnitInfo);
                } else if (unitType.equals("8")) {
                    map.put("applyShigonglaowufenbaoFrom", aeaExProjBuildUnitInfo);
                } else if (unitType.equals("19")) {
                    map.put("applyJianliFrom", aeaExProjBuildUnitInfo);
                }
            }
        }
        System.out.print(map);
        return map;
    }
}
