package com.augurit.aplanmis.common.service.linkman.impl;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.aplanmis.common.constants.ActiveStatus;
import com.augurit.aplanmis.common.constants.AeaLinkmanConstants;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaProjLinkman;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.domain.AeaUnitLinkman;
import com.augurit.aplanmis.common.mapper.AeaLinkmanInfoMapper;
import com.augurit.aplanmis.common.mapper.AeaProjLinkmanMapper;
import com.augurit.aplanmis.common.mapper.AeaUnitInfoMapper;
import com.augurit.aplanmis.common.mapper.AeaUnitLinkmanMapper;
import com.augurit.aplanmis.common.service.linkman.AeaLinkmanInfoService;
import com.augurit.aplanmis.common.vo.LinkmanTypeVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author yinlf
 * @Date 2019/7/8
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AeaLinkmanInfoServiceImpl implements AeaLinkmanInfoService {

    private static Logger LOGGER = LoggerFactory.getLogger(AeaLinkmanInfoService.class);

    @Autowired
    private AeaLinkmanInfoMapper aeaLinkmanInfoMapper;

    @Autowired
    private AeaProjLinkmanMapper aeaProjLinkmanMapper;

    @Autowired
    private AeaUnitLinkmanMapper aeaUnitLinkmanMapper;
    @Autowired
    private AeaUnitInfoMapper aeaUnitInfoMapper;

    @Override
    public void insertAeaLinkmanInfo(AeaLinkmanInfo aeaLinkmanInfo) {
        aeaLinkmanInfo.setLinkmanInfoId(UUID.randomUUID().toString());
        aeaLinkmanInfo.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaLinkmanInfo.setCreater(SecurityContext.getCurrentUserId());
        aeaLinkmanInfo.setCreateTime(new Date());
        aeaLinkmanInfo.setIsDeleted(Status.OFF);
        aeaLinkmanInfo.setIsActive(ActiveStatus.ACTIVE.getValue());
        LOGGER.debug("新增单位");
        aeaLinkmanInfoMapper.insertAeaLinkmanInfo(aeaLinkmanInfo);
    }

    @Override
    public void updateAeaLinkmanInfo(AeaLinkmanInfo aeaLinkmanInfo) {
        aeaLinkmanInfo.setModifier(SecurityContext.getCurrentUserId());
        aeaLinkmanInfo.setModifyTime(new Date());
        LOGGER.debug("修改项目信息");
        aeaLinkmanInfoMapper.updateAeaLinkmanInfo(aeaLinkmanInfo);
    }

    @Override
    public void deleteAeaLinkmanInfo(String... linkmanInfoId) {
        LOGGER.debug("批量删除联系人信息");
        aeaLinkmanInfoMapper.batchLinkmanInfo(linkmanInfoId);
    }

    @Override
    public void deleteAeaLinkmanInfoCascade(String linkmanInfoId) {

    }

    @Override
    public AeaLinkmanInfo getAeaLinkmanInfoByLinkmanInfoId(String linkmanInfoId) {
        LOGGER.debug("根据联系人ID查询联系人信息，联系人ID：{}", linkmanInfoId);
        return aeaLinkmanInfoMapper.getAeaLinkmanInfoById(linkmanInfoId);
    }

    @Override
    public void insertProjLinkman(String applyinstId, String projInfoId, String... linkmanInfoId) {
        List<AeaProjLinkman> aeaProjLinkmanList = new ArrayList<>();
        for (String id : linkmanInfoId) {
            AeaProjLinkman aeaProjLinkman = new AeaProjLinkman();
            aeaProjLinkman.setProjLinkmanId(UUID.randomUUID().toString());
            aeaProjLinkman.setProjInfoId(projInfoId);
            aeaProjLinkman.setApplyinstId(applyinstId);
            aeaProjLinkman.setLinkmanInfoId(id);
            aeaProjLinkman.setCreater(SecurityContext.getCurrentUserId());
            aeaProjLinkman.setCreateTime(new Date());
            aeaProjLinkman.setType(AeaLinkmanConstants.TYPE_LINK);
            aeaProjLinkmanList.add(aeaProjLinkman);
        }
        LOGGER.debug("项目新增联系人");
        aeaProjLinkmanMapper.batchInsertAeaProjLinkman(aeaProjLinkmanList);
    }

    @Override
    public void deleteProjLinkman(String projInfoId, String... linkmanInfoId) {
        LOGGER.debug("删除项目联系人");
        aeaProjLinkmanMapper.deleteProjLinkman(projInfoId, linkmanInfoId);
    }

    @Override
    public void insertUnitLinkman(String unitInfoId, String... linkmanInfoId) {
        List<AeaUnitLinkman> aeaUnitLinkmanList = new ArrayList<>();
        for (String id : linkmanInfoId) {
            AeaUnitLinkman aeaUnitLinkman = new AeaUnitLinkman();
            aeaUnitLinkman.setUnitLinkmanId(UUID.randomUUID().toString());
            aeaUnitLinkman.setUnitInfoId(unitInfoId);
            aeaUnitLinkman.setLinkmanInfoId(id);
            aeaUnitLinkman.setCreater(SecurityContext.getCurrentUserId());
            aeaUnitLinkman.setCreateTime(new Date());
            aeaUnitLinkmanList.add(aeaUnitLinkman);
        }
        LOGGER.debug("新增单位联系人");
        aeaUnitLinkmanMapper.batchInsertAeaUnitLinkman(aeaUnitLinkmanList);
    }

    @Override
    public void deleteUnitLinkman(String unitInfoId, String... linkmanInfoId) {
        LOGGER.debug("删除单位联系人");
        aeaUnitLinkmanMapper.deleteUnitLinkman(unitInfoId, linkmanInfoId);
    }

    @Override
    public List<AeaLinkmanInfo> getProjLinkman(String applyinstId, String projInfoId) {
        LOGGER.debug("查项目：{}，联系人", projInfoId);
        return aeaLinkmanInfoMapper.getProjLinkman(applyinstId, projInfoId);
    }

    @Override
    public void insertApplyProjLinkman(String applyinstId, String projInfoId, String linkmanInfoId) {
        LOGGER.debug("保存项目联系人,项目ID：{}", projInfoId);
        AeaProjLinkman aeaProjLinkman = new AeaProjLinkman();
        aeaProjLinkman.setProjLinkmanId(UUID.randomUUID().toString());
        aeaProjLinkman.setProjInfoId(projInfoId);
        aeaProjLinkman.setApplyinstId(applyinstId);
        aeaProjLinkman.setLinkmanInfoId(linkmanInfoId);
        aeaProjLinkman.setCreater(SecurityContext.getCurrentUserId());
        aeaProjLinkman.setCreateTime(new Date());
        aeaProjLinkman.setType(AeaLinkmanConstants.TYPE_APPLY);
        aeaProjLinkmanMapper.insertAeaProjLinkman(aeaProjLinkman);
    }

    @Override
    public void insertApplyAndLinkProjLinkman(String applyinstId, String[] projInfoIds, String applyLinkmanId, String linkmanInfoId) {
        LOGGER.debug("保存项目联系人和申请人");
        List<AeaProjLinkman> aeaProjLinkmenList = new ArrayList<>();
        for (String projInfoId : projInfoIds) {
            if(StringUtils.isNotBlank(applyLinkmanId)){
                if (!existsPorjLinkmanThenUpdate(applyinstId, applyLinkmanId, projInfoId, AeaLinkmanConstants.TYPE_APPLY)) {
                    AeaProjLinkman applyLinkman = this.createAeaProjLinkman(projInfoId, applyLinkmanId, AeaLinkmanConstants.TYPE_APPLY, applyinstId);
                    aeaProjLinkmenList.add(applyLinkman);
                }
            }
            if(StringUtils.isNotBlank(linkmanInfoId)){
                if (!existsPorjLinkmanThenUpdate(applyinstId, linkmanInfoId, projInfoId, AeaLinkmanConstants.TYPE_LINK)) {
                    AeaProjLinkman projLinkman = this.createAeaProjLinkman(projInfoId, linkmanInfoId, AeaLinkmanConstants.TYPE_LINK, applyinstId);
                    aeaProjLinkmenList.add(projLinkman);
                }
            }
        }
        if(aeaProjLinkmenList.size()>0){
            aeaProjLinkmanMapper.batchInsertAeaProjLinkman(aeaProjLinkmenList);
        }
    }

    private boolean existsPorjLinkmanThenUpdate(String applyinstId, String linkmanId, String projInfoId, String typeApply) {
        AeaProjLinkman aeaProjLinkman = new AeaProjLinkman();
        aeaProjLinkman.setProjInfoId(projInfoId);
        aeaProjLinkman.setLinkmanInfoId(linkmanId);
        aeaProjLinkman.setType(typeApply);
        aeaProjLinkman.setApplyinstId(applyinstId);
        List<AeaProjLinkman> aeaProjLinkmen = aeaProjLinkmanMapper.listAeaProjLinkman(aeaProjLinkman);
        if (CollectionUtils.isNotEmpty(aeaProjLinkmen)) {
            //aeaProjLinkman = aeaProjLinkmen.get(0);
            //aeaProjLinkman.setApplyinstId(applyinstId);
            //aeaProjLinkmanMapper.updateAeaProjLinkman(aeaProjLinkman);
            return true;
        }
        return false;
    }

    /**
     * 创建联系人对象
     *
     * @param projInfoId  项目ID
     * @param linkmanInfoId   项目联系人或项目申请人ID
     * @param type        类型。link：联系人，apply申请人
     * @param applyinstId 申请实例ID
     * @return
     */
    private AeaProjLinkman createAeaProjLinkman(String projInfoId, String linkmanInfoId, String type, String applyinstId) {
        AeaProjLinkman aeaProjLinkman = new AeaProjLinkman();
        aeaProjLinkman.setProjLinkmanId(UUID.randomUUID().toString());
        aeaProjLinkman.setApplyinstId(applyinstId);
        aeaProjLinkman.setProjInfoId(projInfoId);
        aeaProjLinkman.setLinkmanInfoId(linkmanInfoId);
        aeaProjLinkman.setType(type);
        aeaProjLinkman.setCreater(SecurityContext.getCurrentUserId());
        aeaProjLinkman.setCreateTime(new Date());
        return aeaProjLinkman;
    }

    @Override
    public AeaLinkmanInfo getApplyProjLinkman(String applyinstId, String projInfoId) {
        LOGGER.debug("查项目：{}，申请人", projInfoId);
        return aeaLinkmanInfoMapper.getApplyProjLinkman(applyinstId, projInfoId);
    }

    @Override
    public AeaLinkmanInfo getApplyLinkman(String applyinstId) {
        LOGGER.debug("查询申报：{}，联系人", applyinstId);
        return aeaLinkmanInfoMapper.getApplyLinkman(applyinstId);
    }

    @Override
    public List<AeaLinkmanInfo> findAllUnitLinkman(String unitInfoId) {
        LOGGER.debug("查单位：{}，联系人", unitInfoId);
        return aeaLinkmanInfoMapper.findAllUnitLinkman(unitInfoId);
    }

    @Override
    public AeaLinkmanInfo getAeaLinkmanInfoByLoginName(String loginName) {
        LOGGER.debug("根据联系人登录名查询：{}", loginName);
        return aeaLinkmanInfoMapper.getAeaLinkmanInfoByLoginName(loginName,SecurityContext.getCurrentOrgId());
    }

    @Override
    public List<AeaLinkmanInfo> findLinkmanInfoByKeyword(String keyword) {
        LOGGER.debug("关键词搜索联系人：{}", keyword);
        return aeaLinkmanInfoMapper.findLinkmanInfoByKeyword(keyword,SecurityContext.getCurrentOrgId());
    }

    @Override
    public PageInfo<AeaLinkmanInfo> listLinkmanInfoByKeyword(String keyword, Page page) {
        LOGGER.debug("关键词分页搜索联系人：{}", keyword);
        PageHelper.startPage(page);
        List<AeaLinkmanInfo> list = aeaLinkmanInfoMapper.findLinkmanInfoByKeyword(keyword,SecurityContext.getCurrentOrgId());
        return new PageInfo<>(list);
    }

    @Override
    public List<AeaLinkmanInfo> findLinkmanInfo(AeaLinkmanInfo aeaLinkmanInfo) {
        LOGGER.debug("多条件查询联系人");
        aeaLinkmanInfo.setRootOrgId(SecurityContext.getCurrentOrgId());
        return aeaLinkmanInfoMapper.findAeaLinkmanInfo(aeaLinkmanInfo);
    }

    @Override
    public PageInfo<AeaLinkmanInfo> listLinkmanInfo(AeaLinkmanInfo aeaLinkmanInfo, Page page) {
        aeaLinkmanInfo.setRootOrgId(SecurityContext.getCurrentOrgId());
        LOGGER.debug("多条件分页查询联系人");
        PageHelper.startPage(page);
        List<AeaLinkmanInfo> list = aeaLinkmanInfoMapper.findAeaLinkmanInfo(aeaLinkmanInfo);
        return new PageInfo<>(list);
    }

    @Override
    public List<AeaLinkmanInfo> getAeaLinkmanInfoByUnitInfoIdAndIsBindAccount(String unitInfoId, String isBindAccount) throws Exception{
        return aeaLinkmanInfoMapper.getAeaLinkmanInfoByUnitInfoIdAndIsBindAccount(unitInfoId,isBindAccount);
    }

    @Override
    public void updateAeaUnitLinkmanByUnitAndLinkman(AeaUnitLinkman aeaUnitLinkman) throws Exception {
        aeaUnitLinkmanMapper.updateAeaUnitLinkmanByUnitAndLinkman(aeaUnitLinkman);
    }
    @Override
    public List<AeaLinkmanInfo> getByKeyword(String keyword, String unitInfoId) throws Exception {
        if(StringUtils.isBlank(keyword)) throw new Exception("缺少查询关键字");
        AeaLinkmanInfo query=new AeaLinkmanInfo();
        if(StringUtils.isNotBlank(keyword)){
            query.setKeyword(keyword);
        }
        query.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaLinkmanInfo> linkmans = aeaLinkmanInfoMapper.findAeaLinkmanInfo(query);
        if (linkmans.size()>0){
            AeaUnitInfo aeaUnitInfo=aeaUnitInfoMapper.getAeaUnitInfoById(unitInfoId);
            linkmans.stream().forEach(linkman->{
                if(aeaUnitInfo!=null) {
                    linkman.setApplicant(aeaUnitInfo.getApplicant());
                    linkman.setUnitInfoId(unitInfoId);
                }
            });
        }
        return linkmans;
    }

    @Override
    public List<AeaLinkmanInfo> getAeaLinkmanInfoListByCertNo(String certNo){
        AeaLinkmanInfo query=new AeaLinkmanInfo();
        query.setLinkmanCertNo(certNo);
        query.setRootOrgId(SecurityContext.getCurrentOrgId());
        return aeaLinkmanInfoMapper.findAeaLinkmanInfo(query);
    }

    @Override
    public List<LinkmanTypeVo> findLinkmanTypes(String projInfoId, String unitInfoId) {
        List<AeaLinkmanInfo> list = aeaLinkmanInfoMapper.findLinkmanTypes(projInfoId, unitInfoId);
        return (list.size()>0&&!list.isEmpty()&& list.get(0)!=null)?list.stream().map(LinkmanTypeVo::build).collect(Collectors.toList()):new ArrayList<>();
    }
}
