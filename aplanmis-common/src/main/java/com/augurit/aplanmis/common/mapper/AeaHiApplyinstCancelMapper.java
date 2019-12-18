package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaHiApplyinstCancel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 申报撤件实例表-Mapper数据与持久化接口类
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-12-12 14:18:58</li>
 * </ul>
 */
@Mapper
@Repository
public interface AeaHiApplyinstCancelMapper {

    public void insertAeaHiApplyinstCancel(AeaHiApplyinstCancel aeaHiApplyinstCancel) throws Exception;

    public void updateAeaHiApplyinstCancel(AeaHiApplyinstCancel aeaHiApplyinstCancel) throws Exception;

    public void deleteAeaHiApplyinstCancel(@Param("id") String id) throws Exception;

    public List<AeaHiApplyinstCancel> listAeaHiApplyinstCancel(AeaHiApplyinstCancel aeaHiApplyinstCancel) throws Exception;

    public AeaHiApplyinstCancel getAeaHiApplyinstCancelById(@Param("id") String id) throws Exception;

    public List<AeaHiApplyinstCancel> listAeaHiApplyinstCancelByIteminstId(@Param("iteminstId") String iteminstId, @Param("rootOrgId") String rootOrgId) throws Exception;
}
