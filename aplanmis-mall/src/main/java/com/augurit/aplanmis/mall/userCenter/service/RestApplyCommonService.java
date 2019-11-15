package com.augurit.aplanmis.mall.userCenter.service;


import com.augurit.aplanmis.common.vo.AeaUnitInfoVo;
import com.augurit.aplanmis.common.vo.LinkmanTypeVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface RestApplyCommonService {

    List<String> saveOrUpdateUnitInfo(String projInfoId, List<AeaUnitInfoVo> aeaUnitInfos);

    void saveOrUpdateLinkmanTypes(List<LinkmanTypeVo> linkmanTypeVos) throws Exception;

    /**
     * 判断项目是否属于当前登录用户
     * @param projInfoId
     * @param request
     * @return
     */
    Boolean isProjBelong(String projInfoId, HttpServletRequest request);
}
