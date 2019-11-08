package com.augurit.aplanmis.common.service.guide.impl;

import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.sc.dic.code.service.BscDicCodeService;
import com.augurit.agcloud.bsc.upload.factory.UploaderFactory;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaParStageGuide;
import com.augurit.aplanmis.common.mapper.AeaParStageGuideMapper;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.service.guide.AeaParStageGuideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.security.InvalidParameterException;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class AeaParStageGuideServiceImpl implements AeaParStageGuideService {

    @Autowired
    private BscDicCodeService bscDicCodeService;

    @Autowired
    private UploaderFactory uploaderFactory;

    @Autowired
    private AeaParStageGuideMapper aeaParStageGuideMapper;
    @Autowired
    private FileUtilsService fileUtilsService;


    @Override
    public AeaParStageGuide getAeaParStageGuideByStageId(String stageId, String rootOrgId) {

        if (StringUtils.isBlank(stageId)) {
            throw new InvalidParameterException("参数stageId为空！");
        }
        //String rootOrgId = SecurityContext.getCurrentOrgId();
        AeaParStageGuide aeaParStageGuide = aeaParStageGuideMapper.getAeaParStageGuideByStageId(stageId, rootOrgId);
        if (aeaParStageGuide != null && StringUtils.isNotBlank(aeaParStageGuide.getApplyObj())) {
            String applyObjStr = "";
            String applyObj = aeaParStageGuide.getApplyObj();
            String[] applyObjs = applyObj.split(",");
            List<BscDicCodeItem> applyObjList = bscDicCodeService.getActiveItemsByTypeCode("ITEM_FWJGXZ", rootOrgId);
            for (BscDicCodeItem bscDicCodeItem : applyObjList) {
                for (String obj : applyObjs) {
                    if (obj.equals(bscDicCodeItem.getItemCode())) {
                        applyObjStr += bscDicCodeItem.getItemName() + ",";
                    }
                }
            }
            aeaParStageGuide.setApplyObj(StringUtils.isBlank(applyObjStr) ? "" : applyObjStr.substring(0, applyObjStr.length() - 1));
        }
        return aeaParStageGuide;
    }

    @Override
    public AeaParStageGuide getByStageId(String stageId, String rootOrgId) {

        return aeaParStageGuideMapper.getAeaParStageGuideByStageId(stageId, rootOrgId);
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
        fileUtilsService.deleteAttachment(detailId);
//        attachmentAdminService.deleteFileStategy(detailId);
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
                    BscAttForm form = fileUtilsService.upload(tableName, pkName, recordId, null, file);
                    if (null != form) {

                        ids.append(form.getDetailId()).append(",");
                    }
                    /*UploadFileCmd uploadFileCmd = uploaderFactory.createDefaultUpload(file, recordId, tableName, pkName, UploadType.MONGODB.getValue());
                    String detailId = attachmentAdminService.uploadFileStrategy(uploadFileCmd);*/
                }
            }
        }
        return ids.toString();
    }

}
