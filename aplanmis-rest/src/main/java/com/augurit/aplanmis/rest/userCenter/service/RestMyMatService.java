package com.augurit.aplanmis.rest.userCenter.service;

import com.augurit.aplanmis.common.domain.AeaHiItemMatinst;

import java.util.List;

public interface RestMyMatService {

    List<AeaHiItemMatinst> getMyMatListByUser(String unitInfoId, String userInfoId) throws Exception;

}
