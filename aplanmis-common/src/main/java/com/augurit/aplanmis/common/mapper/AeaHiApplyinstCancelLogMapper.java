package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaHiApplyinstCancelLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 申报撤件实例和申请实例历史状态关联表-Mapper数据与持久化接口类
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-12-12 14:18:59</li>
 * </ul>
 */
@Mapper
@Repository
public interface AeaHiApplyinstCancelLogMapper {

    public void insertAeaHiApplyinstCancelLog(AeaHiApplyinstCancelLog aeaHiApplyinstCancelLog) throws Exception;

    public void updateAeaHiApplyinstCancelLog(AeaHiApplyinstCancelLog aeaHiApplyinstCancelLog) throws Exception;

    public void deleteAeaHiApplyinstCancelLog(@Param("id") String id) throws Exception;

    public List<AeaHiApplyinstCancelLog> listAeaHiApplyinstCancelLog(AeaHiApplyinstCancelLog aeaHiApplyinstCancelLog) throws Exception;

    public AeaHiApplyinstCancelLog getAeaHiApplyinstCancelLogById(@Param("id") String id) throws Exception;
}
