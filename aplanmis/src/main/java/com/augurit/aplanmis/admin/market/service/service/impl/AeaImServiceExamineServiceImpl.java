package com.augurit.aplanmis.admin.market.service.service.impl;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.bsc.mapper.BscAttDetailMapper;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.admin.market.service.service.AeaImServiceExamineService;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.vo.AeaHiCertinstBVo;
import com.augurit.aplanmis.common.vo.ElTreeVo;
import com.augurit.aplanmis.common.vo.ServiceMatterVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * -Service服务接口实现类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:thinkpad</li>
 * <li>创建时间：2019-06-10 13:49:01</li>
 * </ul>
 */
@Service
@Transactional
public class AeaImServiceExamineServiceImpl implements AeaImServiceExamineService {


    private static Logger logger = LoggerFactory.getLogger(AeaImServiceExamineServiceImpl.class);
    @Autowired
    private AeaImUnitServiceMapper unitServiceMapper;
    @Autowired
    private AeaImServiceItemMapper serviceItemMapper;
    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;
    @Autowired
    private AeaLinkmanInfoMapper linkmanInfoMapper;
    @Autowired
    private AeaHiCertinstMapper certinstMapper;
    @Autowired
    private BscAttDetailMapper bscAttDetailMapper;
    @Autowired
    private AeaImServiceMajorMapper serviceMajorMapper;
    @Autowired
    private AeaImQualMapper qualMapper;
    @Autowired
    private AeaBusCertinstMapper aeaBusCertinstMapper;

    @Value("${dg.sso.access.platform.org.top-org-id}")
    protected String topOrgId;


    @Override
    public PageInfo<ServiceMatterVo> listServiceMatter(ServiceMatterVo serviceMatterVo, Page page) throws Exception {
        PageHelper.startPage(page);
        List<ServiceMatterVo> serviceMatterVos = unitServiceMapper.listAeaImUnitServiceVo(serviceMatterVo);
        //获取证书列表//拼接证照名
        if (serviceMatterVos != null && serviceMatterVos.size() > 0) {
            for (ServiceMatterVo matterVo : serviceMatterVos) {
                List<AeaHiCertinstBVo> certinstBVos = certinstMapper.getAeaHiCertinstByUnitServiceId(matterVo.getUnitServiceId());
                if (certinstBVos.size() > 0) {
                    String certinstStr = certinstBVos.stream().map(s -> {
                        String certinstName = s.getCertinstName();
                        String qualLevelName = s.getQualLevelName();
                        if (StringUtils.isNotBlank(qualLevelName)) {
                            certinstName = String.format(certinstName + "(%s)", qualLevelName);
                        }
                        return certinstName;
                    }).collect(Collectors.joining("、"));
                    matterVo.setCertinstStr(certinstStr);
                }
                /*if (certinstBVos != null && certinstBVos.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (AeaHiCertinstBVo certinst : certinstBVos) {
                        sb.append(certinst.getCertinstName());
                        if (certinst.getQualLevelName() != null && certinst.getQualLevelName() != "") {
                            sb.append("(").append(certinst.getQualLevelName()).append(")");
                        }
                        sb.append("、");
                    }

                    matterVo.setCertinstStr(sb.toString().substring(0, sb.length() - 1));
                }*/
            }


        }
        return new PageInfo<ServiceMatterVo>(serviceMatterVos);
    }

    @Override
    public ServiceMatterVo getServiceMatterServiceByUnitServiceId(String unitServiceId) throws Exception {
        ServiceMatterVo vo = unitServiceMapper.getServiceMatterVoByUnitserviceId(unitServiceId);

        //得到所有中介服务事项
        List<AeaImServiceItem> itemServices = serviceItemMapper.getItemServiceByServiceId(vo.getServiceId());
        List<AeaItemBasic> itemBasics = new ArrayList<>();
        if (itemServices != null && itemServices.size() > 0) {
            for (AeaImServiceItem item : itemServices) {
                itemBasics.add(aeaItemBasicMapper.getAeaItemBasicByItemVerId(item.getItemVerId(), topOrgId));
            }
        }
        vo.setAgentItemServices(itemBasics);
        //根据serviceId找出从业人员
        List<AeaLinkmanInfo> linkmanList = linkmanInfoMapper.getLinkmanListByServiceId(vo.getServiceId(), vo.getUnitInfoId());
        if (linkmanList != null && linkmanList.size() > 0) {
            // 获取从业人员证书
            for (AeaLinkmanInfo aeaLinkmanInfo : linkmanList) {
                AeaBusCertinst aeaBusCertinst = new AeaBusCertinst();
                aeaBusCertinst.setBusRecordId(aeaLinkmanInfo.getServiceLinkmanId());
                aeaBusCertinst.setBusTableName("aea_im_service_linkman");
                List<AeaHiCertinstBVo> certinsts = certinstMapper.getAeaHiCertinstByBusCertinst(aeaBusCertinst);
                for (AeaHiCertinstBVo certinstBVo : certinsts) {
                    List<BscAttFileAndDir> attInfoList = bscAttDetailMapper.searchFileAndDirsSimple(null, SecurityContext.getCurrentOrgId(), "AEA_HI_CERTINST", "CERTINST_ID", new String[]{certinstBVo.getCertinstId()});
                    certinstBVo.setCertinstDetail(attInfoList);
                }
                aeaLinkmanInfo.setCertinsts(certinsts);
            }
        }

        //获取证书列表
        List<AeaHiCertinstBVo> certinstBVos = certinstMapper.getAeaHiCertinstByUnitServiceId(vo.getUnitServiceId());
        String topOrgId = SecurityContext.getCurrentOrgId();
        for (AeaHiCertinstBVo certinstBVo : certinstBVos) {
            List<BscAttFileAndDir> attInfoList = bscAttDetailMapper.searchFileAndDirsSimple(null, topOrgId, "AEA_HI_CERTINST", "CERTINST_ID", new String[]{certinstBVo.getCertinstId()});
            certinstBVo.setCertinstDetail(attInfoList);
            List<AeaImServiceMajor> serviceMajors = serviceMajorMapper.getServiceMajorListByCertinstId(certinstBVo.getCertinstId());
            certinstBVo.setServiceMajor(serviceMajors);
            //封装ztree，专业树
            if (serviceMajors != null && serviceMajors.size() > 0) {
//                List<ZtreeNode> tree = changeZtree(serviceMajors);
                List<ElTreeVo> tree = changeElTree(serviceMajors);
                certinstBVo.setMajorElTree(tree);
            }
        }

        vo.setLinkmanInfo(linkmanList);
        vo.setCertinstBVos(certinstBVos);
        return vo;
    }

    private List<ElTreeVo> changeElTree(List<AeaImServiceMajor> serviceMajors) throws Exception {
        Map<String, List<AeaImServiceMajor>> groupServiceMajor = serviceMajors.stream().collect(Collectors.groupingBy(AeaImServiceMajor::getQualId));
        List<AeaImQual> quals = qualMapper.listAeaImQual(new AeaImQual());
        List<ElTreeVo> tree = new ArrayList<>();
        if (groupServiceMajor != null && groupServiceMajor.size() > 0) {
            for (String qid : groupServiceMajor.keySet()) {
                ElTreeVo root = new ElTreeVo();
                root.setId(qid);
                for (AeaImQual qual : quals) {
                    if (qid.equalsIgnoreCase(qual.getQualId())) {
                        root.setName(qual.getQualName());
                        break;
                    }
                }
                root.setPid("root");
                List<ElTreeVo> child = new ArrayList<>();
                for (AeaImServiceMajor sm : groupServiceMajor.get(qid)) {
                    ElTreeVo node = new ElTreeVo();
                    node.setId(sm.getMajorId());
                    node.setPid(sm.getpId() == "" || sm.getpId() == null ? qid : sm.getpId());
                    node.setName(sm.getMajorName());
                    child.add(node);
                }
                root.setChild(child);
                tree.add(root);
            }
        }
        return tree;
    }

    //没用到
    private List<ZtreeNode> changeZtree(List<AeaImServiceMajor> serviceMajors) throws Exception {
        Map<String, List<AeaImServiceMajor>> groupServiceMajor = serviceMajors.stream().collect(Collectors.groupingBy(AeaImServiceMajor::getQualId));
        List<AeaImQual> quals = qualMapper.listAeaImQual(new AeaImQual());
        List<ZtreeNode> tree = new ArrayList<>();
        if (groupServiceMajor != null && groupServiceMajor.size() > 0) {
            for (String qid : groupServiceMajor.keySet()) {
                ZtreeNode root = new ZtreeNode();
                root.setId(qid);
                for (AeaImQual qual : quals) {
                    if (qid.equalsIgnoreCase(qual.getQualId())) {
                        root.setName(qual.getQualName());
                        break;
                    }
                }
                root.setpId("root");
                root.setNocheck(false);
                root.setOpen(true);
                tree.add(root);

                for (AeaImServiceMajor sm : groupServiceMajor.get(qid)) {
                    ZtreeNode node = new ZtreeNode();
                    node.setId(sm.getMajorId());
                    node.setpId(sm.getpId() == "" || sm.getpId() == null ? qid : sm.getpId());
                    node.setName(sm.getMajorName());
                    node.setNocheck(false);
                    node.setOpen(false);
                    tree.add(node);
                }

            }
        }
        return tree;
    }

    @Override
    public PageInfo<AeaHiCertinstBVo> listCertinstByUnitServiceid(String unitServiceId, Page page) throws Exception {
        PageHelper.startPage(page);
        //获取证书列表
        List<AeaHiCertinstBVo> certinstBVos = certinstMapper.getAeaHiCertinstByUnitServiceId(unitServiceId);
        String topOrgId = SecurityContext.getCurrentOrgId();
        for (AeaHiCertinstBVo certinstBVo : certinstBVos) {
            List<BscAttFileAndDir> attInfoList = bscAttDetailMapper.searchFileAndDirsSimple(null, topOrgId, "AEA_HI_CERTINST", "CERTINST_ID", new String[]{certinstBVo.getCertinstId()});
            certinstBVo.setCertinstDetail(attInfoList);
        }

        List<AeaImQual> quals = qualMapper.listAeaImQual(new AeaImQual());

        return new PageInfo<AeaHiCertinstBVo>(certinstBVos);
    }

    @Override
    public List<AeaHiCertinstBVo> listCertinstByUnitServiceId(String unitServiceId) throws Exception {
        //获取证书列表
        List<AeaHiCertinstBVo> certinstBVos = certinstMapper.getAeaHiCertinstByUnitServiceId(unitServiceId);
        String topOrgId = SecurityContext.getCurrentOrgId();
        for (AeaHiCertinstBVo certinstBVo : certinstBVos) {
            List<BscAttFileAndDir> attInfoList = bscAttDetailMapper.searchFileAndDirsSimple(null, topOrgId, "AEA_HI_CERTINST", "CERTINST_ID", new String[]{certinstBVo.getCertinstId()});
            certinstBVo.setCertinstDetail(attInfoList);
        }
        return certinstBVos;
    }

    @Override
    public PageInfo<AeaItemBasic> listServiceItemServiceid(String serviceId, Page page) {
        PageHelper.startPage(page);
        //根据serviceid找到所有的itembaiscid
        List<AeaImServiceItem> itemServices = serviceItemMapper.getItemServiceByServiceId(serviceId);
        List<AeaItemBasic> itemBasics = new ArrayList<>();
        if (itemServices != null && itemServices.size() > 0) {
            for (AeaImServiceItem item : itemServices) {
                itemBasics.add(aeaItemBasicMapper.getAeaItemBasicByItemVerId(item.getItemVerId(), topOrgId));
            }
        }

        return new PageInfo<AeaItemBasic>(itemBasics);
    }

    @Override
    public List<AeaItemBasic> listServiceItemByServiceId(String serviceId) {
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
    public void examineService(ServiceMatterVo serviceMatterVo) throws Exception {
        AeaImUnitService unitService = new AeaImUnitService();
//        BeanUtils.copyProperties(unitService, serviceMatterVo);
        unitService.setUnitServiceId(serviceMatterVo.getUnitServiceId());
        unitService.setMemo(serviceMatterVo.getMemo());
        unitService.setAuditFlag(serviceMatterVo.getAuditFlag());
        unitService.setAuditTime(new Date());
        unitServiceMapper.updateAeaImUnitService(unitService);

        AeaBusCertinst aeaBusCertinst = new AeaBusCertinst();
        aeaBusCertinst.setBusRecordId(serviceMatterVo.getUnitServiceId());
        aeaBusCertinst.setBusTableName("AEA_IM_UNIT_SERVICE");
        aeaBusCertinst.setAuditFlag(serviceMatterVo.getAuditFlag());
        aeaBusCertinst.setMemo(serviceMatterVo.getMemo());
        aeaBusCertinstMapper.updateAeaBusCertinstByAudit(aeaBusCertinst);
    }

    @Override
    public AeaHiCertinstBVo getCertinstById(String certinstId) throws Exception {
        AeaHiCertinstBVo detail = certinstMapper.getAeaHiCertinstVoById(certinstId);
        //所有附件信息
        String topOrgId = SecurityContext.getCurrentOrgId();
        List<BscAttFileAndDir> attInfoList = bscAttDetailMapper.searchFileAndDirsSimple(null, topOrgId, "AEA_HI_CERTINST", "CERTINST_ID", new String[]{detail.getCertinstId()});
        detail.setCertinstDetail(attInfoList);
        List<AeaImServiceMajor> serviceMajors = serviceMajorMapper.getServiceMajorListByCertinstId(certinstId);

        detail.setServiceMajor(serviceMajors);

        return detail;
    }

    @Override
    public Map getMajorTreeNode(String serviceId, String certinstId) throws Exception {
//        用ztree
        List<AeaImServiceMajor> serviceMajors = serviceMajorMapper.getServiceMajorListByCertinstId(certinstId);
        Map<String, List<AeaImServiceMajor>> groupServiceMajor = serviceMajors.stream().collect(Collectors.groupingBy(AeaImServiceMajor::getQualId));
        List<AeaImQual> quals = qualMapper.listAeaImQual(new AeaImQual());
        List<ZtreeNode> tree = new ArrayList<>();
        if (groupServiceMajor != null && groupServiceMajor.size() > 0) {
            for (String qid : groupServiceMajor.keySet()) {
                ZtreeNode root = new ZtreeNode();
                root.setId(qid);
                for (AeaImQual qual : quals) {
                    if (qid.equalsIgnoreCase(qual.getQualId())) {
                        root.setName(qual.getQualName());
                        break;
                    }
                }
                root.setpId("root");
                root.setNocheck(false);
                root.setOpen(true);
                tree.add(root);

                for (AeaImServiceMajor sm : groupServiceMajor.get(qid)) {
                    ZtreeNode node = new ZtreeNode();
                    node.setId(sm.getMajorId());
                    node.setpId(sm.getpId() == "" || sm.getpId() == null ? qid : sm.getpId());
                    node.setName(sm.getMajorName());
                    node.setNocheck(false);
                    node.setOpen(false);
                    tree.add(node);
                }

            }
        }

        Map<String, List<ZtreeNode>> result = new HashMap<>();
        result.put("tree", tree);
        return result;
    }
}

