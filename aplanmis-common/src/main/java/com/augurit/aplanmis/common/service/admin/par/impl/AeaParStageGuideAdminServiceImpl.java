package com.augurit.aplanmis.common.service.admin.par.impl;


import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaParStageGuide;
import com.augurit.aplanmis.common.mapper.AeaParStageGuideMapper;
import com.augurit.aplanmis.common.service.admin.par.AeaParStageGuideAdminService;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author ZhangXinhui
 * @date 2019/7/9 009 17:04
 * @desc
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class AeaParStageGuideAdminServiceImpl implements AeaParStageGuideAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaParStageGuideAdminServiceImpl.class);
    @Autowired
    private AeaParStageGuideMapper aeaParStageGuideMapper;
    @Autowired
    private FileUtilsService fileUtilsService;

    @Override
    public AeaParStageGuide getByStageId(String stageId, String rootOrgId) {

        if (StringUtils.isBlank(rootOrgId)) {
            rootOrgId = SecurityContext.getCurrentOrgId();
        }
        return aeaParStageGuideMapper.getAeaParStageGuideByStageId(stageId, rootOrgId);
    }

    @Override
    public AeaParStageGuide getById(String recId) throws Exception {
        return aeaParStageGuideMapper.getAeaParStageGuideById(recId);
    }

    @Override
    public Map getById2(String recId) throws Exception {
        return aeaParStageGuideMapper.getAeaParStageGuideById2(recId);
    }

    @Override
    public int saveAeaParStageGuide(AeaParStageGuide aeaParStageGuide, HttpServletRequest request) throws Exception {
        saveAttFile(aeaParStageGuide, request);
        return aeaParStageGuideMapper.updateAeaParStageGuide(aeaParStageGuide);
    }

    @Override
    public int updateAeaParStageGuide(AeaParStageGuide aeaParStageGuide) throws Exception {
        return aeaParStageGuideMapper.updateAeaParStageGuide(aeaParStageGuide);
    }

    @Override
    public void insertAeaParStageGuide(AeaParStageGuide aeaParStageGuide, HttpServletRequest request) throws Exception {
        saveAttFile(aeaParStageGuide, request);
        aeaParStageGuide.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaParStageGuideMapper.insertAeaParStageGuide(aeaParStageGuide);
    }

    @Override
    public void saveAttFile(AeaParStageGuide aeaParStageGuide, HttpServletRequest request) throws Exception {

        StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request;
        aeaParStageGuide
                .setWsbllct(getSampleFile(req, "wsbllctSample", "AEA_PAR_STAGE_GUIDE", "WSBLLCT", aeaParStageGuide.getGuideId()));
        aeaParStageGuide
                .setCkbllct(getSampleFile(req, "ckbllctSample", "AEA_PAR_STAGE_GUIDE", "CKBLLCT", aeaParStageGuide.getGuideId()));
        aeaParStageGuide.setGuideAtt(getSampleFile(req, "guideAttFile", "AEA_PAR_STAGE_GUIDE", "GUIDE_ATT", aeaParStageGuide
                .getGuideId()));
    }

    @Override
    public void delStageGuideAttFile(String detailId, String guideId, String type) throws Exception {

        AeaParStageGuide aeaParStageGuide = aeaParStageGuideMapper.getAeaParStageGuideById(guideId);
        if (aeaParStageGuide != null) {
            if (type.equals("WSBLLCT")) {
                if (StringUtils.isNotBlank(aeaParStageGuide.getWsbllct())) {
                    aeaParStageGuide.setWsbllct(handleStageGuideWsCk(aeaParStageGuide.getWsbllct(), detailId));
                }
            } else if (type.equals("CKBLLCT")) {
                if (StringUtils.isNotBlank(aeaParStageGuide.getCkbllct())) {
                    aeaParStageGuide.setCkbllct(handleStageGuideWsCk(aeaParStageGuide.getCkbllct(), detailId));
                }
            } else if (type.equals("GUIDE_ATT")) {
                if (StringUtils.isNotBlank(aeaParStageGuide.getGuideAtt())) {
                    aeaParStageGuide.setGuideAtt(handleStageGuideWsCk(aeaParStageGuide.getGuideAtt(), detailId));
                }
            }
            this.updateAeaParStageGuide(aeaParStageGuide);
        }
//        attachmentAdminService.deleteFileStategy(detailId);
        fileUtilsService.deleteAttachment(detailId);
    }

    private String handleStageGuideWsCk(String bizField, String detailId) {

        if (StringUtils.isNotBlank(bizField)) {
            String replaceStr = bizField.replaceAll(detailId + ",", "");
            return replaceStr;
        }
        return bizField;
    }

    private String getSampleFile(StandardMultipartHttpServletRequest request, String fileName, String tableName, String pkName, String recordId) throws Exception {

        StringBuilder ids = new StringBuilder();
        List<MultipartFile> files = request.getFiles(fileName);
        if (files != null && files.size() > 0) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
//                    UploadFileCmd uploadFileCmd = uploaderFactory.createDefaultUpload(file, recordId, tableName, pkName, UploadType.MONGODB.getValue());
//                    String detailId = attachmentAdminService.uploadFileStrategy(uploadFileCmd);
                    BscAttForm form = fileUtilsService.upload(tableName, pkName, recordId, null, file);
                    if (null != form) {
                        ids.append(form.getDetailId()).append(",");
                    }
                }
            }
        }
        return ids.toString();
    }
}
