package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaParFrontPartform;
import com.augurit.aplanmis.common.vo.AeaParFrontPartformVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 阶段的扩展表单前置检测表-Mapper数据与持久化接口类
 */
@Mapper
public interface AeaParFrontPartformMapper {

    void insertAeaParFrontPartform(AeaParFrontPartform aeaParFrontPartform) ;

    void updateAeaParFrontPartform(AeaParFrontPartform aeaParFrontPartform) ;

    void deleteAeaParFrontPartform(@Param("id") String id) ;

    List<AeaParFrontPartform> listAeaParFrontPartform(AeaParFrontPartform aeaParFrontPartform) ;

    AeaParFrontPartform getAeaParFrontPartformById(@Param("id") String id) ;

    List<AeaParFrontPartformVo> listAeaParFrontPartformVo(AeaParFrontPartform aeaParFrontPartform) ;

    Long getMaxSortNo(AeaParFrontPartform aeaParFrontPartform) ;

    List<AeaParFrontPartformVo> listSelectParFrontPartform(AeaParFrontPartform aeaParFrontPartform) ;

    AeaParFrontPartformVo getAeaParFrontPartformVoById(@Param("id") String id) ;

    List<AeaParFrontPartformVo> getAeaParFrontPartformVoByStageId(@Param("stageId") String stageId,
                                                                  @Param("rootOrgId") String rootOrgId);

    void changIsActive(@Param("id") String id, @Param("rootOrgId") String rootOrgId);
}
