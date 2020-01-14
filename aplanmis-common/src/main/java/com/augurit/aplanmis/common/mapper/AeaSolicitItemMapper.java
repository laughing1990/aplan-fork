package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaSolicitItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 按事项征求配置表-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaSolicitItemMapper {

     /**
      * 保存
      *
      * @param aeaSolicitItem
      */
     void insertAeaSolicitItem(AeaSolicitItem aeaSolicitItem);

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
     void deleteAeaSolicitItem(@Param("id") String id);

     /**
      * 批量删除
      *
      * @param ids
      */
     void batchDelSolicitItemByIds(@Param("ids") String[] ids);

     /**
      * 获取，不携带组织信息
      *
      * @param id
      * @return
      */
     AeaSolicitItem getAeaSolicitItemById(@Param("id") String id);

     /**
      * 获取，携带组织信息
      *
      * @param id
      * @return
      */
     AeaSolicitItem getAeaSolicitItemRelInfoById(@Param("id") String id);

     /**
      * 获取列表，不携带组织信息
      *
      * @param aeaSolicitItem
      * @return
      */
     List<AeaSolicitItem> listAeaSolicitItem(AeaSolicitItem aeaSolicitItem);

     /**
      * 获取列表，携带组织信息
      *
      * @param aeaSolicitItem
      * @return
      */
     List<AeaSolicitItem> listAeaSolicitItemRelInfo(AeaSolicitItem aeaSolicitItem);

     /**
      * 删除
      *
      * @param rootOrgId
      */
     void batchDelSolicitItemByRootOrgId(@Param("rootOrgId") String rootOrgId);

     List<AeaSolicitItem> listAeaSolicitItemWithUserIdByItemVerIds(@Param("itemVerIds") List<String> itemVerIds);

    /*
    查询部门确认的用户对应的事项
     */
    List<AeaSolicitItem> listAeaSolicitItemForDeptConfirmByUserId(String currentUserId);

    /*
    根据用户判断是否是审批部门人员
     */
    int countApproveDeptByUserId(String currentUserId);
}
