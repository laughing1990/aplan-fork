package com.augurit.aplanmis.supermarket.credit.service;

import com.augurit.aplanmis.common.dto.AeaCreditSummaryAllDto;

import java.util.List;

public interface SuperMarketCreditSummaryService {

    List<AeaCreditSummaryAllDto> listCreditSummaryDetailByByBizCode(String bizType, String bizCode)throws Exception;

}
