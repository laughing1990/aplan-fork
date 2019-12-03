package com.augurit.aplanmis.front.subject.unit.service;

import com.alibaba.fastjson.JSON;
import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.domain.AeaUnitProj;
import com.augurit.aplanmis.common.domain.AeaUnitProjLinkman;
import com.augurit.aplanmis.common.mapper.AeaUnitLinkmanMapper;
import com.augurit.aplanmis.common.mapper.AeaUnitProjLinkmanMapper;
import com.augurit.aplanmis.common.mapper.AeaUnitProjMapper;
import com.augurit.aplanmis.common.service.linkman.AeaLinkmanInfoService;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.augurit.aplanmis.front.subject.unit.vo.AeaLinkmanType;
import com.augurit.aplanmis.front.subject.unit.vo.UnitAddVo;
import com.augurit.aplanmis.front.subject.unit.vo.UnitEditVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class RestUnitService {

    @Autowired
    private AeaUnitInfoService aeaUnitInfoService;
    @Autowired
    private AeaLinkmanInfoService aeaLinkmanInfoService;
    @Autowired
    private AeaUnitLinkmanMapper aeaUnitLinkmanMapper;
    @Autowired
    private AeaUnitProjMapper aeaUnitProjMapper;
    @Autowired
    private AeaUnitProjLinkmanMapper aeaUnitProjLinkmanMapper;

    public String save(UnitAddVo unitAddVo) throws Exception {
        //20190826 xiaohutu 可能是同时新增单位和联系人,联系人ID不存在
        // 新增企业单位
        AeaUnitInfo aeaUnitInfo = unitAddVo.toAeaUnitInfo();
        aeaUnitInfo.setUnitInfoId(UUID.randomUUID().toString());
        aeaUnitInfoService.insertAeaUnitInfo(aeaUnitInfo);

        // 更新联系人
        if (StringUtils.isEmpty(unitAddVo.getLinkmanInfoId())) {
            AeaLinkmanInfo linkmanInfo = new AeaLinkmanInfo();
            BeanUtils.copyProperties(unitAddVo, linkmanInfo);
            linkmanInfo.setLinkmanInfoId(UUID.randomUUID().toString());
            aeaLinkmanInfoService.insertAeaLinkmanInfo(linkmanInfo);
            unitAddVo.setLinkmanInfoId(linkmanInfo.getLinkmanInfoId());
        } else {
            AeaLinkmanInfo aeaLinkmanInfo = aeaLinkmanInfoService.getAeaLinkmanInfoByLinkmanInfoId(unitAddVo.getLinkmanInfoId());
            if (aeaLinkmanInfo == null) {
                throw new RuntimeException("AeaLinkmanInfo not found. linkmanInfoId: " + unitAddVo.getLinkmanInfoId());
            }
            aeaLinkmanInfoService.updateAeaLinkmanInfo(unitAddVo.mergeAeaLinkmanInfo(aeaLinkmanInfo));
        }

        // 企业单位与联系人关联
        aeaUnitLinkmanMapper.insertAeaUnitLinkman(unitAddVo.toAeaUnitLinkman(aeaUnitInfo.getUnitInfoId(), SecurityContext.getCurrentUserId()));

        // 企业单位与项目关联
        List<AeaUnitProj> aeaUnitProjs = unitAddVo.toAeaUnitProjs(aeaUnitInfo.getUnitInfoId(), aeaUnitInfo.getUnitType(), SecurityContext.getCurrentUserId());
        aeaUnitProjMapper.batchInsertAeaUnitProj(aeaUnitProjs);

        //插入项目单位联系人类型表---保存的时候不需要判断是否存在
        String linkmanType = unitAddVo.getLinkmanType();
        if (StringUtils.isNotBlank(linkmanType)) {
            List<AeaLinkmanType> linkmanTypes = JSON.parseArray(linkmanType, AeaLinkmanType.class);
            if (linkmanTypes.size() == 0) {
                return aeaUnitInfo.getUnitInfoId();
            }
            for (AeaUnitProj unitProj : aeaUnitProjs) {
                String unitProjId = unitProj.getUnitProjId();
                for (AeaLinkmanType vo : linkmanTypes) {
                    if (StringUtils.isNotBlank(vo.getLinkmanInfoId())) {
                        AeaUnitProjLinkman man = new AeaUnitProjLinkman(unitProjId, vo.getLinkmanInfoId(), vo.getLinkmanType());
                        man.setCreater(SecurityContext.getCurrentUserName());
                        man.setCreateTime(new Date());
                        aeaUnitProjLinkmanMapper.insertAeaUnitProjLinkman(man);
                    }

                }
            }
        }
        return aeaUnitInfo.getUnitInfoId();
    }

    public void updateUnitProj(String projInfoId, String unitInfoId, String linkmanInfoId, String unitType, String isOwner) {
        Assert.isTrue(StringUtils.isNotBlank(projInfoId), "projInfoId is null");
        Assert.isTrue(StringUtils.isNotBlank(unitInfoId), "unitInfoId is null");

        AeaUnitProj aeaUnitProj = new AeaUnitProj();
        aeaUnitProj.setProjInfoId(projInfoId);
        aeaUnitProj.setUnitInfoId(unitInfoId);
        try {
            List<AeaUnitProj> aeaUnitProjs = aeaUnitProjMapper.listAeaUnitProj(aeaUnitProj);
            if (CollectionUtils.isNotEmpty(aeaUnitProjs)) {
                aeaUnitProj = aeaUnitProjs.get(0);
                aeaUnitProj.setLinkmanInfoId(linkmanInfoId);
                aeaUnitProj.setUnitType(unitType);
                aeaUnitProjMapper.updateAeaUnitProj(aeaUnitProj);
            } else {
                aeaUnitProj.setUnitProjId(UuidUtil.generateUuid());
                aeaUnitProj.setUnitType(unitType);
                aeaUnitProj.setIsDeleted(Status.OFF);
                aeaUnitProj.setIsOwner(StringUtils.isBlank(isOwner) ? Status.OFF : isOwner);
                aeaUnitProj.setLinkmanInfoId(linkmanInfoId);
                aeaUnitProj.setCreater(SecurityContext.getCurrentUserId());
                aeaUnitProj.setCreateTime(new Date());
                aeaUnitProjMapper.insertAeaUnitProj(aeaUnitProj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 更新项目单位联系人类型表
     * 先删除，在重新插入
     *
     * @param unitEditVo
     */
    public void updateUnitProjLinkman(UnitEditVo unitEditVo) {
//        if (null != unitEditVo && null != unitEditVo.getLinkmanTypes() && !unitEditVo.getLinkmanTypes().isEmpty()) {
        if (null != unitEditVo && null != unitEditVo.getLinkmanType()) {
            String linkmanType = unitEditVo.getLinkmanType();

            List<AeaLinkmanType> linkmanTypes = JSON.parseArray(linkmanType, AeaLinkmanType.class);
            if (linkmanTypes.size() == 0) {
                return;
            }
            String unitInfoId = unitEditVo.getUnitInfoId();
            String projInfoId = unitEditVo.getProjInfoId();
            String unitType = unitEditVo.getUnitType();
            List<AeaUnitProj> unitProjs = aeaUnitProjMapper.findUnitProjByProjIdAndUnitIdAndunitType(projInfoId, unitInfoId, unitType);
            if (unitProjs.size() > 0) {
                AeaUnitProj unitProj = unitProjs.get(0);
                String unitProjId = unitProj.getUnitProjId();
                //先删除所有
                aeaUnitProjLinkmanMapper.deleteAllByUnitProjId(unitProjId, SecurityContext.getCurrentUserName());
                //在新增
                for (AeaLinkmanType vo : linkmanTypes) {
                    if (StringUtils.isNotBlank(vo.getLinkmanInfoId())) {
                        AeaUnitProjLinkman man = new AeaUnitProjLinkman(unitProjId, vo.getLinkmanInfoId(), vo.getLinkmanType());
                        man.setCreateTime(new Date());
                        man.setCreater(SecurityContext.getCurrentUserName());
                        aeaUnitProjLinkmanMapper.insertAeaUnitProjLinkman(man);
                    }

                }
            }

        }
    }

    public void deleteUnitByLinkmanInfoId(String linkmanInfoId, String unitInfoId) {
        if (StringUtils.isNotBlank(linkmanInfoId) && StringUtils.isNotBlank(unitInfoId)) {
            aeaUnitLinkmanMapper.deleteUnitLinkman(unitInfoId, new String[]{linkmanInfoId});
        }
    }
}
