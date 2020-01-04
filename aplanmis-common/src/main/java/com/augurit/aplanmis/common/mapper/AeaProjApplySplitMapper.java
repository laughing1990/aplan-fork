package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaProjApplySplit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 项目拆分申请表-Mapper数据与持久化接口类
*/
@Mapper
public interface AeaProjApplySplitMapper {

    void insertAeaProjApplySplit(AeaProjApplySplit aeaProjApplySplit) throws Exception;

    void updateAeaProjApplySplit(AeaProjApplySplit aeaProjApplySplit) throws Exception;

    void deleteAeaProjApplySplit(@Param("id") String id) throws Exception;

    List<AeaProjApplySplit> listAeaProjApplySplit(AeaProjApplySplit aeaProjApplySplit) throws Exception;

    AeaProjApplySplit getAeaProjApplySplitById(@Param("id") String id) throws Exception;


    List<AeaProjApplySplit> listSplitedProjInfo(String projInfoId);
}
