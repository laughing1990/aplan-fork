package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaHiItemCorrectStateHist;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

    /**
    * 事项输入输出实例表-Mapper数据与持久化接口类
    <ul>
        <li>项目名：奥格erp3.0--第一期建设项目</li>
        <li>版本信息：v1.0</li>
        <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
        <li>创建人:Administrator</li>
        <li>创建时间：2019-08-03 10:32:34</li>
    </ul>
    */
    @Mapper
    public interface AeaHiItemCorrectStateHistMapper {

    public void insertAeaHiItemCorrectStateHist(AeaHiItemCorrectStateHist aeaHiItemCorrectStateHist) throws Exception;
    public void updateAeaHiItemCorrectStateHist(AeaHiItemCorrectStateHist aeaHiItemCorrectStateHist) throws Exception;
    public void deleteAeaHiItemCorrectStateHist(@Param("id") String id) throws Exception;
    public List <AeaHiItemCorrectStateHist> listAeaHiItemCorrectStateHist(AeaHiItemCorrectStateHist aeaHiItemCorrectStateHist) throws Exception;
    public AeaHiItemCorrectStateHist getAeaHiItemCorrectStateHistById(@Param("id") String id) throws Exception;
    }
