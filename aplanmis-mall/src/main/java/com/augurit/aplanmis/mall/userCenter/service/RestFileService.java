package com.augurit.aplanmis.mall.userCenter.service;

import com.augurit.agcloud.framework.ui.result.ResultForm;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface RestFileService {

    List getAttFiles(String matinstId) throws Exception;

    ResultForm delelteAttachment(String[] detailIds, String matinstId) throws Exception;

    void delelteAttachmentByCloud(String[] detailIds, String recordId) throws Exception;

    ModelAndView preview(String detailId, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) throws Exception;

}
