package com.augurit.aplanmis.admin.market.serviceLinkman.service.impl;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.bsc.mapper.BscAttDetailMapper;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.admin.market.serviceLinkman.service.AeaImServiceLinkmanService;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.vo.AeaHiCertinstBVo;
import com.augurit.aplanmis.common.vo.AeaImMajorQualVo;
import com.augurit.aplanmis.common.vo.ElTreeVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * -Service服务接口实现类
 */
@Service
@Transactional
public class AeaImServiceLinkmanServiceImpl implements AeaImServiceLinkmanService {

    private static Logger logger = LoggerFactory.getLogger(AeaImServiceLinkmanServiceImpl.class);

    @Autowired
    private AeaImServiceLinkmanMapper aeaImServiceLinkmanMapper;
    @Autowired
    private AeaHiCertinstMapper aeaHiCertinstMapper;
    @Autowired
    private AeaImUnitServiceMapper aeaImUnitServiceMapper;
    @Autowired
    private AeaImServiceMapper aeaImServiceMapper;
    @Autowired
    private AeaUnitInfoMapper aeaUnitInfoMapper;
    @Autowired
    private AeaImServiceMajorMapper serviceMajorMapper;
    @Autowired
    private BscAttDetailMapper bscAttDetailMapper;
    @Autowired
    private AeaImQualMapper qualMapper;
    @Autowired
    private AeaLinkmanInfoMapper aealinkmanInfoMapper;
    @Autowired
    private AeaImCertinstMajorMapper aeaImCertinstMajorMapper;
    @Autowired
    private AeaBusCertinstMapper aeaBusCertinstMapper;

    public void saveAeaImServiceLinkman(AeaImServiceLinkman aeaImServiceLinkman) throws Exception {
        aeaImServiceLinkmanMapper.insertAeaImServiceLinkman(aeaImServiceLinkman);
    }

    public void updateAeaImServiceLinkman(AeaImServiceLinkman aeaImServiceLinkman) throws Exception {
        aeaImServiceLinkmanMapper.updateAeaImServiceLinkman(aeaImServiceLinkman);
    }

    public void deleteAeaImServiceLinkmanById(String id) throws Exception {
        if (id == null)
            throw new InvalidParameterException(id);
        aeaImServiceLinkmanMapper.deleteAeaImServiceLinkman(id);
    }

    public EasyuiPageInfo<AeaImServiceLinkman> listAeaImServiceLinkman(AeaImServiceLinkman aeaImServiceLinkman, Page page) throws Exception {
        List<AeaImServiceLinkman> list = this.getAeaImServiceLinkmanList(aeaImServiceLinkman, page);
        logger.debug("成功执行分页查询！！");
        return PageHelper.toEasyuiPageInfo(new PageInfo(list));
    }

    public List<AeaImServiceLinkman> getAeaImServiceLinkmanList(AeaImServiceLinkman aeaImServiceLinkman, Page page) throws Exception {
        PageHelper.startPage(page);
        List<AeaImServiceLinkman> list = aeaImServiceLinkmanMapper.listAeaImServiceLinkman(aeaImServiceLinkman);
        for (int i = 0; i < list.size(); i++) {
            try {
                AeaImServiceLinkman serviceLinkman = list.get(i);
                // 获取证书列表
                AeaBusCertinst aeaBusCertinst = new AeaBusCertinst();
                aeaBusCertinst.setBusTableName("aea_im_service_linkman");
                aeaBusCertinst.setBusRecordId(serviceLinkman.getServiceLinkmanId());
                List<AeaHiCertinstBVo> certinstList = aeaHiCertinstMapper.getAeaHiCertinstByBusCertinst(aeaBusCertinst);
                serviceLinkman.setCertinst(certinstList);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
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

    public AeaImServiceLinkman getAeaImServiceLinkmanById(String serviceLinkmanId) throws Exception {
        AeaImServiceLinkman aeaImServiceLinkman = aeaImServiceLinkmanMapper.getAeaImServiceLinkmanDetailById(serviceLinkmanId);
        if (aeaImServiceLinkman != null) {
            //获取单位信息
            AeaUnitInfo unitInfo = aeaUnitInfoMapper.getAeaUnitInfoById(aeaImServiceLinkman.getUnitInfoId());
            aeaImServiceLinkman.setUnitInfo(unitInfo);
            // 获取单位联系人
            List<AeaLinkmanInfo> linkmanInfos = aeaUnitInfoMapper.getLinkmanByUnitInfoId(aeaImServiceLinkman.getUnitInfoId());
            if (linkmanInfos != null && linkmanInfos.size() > 0) {
                aeaImServiceLinkman.getUnitInfo().setContact(linkmanInfos.get(0).getLinkmanName());
                aeaImServiceLinkman.getUnitInfo().setMobile(linkmanInfos.get(0).getLinkmanMobilePhone());
                aeaImServiceLinkman.getUnitInfo().setEmail(linkmanInfos.get(0).getLinkmanMail());
            }

            // 获取证书列表
            AeaBusCertinst aeaBusCertinst = new AeaBusCertinst();
            aeaBusCertinst.setBusTableName("aea_im_service_linkman");
            aeaBusCertinst.setBusRecordId(serviceLinkmanId);
            List<AeaHiCertinstBVo> certinstList = aeaHiCertinstMapper.getAeaHiCertinstByBusCertinst(aeaBusCertinst);
            if (certinstList != null && certinstList.size() > 0) {
                for (AeaHiCertinstBVo aeaHiCertinstBVo : certinstList) {
                    // 查询证书所属专业
                    List<AeaImQual> qualMajorList = aeaImCertinstMajorMapper.listQualMajorByCertinstId(aeaHiCertinstBVo.getCertinstId());
                    if (qualMajorList != null && qualMajorList.size() > 0) {
                        for (AeaImQual aeaImQual : qualMajorList) {
                            List<AeaImMajorQualVo> majorList = new ArrayList<AeaImMajorQualVo>();
                            List<AeaImMajorQualVo> aeaImMajorQualVoList = aeaImQual.getAeaImMajorQualVoList();
                            for (AeaImMajorQualVo aeaImMajorQualVo : aeaImMajorQualVoList) {
                                if (StringUtils.isBlank(aeaImMajorQualVo.getParentMajorId())) {
                                    aeaImMajorQualVo.setChildren(getMajorChildrenList(aeaImMajorQualVo.getMajorId(), aeaImMajorQualVoList));
                                    majorList.add(aeaImMajorQualVo);
                                }
                            }

                            aeaImQual.setAeaImMajorQualVoList(majorList);
                        }
                        aeaHiCertinstBVo.setQualMajorList(qualMajorList);
                    }

                    // 获取证书附件
                    List<BscAttFileAndDir> attInfoList = bscAttDetailMapper.searchFileAndDirsSimple(null, SecurityContext.getCurrentOrgId(), "AEA_HI_CERTINST", "CERTINST_ID", new String[]{aeaHiCertinstBVo.getCertinstId()});
                    aeaHiCertinstBVo.setCertinstDetail(attInfoList);
                }

                aeaImServiceLinkman.setCertinst(certinstList);

            }

        }
        return aeaImServiceLinkman;
    }

    public List<AeaImServiceLinkman> listAeaImServiceLinkman(AeaImServiceLinkman aeaImServiceLinkman) throws Exception {
        List<AeaImServiceLinkman> list = aeaImServiceLinkmanMapper.listAeaImServiceLinkman(aeaImServiceLinkman);
        logger.debug("成功执行查询list！！");
        return list;
    }

    public PageInfo<AeaLinkmanInfo> listAeaImServiceLinkmanByServiceId(String serviceId, Page page) throws Exception {
        PageHelper.startPage(page);
        List<AeaLinkmanInfo> list = aeaImServiceLinkmanMapper.listAeaImServiceLinkmanByServiceId(serviceId);
        for (int i = 0; i < list.size(); i++) {
            AeaLinkmanInfo linkmanInfo = new AeaLinkmanInfo();
            linkmanInfo = list.get(i);
            List<AeaHiCertinstBVo> certinstlist = aeaHiCertinstMapper.getAeaHiCertinstVoByLinkmanInfoId(linkmanInfo.getLinkmanInfoId());
            linkmanInfo.setCertinsts(certinstlist);
            List<String> certinstIdList = new ArrayList<String>();
            for (AeaHiCertinst certinst : certinstlist) {
                certinstIdList.add(certinst.getCertinstId());
            }
            String[] certinstIdstr = certinstIdList.toArray(new String[certinstIdList.size()]);

            linkmanInfo.setBscAttDetails(aeaHiCertinstMapper.getFilesByRecordIds("aea_hi_certinst", "CERTINST_ID", null, certinstIdstr));
        }
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaLinkmanInfo>(list);
    }

    public void updateServiceLinkmanAudit(String serviceLinkmanId, String auditFlag, String memo) throws Exception {

        AeaImServiceLinkman aeaImServiceLinkman = new AeaImServiceLinkman();
        aeaImServiceLinkman.setAuditFlag(auditFlag);
        aeaImServiceLinkman.setMemo(memo);
        aeaImServiceLinkman.setAuditTime(new Date());
        aeaImServiceLinkman.setServiceLinkmanId(serviceLinkmanId);
        aeaImServiceLinkmanMapper.updateAeaImServiceLinkman(aeaImServiceLinkman);

        AeaBusCertinst aeaBusCertinst = new AeaBusCertinst();
        aeaBusCertinst.setBusRecordId(serviceLinkmanId);
        aeaBusCertinst.setBusTableName("aea_im_service_linkman");
        aeaBusCertinst.setAuditFlag(auditFlag);
        aeaBusCertinst.setMemo(memo);
        aeaBusCertinstMapper.updateAeaBusCertinstByAudit(aeaBusCertinst);

    }

    private List<AeaImMajorQualVo> getMajorChildrenList(String parentMajorId, List<AeaImMajorQualVo> majorList) {
        List<AeaImMajorQualVo> children = new ArrayList<AeaImMajorQualVo>();

        for (AeaImMajorQualVo aeaImMajorQualVo : majorList) {
            if (parentMajorId != null && parentMajorId.equals(aeaImMajorQualVo.getParentMajorId())) {
                aeaImMajorQualVo.setChildren(getMajorChildrenList(aeaImMajorQualVo.getMajorId(), majorList));
                children.add(aeaImMajorQualVo);
            }
        }
        return children;
    }
}

