package com.augurit.aplanmis.common.service.solicit;

import com.augurit.aplanmis.common.domain.AeaHiSolicitDetail;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;
/**
* 征求意见详情表-Service服务调用接口类
*/
public interface AeaHiSolicitDetailService {
    public void saveAeaHiSolicitDetail(AeaHiSolicitDetail aeaHiSolicitDetail) throws Exception;
    public void updateAeaHiSolicitDetail(AeaHiSolicitDetail aeaHiSolicitDetail) throws Exception;
    public void deleteAeaHiSolicitDetailById(String id) throws Exception;
    public PageInfo<AeaHiSolicitDetail> listAeaHiSolicitDetail(AeaHiSolicitDetail aeaHiSolicitDetail, Page page) throws Exception;
    public AeaHiSolicitDetail getAeaHiSolicitDetailById(String id) throws Exception;
    public List<AeaHiSolicitDetail> listAeaHiSolicitDetail(AeaHiSolicitDetail aeaHiSolicitDetail) throws Exception;

}
