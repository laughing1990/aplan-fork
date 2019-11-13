package com.augurit.aplanmis.mall.userCenter.service;


import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.mall.userCenter.vo.AeaLinkmanInfoVo;
import com.augurit.aplanmis.mall.userCenter.vo.AeaUnitInfoVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface RestUserCenterService {


    String saveChildProject(HttpServletRequest request, AeaProjInfo aeaProjInfo) throws Exception;

    public String saveProjectInfo(HttpServletRequest request, AeaProjInfo aeaProjInfo) throws Exception;

    /**
     * 撤回办件,即将申报实例记录标志为逻辑删除
     * @param applyInstId
     */
    void withDrawProject(String applyInstId) throws Exception;

    /**
     * 获取用户信息并进行脱敏处理
     * @param userId
     * @return
     * @throws Exception
     */
    AeaLinkmanInfoVo getAeaLinkmanInfoByLinkmanInfoId(String userId)throws Exception;

    /**
     * 取单位信息并进行脱敏处理
     * @param unitInfoId
     * @return
     * @throws Exception
     */
    AeaUnitInfoVo getAeaUnitInfoByUnitInfoId(String unitInfoId)throws Exception;

    /**
     * 获取委托人单位信息list并进行脱敏处理
     * @param userId
     * @return
     * @throws Exception
     */
    List<AeaUnitInfoVo> getUnitInfoListByLinkmanInfoId(String userId);

    /**
     * 获取委托人列表并进行脱敏处理
     * @param unitInfoId
     * @return
     */
    List<AeaLinkmanInfoVo> findAllUnitLinkman(String unitInfoId);

}
