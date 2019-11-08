package com.augurit.aplanmis.common.service.admin.item;

import com.augurit.aplanmis.common.domain.AeaItemFrontPartform;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
* 事项的前置检查事项-Service服务调用接口类
*/
public interface AeaItemFrontPartformAdminService {

     void saveAeaItemFrontPartform(AeaItemFrontPartform aeaItemFrontPartform) ;
     void updateAeaItemFrontPartform(AeaItemFrontPartform aeaItemFrontPartform) ;
     void deleteAeaItemFrontPartformById(String id) ;
     PageInfo<AeaItemFrontPartform> listAeaItemFrontPartform(AeaItemFrontPartform aeaItemFrontPartform, Page page) ;
     AeaItemFrontPartform getAeaItemFrontPartformById(String id) ;
     List<AeaItemFrontPartform> listAeaItemFrontPartform(AeaItemFrontPartform aeaItemFrontPartform) ;

}
