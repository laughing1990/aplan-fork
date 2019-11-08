package com.augurit.aplanmis.common.mapper;
import com.augurit.aplanmis.common.domain.AeaExProjDrawing;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

    /**
    * 施工图审查信息-Mapper数据与持久化接口类
    <ul>
        <li>项目名：奥格erp3.0--第一期建设项目</li>
        <li>版本信息：v1.0</li>
        <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
        <li>创建人:Administrator</li>
        <li>创建时间：2019-11-01 10:14:43</li>
    </ul>
    */
    @Mapper
    public interface AeaExProjDrawingMapper {

    public void insertAeaExProjDrawing(AeaExProjDrawing aeaExProjDrawing) throws Exception;
    public void updateAeaExProjDrawing(AeaExProjDrawing aeaExProjDrawing) throws Exception;
    public void deleteAeaExProjDrawing(@Param("id") String id) throws Exception;
    public List <AeaExProjDrawing> listAeaExProjDrawing(AeaExProjDrawing aeaExProjDrawing) throws Exception;
    public AeaExProjDrawing getAeaExProjDrawingById(@Param("id") String id) throws Exception;
    }
