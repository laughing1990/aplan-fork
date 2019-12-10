package com.augurit.aplanmis.common.service.admin.item;

import com.augurit.agcloud.opus.common.domain.OpuOmUser;
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

     void batchDelItemUserByIds(String[] ids);

     PageInfo<AeaItemUser> listAeaItemUser(AeaItemUser aeaItemUser, Page page);
     
     AeaItemUser getAeaItemUserById(String id);

     List<AeaItemUser> listAeaItemUser(AeaItemUser aeaItemUser);

     List<OpuOmUser> listAllUserRelOrgByOrgId(String orgId);

     PageInfo<AeaItemUser> listUserItemRelItemInfo(AeaItemUser aeaItemUser, Page page);

     List<AeaItemUser> listUserItemRelItemInfo(AeaItemUser aeaItemUser);

     void batchSaveUserItem(String userId, String[] itemIds, String[] sortNos);

     void batchDelItemByUserId(String userId, String orgId);

     Long getMaxSortNo(String rootOrgId);

     void changIsActive(String id, String rootOrgId);
}
