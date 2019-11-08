package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaHiItemSpecial;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

    /**
    * 事项输入输出实例表-Mapper数据与持久化接口类
    <ul>
        <li>项目名：奥格erp3.0--第一期建设项目</li>
        <li>版本信息：v1.0</li>
        <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
        <li>创建人:Administrator</li>
        <li>创建时间：2019-08-03 10:33:44</li>
    </ul>
    */
    @Mapper
    @Repository
    public interface AeaHiItemSpecialMapper {

    public void insertAeaHiItemSpecial(AeaHiItemSpecial aeaHiItemSpecial) throws Exception;
    public void updateAeaHiItemSpecial(AeaHiItemSpecial aeaHiItemSpecial) throws Exception;
    public void deleteAeaHiItemSpecial(@Param("id") String id) throws Exception;
    public List <AeaHiItemSpecial> listAeaHiItemSpecial(AeaHiItemSpecial aeaHiItemSpecial) throws Exception;
    public AeaHiItemSpecial getAeaHiItemSpecialById(@Param("id") String id) throws Exception;

    List<AeaHiItemSpecial> getAeaHiItemSpecialByIteminstId(String iteminstId)throws Exception;
    }
