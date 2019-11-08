package com.augurit.aplanmis.common.utils;

import com.augurit.agcloud.bpm.common.utils.SpringContextHolder;
import com.augurit.agcloud.bsc.domain.BscAttDetail;
import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.mapper.BscAttDetailMapper;
import com.augurit.agcloud.bsc.mapper.BscAttMapper;
import com.augurit.agcloud.bsc.sc.att.service.IBscAttService;
import com.augurit.agcloud.bsc.sc.init.service.BscInitService;
import com.augurit.agcloud.bsc.upload.UploadFileCmd;
import com.augurit.agcloud.bsc.upload.UploadFileStrategy;
import com.augurit.agcloud.bsc.upload.UploadType;
import com.augurit.agcloud.bsc.upload.factory.UploaderFactory;
import com.augurit.agcloud.framework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Date;

public class FileUtilsBase {

    private static final int BUFFER_SIZE = 1024;

    private static Logger logger = LoggerFactory.getLogger(FileUtilsBase.class);

    private static BscInitService bscInitService = SpringContextHolder.getBean(BscInitService.class);
    private static UploaderFactory uploaderFactory = SpringContextHolder.getBean(UploaderFactory.class);
    private static IBscAttService bscAtService = SpringContextHolder.getBean(IBscAttService.class);
    private static BscAttMapper bscAttMapper = SpringContextHolder.getBean(BscAttMapper.class);
    private static BscAttDetailMapper bscAttDetailMapper = SpringContextHolder.getBean(BscAttDetailMapper.class);

    private static BscAttForm buildNewAtForm(String tableName, String recordId, String pkName, String atType, String isDbStore, String isEncrypt, MultipartFile attachment) {
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

    protected static void upload(String tableName, String pkName, String recordId, MultipartFile file) throws Exception {
        BscAttForm form = buildNewAtForm(tableName, recordId, pkName, null, null, null, file);
        UploadFileCmd uploadFileCmd = uploaderFactory.createDefaultUpload(file, recordId, tableName, pkName, UploadType.MONGODB.getValue());
        UploadFileStrategy uploadFileStrategy = uploaderFactory.create(uploadFileCmd.getStoreType());
        String detailId = uploadFileStrategy.upload(uploadFileCmd);
        if (logger.isDebugEnabled()) {
            logger.debug("文件上传成功, detailId: {}", detailId);
        }
        form.setDetailId(detailId);
    }


    protected static void delete(String detailId) throws Exception {
        BscAttDetail form = bscAtService.getAttDetailByDetailId(detailId);
        boolean flag = deleteAttachmentRecordAndFileByDetailId(form);
        if (flag) {
            bscAttMapper.deleteAttLinkByDetailId(detailId);
            bscAttDetailMapper.deleteBscAttDetail(detailId);
        }
    }

    //删除附件
    private static boolean deleteAttachmentRecordAndFileByDetailId(BscAttDetail form) throws Exception {
        boolean result = false;
        if (form != null) {
            //删除MongoDB的文件
            if(UploadType.MONGODB.getValue().equals(form.getStoreType())){
                UploadFileStrategy uploadFileStrategy = uploaderFactory.create(UploadType.MONGODB.getValue());
                result = uploadFileStrategy.delete(form.getDetailId());
            }else
            //删除业务数据库的文件
            if (UploadType.DATABASE.getValue().equals(form.getStoreType())) {
                bscAtService.deleteAttDetailCascadeByDetailId(form.getDetailId());
                result = true;
            }else
            //删除磁盘的文件
            if (!StringUtils.isEmpty(form.getAttPath())) {
                result = deleteAttachmentRecordAndFileByDetailId(form.getAttPath(), form.getAttCode() + "." + form.getAttFormat(), true);
            }
        }
        return result;
    }

    private static boolean deleteAttachmentRecordAndFileByDetailId(String atPath, String atDiskName, boolean isAbsoluteFilePath) {
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

    public static byte[] getFile(String detailId) {
        try {
            if (StringUtils.isBlank(detailId)) {
                return null;
            }

            BscAttDetail form = bscAtService.getAttDetailByDetailId(detailId);

            String storeType = UploadType.MONGODB.getValue();

            if(form!=null){

                storeType = form.getStoreType();
            }
            UploadFileStrategy uploadFileStrategy = uploaderFactory.create(storeType);
            InputStream input = uploadFileStrategy.download(detailId);
            if(input!=null){

                ByteArrayOutputStream output = new ByteArrayOutputStream();
                byte[] buffer = new byte[BUFFER_SIZE];
                int available;
                while ((available = input.read(buffer)) != -1) {
                    if (available < BUFFER_SIZE) {
                        output.write(buffer, 0, available);
                    } else {
                        output.write(buffer, 0, BUFFER_SIZE);
                    }
                }
                return output.toByteArray();

            }

        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return null;
    }

    private static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
}
