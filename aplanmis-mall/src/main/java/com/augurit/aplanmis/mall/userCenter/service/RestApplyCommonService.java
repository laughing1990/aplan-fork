package com.augurit.aplanmis.mall.userCenter.service;


import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.vo.AeaUnitInfoVo;
import com.augurit.aplanmis.common.vo.LinkmanTypeVo;
import com.augurit.aplanmis.mall.userCenter.vo.SmsInfoVo;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface RestApplyCommonService {

    Map<String,Object> saveOrUpdateUnitInfo(String projInfoId, List<AeaUnitInfoVo> aeaUnitInfos);

    void saveOrUpdateLinkmanTypes(List<LinkmanTypeVo> linkmanTypeVos) throws Exception;

    /**
     * 判断项目是否属于当前登录用户
     * @param projInfoId
     * @param request
     * @return
     */
    Boolean isProjBelong(String projInfoId, HttpServletRequest request);

    void insertAeaApplyinstUnitProj(String applyinstId,List<String>unitProjIds);

    /**
     * 整合暂存项目等信息逻辑接口
     * @param smsInfoVo
     * @param resultMap
     * @param aeaProjInfo
     * @return
     * @throws Exception
     */
    public Map<String, Object> getStringObjectMap(@RequestBody SmsInfoVo smsInfoVo, Map<String, Object> resultMap, AeaProjInfo aeaProjInfo) throws Exception;

    //需要将以前的记录删除，重新创建关联关系
    void deleteReInsertAeaApplyinstUnitProj(String applyinstId, List<String> unitProjIds);
}
