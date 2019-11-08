package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaItemServiceFlow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 办理流程-Mapper数据与持久化接口类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:27398</li>
 * <li>创建时间：2018-10-31 10:32:56</li>
 * </ul>
 */
@Mapper
@Repository
public interface AeaItemServiceFlowMapper {

    public void insertAeaItemServiceFlow(AeaItemServiceFlow aeaItemServiceFlow) throws Exception;

    public void updateAeaItemServiceFlow(AeaItemServiceFlow aeaItemServiceFlow) throws Exception;

    public void deleteAeaItemServiceFlow(@Param("id") String id) throws Exception;

    public List<AeaItemServiceFlow> listAeaItemServiceFlow(AeaItemServiceFlow aeaItemServiceFlow) throws Exception;

    public AeaItemServiceFlow getAeaItemServiceFlowById(@Param("id") String id) throws Exception;

    public AeaItemServiceFlow getAeaItemServiceFlowByItemBasicId(@Param("itemBasicId") String itemBasicId);
}
