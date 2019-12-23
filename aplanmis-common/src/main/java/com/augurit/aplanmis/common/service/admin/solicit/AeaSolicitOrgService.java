package com.augurit.aplanmis.common.service.admin.solicit;

import com.augurit.aplanmis.common.domain.AeaSolicitOrg;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
* 按组织征求配置表-Service服务调用接口类
*/
public interface AeaSolicitOrgService {

     void saveAeaSolicitOrg(AeaSolicitOrg aeaSolicitOrg) ;
     void updateAeaSolicitOrg(AeaSolicitOrg aeaSolicitOrg) ;
     void deleteAeaSolicitOrgById(String id) ;
     PageInfo<AeaSolicitOrg> listAeaSolicitOrg(AeaSolicitOrg aeaSolicitOrg, Page page) ;
     AeaSolicitOrg getAeaSolicitOrgById(String id) ;
     List<AeaSolicitOrg> listAeaSolicitOrg(AeaSolicitOrg aeaSolicitOrg) ;
}
