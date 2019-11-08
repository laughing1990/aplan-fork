package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaTraceLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系统操作日志-Mapper服务调用接口类
 *
 * @author jjt
 * @date 2019/10/15
 */
@Mapper
@Repository
public interface AeaTraceLogMapper {

     void insertAeaTraceLog(AeaTraceLog aeaTraceLog) ;

     void updateAeaTraceLog(AeaTraceLog aeaTraceLog) ;

     void deleteAeaTraceLog(@Param("id") String id) ;

     void deleteLogMore(@Param("ids")String[] ids);

     List <AeaTraceLog> listAeaTraceLog(AeaTraceLog aeaTraceLog) ;

     AeaTraceLog getAeaTraceLogById(@Param("id") String id) ;
}
