package com.augurit.aplanmis.common.service.admin.cert.impl;

import com.augurit.agcloud.bsc.util.CommonConstant;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ActiveStatus;
import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.domain.AeaCertType;
import com.augurit.aplanmis.common.mapper.AeaCertMapper;
import com.augurit.aplanmis.common.mapper.AeaCertTypeMapper;
import com.augurit.aplanmis.common.service.admin.cert.AeaCertTypeAdminService;
import com.augurit.aplanmis.common.utils.EuiCertTypeUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ZhangXinhui
 * @date 2019/7/29 029 11:55
 * @desc
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class AeaCertTypeAdminServiceImpl implements AeaCertTypeAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaCertTypeAdminServiceImpl.class);

    @Autowired
    AeaCertTypeMapper aeaCertTypeMapper;

    @Autowired
    private AeaCertMapper aeaCertMapper;

    @Override
    public PageInfo<AeaCertType> listAeaCertType(AeaCertType aeaCertType, Page page) {

        aeaCertType.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaCertType> list = aeaCertTypeMapper.listAeaCertType(aeaCertType);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public Long getMaxSortNo(String parentId, String rootOrgId) {

        if(StringUtils.isBlank(rootOrgId)){
            rootOrgId = SecurityContext.getCurrentOrgId();
        }
        Long sortNo = aeaCertTypeMapper.getMaxSortNo(parentId, rootOrgId);
        return sortNo == null ? 1L : sortNo + 1;
    }

    @Override
    public boolean checkUniqueCertTypeCode(String certTypeId, String typeCode, String rootOrgId) {

        if(StringUtils.isBlank(rootOrgId)){
            rootOrgId = SecurityContext.getCurrentOrgId();
        }
        Integer count = aeaCertTypeMapper.checkUniqueCertTypeCode(certTypeId, typeCode, rootOrgId);
        return count == null || count <= 0;
    }

    @Override
    public void updateAeaCertType(AeaCertType aeaCertType) {

        aeaCertType.setModifier(SecurityContext.getCurrentUserId());
        aeaCertType.setModifyTime(new Date());
        aeaCertTypeMapper.updateOne(aeaCertType);
    }

    @Override
    public void saveAeaCertType(AeaCertType aeaCertType) {

        String rootOrgId = SecurityContext.getCurrentOrgId();
        aeaCertType.setSubCount(0L);
        aeaCertType.setSortNo(getMaxSortNo(aeaCertType.getParentTypeId(), rootOrgId));
        aeaCertType.setTypeSeq(getTypeSeq(aeaCertType.getCertTypeId(), aeaCertType.getParentTypeId()));
        aeaCertType.setIsActive(ActiveStatus.ACTIVE.getValue());
        aeaCertType.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
        aeaCertType.setCreater(SecurityContext.getCurrentUserId());
        aeaCertType.setRootOrgId(rootOrgId);
        aeaCertType.setCreateTime(new Date());
        aeaCertTypeMapper.insertOne(aeaCertType);
    }

    @Override
    public String getTypeSeq(String certTypeId, String parentCertTypeId) {

        if (StringUtils.isNotBlank(parentCertTypeId)) {
            //获取上级的序列
            AeaCertType pcertType = aeaCertTypeMapper.selectOneById(parentCertTypeId);
            if (pcertType != null) {
                String parentSeq = pcertType.getTypeSeq();
                if (StringUtils.isNotBlank(parentSeq)) {
                    return parentSeq + certTypeId + CommonConstant.SEQ_SEPARATOR;
                } else {
                    return CommonConstant.SEQ_SEPARATOR + certTypeId + CommonConstant.SEQ_SEPARATOR;
                }
            } else {
                return CommonConstant.SEQ_SEPARATOR + certTypeId + CommonConstant.SEQ_SEPARATOR;
            }
        } else {
            return CommonConstant.SEQ_SEPARATOR + certTypeId + CommonConstant.SEQ_SEPARATOR;
        }
    }

    @Override
    public void deleteAeaCertTypeById(String id) {

        if (StringUtils.isNotBlank(id)) {
            // 先获取所有子级
            List<AeaCertType> certTypes = aeaCertTypeMapper.listSelfAndChildCertTypeById(id);
            if (certTypes != null && certTypes.size() > 0) {
                List<String> certTypeIds = new ArrayList<>();
                String[] idArr = new String[certTypes.size()];
                for (AeaCertType certType : certTypes) {
                    certTypeIds.add(certType.getCertTypeId());
                }
                // 先删除证照
                aeaCertMapper.batchDeleteCertByCertTypeIds(certTypeIds.toArray(idArr));
                //  删除分类
                aeaCertTypeMapper.batchDeleteCertType(certTypeIds.toArray(idArr));
            }
        }
    }

    @Override
    public void batchDeleteCertType(String[] ids) {

        if (ids != null && ids.length > 0) {
            for (String id : ids) {
                deleteAeaCertTypeById(id);
            }
        }
    }

    @Override
    public void changIsActiveState(String id) {

        aeaCertTypeMapper.changIsActiveState(id);
    }

    @Override
    public AeaCertType getAeaCertTypeById(String id) {

        if (StringUtils.isNotBlank(id)) {
            logger.debug("根据ID获取Form对象，ID为：{}", id);
            return aeaCertTypeMapper.selectOneById(id);
        }
        return null;
    }

    @Override
    public List<ElementUiRsTreeNode> gtreeCertType(String rootOrgId){

        if(StringUtils.isBlank(rootOrgId)){
            rootOrgId = SecurityContext.getCurrentOrgId();
        }
        List<ElementUiRsTreeNode> list = new ArrayList();
        ElementUiRsTreeNode rootNode = new ElementUiRsTreeNode();
        rootNode.setId("root");
        rootNode.setLabel("电子证照库");
        rootNode.setType("root");
        // 电子证照分类
        AeaCertType scertType = new AeaCertType();
        scertType.setRootOrgId(rootOrgId);
        List<AeaCertType> certTypes = aeaCertTypeMapper.listAeaCertType(scertType);
        if(certTypes!=null&&certTypes.size()>0){
            List<ElementUiRsTreeNode> certTypeNodes = EuiCertTypeUtils.buildTree(certTypes);
            if (certTypeNodes != null && certTypeNodes.size() > 0) {
                rootNode.setChildren(certTypeNodes);
            }
        }
        list.add(rootNode);
        return list;
    }

    @Override
    public List<ElementUiRsTreeNode> listOtherCertTypesByCertTypeId(String certTypeId){

        if (StringUtils.isBlank(certTypeId)) {
            throw new InvalidParameterException("参数certTypeId为空!");
        } else {
            List<ElementUiRsTreeNode> list = new ArrayList();
            ElementUiRsTreeNode rootNode = new ElementUiRsTreeNode();
            rootNode.setId("root");
            rootNode.setLabel("设置顶级节点");
            rootNode.setType("root");
            list.add(rootNode);
            String rootOrgId = SecurityContext.getCurrentOrgId();
            List<AeaCertType> certTypes = aeaCertTypeMapper.listOtherCertTypesByCertTypeId(certTypeId, rootOrgId);
            if (certTypes != null && certTypes.size() > 0) {
                List<ElementUiRsTreeNode> certTypeNodes = EuiCertTypeUtils.buildTree(certTypes);
                if (certTypeNodes != null && certTypeNodes.size() > 0) {
                    list.addAll(certTypeNodes);
                }
            }
            return list;
        }
    }

    @Override
    public AeaCertType setParentCertType(String curTypeId, String targetTypeId){

        if(StringUtils.isBlank(curTypeId)){
            throw new InvalidParameterException("传递参数curTypeId为空！");
        }
        if(StringUtils.isBlank(targetTypeId)){
            throw new InvalidParameterException("传递参数targetTypeId为空！");
        }
        String userId = SecurityContext.getCurrentUserId();
        String rootOrgId = SecurityContext.getCurrentOrgId();
        AeaCertType curType = aeaCertTypeMapper.selectOneById(curTypeId);
        if (targetTypeId.equals("root")) {
            String curTypeSeq = "." + curTypeId + ".";
            curType.setParentTypeId("");
            curType.setTypeSeq(curTypeSeq);
            curType.setSortNo(getMaxSortNo(targetTypeId, rootOrgId));
            curType.setModifier(userId);
            curType.setModifyTime(new Date());
            aeaCertTypeMapper.updateOne(curType);
            handleChildCertTypeSeq(curTypeId, curTypeSeq, userId, rootOrgId);
        } else {
            AeaCertType targetType = aeaCertTypeMapper.selectOneById(curTypeId);
            if (curType != null && targetType != null) {
                String curTypeSeq = targetType.getTypeSeq() + curTypeId + ".";
                curType.setParentTypeId(targetTypeId);
                curType.setTypeSeq(curTypeSeq);
                curType.setSortNo(getMaxSortNo(targetTypeId, rootOrgId));
                curType.setModifier(userId);
                curType.setModifyTime(new Date());
                aeaCertTypeMapper.updateOne(curType);
                handleChildCertTypeSeq(curTypeId, curTypeSeq, userId, rootOrgId);
            }
        }
        return curType;
    }

    private void handleChildCertTypeSeq(String curTypeId, String curTypeSeq, String userId, String rootOrgId) {

        List<AeaCertType> childTypes = aeaCertTypeMapper.listAllRelChildCertType(curTypeId, rootOrgId);
        if (childTypes != null && childTypes.size() > 0) {
            for(AeaCertType certType : childTypes){
                String typeSeq = certType.getTypeSeq();
                if (StringUtils.isNotBlank(typeSeq)) {
                    int index = typeSeq.indexOf(curTypeId + ".");
                    if (index > -1) {
                        certType.setTypeSeq(typeSeq.replaceAll(typeSeq.substring(0, index + curTypeId.length() + 1), curTypeSeq));
                    } else {
                        certType.setTypeSeq(curTypeSeq + certType.getCertTypeId() + ".");
                    }
                } else {
                    certType.setTypeSeq(curTypeSeq + certType.getCertTypeId() + ".");
                }
                certType.setModifier(userId);
                certType.setModifyTime(new Date());
                aeaCertTypeMapper.updateOne(certType);
            }
        }
    }
}
