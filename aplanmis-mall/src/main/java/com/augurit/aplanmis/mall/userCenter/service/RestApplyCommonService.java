package com.augurit.aplanmis.mall.userCenter.service;


import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.vo.AeaUnitInfoVo;
import com.augurit.aplanmis.common.vo.LinkmanTypeVo;
import com.augurit.aplanmis.mall.userCenter.vo.*;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface RestApplyCommonService {

    Map<String, Object> saveOrUpdateUnitInfo(String projInfoId, List<AeaUnitInfoVo> aeaUnitInfos);

    void saveOrUpdateLinkmanTypes(List<LinkmanTypeVo> linkmanTypeVos) throws Exception;

    /**
     * 判断项目是否属于当前登录用户
     *
     * @param projInfoId
     * @param request
     * @return
     */
    Boolean isProjBelong(String projInfoId, HttpServletRequest request);

    void insertAeaApplyinstUnitProj(String applyinstId, List<String> unitProjIds);

    /**
     * 整合暂存项目等信息逻辑接口
     *
     * @param projUnitInfoVo
     * @param resultMap
     * @param aeaProjInfo
     * @return
     * @throws Exception
     */
    public Map<String, Object> getStringObjectMap(SmsProjUnitInfoVo projUnitInfoVo, Map<String, Object> resultMap, AeaProjInfo aeaProjInfo) throws Exception;

    /**
     * 整合暂存项目等信息逻辑接口
     *
     * @param projUnitInfoVo
     * @param resultMap
     * @param aeaProjInfo
     * @return
     * @throws Exception
     */
    public Map<String, Object> saveProjUnit(ProjUnitInfoVo projUnitInfoVo, Map<String, Object> resultMap, AeaProjInfo aeaProjInfo) throws Exception;

    String saveSmsInfo(SmsInfoVo smsInfoVo) throws Exception;

    //需要将以前的记录删除，重新创建关联关系
    void deleteReInsertAeaApplyinstUnitProj(String applyinstId, List<String> unitProjIds);

    void deleteReInsertAeaApplyinstUnitProjCurrentLogin(String applyinstId, String unitInfoId, String linkmanInfoId, String projInfoId);

    void deleteReInsertAeaProjLinkmanCurrentLogin(String applyinstId, String userId, String projInfoId);

    void saveOrUpdateAeaApplyinstProj(String applyinstId, String projInfoId) throws Exception;

   // Map<String, Object> submitItemList(ItemListTemporaryParamVo itemListTemporaryParamVo,Map<String,Object> map) throws Exception;

    void insertSeriesIteminst(String seriesApplyinstId, String itemVerId,Map<String,Object> resultMap) throws Exception;

    public List<AeaHiIteminst> deleteReInsertIteminstUnderStageinst(String themeVerId, String stageinstId, List<String> itemVerIds, String appinstId, String branchOrgMap) throws Exception;

    public void deleteReInsertParStateinstUnderStageinst(String applyinstId, String stageinstId, String[] stateIds) throws Exception;

    public void deleteMatUnderIteminst(List<AeaHiIteminst> iteminstList);

    public void saveItemStateBySimpleMerge(List<ParallelItemStateVo> parallelItemStateVoList, List<String> itemVerIds, String applyinstId, String stageinstId);

    public void deleteItemStates(String applyinstId) throws Exception;

    void submitMatmList(MatListCommonTemporaryParamVo matListTemporaryParamVo) throws Exception;

    void submitSeriesStateList(StateListSeriesTemporaryParamVo stateListSeriesTemporaryParamVo,String seriesinstId, Map<String, Object> map) throws Exception;

    void deleteApplyinstAllInstData(String applyinstId) throws Exception;
}