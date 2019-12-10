package com.augurit.aplanmis.common.service.item;

import com.augurit.aplanmis.common.domain.AeaToleranceTimeInst;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface AeaToleranceTimeInstService {
    public void saveAeaToleranceTimeInst(AeaToleranceTimeInst aeaToleranceTimeInst) throws Exception;
    public void updateAeaToleranceTimeInst(AeaToleranceTimeInst aeaToleranceTimeInst) throws Exception;
    public void deleteAeaToleranceTimeInstById(String id) throws Exception;
    public PageInfo<AeaToleranceTimeInst> listAeaToleranceTimeInst(AeaToleranceTimeInst aeaToleranceTimeInst, Page page) throws Exception;
    public AeaToleranceTimeInst getAeaToleranceTimeInstById(String id) throws Exception;
    public List<AeaToleranceTimeInst> listAeaToleranceTimeInst(AeaToleranceTimeInst aeaToleranceTimeInst) throws Exception;
    public List<AeaToleranceTimeInst> getUnCompletedToleranceTimeinstsByJobTimerId(String orgId,String jobTimerId) throws Exception;
    void batchUpdateAeaToleranceTimeInst(List<AeaToleranceTimeInst> list) throws Exception;
    public void completeAeaToleranceTimeinst(String iteminstId) throws Exception;
    public void createToleranceSmsRemindInfo(AeaToleranceTimeInst aeaToleranceTimeInst) throws Exception;
}
