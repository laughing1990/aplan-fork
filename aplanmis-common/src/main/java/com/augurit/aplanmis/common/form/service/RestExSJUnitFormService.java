package com.augurit.aplanmis.common.form.service;

import com.alibaba.fastjson.JSON;
import com.augurit.agcloud.bpm.admin.sto.service.impl.AbstractFormDataOptManager;
import com.augurit.agcloud.bpm.common.constant.EDataOpt;
import com.augurit.agcloud.bpm.common.domain.ActStoForm;
import com.augurit.agcloud.bpm.common.domain.ActStoForminst;
import com.augurit.agcloud.bpm.common.domain.vo.FormDataOptResult;
import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.GDUnitType;
import com.augurit.aplanmis.common.constants.UnitProjLinkmanType;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class RestExSJUnitFormService extends AbstractFormDataOptManager {
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
            if(aeaExProjBuild.getBuildId()==null || aeaExProjBuild.getBuildId() ==""){
                aeaExProjBuild.setBuildId(UuidUtil.generateUuid());
                aeaExProjBuild.setCreateTime(new Date());
                aeaExProjBuild.setRootOrgId(SecurityContext.getCurrentOrgId());
                aeaExProjBuildMapper.insertAeaExProjBuild(aeaExProjBuild);

                if (StringUtils.isBlank(aeaExProjBuild.getFormId())) throw new Exception("缺少formId");
                this.formSave(aeaExProjBuild.getFormId(), aeaExProjBuild.getBuildId(), EDataOpt.INSERT.getOpareteType(), null);
            }else {
                aeaExProjBuildMapper.updateAeaExProjBuild(aeaExProjBuild);
            }
        }
        String aeaExProjBuildUnitInfo = aeaExProjBuild.getAeaExProjBuildUnitInfo();
        List<AeaExProjBuildUnitInfo> list = JSON.parseArray(aeaExProjBuildUnitInfo,AeaExProjBuildUnitInfo.class);
        for (AeaExProjBuildUnitInfo exProjBuildUnitInfo : list) {
            if (exProjBuildUnitInfo.getApplicant()!=null && !"".equals(exProjBuildUnitInfo.getApplicant())){
                //单位基本信息aeaUnitInfo
                AeaUnitInfo aeaUnitInfo = new AeaUnitInfo();
                aeaUnitInfo.setUnitInfoId(exProjBuildUnitInfo.getUnitInfoId());
                aeaUnitInfo.setApplicant(exProjBuildUnitInfo.getApplicant());
                aeaUnitInfo.setIdrepresentative(exProjBuildUnitInfo.getIdrepresentative());
                aeaUnitInfo.setIdmobile(exProjBuildUnitInfo.getIdmobile());
                aeaUnitInfo.setOrganizationalCode(exProjBuildUnitInfo.getOrganizationalCode());
                aeaUnitInfo.setUnifiedSocialCreditCode(exProjBuildUnitInfo.getUnifiedSocialCreditCode());
                aeaUnitInfo.setIsGd(exProjBuildUnitInfo.getIsGd());
                if(aeaUnitInfo.getUnitInfoId() !=null){
                    aeaUnitInfoMapper.updateAeaUnitInfo(aeaUnitInfo);
                }else {
                    String s = UuidUtil.generateUuid();
                    aeaUnitInfo.setUnitInfoId(s);
                    exProjBuildUnitInfo.setUnitInfoId(s);//回填单位ID
                    aeaUnitInfo.setCreateTime(new Date());
                    aeaUnitInfo.setRootOrgId(SecurityContext.getCurrentOrgId());
                    aeaUnitInfo.setCreater(SecurityContext.getCurrentUserName());
                    aeaUnitInfoMapper.insertAeaUnitInfo(aeaUnitInfo);
                }

                //插入证书编号
                AeaHiCertinst aeaHiCertinst = new AeaHiCertinst();
                aeaHiCertinst.setCertinstId(exProjBuildUnitInfo.getCertinstId());
                aeaHiCertinst.setCertinstCode(exProjBuildUnitInfo.getCertinstCode());
                if(aeaHiCertinst.getCertinstId()!=null&&aeaHiCertinst.getCertinstId()!=""){
                    aeaHiCertinstMapper.updateAeaHiCertinst(aeaHiCertinst);
                }else {
                    if(exProjBuildUnitInfo.getCertinstCode()!=null){
                        String s = UuidUtil.generateUuid();
                        aeaHiCertinst.setCertinstId(s);
                        exProjBuildUnitInfo.setCertinstId(s);//回填证书ID
                        aeaHiCertinst.setCreateTime(new Date());
                        aeaHiCertinst.setRootOrgId(SecurityContext.getCurrentOrgId());
                        aeaHiCertinst.setCreater(SecurityContext.getCurrentUserName());
                        aeaHiCertinst.setCertId(UuidUtil.generateUuid());
                        aeaHiCertinstMapper.insertAeaHiCertinst(aeaHiCertinst);
                    }
                }

                //插入负责人信息linkmaninfo表
                AeaLinkmanInfo linkman = new AeaLinkmanInfo();
                linkman.setLinkmanInfoId(exProjBuildUnitInfo.getLinkmanInfoId());
                linkman.setLinkmanName(exProjBuildUnitInfo.getLinkmanName());
                linkman.setLinkmanMobilePhone(exProjBuildUnitInfo.getLinkmanMobilePhone());
                linkman.setLinkmanCertNo(exProjBuildUnitInfo.getLinkmanCertNo());
                if(exProjBuildUnitInfo.getLinkmanInfoId() == null && exProjBuildUnitInfo.getLinkmanName()!=null){
                    String linkmanId = UuidUtil.generateUuid();
                    linkman.setLinkmanInfoId(linkmanId);
                    linkman.setLinkmanType("u");
                    linkman.setIsActive("1");
                    linkman.setIsDeleted("0");
                    linkman.setCreater(SecurityContext.getCurrentUserName());
                    linkman.setCreateTime(new Date());
                    linkman.setRootOrgId(SecurityContext.getCurrentOrgId());
                    exProjBuildUnitInfo.setLinkmanInfoId(linkmanId);//负责人ID回填
                    aeaLinkmanInfoMapper.insertAeaLinkmanInfo(linkman);
                }else {
                    aeaLinkmanInfoMapper.updateAeaLinkmanInfo(linkman);
                }

                //单位项目信息表aea_unit_proj
                AeaUnitProj aeaUnitProj = new AeaUnitProj();
                aeaUnitProj.setUnitProjId(exProjBuildUnitInfo.getUnitProjId());
                aeaUnitProj.setProjInfoId(aeaExProjBuild.getProjInfoId());
                aeaUnitProj.setUnitInfoId(exProjBuildUnitInfo.getUnitInfoId());
                aeaUnitProj.setUnitType(exProjBuildUnitInfo.getUnitType());
                aeaUnitProj.setLinkmanInfoId(exProjBuildUnitInfo.getLinkmanInfoId());
                aeaUnitProj.setQualLevelId(exProjBuildUnitInfo.getQualLevelId());
                aeaUnitProj.setCertinstId(exProjBuildUnitInfo.getCertinstId());
                aeaUnitProj.setSafeLicenceNum(exProjBuildUnitInfo.getUnitSafeLicenceNum());
                if(aeaUnitProj.getUnitProjId() != null){
                    int i = aeaUnitProjMapper.updateAeaUnitProj(aeaUnitProj);
                }else {
                    String s = UuidUtil.generateUuid();
                    aeaUnitProj.setUnitProjId(s);
                    exProjBuildUnitInfo.setUnitProjId(s);//回填单位项目id
                    aeaUnitProj.setCreateTime(new Date());
                    aeaUnitProj.setCreater(SecurityContext.getCurrentUserName());
                    aeaUnitProj.setIsOwner("0");
                    aeaUnitProj.setIsDeleted("0");
                    int i = aeaUnitProjMapper.insertAeaUnitProj(aeaUnitProj);
                }

                //负责人信息aea_unit_proj_linkman表
                AeaUnitProjLinkman manager = new AeaUnitProjLinkman();
                manager.setProjLinkmanId(exProjBuildUnitInfo.getProjLinkmanId());
                manager.setRegisterNum(exProjBuildUnitInfo.getRegisterNum());
                manager.setSafeLicenceNum(exProjBuildUnitInfo.getPersonSafeLicenceNum());
                manager.setProfessionSealNum(exProjBuildUnitInfo.getProfessionSealNum());
                manager.setTitleGrade(exProjBuildUnitInfo.getTitleGrade());
                manager.setTitleCertNum(exProjBuildUnitInfo.getTitleCertNum());
                manager.setLinkmanType(exProjBuildUnitInfo.getLinkmanType());
                manager.setUnitProjId(exProjBuildUnitInfo.getUnitProjId());
                manager.setLinkmanInfoId(exProjBuildUnitInfo.getLinkmanInfoId());
                manager.setProfessionCertType(exProjBuildUnitInfo.getProfessionCertType());
                if(manager.getProjLinkmanId()!=null){
                    aeaUnitProjLinkmanMapper.updateAeaUnitProjLinkman(manager);
                }else {
                    String s = UuidUtil.generateUuid();
                    manager.setProjLinkmanId(s);
                    manager.setCreateTime(new Date());
                    manager.setCreater(SecurityContext.getCurrentUserName());
                    exProjBuildUnitInfo.setProjLinkmanId(s);//回填项目单位联系人Id
                    aeaUnitProjLinkmanMapper.insertAeaUnitProjLinkman(manager);
                }

                //人员设置
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
                if(next.getUnitType()!=null){
                    if(next.getUnitType().equals(GDUnitType.GENERAL_CONTRACTING_UNIT.getValue()) || next.getUnitType().equals(GDUnitType.CONSTRUCTION_CONTRACTOR.getValue())){
                        managerInfo.setLinkmanType(UnitProjLinkmanType.FZR.getValue());
                    }else if(next.getUnitType().equals(GDUnitType.CONSTRUCTION_SUBCONTRACT.getValue()) || next.getUnitType().equals(GDUnitType.LABOR_SUBCONTRACT.getValue())){
                        managerInfo.setLinkmanType(UnitProjLinkmanType.JSFZR.getValue());
                    }else if(next.getUnitType().equals(GDUnitType.SUPERVISION_UNIT.getValue())){
                        managerInfo.setLinkmanType(UnitProjLinkmanType.XMZJ.getValue());
                    }
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
                    aeaExProjBuildUnitInfo.setIsGd(aeaUnitInfoById.getIsGd());
                }
                if (aeaLinkmanInfoById != null) {
                    aeaExProjBuildUnitInfo.setLinkmanInfoId(aeaLinkmanInfoById.getLinkmanInfoId());
                    aeaExProjBuildUnitInfo.setLinkmanName(aeaLinkmanInfoById.getLinkmanName());
                    aeaExProjBuildUnitInfo.setLinkmanMobilePhone(aeaLinkmanInfoById.getLinkmanMobilePhone());
                    aeaExProjBuildUnitInfo.setLinkmanCertNo(aeaLinkmanInfoById.getLinkmanCertNo());
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
                        String unitType = aeaExProjBuildUnitInfo.getUnitType();
                        //personsetting start
                        PersonSetting person = null;
                        if (unitType.equals(GDUnitType.CONSTRUCTION_CONTRACTOR.getValue())){
                            if(unitProjLinkman!=null && !unitProjLinkman.getLinkmanType().equals(UnitProjLinkmanType.FZR.getValue())){
                                person = this.zhuzhuangPersonSetting(unitProjLinkman);
                                person.setUnitProjId(next.getUnitProjId());

                            }
                        }else if(unitType.equals(GDUnitType.CONSTRUCTION_SUBCONTRACT.getValue())){
                            if(unitProjLinkman!=null && !unitProjLinkman.getLinkmanType().equals(UnitProjLinkmanType.JSFZR.getValue())){
                                person = this.zhuzhuangPersonSetting(unitProjLinkman);
                                person.setUnitProjId(next.getUnitProjId());
                            }
                        }else if (unitType.equals(GDUnitType.LABOR_SUBCONTRACT.getValue())){
                            if(unitProjLinkman!=null && !unitProjLinkman.getLinkmanType().equals(UnitProjLinkmanType.JSFZR.getValue())){
                                person = this.zhuzhuangPersonSetting(unitProjLinkman);
                                person.setUnitProjId(next.getUnitProjId());
                            }
                        }else if(unitType.equals(GDUnitType.SUPERVISION_UNIT.getValue())){
                            if(unitProjLinkman!=null && !unitProjLinkman.getLinkmanType().equals(UnitProjLinkmanType.XMZJ.getValue())){
                                person = this.zhuzhuangPersonSetting(unitProjLinkman);
                                person.setUnitProjId(next.getUnitProjId());
                            }
                        }
                        if(person != null){
                            listperson.add(person);
                        }
                    }
                }
                if(listperson != null &&listperson.size()>0){
                    aeaExProjBuildUnitInfo.setPersonSetting(listperson);
                }else {
                    listperson.add(new PersonSetting());
                    aeaExProjBuildUnitInfo.setPersonSetting(listperson);
                }
                String unitType = aeaExProjBuildUnitInfo.getUnitType();
                if(unitType !=null){
                    if (unitType.equals(GDUnitType.GENERAL_CONTRACTING_UNIT.getValue())) {
                        map.put("gongchengzongFrom", aeaExProjBuildUnitInfo);
                    } else if (unitType.equals(GDUnitType.CONSTRUCTION_CONTRACTOR.getValue())) {
                        map.put("applyShigongzongFrom", aeaExProjBuildUnitInfo);
                    } else if (unitType.equals(GDUnitType.CONSTRUCTION_SUBCONTRACT.getValue())) {
                        map.put("applyShigongzhuanyefenbaoFrom", aeaExProjBuildUnitInfo);
                    } else if (unitType.equals(GDUnitType.LABOR_SUBCONTRACT.getValue())) {
                        map.put("applyShigonglaowufenbaoFrom", aeaExProjBuildUnitInfo);
                    } else if (unitType.equals(GDUnitType.SUPERVISION_UNIT.getValue())) {
                        map.put("applyJianliFrom", aeaExProjBuildUnitInfo);
                    }
                }
            }
        }
        return map;
    }

    public PersonSetting zhuzhuangPersonSetting(AeaUnitProjLinkman aeaUnitProjLinkman){
        PersonSetting person = new PersonSetting();
        AeaLinkmanInfo aeaLinkmanInfoById = aeaLinkmanInfoMapper.getAeaLinkmanInfoById(aeaUnitProjLinkman.getLinkmanInfoId());
        if(aeaUnitProjLinkman!=null){
            person.setProjLinkmanId(aeaUnitProjLinkman.getProjLinkmanId());
            person.setLinkmanType(aeaUnitProjLinkman.getLinkmanType());
            person.setLinkmanInfoId(aeaUnitProjLinkman.getLinkmanInfoId());
            person.setSafeLicenceNum(aeaUnitProjLinkman.getSafeLicenceNum());
            person.setProfessionCertType(aeaUnitProjLinkman.getProfessionCertType());
            person.setTitleCertNum(aeaUnitProjLinkman.getTitleCertNum());
        }
        if(aeaLinkmanInfoById!=null){
            person.setLinkmanName(aeaLinkmanInfoById.getLinkmanName());
            person.setLinkmanCertNo(aeaLinkmanInfoById.getLinkmanCertNo());
        }
        return person;
    }

    @Override
    public FormDataOptResult doformSave(String formId, String metaTableId, Integer opType, Object dataEntity) throws Exception {
        FormDataOptResult result = new FormDataOptResult();
        result.setSuccess(true);
        ActStoForminst actStoForminst = new ActStoForminst();
        actStoForminst.setFormId(formId);
        actStoForminst.setFormPrimaryKey(metaTableId);
        result.setActStoForminst(actStoForminst);
        return result;
    }

    @Override
    public FormDataOptResult doformDelete(ActStoForm formVo, Object dataEntity) throws Exception {
        return null;
    }
}
