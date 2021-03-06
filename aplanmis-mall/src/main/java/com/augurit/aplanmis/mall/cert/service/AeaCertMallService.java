package com.augurit.aplanmis.mall.cert.service;

import com.augurit.agcloud.bsc.domain.BscAttDir;
import com.augurit.agcloud.bsc.domain.BscAttLink;
import com.augurit.agcloud.bsc.domain.BscDicRegion;
import com.augurit.agcloud.bsc.mapper.BscAttDirMapper;
import com.augurit.agcloud.bsc.mapper.BscAttMapper;
import com.augurit.agcloud.bsc.mapper.BscDicRegionMapper;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.aplanmis.common.constants.CertHolderConstants;
import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.admin.opus.AplanmisOpuOmOrgAdminService;
import com.augurit.aplanmis.common.service.instance.AeaHiIteminstService;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.vo.LoginInfoVo;
import com.augurit.aplanmis.integration.license.config.LicenseConfig;
import com.augurit.aplanmis.integration.license.dto.*;
import com.augurit.aplanmis.integration.license.service.LicenseApiService;
import com.augurit.aplanmis.mall.cert.vo.AeaCertVo;
import com.augurit.aplanmis.mall.cert.vo.AeaCertinstDetailResultVo;
import com.augurit.aplanmis.mall.cert.vo.AeaCertinstParamVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ZhangXinhui
 * @date 2019/7/29 029 14:58
 * @desc
 **/
@Service
@Transactional
public class AeaCertMallService {

    private static Logger logger = LoggerFactory.getLogger(AeaCertMallService.class);

    @Autowired
    private AeaCertTypeMapper aeaCertTypeMapper;

    @Autowired
    private AeaCertMapper aeaCertMapper;

    @Autowired
    private BscAttDirMapper bscAttDirMapper;

    @Autowired
    private AeaHiIteminstService aeaHiIteminstService;

    @Autowired
    private AplanmisOpuOmOrgAdminService opuOmOrgService;
    @Autowired
    private BscDicRegionMapper bscDicRegionMapper;
    @Autowired
    private LicenseApiService licenseApiService;

    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;
    @Autowired
    private LicenseConfig licenseConfig;

    @Autowired
    private AeaHiCertinstMapper aeaHiCertinstMapper;

    @Autowired
    private AeaProjInfoService aeaProjInfoService;

    @Autowired
    private AeaLinkmanInfoMapper aeaLinkmanInfoMapper;
    @Autowired
    private BscAttMapper bscAttMapper;

    
    public void deleteAeaCertById(String id) {

        if (StringUtils.isNotBlank(id)) {
            aeaCertMapper.deleteById(id);
        }
    }

    
    public void batchDeleteCertByIds(String[] ids) {

        if (ids != null && ids.length > 0) {
            aeaCertMapper.batchDeleteCertByCertIds(ids);
        }
    }

    
    public void saveAeaCert(AeaCert aeaCert) {

        aeaCert.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaCert.setCreater(SecurityContext.getCurrentUserId());
        aeaCert.setCreateTime(LocalDateTime.now());
        aeaCert.setIsDeleted(DeletedStatus.NOT_DELETED);
        aeaCertMapper.insertOne(aeaCert);
    }

    
    public void updateAeaCert(AeaCert aeaCert) {

        aeaCert.setModifier(SecurityContext.getCurrentUserId());
        aeaCert.setModifyTime(LocalDateTime.now());
        aeaCertMapper.updateOne(aeaCert);
    }


    
    public PageInfo<AeaCert> listAeaCert(AeaCert aeaCert, Page page) {

        aeaCert.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaCert> list = aeaCertMapper.listAeaCert(aeaCert);
        logger.debug("成功执行分页查询！");
        return new PageInfo<>(list);
    }

    
    public boolean checkUniqueCertCode(String certId, String certCode, String rootOrgId) {

        Integer count = aeaCertMapper.checkUniqueCertCode(certId, certCode, rootOrgId);
        return count == null || count <= 0;
    }

    
    public List<ZtreeNode> gtreeBscAttDir(String orgId) throws Exception {

        List<ZtreeNode> allNodes = new ArrayList<>();
        if (StringUtils.isNotBlank(orgId)) {
            List<BscAttDir> data = new ArrayList<>();
            // 获取当前组织机构数据
            BscAttDir dir = new BscAttDir();
            dir.setOrgId(orgId);
            dir.setIsRoot(Status.ON);
            List<BscAttDir> list = bscAttDirMapper.listBscAttDir(dir);
            if (list != null && list.size() > 0) {
                data.addAll(list);
            }
            dir.setIsRoot(Status.OFF);
            List<BscAttDir> list2 = bscAttDirMapper.listBscAttDir(dir);
            if (CollectionUtils.isNotEmpty(list2)) {
                data.addAll(list2);
            }
            if (CollectionUtils.isNotEmpty(data)) {
                for (BscAttDir attDir : data) {
                    ZtreeNode node = new ZtreeNode();
                    node.setId(attDir.getDirId());
                    node.setName(attDir.getDirName());
                    node.setpId(attDir.getParentId());
                    node.setOpen(true);
                    node.setIsParent(true);
                    if (StringUtils.isNotBlank(attDir.getParentId())) {
                        node.setIsParent(false);
                    }
                    allNodes.add(node);
                }
            } else {
                allNodes.add(buildZtreeNode());
            }
        } else {
            allNodes.add(buildZtreeNode());
        }
        return allNodes;
    }


    public List<ElementUiRsTreeNode> gtreeAttDirForEui(String orgId) throws Exception {

        List<ElementUiRsTreeNode> allNodes = new ArrayList<ElementUiRsTreeNode>();
        ElementUiRsTreeNode rootNode = new ElementUiRsTreeNode();
        rootNode.setId("root");
        rootNode.setLabel("文件库");
        rootNode.setType("root");
        if (StringUtils.isNotBlank(orgId)) {
            List<BscAttDir> data = new ArrayList<>();
            // 获取当前组织机构数据
            BscAttDir dir = new BscAttDir();
            dir.setOrgId(orgId);
            dir.setIsRoot(Status.ON);
            List<BscAttDir> list = bscAttDirMapper.listBscAttDir(dir);
            if (list != null && list.size() > 0) {
                data.addAll(list);
            }
            dir.setIsRoot(Status.OFF);
            List<BscAttDir> list2 = bscAttDirMapper.listBscAttDir(dir);
            if (CollectionUtils.isNotEmpty(list2)) {
                data.addAll(list2);
            }
            if (CollectionUtils.isNotEmpty(data)) {
                for (BscAttDir attDir : data) {
                    rootNode.getChildren().add(buildEUiNode(attDir));
                }
            }
        }
        allNodes.add(rootNode);
        return allNodes;
    }

    private ElementUiRsTreeNode buildEUiNode(BscAttDir dir) {

        ElementUiRsTreeNode node = new ElementUiRsTreeNode();
        node.setId(dir.getDirId());
        node.setLabel(dir.getDirName());
        node.setType("cert");
        node.setData(dir);
        return node;
    }

    private ZtreeNode buildZtreeNode() {

        ZtreeNode node = new ZtreeNode();
        node.setId("root");
        node.setName("文件库");
        node.setOpen(true);
        node.setIsParent(true);
        node.setNocheck(true);
        return node;
    }

    
    public List<ZtreeNode> gtreeTypeCert(String rootOrgId) {

        if (StringUtils.isBlank(rootOrgId)) {
            rootOrgId = SecurityContext.getCurrentOrgId();
        }
        // 根节点
        List<ZtreeNode> allNodes = new ArrayList<>();
        ZtreeNode rootNode = new ZtreeNode();
        rootNode.setId("root");
        rootNode.setName("电子证照库");
        rootNode.setType("root");
        rootNode.setIsParent(true);
        rootNode.setOpen(true);
        rootNode.setNocheck(true);
        allNodes.add(rootNode);
        // 电子证照分类
        AeaCertType scertType = new AeaCertType();
        scertType.setRootOrgId(rootOrgId);
        List<AeaCertType> certTypes = aeaCertTypeMapper.listAeaCertType(scertType);
        if (certTypes != null && certTypes.size() > 0) {
            // 电子证照数据
            AeaCert scert = new AeaCert();
            scert.setRootOrgId(rootOrgId);
            List<AeaCert> certs = aeaCertMapper.listAeaCert(scert);
            for (AeaCertType certType : certTypes) {
                ZtreeNode certTypeNode = new ZtreeNode();
                certTypeNode.setId(certType.getCertTypeId());
                certTypeNode.setName(certType.getTypeName());
                if (StringUtils.isBlank(certType.getParentTypeId())) {
                    certTypeNode.setpId("root");
                } else {
                    certTypeNode.setpId(certType.getParentTypeId());
                }
                certTypeNode.setType("certType");
                certTypeNode.setIsParent(true);
                certTypeNode.setOpen(true);
                certTypeNode.setNocheck(true);
                allNodes.add(certTypeNode);
                if (certs != null && certs.size() > 0) {
                    List<AeaCert> removeCertList = new ArrayList<>();
                    for (AeaCert cert : certs) {
                        if (cert.getCertTypeId().equals(certType.getCertTypeId())) {
                            ZtreeNode certNode = new ZtreeNode();
                            certNode.setId(cert.getCertId());
                            certNode.setName(cert.getCertName());
                            certNode.setpId(certType.getCertTypeId());
                            certNode.setType("cert");
                            certNode.setIsParent(false);
                            certNode.setOpen(true);
                            certNode.setNocheck(false);
                            allNodes.add(certNode);
                            removeCertList.add(cert);
                        }
                    }
                    // 移除已经遍历过的证照
                    certs.removeAll(removeCertList);
                }
            }
        }
        return allNodes;
    }

    
    public Long getMaxCertSortNo(String rootOrgId) {

        if (StringUtils.isBlank(rootOrgId)) {
            rootOrgId = SecurityContext.getCurrentOrgId();
        }
        Long sortNo = aeaCertMapper.getMaxCertSortNo(rootOrgId);
        return sortNo == null ? 1L : sortNo + 1;
    }

    
    public AeaCert getAeaCertById(String id) {

        if (StringUtils.isBlank(id)) {
            throw new InvalidParameterException(id);
        }
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaCertMapper.selectOneById(id);
    }

    
    public PageInfo<AeaCert> listStageNoSelectCertByPage(AeaParIn aeaParIn, Page page) {

        aeaParIn.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaCert> list = aeaCertMapper.listStageNoSelectCert(aeaParIn);
        return new PageInfo<>(list);
    }

    
    public List<AeaCert> listStageNoSelectCert(AeaParIn aeaParIn) {

        aeaParIn.setRootOrgId(SecurityContext.getCurrentOrgId());
        return aeaCertMapper.listStageNoSelectCert(aeaParIn);
    }

    
    public PageInfo<AeaCert> listItemNoSelectCertByPage(AeaItemInout inout, Page page) {

        inout.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaCert> list = aeaCertMapper.listItemNoSelectCert(inout);
        return new PageInfo<>(list);
    }

    
    public List<AeaCert> listItemNoSelectCert(AeaItemInout inout) {

        inout.setRootOrgId(SecurityContext.getCurrentOrgId());
        return aeaCertMapper.listItemNoSelectCert(inout);
    }

    
    public List<AeaCert> getOutputCertsByIteminstId(String iteminstId) throws Exception {

        if (StringUtils.isBlank(iteminstId)) return null;

        AeaHiIteminst iteminst = aeaHiIteminstService.getAeaHiIteminstById(iteminstId);

        if (iteminst == null) return null;

        return aeaCertMapper.getOutputCertsByItemVerId(iteminst.getItemVerId(), SecurityContext.getCurrentOrgId());
    }


    
    public LicenseAuthResDTO getLicenseAuthRes(String itemVerIds, String identityNumber,LoginInfoVo loginVo) throws Exception {
        String accessToken = licenseApiService.getLoginToken();
        if (StringUtils.isBlank(itemVerIds) || StringUtils.isBlank(identityNumber))
            return new LicenseAuthResDTO();
        List<String> itemverIds = new ArrayList<>();
        Collections.addAll(itemverIds, itemVerIds.split(","));
        List<AeaItemBasic> list = aeaItemBasicMapper.getAeaItemBasicListByItemVerIds(itemverIds);
        return getLicenseAuthRes(list, accessToken, identityNumber,loginVo);
    }

    private LicenseAuthResDTO getLicenseAuthRes(List<AeaItemBasic> list, String accessToken, String identityNumber,LoginInfoVo loginVo) {
        if (list.size() < 1)
            return new LicenseAuthResDTO();
        LicenseAuthResDTO result = new LicenseAuthResDTO();
        ArrayList checkAuthCodes = new ArrayList();//针对多个事项时，进行重复过滤
        ArrayList auth_codes = new ArrayList();
        List<LicenseDTO> licenseDTO = new ArrayList<>();
        //OpuOmUserInfo userinfo = opuOmUserInfoMapper.getOpuOmUserInfoByUserId(SecurityContext.getCurrentUserId());
        OpuOmOrg topOrg = opuOmOrgService.getTopOrgByCurOrgId(SecurityContext.getCurrentOrgId());
        BscDicRegion proDataRegion = bscDicRegionMapper.getBscDicRegionById(topOrg.getRegionId());
        LicenseUserInfoDTO operator = new LicenseUserInfoDTO();
        operator.setIdentity_num(loginVo.getIdCard() == null ? loginVo.getUnifiedSocialCreditCode(): loginVo.getIdCard());//身份证号
        operator.setDivision(topOrg.getOrgName());
        operator.setDivision_code(proDataRegion.getRegionNum());
        operator.setService_org(proDataRegion.getRegionNum());
        operator.setService_org_code(topOrg.getOrgCode());
        for (AeaItemBasic aeaItemBasic : list) {
            LicenseAuthResDTO data = null;
            LicenseAuthReqDTO authReqDTO = new LicenseAuthReqDTO();
            authReqDTO.setService_item_name(aeaItemBasic.getItemName());
            authReqDTO.setService_item_code(aeaItemBasic.getItemCode());//事项编码
            authReqDTO.setIdentity_number(identityNumber);//证件号码（企业的信用代码/-/法人的身份证号）
            authReqDTO.setOperator(operator);
            try {
                data = licenseApiService.licenseAuth(accessToken, authReqDTO);
            } catch (Exception e) {
                logger.debug("事项：" + aeaItemBasic.getItemName() + "查询电子证照库失败！");
            }
            if (data != null) {
                for (int i = 0; i < data.getTotal_count(); i++) {
                    if (checkAuthCodes.contains(data.getData().get(i).getLicense_code()))
                        continue;
                    checkAuthCodes.add(data.getData().get(i).getLicense_code());
                    auth_codes.add(data.getAuth_codes()[i]);
                    data.getData().get(i).setAuth_code(data.getAuth_codes()[i]);
                    licenseDTO.add(data.getData().get(i));
                }
            }
        }
        if (licenseDTO.size() > 0) {
            result.setAuth_codes((String[]) auth_codes.toArray(new String[auth_codes.size()]));
            result.setData(licenseDTO);
            result.setTotal_count((long) licenseDTO.size());
        }
        return result;
    }

    
    public String getViewLicenseURL(String authCode) {
        LicenseTokenResDTO LicenseTokenResDTO;
        try {
            String accessToken = licenseApiService.getLoginToken();
            LicenseTokenResDTO = licenseApiService.LicenseToken(accessToken, authCode);
            return licenseConfig.getWebRoot() + "/license/view_license?license_access_token=" + LicenseTokenResDTO.getData().getLicense_access_token();
        } catch (Exception e) {
            logger.debug("获取电子证照查看链接失败！");
        }
        return null;
    }

    public List<AeaHiCertinst> getCertintListByCertHolder(String certHolder, String keyword, int pageNum, int pageSize,LoginInfoVo loginInfo) {
        String linkmanInfoId=null;
        String unitInfoId=null;
        String[] projInfoIds=null;
        if(CertHolderConstants.CERT_FROM_APPLICANT.equals(certHolder)){//企业证照
            unitInfoId=loginInfo.getUnitId();
            if(StringUtils.isBlank(unitInfoId)) return new ArrayList<>();
        }else if(CertHolderConstants.CERT_FROM_CORPORATION.equals(certHolder)){//法人证照
            if("1".equals(loginInfo.getIsPersonAccount())||com.augurit.agcloud.framework.util.StringUtils.isNotBlank(loginInfo.getUserId())){//个人
                linkmanInfoId=loginInfo.getUserId();
            }else{//法人
                List<AeaLinkmanInfo> linkmanList = aeaLinkmanInfoMapper.findCorporationByUnitInfoId(loginInfo.getUnitId());
                linkmanInfoId=linkmanList.size()>0?linkmanList.get(0).getLinkmanInfoId():null;
            }
            if(StringUtils.isBlank(linkmanInfoId)) return new ArrayList<>();
        }else if(CertHolderConstants.CERT_FROM_PROJ.equals(certHolder)){//项目证照
            List<AeaProjInfo> projList=null;
            if("1".equals(loginInfo.getIsPersonAccount())){//个人
                projList=aeaProjInfoService.findRootAeaProjInfoByLinkmanInfoId(loginInfo.getUserId());
            }else if(com.augurit.agcloud.framework.util.StringUtils.isNotBlank(loginInfo.getUserId())){//委托人
                projList=aeaProjInfoService.findRootAeaProjInfoByLinkmanInfoIdAndUnitInfoId(loginInfo.getUserId(),loginInfo.getUnitId());
            }else{//企业
                projList=aeaProjInfoService.findRootAeaProjInfoByUnitInfoId(loginInfo.getUnitId());
            }
            projInfoIds=projList.size()>0?projList.stream().map(AeaProjInfo::getProjInfoId).toArray(String[]::new):null;
            if(projInfoIds==null||projInfoIds.length==0) return new ArrayList<>();
        }else{
            return new ArrayList<>();
        }
        com.github.pagehelper.PageHelper.startPage(pageNum, pageSize);
        List<AeaHiCertinst>list=aeaHiCertinstMapper.getCertintList(linkmanInfoId,unitInfoId,projInfoIds,keyword,SecurityContext.getCurrentOrgId());
        return list;
    }

    public List<AeaCertVo> getCertTypesAndCertList(String certHolder) {
        AeaCertType typeQ = new AeaCertType();
        typeQ.setRootOrgId(SecurityContext.getCurrentOrgId());
        typeQ.setIsActive("1");
        List<AeaCertType> list = aeaCertTypeMapper.listAeaCertType(typeQ);
        if(list.size()==0) return new ArrayList<>();
        AeaCert query=new AeaCert();
        query.setRootOrgId(SecurityContext.getCurrentOrgId());
        query.setCertHolder(StringUtils.isNotBlank(certHolder)?certHolder:null);
        return list.stream().map(AeaCertVo::forMat).peek(vo->{
            query.setCertTypeId(vo.getCertTypeId());
            vo.setCertList(aeaCertMapper.listAeaCert(query));
            }).collect(Collectors.toList());
        }

    public AeaHiCertinst saveCertint(AeaCertinstParamVo aeaCertinstParamVo, LoginInfoVo loginVo) throws Exception {
        String certintId=aeaCertinstParamVo.getCertinstId();
        String certHolder=aeaCertinstParamVo.getCertHolder();
        AeaHiCertinst aeaHiCertinst=new AeaHiCertinst();
        BeanUtils.copyProperties(aeaCertinstParamVo,aeaHiCertinst);
        aeaHiCertinst.setCertinstSource("local");
        if("u".equals(certHolder) && StringUtils.isNotBlank(loginVo.getUserId())){//个人
            aeaHiCertinst.setLinkmanInfoId(loginVo.getUserId());
        }else if("c".equals(certHolder) && StringUtils.isNotBlank(loginVo.getUnitId())){//单位
            aeaHiCertinst.setUnitInfoId(loginVo.getUnitId());
        }else{
            throw new Exception("当前用户不可创建该类型证照!");
        }
        if(StringUtils.isNotBlank(certintId)){//更新
            aeaHiCertinst.setModifier(SecurityContext.getCurrentUserName());
            aeaHiCertinst.setModifyTime(new Date());
            aeaHiCertinstMapper.updateAeaHiCertinst(aeaHiCertinst);
        }else{//新增
            aeaHiCertinst.setCertinstId(UUID.randomUUID().toString());
            aeaHiCertinst.setCreater(SecurityContext.getCurrentUserName());
            aeaHiCertinst.setCreateTime(new Date());
            //aeaHiCertinst.setCertinstSource("external");
            aeaHiCertinstMapper.insertAeaHiCertinst(aeaHiCertinst);
        }
        return aeaHiCertinst;
    }

    public void deleteCertinstById(String id) throws Exception {
        if (StringUtils.isNotBlank(id)) {
            aeaHiCertinstMapper.deleteAeaHiCertinst(id);
        }
    }

    public AeaCertinstDetailResultVo getCertinstById(String certinstId) throws Exception {
        AeaCertinstDetailResultVo vo=new AeaCertinstDetailResultVo();
        AeaHiCertinst certinst = aeaHiCertinstMapper.getAeaHiCertinstById(certinstId);
        BeanUtils.copyProperties(certinst,vo);
        if(StringUtils.isNotBlank(certinst.getLinkmanInfoId())){
            vo.setCertHolder("u");
        }else{
            vo.setCertHolder("c");
        }
        AeaCert cert = aeaCertMapper.getAeaCertById(certinst.getCertId(), SecurityContext.getCurrentOrgId());
        vo.setCertType(cert.getCertTypeId());
        BscAttLink query=new BscAttLink();
        query.setLinkId(vo.getAttLinkId());
        List<BscAttLink> attLinkL = bscAttMapper.listBscAttLink(query);
        if(attLinkL.size()>0) vo.setAttDetailId(attLinkL.get(0).getDetailId());
        return vo;
    }
}
