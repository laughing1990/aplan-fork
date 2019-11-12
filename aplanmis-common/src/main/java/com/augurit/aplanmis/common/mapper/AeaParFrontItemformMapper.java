package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaParFrontItemform;
import com.augurit.aplanmis.common.vo.AeaParFrontItemformVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 阶段事项表单前置检测表-Mapper数据与持久化接口类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-11-01 10:48:12</li>
 * </ul>
 */
@Mapper
public interface AeaParFrontItemformMapper {

    void insertAeaParFrontItemform(AeaParFrontItemform aeaParFrontItemform) throws Exception;

    void updateAeaParFrontItemform(AeaParFrontItemform aeaParFrontItemform) throws Exception;

    void deleteAeaParFrontItemform(@Param("id") String id) throws Exception;

    List<AeaParFrontItemform> listAeaParFrontItemform(AeaParFrontItemform aeaParFrontItemform) throws Exception;

    AeaParFrontItemform getAeaParFrontItemformById(@Param("id") String id) throws Exception;

    List<AeaParFrontItemformVo> listAeaParFrontItemformVo(AeaParFrontItemform aeaParFrontItemform) throws Exception;

    Long getMaxSortNo(AeaParFrontItemform aeaParFrontItemform) throws Exception;

    List<AeaParFrontItemformVo> listSelectParFrontItemform(AeaParFrontItemform aeaParFrontItemform) throws Exception;

    AeaParFrontItemformVo getAeaParFrontItemformVoById(@Param("id") String id) throws Exception;

    List<AeaParFrontItemformVo> getAeaParFrontItemformVoByStageId(@Param("stageId") String stageId, @Param("rootOrgId") String rootOrgId);
}
