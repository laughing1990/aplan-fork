package com.augurit.aplanmis.common.service.admin.item.impl;

import com.augurit.agcloud.bsc.util.CommonConstant;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ActiveStatus;
import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.domain.AeaItemMatType;
import com.augurit.aplanmis.common.mapper.AeaItemMatTypeMapper;
import com.augurit.aplanmis.common.service.admin.item.AeaItemMatTypeAdminService;
import com.augurit.aplanmis.common.utils.EuiMatTypeUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ZhangXinhui
 * @date 2019/7/26 026 14:23
 * @desc
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class AeaItemMatTypeAdminServiceImpl implements AeaItemMatTypeAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaItemMatTypeAdminServiceImpl.class);

    @Autowired
    private AeaItemMatTypeMapper aeaItemMatTypeMapper;

    @Override
    public List<ZtreeNode> getListMatTypeZtreeNode(AeaItemMatType aeaItemMatType)  {

        List<ZtreeNode> result = new ArrayList<>();
        aeaItemMatType.setIsActive(ActiveStatus.ACTIVE.getValue());
        aeaItemMatType.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
        aeaItemMatType.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaItemMatType> list = aeaItemMatTypeMapper.listAeaItemMatType(aeaItemMatType);
        if (list != null && list.size() > 0) {
            for (AeaItemMatType type : list) {
                ZtreeNode node = new ZtreeNode();
                node.setId(type.getMatTypeId());
                node.setpId(type.getParentTypeId());
                node.setName(type.getTypeName());
                node.setOpen(false);
                result.add(node);
            }
        }
        return result;
    }

    @Override
    public AeaItemMatType getAeaItemMatTypeById(String id) {

        Assert.notNull(id, "id is null.");
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaItemMatTypeMapper.selectOneById(id);
    }

    @Override
    public PageInfo<AeaItemMatType> listAeaItemMatType(AeaItemMatType matType, Page page) throws Exception {

        matType.setIsActive(ActiveStatus.ACTIVE.getValue());
        matType.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaItemMatType> list = aeaItemMatTypeMapper.listAeaItemMatType(matType);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public List<ZtreeNode> gtreeMatType()  {

        AeaItemMatType smatType = new AeaItemMatType();
        smatType.setIsActive(ActiveStatus.ACTIVE.getValue());
        smatType.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
        smatType.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaItemMatType> matTypes = aeaItemMatTypeMapper.listAeaItemMatType(smatType);
        List<ZtreeNode> allNodes = new ArrayList<>();
        ZtreeNode rootNode = new ZtreeNode();
        rootNode.setId("root");
        rootNode.setName("材料分类定义库");
        rootNode.setType("root");
        rootNode.setIsParent(true);
        rootNode.setOpen(true);
        rootNode.setNocheck(true);
        allNodes.add(rootNode);
        if (matTypes != null && matTypes.size() > 0) {
            for (AeaItemMatType matType : matTypes) {
                ZtreeNode matTypeNode = new ZtreeNode();
                matTypeNode.setId(matType.getMatTypeId());
                matTypeNode.setName(matType.getTypeName());
                if (StringUtils.isBlank(matType.getParentTypeId())) {
                    matTypeNode.setpId("root");
                } else {
                    matTypeNode.setpId(matType.getParentTypeId());
                }
                matTypeNode.setType("matType");
                matTypeNode.setIsParent(true);
                matTypeNode.setOpen(true);
                matTypeNode.setNocheck(true);
                allNodes.add(matTypeNode);
            }
        }
        return allNodes;
    }

    @Override
    public List<ElementUiRsTreeNode> gtreeMatTypeForEUi(){

        List<ElementUiRsTreeNode> allNodes = new ArrayList<ElementUiRsTreeNode>();

        // 虚拟根节点
        ElementUiRsTreeNode rootNode = new ElementUiRsTreeNode();
        rootNode.setId("root");
        rootNode.setLabel("材料分类定义库");
        rootNode.setType("root");
        allNodes.add(rootNode);

        // 真实数据
        AeaItemMatType smatType = new AeaItemMatType();
        smatType.setIsActive(ActiveStatus.ACTIVE.getValue());
        smatType.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
        smatType.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaItemMatType> matTypes = aeaItemMatTypeMapper.listAeaItemMatType(smatType);
        if (matTypes != null && matTypes.size() > 0) {
            rootNode.setChildren(EuiMatTypeUtils.buildTree(matTypes));
        }
        return allNodes;
    }

    @Override
    public boolean checkUniqueTypeCode(String matTypeId, String typeCode) {

        String rootOrgId = SecurityContext.getCurrentOrgId();
        Integer count = aeaItemMatTypeMapper.checkUniqueTypeCode(matTypeId, typeCode, rootOrgId);
        return count == null || count <= 0;
    }

    @Override
    public void updateAeaItemMatType(AeaItemMatType aeaItemMatType) {

        String parentTypeId = aeaItemMatType.getParentTypeId();
        if(StringUtils.isNotBlank(parentTypeId)&&"root".equals(parentTypeId)){
            aeaItemMatType.setParentTypeId(null);
        }
        aeaItemMatType.setModifier(SecurityContext.getCurrentUserId());
        aeaItemMatType.setModifyTime(new Date());
        aeaItemMatTypeMapper.updateOne(aeaItemMatType);
    }

    @Override
    public void saveAeaItemMatType(AeaItemMatType aeaItemMatType) {

        String parentTypeId = aeaItemMatType.getParentTypeId();
        if(StringUtils.isNotBlank(parentTypeId)&&"root".equals(parentTypeId)){
            aeaItemMatType.setParentTypeId(null);
        }
        aeaItemMatType.setSubCount(0L);
        aeaItemMatType.setSortNo(getMaxSortNo());
        aeaItemMatType.setTypeSeq(getTypeSeq(aeaItemMatType.getMatTypeId(), aeaItemMatType.getParentTypeId()));
        aeaItemMatType.setIsActive(ActiveStatus.ACTIVE.getValue());
        aeaItemMatType.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
        aeaItemMatType.setCreater(SecurityContext.getCurrentUserId());
        aeaItemMatType.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaItemMatType.setCreateTime(new Date());
        aeaItemMatTypeMapper.insertOne(aeaItemMatType);
    }

    @Override
    public Long getMaxSortNo() {

        String rootOrgId = SecurityContext.getCurrentOrgId();
        Long sortNo = aeaItemMatTypeMapper.getMaxSortNo(rootOrgId);
        return sortNo == null ? 1L : sortNo + 1;
    }

    @Override
    public void deleteAeaItemMatTypeById(String id) {

        String rootOrgId = SecurityContext.getCurrentOrgId();
        if (StringUtils.isNotBlank(id)) {
            aeaItemMatTypeMapper.deleteSelfAndAllChildMatType(id, rootOrgId);
        }
    }

    @Override
    public void batchDeleteAeaItemMatType(String[] ids) {

        if (ids != null && ids.length > 0) {
            String rootOrgId = SecurityContext.getCurrentOrgId();
            for (String id : ids) {
                aeaItemMatTypeMapper.deleteSelfAndAllChildMatType(id, rootOrgId);
            }
        }
    }

    private String getTypeSeq(String matTypeId, String parentMatTypeId) {

        if (StringUtils.isNotBlank(parentMatTypeId)) {
            //获取上级的序列
            AeaItemMatType matType = aeaItemMatTypeMapper.selectOneById(parentMatTypeId);
            if (matType != null) {
                String parentSeq = matType.getTypeSeq();
                if (StringUtils.isNotBlank(parentSeq)) {
                    return parentSeq + matTypeId + CommonConstant.SEQ_SEPARATOR;
                } else {
                    return CommonConstant.SEQ_SEPARATOR + matTypeId + CommonConstant.SEQ_SEPARATOR;
                }
            } else {
                return CommonConstant.SEQ_SEPARATOR + matTypeId + CommonConstant.SEQ_SEPARATOR;
            }
        } else {
            return CommonConstant.SEQ_SEPARATOR + matTypeId + CommonConstant.SEQ_SEPARATOR;
        }
    }

    @Override
    public List<ElementUiRsTreeNode> listOtherMatTypesByMatTypeId(String matTypeId){

        if (StringUtils.isBlank(matTypeId)) {
            throw new InvalidParameterException("参数matTypeId为空!");
        } else {
            List<ElementUiRsTreeNode> list = new ArrayList();
            ElementUiRsTreeNode rootNode = new ElementUiRsTreeNode();
            rootNode.setId("root");
            rootNode.setLabel("设置顶级节点");
            rootNode.setType("root");
            list.add(rootNode);
            String rootOrgId = SecurityContext.getCurrentOrgId();
            List<AeaItemMatType> matTypes = aeaItemMatTypeMapper.listOtherMatTypesByMatTypeId(matTypeId, rootOrgId);
            if (matTypes != null && matTypes.size() > 0) {
                List<ElementUiRsTreeNode> matTypeNodes = EuiMatTypeUtils.buildTree(matTypes);
                if (matTypeNodes != null && matTypeNodes.size() > 0) {
                    list.addAll(matTypeNodes);
                }
            }
            return list;
        }
    }

    @Override
    public AeaItemMatType setParentMatType(String curTypeId, String targetTypeId){

        if(StringUtils.isBlank(curTypeId)){
            throw new InvalidParameterException("传递参数curTypeId为空！");
        }
        if(StringUtils.isBlank(targetTypeId)){
            throw new InvalidParameterException("传递参数targetTypeId为空！");
        }
        String userId = SecurityContext.getCurrentUserId();
        String rootOrgId = SecurityContext.getCurrentOrgId();
        AeaItemMatType curType = aeaItemMatTypeMapper.selectOneById(curTypeId);
        if (targetTypeId.equals("root")) {
            String curTypeSeq = "." + curTypeId + ".";
            curType.setParentTypeId("");
            curType.setTypeSeq(curTypeSeq);
            curType.setSortNo(getMaxSortNo());
            curType.setModifier(userId);
            curType.setModifyTime(new Date());
            aeaItemMatTypeMapper.updateOne(curType);
            handleChildMatTypeSeq(curTypeId, curTypeSeq, userId, rootOrgId);
        } else {
            AeaItemMatType targetType = aeaItemMatTypeMapper.selectOneById(curTypeId);
            if (curType != null && targetType != null) {
                String curTypeSeq = targetType.getTypeSeq() + curTypeId + ".";
                curType.setParentTypeId(targetTypeId);
                curType.setTypeSeq(curTypeSeq);
                curType.setSortNo(getMaxSortNo());
                curType.setModifier(userId);
                curType.setModifyTime(new Date());
                aeaItemMatTypeMapper.updateOne(curType);
                handleChildMatTypeSeq(curTypeId, curTypeSeq, userId, rootOrgId);
            }
        }
        return curType;
    }

    private void handleChildMatTypeSeq(String curTypeId, String curTypeSeq, String userId, String rootOrgId) {

        List<AeaItemMatType> childTypes = aeaItemMatTypeMapper.listAllRelChildMatType(curTypeId, rootOrgId);
        if (childTypes != null && childTypes.size() > 0) {
            for(AeaItemMatType matType : childTypes){
                String typeSeq = matType.getTypeSeq();
                if (StringUtils.isNotBlank(typeSeq)) {
                    int index = typeSeq.indexOf(curTypeId + ".");
                    if (index > -1) {
                        matType.setTypeSeq(typeSeq.replaceAll(typeSeq.substring(0, index + curTypeId.length() + 1), curTypeSeq));
                    } else {
                        matType.setTypeSeq(curTypeSeq + matType.getMatTypeId() + ".");
                    }
                } else {
                    matType.setTypeSeq(curTypeSeq + matType.getMatTypeId() + ".");
                }
                matType.setModifier(userId);
                matType.setModifyTime(new Date());
                aeaItemMatTypeMapper.updateOne(matType);
            }
        }
    }
}
