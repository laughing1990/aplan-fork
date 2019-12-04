package com.augurit.aplanmis.common.service.admin.item;

import com.augurit.aplanmis.common.domain.AeaItemUser;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
* 用户事项管理-Service服务调用接口类
*/
public interface AeaItemUserAdminService {

     void saveAeaItemUser(AeaItemUser aeaItemUser);

     void updateAeaItemUser(AeaItemUser aeaItemUser);

     void deleteAeaItemUserById(String id);

     PageInfo<AeaItemUser> listAeaItemUser(AeaItemUser aeaItemUser, Page page);
     
     AeaItemUser getAeaItemUserById(String id);

     List<AeaItemUser> listAeaItemUser(AeaItemUser aeaItemUser);

}
