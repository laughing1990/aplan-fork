package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaItemServiceConsulting;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 服务咨询-Mapper数据与持久化接口类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:admin</li>
 * <li>创建时间：2018-10-11 17:05:02</li>
 * </ul>
 */
@Mapper
@Repository
public interface AeaItemServiceConsultingMapper {

    public void insertAeaItemServiceConsulting(AeaItemServiceConsulting aeaItemServiceConsulting) throws Exception;

    public void updateAeaItemServiceConsulting(AeaItemServiceConsulting aeaItemServiceConsulting) throws Exception;

    public void deleteAeaItemServiceConsulting(@Param("id") String id) throws Exception;

    public List<AeaItemServiceConsulting> listAeaItemServiceConsulting(AeaItemServiceConsulting aeaItemServiceConsulting) throws Exception;

    public AeaItemServiceConsulting getAeaItemServiceConsultingById(@Param("id") String id) throws Exception;

    public List<AeaItemServiceConsulting> getAeaItemServiceConsultingByItemId(@Param("itemId") String itemId);
}
