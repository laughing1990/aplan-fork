package com.augurit.aplanmis.common.service.admin.par;

import com.augurit.aplanmis.common.domain.AeaParFrontPartform;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 阶段的扩展表单前置检测表-Service服务调用接口类
 */
public interface AeaParFrontPartformService {

    void saveAeaParFrontPartform(AeaParFrontPartform aeaParFrontPartform) throws Exception;

    void updateAeaParFrontPartform(AeaParFrontPartform aeaParFrontPartform) throws Exception;

    void deleteAeaParFrontPartformById(String id) throws Exception;

    PageInfo<AeaParFrontPartform> listAeaParFrontPartform(AeaParFrontPartform aeaParFrontPartform, Page page) throws Exception;

    AeaParFrontPartform getAeaParFrontPartformById(String id) throws Exception;

    List<AeaParFrontPartform> listAeaParFrontPartform(AeaParFrontPartform aeaParFrontPartform) throws Exception;

    PageInfo<AeaParFrontPartform> listAeaParFrontPartformVoByPage(AeaParFrontPartform aeaParFrontPartform, Page page) throws Exception;

    Long getMaxSortNo(String stageId, String rootOrgId) throws Exception;

    PageInfo<AeaParFrontPartform> listSelectParFrontPartformByPage(AeaParFrontPartform aeaParFrontPartform, Page page) throws Exception;

    AeaParFrontPartform getAeaParFrontPartformVoById(String frontPartformId) throws Exception;

    List<AeaParFrontPartform> getAeaParFrontPartformVoByStageId(String stageId);

    void batchSaveAeaParFrontPartform(String stageId, String[] stagePartformIds)throws Exception;

    List<AeaParFrontPartform> listAeaParFrontPartformVoByNoPage(AeaParFrontPartform aeaParFrontPartform) throws Exception;

    void changIsActive(String id, String rootOrgId);
}
