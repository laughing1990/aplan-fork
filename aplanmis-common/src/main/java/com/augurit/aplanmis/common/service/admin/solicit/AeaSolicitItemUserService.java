package com.augurit.aplanmis.common.service.admin.solicit;

import com.augurit.aplanmis.common.domain.AeaSolicitItemUser;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
* 按事项征求的人员名单表-Service服务调用接口类
*/
public interface AeaSolicitItemUserService {

     void saveAeaSolicitItemUser(AeaSolicitItemUser aeaSolicitItemUser) ;

     void updateAeaSolicitItemUser(AeaSolicitItemUser aeaSolicitItemUser) ;

     void deleteAeaSolicitItemUserById(String id) ;

     void batchDelSolicitItemUserByIds(String[] ids) ;

     AeaSolicitItemUser getAeaSolicitItemUserById(String id) ;

     PageInfo<AeaSolicitItemUser> listAeaSolicitItemUser(AeaSolicitItemUser aeaSolicitItemUser, Page page) ;

     List<AeaSolicitItemUser> listAeaSolicitItemUser(AeaSolicitItemUser aeaSolicitItemUser) ;

     PageInfo<AeaSolicitItemUser> listAeaSolicitItemUserRelInfo(AeaSolicitItemUser aeaSolicitItemUser, Page page) ;

     List<AeaSolicitItemUser> listAeaSolicitItemUserRelInfo(AeaSolicitItemUser aeaSolicitItemUser) ;

     void batchSaveSolicitItemUser(String solicitItemId, String[] userIds, String[] sortNos);

     void batchDelSolicitItemUserBySolicitItemId(String solicitItemId);

     Long getMaxSortNo();

     void changeIsActive(String id);
}
