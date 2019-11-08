package com.augurit.aplanmis.common.service.admin.item.impl;

import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.upload.factory.UploaderFactory;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaItemInout;
import com.augurit.aplanmis.common.domain.AeaItemMat;
import com.augurit.aplanmis.common.dto.InoutMatAddDto;
import com.augurit.aplanmis.common.dto.InoutMatEditDto;
import com.augurit.aplanmis.common.mapper.AeaItemInoutMapper;
import com.augurit.aplanmis.common.mapper.AeaItemMatMapper;
import com.augurit.aplanmis.common.service.admin.item.AeaItemInoutAdminService;
import com.augurit.aplanmis.common.service.admin.item.AeaItemStateFormAdminService;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@Transactional
public class MatService {

    private AeaItemInoutMapper aeaItemInoutMapper;

    private AeaItemMatMapper aeaItemMatMapper;

    private UploaderFactory uploaderFactory;

    @Autowired
    private AeaItemStateFormAdminService aeaItemStateFormAdminService;

    @Autowired
    private AeaItemInoutAdminService aeaItemInoutAdminService;
    @Autowired
    private FileUtilsService fileUtilsService;

    public void addMat(HttpServletRequest request, InoutMatAddDto inoutMatAddDto) throws Exception {

        Assert.notNull(inoutMatAddDto.getItemVerId(), "itemVerId不能为空");
        String matId = addAeaItemMat(request, inoutMatAddDto);
        Assert.notNull(matId, "matId不能为空");
        associateWithInout(matId, inoutMatAddDto);
    }

    private String addAeaItemMat(HttpServletRequest request, InoutMatAddDto inoutMatAddDto) throws Exception {

        String userId = SecurityContext.getCurrentUserId();
        String rootOrgId = SecurityContext.getCurrentOrgId();
        AeaItemMat aeaItemMat = inoutMatAddDto.toAeaItemMat(userId, rootOrgId);
        handleAttachment(request, aeaItemMat);
        aeaItemMatMapper.insertAeaItemMat(aeaItemMat);
        return aeaItemMat.getMatId();
    }

    private void associateWithInout(String matId, InoutMatAddDto inoutMatAddDto) {

        inoutMatAddDto.setMatId(matId);
        String userId = SecurityContext.getCurrentUserId();
        String rootOrgId = SecurityContext.getCurrentOrgId();
        Long inSortNo = aeaItemInoutAdminService.getMaxSortNo(inoutMatAddDto.getItemVerId(), inoutMatAddDto.getStateVerId(), inoutMatAddDto.getIsInput(), rootOrgId);
        Long stateFormSortNo = 1L;
        // 为了排序方便
        if (StringUtils.isNotBlank(inoutMatAddDto.getIsInput()) && inoutMatAddDto.getIsInput().equals(Status.ON)) {
            stateFormSortNo = aeaItemStateFormAdminService.getMaxSortNo(inoutMatAddDto.getItemVerId(), inoutMatAddDto.getStateVerId());
        }
        AeaItemInout aeaItemInout = inoutMatAddDto.toAeaItemInout(userId, rootOrgId);
        aeaItemInout.setSortNo(inSortNo >= stateFormSortNo ? inSortNo : stateFormSortNo);
        aeaItemInoutMapper.insertAeaItemInout(aeaItemInout);
    }

    public void editMat(HttpServletRequest request, InoutMatEditDto inoutMatEditDto) throws Exception {

        Assert.notNull(inoutMatEditDto.getMatId(), "matId不能为空");
        Assert.notNull(inoutMatEditDto.getMatCode(), "matCode不能为空");
        String userId = SecurityContext.getCurrentUserId();
        AeaItemMat aeaItemMat = aeaItemMatMapper.selectOneById(inoutMatEditDto.getMatId());
        if (aeaItemMat == null) {
            throw new RuntimeException("无法找到需要修改的材料, 请联系管理员, matId: " + inoutMatEditDto.getMatId());
        }
        aeaItemMat = inoutMatEditDto.merge(aeaItemMat, userId);
        handleAttachment(request, aeaItemMat);
        aeaItemMatMapper.updateAeaItemMat(aeaItemMat);
    }

    private AeaItemMat handleAttachment(HttpServletRequest request, AeaItemMat aeaItemMat) throws Exception {

        StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request;
        List<MultipartFile> templateDocFiles = req.getFiles("templateDocFile");
        List<MultipartFile> sampleDocFiles = req.getFiles("sampleDocFile");
        List<MultipartFile> reviewSampleDocFiles = req.getFiles("reviewSampleDocFile");
        handleAttr("0", templateDocFiles, aeaItemMat);
        handleAttr("1", sampleDocFiles, aeaItemMat);
        handleAttr("2", reviewSampleDocFiles, aeaItemMat);
        return aeaItemMat;
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
                   /* UploadFileCmd uploadFileCmd = uploaderFactory.createDefaultUpload(file, aeaItemMat.getMatId(), "AEA_ITEM_MAT", pkName, UploadType.MONGODB.getValue());
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

    @Autowired
    public void setAeaItemInoutMapper(AeaItemInoutMapper aeaItemInoutMapper) {
        this.aeaItemInoutMapper = aeaItemInoutMapper;
    }

    @Autowired
    public void setAeaItemMatMapper(AeaItemMatMapper aeaItemMatMapper) {
        this.aeaItemMatMapper = aeaItemMatMapper;
    }

    @Autowired
    public void setUploaderFactory(UploaderFactory uploaderFactory) {
        this.uploaderFactory = uploaderFactory;
    }
}
