package com.augurit.aplanmis.common.service.solicit;

import com.augurit.aplanmis.common.domain.AeaHiSolicit;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;
/**
* 征求意见主表-Service服务调用接口类
*/
public interface AeaHiSolicitService {
    public void saveAeaHiSolicit(AeaHiSolicit aeaHiSolicit) throws Exception;
    public void updateAeaHiSolicit(AeaHiSolicit aeaHiSolicit) throws Exception;
    public void deleteAeaHiSolicitById(String id) throws Exception;
    public PageInfo<AeaHiSolicit> listAeaHiSolicit(AeaHiSolicit aeaHiSolicit, Page page) throws Exception;
    public AeaHiSolicit getAeaHiSolicitById(String id) throws Exception;
    public List<AeaHiSolicit> listAeaHiSolicit(AeaHiSolicit aeaHiSolicit) throws Exception;

}
