package com.augurit.aplanmis.mall.userCenter.service.impl;

import com.augurit.agcloud.bsc.domain.BscAttDetail;
import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.bsc.domain.BscAttLink;
import com.augurit.agcloud.bsc.domain.BscAttStoreDb;
import com.augurit.agcloud.bsc.mapper.BscAttDetailMapper;
import com.augurit.agcloud.bsc.mapper.BscAttMapper;
import com.augurit.agcloud.bsc.sc.att.service.IBscAttService;
import com.augurit.agcloud.bsc.upload.MongoDbAchieve;
import com.augurit.agcloud.bsc.upload.UploadType;
import com.augurit.agcloud.bsc.upload.factory.UploaderFactory;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaHiItemInoutinst;
import com.augurit.aplanmis.common.domain.AeaHiItemMatinst;
import com.augurit.aplanmis.common.mapper.AeaHiItemInoutinstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiItemMatinstMapper;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.service.instance.AeaHiItemInoutinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiItemMatinstService;
import com.augurit.aplanmis.common.utils.SessionUtil;
import com.augurit.aplanmis.common.vo.LoginInfoVo;
import com.augurit.aplanmis.mall.userCenter.service.RestFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

@Service
@Transactional
public class RestFileServiceImpl implements RestFileService {
    private static Logger logger = LoggerFactory.getLogger(RestFileServiceImpl.class);
    @Autowired
    private AeaHiItemInoutinstService aeaHiItemInoutinstService;
    @Autowired
    private AeaHiItemMatinstService aeaHiItemMatinstService;
    @Autowired
    private AeaHiItemInoutinstMapper aeaHiItemInoutinstMapper;
    @Autowired
    private BscAttDetailMapper bscAttDetailMapper;
    @Autowired
    protected IBscAttService bscAttService;
    @Autowired
    private AeaHiItemMatinstMapper aeaHiItemMatinstMapper;
    @Autowired
    private BscAttMapper bscAttMapper;
    @Autowired
    private FileUtilsService fileUtilsService;
    @Autowired
    private UploaderFactory uploaderFactory;

    private static int BUFFER_SIZE = 2048;
    @Value("${mall.check.authority:false}")
    private boolean isCheckAuthority;
    @Override
    public List<BscAttFileAndDir> getAttFiles(String matinstId) throws Exception {
        String[] recordIds = {matinstId};
        List<BscAttFileAndDir> bscAttFileAndDirs = new ArrayList<BscAttFileAndDir>();
        if (recordIds.length > 0) {
            bscAttFileAndDirs.addAll(bscAttDetailMapper.searchFileAndDirsSimple(null, SecurityContext.getCurrentOrgId(), "AEA_HI_ITEM_MATINST", "MATINST_ID", recordIds));
        }
        return bscAttFileAndDirs;
    }

    @Override
    public List<BscAttFileAndDir> getAttFilesByPK(String tableName,String pkName,String recordId) throws Exception {
        String[] recordIds = {recordId};
        List<BscAttFileAndDir> bscAttFileAndDirs = new ArrayList<BscAttFileAndDir>();
        if (recordIds.length > 0) {
            bscAttFileAndDirs.addAll(bscAttDetailMapper.searchFileAndDirsSimple(null, SecurityContext.getCurrentOrgId(), tableName, pkName, recordIds));
        }
        return bscAttFileAndDirs;
    }

    @Override
    public ResultForm delelteAttachment(String[] detailIds, String matinstId) throws Exception {
        boolean flag = delelteFiles(detailIds, matinstId);
        AeaHiItemMatinst aeaHiItemMatinst = aeaHiItemMatinstService.getAeaHiItemMatinstById(matinstId);
        if (aeaHiItemMatinst == null) {
            throw new InvalidParameterException(aeaHiItemMatinst);
        }
        String[] recordIds = {matinstId};
        List<BscAttFileAndDir> bscAttFileAndDirs = bscAttDetailMapper.searchFileAndDirsSimple(null, SecurityContext.getCurrentOrgId(), "AEA_HI_ITEM_MATINST", "MATINST_ID", recordIds);
        ContentResultForm resultForm = new ContentResultForm(flag);
        if (bscAttFileAndDirs.size() < 1) {
            AeaHiItemInoutinst aeaHiItemInoutinst = new AeaHiItemInoutinst();
            aeaHiItemInoutinst.setMatinstId(matinstId);
            List<AeaHiItemInoutinst> aeaHiItemInoutinsts = aeaHiItemInoutinstMapper.listAeaHiItemInoutinst(aeaHiItemInoutinst);
            for (AeaHiItemInoutinst aeaHiItemInoutinst1 : aeaHiItemInoutinsts) {
                if (StringUtils.isNotBlank(aeaHiItemInoutinst1.getInoutinstId()))
                    aeaHiItemInoutinstMapper.deleteAeaHiItemInoutinst(aeaHiItemInoutinst1.getInoutinstId());
            }
            aeaHiItemMatinstService.deleteAeaHiItemMatinstById(matinstId);
            //resultForm.setContent("");
        } else {
            aeaHiItemMatinst.setAttCount(new Long(bscAttFileAndDirs.size()));
            aeaHiItemMatinstMapper.updateAeaHiItemMatinst(aeaHiItemMatinst);
        }
        resultForm.setContent(matinstId);
        return resultForm;
    }

    @Override
    public void delelteAttachmentByCloud(String[] detailIds, String recordId) throws Exception {
        delelteFiles(detailIds, recordId);
    }

    private boolean delelteFiles(String[] detailIds, String recordId) throws Exception {
        boolean flag = false;
        for (String detailId : detailIds) {
            if (StringUtils.isBlank(detailId)) {
                return false;
            }
//            List<BscAttDetail> linkList = aeaHiItemMatinstMapper.getAeaHiItemMatinstFile("AEA_HI_ITEM_MATINST", "MATINST_ID", "", new String[]{recordId}, null);
            List<BscAttLink> linkList = aeaHiItemMatinstMapper.getBscAttLinkByDetailId(detailId);

            if (linkList.size() > 1) {
                BscAttLink bscAttLink = new BscAttLink();
                bscAttLink.setRecordId(recordId);
                bscAttLink.setDetailId(detailId);
                aeaHiItemMatinstMapper.deleteBscAttLink(bscAttLink);
                flag = true;
            } else {
                BscAttDetail form = bscAttService.getAttDetailByDetailId(detailId);
                flag = this.deleteAttachmentRecordAndFileByDetailId(form);
                if (flag) {
                    bscAttMapper.deleteAttLinkByDetailId(detailId);
                    bscAttDetailMapper.deleteBscAttDetail(detailId);
                }
            }
        }
        return flag;
    }

    //删除附件
    public boolean deleteAttachmentRecordAndFileByDetailId(BscAttDetail form) throws Exception {
        boolean result = false;
        if (form != null) {
            boolean success = false;
            if (!StringUtils.isEmpty(form.getAttPath())) {
                success = this.deleteAttachmentRecordAndFileByDetailId(form.getAttPath(), form.getAttCode() + "." + form.getAttFormat(), true);
            } else {
                success = true;
            }
            if (success && UploadType.DATABASE.getValue().equals(form.getStoreType())) {
                bscAttService.deleteAttDetailCascadeByDetailId(form.getDetailId());
                result = true;
            } else {
                result = success;
            }
        }
        return result;
    }

    private boolean deleteAttachmentRecordAndFileByDetailId(String atPath, String atDiskName, boolean isAbsoluteFilePath) throws Exception {
        boolean result = false;
        if (atPath != null && atPath.trim().length() > 0 && atDiskName != null && atDiskName.trim().length() > 0) {
            atPath = atPath.replace('/', '\\');
            String filePath = isAbsoluteFilePath ? atPath : this.getRequest().getRealPath("\\");
            String wholeFilePath = filePath + "\\" + atDiskName;
            return deletePhysicalFile(wholeFilePath).isSuccess();
        } else {
            return result;
        }
    }

    private HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    private ResultForm deletePhysicalFile(String wholeFilePath) {
        boolean result = false;
        try {
            File file = new File(wholeFilePath);
            if (file.exists()) {
                result = file.delete();
                return new ResultForm(result, "实际文件被删除!");
            } else {
                return new ResultForm(true, "实际文件已经不存在!");
            }
        } catch (Exception var5) {
            var5.printStackTrace();
            return new ResultForm(false, var5.getMessage());
        }
    }

    @Override
    public ModelAndView preview(String detailId, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        BscAttDetail att = bscAttService.getAttDetailByDetailId(detailId);
        if (att != null) {
            //文档类型使用NTKO预览
            if (Pattern.matches("(?i).+\\.(doc|docx|xls|xlsx|pdf|ppt|pptx)$", att.getAttName())) {
                String url = "/rest/file/att/read";
                String jumpUrl = "/rest/ntko/ntkoOpenWin?online=true&suffixName=." + att.getAttFormat() + "&fileUrl=" + url + "?detailId=" + detailId;
                jumpUrl = URLEncoder.encode(jumpUrl, "UTF-8");
                modelAndView.addObject("jumpUrl", jumpUrl);
                modelAndView.setViewName("ntko/ntkoOpenWin");
                return modelAndView;
            }
            if (Pattern.matches("(?i).+\\.(txt|text)$", att.getAttName())) {
                    /*redirectAttributes.addAttribute("detailId", detailId);
                    modelAndView.setViewName("redirect:/rest/mats/att/read");*/
                fileUtilsService.readFile(detailId, request, response);
                return modelAndView;
            }
            //其他类型直接下载
            if (!Pattern.matches("(?i).+\\.(png|jpg|jpeg|bmp|gif|svg|tiff|txt)$", att.getAttName())) {
                fileUtilsService.downloadAttachment(detailId.split(","), response, request, false);
                    /*redirectAttributes.addAttribute("detailIds", detailId);
                    modelAndView.setViewName("redirect:/rest/mats/att/download");*/
                return modelAndView;
            }
            //图片类型使用jquery-viewer预览
            modelAndView.setViewName("preview/viewPic");
            String atName = att.getAttName();
            String attFormat = att.getAttFormat();
            if (false) {
                modelAndView.addObject("emtpyResult", "1");
            } else {
                byte[] content;
                String base64Content = null;
                try {
                    if (UploadType.MONGODB.getValue().equals(att.getStoreType())) {
                        //从MongoDB上下载
                        MongoDbAchieve mongoDbAchieve = (MongoDbAchieve) uploaderFactory.create(att.getStoreType());
                        content = mongoDbAchieve.getDownloadBytes(att.getDetailId());

                        if (content != null && content.length > 0) {
//                            base64Content = new BASE64Encoder().encode(content);
                            Base64.Encoder encoder = Base64.getEncoder();
                            base64Content = encoder.encodeToString(content);
                        } else {
                            modelAndView.addObject("emtpyResult", "1");
                        }
                    } else if (UploadType.DATABASE.getValue().equals(att.getStoreType())) {
                        List<BscAttStoreDb> stores = bscAttService.findAttStoreDbByIds(att.getDetailId());
                        content = stores.get(0).getAttContent();
                        if (content != null && content.length > 0) {
//                            base64Content = new BASE64Encoder().encode(content);
                            Base64.Encoder encoder = Base64.getEncoder();
                            base64Content = encoder.encodeToString(content);
                        } else {
                            modelAndView.addObject("emtpyResult", "1");
                        }
                    }
                    base64Content = base64Content.replaceAll("\r\n|\n", "");
                    String fullBase64 = "data:image/" + attFormat + ";base64," + base64Content;
                    modelAndView.addObject("fullBase64", fullBase64);
                    modelAndView.addObject("fileName", atName);
                    modelAndView.addObject("fileType", attFormat);
                    modelAndView.addObject("emtpyResult", "0");
                } catch (Exception e) {
                    modelAndView.addObject("emtpyResult", "1");
                }
            }
        } else {
            modelAndView.addObject("emtpyResult", "1");
        }
        return modelAndView;
    }
    @Override
    public Boolean isAllowFileType(HttpServletRequest request) {

        StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request;
        List<MultipartFile> files = req.getFiles("file");
        AtomicReference<Boolean> isAllowFileType = new AtomicReference<>(true);
        String[] fileTypes = {".jpg", ".png", ".rar", ".txt", ".zip", ".doc", ".ppt", ".xls", ".pdf", ".docx", ".xlsx"};

        files.stream().forEach(file->{
            String fileType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")).toLowerCase();
            if (!Arrays.asList(fileTypes).contains(fileType)) {
                isAllowFileType.set(false);
            }
        });
        return isAllowFileType.get();
    }

}
