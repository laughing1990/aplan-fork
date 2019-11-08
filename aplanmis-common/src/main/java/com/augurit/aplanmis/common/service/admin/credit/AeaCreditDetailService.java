package com.augurit.aplanmis.common.service.admin.credit;

import com.augurit.aplanmis.common.domain.AeaCreditDetail;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
* 信用管理-信用汇总子表（字段列表）-Service服务调用接口类
*/
public interface AeaCreditDetailService {
    
     void saveAeaCreditDetail(AeaCreditDetail aeaCreditDetail) ;

     void updateAeaCreditDetail(AeaCreditDetail aeaCreditDetail) ;

     void deleteAeaCreditDetailById(String id) ;

     PageInfo<AeaCreditDetail> listAeaCreditDetail(AeaCreditDetail aeaCreditDetail, Page page) ;

     AeaCreditDetail getAeaCreditDetailById(String id) ;

     List<AeaCreditDetail> listAeaCreditDetail(AeaCreditDetail aeaCreditDetail) ;

}
