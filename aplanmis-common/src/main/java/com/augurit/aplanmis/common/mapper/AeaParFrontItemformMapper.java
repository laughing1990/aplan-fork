package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaParFrontItemform;
import com.augurit.aplanmis.common.vo.AeaParFrontItemformVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 阶段事项表单前置检测表-Mapper数据与持久化接口类
 */
@Mapper
public interface AeaParFrontItemformMapper {

    void insertAeaParFrontItemform(AeaParFrontItemform aeaParFrontItemform) ;

    void updateAeaParFrontItemform(AeaParFrontItemform aeaParFrontItemform) ;

    void deleteAeaParFrontItemform(@Param("id") String id) ;

    List<AeaParFrontItemform> listAeaParFrontItemform(AeaParFrontItemform aeaParFrontItemform) ;

    AeaParFrontItemform getAeaParFrontItemformById(@Param("id") String id) ;

    List<AeaParFrontItemformVo> listAeaParFrontItemformVo(AeaParFrontItemform aeaParFrontItemform) ;

    Long getMaxSortNo(AeaParFrontItemform aeaParFrontItemform) ;

    List<AeaParFrontItemformVo> listSelectParFrontItemform(AeaParFrontItemform aeaParFrontItemform) ;

    AeaParFrontItemformVo getAeaParFrontItemformVoById(@Param("id") String id) ;

    List<AeaParFrontItemformVo> getAeaParFrontItemformVoByStageId(@Param("stageId") String stageId,
                                                                  @Param("rootOrgId") String rootOrgId);

    void changIsActive(@Param("id") String id, @Param("rootOrgId") String rootOrgId);
}
