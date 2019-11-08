package com.augurit.aplanmis.common.service.form.impl;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.form.AeaProjDrawingSerivce;
import com.augurit.aplanmis.common.vo.AeaProjDrawing;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class AeaProjDrawingSerivceImpl implements AeaProjDrawingSerivce {

    @Autowired
    private AeaUnitProjMapper aeaUnitProjMapper;
    @Autowired
    private AeaUnitProjLinkmanMapper aeaUnitProjLinkmanMapper;
    @Autowired
    private AeaUnitLinkmanMapper aeaUnitLinkmanMapper;
    @Autowired
    private AeaLinkmanInfoMapper aeaLinkmanInfoMapper;
    @Autowired
    private AeaUnitInfoMapper aeaUnitInfoMapper;


    @Override
    public List<AeaProjDrawing> getAeaProjDrawing(String projInfoId) {
        AeaUnitProj aeaUnitProj = new AeaUnitProj();
        aeaUnitProj.setProjInfoId(projInfoId);

        List<AeaUnitProj> aeaUnitInfos = null;

        try {
            aeaUnitInfos = aeaUnitProjMapper.listAeaUnitProj(aeaUnitProj);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<AeaProjDrawing> drawings = new ArrayList<>();


        for ( AeaUnitProj id :aeaUnitInfos) {
            //info.getUnitType().equals("4")||info.getUnitType().equals("3")||info.getUnitType().equals("13") && info != null(info.getUnitType().equals("9")||info.getUnitType().equals("3")||info.getUnitType().equals("2")) &&
            if(   id.getUnitType()!=null && (id.getUnitType().equals("9")||id.getUnitType().equals("4")||id.getUnitType().equals("3"))  ) {
                AeaProjDrawing aeaProjDrawing = new AeaProjDrawing();
                AeaUnitInfo infoQ = new AeaUnitInfo();
                infoQ.setUnitInfoId(id.getUnitInfoId());
                aeaProjDrawing.setUnitProjId(id.getUnitProjId());
                aeaProjDrawing.setProjInfoId(id.getProjInfoId());
                aeaProjDrawing.setUnitType(id.getUnitType());

                List<AeaUnitInfo> infos = aeaUnitInfoMapper.listAeaUnitInfo(infoQ);
                if ( id.getLinkmanInfoId()!=null&&id.getLinkmanInfoId()!="") {
                    String projectLearderId = id.getLinkmanInfoId();
                    AeaLinkmanInfo aeaLinkmanInfoById = aeaLinkmanInfoMapper.getAeaLinkmanInfoById(projectLearderId);
                    aeaProjDrawing.setProjectLeader(aeaLinkmanInfoById.getLinkmanName());
                    aeaProjDrawing.setProjectLeaderCertNum(aeaLinkmanInfoById.getLinkmanCertNo());
                    aeaProjDrawing.setLinkmanInfoId(aeaLinkmanInfoById.getLinkmanInfoId());

                    AeaUnitProjLinkman aeaUnitProjLinkmanQ = new AeaUnitProjLinkman();
                    aeaUnitProjLinkmanQ.setLinkmanInfoId(aeaLinkmanInfoById.getLinkmanInfoId());
                    aeaUnitProjLinkmanQ.setUnitProjId(id.getUnitProjId());
                    List<AeaUnitProjLinkman> linkmanList = aeaUnitProjLinkmanMapper.listAeaUnitProjLinkman(aeaUnitProjLinkmanQ);
                    if (linkmanList.size()>0){
                        AeaUnitProjLinkman fuzeren = linkmanList.get(0);
                        aeaProjDrawing.setLinkmanType(fuzeren.getLinkmanType());
                        aeaProjDrawing.setProfessionCertType(fuzeren.getProfessionCertType());
                        aeaProjDrawing.setProfessionSealNum(fuzeren.getRegisterNum());
                        aeaProjDrawing.setTitleCertNum(fuzeren.getTitleCertNum());
                        aeaProjDrawing.setTitleGrade(fuzeren.getTitleGrade());
                        aeaProjDrawing.setPrjSpty(fuzeren.getPrjSpty());

                    }
                }
                if (infos.size()>0){
                    AeaUnitInfo info = infos.get(0);
                    aeaProjDrawing.setOrganizationalCode(info.getOrganizationalCode());
                    aeaProjDrawing.setUnifiedSocialCreditCode(info.getUnifiedSocialCreditCode());
                    aeaProjDrawing.setApplicant(info.getApplicant());

                    aeaProjDrawing.setUnitInfoId(info.getUnitInfoId());
                    ArrayList<AeaUnitProjLinkman> linkmen = new ArrayList<>();
                    List<String> ids = aeaUnitLinkmanMapper.getLinkManIdByUnitInfoId(info.getUnitInfoId());
                    for (String ide :ids) {
                        AeaLinkmanInfo linkmanInfo = aeaLinkmanInfoMapper.getAeaLinkmanInfoById(ide);
                        AeaUnitProjLinkman aeaUnitProjLinkman = new AeaUnitProjLinkman();
                        if (linkmanInfo!=null){
                            aeaUnitProjLinkman.setLinkmanInfoId(linkmanInfo.getLinkmanInfoId());
                            aeaUnitProjLinkman.setLinkmanName(linkmanInfo.getLinkmanName());
                            aeaUnitProjLinkman.setUnitProjId(info.getUnitProjId());
                            aeaUnitProjLinkman.setLinkmanCertNo(linkmanInfo.getLinkmanCertNo());
                            aeaUnitProjLinkman.setUnitProjId(id.getUnitProjId());
                            AeaUnitProjLinkman query = new AeaUnitProjLinkman();
                            query.setUnitProjId(info.getUnitProjId());
                            query.setLinkmanInfoId(linkmanInfo.getLinkmanInfoId());
                            List<AeaUnitProjLinkman> linkmanList = aeaUnitProjLinkmanMapper.listAeaUnitProjLinkman(query);
                            if (linkmanList.size()>0) {
                                AeaUnitProjLinkman linkman = linkmanList.get(0);
                                aeaUnitProjLinkman.setPrjSpty(linkman.getPrjSpty());
                                aeaUnitProjLinkman.setTitleCertNum(linkman.getTitleCertNum());
                                aeaUnitProjLinkman.setProjLinkmanId(linkman.getProjLinkmanId());
                                aeaUnitProjLinkman.setLinkmanType(linkman.getLinkmanType());
                                aeaUnitProjLinkman.setProfessionCertType(linkman.getProfessionCertType());
                                aeaUnitProjLinkman.setProfessionSealNum(linkman.getProfessionSealNum());
                                aeaUnitProjLinkman.setRegisterNum(linkman.getRegisterNum());
                                aeaUnitProjLinkman.setTitleGrade(linkman.getTitleGrade());
                                linkmen.add(aeaUnitProjLinkman);
                            }
                        }
                        aeaProjDrawing.setLinkmen(linkmen);
                    }
                    drawings.add(aeaProjDrawing);
                }
            }
        }

        return drawings;
    }



    @Override
    public void saveAeaProjDrawing(List<AeaProjDrawing> drawings) {
        for (AeaProjDrawing aeaProjDrawing: drawings) {
            AeaUnitProj aeaUnitProj = new AeaUnitProj();
            aeaUnitProj.setUnitInfoId(aeaProjDrawing.getUnitInfoId());
            aeaUnitProj.setLinkmanInfoId(aeaProjDrawing.getLinkmanInfoId());
            aeaUnitProj.setProjInfoId(aeaProjDrawing.getProjInfoId());
            aeaUnitProj.setIsOwner("1");
            aeaUnitProj.setUnitType(aeaProjDrawing.getUnitType());
            aeaUnitProj.setLinkmanInfoId(aeaProjDrawing.getProjectLeaderCertNum());
            aeaUnitProj.setCreater(SecurityContext.getCurrentUser().getUserName());
            aeaUnitProj.setCreateTime(new Date());

            if (aeaProjDrawing.getUnitProjId()==null||aeaProjDrawing.getUnitInfoId().equals("")) {
                aeaUnitProj.setUnitProjId(UUID.randomUUID().toString());
                aeaUnitProjMapper.insertAeaUnitProj(aeaUnitProj);
            }else{
                try {
                    aeaUnitProjMapper.updateAeaUnitProj(aeaUnitProj);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            List<AeaUnitProjLinkman> linkmen = aeaProjDrawing.getLinkmen();

            String unitProjId = aeaUnitProj.getUnitProjId();
            String unitInfoId = aeaUnitProj.getUnitInfoId();
            String projInfoId = aeaUnitProj.getProjInfoId();
            String unitType = aeaUnitProj.getUnitType();
            List<AeaUnitProj> unitProjs = aeaUnitProjMapper.findUnitProjByProjIdAndUnitIdAndunitType(projInfoId, unitInfoId, unitType);

            if (unitProjs.size() > 0) {
                AeaUnitProj unitProj = unitProjs.get(0);
                String unitProjId1 = unitProj.getUnitProjId();
                //先删除所有
                aeaUnitProjLinkmanMapper.deleteAllByUnitProjId(unitProjId1, SecurityContext.getCurrentUserName());
                //新增
                for (AeaUnitProjLinkman vo : linkmen) {
                    if (StringUtils.isNotBlank(vo.getLinkmanInfoId())) {
                        AeaUnitProjLinkman man = new AeaUnitProjLinkman(unitProjId, vo.getLinkmanInfoId(), vo.getLinkmanType());
                        man.setProfessionCertType(vo.getProfessionCertType());
                        man.setProfessionSealNum(vo.getProfessionSealNum());
                        man.setTitleGrade(vo.getTitleGrade());
                        man.setTitleCertNum(vo.getTitleCertNum());
                        man.setRegisterNum(vo.getRegisterNum());
                        man.setSafeLicenceNum(vo.getSafeLicenceNum());
                        man.setPrjSpty(vo.getPrjSpty());
                        man.setCreater(SecurityContext.getCurrentUserName());
                        man.setCreateTime(new Date());
                        aeaUnitProjLinkmanMapper.insertAeaUnitProjLinkman(man);
                    }
                }
            }
            AeaUnitProjLinkman man = new AeaUnitProjLinkman();

            //todo fuzeren负责人信息
            man.setProjLinkmanId(UUID.randomUUID().toString());
            man.setLinkmanInfoId(aeaProjDrawing.getLinkmanInfoId());
            man.setUnitProjId(aeaProjDrawing.getUnitProjId());
            man.setLinkmanType(aeaProjDrawing.getLinkmanType());
            man.setProfessionCertType(aeaProjDrawing.getProfessionCertType());
            man.setProfessionSealNum(aeaProjDrawing.getProfessionSealNum());
            man.setTitleGrade(aeaProjDrawing.getTitleGrade());
            man.setTitleCertNum(aeaProjDrawing.getTitleCertNum());
            man.setPrjSpty(aeaProjDrawing.getPrjSpty());
            aeaUnitProjLinkmanMapper.insertAeaUnitProjLinkman(man);
            /**
             *
             *     private String linkmanInfoId;//项目负责人id
             *     private String projectLeader ;//项目负责人
             *     private String projectLeaderCertNum;//项目负责人 ID Card
             *
             *     private String linkmanType;//承担角色，来PROJ_UNIT_LINKMAN_TYPE项目单位联系人类型
             *     private String professionCertType;//执业注册证类型',',
             *     private String professionSealNum;//执业印章号
             *     private String titleGrade;//职称等级，来自于数据字典（C_TITLE）
             *     private String titleCertNum;//职称证号',
             * **/


        }
    }

}
