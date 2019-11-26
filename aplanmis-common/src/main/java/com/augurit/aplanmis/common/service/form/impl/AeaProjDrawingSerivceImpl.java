package com.augurit.aplanmis.common.service.form.impl;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.GDUnitType;
import com.augurit.aplanmis.common.constants.UnitProjLinkmanType;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.form.AeaProjDrawingSerivce;
import com.augurit.aplanmis.common.vo.AeaProjDrawing;
import com.augurit.aplanmis.common.vo.AeaProjDrawingVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
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


        //筛选出 施工图审查机构 勘察单位 设计单位 //遍历单位 封装联系人
        for (AeaUnitProj id : aeaUnitInfos) {
            //info.getUnitType().equals("4")||info.getUnitType().equals("3")||info.getUnitType().equals("13") && info != null(info.getUnitType().equals("9")||info.getUnitType().equals("3")||info.getUnitType().equals("2")) &&
            if (id.getUnitType() != null && (id.getUnitType().equals(GDUnitType.CONSTRUCTION_DRAWING_REVIEW.getValue()) || id.getUnitType().equals(GDUnitType.SURVEY_UNIT.getValue()) || id.getUnitType().equals(GDUnitType.DESIGN_UNIT.getValue()))) {
                AeaProjDrawing aeaProjDrawing = new AeaProjDrawing();
                AeaUnitInfo infoQ = new AeaUnitInfo();
                infoQ.setUnitInfoId(id.getUnitInfoId());
                //封装 返回信息
                aeaProjDrawing.setUnitProjId(id.getUnitProjId());
                aeaProjDrawing.setProjInfoId(id.getProjInfoId());
                aeaProjDrawing.setUnitType(id.getUnitType());

                //aeaProjDrawing.setUnitInfoId(id.getUnitInfoId());
                //先封装负责人相关信息返回 后封装所有联系人返回
                List<AeaUnitInfo> infos = aeaUnitInfoMapper.listAeaUnitInfo(infoQ);

                List<AeaUnitProjLinkman> fl = aeaUnitProjLinkmanMapper.queryByUnitProjIdAndlinkType(id.getUnitProjId(), null, "1");

                if (fl.size() > 0) {
                    AeaUnitProjLinkman fuzeren = fl.get(0);
                    aeaProjDrawing.setLinkmanType(fuzeren.getLinkmanType());
                    aeaProjDrawing.setProfessionCertType(fuzeren.getProfessionCertType());
                    aeaProjDrawing.setProfessionSealNum(fuzeren.getProfessionSealNum());
                    aeaProjDrawing.setTitleCertNum(fuzeren.getTitleCertNum());
                    aeaProjDrawing.setTitleGrade(fuzeren.getTitleGrade());
                    aeaProjDrawing.setPrjSpty(fuzeren.getPrjSpty());
                    String linkmanInfoId = fuzeren.getLinkmanInfoId();
                    AeaLinkmanInfo idcard = aeaLinkmanInfoMapper.getAeaLinkmanInfoById(linkmanInfoId);
                    aeaProjDrawing.setProjectLeaderCertNum(idcard.getLinkmanCertNo());
                    aeaProjDrawing.setProjectLeader(idcard.getLinkmanName());
                    aeaProjDrawing.setProjectLeaderCertNum(idcard.getLinkmanCertNo());
                    aeaProjDrawing.setLinkmanInfoId(idcard.getLinkmanInfoId());

                }

                if (infos.size() > 0) {
                    AeaUnitInfo info = infos.get(0);
                    aeaProjDrawing.setOrganizationalCode(info.getOrganizationalCode());
                    aeaProjDrawing.setUnifiedSocialCreditCode(info.getUnifiedSocialCreditCode());
                    aeaProjDrawing.setApplicant(info.getApplicant());
                    aeaProjDrawing.setUnitInfoId(info.getUnitInfoId());
                    ArrayList<AeaUnitProjLinkman> linkmen = new ArrayList<>();
                    //查找企业项目所有人员 is delete = 0

                    AeaUnitProjLinkman aeaUnitProjLinkman1 = new AeaUnitProjLinkman();
                    aeaUnitProjLinkman1.setUnitProjId(id.getUnitProjId());
                    aeaUnitProjLinkman1.setIsDeleted("0");
                    List<AeaUnitProjLinkman> aeaUnitProjLinkmen = aeaUnitProjLinkmanMapper.listfuzeren(aeaUnitProjLinkman1);
                    for (AeaUnitProjLinkman linkman : aeaUnitProjLinkmen) {

                        AeaLinkmanInfo linkmanInfo = aeaLinkmanInfoMapper.getAeaLinkmanInfoById(linkman.getLinkmanInfoId());
                        //如果是负责人就不在人员设置里显示
                        if (UnitProjLinkmanType.FZR.getValue().equals(linkman.getLinkmanType())) {
                            continue;
                        }
                        AeaUnitProjLinkman aeaUnitProjLinkman = new AeaUnitProjLinkman();
                        aeaUnitProjLinkman.setLinkmanInfoId(linkmanInfo.getLinkmanInfoId());
                        aeaUnitProjLinkman.setLinkmanName(linkmanInfo.getLinkmanName());
                        aeaUnitProjLinkman.setUnitProjId(info.getUnitProjId());
                        aeaUnitProjLinkman.setLinkmanCertNo(linkmanInfo.getLinkmanCertNo());
                        aeaUnitProjLinkman.setUnitProjId(id.getUnitProjId());
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
                    aeaProjDrawing.setLinkmen(linkmen);

                    drawings.add(aeaProjDrawing);
                }
            }
        }

        return drawings;
    }


    @Override
    public void saveAeaProjDrawing(AeaProjDrawingVo aeaProjDrawingVo) throws Exception {
        List<AeaProjDrawing> drawings = aeaProjDrawingVo.getAeaProjDrawing();

        for (AeaProjDrawing aeaProjDrawing : drawings) {
            AeaUnitProj aeaUnitProj = new AeaUnitProj();
            aeaUnitProj.setUnitInfoId(aeaProjDrawing.getUnitInfoId());
            aeaUnitProj.setLinkmanInfoId(aeaProjDrawing.getLinkmanInfoId());
            aeaUnitProj.setProjInfoId(aeaProjDrawingVo.getProjInfoId());
            aeaUnitProj.setIsOwner("0");
            aeaUnitProj.setUnitType(aeaProjDrawing.getUnitType());
            aeaUnitProj.setLinkmanInfoId(aeaProjDrawing.getLinkmanInfoId());
            aeaUnitProj.setCreater(SecurityContext.getCurrentUser().getUserName());
            aeaUnitProj.setCreateTime(new Date());
            aeaUnitProj.setUnitProjId(aeaProjDrawing.getUnitProjId());
            aeaUnitProj.setIsDeleted("0");
            //新增单位  单位联系人关联表
            if (aeaProjDrawing.getUnitInfoId() == null || aeaProjDrawing.getUnitInfoId().equals("")) {
                AeaUnitInfo info = new AeaUnitInfo();
                info.setUnitInfoId(UUID.randomUUID().toString());
                info.setApplicant(aeaProjDrawing.getApplicant());
                info.setCreater(SecurityContext.getCurrentUser().getUserName());
                info.setCreateTime(new Date());
                info.setOrganizationalCode(aeaProjDrawing.getOrganizationalCode());
                info.setUnifiedSocialCreditCode(aeaProjDrawing.getUnifiedSocialCreditCode());
                aeaUnitInfoMapper.insertAeaUnitInfo(info);
                aeaUnitProj.setUnitInfoId(info.getUnitInfoId());

                // 保存单位与联系人关联
                String linkmanInfoIds = aeaProjDrawing.getLinkManInfoIds();
                if (StringUtils.isNotBlank(linkmanInfoIds)) {
                    for (String linkmanInfoId : linkmanInfoIds.split(",")) {
                        AeaUnitLinkman aeaUnitLinkman = new AeaUnitLinkman();
                        aeaUnitLinkman.setUnitLinkmanId(UUID.randomUUID().toString());
                        aeaUnitLinkman.setUnitInfoId(info.getUnitInfoId());
                        aeaUnitLinkman.setLinkmanInfoId(linkmanInfoId);
                        aeaUnitLinkman.setCreater(SecurityContext.getCurrentUserId());
                        aeaUnitLinkman.setCreateTime(new Date());
                        aeaUnitLinkmanMapper.insertAeaUnitLinkman(aeaUnitLinkman);
                    }
                }

            } else {
                AeaUnitInfo info = new AeaUnitInfo();
                info.setUnitInfoId(aeaProjDrawing.getUnitInfoId());
                info.setApplicant(aeaProjDrawing.getApplicant());
                info.setModifier(SecurityContext.getCurrentUser().getUserName());
                info.setModifyTime(new Date());
                info.setOrganizationalCode(aeaProjDrawing.getOrganizationalCode());
                info.setUnifiedSocialCreditCode(aeaProjDrawing.getUnifiedSocialCreditCode());
                aeaUnitInfoMapper.updateAeaUnitInfo(info);
            }
            //新增或修改单位项目表
            if (aeaProjDrawing.getUnitProjId() == null || aeaProjDrawing.getUnitProjId().equals("")) {
                aeaUnitProj.setUnitProjId(UUID.randomUUID().toString());
                aeaUnitProjMapper.insertAeaUnitProj(aeaUnitProj);
            } else {
                try {
                    aeaUnitProjMapper.updateAeaUnitProj(aeaUnitProj);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            List<AeaUnitProjLinkman> linkmen = aeaProjDrawing.getLinkmen();
            String unitProjId;
            if (aeaProjDrawing.getUnitProjId() == null || "".equals(aeaProjDrawing.getUnitProjId())) {
                unitProjId = aeaUnitProj.getUnitProjId();
            } else {
                unitProjId = aeaProjDrawing.getUnitProjId();
            }


            String unitInfoId = aeaUnitProj.getUnitInfoId();
            String projInfoId = aeaProjDrawingVo.getProjInfoId();
            String unitType = aeaUnitProj.getUnitType();
            List<AeaUnitProj> unitProjs = aeaUnitProjMapper.findUnitProjByProjIdAndUnitIdAndunitType(projInfoId, unitInfoId, unitType);

            if (unitProjs.size() > 0) {
                AeaUnitProj unitProj = unitProjs.get(0);
                String unitProjId1 = unitProj.getUnitProjId();
                //先删除所有 单位项目联系人
                aeaUnitProjLinkmanMapper.deleteAllByUnitProjId(unitProjId1, SecurityContext.getCurrentUserName());

                //再新增
                if (linkmen != null && linkmen.size() > 0) {
                    for (AeaUnitProjLinkman vo : linkmen) {
                        if (StringUtils.isNotBlank(vo.getLinkmanInfoId())) {
                            AeaUnitProjLinkman man = new AeaUnitProjLinkman(unitProjId, vo.getLinkmanInfoId(), vo.getLinkmanType());
                            man.setProfessionCertType(vo.getProfessionCertType());
                            man.setProfessionSealNum(vo.getProfessionSealNum());
                            man.setLinkmanInfoId(vo.getLinkmanInfoId());
                            if (vo.getUnitProjId() == null || "".equals(vo.getUnitProjId())) {
                                man.setUnitProjId(unitProjId);
                            } else {
                                man.setUnitProjId(vo.getUnitProjId());
                            }

                            man.setTitleGrade(vo.getTitleGrade());
                            man.setTitleCertNum(vo.getTitleCertNum());
                            man.setRegisterNum(vo.getRegisterNum());
                            man.setSafeLicenceNum(vo.getSafeLicenceNum());
                            man.setPrjSpty(vo.getPrjSpty());
                            man.setCreater(SecurityContext.getCurrentUserName());
                            man.setCreateTime(new Date());
                            man.setLinkmanType(vo.getLinkmanType());
                            aeaUnitProjLinkmanMapper.insertAeaUnitProjLinkman(man);
                        }
                    }
                }
            }
            AeaUnitProjLinkman man = new AeaUnitProjLinkman();

            // fuzeren负责人信息
            man.setProjLinkmanId(UUID.randomUUID().toString());
            man.setLinkmanInfoId(aeaProjDrawing.getLinkmanInfoId());
            if (aeaProjDrawing.getUnitProjId() == null || "".equals(aeaProjDrawing.getUnitProjId())) {
                man.setUnitProjId(unitProjId);
            } else {
                man.setUnitProjId(aeaProjDrawing.getUnitProjId());
            }

            man.setLinkmanType(aeaProjDrawing.getLinkmanType());
            man.setProfessionCertType(aeaProjDrawing.getProfessionCertType());
            man.setProfessionSealNum(aeaProjDrawing.getProfessionSealNum());
            man.setTitleGrade(aeaProjDrawing.getTitleGrade());
            man.setTitleCertNum(aeaProjDrawing.getTitleCertNum());
            man.setPrjSpty(aeaProjDrawing.getPrjSpty());
            man.setCreater(SecurityContext.getCurrentUserName());
            man.setCreateTime(new Date());

            aeaUnitProjLinkmanMapper.insertAeaUnitProjLinkman(man);

            //


        }
    }

}
