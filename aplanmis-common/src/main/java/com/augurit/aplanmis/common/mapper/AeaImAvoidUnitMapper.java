package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaImAvoidUnit;
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
    <li>创建人:Augurit</li>
    <li>创建时间：2019-07-02 16:00:48</li>
</ul>
*/
@Mapper
@Repository
public interface AeaImAvoidUnitMapper {

public void insertAeaImAvoidUnit(AeaImAvoidUnit aeaImAvoidUnit) throws Exception;
public void updateAeaImAvoidUnit(AeaImAvoidUnit aeaImAvoidUnit) throws Exception;
public void deleteAeaImAvoidUnit(@Param("id") String id) throws Exception;
public List <AeaImAvoidUnit> listAeaImAvoidUnit(AeaImAvoidUnit aeaImAvoidUnit) throws Exception;
public AeaImAvoidUnit getAeaImAvoidUnitById(@Param("id") String id) throws Exception;
}
