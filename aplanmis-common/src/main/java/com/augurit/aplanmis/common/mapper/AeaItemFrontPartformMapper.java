package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaItemFrontPartform;
import com.augurit.aplanmis.common.vo.AeaItemFrontPartformVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 事项的前置检查事项-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaItemFrontPartformMapper {

    void insertAeaItemFrontPartform(AeaItemFrontPartform aeaItemFrontPartform);

    void updateAeaItemFrontPartform(AeaItemFrontPartform aeaItemFrontPartform);

    void deleteAeaItemFrontPartform(@Param("id") String id);

    void deleteAeaItemFrontPartformByIds(@Param("ids") String[] ids);

    List<AeaItemFrontPartform> listAeaItemFrontPartform(AeaItemFrontPartform aeaItemFrontPartform);

    AeaItemFrontPartform getAeaItemFrontPartformById(@Param("id") String id);

    List<AeaItemFrontPartformVo> listAeaItemFrontPartformVo(AeaItemFrontPartform aeaItemFrontPartform);

    Long getMaxSortNo(@Param("itemVerId") String itemVerId, @Param("rootOrgId") String rootOrgId);

    List<AeaItemFrontPartformVo> listItemNoSelectPartform(AeaItemFrontPartform aeaItemFrontPartform);

    List<AeaItemFrontPartformVo> getAeaItemFrontPartformVoByItemVerId(@Param("itemVerId") String itemVerId, @Param("rootOrgId") String rootOrgId);

    AeaItemFrontPartformVo getAeaItemFrontPartformVoById(@Param("id") String id);

    void changIsActive(@Param("id")String id, @Param("rootOrgId")String rootOrgId);
}
