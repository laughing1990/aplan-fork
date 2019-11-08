package com.augurit.aplanmis.common.service.admin.item;

import com.augurit.aplanmis.common.domain.AeaItemPartform;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;
/**
* 事项与扩展表单关联表-Service服务调用接口类
*/
public interface AeaItemPartformAdminService {

     void saveAeaItemPartform(AeaItemPartform aeaItemPartform);
     void updateAeaItemPartform(AeaItemPartform aeaItemPartform);
     void deleteAeaItemPartformById(String id);
     PageInfo<AeaItemPartform> listAeaItemPartform(AeaItemPartform aeaItemPartform, Page page);
     AeaItemPartform getAeaItemPartformById(String id);
     List<AeaItemPartform> listAeaItemPartform(AeaItemPartform aeaItemPartform);

}
