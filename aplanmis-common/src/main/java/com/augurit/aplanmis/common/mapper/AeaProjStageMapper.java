package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yinlf
 * @date 2019/09/03
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
@Mapper
@Repository
public interface AeaProjStageMapper {

    void insertAeaProjStage(AeaProjStage aeaProjStage);

    void updateAeaProjStage(AeaProjStage aeaProjStage);

    void deleteAeaProjStage(@Param("id") String id);

    List<AeaProjStage> listAeaProjStage(AeaProjStage aeaProjStage);

    AeaProjStage getAeaProjStageById(@Param("id") String id);

    /**
     * 查询项目的阶段办理状态
     *
     * @param projInfoId     项目ID
     * @param standardSortNo 阶段ID
     * @return
     */
    AeaProjStage getAeaParStageByProjInfoIdAndStandardSortNo(@Param("projInfoId") String projInfoId, @Param("standardSortNo") String standardSortNo);

    /**
     * 查询项目所有阶段办理状态
     *
     * @param projInfoId
     * @return
     */
    List<AeaProjStage> findAeaParStageByProjInfoId(@Param("projInfoId") String projInfoId);

    /**
     * 根据事项实例ID查询阶段ID和项目ID
     *
     * @param iteminstId 事项实例ID
     * @return
     */
    ProjStageForm getProjInfoIdAndStageIdByIteminstId(@Param("iteminstId") String iteminstId);

    /**
     * 根据申请实例ID查询阶段ID和项目ID
     *
     * @param applyinstId 申请实例ID
     * @return
     */
    ProjStageForm getProjInfoIdAndStageIdByApplyinstId(@Param("applyinstId") String applyinstId);

    /**
     * 获取项目申报的阶段实例
     *
     * @param projInfoId     项目ID
     * @param standardSortNo 标准阶段序号
     * @return
     */
    List<AeaHiParStageinst> getStageinstByProjInfoIdAndStanrdSortNo(@Param("projInfoId") String projInfoId, @Param("standardSortNo") String standardSortNo);


    /**
     * 获取阶段下并联里程碑事项
     *
     * @param stageId
     * @return
     */
    List<AeaItemBasic> getAllMilestoneItemByStageId(@Param("stageId") String stageId);

    /**
     * 查询项目的事项是否办结通过
     *
     * @param projInfoId
     * @param itemIds
     * @return
     */
    List<AeaHiIteminst> findEndIteminstByProjInfoAndItemIds(@Param("projInfoId") String projInfoId, @Param("itemIds") String[] itemIds);
}