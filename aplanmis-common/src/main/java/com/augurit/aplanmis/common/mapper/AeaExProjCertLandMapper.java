package com.augurit.aplanmis.common.mapper;
import com.augurit.aplanmis.common.domain.AeaExProjCertLand;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

    /**
    * 建设项目用地规划许可证-Mapper数据与持久化接口类
    <ul>
        <li>项目名：奥格erp3.0--第一期建设项目</li>
        <li>版本信息：v1.0</li>
        <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
        <li>创建人:Administrator</li>
        <li>创建时间：2019-10-29 19:48:12</li>
    </ul>
    */
    @Mapper
    public interface AeaExProjCertLandMapper {

    public void insertAeaExProjCertLand(AeaExProjCertLand aeaExProjCertLand) throws Exception;
    public void updateAeaExProjCertLand(AeaExProjCertLand aeaExProjCertLand) throws Exception;
    public void deleteAeaExProjCertLand(@Param("id") String id) throws Exception;
    public List <AeaExProjCertLand> listAeaExProjCertLand(AeaExProjCertLand aeaExProjCertLand) throws Exception;
    public AeaExProjCertLand getAeaExProjCertLandById(@Param("id") String id) throws Exception;
    }
