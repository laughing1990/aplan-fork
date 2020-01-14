package com.augurit.aplanmis.common.service.unit.impl;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.AeaUnitConstants;
import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author yinlf
 * @Date 2019/7/8
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AeaUnitInfoServiceImpl implements AeaUnitInfoService {

    private static Logger LOGGER = LoggerFactory.getLogger(AeaUnitInfoServiceImpl.class);

    @Autowired
    private AeaUnitInfoMapper aeaUnitInfoMapper;

    @Autowired
    private AeaUnitProjMapper aeaUnitProjMapper;

    @Autowired
    private AeaApplyinstUnitProjMapper aeaApplyinstUnitProjMapper;
    @Autowired
    private AeaApplyinstProjMapper aeaApplyinstProjMapper;
    @Autowired
    private AeaUnitProjLinkmanMapper aeaUnitProjLinkmanMapper;

    @Override
    public void insertAeaUnitInfo(AeaUnitInfo aeaUnitInfo) {
        if (StringUtils.isEmpty(aeaUnitInfo.getUnitInfoId())) aeaUnitInfo.setUnitInfoId(UUID.randomUUID().toString());
        aeaUnitInfo.setCreater(SecurityContext.getCurrentUserId());
        aeaUnitInfo.setCreateTime(new Date());
        aeaUnitInfo.setIsDeleted(Status.OFF);
        LOGGER.debug("新增单位");
        aeaUnitInfo.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaUnitInfoMapper.insertAeaUnitInfo(aeaUnitInfo);
    }

    @Override
    public void updateAeaUnitInfo(AeaUnitInfo aeaUnitInfo) {
        aeaUnitInfo.setModifier(SecurityContext.getCurrentUserId());
        aeaUnitInfo.setModifyTime(new Date());
        LOGGER.debug("修改单位信息");
        aeaUnitInfoMapper.updateAeaUnitInfo(aeaUnitInfo);
    }

    @Override
    public void insertAeaUnitProj(AeaUnitProj aeaUnitProj) {
        aeaUnitProj.setIsDeleted("0");
        aeaUnitProjMapper.insertAeaUnitProj(aeaUnitProj);
    }

    @Override
    public void batchInserAeaUnitProj(List<AeaUnitProj> aeaUnitProjList) {
        aeaUnitProjMapper.batchInsertAeaUnitProj(aeaUnitProjList);
    }

    @Override
    public void deleteAeaUnitInfo(String... unitInfoId) {
        LOGGER.debug("批量删除单位信息");
        aeaUnitInfoMapper.batchDeleteUnitInfo(unitInfoId);
    }

    @Override
    public void deleteAeaUnitInfoCascade(String... unitInfoId) {

    }

    @Override
    public AeaUnitInfo getAeaUnitInfoByUnitInfoId(String unitInfoId) {
        LOGGER.debug("单位ID查询单位信息");
        return aeaUnitInfoMapper.getAeaUnitInfoById(unitInfoId);
    }

    @Override
    public void insertOwnerUnitProj(String projInfoId, String... unitInfoId) {
        LOGGER.debug("项目添加建设单位");
        List<AeaUnitProj> aeaUnitProjList = this.createAeaUnitProjList(projInfoId, AeaUnitConstants.IS_OWNER_TRUE, unitInfoId);
        aeaUnitProjMapper.batchInsertAeaUnitProj(aeaUnitProjList);
    }

    @Override
    public void insertOwnerUnitProj(Map<String, List<String>> unitProjMap) {
        LOGGER.debug("项目添加建设单位");
        List<AeaUnitProj> aeaUnitProjListMutiProj = new ArrayList<>();
        for (String projInfoId : unitProjMap.keySet()) {
            List<String> unitInfoIdList = unitProjMap.get(projInfoId);
            List<AeaUnitProj> aeaUnitProjList = this.createAeaUnitProjList(projInfoId, AeaUnitConstants.IS_OWNER_TRUE, unitInfoIdList.toArray(new String[unitInfoIdList.size()]));
            aeaUnitProjListMutiProj.addAll(aeaUnitProjList);
        }
        aeaUnitProjMapper.batchInsertAeaUnitProj(aeaUnitProjListMutiProj);
    }

    @Override
    public void insertNonOwnerUnitProj(String projInfoId, String... unitInfoId) {
        LOGGER.debug("项目添加经办单位");
        List<AeaUnitProj> aeaUnitProjList = this.createAeaUnitProjList(projInfoId, AeaUnitConstants.IS_OWNER_FLASE, unitInfoId);
        aeaUnitProjMapper.batchInsertAeaUnitProj(aeaUnitProjList);
    }


    @Override
    public void insertNonOwnerUnitProj(List<String> projInfoIds, String... unitInfoId) {
        LOGGER.debug("多项目添加经办单位");
        List<AeaUnitProj> aeaUnitProjListMutiProj = new ArrayList<>();
        for (String projInfoId : projInfoIds) {
            List<AeaUnitProj> aeaUnitProjList = this.createAeaUnitProjList(projInfoId, AeaUnitConstants.IS_OWNER_FLASE, unitInfoId);
            aeaUnitProjListMutiProj.addAll(aeaUnitProjList);
        }
        aeaUnitProjMapper.batchInsertAeaUnitProj(aeaUnitProjListMutiProj);
    }

    private List<AeaUnitProj> createAeaUnitProjList(String projInfoId, String isOwner, String[] unitInfoIds) {
        List<AeaUnitProj> AeaUnitProjList = new ArrayList<>();
        for (String id : unitInfoIds) {
            AeaUnitProj aeaUnitProj = new AeaUnitProj();
            aeaUnitProj.setUnitProjId(UUID.randomUUID().toString());
            aeaUnitProj.setUnitInfoId(id);
            aeaUnitProj.setProjInfoId(projInfoId);
            aeaUnitProj.setCreater(SecurityContext.getCurrentUserId());
            aeaUnitProj.setCreateTime(new Date());
            aeaUnitProj.setIsOwner(isOwner);
            aeaUnitProj.setIsDeleted("0");
            AeaUnitInfo unit = aeaUnitInfoMapper.getAeaUnitInfoById(id);
            if(unit!=null){
                aeaUnitProj.setUnitType(unit.getUnitType());
            }
            AeaUnitProjList.add(aeaUnitProj);
        }
        return AeaUnitProjList;
    }

    @Override
    public void deleteUnitProj(String projInfoId, String isOwner, String... unitInfoId) {
        LOGGER.debug("删除项目单位关联");
        aeaUnitProjMapper.batchDeleteUnitProj(projInfoId, isOwner, unitInfoId);
    }

    @Override
    public void insertApplyOwnerUnitProj(String applyinstId, String projInfoId, String... unitInfoId) {
        LOGGER.debug("新增申报项目建设单位");
        String isOwner = AeaUnitConstants.IS_OWNER_FLASE;
        List<AeaUnitProj> aeaUnitProjListMutiProj = new ArrayList<>();
        List<AeaApplyinstUnitProj> applyinstUnitProjListMutiProj = new ArrayList<>();

        List<AeaUnitProj> aeaUnitProjList = aeaUnitProjMapper.findUnitPorojByProjInfoIdsAndUnitInfoIds(new String[]{projInfoId}, unitInfoId, AeaUnitConstants.IS_OWNER_TRUE);
        //已存在项目与单位关联关系，不要新增关联
        for (AeaUnitProj aeaUnitProj : aeaUnitProjList) {
            AeaApplyinstUnitProj aeaApplyinstUnitProj = this.createAeaApplyinstUnitProj(applyinstId, aeaUnitProj.getUnitProjId());
            applyinstUnitProjListMutiProj.add(aeaApplyinstUnitProj);
        }
        this.createApplyinstUnitProjAndUnitProj(applyinstId, isOwner, aeaUnitProjListMutiProj, applyinstUnitProjListMutiProj, projInfoId, Arrays.asList(unitInfoId), aeaUnitProjList);

        if(aeaUnitProjListMutiProj.size()>0){
            aeaUnitProjMapper.batchInsertAeaUnitProj(aeaUnitProjListMutiProj);
        }
        aeaApplyinstUnitProjMapper.batchInsertAeaApplyinstUnitProjMapper(applyinstUnitProjListMutiProj);
    }

    @Override
    public void insertApplyOwnerUnitProj(String applyinstId, Map<String, List<String>> unitProjMap) {
        LOGGER.debug("新增申报项目建设单位");
        String isOwner = AeaUnitConstants.IS_OWNER_TRUE;
        List<AeaUnitProj> aeaUnitProjListMutiProj = new ArrayList<>();
        List<AeaApplyinstUnitProj> applyinstUnitProjListMutiProj = new ArrayList<>();

        for (String projInfoId : unitProjMap.keySet()) {
            List<String> unitInfoIdList = unitProjMap.get(projInfoId);
            List<AeaUnitProj> aeaUnitProjList = aeaUnitProjMapper.findUnitPorojByProjInfoIdsAndUnitInfoIds(new String[]{projInfoId}, unitInfoIdList.toArray(new String[unitInfoIdList.size()]), isOwner);
            //已存在项目与单位关联关系，不要新增关联
            for (AeaUnitProj aeaUnitProj : aeaUnitProjList) {
                AeaApplyinstUnitProj aeaApplyinstUnitProj = this.createAeaApplyinstUnitProj(applyinstId, aeaUnitProj.getUnitProjId());
                applyinstUnitProjListMutiProj.add(aeaApplyinstUnitProj);
            }
            this.createApplyinstUnitProjAndUnitProj(applyinstId, isOwner, aeaUnitProjListMutiProj, applyinstUnitProjListMutiProj, projInfoId, unitInfoIdList, aeaUnitProjList);
        }
        if(aeaUnitProjListMutiProj.size()>0){
            for (AeaUnitProj aeaUnitProj : aeaUnitProjListMutiProj) {
                aeaUnitProj.setIsDeleted("0");
            }
            aeaUnitProjMapper.batchInsertAeaUnitProj(aeaUnitProjListMutiProj);
        }
        aeaApplyinstUnitProjMapper.batchInsertAeaApplyinstUnitProjMapper(applyinstUnitProjListMutiProj);
    }

    private void createApplyinstUnitProjAndUnitProj(String applyinstId, String isOwner, List<AeaUnitProj> aeaUnitProjListMutiProj, List<AeaApplyinstUnitProj> applyinstUnitProjListMutiProj, String projInfoId, List<String> unitInfoIdList, List<AeaUnitProj> aeaUnitProjList) {
        for (String id : unitInfoIdList) {
            boolean exist = false;
            for (AeaUnitProj aeaUnitProj : aeaUnitProjList) {
                aeaUnitProj.setIsDeleted("0");
                if (aeaUnitProj.getProjInfoId().equals(projInfoId)&&aeaUnitProj.getUnitInfoId().equals(id)) {
                    exist = true;
                }
            }
            //不存在项目与单位关联关系，新增关联
            if(!exist){
                AeaUnitProj newAeaUnitProj = new AeaUnitProj();
                newAeaUnitProj.setUnitProjId(UUID.randomUUID().toString());
                newAeaUnitProj.setUnitInfoId(id);
                newAeaUnitProj.setProjInfoId(projInfoId);
                newAeaUnitProj.setCreater(SecurityContext.getCurrentUserId());
                newAeaUnitProj.setCreateTime(new Date());
                newAeaUnitProj.setIsOwner(isOwner);
                newAeaUnitProj.setIsDeleted("0");
                AeaUnitInfo unit = aeaUnitInfoMapper.getAeaUnitInfoById(id);
                if(unit!=null){
                    newAeaUnitProj.setUnitType(unit.getUnitType());
                }
                aeaUnitProjListMutiProj.add(newAeaUnitProj);
                AeaApplyinstUnitProj aeaApplyinstUnitProj = this.createAeaApplyinstUnitProj(applyinstId, newAeaUnitProj.getUnitProjId());
                applyinstUnitProjListMutiProj.add(aeaApplyinstUnitProj);
            }
        }
    }

    @Override
    public void insertApplyNonOwnerUnitProj(String applyinstId, String projInfoId, String... unitInfoId) {
        this.insertApplyNonOwnerUnitProj(applyinstId, new String[]{projInfoId},unitInfoId);
    }

    @Override
    public void insertApplyNonOwnerUnitProj(String applyinstId, String[] projInfoIds, String... unitInfoId) {
        LOGGER.debug("新增申报项目经办单位");
        String isOwner = AeaUnitConstants.IS_OWNER_FLASE;
        List<AeaUnitProj> aeaUnitProjListMutiProj = new ArrayList<>();
        List<AeaApplyinstUnitProj> applyinstUnitProjListMutiProj = new ArrayList<>();

        List<AeaUnitProj> aeaUnitProjList = aeaUnitProjMapper.findUnitPorojByProjInfoIdsAndUnitInfoIds(projInfoIds, unitInfoId, isOwner);
        //已存在项目与单位关联关系，不要新增关联
        for (AeaUnitProj aeaUnitProj : aeaUnitProjList) {
            AeaApplyinstUnitProj aeaApplyinstUnitProj = this.createAeaApplyinstUnitProj(applyinstId, aeaUnitProj.getUnitProjId());
            applyinstUnitProjListMutiProj.add(aeaApplyinstUnitProj);
        }
        for (String projInfoId : projInfoIds) {
            this.createApplyinstUnitProjAndUnitProj(applyinstId, isOwner, aeaUnitProjListMutiProj, applyinstUnitProjListMutiProj, projInfoId, Arrays.asList(unitInfoId), aeaUnitProjList);
        }
        if (aeaUnitProjListMutiProj.size() > 0) {
            aeaUnitProjMapper.batchInsertAeaUnitProj(aeaUnitProjListMutiProj);
        }
        aeaApplyinstUnitProjMapper.batchInsertAeaApplyinstUnitProjMapper(applyinstUnitProjListMutiProj);
    }

    private AeaApplyinstUnitProj createAeaApplyinstUnitProj(String applyinstId, String unitProjId) {
        AeaApplyinstUnitProj aeaApplyinstUnitProj = new AeaApplyinstUnitProj();
        aeaApplyinstUnitProj.setApplyinstUnitProjId(UUID.randomUUID().toString());
        aeaApplyinstUnitProj.setApplyinstId(applyinstId);
        aeaApplyinstUnitProj.setUnitProjId(unitProjId);
        aeaApplyinstUnitProj.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
        aeaApplyinstUnitProj.setCreater(SecurityContext.getCurrentUserId());
        aeaApplyinstUnitProj.setCreateTime(new Date());
        return aeaApplyinstUnitProj;
    }

    @Override
    public List<AeaUnitInfo> findApplyUnitProj(String applyinstId, String projInfoId) {
        LOGGER.debug("查询项目申报单位");
        return aeaUnitInfoMapper.getApplyUnitProj(applyinstId,projInfoId);
        //return aeaUnitInfoMapper.findApplyUnitProj(applyinstId, projInfoId, null);
    }

    @Override
    public List<AeaUnitInfo> findApplyOwnerUnitProj(String applyinstId, String projInfoId) {
        LOGGER.debug("查询项目申报建设单位");
        return aeaUnitInfoMapper.findApplyUnitProj(applyinstId, projInfoId, AeaUnitConstants.IS_OWNER_TRUE);
    }

    @Override
    public List<AeaUnitInfo> findApplyNonOwnerUnitProj(String applyinstId, String projInfoId) {
        LOGGER.debug("查询项目申报经办单位");
        return aeaUnitInfoMapper.findApplyUnitProj(applyinstId, projInfoId, AeaUnitConstants.IS_OWNER_FLASE);
    }

    @Override
    public List<AeaUnitInfo> findUnitProjByProjInfoIdAndType(String projInfoId, String unitType) {
        LOGGER.debug("根据类型查询项目单位，projInfoId：{}，unitType：{}", projInfoId, unitType);
        return aeaUnitInfoMapper.findUnitProjByProjInfoIdAndType(projInfoId, unitType,SecurityContext.getCurrentOrgId());
    }

    @Override
    public List<AeaUnitInfo> findAllProjUnit(String projInfoId) {
        LOGGER.debug("查询项目所有单位");
        return aeaUnitInfoMapper.findUnitProjByProjInfoIdAndIsOwner(projInfoId, null);
    }

    @Override
    public List<AeaUnitInfo> findOwerUnitProj(String projInfoId) {
        LOGGER.debug("查询项目建设单位");
        return aeaUnitInfoMapper.findUnitProjByProjInfoIdAndIsOwner(projInfoId, AeaUnitConstants.IS_OWNER_TRUE);
    }

    @Override
    public List<AeaUnitInfo> findNonOwerUnitProj(String projInfoId) {
        LOGGER.debug("查询项目经办单位");
        return aeaUnitInfoMapper.findUnitProjByProjInfoIdAndIsOwner(projInfoId, AeaUnitConstants.IS_OWNER_FLASE);
    }

    @Override
    public List<AeaUnitInfo> findPrimaryUnitProj(String projInfoId) {
        return null;
    }

    @Override
    public List<AeaUnitInfo> findNonPrimaryUnitProj(String projInfoId) {
        return null;
    }

    @Override
    public List<AeaUnitInfo> findChildUnit(String unitInfoId) {
        LOGGER.debug("查询子单位列表");
        return aeaUnitInfoMapper.findChildUnit(unitInfoId);
    }

    @Override
    public AeaUnitInfo getParentUnit(String unitInfoId) {
        LOGGER.debug("查询父单位信息");
        return aeaUnitInfoMapper.getParentUnit(unitInfoId);
    }

    @Override
    public AeaUnitInfo getAeaUnitInfoByLoginName(String loginName) {
        LOGGER.debug("根据登录用户名查询单位信息");
        return aeaUnitInfoMapper.getAeaUnitInfoByLoginName(loginName,SecurityContext.getCurrentOrgId());
    }

    @Override
    public List<AeaUnitInfo> findAeaUnitInfoByKeyword(String keyword) {
        LOGGER.debug("搜索单位信息");
        return aeaUnitInfoMapper.findAeaUnitInfoByKeyword(keyword,SecurityContext.getCurrentOrgId());
    }

    @Override
    public PageInfo<AeaUnitInfo> listAeaUnitInfoByKeyword(String keyword, Page page) {
        LOGGER.debug("关键词分页搜索单位：{}", keyword);
        PageHelper.startPage(page);
        List<AeaUnitInfo> list = aeaUnitInfoMapper.findAeaUnitInfoByKeyword(keyword,SecurityContext.getCurrentOrgId());
        return new PageInfo<>(list);
    }

    @Override
    public List<AeaUnitInfo> findAeaUnitInfo(AeaUnitInfo aeaUnitInfo) {
        LOGGER.debug("综合查询单位");
        return aeaUnitInfoMapper.listAeaUnitInfo(aeaUnitInfo);
    }

    @Override
    public PageInfo<AeaUnitInfo> listAeaUnitInfo(AeaUnitInfo aeaUnitInfo, Page page) {
        LOGGER.debug("综合分页查询单位");
        PageHelper.startPage(page);
        List<AeaUnitInfo> list = aeaUnitInfoMapper.listAeaUnitInfo(aeaUnitInfo);
        return new PageInfo<>(list);
    }

    @Override
    public List<AeaUnitInfo> getUnitInfoByLinkmanInfoId(String userId){
        return aeaUnitInfoMapper.listAeaUnitInfoByLinkIdHasBind(userId);
    }

    @Override
    public List<AeaUnitInfo> getUnitInfoListByIdCard(String idcard){
        AeaUnitInfo query=new AeaUnitInfo();
        query.setUnifiedSocialCreditCode(idcard);
        return aeaUnitInfoMapper.listAeaUnitInfo(query);
    }

    @Override
    public List<ExSJUnitFromDetails> findAeaExProBuildUnitInfoByKeyword(String keyword) {
        LOGGER.debug("搜索单位信息");
        return aeaUnitInfoMapper.findAeaExProBuildUnitInfoByKeyword(keyword,SecurityContext.getCurrentOrgId());
    }

    @Override
    public List<AeaUnitInfo> findAeaUnitInfoByProjLocalCode(String localCode){
        LOGGER.debug("根据项目代码搜索单位信息列表");
        return aeaUnitInfoMapper.findAeaUnitInfoByProjLocalCode(localCode,SecurityContext.getCurrentOrgId());
    }

    @Override
    public void replaceProjRelationByChild(String applyinstId,String parentProjInfoId,String childProjInfoId) throws Exception {
        LOGGER.debug("用工程替换项目的关联关系");
        //    aea_unit_proj ,aea_unit_proj_linkman 没有的就新增   aea_applyinst_unit_proj,aea_applyinst_proj 更新为工程的关联关系
        List<AeaUnitInfo> unitList = aeaUnitInfoMapper.getApplyUnitProj(applyinstId, parentProjInfoId);
        List<AeaUnitProj> childAeaUnitProjs=new ArrayList<>();
        if(unitList.size()>0){
            List<String> unitProjIds = unitList.stream().map(AeaUnitInfo::getUnitProjId).collect(Collectors.toList());
            for (String unitProjId:unitProjIds){
                AeaUnitProj unitProjEntity = aeaUnitProjMapper.getAeaUnitProjById(unitProjId);
                AeaUnitProj childUnitProj = aeaUnitProjMapper.findUnitPorojByProjInfoIdAndUnitInfoId(unitProjEntity.getUnitInfoId(), childProjInfoId, unitProjEntity.getIsOwner());
                if(childUnitProj==null){
                    String childUnitProjId=UUID.randomUUID().toString();
                    unitProjEntity.setCreateTime(new Date());
                    unitProjEntity.setCreater(SecurityContext.getCurrentUserName());
                    unitProjEntity.setUnitProjId(childUnitProjId);
                    unitProjEntity.setProjInfoId(childProjInfoId);
                    aeaUnitProjMapper.insertAeaUnitProj(unitProjEntity);
                    childAeaUnitProjs.add(unitProjEntity);
                    AeaUnitProjLinkman query=new AeaUnitProjLinkman();
                    query.setUnitProjId(unitProjId);
                    List<AeaUnitProjLinkman> projLinkmans = aeaUnitProjLinkmanMapper.listAeaUnitProjLinkman(query);
                    if(projLinkmans.size()>0){
                        for (AeaUnitProjLinkman entity:projLinkmans){
                            AeaUnitProjLinkman aeaUnitProjLinkman=new AeaUnitProjLinkman();
                            aeaUnitProjLinkman.setProjLinkmanId(UUID.randomUUID().toString());
                            aeaUnitProjLinkman.setUnitProjId(unitProjId);
                            aeaUnitProjLinkman.setIsDeleted(Status.OFF);
                            aeaUnitProjLinkman.setCreater(SecurityContext.getCurrentUserName());
                            aeaUnitProjLinkman.setCreateTime(new Date());
                            aeaUnitProjLinkman.setLinkmanType(entity.getLinkmanType());
                            aeaUnitProjLinkman.setLinkmanInfoId(entity.getLinkmanInfoId());
                            aeaUnitProjLinkmanMapper.insertAeaUnitProjLinkman(aeaUnitProjLinkman);
                        }
                    }
                }else{
                    childAeaUnitProjs.add(childUnitProj);
                }
            }
        }

        List<AeaApplyinstUnitProj> applyinstUnitProjs = aeaApplyinstUnitProjMapper.getAeaApplyinstUnitProjByApplyinstId(applyinstId);
        List<AeaApplyinstProj> applyinstProjs = aeaApplyinstProjMapper.getAeaApplyinstProjByApplyinstId(applyinstId);
        if(applyinstUnitProjs.size()>0){
            String[] applyinstUnitProjIds=applyinstUnitProjs.stream().map(AeaApplyinstUnitProj::getApplyinstUnitProjId).toArray(String[]::new);
            aeaApplyinstUnitProjMapper.batchDeleteAeaApplyinstUnitProj(applyinstUnitProjIds);
        }
        if(applyinstProjs.size()>0){
            for (AeaApplyinstProj aeaApplyinstProj:applyinstProjs){
                aeaApplyinstProj.setProjInfoId(childProjInfoId);
                aeaApplyinstProj.setCreater(SecurityContext.getCurrentUserName());
                aeaApplyinstProj.setCreateTime(new Date());
                aeaApplyinstProjMapper.updateAeaApplyinstProj(aeaApplyinstProj);
            }
        }

        if(childAeaUnitProjs.size()>0){
            for (AeaUnitProj child:childAeaUnitProjs){
                AeaApplyinstUnitProj aeaApplyinstUnitProj=new AeaApplyinstUnitProj();
                aeaApplyinstUnitProj.setApplyinstUnitProjId(UUID.randomUUID().toString()); // ()
                aeaApplyinstUnitProj.setUnitProjId(child.getUnitProjId()); // (企业项目关联ID)
                aeaApplyinstUnitProj.setApplyinstId(applyinstId); // (申请实例ID)
                aeaApplyinstUnitProj.setIsDeleted(Status.OFF); // (逻辑删除标记。0表示正常记录，1表示已删除记录。)
                aeaApplyinstUnitProj.setCreater(SecurityContext.getCurrentUserName()); // (创建人)
                aeaApplyinstUnitProj.setCreateTime(new Date()); // (创建时间)
                aeaApplyinstUnitProjMapper.insertAeaApplyinstUnitProj(aeaApplyinstUnitProj);
            }
        }
    }
}
