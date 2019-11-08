package com.augurit.aplanmis.common.service.admin.par;

import com.augurit.aplanmis.common.domain.AeaParFrontProj;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 阶段的项目信息前置检测-Service服务调用接口类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-11-01 10:48:33</li>
 * </ul>
 */
public interface AeaParFrontProjService {
    void saveAeaParFrontProj(AeaParFrontProj aeaParFrontProj) throws Exception;

    void updateAeaParFrontProj(AeaParFrontProj aeaParFrontProj) throws Exception;

    void deleteAeaParFrontProjById(String id) throws Exception;

    PageInfo<AeaParFrontProj> listAeaParFrontProj(AeaParFrontProj aeaParFrontProj, Page page) throws Exception;

    AeaParFrontProj getAeaParFrontProjById(String id) throws Exception;

    List<AeaParFrontProj> listAeaParFrontProj(AeaParFrontProj aeaParFrontProj) throws Exception;

    Long getMaxSortNo(AeaParFrontProj aeaParFrontProj) throws Exception;
}
