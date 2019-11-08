package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaImServiceTypeItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* -Mapper数据与持久化接口类
<ul>
    <li>项目名：奥格erp3.0--第一期建设项目</li>
    <li>版本信息：v1.0</li>
    <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
    <li>创建人:Administrator</li>
    <li>创建时间：2019-06-04 10:25:10</li>
</ul>
*/
@Mapper
@Repository
public interface AeaImServiceTypeItemMapper {

public void insertAeaImServiceTypeItem(AeaImServiceTypeItem aeaImServiceTypeItem) throws Exception;
public void updateAeaImServiceTypeItem(AeaImServiceTypeItem aeaImServiceTypeItem) throws Exception;
public void deleteAeaImServiceTypeItem(@Param("id") String id) throws Exception;
public List <AeaImServiceTypeItem> listAeaImServiceTypeItem(AeaImServiceTypeItem aeaImServiceTypeItem) throws Exception;
public AeaImServiceTypeItem getAeaImServiceTypeItemById(@Param("id") String id) throws Exception;
}
