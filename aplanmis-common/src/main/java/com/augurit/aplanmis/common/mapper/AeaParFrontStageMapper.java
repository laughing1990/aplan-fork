package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaParFrontStage;
import com.augurit.aplanmis.common.vo.AeaParFrontStageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 阶段的前置阶段检测表-Mapper数据与持久化接口类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-11-01 10:48:41</li>
 * </ul>
 */
@Mapper
public interface AeaParFrontStageMapper {

    void insertAeaParFrontStage(AeaParFrontStage aeaParFrontStage) throws Exception;

    void updateAeaParFrontStage(AeaParFrontStage aeaParFrontStage) throws Exception;

    void deleteAeaParFrontStage(@Param("id") String id) throws Exception;

    List<AeaParFrontStage> listAeaParFrontStage(AeaParFrontStage aeaParFrontStage) throws Exception;

    AeaParFrontStage getAeaParFrontStageById(@Param("id") String id) throws Exception;

    List<AeaParFrontStageVo> listAeaParFrontStageVo(AeaParFrontStage aeaParFrontStage) throws Exception;

    Long getMaxSortNo(AeaParFrontStage aeaParFrontStage) throws Exception;

    AeaParFrontStageVo getAeaParFrontStageVoById(@Param("id") String id) throws Exception;

    List<AeaParFrontStageVo> listSelectParFrontStage(AeaParFrontStage aeaParFrontStage) throws Exception;



}
