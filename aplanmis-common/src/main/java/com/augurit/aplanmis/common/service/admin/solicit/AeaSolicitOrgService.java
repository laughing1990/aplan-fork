package com.augurit.aplanmis.common.service.admin.solicit;

import com.augurit.aplanmis.common.domain.AeaSolicitOrg;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
* 按组织征求配置表-Service服务调用接口类
*/
public interface AeaSolicitOrgService {

     /**
      * 保存
      *
      * @param aeaSolicitOrg
      */
     void saveAeaSolicitOrg(AeaSolicitOrg aeaSolicitOrg);

     /**
      * 更新
      *
      * @param aeaSolicitOrg
      */
     void updateAeaSolicitOrg(AeaSolicitOrg aeaSolicitOrg);

     /**
      * 删除
      *
      * @param id
      */
     void deleteAeaSolicitOrgById(String id);

     /**
      * 批量删除
      *
      * @param ids
      */
     void batchDelSolicitOrgByIds(String[] ids);

     /**
      * 分页获取
      *
      * @param aeaSolicitOrg
      * @param page
      * @return
      */
     PageInfo<AeaSolicitOrg> listAeaSolicitOrg(AeaSolicitOrg aeaSolicitOrg, Page page);

     /**
      * 通过id获取
      *
      * @param id
      * @return
      */
     AeaSolicitOrg getAeaSolicitOrgById(String id);


     /**
      * 通过id获取,并获取组织信息
      *
      * @param id
      * @return
      */
     AeaSolicitOrg getSolicitOrgRelOrgInfoById(String id);

     /**
      * 获取列表不携带组织信息
      *
      * @param aeaSolicitOrg
      * @return
      */
     List<AeaSolicitOrg> listAeaSolicitOrg(AeaSolicitOrg aeaSolicitOrg);

     /**
      * 分页获取列表携带组织信息
      *
      * @param aeaSolicitOrg
      * @param page
      * @return
      */
     PageInfo<AeaSolicitOrg> listAeaSolicitOrgRelOrgInfo(AeaSolicitOrg aeaSolicitOrg,Page page);

     /**
      * 获取列表携带组织信息
      *
      * @param aeaSolicitOrg
      * @return
      */
     List<AeaSolicitOrg> listAeaSolicitOrgRelOrgInfo(AeaSolicitOrg aeaSolicitOrg);

     /**
      * 批量保存
      *
      * @param orgIds
      */
     void batchSaveSolicitOrg(String[] orgIds);

     /**
      * 删除
      *
      * @param rootOrgId
      */
     void batchDelSolicitOrgByRootOrgId(String rootOrgId);
}
