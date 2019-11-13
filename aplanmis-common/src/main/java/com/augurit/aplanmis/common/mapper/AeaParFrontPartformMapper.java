package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaParFrontPartform;
import com.augurit.aplanmis.common.vo.AeaParFrontPartformVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 阶段的扩展表单前置检测表-Mapper数据与持久化接口类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-11-01 10:48:23</li>
 * </ul>
 */
@Mapper
public interface AeaParFrontPartformMapper {

    void insertAeaParFrontPartform(AeaParFrontPartform aeaParFrontPartform) throws Exception;

    void updateAeaParFrontPartform(AeaParFrontPartform aeaParFrontPartform) throws Exception;

    void deleteAeaParFrontPartform(@Param("id") String id) throws Exception;

    List<AeaParFrontPartform> listAeaParFrontPartform(AeaParFrontPartform aeaParFrontPartform) throws Exception;

    AeaParFrontPartform getAeaParFrontPartformById(@Param("id") String id) throws Exception;

    List<AeaParFrontPartformVo> listAeaParFrontPartformVo(AeaParFrontPartform aeaParFrontPartform) throws Exception;

    Long getMaxSortNo(AeaParFrontPartform aeaParFrontPartform) throws Exception;

    List<AeaParFrontPartformVo> listSelectParFrontPartform(AeaParFrontPartform aeaParFrontPartform) throws Exception;

    AeaParFrontPartformVo getAeaParFrontPartformVoById(@Param("id") String id) throws Exception;

    List<AeaParFrontPartformVo> getAeaParFrontPartformVoByStageId(@Param("stageId") String stageId, @Param("rootOrgId") String rootOrgId);
}
