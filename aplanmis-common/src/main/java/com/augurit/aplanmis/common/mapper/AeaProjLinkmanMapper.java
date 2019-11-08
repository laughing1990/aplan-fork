package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaProjLinkman;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * -Mapper数据与持久化接口类
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:小糊涂</li>
 * <li>创建时间：2019-07-04 16:43:27</li>
 * </ul>
 */
@Repository
@Mapper
public interface AeaProjLinkmanMapper {

    int insertAeaProjLinkman(AeaProjLinkman aeaProjLinkman);

    int updateAeaProjLinkman(AeaProjLinkman aeaProjLinkman);

    int deleteAeaProjLinkman(@Param("id") String id);

    List<AeaProjLinkman> listAeaProjLinkman(AeaProjLinkman aeaProjLinkman);

    AeaProjLinkman getAeaProjLinkmanById(@Param("id") String id);

    int batchInsertAeaProjLinkman(@Param("aeaProjLinkmanList") List<AeaProjLinkman> aeaProjLinkmanList);

    int deleteProjLinkman(@Param("projInfoId") String projInfoId, @Param("linkmanInfoIds") String[] linkmanInfoIds);

    List<AeaProjLinkman> getAeaProjLinkmanByApplyinstId(@Param("applyinstId") String applyinstId, @Param("projInfoId") String projInfoId);
}
