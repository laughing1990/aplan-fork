package com.augurit.aplanmis.common.service.admin.log;

import com.augurit.aplanmis.common.domain.AeaTraceLog;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
* 系统操作日志-Service服务调用接口类
 *
 * @author jjt
 * @date 2019/10/15
*/
public interface AeaTraceLogService {

     void saveAeaTraceLog(AeaTraceLog aeaTraceLog) ;

     void updateAeaTraceLog(AeaTraceLog aeaTraceLog) ;

     void deleteAeaTraceLogById(String id) ;

     void deleteLogMore(String[] ids);

     PageInfo<AeaTraceLog> listAeaTraceLog(AeaTraceLog aeaTraceLog, Page page) ;

     AeaTraceLog getAeaTraceLogById(String id) ;

     List<AeaTraceLog> listAeaTraceLog(AeaTraceLog aeaTraceLog) ;
}
