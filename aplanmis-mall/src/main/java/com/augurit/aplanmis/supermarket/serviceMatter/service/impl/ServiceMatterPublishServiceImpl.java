package com.augurit.aplanmis.supermarket.serviceMatter.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.bsc.mapper.BscAttDetailMapper;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmUser;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.CommonLoginService;
import com.augurit.aplanmis.common.utils.FileUtils;
import com.augurit.aplanmis.common.vo.AeaHiCertinstBVo;
import com.augurit.aplanmis.common.vo.ElTreeVo;
import com.augurit.aplanmis.common.vo.ServiceMatterVo;
import com.augurit.aplanmis.supermarket.serviceMatter.service.ServiceMatterPublishService;
import com.augurit.aplanmis.supermarket.vo.ServiceQualVo;
import com.github.pagehelper.Page;
import com.google.common.base.Strings;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author chenjw
 * @version v1.0.0
 * @description
 * @date Created in 2019/6/3/003 16:52
 */
@Service
@Transactional
public class ServiceMatterPublishServiceImpl implements ServiceMatterPublishService {

    private static Logger logger = LoggerFactory.getLogger(ServiceMatterPublishServiceImpl.class);

    @Autowired
    private AeaImServiceItemMapper serviceItemMapper;
    @Autowired
    private AeaImServiceMapper serviceMapper;
    @Autowired
    private AeaImServiceMajorMapper aeaImServiceMajorMapper;
    @Autowired
    private AeaImQualMapper aeaImQualMapper;
    @Autowired
    private AeaImQualLevelMapper aeaImQualLevelMapper;
    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;
    @Autowired
    private AeaHiCertinstMapper certinstMapper;
    @Autowired
    private AeaUnitInfoMapper unitInfoMapper;
    @Autowired
    private AeaImUnitServiceMapper unitServiceMapper;
    @Autowired
    private AeaCertMapper certMapper;
    @Autowired
    private AeaBusCertinstMapper busCertinstMapper;

    @Autowired
    private AeaLinkmanInfoMapper linkmanInfoMapper;

    @Autowired
    private AeaImCertinstMajorMapper certinstMajorMapper;
    @Autowired
    private AeaImUnitServiceLinkmanMapper unitServiceLinkmanMapper;
    @Autowired
    private BscAttDetailMapper bscAttDetailMapper;
    @Autowired
    private AeaImServiceQualMapper aeaImServiceQualMapper;
    @Autowired
    private CommonLoginService commonLoginService;

    @Autowired
    private AeaItemRelevanceMapper aeaItemRelevanceMapper;

    @Autowired
    private AeaImCertinstMajorMapper aeaImCertinstMajorMapper;

    @Value("${dg.sso.access.platform.org.top-org-id}")
    protected String topOrgId;

    private static final String IS_NO_DELETE = "0";

    private void setDefaultUser() {
//        try {
//            SecurityContext.getCurrentUserName();
//        }catch (Exception e){
//            OpuOmUser opuOmUser = new OpuOmUser();
//            opuOmUser.setLoginName("htry");
//            opuOmUser.setLoginPwd("202cb962ac59075b964b07152d234b70");
//            opuOmUser.setUserId("4df31dde-9ed6-4f7c-968b-aa22fdd9ab7f");
//            opuOmUser.setUserName("系统管理");
//            loginService.setLoginUserToSecurityContext(opuOmUser);
//        }
        //测试用的默认用户。不知道为啥catch不了异常。
        OpuOmUser opuOmUser = new OpuOmUser();
        opuOmUser.setLoginName("htry");
        opuOmUser.setLoginPwd("202cb962ac59075b964b07152d234b70");
        opuOmUser.setUserId("4df31dde-9ed6-4f7c-968b-aa22fdd9ab7f");
        opuOmUser.setUserName("系统管理");
        commonLoginService.setLoginUserToSecurityContext(opuOmUser);
    }

    @Override
    public List<AeaImServiceMajor> getMajorList() throws Exception {
        AeaImServiceMajor query = new AeaImServiceMajor();
        query.setRootOrgId(topOrgId);
        return aeaImServiceMajorMapper.listAeaImServiceMajor(query);
    }

    @Override
    public List<ZtreeNode> getQualLevelList() throws Exception {
        AeaImQualLevel query = new AeaImQualLevel();
        query.setRootOrgId(topOrgId);
        List<AeaImQualLevel> qualLevelList = aeaImQualLevelMapper.listAeaImQualLevel(query);
        List<ZtreeNode> allNodes = new ArrayList<>();
        ZtreeNode rootNode = new ZtreeNode();
        rootNode.setId("root");
        rootNode.setName("资质等级");
        rootNode.setType("root");
        rootNode.setIsParent(true);
        rootNode.setOpen(true);
        rootNode.setNocheck(true);
        allNodes.add(rootNode);
//TODO 将数据放到树节点里面去
        if (qualLevelList != null && qualLevelList.size() > 0) {
            qualLevelList.stream().forEach(qualLevel -> {
                ZtreeNode qualLevelNode = new ZtreeNode();
                qualLevelNode.setId(qualLevel.getQualLevelId());
                qualLevelNode.setName(qualLevel.getQualLevelName());
                String pId = StringUtils.isBlank(qualLevel.getParentQualLevelId()) ? "root" : qualLevel.getParentQualLevelId();
                qualLevelNode.setpId(pId);
                qualLevelNode.setType("qualLevel");
                qualLevelNode.setIsParent(true);
                qualLevelNode.setOpen(true);
                qualLevelNode.setNocheck(true);
                allNodes.add(qualLevelNode);
            });
        }
        return allNodes;
    }

    @Override
    public void saveServiceMatter(ServiceMatterVo serviceMatterVo) throws Exception {

        //保存保存发布中介服务：
        String unitServiceId = UUID.randomUUID().toString();
        serviceMatterVo.setUnitServiceId(unitServiceId);
        serviceMatterVo.setCreater(SecurityContext.getCurrentUserId());
//        serviceMatterVo.setCreater("test");
        serviceMatterVo.setCreateTime(new Date());
        serviceMatterVo.setIsActive("1");
        serviceMatterVo.setIsDelete(IS_NO_DELETE);
        serviceMatterVo.setAuditFlag("2");
        serviceMatterVo.setRootOrgId(topOrgId);
        unitServiceMapper.insertAeaImUnitService(serviceMatterVo);

      /*  //添加记录到中介服务事项和服务类型管理表
        AeaImServiceTypeItem serviceTypeItem = new AeaImServiceTypeItem();
        String serviceTypeItemId = UUID.randomUUID().toString();
        serviceTypeItem.setServiceTypeItemId(serviceTypeItemId);
//        serviceTypeItem.setCreater(SecurityContext.getCurrentUserId());
        serviceTypeItem.setCreater("test");
        serviceTypeItem.setCreateTime(new Date());
        serviceTypeItem.setIsDelete(IS_NO_DELETE);
        //serviceTypeItem.setItemVerId(itemBasic.getItemVerId());
//        serviceTypeItem.setServiceTypeId(serviceMatterVo.getServiceTypeId());
        serviceTypeItemMapper.insertAeaImServiceTypeItem(serviceTypeItem);*/


        //根据有多少根证照添加记录到业务与证照实例关联表，用于关联证照
        List<AeaHiCertinstBVo> certinstBVos = serviceMatterVo.getCertinstBVos();
        List<AeaBusCertinst> busCertinsts = new ArrayList<>();
        if (certinstBVos != null && certinstBVos.size() > 0) {
            for (AeaHiCertinstBVo bvo : certinstBVos) {
                AeaBusCertinst busCertinst = new AeaBusCertinst();
                busCertinst.setBusCertinstId(UUID.randomUUID().toString());
                busCertinst.setBusTableName("AEA_IM_UNIT_SERVICE");
                busCertinst.setPkName("UNIT_SERVICE_ID");
                busCertinst.setCertinstId(bvo.getCertinstId());
                busCertinst.setAuditFlag("2");
                busCertinst.setIsDelete(IS_NO_DELETE);
//                busCertinst.setCreater("test");
                busCertinst.setCreater(SecurityContext.getCurrentUserId());

                busCertinst.setCreateTime(new Date());
                busCertinst.setBusRecordId(unitServiceId);
                busCertinsts.add(busCertinst);
            }
            busCertinstMapper.batchInsertBusCertinst(busCertinsts);

        }

        //有一种情况是：填完证照后有修改了对应部门需要修改证照实例关联的unitInfoId


    }

    @Override
    public JSONObject getBasicInfo() throws Exception {
        //中介服务列表
        AeaImService aeaImService = new AeaImService();
        aeaImService.setRootOrgId(topOrgId);
        List<AeaImService> serviceList = serviceMapper.listAeaImService(aeaImService);

        //证照定义
        List<AeaCert> certList = certMapper.listAeaCert(new AeaCert());
        //证照等级
        AeaImQualLevel query = new AeaImQualLevel();
        query.setRootOrgId(topOrgId);
        List<AeaImQualLevel> qualLevelList = aeaImQualLevelMapper.listAeaImQualLevel(query);

        JSONObject result = new JSONObject();
        result.put("service", serviceList);
        result.put("qualLevel", qualLevelList);
/*
        //事项基础信息表可c参考listServiceTypeAeaItemBasicTreeByPage这个类
        AeaItemBasic aeaItemBasic = new AeaItemBasic();
        aeaItemBasic.setItemNature("8");

        List<AeaItemBasic> itemBasicList = aeaItemBasicMapper.listAeaItemBasic(aeaItemBasic);
*/
        return result;
    }

    @Override
    public List<AeaLinkmanInfo> listLinkmanByServiceUnitInfoId(String serviceId, String unitInfoId) throws Exception {

        List<AeaLinkmanInfo> linkmanInfos = linkmanInfoMapper.getLinkmanListByServiceId(serviceId, unitInfoId);
        return linkmanInfos;
    }

    @Override
    public List<ServiceMatterVo> listServiceMatter(ServiceMatterVo serviceMatterVo, Page page) throws Exception {
        PageHelper.startPage(page);

        List<ServiceMatterVo> matterVos = unitServiceMapper.listAeaImUnitServiceVo(serviceMatterVo);
        if (matterVos != null && matterVos.size() > 0) {
            for (ServiceMatterVo matterVo : matterVos) {
                List<AeaLinkmanInfo> linkmanInfos = linkmanInfoMapper.getLinkmanListByServiceId(matterVo.getServiceId(), matterVo.getUnitInfoId());
                matterVo.setLinkmanInfo(linkmanInfos);

                AeaBusCertinst aeaBusCertinst = new AeaBusCertinst();
                aeaBusCertinst.setBusRecordId(matterVo.getUnitServiceId());
                aeaBusCertinst.setBusTableName("AEA_IM_UNIT_SERVICE");
                List<AeaHiCertinstBVo> certinstBVos = certinstMapper.getAeaHiCertinstByBusCertinst(aeaBusCertinst);
                for (AeaHiCertinstBVo certinstBVo : certinstBVos) {
                    //查询major-id
                    String certinstId = certinstBVo.getCertinstId();
                    AeaImCertinstMajor certinstMajor = new AeaImCertinstMajor();
                    certinstMajor.setCertinstId(certinstId);
                    List<AeaImCertinstMajor> majors = aeaImCertinstMajorMapper.listAeaImCertinstMajor(certinstMajor);
                    if (majors.size() > 0) {
                        List<String> majorList = majors.stream().map(AeaImCertinstMajor::getMajorId).collect(Collectors.toList());
                        certinstBVo.setMajorId(majorList);
                    }

                    List<BscAttFileAndDir> attInfoList = bscAttDetailMapper.searchFileAndDirsSimple(null, SecurityContext.getCurrentOrgId(), "AEA_HI_CERTINST", "CERTINST_ID", new String[]{certinstId});
                    certinstBVo.setCertinstDetail(attInfoList);
                }
                matterVo.setCertinstBVos(certinstBVos);
            }

        }


        return matterVos;
    }

    @Autowired
    private AeaCertMapper aeaCertMapper;

    @Override
    public AeaHiCertinstBVo saveCertificateInfo(AeaHiCertinstBVo certinstBVo, HttpServletRequest req) throws Exception {
        this.setDefaultUser();
        //保存到照片实例表
        AeaHiCertinst hiCertinst = new AeaHiCertinst();
        BeanUtils.copyProperties(hiCertinst, certinstBVo);
        String certinstId = UUID.randomUUID().toString();
        hiCertinst.setCertinstId(certinstId);
        if (StringUtils.isBlank(hiCertinst.getCertinstName())) {
            String certId = hiCertinst.getCertId();
            AeaCert cert = aeaCertMapper.getAeaCertById(certId, topOrgId);
            if (null != cert) {
                hiCertinst.setCertinstName(cert.getCertName());
            }
        }
        if (null == hiCertinst.getIssueDate()) {
            hiCertinst.setIssueDate(hiCertinst.getTermStart());
        }
        hiCertinst.setCreater(SecurityContext.getCurrentUserName());
        hiCertinst.setCreateTime(new Date());
        hiCertinst.setRootOrgId(topOrgId);
        certinstMapper.insertAeaHiCertinst(hiCertinst);

        AeaHiCertinstBVo certinstBVo1 = uploadServiceMatter(certinstBVo.getMajorId(), req, hiCertinst);
        return certinstBVo1;
    }

    @Override
    public void updateServiceMatter(ServiceMatterVo serviceMatterVo) throws Exception {

        //update发布中介服务：
        serviceMatterVo.setAuditFlag("2");
        serviceMatterVo.setModifier(SecurityContext.getCurrentUserName());
        serviceMatterVo.setModifyTime(new Date());
        unitServiceMapper.updateAeaImUnitService(serviceMatterVo);

        //更新记录到业务与证照实例关联表
        List<AeaHiCertinstBVo> certinstBVos = serviceMatterVo.getCertinstBVos();
        busCertinstMapper.deleteAeaBusCertinstByTableNameAndRecordId("AEA_IM_UNIT_SERVICE", serviceMatterVo.getUnitServiceId());
        List<AeaBusCertinst> busCertinsts = new ArrayList();
        if (certinstBVos != null && certinstBVos.size() > 0) {
            for (AeaHiCertinstBVo certinstBVo : certinstBVos) {
                AeaBusCertinst busCertinst = new AeaBusCertinst();
                busCertinst.setBusCertinstId(UUID.randomUUID().toString());
                busCertinst.setBusTableName("AEA_IM_UNIT_SERVICE");
                busCertinst.setPkName("UNIT_SERVICE_ID");
                busCertinst.setAuditFlag("2");
                busCertinst.setBusRecordId(serviceMatterVo.getUnitServiceId());
                busCertinst.setCertinstId(certinstBVo.getCertinstId());
                busCertinst.setIsDelete(IS_NO_DELETE);
                busCertinst.setCreater(SecurityContext.getCurrentUserId());
//                busCertinst.setCreater("test");
                busCertinst.setCreateTime(new Date());
                busCertinsts.add(busCertinst);
            }
            busCertinstMapper.batchInsertBusCertinst(busCertinsts);
        }
    }


    @Override
    public AeaHiCertinstBVo updateAeaHicertinstBvo(AeaHiCertinstBVo certinstBVo, HttpServletRequest req) throws Exception {
        this.setDefaultUser();
        //update照片实例表
        AeaHiCertinst hiCertinst = new AeaHiCertinst();
        BeanUtils.copyProperties(hiCertinst, certinstBVo);
        hiCertinst.setModifyTime(new Date());
        certinstMapper.updateAeaHiCertinst(hiCertinst);

        //先删除在，更新到资质专业和证照实例关联表
        certinstMajorMapper.deleteAeaImCertinstMajorByCertinstId(hiCertinst.getCertinstId());
        AeaHiCertinstBVo certinstBVo1 = uploadServiceMatter(certinstBVo.getMajorId(), req, hiCertinst);
        return certinstBVo1;
    }

    private AeaHiCertinstBVo uploadServiceMatter(List<String> majorList, HttpServletRequest req, AeaHiCertinst hiCertinst) throws Exception {
        //上传证照到mongodb
        FileUtils.uploadFile("AEA_HI_CERTINST", "CERTINST_ID", hiCertinst.getCertinstId(), req);

        if (majorList != null && majorList.size() > 0) {
            for (String majorId : majorList) {
                AeaImCertinstMajor certinstMajor = new AeaImCertinstMajor();
                certinstMajor.setCertinstMajorId(UUID.randomUUID().toString());
                certinstMajor.setCertinstId(hiCertinst.getCertinstId());
                certinstMajor.setMajorId(majorId);
                certinstMajor.setCreater(SecurityContext.getCurrentUserName());
                certinstMajor.setCreateTime(new Date());
                certinstMajor.setIsDelete(IS_NO_DELETE);
                certinstMajorMapper.insertAeaImCertinstMajor(certinstMajor);
            }
        }
        AeaHiCertinstBVo certinstBVo1 = certinstMapper.getAeaHiCertinstVoById(hiCertinst.getCertinstId());
        if (certinstBVo1 != null) {
            certinstBVo1.setMajorId(majorList);
            List<BscAttFileAndDir> attInfoList = bscAttDetailMapper.searchFileAndDirsSimple(null, SecurityContext.getCurrentOrgId(), "AEA_HI_CERTINST", "CERTINST_ID", new String[]{certinstBVo1.getCertinstId()});
            certinstBVo1.setCertinstDetail(attInfoList);
        }
        return certinstBVo1;
    }

    @Override
    public void deleteCertificateInfo(String certinstId) throws Exception {
        certinstMapper.deleteAeaHiCertinst(certinstId);
        certinstMajorMapper.deleteAeaImCertinstMajorByCertinstId(certinstId);
        //删除文件
        List<BscAttFileAndDir> attFileAndDirList = bscAttDetailMapper.searchFileAndDirsSimple(null, SecurityContext.getCurrentOrgId(), "AEA_HI_CERTINST", "CERTINST_ID", new String[]{certinstId});
        List<String> detailIds = new ArrayList<>();
        if (attFileAndDirList != null && attFileAndDirList.size() > 0) {
            for (BscAttFileAndDir fileAndDir : attFileAndDirList) {
                detailIds.add(fileAndDir.getBscAttDetail().getDetailId());
            }

        }
        String[] array = detailIds.toArray(new String[detailIds.size()]);
        FileUtils.deleteFiles(array);
    }

    @Override
    public List<ServiceQualVo> getMajorTreeNode(String serviceId) throws Exception {
        List<ServiceQualVo> result = new ArrayList<>();
        List<AeaImServiceQual> aeaImServiceQuals = aeaImServiceQualMapper.getAeaImServiceQualListByServiceId(serviceId);
        for (AeaImServiceQual aeaImServiceQual : aeaImServiceQuals) {
            String qualId = aeaImServiceQual.getQualId();
            String qualName = aeaImServiceQual.getQualName();
            List<AeaImServiceMajor> serviceMajors = aeaImServiceMajorMapper.getServiceMajorListByQualId(qualId);
            List<ElTreeVo> treeVos = new ArrayList();
            List<AeaImServiceMajor> top = serviceMajors.stream().filter(majors -> Strings.isNullOrEmpty(majors.getParentMajorId())).collect(Collectors.toList());
            for (AeaImServiceMajor m : top) {
                ElTreeVo node = new ElTreeVo();
                node.setId(m.getMajorId());
                node.setName(m.getMajorName());
                List<ElTreeVo> child = serviceMajors.stream().
                        filter(major -> m.getMajorId().equalsIgnoreCase(major.getParentMajorId()))
                        .map(obj -> {
                            ElTreeVo vo = new ElTreeVo();
                            vo.setId(obj.getMajorId());
                            vo.setName(obj.getMajorName());
                            vo.setPid(obj.getParentMajorId());
                            return vo;
                        }).collect(Collectors.toList());
                node.setChild(child);
                treeVos.add(node);
            }
            if (treeVos.size() > 0) {
                ServiceQualVo vo = new ServiceQualVo();
                vo.setQualName(qualName);
                vo.setMajorTree(treeVos);
                AeaImQual aeaImQual = aeaImQualMapper.getAeaImQualById(qualId);
                if (aeaImQual != null && StringUtils.isNotBlank(aeaImQual.getQualLevelId())) {
                    AeaImQualLevel search = new AeaImQualLevel();
                    search.setParentQualLevelId(aeaImQual.getQualLevelId());
                    search.setRootOrgId(topOrgId);
                    List<AeaImQualLevel> qualLevels = aeaImQualLevelMapper.listAeaImQualLevel(search);
                    vo.setLevelList(qualLevels);
                    result.add(vo);
                }
            }
        }
        return result;
    }

    @Override
    public List<AeaItemBasic> listItemBasicByServiceId(String serviceId, Page page) throws Exception {
        PageHelper.startPage(page);
        //根据serviceid找到所有的itembaiscid
        List<AeaImServiceItem> itemServices = serviceItemMapper.getItemServiceByServiceId(serviceId);
        List<AeaItemBasic> itemBasics = new ArrayList<>();
        if (itemServices != null && itemServices.size() > 0) {
            for (AeaImServiceItem item : itemServices) {
                itemBasics.add(aeaItemBasicMapper.getAeaItemBasicByItemVerId(item.getItemVerId(), topOrgId));
            }
        }

        return itemBasics;
    }

    @Override
    public List<AeaItemBasic> listItemBasicByServiceIdNoPage(String serviceId) throws Exception {
        //根据serviceid找到所有的itembaiscid
        AeaImServiceItem aeaImServiceItem = new AeaImServiceItem();
        aeaImServiceItem.setIsDelete("0");
        aeaImServiceItem.setServiceId(serviceId);
        List<AeaImServiceItem> itemServices = serviceItemMapper.listAeaImServiceItem(aeaImServiceItem);
        List<AeaItemBasic> itemBasics = new ArrayList<>();
        if (itemServices != null && itemServices.size() > 0) {
            for (AeaImServiceItem item : itemServices) {
                AeaItemBasic aeaItemBasic = aeaItemBasicMapper.getAeaItemBasicByItemVerId(item.getItemVerId(), topOrgId);
                if (null != aeaItemBasic) {
                    List<AeaItemBasic> result = new ArrayList<>();
                    if (StringUtils.isNotBlank(aeaItemBasic.getItemId())) {
                        List<String> idList = this.getItemIdList(aeaItemBasic.getItemId());
                        if (idList.size() > 0) {
                            AeaItemBasic aib = new AeaItemBasic();
                            aib.setSearchItemIds(idList.toArray(new String[idList.size()]));
                            aib.setRootOrgId(topOrgId);
                            result = aeaItemBasicMapper.listLatestAeaItemBasic(aib);
                        }
                    }
                    aeaItemBasic.setChildAeaItemBasic(result);
                    itemBasics.add(aeaItemBasic);
                }
            }
        }

        return itemBasics;
    }

    private List<String> getItemIdList(String itemId) throws Exception {
        List<String> idList = new ArrayList<>();
        if (StringUtils.isNotBlank(itemId)) {
            AeaItemRelevance search = new AeaItemRelevance();
            search.setChildItemId(itemId);
            List<AeaItemRelevance> relevances = aeaItemRelevanceMapper.listAeaItemRelevance(search);
            if (relevances != null && relevances.size() > 0) {
                for (AeaItemRelevance relevance : relevances) {
                    idList.add(relevance.getParentItemId());
                }
            }
        }
        return idList;
    }


    @Override
    public List<AeaCert> getCertListByserviceId(String serviceId) {
        List<AeaCert> certList = certMapper.getCertByServiceId(serviceId);
        return certList;
    }

    @Override
    public void deleteCertinstAtt(String detailId) {
        FileUtils.deleteFiles(new String[]{detailId});
    }

    @Override
    public List<AeaLinkmanInfo> getLinkManList(String unitInfoId, String serviceId) throws Exception {
        List<AeaLinkmanInfo> list = linkmanInfoMapper.getLinkmanList(unitInfoId, serviceId, null);
        if (list != null && list.size() > 0) {
            List<String> linkmanIds = new ArrayList<>();
            for (AeaLinkmanInfo linkmanInfo : list) {
                linkmanIds.add(linkmanInfo.getLinkmanInfoId());
            }
            String[] ids = linkmanIds.toArray(new String[linkmanIds.size()]);
            List<AeaLinkmanInfo> infos = linkmanInfoMapper.listLinkmanServiceName(ids);
            Map<String, String> serviceNameMap = null;
            Map<String, String> certNameMap = null;
            if (infos != null && infos.size() > 0) {
                //拼接可提供的服务类型
                serviceNameMap = new HashMap<>();
                for (int i = 0, len = infos.size(); i < len; i++) {
                    AeaLinkmanInfo linkmanInfo = infos.get(i);
                    String serviceName = linkmanInfo.getServiceName();
                    String linkmanInfoId = linkmanInfo.getLinkmanInfoId();
                    String temp = serviceNameMap.get(linkmanInfoId);
                    serviceNameMap.put(linkmanInfoId, temp == null ? serviceName : temp + "、" + serviceName);
                }
            }
            List<AeaBusCertinst> busCertinsts = busCertinstMapper.listLinkmanCertinstName(ids);
            if (busCertinsts != null && busCertinsts.size() > 0) {
                //拼接已有资格证书/职称证书
                certNameMap = new HashMap<>();
                for (int i = 0, len = busCertinsts.size(); i < len; i++) {
                    AeaBusCertinst certinst = busCertinsts.get(i);
                    String certName = certinst.getCertinstName();
                    String linkmanInfoId = certinst.getLinkmanInfoId();
                    String temp = certNameMap.get(linkmanInfoId);
                    certNameMap.put(linkmanInfoId, temp == null ? certName : temp + "、" + certName);
                }
            }
            //设置服务和证书
            for (int i = 0, len = list.size(); i < len; i++) {
                AeaLinkmanInfo linkmanInfo = list.get(i);
                String linkmanInfoId = linkmanInfo.getLinkmanInfoId();
                if (serviceNameMap != null) {
                    String serviceNames = serviceNameMap.get(linkmanInfoId);
                    linkmanInfo.setServiceNames(serviceNames);
                }
                if (certNameMap != null) {
                    String certNames = certNameMap.get(linkmanInfoId);
                    linkmanInfo.setCertNames(certNames);
                }
            }
        }
        return list;
    }

}
