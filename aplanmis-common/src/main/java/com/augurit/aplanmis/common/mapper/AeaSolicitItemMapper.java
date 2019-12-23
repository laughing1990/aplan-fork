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

     void insertAeaSolicitItem(AeaSolicitItem aeaSolicitItem) ;
     void updateAeaSolicitItem(AeaSolicitItem aeaSolicitItem) ;
     void deleteAeaSolicitItem(@Param("id") String id) ;
     List <AeaSolicitItem> listAeaSolicitItem(AeaSolicitItem aeaSolicitItem) ;
     AeaSolicitItem getAeaSolicitItemById(@Param("id") String id) ;
}
