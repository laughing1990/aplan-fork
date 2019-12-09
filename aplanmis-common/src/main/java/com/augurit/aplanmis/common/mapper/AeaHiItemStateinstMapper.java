package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaHiItemStateinst;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 单个事项情形实例表-Mapper数据与持久化接口类
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-07-04 16:45:15</li>
 * </ul>
 */
@Mapper
@Repository
public interface AeaHiItemStateinstMapper {

    int insertAeaHiItemStateinst(AeaHiItemStateinst aeaHiItemStateinst) throws Exception;

    int updateAeaHiItemStateinst(AeaHiItemStateinst aeaHiItemStateinst) throws Exception;

    int deleteAeaHiItemStateinst(@Param("id") String id) throws Exception;

    int batchDeleteAeaHiItemStateinst(@Param("ids") List<String> ids) throws Exception;

    List<AeaHiItemStateinst> listAeaHiItemStateinst(AeaHiItemStateinst aeaHiItemStateinst) throws Exception;

    AeaHiItemStateinst getAeaHiItemStateinstById(@Param("id") String id) throws Exception;

    /**
     * 根据主键批量查询
     *
     * @param itemStateinstIds 情形实例ID
     * @return List<AeaHiItemStateinst>
     * @throws Exception e
     */
    List<AeaHiItemStateinst> listAeaHiItemStateinstByIds(@Param("itemStateinstIds") String[] itemStateinstIds) throws Exception;

    /**
     * 根据申请实例或单项实例查询已选择的情形
     *
     * @param applyinstId  申请实例ID
     * @param seriesinstId 单项实例ID
     * @return List<AeaHiItemStateinst>
     * @throws Exception e
     */
    List<AeaHiItemStateinst> listAeaHiItemStateinstByApplyinstIdOrSeriesinstId(@Param("applyinstId") String applyinstId, @Param("seriesinstId") String seriesinstId) throws Exception;

    /**
     * 根据申请实例或阶段实例查询已选择的情形
     *
     * @param applyinstId 申请实例ID
     * @param stageinstId 阶段实例ID
     */
    List<AeaHiItemStateinst> listAeaHiItemStateinstByApplyinstIdOrStageinstId(@Param("applyinstId") String applyinstId, @Param("stageinstId") String stageinstId) throws Exception;
}
