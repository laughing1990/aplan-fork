package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaProjInfoLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 项目变更信息-Mapper数据与持久化接口类
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:小糊涂</li>
 * <li>创建时间：2019-07-04 16:43:27</li>
 * </ul>
 */
@Mapper
public interface AeaProjInfoLogMapper {

    int insertAeaProjInfoLog(AeaProjInfoLog aeaProjInfoLog) throws Exception;

    int updateAeaProjInfoLog(AeaProjInfoLog aeaProjInfoLog) throws Exception;

    int deleteAeaProjInfoLog(@Param("id") String id) throws Exception;

    List<AeaProjInfoLog> listAeaProjInfoLog(AeaProjInfoLog aeaProjInfoLog) throws Exception;

    AeaProjInfoLog getAeaProjInfoLogById(@Param("id") String id) throws Exception;
}
