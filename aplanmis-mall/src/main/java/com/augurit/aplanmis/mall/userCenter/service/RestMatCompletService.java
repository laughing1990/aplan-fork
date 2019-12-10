package com.augurit.aplanmis.mall.userCenter.service;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.bsc.domain.BscAttLink;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.aplanmis.common.domain.AeaHiItemCorrectRealIninst;
import com.augurit.aplanmis.mall.userCenter.vo.UserInfoVo;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface RestMatCompletService {

    void uploadFileByCloud(String attRealIninstId, String  detailIds) throws Exception;


    /**
     * 根据材料补全实例ID查询所有申报主体及建设单位或个人信息
     * @param request
     * @param applyinstCorrectId
     * @return
     * @throws Exception
     */
    UserInfoVo getAllApplyObject(HttpServletRequest request, String applyinstCorrectId) throws Exception;

}
