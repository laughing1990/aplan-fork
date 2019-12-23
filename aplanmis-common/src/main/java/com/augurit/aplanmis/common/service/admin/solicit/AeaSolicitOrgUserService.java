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
     PageInfo<AeaSolicitOrgUser> listAeaSolicitOrgUser(AeaSolicitOrgUser aeaSolicitOrgUser, Page page) ;
     AeaSolicitOrgUser getAeaSolicitOrgUserById(String id) ;
     List<AeaSolicitOrgUser> listAeaSolicitOrgUser(AeaSolicitOrgUser aeaSolicitOrgUser) ;

}
