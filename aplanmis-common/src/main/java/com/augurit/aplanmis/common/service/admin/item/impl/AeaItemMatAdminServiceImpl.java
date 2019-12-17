package com.augurit.aplanmis.common.service.admin.item.impl;

import com.augurit.agcloud.bpm.common.domain.ActStoForm;
import com.augurit.agcloud.bpm.common.mapper.ActStoFormMapper;
import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.sc.rule.code.service.AutoCodeNumberService;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.agcloud.framework.util.JsonUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ActiveStatus;
import com.augurit.aplanmis.common.constants.CommonConstant;
import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.constants.MindType;
import com.augurit.aplanmis.common.convert.AeaItemMatConvert;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.admin.item.AeaItemAdminService;
import com.augurit.aplanmis.common.service.admin.item.AeaItemInoutAdminService;
import com.augurit.aplanmis.common.service.admin.item.AeaItemMatAdminService;
import com.augurit.aplanmis.common.service.admin.item.AeaItemStateFormAdminService;
import com.augurit.aplanmis.common.service.admin.par.AeaParInAdminService;
import com.augurit.aplanmis.common.service.admin.par.AeaParStateFormAdminService;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.vo.AeaItemMatKpVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author ZhangXinhui
 * @date 2019/7/30 030 11:10
 * @desc
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class AeaItemMatAdminServiceImpl implements AeaItemMatAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaItemMatAdminServiceImpl.class);

    @Autowired
    private AeaItemMatMapper aeaItemMatMapper;

    @Autowired
    private AeaItemMatTypeMapper aeaItemMatTypeMapper;

    @Autowired
    private AeaItemInoutAdminService aeaItemInoutAdminService;

    @Autowired
    private AeaItemInoutMapper aeaItemInoutMapper;

    @Autowired
    private AeaParInMapper aeaParInMapper;

    @Autowired
    private AeaParInAdminService aeaParInAdminService;

    @Autowired
    private AutoCodeNumberService autoCodeNumberService;

    @Autowired
    private AeaItemAdminService aeaItemAdminService;

    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;

    @Autowired
    private AeaParStateFormAdminService aeaParStateFormAdminService;

    @Autowired
    private AeaItemStateFormAdminService aeaItemStateFormAdminService;

    @Autowired
    private FileUtilsService fileUtilsService;

    @Autowired
    private ActStoFormMapper actStoFormMapper;

    @Autowired
    private AeaStdmatMapper aeaStdmatMapper;

    @Override
    public void saveAeaItemMatAndParIn(HttpServletRequest request,
                                       String stageId,
                                       String isStateIn,
                                       String stateId,
                                       AeaItemMat aeaItemMat) throws Exception {

        // 保存材料
        saveAeaItemMat(request, aeaItemMat);
        // 建立关联
        AeaParIn aeaParIn = new AeaParIn();
        aeaParIn.setInId(UUID.randomUUID().toString());
        aeaParIn.setMatId(aeaItemMat.getMatId());
        aeaParIn.setIsDeleted("0");
        aeaParIn.setStageId(stageId);
        aeaParIn.setIsOwner("1");
        aeaParIn.setFileType("mat");
        aeaParIn.setCreater(aeaItemMat.getCreater());
        aeaParIn.setCreateTime(new Date());
        aeaParIn.setRootOrgId(SecurityContext.getCurrentOrgId());
        Long inSortNo = aeaParInAdminService.getMaxSortNoByStageId(stageId);
        Long formSortNo = aeaParStateFormAdminService.getMaxSortNo(stageId);
        aeaParIn.setSortNo(inSortNo >= formSortNo ? inSortNo : formSortNo);
        if (Status.ON.equals(aeaItemMat.getIsCommon())) {
            aeaParIn.setIsStateIn(Status.OFF);
        } else {
            if (StringUtils.isBlank(isStateIn)) {
                aeaParIn.setIsStateIn(Status.OFF);
            } else {
                aeaParIn.setIsStateIn(isStateIn);
                aeaParIn.setParStateId(stateId);
            }
        }
        aeaParInMapper.insertAeaParIn(aeaParIn);
        logger.debug("成功保存事项材料和输入输出!");
    }

    @Override
    public void saveAeaItemMatAndParIn(HttpServletRequest request, AeaItemMat aeaItemMat, AeaParIn aeaParIn) throws Exception {

        saveAeaItemMat(request, aeaItemMat);
        aeaParIn.setInId(UUID.randomUUID().toString());
        aeaParIn.setCreater(aeaItemMat.getCreater());
        aeaParIn.setCreateTime(new Date());
        aeaParIn.setMatId(aeaItemMat.getMatId());
        aeaParIn.setIsDeleted("0");
        aeaParIn.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaParInMapper.insertAeaParIn(aeaParIn);
        logger.debug("成功保存事项材料和输入输出!");
    }

    @Override
    public PageInfo<AeaItemMat> listStageNoSelectGlobalMat(String stageId, String keyword, Page page) {

        String rootOrgId = SecurityContext.getCurrentOrgId();
        PageHelper.startPage(page);
        List<AeaItemMat> list = aeaItemMatMapper.listStageNoSelectGlobalMat(stageId, keyword, rootOrgId);
        return new PageInfo<>(list);
    }

    @Override
    public void updateAeaItemMat(HttpServletRequest request, AeaItemMat aeaItemMat) throws Exception {

        updateAeaItemMat(aeaItemMat);
        AeaItemMat oldMat = getAeaItemMatById(aeaItemMat.getMatId());
        if (oldMat != null) {
            StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request;
            List<MultipartFile> templateDocFiles = req.getFiles("templateDocFile");
            List<MultipartFile> sampleDocFiles = req.getFiles("sampleDocFile");
            List<MultipartFile> reviewSampleDocFiles = req.getFiles("reviewSampleDocFile");
            handleAttr("0", templateDocFiles, oldMat);
            handleAttr("1", sampleDocFiles, oldMat);
            handleAttr("2", reviewSampleDocFiles, oldMat);
            updateAeaItemMat(oldMat);
        }
    }

    @Override
    public void saveAeaItemMat(HttpServletRequest request, AeaItemMat aeaItemMat) throws Exception {

        StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request;
        List<MultipartFile> templateDocFiles = req.getFiles("templateDocFile");
        List<MultipartFile> sampleDocFiles = req.getFiles("sampleDocFile");
        List<MultipartFile> reviewSampleDocFiles = req.getFiles("reviewSampleDocFile");
        handleAttr("0", templateDocFiles, aeaItemMat);
        handleAttr("1", sampleDocFiles, aeaItemMat);
        handleAttr("2", reviewSampleDocFiles, aeaItemMat);
        saveAeaItemMat(aeaItemMat);
    }

    @Override
    public void saveAeaItemMat(AeaItemMat aeaItemMat) throws Exception {

        aeaItemMat.setCreater(SecurityContext.getCurrentUserId());
        aeaItemMat.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaItemMat.setCreateTime(new Date());
        aeaItemMat.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
        aeaItemMat.setSortNo(getMatMaxSortNo());
        aeaItemMatMapper.insertAeaItemMat(aeaItemMat);
    }

    @Override
    public Long getMatMaxSortNo() {

        String rootOrgId = SecurityContext.getCurrentOrgId();
        Long sortNo = aeaItemMatMapper.getMaxSortNo(rootOrgId);
        return sortNo == null ? 1L : sortNo + 1;
    }

    @Override
    public void updateAeaItemMat(AeaItemMat aeaItemMat) throws Exception {

        aeaItemMat.setModifier(SecurityContext.getCurrentUserId());
        aeaItemMat.setModifyTime(new Date());
        aeaItemMatMapper.updateAeaItemMat(aeaItemMat);
    }

    @Override
    public void deleteAeaItemMatById(String id) throws Exception {
        aeaItemMatMapper.deleteAeaItemMatById(Optional.of(id).get());
    }

    @Override
    public boolean checkMatCode(String matId, String matCode, String rootOrgId) {

        Integer count = aeaItemMatMapper.checkMatCode(matId, matCode, rootOrgId);
        return count == null || count <= 0;
    }

    @Override
    public boolean checkMatName(String matId, String matName, String isCommon, String rootOrgId){

        Integer count = aeaItemMatMapper.checkMatName(matId, matName,isCommon, rootOrgId);
        return count == null || count <= 0;
    }

    @Override
    public PageInfo<AeaItemMat> listAeaItemMat(AeaItemMat aeaItemMat, Page page) throws Exception {

        String rootOrgId = SecurityContext.getCurrentOrgId();
        aeaItemMat.setRootOrgId(rootOrgId);
        PageHelper.startPage(page);
        List<AeaItemMat> list = aeaItemMatMapper.listAeaItemMat(aeaItemMat);
        logger.debug("成功执行分页查询！！");
//        if (list != null && list.size() > 0) {
//            AeaItemMatType aeaItemMatType = new AeaItemMatType();
//            aeaItemMatType.setRootOrgId(rootOrgId);
//            List<AeaItemMatType> typeList = aeaItemMatTypeMapper.listAeaItemMatType(aeaItemMatType);
//            if (typeList != null && typeList.size() > 0) {
//                for (AeaItemMat matInfo : list) {
//                    for (AeaItemMatType matType : typeList) {
//                        if (matType.getMatTypeId().equals(matInfo.getMatTypeId())) {
//                            matInfo.setMatTypeName(matType.getTypeName());
//                            break;
//                        }
//                    }
//                }
//            }
//        }
//        if (list != null && list.size() > 0) {
//            AeaStdmat mat = new AeaStdmat();
//            mat.setIsActive(ActiveStatus.ACTIVE.getValue());
//            mat.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
//            mat.setRootOrgId(rootOrgId);
//            List<AeaStdmat> matList = aeaStdmatMapper.listAeaStdmat(mat);
//            if(matList!=null&&matList.size()>0) {
//                for (AeaItemMat matInfo : list) {
//                    for (AeaStdmat item : matList) {
//                        if (item.getStdmatId().equals(matInfo.getStdmatId())) {
//                            matInfo.setStdmatName(item.getStdmatName());
//                            break;
//                        }
//                    }
//                }
//            }
//        }
        return new PageInfo<>(list);
    }

    @Override
    public AeaItemMat getAeaItemMatById(String id) {

        Assert.notNull(id, "id is null.");
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaItemMatMapper.selectOneById(id);
    }

    @Override
    public void batchDeleteAeaItemMatByIds(String[] ids) {

        aeaItemMatMapper.batchDeleteAeaItemMatByIds(ids);
        logger.debug("批量删除事项材料,ids:{}", JsonUtils.toJson(ids));
    }

    @Override
    public void handleKpItemMat(AeaItemMatKpVo aeaItemMatKpVo, AeaItemMat aeaItemMat) throws Exception {

        //对mat表的处理
        String userId = SecurityContext.getCurrentUserId();
        String rootOrgId = SecurityContext.getCurrentOrgId();
        AeaItemMatConvert.convertKpMatToAeaMat(aeaItemMatKpVo, aeaItemMat);
        String matCode = aeaItemMat.getMatCode();
        AeaItemMat oldMat = null;
        AeaItemMat searchMat = new AeaItemMat();
        searchMat.setRootOrgId(rootOrgId);
        searchMat.setMatId(aeaItemMat.getMatId());
        List<AeaItemMat> matList = aeaItemMatMapper.listAeaItemMat(searchMat);
        if (matList != null && !matList.isEmpty()) {
            oldMat = matList.get(0);
        }
        if (oldMat == null) {
            try {
                matCode = autoCodeNumberService.generate("AEA-ITEM-MAT-CODE", rootOrgId);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            aeaItemMat.setRootOrgId(rootOrgId);
            aeaItemMat.setMatCode(matCode);
            aeaItemMat.setCreater(userId);
            aeaItemMat.setCreateTime(new Date());
            aeaItemMat.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
            aeaItemAdminService.handleMatAttachments(aeaItemMat);
            aeaItemMatMapper.insertAeaItemMat(aeaItemMat);
        } else {
            aeaItemMat.setMatCode(oldMat.getMatCode());
            aeaItemMat.setModifier(userId);
            aeaItemMat.setModifyTime(new Date());
            aeaItemAdminService.handleMatAttachments(aeaItemMat);
            aeaItemMatMapper.updateAeaItemMat(aeaItemMat);
        }
    }

    private void handleAttr(String type, List<MultipartFile> files, AeaItemMat aeaItemMat) throws Exception {

        if (files != null && files.size() > 0) {
            String pkName = "";
            if ("0".equals(type)) {
                pkName = "TEMPLATE_DOC";
            } else if ("1".equals(type)) {
                pkName = "SAMPLE_DOC";
            } else if ("2".equals(type)) {
                pkName = "REVIEW_SAMPLE_DOC";
            }
            StringBuilder ids = new StringBuilder();
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    BscAttForm form = fileUtilsService.upload("AEA_ITEM_MAT", pkName, aeaItemMat.getMatId(), null, file);
                    if (null != form) {

                        ids.append(form.getDetailId()).append(",");
                    }
                    /*UploadFileCmd uploadFileCmd = uploaderFactory.createDefaultUpload(file, aeaItemMat.getMatId(), "AEA_ITEM_MAT", pkName, UploadType.MONGODB.getValue());
                    String detailId = attachmentAdminService.uploadFileStrategy(uploadFileCmd);*/
                }
            }
            if (StringUtils.isNotBlank(ids.toString())) {
                if ("0".equals(type)) {
                    aeaItemMat.setTemplateDoc((StringUtils.isBlank(aeaItemMat.getTemplateDoc()) ? "" : aeaItemMat.getTemplateDoc()) + ids);
                } else if ("1".equals(type)) {
                    aeaItemMat.setSampleDoc((StringUtils.isBlank(aeaItemMat.getSampleDoc()) ? "" : aeaItemMat.getSampleDoc()) + ids);
                } else if ("2".equals(type)) {
                    aeaItemMat.setReviewSampleDoc((StringUtils.isBlank(aeaItemMat.getReviewSampleDoc()) ? "" : aeaItemMat.getReviewSampleDoc()) + ids);
                }
            }
        }
    }

    /**
     * 删除材料附件
     *
     * @param type
     * @param matId
     * @param detailId
     * @throws Exception
     */
    @Override
    public void delelteFile(Integer type, String matId, String detailId) throws Exception {

        AeaItemMat aeaItemMat = aeaItemMatMapper.selectOneById(matId);
        if (aeaItemMat != null) {
            if (type == 0) {
                if (StringUtils.isNotBlank(aeaItemMat.getTemplateDoc())) {
                    aeaItemMat.setTemplateDoc(aeaItemMat.getTemplateDoc().replace(detailId + ",", ""));
                }
            } else if (type == 1) {
                if (StringUtils.isNotBlank(aeaItemMat.getSampleDoc())) {
                    aeaItemMat.setSampleDoc(aeaItemMat.getSampleDoc().replace(detailId + ",", ""));
                }
            } else if (type == 2) {
                if (StringUtils.isNotBlank(aeaItemMat.getReviewSampleDoc())) {
                    aeaItemMat.setReviewSampleDoc(aeaItemMat.getReviewSampleDoc().replace(detailId + ",", ""));
                }
            }
            fileUtilsService.deleteAttachment(detailId);
//            attachmentAdminService.deleteFileStategy(detailId);
            aeaItemMatMapper.updateAeaItemMat(aeaItemMat);
        }
    }

    @Override
    public boolean downloadDoc(Integer type, String detailId, HttpServletResponse response, HttpServletRequest request) throws Exception {
        String[] detailIds = {detailId};
        return fileUtilsService.downloadAttachment(detailIds, response, request, false);
//        return attachmentAdminService.downloadFileStrategy(detailId,response);
    }

    @Override
    public PageInfo<AeaItemMat> listItemInOutNoSelectGlobalMat(String rootOrgId, String itemVerId, String stateVerId, String isInput, String keyword, Page page) {

        PageHelper.startPage(page);
        List<AeaItemMat> list = aeaItemMatMapper.listItemInOutNoSelectGlobalMat(rootOrgId, itemVerId, stateVerId, isInput, keyword);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<AeaItemMat> listStageOrItemNoSelectMatPage(AeaItemMat aeaItemMat, Page page) {

        aeaItemMat.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaItemMat> list = aeaItemMatMapper.listAeaItemMat(aeaItemMat);
        logger.debug("成功执行全局材料分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public void saveChooseStageMatAndParIn(String ids, String stageId, String isStateIn, String stateId) {

        if (StringUtils.isNotBlank(ids)) {
            String userId = SecurityContext.getCurrentUserId();
            String rootOrgId = SecurityContext.getCurrentOrgId();
            String[] idArr = ids.split(CommonConstant.COMMA_SEPARATOR);
            if (idArr != null && idArr.length > 0) {
                AeaParIn in = new AeaParIn();
                in.setStageId(stageId);
                in.setIsStateIn(isStateIn);
                in.setFileType(MindType.MATERIAL.getValue());
                in.setIsDeleted(Status.OFF);
                in.setRootOrgId(rootOrgId);
                // 阶段下通用材料或者情形材料
                List<AeaParIn> ins = aeaParInMapper.listAeaParIn(in);
                for (String id : idArr) {
                    if (StringUtils.isNotBlank(id)) {
                        int i = 0;
                        if (ins != null && ins.size() > 0) {
                            for (AeaParIn ino : ins) {
                                // 通用材料
                                if (isStateIn.equals("0")) {
                                    if (id.equals(ino.getMatId())) {
                                        i++;
                                        break;
                                    }
                                    // 情形材料
                                } else {
                                    if (stateId.equals(ino.getParStateId()) && id.equals(ino.getMatId())) {
                                        i++;
                                        break;
                                    }
                                }
                            }
                        }
                        if (i == 0) {
                            AeaParIn aeaParIn = new AeaParIn();
                            aeaParIn.setInId(UUID.randomUUID().toString());
                            aeaParIn.setStageId(stageId);
                            aeaParIn.setIsOwner(Status.ON);
                            aeaParIn.setIsStateIn(isStateIn);
                            aeaParIn.setParStateId(stateId);
                            aeaParIn.setMatId(id);
                            aeaParIn.setFileType(MindType.MATERIAL.getValue());
                            aeaParIn.setIsDeleted(Status.OFF);
                            aeaParIn.setCreater(userId);
                            aeaParIn.setRootOrgId(rootOrgId);
                            aeaParIn.setCreateTime(new Date());
                            Long inSortNo = aeaParInAdminService.getMaxSortNoByStageId(stageId);
                            Long formSortNo = aeaParStateFormAdminService.getMaxSortNo(stageId);
                            aeaParIn.setSortNo(inSortNo >= formSortNo ? inSortNo : formSortNo);
                            aeaParInMapper.insertAeaParIn(aeaParIn);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void saveChooseItemMatAndInout(String ids, String itemVerId, String isStateIn, String itemStateId, String stateVerId, String isCommon) {

        String rootOrgId = SecurityContext.getCurrentOrgId();
        AeaItemBasic aeaItemBasic = aeaItemBasicMapper.getOneByItemVerId(itemVerId, rootOrgId);
        if (StringUtils.isNotBlank(ids)) {
            String userId = SecurityContext.getCurrentUserId();
            String[] idArr = ids.split(CommonConstant.COMMA_SEPARATOR);
            for (String id : idArr) {
                if (StringUtils.isNotBlank(id)) {
                    AeaItemInout inout = new AeaItemInout();
                    inout.setInoutId(UUID.randomUUID().toString());
                    inout.setItemId(aeaItemBasic.getItemId());
                    inout.setItemVerId(itemVerId);
                    inout.setStateVerId(stateVerId);
                    inout.setIsOwner(Status.ON);
                    inout.setIsInput(Status.ON);
                    inout.setIsStateIn(isStateIn);
                    inout.setItemStateId(itemStateId);
                    inout.setMatId(id);
                    inout.setFileType(MindType.MATERIAL.getValue());
                    inout.setCreater(userId);
                    inout.setRootOrgId(rootOrgId);
                    inout.setCreateTime(new Date());
                    inout.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
                    Long inSortNo = aeaItemInoutAdminService.getMaxSortNo(inout.getItemVerId(), inout.getStateVerId(), inout.getIsInput(), rootOrgId);
                    inout.setSortNo(inSortNo);
                    aeaItemInoutMapper.insertAeaItemInout(inout);
                }
            }
        }
    }

    @Override
    public List<ZtreeNode> gtreeForm(String rootOrgId){

        List<ZtreeNode> allNodes = new ArrayList<>();
        ZtreeNode rootNode = new ZtreeNode();
        rootNode.setId("root");
        rootNode.setName("表单库");
        rootNode.setType("root");
        rootNode.setIsParent(true);
        rootNode.setOpen(true);
        rootNode.setNocheck(true);
        allNodes.add(rootNode);
        ActStoForm sform = new ActStoForm();
        sform.setFormOrgId(rootOrgId);
        List<ActStoForm> forms = aeaItemMatMapper.listActStoForm(sform);
        if(forms!=null&&forms.size()>0){
            for(ActStoForm form : forms){
                ZtreeNode node = new ZtreeNode();
                String id = form.getFormId();
                String name = form.getFormName();
                String property = form.getFormProperty();
                String formCode = form.getFormCode();
                // meta-biz表示元数据普通表单，meta-flow表示元数据流程表单，smart-biz表示智能普通表单，smart-flow表示智能流程表单
                if(StringUtils.isNotBlank(property)){
                   if("meta-biz".equals(property)||"dev-biz".equals(property)){
                       property =  "【普通开发表单】";
                   }else if("meta-flow".equals(property)){
                       property =  "【流程开发表单】" ;
                   }else if("smart-biz".equals(property)){
                       property =  "【普通智能表单】";
                   }else if("smart-flow".equals(property)){
                       property =  "【流程智能表单】";
                   }else{
                       property =  "";
                   }
                }
                if(StringUtils.isNotBlank(formCode)){
                    formCode = "【"+ formCode +"】";
                }
                node.setId(id);
                node.setName(property + formCode + name);
                node.setType("form");
                node.setpId(StringUtils.isBlank(form.getParentFormId())?"root":form.getParentFormId());
                node.setIsParent(StringUtils.isNotBlank(form.getParentFormId())?false:true);
                node.setOpen(true);
                node.setNocheck(false);
                allNodes.add(node);
            }
        }
        return allNodes;
    }
}
