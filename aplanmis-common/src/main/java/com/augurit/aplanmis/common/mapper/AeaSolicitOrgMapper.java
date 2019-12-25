package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaSolicitOrg;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* 按组织征求配置表-Mapper数据与持久化接口类
*/
@Mapper
@Repository
public interface AeaSolicitOrgMapper {

     /**
      * 保存
      *
      * @param aeaSolicitOrg
      */
     void insertAeaSolicitOrg(AeaSolicitOrg aeaSolicitOrg);

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
     void deleteAeaSolicitOrg(@Param("id") String id);

     /**
      * 批量删除
      *
      * @param ids
      */
     void batchDelSolicitOrgByIds(@Param("ids") String[] ids);

     /**
      * 获取，不携带组织信息
      *
      * @param id
      * @return
      */
     AeaSolicitOrg getAeaSolicitOrgById(@Param("id") String id);

     /**
      * 获取，携带组织信息
      *
      * @param id
      * @return
      */
     AeaSolicitOrg getSolicitOrgRelOrgInfoById(@Param("id") String id);

     /**
      * 获取列表，不携带组织信息
      *
      * @param aeaSolicitOrg
      * @return
      */
     List<AeaSolicitOrg> listAeaSolicitOrg(AeaSolicitOrg aeaSolicitOrg);

     /**
      * 获取列表，携带组织信息
      *
      * @param aeaSolicitOrg
      * @return
      */
     List<AeaSolicitOrg> listAeaSolicitOrgRelOrgInfo(AeaSolicitOrg aeaSolicitOrg);

     /**
      * 删除
      *
      * @param rootOrgId
      */
     void batchDelSolicitOrgByRootOrgId(@Param("rootOrgId") String rootOrgId);
}
