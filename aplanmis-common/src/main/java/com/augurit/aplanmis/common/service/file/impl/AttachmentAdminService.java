package com.augurit.aplanmis.common.service.file.impl;

import com.augurit.agcloud.bsc.domain.BscAttDetail;
import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.domain.BscAttLink;
import com.augurit.agcloud.bsc.domain.BscAttStoreDb;
import com.augurit.agcloud.bsc.mapper.BscAttDetailMapper;
import com.augurit.agcloud.bsc.mapper.BscAttMapper;
import com.augurit.agcloud.bsc.sc.att.convert.BscAttConvertor;
import com.augurit.agcloud.bsc.sc.att.service.IBscAttService;
import com.augurit.agcloud.bsc.sc.att.service.IFileEncrypter;
import com.augurit.agcloud.bsc.sc.att.utils.AttUtils;
import com.augurit.agcloud.bsc.sc.init.service.BscInitService;
import com.augurit.agcloud.bsc.upload.UploadFileCmd;
import com.augurit.agcloud.bsc.upload.UploadFileStrategy;
import com.augurit.agcloud.bsc.upload.UploadType;
import com.augurit.agcloud.bsc.upload.factory.UploaderFactory;
import com.augurit.agcloud.bsc.util.CommonConstant;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author ZhangXinhui
 * @date 2019/7/25 025 17:25
 * @desc
 **/
/*@Slf4j
@Transactional
@Service*/
public class AttachmentAdminService {
//    private static final int BUFFER_SIZE = 1024;
//
//    @Autowired
//    private IBscAttService bscAttService;
//
//    @Autowired
//    private UploaderFactory uploaderFactory;

/*
    public boolean downloadFile(String detailId, HttpServletResponse response, HttpServletRequest request) throws Exception {

        BscAttDetail form = bscAttService.getAttDetailByDetailId(detailId);
        return downloadFile(form, response, request);
    }

    private IFileEncrypter getEncrypter(String className) throws Exception {

        if (StringUtils.isEmpty(className)) {
            throw new Exception("数据异常，附件描述表的加解密属性为空！");
        } else {
            Class clazz = Class.forName(className);
            Object encrypter = clazz.newInstance();
            if (encrypter instanceof IFileEncrypter) {
                return (IFileEncrypter) encrypter;
            } else {
                throw new Exception("加解密类配置有误，该类没有实现com.augurit.ads.bsc.sc.att.service.IFileEncrypter接口！");
            }
        }
    }


    private HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }


    private boolean downloadFile(BscAttDetail form, HttpServletResponse response, HttpServletRequest request) throws Exception {

        InputStream in = null;
        ServletOutputStream out = null;
        OutputStream outtemp = null;
        String atPath = form.getAttPath();
        String atName = form.getAttName();
        String attDiskName = form.getAttDiskName();
        if ((StringUtils.isBlank(atPath) || StringUtils.isBlank(atName)) && !UploadType.DATABASE.getValue().equals(form.getStoreType())) {
            return false;
        } else {
            String directoryPath = request.getServletContext().getRealPath("/");
            boolean retState = true;
            File file = null;
            byte[] content = null;
            if (!UploadType.DATABASE.getValue().equals(form.getStoreType())) {
                file = AttUtils.openFile(atPath, attDiskName, Status.OFF.equals(form.getIsRelativePath()), directoryPath, true, true);
            } else {
                List<BscAttStoreDb> stores = bscAttService.findAttStoreDbByIds(form.getDetailId());
                content = stores.get(0).getAttContent();
            }

            try {
                response.reset();
                response.setContentType("application/octet-stream");
                atName = AttUtils.reEncodeChineseFileName(atName, this.getRequest());
                response.setHeader("Content-Disposition", "attachment; filename=" + atName);
                if (file != null) {
                    boolean isEcrypt = Status.ON.equals(form.getIsEncrypt());
                    if (isEcrypt) {
                        IFileEncrypter encrypter = this.getEncrypter(form.getEncryptClass());
                        String tempfile = encrypter.decrypt(file.getAbsolutePath());
                        in = new BufferedInputStream(new FileInputStream(tempfile), 2048);
                    } else {
                        in = new BufferedInputStream(new FileInputStream(file), 2048);
                    }
                } else if (content != null) {
                    in = new ByteArrayInputStream(content);
                    if (Status.ON.equals(form.getIsEncrypt())) {
                        File tempFile = File.createTempFile(UUID.randomUUID().toString(), ".tmp");
                        outtemp = new FileOutputStream(tempFile);
                        byte[] buffer = new byte[2048];
                        int available;
                        while ((available = in.read(buffer)) != -1) {
                            if (available < 2048) {
                                outtemp.write(buffer, 0, available);
                            } else {
                                outtemp.write(buffer, 0, 2048);
                            }
                        }
                        IFileEncrypter encrypter = this.getEncrypter(form.getEncryptClass());
                        String deCryptFilePath = encrypter.decrypt(tempFile.getAbsolutePath());
                        in = new BufferedInputStream(new FileInputStream(deCryptFilePath), 2048);
                    }
                }

                out = response.getOutputStream();
                byte[] buffer = new byte[2048];

                int available;
                while ((available = Objects.requireNonNull(in).read(buffer)) != -1) {
                    if (available < 2048) {
                        out.write(buffer, 0, available);
                    } else {
                        out.write(buffer, 0, 2048);
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
            return retState;
        }
    }


    *//**
     * 文件上传统一入口
     *//*
    public String uploadFileStrategy(UploadFileCmd uploadFileCmd) {
        if (uploadFileCmd.getFile().getSize() == 0L) {
            throw new RuntimeException("文件不能为空！");
        }

        UploadFileStrategy uploadFileStrategy = uploaderFactory.create(uploadFileCmd.getStoreType());
        String detailId = uploadFileStrategy.upload(uploadFileCmd);

        if (log.isDebugEnabled()) {
            log.debug("文件上传成功, detailId: {}", detailId);
        }
        return detailId;
    }

    public boolean downloadFileStrategy(String detailId, HttpServletResponse response) throws Exception {
        BscAttDetail form = bscAttService.getAttDetailByDetailId(detailId);
        UploadFileStrategy uploadFileStrategy = uploaderFactory.create(form.getStoreType());
        InputStream in = uploadFileStrategy.download(detailId);
        ServletOutputStream out = null;
        OutputStream outtemp = null;
        String atName = form.getAttName();
        doDownload(response, in, out, outtemp, atName);
        return true;
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

    public boolean deleteFileStategy(String detailId) {

        Assert.notNull(detailId, "detailId不能为空");
        BscAttDetail form = bscAttService.getAttDetailByDetailId(detailId);
        if (form != null) {
            UploadFileStrategy uploadFileStrategy = uploaderFactory.create(form.getStoreType());
            return uploadFileStrategy.delete(detailId);
        }
        return true;
    }*/
}
