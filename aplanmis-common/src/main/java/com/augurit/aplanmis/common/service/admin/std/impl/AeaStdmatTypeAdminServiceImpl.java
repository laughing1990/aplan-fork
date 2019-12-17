package com.augurit.aplanmis.common.service.admin.std.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ActiveStatus;
import com.augurit.aplanmis.common.constants.CommonConstant;
import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.domain.AeaStdmat;
import com.augurit.aplanmis.common.domain.AeaStdmatType;
import com.augurit.aplanmis.common.mapper.AeaStdmatMapper;
import com.augurit.aplanmis.common.mapper.AeaStdmatTypeMapper;
import com.augurit.aplanmis.common.service.admin.std.AeaStdmatTypeAdminService;
import com.augurit.aplanmis.common.utils.EuiStdMatTypeUtils;
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

/**
* 材料类型表-Service服务接口实现类
*/
@Service
@Transactional
public class AeaStdmatTypeAdminServiceImpl implements AeaStdmatTypeAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaStdmatTypeAdminServiceImpl.class);

    @Autowired
    private AeaStdmatTypeMapper aeaStdmatTypeMapper;

    @Autowired
    private AeaStdmatMapper aeaStdmatMapper;

    @Override
    public void saveAeaStdmatType(AeaStdmatType aeaStdmatType) {

        String rootOrgId = SecurityContext.getCurrentOrgId();
        String userId = SecurityContext.getCurrentUserId();
        aeaStdmatType.setSubCount(0L);
        aeaStdmatType.setSortNo(getMaxSortNo(rootOrgId));
        aeaStdmatType.setTypeSeq(getTypeSeq(aeaStdmatType.getStdmatTypeId(), aeaStdmatType.getParentTypeId()));
        aeaStdmatType.setIsActive(ActiveStatus.ACTIVE.getValue());
        aeaStdmatType.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
        aeaStdmatType.setCreater(userId);
        aeaStdmatType.setRootOrgId(rootOrgId);
        aeaStdmatType.setCreateTime(new Date());
        aeaStdmatTypeMapper.insertOne(aeaStdmatType);
    }

    @Override
    public void updateAeaStdmatType(AeaStdmatType aeaStdmatType) {

        aeaStdmatType.setModifier(SecurityContext.getCurrentUserId());
        aeaStdmatType.setModifyTime(new Date());
        aeaStdmatTypeMapper.updateOne(aeaStdmatType);
    }

    private String getTypeSeq(String matTypeId, String parentMatTypeId) {

        if (StringUtils.isNotBlank(parentMatTypeId)) {
            //获取上级的序列
            AeaStdmatType matType = aeaStdmatTypeMapper.selectOneById(parentMatTypeId);
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
    public void deleteAeaStdmatTypeById(String id) {

        if(StringUtils.isBlank(id)) {
            throw new InvalidParameterException("参数id为空!");
        }
        aeaStdmatTypeMapper.deleteById(id);
    }

    @Override
    public void deleteSelfAndAllChildById(String id, String rootOrgId){

        if(StringUtils.isBlank(id)) {
            throw new InvalidParameterException("参数id为空!");
        }
        aeaStdmatTypeMapper.deleteSelfAndAllChildById(id, rootOrgId);
    }

    @Override
    public PageInfo<AeaStdmatType> listAeaStdmatType(AeaStdmatType aeaStdmatType,Page page) {

        aeaStdmatType.setIsActive(ActiveStatus.ACTIVE.getValue());
        aeaStdmatType.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaStdmatType> list = aeaStdmatTypeMapper.listAeaStdmatType(aeaStdmatType);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaStdmatType>(list);
    }

    @Override
    public AeaStdmatType getAeaStdmatTypeById(String id) {

        if(StringUtils.isBlank(id)) {
            throw new InvalidParameterException("参数id为空!");
        }
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaStdmatTypeMapper.selectOneById(id);
    }

    @Override
    public List<AeaStdmatType> listAeaStdmatType(AeaStdmatType aeaStdmatType) {

        aeaStdmatType.setIsActive(ActiveStatus.ACTIVE.getValue());
        aeaStdmatType.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaStdmatType> list = aeaStdmatTypeMapper.listAeaStdmatType(aeaStdmatType);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public boolean checkUniqueTypeCode(String matTypeId, String typeCode) {

        String rootOrgId = SecurityContext.getCurrentOrgId();
        Long count = aeaStdmatTypeMapper.checkUniqueTypeCode(matTypeId, typeCode, rootOrgId);
        return count == null || count <= 0;
    }

    @Override
    public Long getMaxSortNo(String rootOrgId) {

        Long sortNo = aeaStdmatTypeMapper.getMaxSortNo(rootOrgId);
        return sortNo == null ? 1L : sortNo + 1;
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
            List<AeaStdmatType> matTypes = aeaStdmatTypeMapper.listOtherStdMatTypesById(matTypeId, rootOrgId);
            if (matTypes != null && matTypes.size() > 0) {
                List<ElementUiRsTreeNode> matTypeNodes = EuiStdMatTypeUtils.buildTree(matTypes);
                if (matTypeNodes != null && matTypeNodes.size() > 0) {
                    list.addAll(matTypeNodes);
                }
            }
            return list;
        }
    }

    @Override
    public AeaStdmatType setParentMatType(String curTypeId, String targetTypeId){

        if(StringUtils.isBlank(curTypeId)){
            throw new InvalidParameterException("传递参数curTypeId为空！");
        }
        if(StringUtils.isBlank(targetTypeId)){
            throw new InvalidParameterException("传递参数targetTypeId为空！");
        }
        String userId = SecurityContext.getCurrentUserId();
        String rootOrgId = SecurityContext.getCurrentOrgId();
        AeaStdmatType curType = aeaStdmatTypeMapper.selectOneById(curTypeId);
        if (targetTypeId.equals("root")) {
            String curTypeSeq = "." + curTypeId + ".";
            curType.setParentTypeId("");
            curType.setTypeSeq(curTypeSeq);
            curType.setSortNo(getMaxSortNo(rootOrgId));
            curType.setModifier(userId);
            curType.setModifyTime(new Date());
            aeaStdmatTypeMapper.updateOne(curType);
            handleChildStdMatTypeSeq(curTypeId, curTypeSeq, userId, rootOrgId);
        } else {
            AeaStdmatType targetType = aeaStdmatTypeMapper.selectOneById(curTypeId);
            if (curType != null && targetType != null) {
                String curTypeSeq = targetType.getTypeSeq() + curTypeId + ".";
                curType.setParentTypeId(targetTypeId);
                curType.setTypeSeq(curTypeSeq);
                curType.setSortNo(getMaxSortNo(rootOrgId));
                curType.setModifier(userId);
                curType.setModifyTime(new Date());
                aeaStdmatTypeMapper.updateOne(curType);
                handleChildStdMatTypeSeq(curTypeId, curTypeSeq, userId, rootOrgId);
            }
        }
        return curType;
    }

    private void handleChildStdMatTypeSeq(String curTypeId, String curTypeSeq, String userId, String rootOrgId) {

        List<AeaStdmatType> childTypes = aeaStdmatTypeMapper.listAllRelChildStdMatType(curTypeId, rootOrgId);
        if (childTypes != null && childTypes.size() > 0) {
            for(AeaStdmatType matType : childTypes){
                String typeSeq = matType.getTypeSeq();
                if (StringUtils.isNotBlank(typeSeq)) {
                    int index = typeSeq.indexOf(curTypeId + ".");
                    if (index > -1) {
                        matType.setTypeSeq(typeSeq.replaceAll(typeSeq.substring(0, index + curTypeId.length() + 1), curTypeSeq));
                    } else {
                        matType.setTypeSeq(curTypeSeq + matType.getStdmatTypeId() + ".");
                    }
                } else {
                    matType.setTypeSeq(curTypeSeq + matType.getStdmatTypeId() + ".");
                }
                matType.setModifier(userId);
                matType.setModifyTime(new Date());
                aeaStdmatTypeMapper.updateOne(matType);
            }
        }
    }

    @Override
    public List<ZtreeNode> gtrStdTypeMatZtree(String rootOrgId){

        List<ZtreeNode> allNodes = new ArrayList<>();
        // 根节点数据
        ZtreeNode rootNode = new ZtreeNode();
        rootNode.setId("root");
        rootNode.setName("标准材料库");
        rootNode.setpId("");
        rootNode.setType("root");
        rootNode.setOpen(true);
        rootNode.setIsParent(true);
        rootNode.setNocheck(true);
        allNodes.add(rootNode);
        // 分类节点数据
        AeaStdmatType matType = new AeaStdmatType();
        matType.setIsActive(ActiveStatus.ACTIVE.getValue());
        matType.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
        matType.setRootOrgId(rootOrgId);
        List<AeaStdmatType> matTypeList = aeaStdmatTypeMapper.listAeaStdmatType(matType);
        if(matTypeList!=null&&matTypeList.size()>0){
            for(AeaStdmatType item : matTypeList){
                ZtreeNode matTypeNode = new ZtreeNode();
                matTypeNode.setId(item.getStdmatTypeId());
                matTypeNode.setName(item.getTypeName());
                matTypeNode.setpId(item.getParentTypeId());
                matTypeNode.setType("stdMatType");
                matTypeNode.setOpen(true);
                matTypeNode.setIsParent(true);
                matTypeNode.setNocheck(true);
                allNodes.add(matTypeNode);
            }
            // 材料节点数据
            AeaStdmat mat = new AeaStdmat();
            mat.setIsActive(ActiveStatus.ACTIVE.getValue());
            mat.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
            mat.setRootOrgId(rootOrgId);
            List<AeaStdmat> matList = aeaStdmatMapper.listAeaStdmat(mat);
            if(matList!=null&&matList.size()>0){
                for(AeaStdmat item : matList){
                    ZtreeNode matNode = new ZtreeNode();
                    matNode.setId(item.getStdmatId());
                    matNode.setName(item.getStdmatName());
                    matNode.setpId(item.getStdmatTypeId());
                    matNode.setType("stdMat");
                    matNode.setOpen(true);
                    matNode.setIsParent(false);
                    matNode.setNocheck(false);
                    allNodes.add(matNode);
                }
            }
        }
        return allNodes;
    }
}

