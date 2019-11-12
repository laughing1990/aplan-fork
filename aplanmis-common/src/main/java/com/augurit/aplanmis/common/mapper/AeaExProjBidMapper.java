package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaExProjBid;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.domain.AeaUnitProj;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 招投标信息-Mapper数据与持久化接口类
<ul>
    <li>项目名：奥格erp3.0--第一期建设项目</li>
    <li>版本信息：v1.0</li>
    <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
    <li>创建人:刘赵雄</li>
    <li>创建时间：2019-10-31 15:56:12</li>
</ul>
*/
@Mapper
public interface AeaExProjBidMapper {

public void insertAeaExProjBid(AeaExProjBid aeaExProjBid) throws Exception;

public void updateAeaExProjBid(AeaExProjBid aeaExProjBid) throws Exception;

public AeaExProjBid getAeaExProjBidByProjId(@Param("projId") String projId) throws Exception;

public List<AeaUnitInfo> findUnitProjByProjInfoIdAndType(@Param("projInfoId") String projInfoId, @Param("unitType") String unitType, @Param("rootOrgId") String rootOrgId);

public List<AeaUnitProj> getAeaUnitProjById(@Param("projInfoId")String unitProjId);

public int batchDeleteUnitProjByType(@Param("projId")String projId,@Param("unitTypes")List<String> unitTypes);

}
