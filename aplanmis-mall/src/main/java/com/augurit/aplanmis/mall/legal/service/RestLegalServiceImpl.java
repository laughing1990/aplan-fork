package com.augurit.aplanmis.mall.legal.service;


import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.sc.att.service.IBscAttService;
import com.augurit.aplanmis.common.domain.AeaServiceLegal;
import com.augurit.aplanmis.common.dto.ApproveProjInfoDto;
import com.augurit.aplanmis.common.mapper.AeaServiceLegalMapper;
import com.augurit.aplanmis.common.service.admin.legal.AeaServiceLegalAdminService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RestLegalServiceImpl {

    @Autowired
    AeaServiceLegalMapper aeaServiceLegalMapper;
    @Autowired
    IBscAttService bscAttService;
    @Autowired
    AeaServiceLegalAdminService legalAdminService;
    @Value("${dg.sso.access.platform.org.top-org-id:0368948a-1cdf-4bf8-a828-71d796ba89f6}")
    protected String topOrgId;
    public PageInfo<AeaServiceLegal> getLegalListByKeyword(String keyword, int pageNum, int pageSize) throws Exception {
        AeaServiceLegal aeaServiceLegal = new AeaServiceLegal();
        aeaServiceLegal.setLegalLevel(keyword);
        PageHelper.startPage(pageNum,pageSize);
        List<AeaServiceLegal> list = aeaServiceLegalMapper.listAeaServiceLegal(aeaServiceLegal);
        for (AeaServiceLegal legal:list){
            List<BscAttForm> attList = bscAttService.listAttLinkAndDetailNoPage("AEA_SERVICE_LEGAL", "SERVICE_LEGAL_ATT", legal.getLegalId(), null, topOrgId, null);
            legal.setLegalAtts((attList == null||attList.size()==0) ?new ArrayList<>(0):attList.subList(0,1));
            legal.setServiceLegalAttCount(attList == null ? 0L : attList.size());
        }
        return new PageInfo(list);
    }
}
