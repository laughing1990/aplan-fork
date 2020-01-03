package com.augurit.aplanmis.common.service.admin.solicit;

import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.aplanmis.common.domain.AeaSolicitItem;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
* 按事项征求配置表-Service服务调用接口类
*/
public interface AeaSolicitItemService {

     /**
      * 保存
      *
      * @param aeaSolicitItem
      */
     void saveAeaSolicitItem(AeaSolicitItem aeaSolicitItem);

     /**
      * 更新
      *
      * @param aeaSolicitItem
      */
     void updateAeaSolicitItem(AeaSolicitItem aeaSolicitItem);

     /**
      * 删除
      *
      * @param id
      */
     void deleteAeaSolicitItemById(String id);

     /**
      * 批量删除
      *
      * @param ids
      */
     void batchDelSolicitItemByIds(String[] ids);

     /**
      * 分页获取
      *
      * @param aeaSolicitItem
      * @param page
      * @return
      */
     PageInfo<AeaSolicitItem> listAeaSolicitItem(AeaSolicitItem aeaSolicitItem, Page page);

     /**
      * 通过id获取
      *
      * @param id
      * @return
      */
     AeaSolicitItem getAeaSolicitItemById(String id);


     /**
      * 通过id获取,并获取组织信息
      *
      * @param id
      * @return
      */
     AeaSolicitItem getAeaSolicitItemRelInfoById(String id);

     /**
      * 获取列表不携带组织信息
      *
      * @param aeaSolicitItem
      * @return
      */
     List<AeaSolicitItem> listAeaSolicitItem(AeaSolicitItem aeaSolicitItem);

     /**
      * 分页获取列表携带组织信息
      *
      * @param aeaSolicitItem
      * @param page
      * @return
      */
     PageInfo<AeaSolicitItem> listAeaSolicitItemRelInfo(AeaSolicitItem aeaSolicitItem, Page page);

     /**
      * 获取列表携带组织信息
      *
      * @param aeaSolicitItem
      * @return
      */
     List<AeaSolicitItem> listAeaSolicitItemRelInfo(AeaSolicitItem aeaSolicitItem);

     /**
      * 批量保存
      *
      * @param busType
      * @param solicitType
      * @param itemIds
      */
     void batchSaveSolicitItem(String busType, String solicitType, String[] itemIds);

     /**
      * 删除
      *
      * @param rootOrgId
      */
     void batchDelSolicitItemByRootOrgId(String rootOrgId);

     List<ZtreeNode> gtreeSolicitItem(String rootOrgId);

}
