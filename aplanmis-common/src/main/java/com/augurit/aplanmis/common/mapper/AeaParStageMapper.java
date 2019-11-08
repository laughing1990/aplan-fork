package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.domain.InFormBill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 阶段/环节定义表-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaParStageMapper {

    int insertAeaParStage(AeaParStage aeaParStage) ;

    int updateAeaParStage(AeaParStage aeaParStage) ;

    int deleteAeaParStage(@Param("id") String id) ;

    List<AeaParStage> listAeaParStage(AeaParStage aeaParStage) ;

    AeaParStage getAeaParStageById(@Param("id") String id) ;

    /**
     * 根据主题版本ID查询版本列表
     * @param themeVerId 主题版本ID
     * @return List<AeaParStage>
     */
    List<AeaParStage> listAeaParStageByThemeVerId(String themeVerId);

    List<AeaParStage> getAeaParStageListByIteminstId(String iteminstId);

    /**
     * 获取某主题版本下最大的阶段排序号 (后端)
     *
     * @param themeVerId
     * @param rootOrgId
     * @return
     */
    Long getStageMaxSortNoByThemeVerId(@Param("themeVerId")String themeVerId,
                                       @Param("rootOrgId")String rootOrgId);

    void deleteAeaParStageByIds(@Param("stageIds") String[] stageIds);

    List<InFormBill> getPrintInFormBillData(@Param("stageinstId") String stageinstId);

    List<AeaParStage> getAeaParStageByApplyinstId(@Param("applyinstId") String applyinstId);

    List<String> getApplyInstStatusByProjInfoIdAndStageIdAndLinkmanInfoId(@Param("stageId") String stageId, @Param("projInfoId") String projInfoId,@Param("linkmanInfoId") String linkmanInfoId);


    List<String> getApplyInstStatusByProjInfoIdAndStageIdAndUnitInfoId(@Param("stageId") String stageId, @Param("projInfoId") String projInfoId, @Param("unitInfoId") String unitInfoId);


    List<AeaParStage> listAeaParStageByIds(@Param("ids") List<String> ids);

    List<AeaParStage> listTestOrPublishThemeVerRelStage(@Param("rootOrgId")String rootOrgId);

    /**
     * 根据标准阶段序号(DYBZSPJDXH)查询所有阶段ID
     * @param xh
     * @return
     */
    List<String> getStageIdByDYBZSPJDXH(@Param("xh") String xh);
}
