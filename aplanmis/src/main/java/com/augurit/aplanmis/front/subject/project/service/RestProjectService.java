package com.augurit.aplanmis.front.subject.project.service;

import com.augurit.agcloud.bsc.domain.BscDicRegion;
import com.augurit.agcloud.bsc.mapper.BscDicRegionMapper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.AeaLinkmanInfoMapper;
import com.augurit.aplanmis.common.mapper.AeaProjLinkmanMapper;
import com.augurit.aplanmis.common.mapper.AeaUnitProjLinkmanMapper;
import com.augurit.aplanmis.common.service.linkman.AeaLinkmanInfoService;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.service.theme.AeaParThemeService;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.augurit.aplanmis.front.subject.project.vo.ChildProjectAddVo;
import com.augurit.aplanmis.front.subject.project.vo.ProjectApplySubjectApplicantVo;
import com.augurit.aplanmis.front.subject.project.vo.ProjectApplySubjectEnterpriseVo;
import com.augurit.aplanmis.front.subject.project.vo.ProjectDetailVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class RestProjectService {

    @Autowired
    private AeaProjInfoService aeaProjInfoService;
    @Autowired
    private AeaProjLinkmanMapper aeaProjLinkmanMapper;
    @Autowired
    private AeaLinkmanInfoService aeaLinkmanInfoService;
    @Autowired
    private AeaUnitInfoService aeaUnitInfoService;
    @Autowired
    private AeaLinkmanInfoMapper aeaLinkmanInfoMapper;
    @Autowired
    private AeaParThemeService aeaParThemeService;
    @Autowired
    private BscDicRegionMapper bscDicRegionMapper;
    @Autowired
    private AeaUnitProjLinkmanMapper aeaUnitProjLinkmanMapper;

    /**
     * 查询项目的详细信息
     *
     * @param projInfoId 项目id
     * @return ProjectDetailVo
     */
    public ProjectDetailVo getProjectDetail(String projInfoId) throws Exception {
        AeaProjInfo aeaProjInfo = aeaProjInfoService.getAeaProjInfoByProjInfoId(projInfoId);
        if (aeaProjInfo == null) {
            return null;
        }
        if (StringUtils.isNotBlank(aeaProjInfo.getRegionalism())) {
            BscDicRegion region = bscDicRegionMapper.getBscDicRegionById(aeaProjInfo.getRegionalism());
            aeaProjInfo.setRegionName(Optional.ofNullable(region).orElse(new BscDicRegion()).getRegionName());
        }
        ProjectDetailVo projectDetail = ProjectDetailVo.from(aeaProjInfo);
        if (StringUtils.isNotBlank(aeaProjInfo.getThemeId())) {
            AeaParTheme theme = aeaParThemeService.getAeaParThemeByThemeId(aeaProjInfo.getThemeId());
            projectDetail.setProjApplyType(Optional.ofNullable(theme).orElse(new AeaParTheme()).getThemeName());
        }
        // 查询项目是否存在个人申报主体
        ProjectApplySubjectApplicantVo applicant = getApplicant(projInfoId);
        if (applicant != null && StringUtils.isNotBlank(applicant.getApplyLinkmanId())) {
            projectDetail.setApplySubjectType("0");
        } else {
            projectDetail.setApplySubjectType("1");
        }
        projectDetail.setPersonalApplicant(applicant);

        // 查询建设单位
        projectDetail.setBuildUnits(aeaUnitInfoService.findOwerUnitProj(projInfoId).stream()
                .map(unit -> {
                    List<AeaLinkmanInfo> allUnitLinkman = aeaLinkmanInfoService.findAllUnitLinkman(unit.getUnitInfoId());
                    if (allUnitLinkman.size() > 0) {
                        AeaLinkmanInfo aeaLinkmanInfo = allUnitLinkman.get(0);
                        ProjectApplySubjectEnterpriseVo from = ProjectApplySubjectEnterpriseVo.from(aeaLinkmanInfo, unit, "1");
                        // 灵玲要求建设单位这里写死
                        from.setUnitType("1");
                        from.setUnitTypeName("建设单位");
                        //20191018 查询联系人类型
                        List<AeaUnitProjLinkman> linkmanList = aeaUnitProjLinkmanMapper.queryByUnitProjIdAndlinkType(unit.getUnitProjId(), null, null);
                        from.changeListToVo(linkmanList, from);
                        return from;
                    }
                    return null;
                }).filter(Objects::nonNull).collect(Collectors.toList())
        );
        // 经办单位
        projectDetail.setAgentUnits(aeaUnitInfoService.findNonOwerUnitProj(projInfoId).stream()
                .map(unit -> {
                    List<AeaLinkmanInfo> allUnitLinkman = aeaLinkmanInfoService.findAllUnitLinkman(unit.getUnitInfoId());
                    if (allUnitLinkman.size() > 0) {
                        AeaLinkmanInfo aeaLinkmanInfo = allUnitLinkman.get(0);
                        ProjectApplySubjectEnterpriseVo from = ProjectApplySubjectEnterpriseVo.from(aeaLinkmanInfo, unit, "0");
                        //20191018 查询联系人类型
                        List<AeaUnitProjLinkman> linkmanList = aeaUnitProjLinkmanMapper.queryByUnitProjIdAndlinkType(unit.getUnitProjId(), null, null);
                        from.changeListToVo(linkmanList, from);
                        return from;
                    }
                    return null;
                }).filter(Objects::nonNull).collect(Collectors.toList())
        );
        try {
            AeaParTheme aeaParTheme = aeaParThemeService.getAeaParThemeByProjIdorProjCode(projInfoId, null);
            if (aeaParTheme != null) {
                projectDetail.setThemeId(aeaParTheme.getThemeId());
                projectDetail.setThemeType(aeaParTheme.getThemeType());
                // 立项类型
                projectDetail.setProjApplyType(aeaParTheme.getThemeName());
                projectDetail.setThemeVerId(aeaProjInfo.getThemeVerId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Query themeId and themeType error, projInfoId: " + projInfoId, e);
        }

        return projectDetail;
    }

    private ProjectApplySubjectApplicantVo getApplicant(String projInfoId) {
        AeaLinkmanInfo applicant = null;
        AeaLinkmanInfo contact = null;
        AeaProjLinkman aeaProjLinkman = new AeaProjLinkman();
        aeaProjLinkman.setProjInfoId(projInfoId);
        List<AeaProjLinkman> aeaProjLinkmens;
        try {
            aeaProjLinkmens = aeaProjLinkmanMapper.listAeaProjLinkman(aeaProjLinkman);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("查询个人申报主体信息错误" + projInfoId, e);
        }
        for (AeaProjLinkman projLinkman : aeaProjLinkmens) {
            if ("apply".equals(projLinkman.getType()) && applicant == null) {
                applicant = aeaLinkmanInfoMapper.getAeaLinkmanInfoById(projLinkman.getLinkmanInfoId());
            } else if ("link".equals(projLinkman.getType()) && contact == null) {
                contact = aeaLinkmanInfoMapper.getAeaLinkmanInfoById(projLinkman.getLinkmanInfoId());
            }
            if (applicant != null && contact != null) {
                break;
            }
        }
        return ProjectApplySubjectApplicantVo.from(applicant, contact);
    }

    public AeaProjInfo addChildProject(ChildProjectAddVo childProjectAddVo) throws Exception {
        Assert.isTrue(StringUtils.isNotBlank(childProjectAddVo.getParentProjInfoId()), "parentProjInfoId is null");
        Assert.isTrue(StringUtils.isNotBlank(childProjectAddVo.getProjName()), "projName is null");
        AeaProjInfo aeaProjInfo = new AeaProjInfo();
        aeaProjInfo.setParentProjId(childProjectAddVo.getParentProjInfoId());
        aeaProjInfo.setProjName(childProjectAddVo.getProjName());
        aeaProjInfo.setForeignRemark(childProjectAddVo.getForeignRemark());
        aeaProjInfo.setStageFlag(childProjectAddVo.getStageFlag());
        return aeaProjInfoService.addChildProjInfo(aeaProjInfo, childProjectAddVo.getIsSecond());
    }
}
