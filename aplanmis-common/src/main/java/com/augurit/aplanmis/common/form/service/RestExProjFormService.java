package com.augurit.aplanmis.common.form.service;

import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.form.vo.ExProjFormVo;

public interface RestExProjFormService {

    ExProjFormVo getExProjForm(String projInfoId) throws Exception;

    ResultForm saveExProjForm(ExProjFormVo exProjFormVo) throws Exception;
}
