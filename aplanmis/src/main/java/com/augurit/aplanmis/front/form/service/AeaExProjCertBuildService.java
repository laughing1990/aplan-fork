package com.augurit.aplanmis.front.form.service;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.aplanmis.common.domain.AeaExProjBuild;
import com.augurit.aplanmis.common.domain.AeaExProjCertBuild;
import com.augurit.aplanmis.common.domain.AeaExProjContract;
import com.augurit.aplanmis.common.vo.AeaCertiVo;
import com.augurit.aplanmis.common.vo.AeaProjDrawingVo;
import com.augurit.aplanmis.front.form.vo.ExProjFormVo;
import org.springframework.stereotype.Service;

import java.util.Map;

public interface AeaExProjCertBuildService {
    //同步建设项目登记表数据到aea_ex_proj_cert_build表上
    ContentResultForm<String> SynchronizeDataByExProjForm(ExProjFormVo vo) throws Exception;

    //同步建设项目两证一书信息到aea_ex_proj_cert_build表
    ContentResultForm<String> SynchronizeDataByAeaExProjCertLandForm(AeaCertiVo vo) throws Exception;

    //同步合同信息到aea_ex_proj_cert_build表
    ContentResultForm<String> SynchronizeDataByAeaExProjContractForm(AeaExProjContract vo) throws Exception;

    //同步施工图审查信息到aea_ex_proj_cert_build表
    ContentResultForm<String> SynchronizeDataByAeaProjDrawingForm(AeaProjDrawingVo vo) throws Exception;

    //同步施工监察单位信息到aea_ex_proj_cert_build表
    ContentResultForm<String> SynchronizeDataByAeaExProjBuild(AeaExProjBuild aeaExProjBuild)throws Exception;
    //保存
    void saveForm(AeaExProjCertBuild aeaExProjCertBuild)throws Exception;
    //根据项目ID查询
    AeaExProjCertBuild findByProjId(String projId) throws Exception;
}
