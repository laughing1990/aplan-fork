package com.augurit.aplanmis.common.service.admin.item;

import com.augurit.aplanmis.common.domain.AeaItemOneform;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
* 事项与总表单关联表-Service服务调用接口类
*/
public interface AeaItemOneformAdminService {

     void saveAeaItemOneform(AeaItemOneform aeaItemOneform) ;
     void updateAeaItemOneform(AeaItemOneform aeaItemOneform) ;
     void deleteAeaItemOneformById(String id) ;
     PageInfo<AeaItemOneform> listAeaItemOneform(AeaItemOneform aeaItemOneform, Page page) ;
     AeaItemOneform getAeaItemOneformById(String id) ;
     List<AeaItemOneform> listAeaItemOneform(AeaItemOneform aeaItemOneform) ;

}
