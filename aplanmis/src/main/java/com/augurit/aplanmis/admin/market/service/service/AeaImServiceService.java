package com.augurit.aplanmis.admin.market.service.service;

import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.domain.AeaImService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* -Service服务调用接口类
*/
@Repository
public interface AeaImServiceService {
    public void saveAeaImService(AeaImService aeaImService) throws Exception;
    public void updateAeaImService(AeaImService aeaImService) throws Exception;
    public void deleteAeaImServiceById(String id) throws Exception;
    public PageInfo<AeaImService> listAeaImService(AeaImService aeaImService, Page page) throws Exception;
    public AeaImService getAeaImServiceById(String id) throws Exception;
    public List<AeaImService> listAeaImService(AeaImService aeaImService) throws Exception;

    public ResultForm saveConfigService(String itemVerId, String[] saveServiceIds, String[] cancelServiceIds) throws Exception;

    public List<AeaImService> listAeaImServiceNoPageByItemVerId(String itemVerId) throws Exception;
}
