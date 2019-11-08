package com.augurit.aplanmis.common.service.admin.legal.impl;

import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.util.CommonConstant;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.convert.AeaServiceLegalConvert;
import com.augurit.aplanmis.common.domain.AeaServiceLegal;
import com.augurit.aplanmis.common.domain.AeaServiceLegalClause;
import com.augurit.aplanmis.common.mapper.AeaServiceLegalClauseMapper;
import com.augurit.aplanmis.common.mapper.AeaServiceLegalMapper;
import com.augurit.aplanmis.common.service.admin.legal.AeaServiceLegalAdminService;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.utils.EuiLegalUtils;
import io.jsonwebtoken.lang.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ZhangXinhui
 * @date 2019/7/25 025 13:50
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class AeaServiceLegalAdminServiceImpl implements AeaServiceLegalAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaServiceLegalAdminServiceImpl.class);

    @Autowired
    private AeaServiceLegalMapper aeaServiceLegalMapper;

    @Autowired
    private AeaServiceLegalClauseMapper aeaServiceLegalClauseMapper;

    @Autowired
    private FileUtilsService fileUtilsService;

    @Override
    public void deleteAeaServiceLegalById(String id) throws Exception {

        Assert.notNull(id, "id is null");
        aeaServiceLegalClauseMapper.deleteAeaServiceLegalClauseBylegalId(id);

        aeaServiceLegalMapper.deleteAeaServiceLegal(id);
    }

    @Override
    public List<ZtreeNode> getListLegalZtreeNode(AeaServiceLegal aeaServiceLegal) throws Exception {

        List<ZtreeNode> result = new ArrayList<>();
        String rootOrgId = SecurityContext.getCurrentOrgId();
        AeaServiceLegalClause aeaServiceLegalClause = new AeaServiceLegalClause();
        aeaServiceLegal.setRootOrgId(rootOrgId);
        aeaServiceLegalClause.setRootOrgId(rootOrgId);
        List<AeaServiceLegal> list = aeaServiceLegalMapper.listAeaServiceLegal(aeaServiceLegal);
        List<AeaServiceLegalClause> clauseList = aeaServiceLegalClauseMapper.listAeaServiceLegalClause(aeaServiceLegalClause);
        ZtreeNode rootNode = new ZtreeNode();
        rootNode.setId("root");
        rootNode.setName("法律法规库");
        rootNode.setpId("");
        rootNode.setType("root");
        rootNode.setOpen(true);
        rootNode.setIsParent(true);
        rootNode.setNocheck(true);
        result.add(rootNode);
        if (list != null && list.size() > 0) {
            for (AeaServiceLegal legal : list) {
                ZtreeNode node = new ZtreeNode();
                node.setId(legal.getLegalId());
                if (StringUtils.isNotBlank(legal.getParentLegalId())) {
                    node.setpId(legal.getParentLegalId());
                } else {
                    node.setpId("root");
                }
                node.setName(legal.getLegalName() + "【法律法规】");
                node.setOpen(true);
                node.setType("legal");
                node.setNocheck(true);
                result.add(node);
            }
        }

        //增加法律法规条款
        if (clauseList != null && clauseList.size() > 0) {
            for (AeaServiceLegalClause clause : clauseList) {
                ZtreeNode clauseNode = new ZtreeNode();
                clauseNode.setId(clause.getLegalClauseId());
                clauseNode.setName(clause.getClauseTitle() + "【条款】");
                clauseNode.setIsParent(false);
                clauseNode.setpId(clause.getLegalId());
                clauseNode.setOpen(true);
                clauseNode.setType("clause");
                clauseNode.setNocheck(false);
                result.add(clauseNode);
            }
        }

        return result;
    }

    @Override
    public void updateAeaServiceLegalAndAtt(HttpServletRequest request, AeaServiceLegal aeaServiceLegal) throws Exception {

        AeaServiceLegal oldLegal = getAeaServiceLegalById(aeaServiceLegal.getLegalId());
        if (oldLegal != null) {
            StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request;
            List<MultipartFile> serviceLegalAttFiles = req.getFiles("serviceLegalAttFile");
            handleAttr("serviceLegalAtt", serviceLegalAttFiles, oldLegal);
            aeaServiceLegal.setServiceLegalAtt(oldLegal.getServiceLegalAtt());
        }
        updateAeaServiceLegal(aeaServiceLegal);
    }

    @Override
    public AeaServiceLegal getAeaServiceLegalById(String id) throws Exception {

        Assert.notNull(id, "id is null");
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaServiceLegalMapper.getAeaServiceLegalById(id);
    }

    @Override
    public void updateAeaServiceLegal(AeaServiceLegal aeaServiceLegal) throws Exception {

        String legalSeq = getlegalSeq(aeaServiceLegal.getLegalId(), aeaServiceLegal.getParentLegalId());
        aeaServiceLegal.setLegalSeq(legalSeq);
        aeaServiceLegal.setModifier(SecurityContext.getCurrentUserId());
        aeaServiceLegal.setModifyTime(new Date());
        aeaServiceLegalMapper.updateAeaServiceLegal(aeaServiceLegal);
    }

    @Override
    public void saveAeaServiceLegal(AeaServiceLegal aeaServiceLegal) throws Exception {

        aeaServiceLegal.setRootOrgId(SecurityContext.getCurrentOrgId());
        String legalSeq = getlegalSeq(aeaServiceLegal.getLegalId(), aeaServiceLegal.getParentLegalId());
        aeaServiceLegal.setLegalSeq(legalSeq);
        aeaServiceLegal.setCreater(SecurityContext.getCurrentUserId());
        aeaServiceLegal.setCreateTime(new Date());
        aeaServiceLegalMapper.insertAeaServiceLegal(aeaServiceLegal);
    }

    @Override
    public void saveAeaServiceLegalAndAtt(HttpServletRequest request, AeaServiceLegal aeaServiceLegal) throws Exception {

        StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request;
        List<MultipartFile> serviceLegalAttFiles = req.getFiles("serviceLegalAttFile");
        handleAttr("serviceLegalAtt", serviceLegalAttFiles, aeaServiceLegal);
        saveAeaServiceLegal(aeaServiceLegal);
    }

    private void handleAttr(String type, List<MultipartFile> files, AeaServiceLegal aeaServiceLegal) throws Exception {

        if (files != null && files.size() > 0) {
            String pkName = "";
            if ("serviceLegalAtt".equals(type)) {
                pkName = "SERVICE_LEGAL_ATT";
            }
            StringBuilder ids = new StringBuilder();
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    BscAttForm form = fileUtilsService.upload("AEA_SERVICE_LEGAL", pkName, aeaServiceLegal.getLegalId(), null, file);
                    if (null != form) {

                        ids.append(form.getDetailId()).append(",");
                    }
                }
            }
            if (StringUtils.isNotBlank(ids.toString())) {
                if ("serviceLegalAtt".equals(type)) {
                    aeaServiceLegal.setServiceLegalAtt((StringUtils.isBlank(aeaServiceLegal.getServiceLegalAtt()) ? "" : aeaServiceLegal.getServiceLegalAtt()) + ids);
                }
            }
        }
    }

    private String getlegalSeq(String legalId, String parentLegalId) throws Exception {

        if (StringUtils.isNotBlank(parentLegalId)) {
            AeaServiceLegal parentItemServiceLegal = aeaServiceLegalMapper.getAeaServiceLegalById(parentLegalId);
            String parentlegalSeq = CommonConstant.SEQ_SEPARATOR;
            if (parentItemServiceLegal != null && StringUtils.isNotBlank(parentItemServiceLegal.getLegalSeq())) {
                parentlegalSeq = parentItemServiceLegal.getLegalSeq();
            }
            return parentlegalSeq + legalId + CommonConstant.SEQ_SEPARATOR;
        } else {
            return CommonConstant.SEQ_SEPARATOR + legalId + CommonConstant.SEQ_SEPARATOR;
        }
    }

    @Override
    public void delelteFile(String type, String bizId, String detailId) throws Exception {

        if (type.equals("serviceLegalAtt")) {

            AeaServiceLegal legal = aeaServiceLegalMapper.getAeaServiceLegalById(bizId);
            if (legal != null && StringUtils.isNotBlank(legal.getServiceLegalAtt())) {
                String replaceStr = legal.getServiceLegalAtt().replaceAll(detailId + ",", "");
                legal.setServiceLegalAtt(replaceStr);
                aeaServiceLegalMapper.updateAeaServiceLegal(legal);
            }

        } else if (type.equals("clauseAtt")) {

            AeaServiceLegalClause legalClause = aeaServiceLegalClauseMapper.getAeaServiceLegalClauseById(bizId);
            if (legalClause != null && StringUtils.isNotBlank(legalClause.getClauseAtt())) {
                String replaceStr = legalClause.getClauseAtt().replaceAll(detailId + ",", "");
                legalClause.setClauseAtt(replaceStr);
                aeaServiceLegalClauseMapper.updateAeaServiceLegalClause(legalClause);
            }
        }
        // 删除附件
        fileUtilsService.deleteAttachment(detailId);
    }

    @Override
    public List<ElementUiRsTreeNode> gtreeLegalAndClauseForEui(AeaServiceLegal aeaServiceLegal) {

        String rootOrgId = SecurityContext.getCurrentOrgId();
        List<ElementUiRsTreeNode> list = new ArrayList<>();
        ElementUiRsTreeNode rootNode = new ElementUiRsTreeNode();
        rootNode.setId("root");
        rootNode.setLabel("法律法规库");
        rootNode.setType("root");
        aeaServiceLegal.setRootOrgId(rootOrgId);
        List<AeaServiceLegal> legalList = aeaServiceLegalMapper.listAeaServiceLegal(aeaServiceLegal);
        if (legalList != null && legalList.size() > 0) {
            AeaServiceLegalClause aeaServiceLegalClause = new AeaServiceLegalClause();
            aeaServiceLegalClause.setRootOrgId(rootOrgId);
            List<AeaServiceLegalClause> clauseList = aeaServiceLegalClauseMapper.listAeaServiceLegalClause(aeaServiceLegalClause);
            if (clauseList != null && clauseList.size() > 0) {
                for (AeaServiceLegal legal : legalList) {
                    List<AeaServiceLegalClause> needRemoveClauseList = new ArrayList<>();
                    for (AeaServiceLegalClause clause : clauseList) {
                        if (clause.getLegalId().equals(legal.getLegalId())) {
                            needRemoveClauseList.add(clause);
                            legal.getLegalClauses().add(clause);
                        }
                    }
                    clauseList.removeAll(needRemoveClauseList);
                }
            }
            rootNode.setChildren(EuiLegalUtils.buildTree(legalList));
        }
        list.add(rootNode);
        return list;
    }

    /**
     * 获取某组织下的所有法律法规（不包括自己以及包含的自己子法律法规）
     *
     * @param legalId
     * @return
     */
    @Override
    public List<ElementUiRsTreeNode> listOtherLegalByLegalId(String legalId) {

        if (StringUtils.isBlank(legalId)) {
            throw new InvalidParameterException("参数legalId为空!");
        } else {
            List<ElementUiRsTreeNode> list = new ArrayList();
            ElementUiRsTreeNode rootNode = new ElementUiRsTreeNode();
            rootNode.setId("root");
            rootNode.setLabel("设置顶级节点");
            rootNode.setType("root");
            list.add(rootNode);
            String rootOrgId = SecurityContext.getCurrentOrgId();
            List<AeaServiceLegal> legals = aeaServiceLegalMapper.listOtherLegalByLegalId(legalId, rootOrgId);
            if (legals != null && legals.size() > 0) {
                List<ElementUiRsTreeNode> legalNodes = EuiLegalUtils.buildTree(legals);
                if (legalNodes != null && legalNodes.size() > 0) {
                    list.addAll(legalNodes);
                }
            }
            return list;
        }
    }

    /**
     * 设置父级
     *
     * @param curLegalId
     * @param targetLegalId
     * @return
     */
    @Override
    public AeaServiceLegal setParentLegal(String curLegalId, String targetLegalId) {

        if (StringUtils.isBlank(curLegalId)) {
            throw new InvalidParameterException("传递参数curLegalId为空！");
        }
        if (StringUtils.isBlank(targetLegalId)) {
            throw new InvalidParameterException("传递参数targetLegalId为空！");
        }
        String userId = SecurityContext.getCurrentUserId();
        String rootOrgId = SecurityContext.getCurrentOrgId();
        AeaServiceLegal curLegal = aeaServiceLegalMapper.getAeaServiceLegalById(curLegalId);
        if (targetLegalId.equals("root")) {
            String curLegalSeq = "." + curLegalId + ".";
            curLegal.setParentLegalId("");
            curLegal.setLegalSeq(curLegalSeq);
            curLegal.setModifier(userId);
            curLegal.setModifyTime(new Date());
            aeaServiceLegalMapper.updateAeaServiceLegal(curLegal);
            handleChildLegalSeq(curLegalId, curLegalSeq, userId, rootOrgId);
        } else {
            AeaServiceLegal targetLegal = aeaServiceLegalMapper.getAeaServiceLegalById(curLegalId);
            if (curLegal != null && targetLegal != null) {
                String curLegalSeq = targetLegal.getLegalSeq() + curLegalId + ".";
                curLegal.setParentLegalId(targetLegalId);
                curLegal.setLegalSeq(curLegalSeq);
                curLegal.setModifier(userId);
                curLegal.setModifyTime(new Date());
                aeaServiceLegalMapper.updateAeaServiceLegal(curLegal);
                handleChildLegalSeq(curLegalId, curLegalSeq, userId, rootOrgId);
            }
        }
        return curLegal;
    }

    private void handleChildLegalSeq(String curLegalId, String curLegalSeq, String userId, String rootOrgId) {

        List<AeaServiceLegal> childLegals = aeaServiceLegalMapper.listAllRelChildLegal(curLegalId, rootOrgId);
        if (childLegals != null && childLegals.size() > 0) {
            for (AeaServiceLegal legal : childLegals) {
                String LegalSeq = legal.getLegalSeq();
                if (StringUtils.isNotBlank(LegalSeq)) {
                    int index = LegalSeq.indexOf(curLegalId + ".");
                    if (index > -1) {
                        legal.setLegalSeq(LegalSeq.replaceAll(LegalSeq.substring(0, index + curLegalId.length() + 1), curLegalSeq));
                    } else {
                        legal.setLegalSeq(curLegalSeq + legal.getLegalId() + ".");
                    }
                } else {
                    legal.setLegalSeq(curLegalSeq + legal.getLegalId() + ".");
                }
                legal.setModifier(userId);
                legal.setModifyTime(new Date());
                aeaServiceLegalMapper.updateAeaServiceLegal(legal);
            }
        }
    }
}
