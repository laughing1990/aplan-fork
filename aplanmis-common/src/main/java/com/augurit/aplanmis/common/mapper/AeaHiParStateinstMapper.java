package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaHiParStateinst;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 主题阶段情形实例表-Mapper数据与持久化接口类
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-07-04 16:45:19</li>
 * </ul>
 */
@Mapper
@Repository
public interface AeaHiParStateinstMapper {

    public void insertAeaHiParStateinst(AeaHiParStateinst aeaHiParStateinst) throws Exception;

    public void updateAeaHiParStateinst(AeaHiParStateinst aeaHiParStateinst) throws Exception;

    public void deleteAeaHiParStateinst(@Param("id") String id) throws Exception;

    public List<AeaHiParStateinst> listAeaHiParStateinst(AeaHiParStateinst aeaHiParStateinst) throws Exception;

    public AeaHiParStateinst getAeaHiParStateinstById(@Param("id") String id) throws Exception;

    /**
     * 根据阶段实例ID获取已选择的情形实例
     *
     * @param stageinstId 情形实例ID
     * @param applyinstId 申请实例ID
     * @return List<AeaHiParStateinst> 情形实例列表
     * @throws Exception e
     */
    List<AeaHiParStateinst> listAeaHiParStateinstByApplyinstIdOrStageinstId(@Param("applyinstId") String applyinstId, @Param("stageinstId") String stageinstId) throws Exception;

    void batchDeleteAeaHiParStateinst(@Param("ids")String[] stateinstIds);
}
