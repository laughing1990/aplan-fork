package com.augurit.aplanmis.common.service.admin.solicit;

import com.augurit.aplanmis.common.domain.AeaSolicitItem;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
* 按事项征求配置表-Service服务调用接口类
*/
public interface AeaSolicitItemService {

     void saveAeaSolicitItem(AeaSolicitItem aeaSolicitItem) ;
     void updateAeaSolicitItem(AeaSolicitItem aeaSolicitItem) ;
     void deleteAeaSolicitItemById(String id) ;
     PageInfo<AeaSolicitItem> listAeaSolicitItem(AeaSolicitItem aeaSolicitItem, Page page) ;
     AeaSolicitItem getAeaSolicitItemById(String id) ;
     List<AeaSolicitItem> listAeaSolicitItem(AeaSolicitItem aeaSolicitItem) ;

}
