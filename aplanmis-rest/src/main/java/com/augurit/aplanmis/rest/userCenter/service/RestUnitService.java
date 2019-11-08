package com.augurit.aplanmis.rest.userCenter.service;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.domain.AeaUnitProj;
import com.augurit.aplanmis.common.mapper.AeaUnitLinkmanMapper;
import com.augurit.aplanmis.common.mapper.AeaUnitProjMapper;
import com.augurit.aplanmis.common.service.linkman.AeaLinkmanInfoService;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.augurit.aplanmis.rest.userCenter.vo.UnitAddVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

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

        return aeaUnitInfo.getUnitInfoId();
    }

    public void updateUnitProj(String projInfoId, String unitInfoId, String linkmanInfoId) {
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
                aeaUnitProjMapper.updateAeaUnitProj(aeaUnitProj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
