package com.augurit.aplanmis.rest.userCenter.service;


import com.augurit.aplanmis.common.domain.AeaProjInfo;

import javax.servlet.http.HttpServletRequest;

public interface RestUserCenterService {

    String saveChildProject(HttpServletRequest request, AeaProjInfo aeaProjInfo) throws Exception;

}
