package com.augurit.aplanmis.common.service.file.impl;

import com.augurit.agcloud.bsc.domain.BscAttDetail;
import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.mapper.BscAttDetailMapper;
import com.augurit.agcloud.bsc.mapper.BscAttMapper;
import com.augurit.agcloud.bsc.sc.att.utils.AttUtils;
import com.augurit.agcloud.bsc.sc.init.service.BscInitService;
import com.augurit.agcloud.bsc.upload.MongoDbAchieve;
import com.augurit.agcloud.bsc.upload.UploadFileCmd;
import com.augurit.agcloud.bsc.upload.UploadFileStrategy;
import com.augurit.agcloud.bsc.upload.UploadType;
import com.augurit.agcloud.bsc.upload.factory.UploaderFactory;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.service.file.LocalUploadConfig;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

public abstract class FileAbstractService implements FileUtilsService {

    private static Logger logger = LoggerFactory.getLogger(FileAbstractService.class);

    private static final int BUFFER_SIZE = 1024;

    @Autowired
    private LocalUploadConfig localUploadConfig;
    @Autowired
    private BscAttMapper bscAttMapper;
    @Autowired
    private UploaderFactory uploaderFactory;
    @Autowired
    private BscInitService bscInitService;
    @Autowired
    private BscAttDetailMapper bscAttDetailMapper;

    /**
     * 文件下载
     *
     * @param response HttpServletResponse
     * @param atPath   文件路径
     * @param atName   文件名字
     * @return boolean
     * @throws Exception e
     */
    boolean downloadAttachment(HttpServletResponse response, String atPath, String atName) throws Exception {
        boolean retState = true;
        try {
            String path = atPath + atName;
            File file = new File(path);
            if (file.exists()) {
                InputStream ins = new FileInputStream(path);
                BufferedInputStream bins = new BufferedInputStream(ins);
                OutputStream outs = response.getOutputStream();
                BufferedOutputStream bouts = new BufferedOutputStream(outs);
                response.setContentType("application/octet-stream");
                response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(atName, "UTF-8"));
                byte[] buffer = new byte[8192];
                int bytesRead = 0;
                while ((bytesRead = bins.read(buffer, 0, 8192)) != -1) {
                    bouts.write(buffer, 0, bytesRead);
                }
                bouts.flush();
                ins.close();
                bins.close();
                outs.close();
                bouts.close();
            } else {
                response.sendRedirect("../error.jsp");
                retState = false;
            }

            return retState;
        } catch (IOException var14) {
            retState = false;
            throw var14;
        }
    }

    String generateZipFilePath(HttpServletRequest request) {
        return request.getServletContext().getRealPath("\\") + "\\download_temp\\";
    }

    boolean zipFile(HttpServletRequest request, Date currentDate, List<BscAttDetail> ats) throws Exception {
        String zipFilePath = this.generateZipFilePath(request) + this.generateZipFileName(currentDate);
        File dir = new File(this.generateZipFilePath(request));
        if (!dir.exists()) {
            dir.mkdirs();
        }
        FileInputStream fis = null;
        ByteArrayInputStream in = null;
        FileOutputStream outtemp = null;
        File tempFile = null;
        boolean retState = true;
        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFilePath));
            byte[] buffer = new byte[2048];
            Iterator var13 = ats.iterator();
            while (var13.hasNext()) {
                BscAttDetail form = (BscAttDetail) var13.next();
                out.putNextEntry(new ZipEntry(form.getAttName()));
                out.setEncoding("GBK");
                int available;
                UploadFileStrategy uploadFileStrategy = uploaderFactory.create(form.getStoreType());
                InputStream inputStream = uploadFileStrategy.download(form.getDetailId());
                //byte[] bytes = AttUtils.inputStreamToBytesAndClose(inputStream);
                //in = (ByteArrayInputStream) uploadFileStrategy.download(form.getDetailId());
                /*if(null==in){
                    return false;
                }*/
                while ((available = inputStream.read(buffer)) != -1) {
                    if (available < 2048) {
                        out.write(buffer, 0, available);
                    } else {
                        out.write(buffer, 0, 2048);
                    }
                }
                out.closeEntry();
            }
            out.close();
        } catch (Exception var29) {
            retState = false;
            throw var29;
        } finally {
            if (fis != null) {
                fis.close();
            }
            if (in != null) {
                in.close();
            }
            if (outtemp != null) {
                outtemp.close();
            }
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }

        return retState;
    }

    boolean zipPdfFile(HttpServletRequest request, Date currentDate, Map<String, InputStream> fileStreamMap) throws Exception {
        String zipFilePath = this.generateZipFilePath(request) + this.generateZipFileName(currentDate);
        File dir = new File(this.generateZipFilePath(request));
        if (!dir.exists()) {
            dir.mkdirs();
        }
        FileInputStream fis = null;
        ByteArrayInputStream in = null;
        FileOutputStream outtemp = null;
        File tempFile = null;
        boolean retState = false;
        if (fileStreamMap != null) {
            try {
                ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFilePath));
                byte[] buffer = new byte[2048];
                Set<Map.Entry<String, InputStream>> entrySet = fileStreamMap.entrySet();
                for (Map.Entry<String, InputStream> entry : entrySet) {
                    String fileName = entry.getKey();
                    InputStream inputStream = entry.getValue();
                    out.putNextEntry(new ZipEntry(fileName));
                    out.setEncoding("GBK");
                    int available;
                    while ((available = inputStream.read(buffer)) != -1) {
                        if (available < 2048) {
                            out.write(buffer, 0, available);
                        } else {
                            out.write(buffer, 0, 2048);
                        }
                    }
                    out.closeEntry();
                }
                out.close();
                retState = true;
            } catch (Exception e) {
                return false;
            } finally {
                if (fis != null) {
                    fis.close();
                }
                if (in != null) {
                    in.close();
                }
                if (outtemp != null) {
                    outtemp.close();
                }
                if (tempFile != null && tempFile.exists()) {
                    tempFile.delete();
                }
            }
        }
        return retState;
    }

    String generateZipFileName(Date currentDate) {
        return currentDate.getTime() + ".zip";
    }

    boolean downloadFileStrategy(String detailId, HttpServletResponse response) throws Exception {
        BscAttDetail form = bscAttMapper.getAttDetailByDetailId(detailId);
        UploadFileStrategy uploadFileStrategy = uploaderFactory.create(form.getStoreType());
        InputStream in = uploadFileStrategy.download(detailId);
        ServletOutputStream out = null;
        OutputStream outtemp = null;
        String atName = form.getAttName();
        doDownload(response, in, out, outtemp, atName);
        return true;
    }

    boolean downloadPdfs(List<String> detailIdList, HttpServletResponse response, HttpServletRequest request) throws Exception {
        boolean canZip = false;
        if (detailIdList != null && detailIdList.size() > 0) {
            Map<String, InputStream> inputStreamMap = new HashMap<>();
            for (int i = 0, len = detailIdList.size(); i < len; i++) {
                String detailId = detailIdList.get(i);
                MongoDbAchieve uploadFileStrategy = (MongoDbAchieve) uploaderFactory.create(UploadType.MONGODB.getValue());
                Map<String, InputStream> map = uploadFileStrategy.downloadCovertPdf(detailId);
                inputStreamMap.putAll(map);
            }
            if (inputStreamMap.size() == 1) {
                Set<Map.Entry<String, InputStream>> entrySet = inputStreamMap.entrySet();
                InputStream inputStream = null;
                String attName = System.currentTimeMillis() + "";
                for (Map.Entry<String, InputStream> entry : entrySet) {
                    inputStream = entry.getValue();
                    attName = entry.getKey();
                }
                ServletOutputStream out = null;
                OutputStream outtemp = null;
                doDownload(response, inputStream, out, outtemp, attName);
                canZip = true;
            } else if (inputStreamMap.size() > 1) {
                Date currentDate = new Date();
                boolean zipPdfFile = zipPdfFile(request, currentDate, inputStreamMap);
                if (zipPdfFile) {
                    canZip = this.downloadAttachment(response, generateZipFilePath(request), this.generateZipFileName(currentDate));
                }
            }
        }
        return canZip;
    }

    private void doDownload(HttpServletResponse response, InputStream in, ServletOutputStream out, OutputStream outtemp, String atName) throws Exception {
        try {
            response.reset();
            response.setContentType("application/octet-stream;charset=UTF-8");
            atName = AttUtils.reEncodeChineseFileName(atName, this.getRequest());
            response.setHeader("Content-Disposition", "attachment; filename=" + atName);
            out = response.getOutputStream();
            byte[] buffer = new byte[BUFFER_SIZE];
            int available;
            while ((available = Objects.requireNonNull(in).read(buffer)) != -1) {
                if (available < BUFFER_SIZE) {
                    out.write(buffer, 0, available);
                } else {
                    out.write(buffer, 0, BUFFER_SIZE);
                }
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (outtemp != null) {
                outtemp.close();
            }
        }
    }

    HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    protected boolean delete(String detailId) throws Exception {
        BscAttDetail form = bscAttMapper.getAttDetailByDetailId(detailId);
//        deleteAttachmentRecordAndFileByDetailId(form);
        if (form != null) {
            UploadFileStrategy uploadFileStrategy = uploaderFactory.create(form.getStoreType());
            return uploadFileStrategy.delete(detailId);
        }
        return false;
    }

    //删除附件
    private boolean deleteAttachmentRecordAndFileByDetailId(BscAttDetail form) throws Exception {
        boolean result = false;
        if (form != null) {
            //删除MongoDB的文件
            if (UploadType.MONGODB.getValue().equals(form.getStoreType())) {
                UploadFileStrategy uploadFileStrategy = uploaderFactory.create(UploadType.MONGODB.getValue());
                result = uploadFileStrategy.delete(form.getDetailId());
            } else {

                //删除磁盘的文件
                if (UploadType.FTP.getValue().equals(form.getStoreType())) {
                    if (StringUtils.isNotBlank(form.getAttPath())) {
                        deleteAttachmentRecordAndFileByDetailId(form.getAttPath(), form.getAttCode() + "." + form.getAttFormat(), true);
                    }
                }

                bscAttMapper.deleteAttStoreDbByDetailId(form.getDetailId());
                bscAttMapper.deleteAttLinkByDetailId(form.getDetailId());
                bscAttMapper.deleteAttDetailByDetailId(form.getDetailId());
                result = true;
            }
        }
        return result;
    }

    private boolean deleteAttachmentRecordAndFileByDetailId(String atPath, String atDiskName, boolean isAbsoluteFilePath) {
        try {
            if (atPath != null && atPath.trim().length() > 0 && atDiskName != null && atDiskName.trim().length() > 0) {
                atPath = atPath.replace('/', '\\');
                String filePath = isAbsoluteFilePath ? atPath : getRequest().getRealPath("\\");
                String wholeFilePath = filePath + "\\" + atDiskName;
                File file = new File(wholeFilePath);
                if (file.exists()) file.delete();
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    //上传附件----recordId, tableName, pkName 三个参数必须，新增dirId，可选参数 文件夹id，

    /**
     * @param tableName 业务表名
     * @param pkName    业务表主键
     * @param recordId  业务表主键ID值
     * @param dirId     文件夹ID
     * @param file      文件对象
     * @throws Exception
     */
    public BscAttForm upload(String tableName, String pkName, String recordId, String dirId, MultipartFile file) throws Exception {
        BscAttForm form = buildNewAtForm(tableName, recordId, pkName, null, null, null, file, dirId);
//        UploadFileCmd uploadFileCmd = uploaderFactory.createDefaultUpload(file, recordId, tableName, pkName, UploadType.MONGODB.getValue());
        UploadFileCmd uploadFileCmd = uploaderFactory.createDefaultUpload(file, form, UploadType.MONGODB.getValue());
        String detailId;


        if (localUploadConfig.isOpen()) {//开启本地mongo上传
            logger.info("本地文件上传配置：" + localUploadConfig.toString());
            MongoDbAchieve uploadFileStrategy = (MongoDbAchieve) uploaderFactory.create(uploadFileCmd.getStoreType());
            uploadFileStrategy.setMongodbUri(localUploadConfig.getUrl());
            detailId = uploadFileStrategy.upload(uploadFileCmd);
        } else {
            UploadFileStrategy uploadFileStrategy = uploaderFactory.create(uploadFileCmd.getStoreType());
            detailId = uploadFileStrategy.upload(uploadFileCmd);
        }
        //String detailId = uploadFileStrategy.upload(uploadFileCmd);
        if (logger.isDebugEnabled()) {
            logger.debug("文件上传成功, detailId: {}", detailId);
        }
        form.setDetailId(detailId);
        return form;
    }

    //recordId, tableName, pkName 三个参数必须，新增dirId，可选参数，
    private BscAttForm buildNewAtForm(String tableName, String recordId, String pkName, String atType, String isDbStore, String isEncrypt, MultipartFile attachment, String dirId) {
        String configPathDefault = bscInitService.getConfigValueByConfigKey("uploadPath");
//        String isDbStroDefault = this.bscInitService.getConfigValueByConfigKey("isDbStore");
        String isDbStroDefault = UploadType.MONGODB.getValue();
        String isEncryptDefault = bscInitService.getConfigValueByConfigKey("isEncrypt");
        String isreDefault = bscInitService.getConfigValueByConfigKey("isRelative");
        String iniencryptClass = bscInitService.getConfigValueByConfigKey("encryptClassFullName");
        BscAttForm form = new BscAttForm();
        form.setTableName(tableName);
        form.setEncryptClass(iniencryptClass);
        form.setRecordId(recordId);
        form.setPkName(pkName);
        form.setAttCode((new Date()).getTime() + "");
        form.setDirId(dirId);
        String attName = attachment.getOriginalFilename();
        String[] split = attName.split("\\\\");
        attName = split[split.length - 1];
        form.setAttName(attName);

        form.setAttPath(configPathDefault + "\\" + tableName + "\\" + recordId);
        form.setAttType(atType);
        form.setAttSize(String.valueOf(attachment.getSize()));
        form.setAttFormat(attachment.getOriginalFilename().substring(attachment.getOriginalFilename().lastIndexOf(".") + 1));
        form.setStoreType(isDbStore == null ? isDbStroDefault : isDbStore);
        form.setIsEncrypt(isEncrypt == null || !"0".equals(isEncrypt) && !"1".equals(isEncrypt) ? isEncryptDefault : isEncrypt);
        form.setIsRelativePath(isreDefault);
        form.setAttDiskName(form.getAttCode() + "." + form.getAttFormat());
        return form;
    }

    public List<BscAttFileAndDir> getBscAttFileAndDirListByinstId(String instId, String pkName, String tableName) throws Exception {
        List<BscAttFileAndDir> bscAttFileAndDirs = new ArrayList<BscAttFileAndDir>();
        if (StringUtils.isBlank(instId)) return bscAttFileAndDirs;
        String[] recordIds = instId.split(",");

        if (recordIds.length > 0) {
            List<BscAttFileAndDir> attFileList = bscAttDetailMapper.searchFileAndDirsSimple(null, null, tableName, pkName, recordIds);
            bscAttFileAndDirs.addAll(attFileList == null ? new ArrayList<>() : attFileList);
        }
        return bscAttFileAndDirs;
    }

}
