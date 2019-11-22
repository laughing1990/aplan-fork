package com.augurit.aplanmis.mall.userCenter.service;

import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaProjLinkman;
import com.augurit.aplanmis.common.domain.AeaUnitLinkman;
import com.augurit.aplanmis.common.mapper.AeaProjLinkmanMapper;
import com.augurit.aplanmis.common.mapper.AeaUnitLinkmanMapper;
import com.augurit.aplanmis.common.service.linkman.AeaLinkmanInfoService;

import com.augurit.aplanmis.mall.userCenter.vo.LinkmanAddVo;
import com.augurit.aplanmis.mall.userCenter.vo.PersonalInfoVo;
import com.augurit.aplanmis.mall.userCenter.vo.PersonalResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class RestLinkmanService {
    @Autowired
    private AeaLinkmanInfoService aeaLinkmanInfoService;
    @Autowired
    private AeaUnitLinkmanMapper aeaUnitLinkmanMapper;
    @Autowired
    private AeaProjLinkmanMapper aeaProjLinkmanMapper;

    public String save(LinkmanAddVo linkmanAddVo) {
        AeaLinkmanInfo aeaLinkmanInfo = linkmanAddVo.toAeaLinkmanInfo();
        aeaLinkmanInfoService.insertAeaLinkmanInfo(aeaLinkmanInfo);
        // 企业单位关联
        if (StringUtils.isNotBlank(linkmanAddVo.getUnitInfoId())) {
            insertAeaUnitLinkman(aeaLinkmanInfo.getLinkmanInfoId(), linkmanAddVo.getUnitInfoId());
        }
        // 项目关联
        if (StringUtils.isNotBlank(linkmanAddVo.getProjInfoId())) {
            insertAeaProjLinkman(aeaLinkmanInfo.getLinkmanInfoId(), linkmanAddVo.getProjInfoId());
        }
        return aeaLinkmanInfo.getLinkmanInfoId();
    }

    private void insertAeaProjLinkman(String linkmanInfoId, String projInfoId) {
        aeaProjLinkmanMapper.insertAeaProjLinkman(buildAeaProjLinkman(linkmanInfoId, projInfoId));
    }

    private AeaProjLinkman buildAeaProjLinkman(String linkmanInfoId, String projInfoId) {
        AeaProjLinkman aeaProjLinkman = new AeaProjLinkman();
        aeaProjLinkman.setProjLinkmanId(UuidUtil.generateUuid());
        aeaProjLinkman.setProjInfoId(projInfoId);
        aeaProjLinkman.setLinkmanInfoId(linkmanInfoId);
        aeaProjLinkman.setType("link");
        aeaProjLinkman.setCreater(SecurityContext.getCurrentUserId());
        aeaProjLinkman.setCreateTime(new Date());
        return aeaProjLinkman;
    }

    private void insertAeaUnitLinkman(String linkmanInfoId, String unitInfoId) {
        try {
            aeaUnitLinkmanMapper.insertAeaUnitLinkman(buildAeaUnitLinkman(linkmanInfoId, unitInfoId));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Linkman associate unit failed, linkmanInfoId: " + linkmanInfoId + ", unitInfoId: " + unitInfoId, e);
        }
    }

    private AeaUnitLinkman buildAeaUnitLinkman(String linkmanInfoId, String unitInfoId) {
        AeaUnitLinkman aeaUnitLinkman = new AeaUnitLinkman();
        aeaUnitLinkman.setUnitLinkmanId(UuidUtil.generateUuid());
        aeaUnitLinkman.setUnitInfoId(unitInfoId);
        aeaUnitLinkman.setLinkmanInfoId(linkmanInfoId);
        aeaUnitLinkman.setCreater(SecurityContext.getCurrentUserId());
        aeaUnitLinkman.setCreateTime(new Date());
        return aeaUnitLinkman;
    }

    public PersonalResultVo saveOrUpdatePersonalInfo(List<PersonalInfoVo> personalInfos) {
        PersonalResultVo personalResultVo = new PersonalResultVo();
        personalInfos.forEach(p -> {
            String linkmanInfoId;
            if (StringUtils.isNotBlank(p.getLinkmanInfoId())) {
                linkmanInfoId = updatePersonalInfo(p);
                populateLinkmanInfoId(personalResultVo, p.getApplyOrLinkType(), linkmanInfoId);
                saveOrUpdateProjLinkmaninfo(p.getProjInfoId(), p, linkmanInfoId);
            } else {
                List<AeaLinkmanInfo> linkmanInfos = aeaLinkmanInfoService.findLinkmanInfo(p.toQueryParam());
                if (linkmanInfos.size() > 0) {
                    populateLinkmanInfoId(personalResultVo, p.getApplyOrLinkType(), linkmanInfos.get(0).getLinkmanInfoId());
                    saveOrUpdateProjLinkmaninfo(p.getProjInfoId(), p, linkmanInfos.get(0).getLinkmanInfoId());
                    return;
                }
                linkmanInfoId = savePersonalInfo(p);
                saveOrUpdateProjLinkmaninfo(p.getProjInfoId(), p, linkmanInfoId);
            }
            populateLinkmanInfoId(personalResultVo, p.getApplyOrLinkType(), linkmanInfoId);
        });
        return personalResultVo;
    }

    // 保存申请人与项目信息关联
    private void saveOrUpdateProjLinkmaninfo(String projInfoId, PersonalInfoVo p, String linkmanInfoId) {
        if (StringUtils.isNotBlank(projInfoId)) {
            AeaProjLinkman aeaProjLinkman = new AeaProjLinkman();
            aeaProjLinkman.setProjInfoId(projInfoId);
            aeaProjLinkman.setLinkmanInfoId(linkmanInfoId);
            aeaProjLinkman.setType(p.getApplyOrLinkType());
            List<AeaProjLinkman> aeaProjLinkmen = aeaProjLinkmanMapper.listAeaProjLinkman(aeaProjLinkman);
            if (CollectionUtils.isNotEmpty(aeaProjLinkmen)) {
                aeaProjLinkman = aeaProjLinkmen.get(0);
                aeaProjLinkman.setType(p.getApplyOrLinkType());
                aeaProjLinkmanMapper.updateAeaProjLinkman(aeaProjLinkman);
            } else {
                aeaProjLinkman.setType(p.getApplyOrLinkType());
                aeaProjLinkman.setProjLinkmanId(UuidUtil.generateUuid());
                aeaProjLinkman.setCreater(SecurityContext.getCurrentUserId());
                aeaProjLinkman.setCreateTime(new Date());
                aeaProjLinkmanMapper.insertAeaProjLinkman(aeaProjLinkman);
            }
        }
    }

    // 保存
    private String savePersonalInfo(PersonalInfoVo p) {
        AeaLinkmanInfo aeaLinkmanInfo = p.toAeaLinkmanInfo();
        aeaLinkmanInfoService.insertAeaLinkmanInfo(aeaLinkmanInfo);
        return aeaLinkmanInfo.getLinkmanInfoId();
    }

    // 更新
    private String updatePersonalInfo(PersonalInfoVo p) {
        AeaLinkmanInfo aeaLinkmanInfo = aeaLinkmanInfoService.getAeaLinkmanInfoByLinkmanInfoId(p.getLinkmanInfoId());
        p.merge(aeaLinkmanInfo);
        aeaLinkmanInfoService.updateAeaLinkmanInfo(aeaLinkmanInfo);
        return aeaLinkmanInfo.getLinkmanInfoId();
    }

    private void populateLinkmanInfoId(PersonalResultVo personalResultVo, String linkType, String linkmanInfoId) {
        if (PersonalInfoVo.LINK.equals(linkType)) {
            personalResultVo.setLinkmanInfoId(linkmanInfoId);
        } else {
            personalResultVo.setApplyInfoId(linkmanInfoId);
        }
    }

    /**
     * 删除企业联系人, 包含关联表
     * @param linkmanInfoId 联系人id
     * @param unitInfoId 企业id
     */
    public void deleteLinkmanInfoByUnitId(String linkmanInfoId, String unitInfoId) {
        aeaLinkmanInfoService.deleteUnitLinkman(unitInfoId, linkmanInfoId);
        aeaLinkmanInfoService.deleteAeaLinkmanInfo(linkmanInfoId);
    }
}
