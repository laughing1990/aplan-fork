package com.augurit.aplanmis.common.service.admin.item;

import com.augurit.aplanmis.common.domain.AeaItemFrontProj;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;
/**
* 项目信息前置检测-Service服务调用接口类
*/
public interface AeaItemFrontProjAdminService {

     void saveAeaItemFrontProj(AeaItemFrontProj aeaItemFrontProj) ;
     void updateAeaItemFrontProj(AeaItemFrontProj aeaItemFrontProj) ;
     void deleteAeaItemFrontProjById(String id) ;
     PageInfo<AeaItemFrontProj> listAeaItemFrontProj(AeaItemFrontProj aeaItemFrontProj, Page page) ;
     AeaItemFrontProj getAeaItemFrontProjById(String id) ;
     List<AeaItemFrontProj> listAeaItemFrontProj(AeaItemFrontProj aeaItemFrontProj) ;

     Long getMaxSortNo(AeaItemFrontProj aeaItemFrontProj);

}
