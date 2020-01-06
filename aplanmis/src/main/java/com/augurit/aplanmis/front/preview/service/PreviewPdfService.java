package com.augurit.aplanmis.front.preview.service;

import com.augurit.agcloud.bsc.domain.BscAttDetail;
import com.augurit.agcloud.bsc.mapper.BscAttDetailMapper;
import com.augurit.agcloud.bsc.sc.att.service.IBscAttService;
import com.augurit.agcloud.bsc.sc.att.utils.AttUtils;
import com.augurit.agcloud.bsc.upload.MongoDbAchieve;
import com.augurit.agcloud.bsc.upload.UploadType;
import com.augurit.agcloud.bsc.upload.factory.UploaderFactory;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.aplanmis.common.service.file.impl.FileUtilsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.bsc.domain.BscAttDetailPdf;
import com.augurit.agcloud.bsc.mapper.BscAttDetailPdfMapper;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import java.io.IOException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
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
    @Autowired
    private BscAttDetailMapper bscAttDetailMapper;
    /**
     *下载pdf
     * @param detailId  文件detailId
     * @param isCovert  true表示下载转换后的pdf文件，false表示下载原文件
     * @param response
     * @return
     * @throws Exception
     */
    public boolean downLoadPdf(String detailId,boolean isCovert, HttpServletResponse response) throws Exception{
        boolean result = false;
        BscAttDetail form = bscAttService.getAttDetailByDetailId(detailId);
        if(form != null){
            MongoDbAchieve uploadFileStrategy = (MongoDbAchieve)uploaderFactory.create(UploadType.MONGODB.getValue());
            InputStream inputStream = null;
            String attName = null;
            if(isCovert){
                Map<String, InputStream> streamMap = uploadFileStrategy.downloadCovertPdf(detailId);
                if(streamMap != null && streamMap.size() > 0){
                    Set<Map.Entry<String, InputStream>> entrySet = streamMap.entrySet();
                    for(Map.Entry<String, InputStream> entry:entrySet){
                        attName = entry.getKey();
                        inputStream = entry.getValue();
                        break;
                    }
                }
            }else{
                attName = form.getAttName();
                inputStream = uploadFileStrategy.download(detailId);
            }
            result = FileUtilsServiceImpl.writeContent(response, inputStream, attName);
        }
        return result;
    }

    public ResultForm checkIsCoverted(String detailId)throws Exception{
        ContentResultForm resultForm = new ContentResultForm(false);
        if(StringUtils.isNotBlank(detailId)){
            BscAttDetail bscAttDetail = bscAttDetailMapper.getBscAttDetailById(detailId);
            if(bscAttDetail != null && "0".equals(bscAttDetail.getAttSize())){
                resultForm.setMessage("上传文件大小为空，无法转换成功！");
                resultForm.setContent("FILE_SIZE_ZERO");
                return resultForm;
            }
            BscAttDetailPdf pdf = new BscAttDetailPdf();
            pdf.setDetailId(detailId);
            List<BscAttDetailPdf> list = bscAttDetailPdfMapper.listBscAttDetailPdf(pdf);
            resultForm.setSuccess(list.size()>0);
        }
        return resultForm;
    }

}
