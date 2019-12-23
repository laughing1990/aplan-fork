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
     PageInfo<AeaSolicitItemUser> listAeaSolicitItemUser(AeaSolicitItemUser aeaSolicitItemUser, Page page) ;
     AeaSolicitItemUser getAeaSolicitItemUserById(String id) ;
     List<AeaSolicitItemUser> listAeaSolicitItemUser(AeaSolicitItemUser aeaSolicitItemUser) ;

}
