package com.augurit.aplanmis.common.service.admin.cert.impl;

import com.augurit.agcloud.bsc.domain.BscAttDir;
import com.augurit.agcloud.bsc.mapper.BscAttDirMapper;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.AeaCertMapper;
import com.augurit.aplanmis.common.mapper.AeaCertTypeMapper;
import com.augurit.aplanmis.common.service.admin.cert.AeaCertAdminService;
import com.augurit.aplanmis.common.service.instance.AeaHiIteminstService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ZhangXinhui
 * @date 2019/7/29 029 14:58
 * @desc
 **/
@Service
@Transactional
public class AeaCertAdminServiceImpl implements AeaCertAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaCertAdminServiceImpl.class);

    @Autowired
    private AeaCertTypeMapper aeaCertTypeMapper;

    @Autowired
    private AeaCertMapper aeaCertMapper;

    @Autowired
    private BscAttDirMapper bscAttDirMapper;

    @Autowired
    private AeaHiIteminstService aeaHiIteminstService;

    @Override
    public void deleteAeaCertById(String id) {

        if (StringUtils.isNotBlank(id)) {
            aeaCertMapper.deleteById(id);
        }
    }

    @Override
    public void batchDeleteCertByIds(String[] ids) {

        if (ids != null && ids.length > 0) {
            aeaCertMapper.batchDeleteCertByCertIds(ids);
        }
    }

    @Override
    public void saveAeaCert(AeaCert aeaCert) {

        aeaCert.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaCert.setCreater(SecurityContext.getCurrentUserId());
        aeaCert.setCreateTime(LocalDateTime.now());
        aeaCert.setIsDeleted(DeletedStatus.NOT_DELETED);
        aeaCertMapper.insertOne(aeaCert);
    }

    @Override
    public void updateAeaCert(AeaCert aeaCert) {

        aeaCert.setModifier(SecurityContext.getCurrentUserId());
        aeaCert.setModifyTime(LocalDateTime.now());
        aeaCertMapper.updateOne(aeaCert);
    }


    @Override
    public PageInfo<AeaCert> listAeaCert(AeaCert aeaCert, Page page) {

        aeaCert.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaCert> list = aeaCertMapper.listAeaCert(aeaCert);
        logger.debug("成功执行分页查询！");
        return new PageInfo<>(list);
    }

    @Override
    public boolean checkUniqueCertCode(String certId, String certCode, String rootOrgId) {

        Integer count = aeaCertMapper.checkUniqueCertCode(certId, certCode, rootOrgId);
        return count == null || count <= 0;
    }

    @Override
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

    @Override
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

    @Override
    public List<ZtreeNode> gtreeTypeCert(String rootOrgId) {

        if (StringUtils.isBlank(rootOrgId)) {
            rootOrgId = SecurityContext.getCurrentOrgId();
        }
        // 电子证照分类
        AeaCertType scertType = new AeaCertType();
        scertType.setRootOrgId(rootOrgId);
        List<AeaCertType> certTypes = aeaCertTypeMapper.listAeaCertType(scertType);
        // 电子证照数据
        AeaCert scert = new AeaCert();
        scert.setRootOrgId(rootOrgId);
        List<AeaCert> certs = aeaCertMapper.listAeaCert(scert);
        List<ZtreeNode> allNodes = new ArrayList<>();
        ZtreeNode rootNode = new ZtreeNode();
        rootNode.setId("root");
        rootNode.setName("电子证照库");
        rootNode.setType("root");
        rootNode.setIsParent(true);
        rootNode.setOpen(true);
        rootNode.setNocheck(true);
        allNodes.add(rootNode);
        if (certTypes != null && certTypes.size() > 0) {
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

    @Override
    public Long getMaxCertSortNo(String rootOrgId) {

        if (StringUtils.isBlank(rootOrgId)) {
            rootOrgId = SecurityContext.getCurrentOrgId();
        }
        Long sortNo = aeaCertMapper.getMaxCertSortNo(rootOrgId);
        return sortNo == null ? 1L : sortNo + 1;
    }

    @Override
    public AeaCert getAeaCertById(String id) {

        if (StringUtils.isBlank(id)) {
            throw new InvalidParameterException(id);
        }
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaCertMapper.selectOneById(id);
    }

    @Override
    public PageInfo<AeaCert> listStageNoSelectCertByPage(AeaParIn aeaParIn, Page page) {

        aeaParIn.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaCert> list = aeaCertMapper.listStageNoSelectCert(aeaParIn);
        return new PageInfo<>(list);
    }

    @Override
    public List<AeaCert> listStageNoSelectCert(AeaParIn aeaParIn) {

        aeaParIn.setRootOrgId(SecurityContext.getCurrentOrgId());
        return aeaCertMapper.listStageNoSelectCert(aeaParIn);
    }

    @Override
    public PageInfo<AeaCert> listItemNoSelectCertByPage(AeaItemInout inout, Page page) {

        inout.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaCert> list = aeaCertMapper.listItemNoSelectCert(inout);
        return new PageInfo<>(list);
    }

    @Override
    public List<AeaCert> listItemNoSelectCert(AeaItemInout inout) {

        inout.setRootOrgId(SecurityContext.getCurrentOrgId());
        return aeaCertMapper.listItemNoSelectCert(inout);
    }

    @Override
    public List<AeaCert> getOutputCertsByIteminstId(String iteminstId) throws Exception {

        if (StringUtils.isBlank(iteminstId)) return null;

        AeaHiIteminst iteminst = aeaHiIteminstService.getAeaHiIteminstById(iteminstId);

        if (iteminst == null) return null;

        return aeaCertMapper.getOutputCertsByItemVerId(iteminst.getItemVerId(), SecurityContext.getCurrentOrgId());
    }
}
