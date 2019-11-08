package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaImMajorQual;
import com.augurit.aplanmis.common.vo.AeaImMajorQualVo;
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
 <li>创建人:tiantian</li>
 <li>创建时间：2019-06-11 18:54:38</li>
 </ul>
 */
@Mapper
@Repository
public interface AeaImMajorQualMapper {

    public void insertAeaImMajorQual(AeaImMajorQual aeaImMajorQual) throws Exception;
    public void updateAeaImMajorQual(AeaImMajorQual aeaImMajorQual) throws Exception;
    public void deleteAeaImMajorQual(@Param("id") String id) throws Exception;
    public List <AeaImMajorQual> listAeaImMajorQual(AeaImMajorQual aeaImMajorQual) throws Exception;
    public AeaImMajorQual getAeaImMajorQualById(@Param("id") String id) throws Exception;

    List<AeaImMajorQualVo> listAeaImMajorQualVo(@Param("qualId") String qualId, @Param("qualLevelId") String qualLevelId, @Param("unitRequireId") String unitRequireId)throws Exception;

    List<AeaImMajorQualVo> listAeaImMajorQualByUnitRequireId(String unitRequireId);

    void deleteAeaImMajorQualByUnitRequireId(@Param("unitRequireId") String unitRequireId) throws Exception;
}