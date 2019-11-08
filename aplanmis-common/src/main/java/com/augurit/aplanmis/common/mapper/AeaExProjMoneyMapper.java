package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaExProjMoney;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

    /**
    * 项目资金来源构成比例-Mapper数据与持久化接口类
    <ul>
        <li>项目名：奥格erp3.0--第一期建设项目</li>
        <li>版本信息：v1.0</li>
        <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
        <li>创建人:Administrator</li>
        <li>创建时间：2019-10-31 09:28:01</li>
    </ul>
    */
    @Mapper
    public interface AeaExProjMoneyMapper {

    public void insertAeaExProjMoney(AeaExProjMoney aeaExProjMoney) throws Exception;
    public void updateAeaExProjMoney(AeaExProjMoney aeaExProjMoney) throws Exception;
    public void deleteAeaExProjMoney(@Param("id") String id) throws Exception;
    public List <AeaExProjMoney> listAeaExProjMoney(AeaExProjMoney aeaExProjMoney) throws Exception;
    public AeaExProjMoney getAeaExProjMoneyById(@Param("id") String id) throws Exception;
    }
