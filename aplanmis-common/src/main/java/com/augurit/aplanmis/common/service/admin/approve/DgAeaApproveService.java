package com.augurit.aplanmis.common.service.admin.approve;


import java.util.Map;

public interface DgAeaApproveService {


    Map<String,Object> getAeaStateAndItemsByStageId(String itemVerId, String currentBusiType);

}
