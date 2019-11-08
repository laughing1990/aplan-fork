package com.augurit.aplanmis.rest.preview.service;

import com.augurit.agcloud.bsc.domain.BscAttDetail;
import com.augurit.agcloud.bsc.domain.BscAttDetailPdf;
import com.augurit.agcloud.bsc.mapper.BscAttDetailPdfMapper;
import com.augurit.agcloud.bsc.sc.att.service.IBscAttService;
import com.augurit.agcloud.bsc.sc.att.utils.AttUtils;
import com.augurit.agcloud.bsc.upload.MongoDbAchieve;
import com.augurit.agcloud.bsc.upload.UploadType;
import com.augurit.agcloud.bsc.upload.factory.UploaderFactory;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhangwn on 2019/8/15.
 */
@Service
@Transactional
@Slf4j
public class PreviewPdfService {

    @Autowired
    private IBscAttService bscAttService;
    @Autowired
    private UploaderFactory uploaderFactory;
    @Autowired
    private BscAttDetailPdfMapper bscAttDetailPdfMapper;

    /**
     * 下载pdf
     *
     * @param detailId 文件detailId
     * @param isCovert true表示下载转换后的pdf文件，false表示下载原文件
     * @param response
     * @return
     * @throws Exception
     */
    public boolean downLoadPdf(String detailId, boolean isCovert, HttpServletResponse response) throws Exception {
        boolean result = false;
        BscAttDetail form = bscAttService.getAttDetailByDetailId(detailId);
        if (form != null) {
            MongoDbAchieve uploadFileStrategy = (MongoDbAchieve) uploaderFactory.create(UploadType.MONGODB.getValue());
            InputStream inputStream = null;
            String attName = null;
            if (isCovert) {
                Map<String, InputStream> streamMap = uploadFileStrategy.downloadCovertPdf(detailId);
                if (streamMap != null && streamMap.size() > 0) {
                    Set<Map.Entry<String, InputStream>> entrySet = streamMap.entrySet();
                    for (Map.Entry<String, InputStream> entry : entrySet) {
                        attName = entry.getKey();
                        inputStream = entry.getValue();
                        break;
                    }
                }
            } else {
                attName = form.getAttName();
                inputStream = uploadFileStrategy.download(detailId);
            }
            result = writeContent(response, inputStream, attName);
        }
        return result;
    }

    public ResultForm checkIsCoverted(String detailId) throws Exception {
        ResultForm resultForm = new ResultForm(false);
        if (StringUtils.isNotBlank(detailId)) {
            BscAttDetailPdf pdf = new BscAttDetailPdf();
            pdf.setDetailId(detailId);
            List<BscAttDetailPdf> list = bscAttDetailPdfMapper.listBscAttDetailPdf(pdf);
            resultForm.setSuccess(list.size() > 0);
        }
        return resultForm;
    }

    private boolean writeContent(HttpServletResponse response, InputStream inputStream, String attName) throws IOException {
        byte[] bytes = AttUtils.inputStreamToBytesAndClose(inputStream);
        if (bytes != null) {
            response.reset();
//            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//            String charset = request.getCharacterEncoding();
//            attName = new String(attName.getBytes(charset), "UTF-8");
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + URLEncoder.encode(attName, "UTF-8"));
            ServletOutputStream out = null;
            out = response.getOutputStream();
            out.write(bytes, 0, bytes.length);
            out.close();
        }
        return true;
    }

}
