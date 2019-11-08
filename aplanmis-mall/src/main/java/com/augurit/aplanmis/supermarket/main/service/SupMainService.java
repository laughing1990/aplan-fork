package com.augurit.aplanmis.supermarket.main.service;


import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.supermarket.main.vo.IndexDataVo;

import java.util.List;
import java.util.Map;

/**
 * 中介超市
 */
public interface SupMainService {
    List<AeaUnitInfo> listAgencyOrg(String agencyName, String serviceType, String stimeOrder, String countOrder, String commentOrder);

    List<AeaUnitInfo> oneAgency(String agencyName);

    List<AeaItemBasic> listService(String serviceType);

    List<AeaItemBasic> listItemName(String itemName);

    IndexDataVo getIndexData() throws Exception;

    Map getFileStream(String detailId) throws Exception;
}


