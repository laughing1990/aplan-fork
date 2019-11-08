package com.augurit.aplanmis.common.service.admin.credit;

import com.augurit.aplanmis.common.domain.AeaCreditSummary;
import com.augurit.aplanmis.common.dto.AeaCreditSummaryAllDto;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
* 信用管理-红黑名单管理-Service服务调用接口类
*/
public interface AeaCreditSummaryService {
    
     void saveAeaCreditSummary(AeaCreditSummary aeaCreditSummary) ;

     void updateAeaCreditSummary(AeaCreditSummary aeaCreditSummary) ;

     void deleteAeaCreditSummaryById(String id) ;

     PageInfo<AeaCreditSummary> listAeaCreditSummary(AeaCreditSummary aeaCreditSummary, Page page) ;

     AeaCreditSummary getAeaCreditSummaryById(String id) ;

     List<AeaCreditSummary> listAeaCreditSummary(AeaCreditSummary aeaCreditSummary) ;

     List<AeaCreditSummary> listCreditSummaryByBizId(String bizType, String creditType, String bizId);

     List<AeaCreditSummary> listCreditSummaryByBizCode(String bizType, String creditType, String bizCode);

     List<AeaCreditSummaryAllDto> listCreditSummaryDetailByBizId(String bizType, String bizId)throws Exception;

     List<AeaCreditSummaryAllDto> listCreditSummaryDetailByByBizCode(String bizType, String bizCode)throws Exception;
}
