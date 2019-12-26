package com.augurit.aplanmis.common.form.service.impl;

import com.augurit.agcloud.bpm.admin.sto.service.impl.AbstractFormDataOptManager;
import com.augurit.agcloud.bpm.common.constant.EDataOpt;
import com.augurit.agcloud.bpm.common.domain.ActStoForm;
import com.augurit.agcloud.bpm.common.domain.ActStoForminst;
import com.augurit.agcloud.bpm.common.domain.vo.FormDataOptResult;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.UnitProjLinkmanType;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.form.service.AeaExProjBidService;
import com.augurit.aplanmis.common.form.vo.AeaExProjBidVo;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 招投标信息-Service服务接口实现类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:刘赵雄</li>
 * <li>创建时间：2019-10-31 15:56:12</li>
 * </ul>
 */
@Service
@Transactional
@Slf4j
public class AeaExProjBidServiceImpl extends AbstractFormDataOptManager implements AeaExProjBidService {

    @Autowired
    private AeaUnitInfoService aeaUnitInfoService;
    @Autowired
    private AeaExProjBidMapper aeaExProjBidMapper;
    @Autowired
    private AeaProjInfoMapper aeaProjInfoMapper;
    @Autowired
    private AeaUnitProjMapper aeaUnitProjMapper;
    @Autowired
    private AeaUnitProjLinkmanMapper aeaUnitProjLinkmanMapper;
    @Autowired
    private AeaUnitLinkmanMapper aeaUnitLinkmanMapper;

    public void saveAeaExProjBid(AeaExProjBid aeaExProjBid) throws Exception {
        aeaExProjBid.setCreater(SecurityContext.getCurrentUserName());
        aeaExProjBid.setCreateTime(new Date());
        aeaExProjBid.setRootOrgId(SecurityContext.getCurrentUserId());
        aeaExProjBidMapper.insertAeaExProjBid(aeaExProjBid);

        if (StringUtils.isBlank(aeaExProjBid.getFormId())) throw new Exception("缺少formId");
        this.formSave(aeaExProjBid.getFormId(), aeaExProjBid.getBidId(), EDataOpt.INSERT.getOpareteType(), null);
    }

    public void updateAeaExProjBid(AeaExProjBid aeaExProjBid) throws Exception {
        aeaExProjBid.setModifier(SecurityContext.getCurrentUserId());
        aeaExProjBid.setModifyTime(new Date());
        aeaExProjBidMapper.updateAeaExProjBid(aeaExProjBid);
    }

    public AeaExProjBid getAeaExProjBidByProjId(String projId) throws Exception {
        if (projId == null) {
            throw new InvalidParameterException(projId);
        }
        log.debug("根据ID获取Form对象，ID为：{}", projId);
        return aeaExProjBidMapper.getAeaExProjBidByProjId(projId);
    }

    @Override
    public AeaProjInfo getProjInfoByProjId(String projId) {
        if (projId == null) {
            throw new InvalidParameterException(projId);
        }
        log.debug("根据ID获取Form对象，ID为：{}", projId);
        return aeaProjInfoMapper.getAeaProjInfoById(projId);
    }


    /**
     * 根据项目id和类型查找单位信息
     *
     * @param projInfoId
     * @param unitType
     * @return
     */
    @Override
    public List<AeaUnitInfo> findUnitProjByProjInfoIdAndType(String projInfoId, String unitType) {
        log.debug("根据类型查询项目单位，projInfoId：{}，unitType：{}", projInfoId, unitType);
        return aeaExProjBidMapper.findUnitProjByProjInfoIdAndType(projInfoId, unitType, SecurityContext.getCurrentOrgId());
    }


    @Override
    public int updateAeaUnitProj(AeaUnitProj aeaUnitProj) throws Exception {
        return aeaUnitProjMapper.updateAeaUnitProj(aeaUnitProj);
    }

    /**
     * 更新项目单位关联表
     *
     * @param projId
     * @param unitId
     * @param unitProjId
     * @param unitType
     * @throws Exception
     */
    @Override
    public void updateUnitProjInfo(String projId, String unitId, String unitProjId, String unitType) throws Exception {
        AeaUnitProj aeaUnitProj = new AeaUnitProj();
        aeaUnitProj.setProjInfoId(projId);
        aeaUnitProj.setUnitInfoId(unitId);
        aeaUnitProj.setUnitProjId(unitProjId);
        aeaUnitProj.setUnitType(unitType);
        aeaUnitProj.setIsOwner("0");
        this.updateAeaUnitProj(aeaUnitProj);
    }

    /**
     * 删除项目单位关联表
     *
     * @param projId
     * @param unitTypes
     * @throws Exception
     */
    @Override
    public void delUnitProjInfo(String projId, List<String> unitTypes, String isOwner) throws Exception {
        aeaExProjBidMapper.batchDeleteUnitProjByType(projId, unitTypes, isOwner);
    }

    @Override
    public void saveOrUpdateUnitInfo(AeaExProjBidVo aeaExProjBidVo, List<AeaUnitInfo> aeaUnitInfos, String unitType) throws Exception {
        if (aeaUnitInfos != null) {
            for (AeaUnitInfo aeaUnitInfo : aeaUnitInfos) {
                if (aeaUnitInfo.getUnitInfoId() != null && !"".equals(aeaUnitInfo.getUnitInfoId())) {
                    aeaUnitInfo.setUnitType(null);//单位表里面的类型不更改，以关联表的为准
                    aeaUnitInfoService.updateAeaUnitInfo(aeaUnitInfo);
                    aeaUnitInfo.setUnitType(unitType);//做回显
                    //如果本身有关联表记录则更新，否则重新保存项目单位关联表信息
                    AeaUnitProj aeaUnitProj = new AeaUnitProj();
                    if (StringUtils.isNotBlank(aeaUnitInfo.getUnitProjId())) {
                        this.updateUnitProjInfo(aeaExProjBidVo.getProjInfoId(), aeaUnitInfo.getUnitInfoId(), aeaUnitInfo.getUnitProjId(), unitType);
                    } else {
                        aeaUnitProj = aeaExProjBidVo.toAeaUnitProj(aeaUnitInfo.getUnitInfoId(), unitType);
                        aeaUnitInfo.setUnitProjId(aeaUnitProj.getUnitProjId());//做回显
                        aeaUnitInfoService.insertAeaUnitProj(aeaUnitProj);
                    }
                    if (StringUtils.isNotBlank(aeaUnitInfo.getProjectLeaderId())) {
                        //负责人
                        if (StringUtils.isNotBlank(aeaUnitInfo.getProjLinkmanId())) {
                            AeaUnitProjLinkman man = this.getUnitProjLinkman(aeaUnitInfo, aeaUnitInfo.getProjLinkmanId(), "u");
                            aeaUnitProjLinkmanMapper.updateAeaUnitProjLinkman(man);
                        } else {
                            AeaUnitProjLinkman man = this.getUnitProjLinkman(aeaUnitInfo, UUID.randomUUID().toString(), "c");
                            aeaUnitProjLinkmanMapper.insertAeaUnitProjLinkman(man);
                        }
                    }
                } else {
                    aeaUnitInfo.setUnitInfoId(UUID.randomUUID().toString());
                    aeaUnitInfo.setUnitType(null);//单位表里面的类型不保存，以关联表的为准
                    aeaUnitInfoService.insertAeaUnitInfo(aeaUnitInfo);
                    aeaUnitInfo.setUnitType(unitType);
                    AeaUnitProj aeaUnitProj = aeaExProjBidVo.toAeaUnitProj(aeaUnitInfo.getUnitInfoId(), unitType);
                    aeaUnitInfo.setUnitProjId(aeaUnitProj.getUnitProjId());
                    aeaUnitInfoService.insertAeaUnitProj(aeaUnitProj);

                    // 负责人信息
                    if (StringUtils.isNotBlank(aeaUnitInfo.getProjectLeaderId())) {
                        AeaUnitProjLinkman man = this.getUnitProjLinkman(aeaUnitInfo, UUID.randomUUID().toString(), "c");
                        aeaUnitProjLinkmanMapper.insertAeaUnitProjLinkman(man);
                    }

                    // 保存单位与联系人关联
                    String linkmanInfoIds = aeaExProjBidVo.getWinBidLinkManInfoids();
                    if (StringUtils.isNotBlank(linkmanInfoIds)) {
                        for (String linkmanInfoId : linkmanInfoIds.split(",")) {
                            if (StringUtils.isBlank(linkmanInfoId)) {
                                continue;
                            }

                            AeaUnitLinkman aeaUnitLinkman = new AeaUnitLinkman();
                            aeaUnitLinkman.setUnitLinkmanId(UUID.randomUUID().toString());
                            aeaUnitLinkman.setUnitInfoId(aeaUnitInfo.getUnitInfoId());
                            aeaUnitLinkman.setLinkmanInfoId(linkmanInfoId);
                            aeaUnitLinkman.setCreater(SecurityContext.getCurrentUserId());
                            aeaUnitLinkman.setCreateTime(new Date());
                            aeaUnitLinkmanMapper.insertAeaUnitLinkman(aeaUnitLinkman);
                        }
                    }
                }
            }
        }
    }

    /**
     * @param aeaUnitInfo   单位信息
     * @param projLinkmanId 主键取值
     * @param type          c创建，u更新
     * @return
     */
    private AeaUnitProjLinkman getUnitProjLinkman(AeaUnitInfo aeaUnitInfo, String projLinkmanId, String type) {
        AeaUnitProjLinkman man = new AeaUnitProjLinkman();
        man.setProjLinkmanId(projLinkmanId);
        man.setLinkmanInfoId(aeaUnitInfo.getProjectLeaderId());
        man.setUnitProjId(aeaUnitInfo.getUnitProjId());
        man.setLinkmanType(UnitProjLinkmanType.FZR.getValue());
        if ("c".equals(type)) {
            man.setCreater(SecurityContext.getCurrentUserId());
            man.setCreateTime(new Date());
        } else {
            man.setModifier(SecurityContext.getCurrentUserId());
            man.setModifyTime(new Date());
        }
        man.setIsDeleted("0");
        return man;
    }

    @Override
    public FormDataOptResult doformSave(String formId, String metaTableId, Integer opType, Object dataEntity) throws Exception {
        FormDataOptResult result = new FormDataOptResult();
        result.setSuccess(true);
        ActStoForminst actStoForminst = new ActStoForminst();
        actStoForminst.setFormId(formId);
        actStoForminst.setFormPrimaryKey(metaTableId);
        result.setActStoForminst(actStoForminst);
        result.setDataOpt(EDataOpt.INSERT);
        return result;
    }

    @Override
    public FormDataOptResult doformDelete(ActStoForm formVo, Object dataEntity) throws Exception {
        return null;
    }
}

