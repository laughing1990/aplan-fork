package com.augurit.aplanmis.common.service.admin.par;

import com.augurit.aplanmis.common.domain.AeaParFrontProj;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 阶段的项目信息前置检测-Service服务调用接口类
 */
public interface AeaParFrontProjService {

    void saveAeaParFrontProj(AeaParFrontProj aeaParFrontProj) throws Exception;

    void updateAeaParFrontProj(AeaParFrontProj aeaParFrontProj) throws Exception;

    void deleteAeaParFrontProjById(String id) throws Exception;

    PageInfo<AeaParFrontProj> listAeaParFrontProj(AeaParFrontProj aeaParFrontProj, Page page) throws Exception;

    AeaParFrontProj getAeaParFrontProjById(String id) throws Exception;

    List<AeaParFrontProj> listAeaParFrontProj(AeaParFrontProj aeaParFrontProj) throws Exception;

    Long getMaxSortNo(AeaParFrontProj aeaParFrontProj) throws Exception;

    void changIsActive(String id, String rootOrgId);
}
