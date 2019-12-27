package com.augurit.aplanmis.mall.userCenter.service;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface RestFileService {

    List<BscAttFileAndDir> getAttFiles(String matinstId) throws Exception;

    public List<BscAttFileAndDir> getAttFilesByPK(String tableName,String pkName,String recordId) throws Exception;

    ResultForm delelteAttachment(String[] detailIds, String matinstId) throws Exception;

    void delelteAttachmentByCloud(String[] detailIds, String recordId) throws Exception;

    ModelAndView preview(String detailId, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) throws Exception;

    /**
     * 判断是否允许上传的文件类型
     * @param request
     * @return
     */
    Boolean isAllowFileType(HttpServletRequest request);


}
