package com.augurit.aplanmis.common.service.admin.solicit;

import com.augurit.aplanmis.common.domain.AeaSolicitOrgUser;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
* 按组织征求的人员名单表-Service服务调用接口类
*/
public interface AeaSolicitOrgUserService {

     void saveAeaSolicitOrgUser(AeaSolicitOrgUser aeaSolicitOrgUser) ;

     void updateAeaSolicitOrgUser(AeaSolicitOrgUser aeaSolicitOrgUser) ;

     void deleteAeaSolicitOrgUserById(String id) ;

     void batchDelSolicitOrgUserByIds(String[] ids) ;

     AeaSolicitOrgUser getAeaSolicitOrgUserById(String id) ;

     PageInfo<AeaSolicitOrgUser> listAeaSolicitOrgUser(AeaSolicitOrgUser aeaSolicitOrgUser, Page page) ;

     List<AeaSolicitOrgUser> listAeaSolicitOrgUser(AeaSolicitOrgUser aeaSolicitOrgUser) ;

     PageInfo<AeaSolicitOrgUser> listAeaSolicitOrgUserRelInfo(AeaSolicitOrgUser aeaSolicitOrgUser, Page page) ;

     List<AeaSolicitOrgUser> listAeaSolicitOrgUserRelInfo(AeaSolicitOrgUser aeaSolicitOrgUser) ;

     void batchSaveSolicitOrgUser(String SolicitOrgId, String[] userIds, String[] sortNos);

     void batchDelSolicitOrgUserBySolicitOrgId(String SolicitOrgId);

     Long getMaxSortNo();

     void changeIsActive(String id);
}
