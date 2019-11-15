package com.augurit.aplanmis.common.service.file.impl;

import com.augurit.agcloud.bsc.domain.BscAttDetail;
import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.domain.BscAttStoreDb;
import com.augurit.agcloud.bsc.mapper.BscAttDetailMapper;
import com.augurit.agcloud.bsc.sc.att.service.IBscAttService;
import com.augurit.agcloud.bsc.upload.MongoDbAchieve;
import com.augurit.agcloud.bsc.upload.UploadType;
import com.augurit.agcloud.bsc.upload.factory.UploaderFactory;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author xiaohutu
 */
@Service
public class FileUtilsServiceImpl extends FileAbstractService {
    private static final int BUFFER_SIZE = 1024;
    @Autowired
    private IBscAttService bscAttService;
    @Autowired
    private BscAttDetailMapper bscAttDetailMapper;
    @Autowired
    private UploaderFactory uploaderFactory;

    /**
     * ，
     * 根据detailId下载文件、批量或单独
     *
     * @param detailIds BSC_ATT_DETAIL表的主键 每个文件存储后对应的唯一值
     * @param response  HttpServletResponse
     * @param request   HttpServletRequest
     * @param request   isDownloadCovertPdf 是否下载转换的pdf文件，默认false
     * @return true || false
     * @throws Exception e
     */
    @Override
    public boolean downloadAttachment(String[] detailIds, HttpServletResponse response, HttpServletRequest request, Boolean isDownloadCovertPdf) throws Exception {
        boolean canZip = false;
        if (detailIds.length == 1) {
            BscAttDetail form = bscAttService.getAttDetailByDetailId(detailIds[0]);
            if (null == form) {
                response.reset();
                PrintWriter writer = response.getWriter();
                writer.write("所需下载的文件不存在！");
                writer.flush();
                writer.close();
                return false;
            }
            if (isDownloadCovertPdf != null && isDownloadCovertPdf) {
                canZip = this.downloadPdfs(Arrays.asList(detailIds), response, request);
            } else {
                canZip = this.downloadFileStrategy(detailIds[0], response);
            }
        } else if (detailIds.length > 1) {
            List<BscAttDetail> formList = this.bscAttService.getAttDetailsByDetailIds(detailIds);
            if (formList != null && formList.size() > 0) {
                Date currentDate = new Date();
                try {
                    if (isDownloadCovertPdf != null && isDownloadCovertPdf) {
                        canZip = this.downloadPdfs(Arrays.asList(detailIds), response, request);
                        return canZip;
                    } else {
                        canZip = this.zipFile(request, currentDate, formList);
                    }
                } catch (Exception var9) {
                    var9.printStackTrace();
//                    ServletOutputStream out = response.getOutputStream();
//                    out.print("所需下载的文件不存在！");
//                    out.print(URLEncoder.encode("所需下载的文件不存在！", "utf-8"));
                    //上面输入中文如果系统没有设置UTF-8会报错；stream输出的二进制的流，没有对字符进行编码，stream只能够使用iso 8859-1编码的字符，writer输出的文本信息，是经过系统编码之后输出的。
                    // 使用以下方法
                    response.reset();
                    PrintWriter writer = response.getWriter();
                    writer.write("所需下载的文件不存在！");
                    writer.flush();
                    writer.close();
                }
                if (canZip) {
                    canZip = this.downloadAttachment(response, generateZipFilePath(request), this.generateZipFileName(currentDate));
                }
            }
        }
        return canZip;
    }

    @Override
    public boolean downloadAttachment(String detailId, HttpServletResponse response, HttpServletRequest request, Boolean isDownloadCovertPdf) throws Exception {
        String[] detailIds = {detailId};
        return this.downloadAttachment(detailIds, response, request, isDownloadCovertPdf);
    }

    @Override
    public boolean deleteAttachment(String detailId) throws Exception {
        if (StringUtils.isBlank(detailId)) throw new Exception("detailIds不能为空");
        return delete(detailId);
    }

    @Override
    public boolean deleteAttachments(String[] detailIds) throws Exception {
        if (null == detailIds || detailIds.length < 1) return false;
        boolean flag = true;
        for (String detailId : detailIds) {
            if (StringUtils.isNotBlank(detailId))
                flag = delete(detailId);
        }
        return true;
    }

    @Override
    public List<BscAttForm> getAttachmentsByRecordId(String[] recordIds, String tableName, String pkName) throws Exception {
        List<BscAttForm> bscAttForms = new ArrayList();
        /*if (recordIds.length < 1) throw new Exception("业务ID数组长度为0");
        if (StringUtils.isBlank(tableName)) throw new Exception("业务表名为空");
        if (StringUtils.isBlank(pkName)) throw new Exception("业务主键为空");*/
        if (null == recordIds || recordIds.length < 1) return bscAttForms;
        if (StringUtils.isBlank(tableName) || StringUtils.isBlank(pkName)) return bscAttForms;


        for (String recordId : recordIds) {
            if (StringUtils.isNotBlank(recordId))
                bscAttForms.addAll(bscAttService.listAttLinkAndDetailByTablePKRecordId(tableName, pkName, recordId, SecurityContext.getCurrentOrgId()));
        }
        return bscAttForms;
    }

    @Override
    public void uploadAttachments(String tableName, String pkName, String recordId, String dirId, HttpServletRequest request) throws Exception {
        if (StringUtils.isBlank(tableName)) throw new Exception("业务表名不能为空");
        if (StringUtils.isBlank(pkName)) throw new Exception("业务主键不能为空");
        if (StringUtils.isBlank(recordId)) throw new Exception("业务ID不能为空");

        StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request;
        //List<MultipartFile> files = req.getFiles("file");
        List<MultipartFile> files = req.getMultiFileMap().get("files");
        if (files == null) return;
        for (MultipartFile file : files) {
            if (file.getSize() <= 0 && StringUtils.isBlank(file.getOriginalFilename())) {
                continue;
            }
            upload(tableName, pkName, recordId, dirId, file);
        }
    }

    @Override
    public void uploadAttachments(String tableName, String pkName, String recordId, List<MultipartFile> files) throws Exception {
        if (StringUtils.isBlank(tableName)) throw new Exception("业务表名不能为空");
        if (StringUtils.isBlank(pkName)) throw new Exception("业务主键不能为空");
        if (StringUtils.isBlank(recordId)) throw new Exception("业务ID不能为空");
        for (MultipartFile file : files) {
            if (file.getSize() <= 0 && StringUtils.isBlank(file.getOriginalFilename())) {
                continue;
            }
            upload(tableName, pkName, recordId, null, file);
        }
    }

    /**
     * 根据材料实例ID 查询附件列表
     *
     * @param matinstId 材料实例ID
     * @return List<BscAttFileAndDir>
     * @throws Exception
     */
    @Override
    public List<BscAttFileAndDir> getMatAttDetailByMatinstId(String matinstId) throws Exception {
        String[] recordIds = matinstId.split(",");
        List<BscAttFileAndDir> bscAttFileAndDirs = new ArrayList<BscAttFileAndDir>();
        if (recordIds.length > 0) {
            List<BscAttFileAndDir> attFileList = bscAttDetailMapper.searchFileAndDirsSimple(null, null, "AEA_HI_ITEM_MATINST", "MATINST_ID", recordIds);
            bscAttFileAndDirs.addAll(attFileList == null ? new ArrayList<>() : attFileList);
        }
        return bscAttFileAndDirs;
    }

    //文件上传
    @Override
    public BscAttForm upload(String tableName, String pkName, String recordId, String dirId, MultipartFile file) throws Exception {
        return super.upload(tableName, pkName, recordId, dirId, file);
    }

    @Override
    public List<BscAttFileAndDir> getBscAttFileAndDirListByinstId(String instId, String pkName, String tableName) throws Exception {
        return super.getBscAttFileAndDirListByinstId(instId, pkName, tableName);
    }

    @Override
    public ModelAndView preview(String detailId, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        BscAttDetail att = bscAttService.getAttDetailByDetailId(detailId);
        if (att != null) {
            //文档类型使用NTKO预览
            if (Pattern.matches("(?i).+\\.(doc|docx|xls|xlsx|pdf|ppt|pptx)$", att.getAttName())) {
                String url = "/rest/mats/att/read";
                String jumpUrl = "/rest/ntko/ntkoOpenWin?online=true&suffixName=." + att.getAttFormat() + "&fileUrl=" + url + "?detailId=" + detailId;
                jumpUrl = URLEncoder.encode(jumpUrl, "UTF-8");
                modelAndView.addObject("jumpUrl", jumpUrl);
                modelAndView.setViewName("ntko/ntkoOpenWin");
                return modelAndView;
            }
            if (Pattern.matches("(?i).+\\.(txt|text)$", att.getAttName())) {
                /*redirectAttributes.addAttribute("detailId", detailId);
                modelAndView.setViewName("redirect:/rest/mats/att/read");*/
                this.readFile(detailId, request, response);
                return modelAndView;
            }
            //其他类型直接下载
            if (!Pattern.matches("(?i).+\\.(png|jpg|jpeg|bmp|gif|svg|tiff|txt)$", att.getAttName())) {
                this.downloadAttachment(detailId.split(","), response, request, false);
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

    /**
     * 读取电子件文档流
     *
     * @param detailId 文件ID
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws Exception
     */
    @Override
    public void readFile(String detailId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (StringUtils.isBlank(detailId)) {
            throw new InvalidParameterException(detailId);
        }
        BscAttDetail bscAttDetail = bscAttDetailMapper.getBscAttDetailById(detailId);
        if (bscAttDetail == null) {
            throw new InvalidParameterException(bscAttDetail);
        }
        OutputStream toClient = null;
        //从ftp获取数据
        byte[] buffer = null;
        if (UploadType.MONGODB.getValue().equals(bscAttDetail.getStoreType())) { //从MongoDB上下载
            MongoDbAchieve mongoDbAchieve = (MongoDbAchieve) uploaderFactory.create(bscAttDetail.getStoreType());
            buffer = mongoDbAchieve.getDownloadBytes(bscAttDetail.getDetailId());
        }
        try {
            response.reset();
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.addHeader("Content-Length", "" + buffer.length);   // 设置response的Header
            response.setCharacterEncoding("UTF-8");
            if ("txt".equals(bscAttDetail.getAttFormat())) {
                response.setContentType("text/plain");
            }
            toClient = new BufferedOutputStream(response.getOutputStream());
            toClient.write(buffer);
            toClient.flush();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (toClient != null) {
                toClient.close();
            }
        }
    }

}
