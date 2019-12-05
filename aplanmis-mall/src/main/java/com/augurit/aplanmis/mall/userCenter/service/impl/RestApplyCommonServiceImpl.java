package com.augurit.aplanmis.mall.userCenter.service.impl;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.aplanmis.common.constants.AeaUnitConstants;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.instance.*;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.augurit.aplanmis.common.vo.AeaUnitInfoVo;
import com.augurit.aplanmis.common.vo.LinkmanTypeVo;
import com.augurit.aplanmis.common.vo.LoginInfoVo;
import com.augurit.aplanmis.mall.userCenter.service.RestApplyCommonService;
import com.augurit.aplanmis.mall.userCenter.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RestApplyCommonServiceImpl implements RestApplyCommonService {

    @Autowired
    private AeaUnitInfoService aeaUnitInfoService;
    @Autowired
    private AeaUnitProjMapper aeaUnitProjMapper;
    @Autowired
    private AeaUnitProjLinkmanMapper aeaUnitProjLinkmanMapper;
    @Autowired
    private AeaUnitLinkmanMapper aeaUnitLinkmanMapper;
    @Autowired
    private AeaApplyinstUnitProjMapper aeaApplyinstUnitProjMapper;
    @Autowired
    private AeaHiSmsInfoService aeaHiSmsInfoService;
    @Autowired
    private AeaProjInfoService aeaProjInfoService;
    @Autowired
    private RestApplyCommonService restApplyCommonService;
    @Autowired
    private AeaProjLinkmanMapper aeaProjLinkmanMapper;
    @Autowired
    private AeaApplyinstProjMapper aeaApplyinstProjMapper;
    @Autowired
    private AeaHiParStageinstService aeaHiParStageinstService;
    @Autowired
    private AeaHiIteminstService aeaHiIteminstService;
    @Autowired
    private AeaHiParStateinstService aeaHiParStateinstService;
    @Autowired
    private AeaParStageMapper aeaParStageMapper;
    @Autowired
    private AeaHiItemStateinstService aeaHiItemStateinstService;
    @Autowired
    private AeaHiParStateinstMapper aeaHiParStateinstMapper;
    @Autowired
    private AeaHiSeriesinstService aeaHiSeriesinstService;

    @Override
    public Map<String,Object> saveOrUpdateUnitInfo(String projInfoId, List<AeaUnitInfoVo> aeaUnitInfos) {
        Map<String,Object> map=new HashMap<>(2);
        List<String> projUnitIds = new ArrayList<>();
        if (aeaUnitInfos==null||aeaUnitInfos.size()==0) return map;
        AeaUnitProj aeaUnitProj = new AeaUnitProj();
        aeaUnitProj.setProjInfoId(projInfoId);
        aeaUnitProj.setCreater(SecurityContext.getCurrentUserId());
        aeaUnitProj.setCreateTime(new Date());
        List<Map> unitReturnJson=new ArrayList<>();
        aeaUnitInfos.stream().forEach(aeaUnitInfo -> {
            Map<String,Object> jsonMap=new HashMap<>(2);
            String unitInfoId = "";
            //unitInfoId = aeaUnitInfo.getUnitInfoId();
            if (StringUtils.isEmpty(aeaUnitInfo.getUnitInfoId())){
                aeaUnitInfo.setUnitInfoId(UUID.randomUUID().toString());
                aeaUnitInfoService.insertAeaUnitInfo(AeaUnitInfoVo.returnForm(aeaUnitInfo));
            }else  {
                aeaUnitInfoService.updateAeaUnitInfo(AeaUnitInfoVo.returnForm(aeaUnitInfo));
            }
            unitInfoId = aeaUnitInfo.getUnitInfoId();
            jsonMap.put("unitInfoId",unitInfoId);
            jsonMap.put("unifiedSocialCreditCode",aeaUnitInfo.getUnifiedSocialCreditCode());
            unitReturnJson.add(jsonMap);
            //AeaLinkmanInfo selectedLinkman = aeaUnitInfo.getSelectedLinkman();
            aeaUnitProj.setUnitType(aeaUnitInfo.getUnitType());
            aeaUnitProj.setIsOwner(AeaUnitConstants.IS_OWNER_TRUE);
            aeaUnitProj.setUnitInfoId(unitInfoId);
            aeaUnitProj.setIsDeleted("0");
            try {
                testUnitLinkman(aeaUnitInfo);//检测单位和当前联系人是否存在关联关系，若不存在，则需保存
                List<AeaUnitProj> unitProjList =  aeaUnitProjMapper.listAeaUnitProj(aeaUnitProj);//先判断有无关联关系
                if (unitProjList==null||unitProjList.size()==0){
                    String unitProjId = UUID.randomUUID().toString();
                    aeaUnitProj.setUnitProjId(unitProjId);
                    aeaUnitProj.setLinkmanInfoId(aeaUnitInfo.getLinkmanInfoId());
                    aeaUnitProjMapper.insertAeaUnitProj(aeaUnitProj);
                    projUnitIds.add(unitProjId);
                } else {
                    projUnitIds.add(unitProjList.get(0).getUnitProjId());
                }
                if (aeaUnitInfo.getLinkmanTypes() != null && aeaUnitInfo.getLinkmanTypes().size() > 0 && StringUtils.isNotBlank(aeaUnitProj.getUnitProjId())) {
                    AeaUnitProjLinkman aeaUnitProjLinkman = new AeaUnitProjLinkman();
                    aeaUnitProjLinkman.setCreater(SecurityContext.getCurrentUserName());
                    aeaUnitProjLinkman.setCreateTime(new Date());
                    aeaUnitProjLinkman.setIsDeleted("0");
                    aeaUnitProjLinkman.setUnitProjId(aeaUnitProj.getUnitProjId());
                    AeaUnitProjLinkman query = new AeaUnitProjLinkman();
                    query.setUnitProjId(aeaUnitProj.getUnitProjId());
                    for (LinkmanTypeVo vo : aeaUnitInfo.getLinkmanTypes()) {
                        aeaUnitProjLinkman.setLinkmanInfoId(vo.getLinkmanInfoId());
                        aeaUnitProjLinkman.setLinkmanType(vo.getLinkmanType());
                        query.setLinkmanType(vo.getLinkmanType());
                        List<AeaUnitProjLinkman> list = aeaUnitProjLinkmanMapper.listAeaUnitProjLinkman(query);
                        if (list.size() == 0) {
                            aeaUnitProjLinkman.setProjLinkmanId(UUID.randomUUID().toString());
                            aeaUnitProjLinkmanMapper.insertAeaUnitProjLinkman(aeaUnitProjLinkman);
                        } else {
                            AeaUnitProjLinkman updateParam = list.get(0);
                            updateParam.setModifier(SecurityContext.getCurrentUserName());
                            updateParam.setModifyTime(new Date());
                            updateParam.setLinkmanInfoId(vo.getLinkmanInfoId());
                            aeaUnitProjLinkmanMapper.updateAeaUnitProjLinkman(updateParam);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        map.put("unitProjIds",projUnitIds);
        map.put("unitReturnJson",unitReturnJson);
        return map;
    }

    private void testUnitLinkman(AeaUnitInfoVo aeaUnitInfo) throws Exception {
        String currentUnitId=aeaUnitInfo.getUnitInfoId();
        String currentLinkmanInfoId=aeaUnitInfo.getLinkmanInfoId();
        if(StringUtils.isNotBlank(currentUnitId) && StringUtils.isNotBlank(currentLinkmanInfoId)){
            AeaUnitLinkman param=new AeaUnitLinkman();
            param.setLinkmanInfoId(currentLinkmanInfoId);
            param.setUnitInfoId(currentUnitId);
            List<AeaUnitLinkman> list = aeaUnitLinkmanMapper.listAeaUnitLinkman(param);
            if(list.size()==0){
                param.setCreater(SecurityContext.getCurrentUserName());
                param.setCreateTime(new Date());
                param.setUnitLinkmanId(UUID.randomUUID().toString());
                aeaUnitLinkmanMapper.insertAeaUnitLinkman(param);
            }
        }
    }

    @Override
    public void saveOrUpdateLinkmanTypes(List<LinkmanTypeVo> linkmanTypeVos) throws Exception {
        if (linkmanTypeVos.size() > 0) {
            AeaUnitProjLinkman aeaUnitProjLinkman = new AeaUnitProjLinkman();
            aeaUnitProjLinkman.setCreater(SecurityContext.getCurrentUserName());
            aeaUnitProjLinkman.setCreateTime(new Date());
            aeaUnitProjLinkman.setIsDeleted("0");
            AeaUnitProjLinkman query = new AeaUnitProjLinkman();
            for (LinkmanTypeVo vo : linkmanTypeVos) {
                AeaUnitProj aeaUnitProj = new AeaUnitProj();
                aeaUnitProj.setUnitInfoId(vo.getUnitInfoId());
                aeaUnitProj.setProjInfoId(vo.getProjInfoId());
                aeaUnitProj.setUnitType("1");//建设单位
                List<AeaUnitProj> unitProjList = aeaUnitProjMapper.listAeaUnitProj(aeaUnitProj);//先判断有无关联关系
                if (unitProjList.size() > 0) {
                    aeaUnitProjLinkman.setUnitProjId(unitProjList.get(0).getUnitProjId());
                    query.setUnitProjId(unitProjList.get(0).getUnitProjId());
                    aeaUnitProjLinkman.setLinkmanInfoId(vo.getLinkmanInfoId());
                    aeaUnitProjLinkman.setLinkmanType(vo.getLinkmanType());
                    query.setLinkmanType(vo.getLinkmanType());
                    List<AeaUnitProjLinkman> list = aeaUnitProjLinkmanMapper.listAeaUnitProjLinkman(query);
                    if (list.size() == 0) {
                        aeaUnitProjLinkman.setProjLinkmanId(UUID.randomUUID().toString());
                        aeaUnitProjLinkmanMapper.insertAeaUnitProjLinkman(aeaUnitProjLinkman);
                    } else {
                        AeaUnitProjLinkman updateParam = list.get(0);
                        updateParam.setModifier(SecurityContext.getCurrentUserName());
                        updateParam.setModifyTime(new Date());
                        updateParam.setLinkmanInfoId(vo.getLinkmanInfoId());
                        aeaUnitProjLinkmanMapper.updateAeaUnitProjLinkman(updateParam);
                    }
                }
            }
        }
    }

    @Override
    public Boolean isProjBelong(String projInfoId, HttpServletRequest request) {

        return null;
    }

    @Override
    public void insertAeaApplyinstUnitProj(String applyinstId,List<String>unitProjIds){
        if(unitProjIds!=null && unitProjIds.size()>0 && StringUtils.isNotBlank(applyinstId)){
            List<AeaApplyinstUnitProj> paramList=new ArrayList<>();
            for (String unitProjId:unitProjIds){
                AeaApplyinstUnitProj entity=new AeaApplyinstUnitProj();
                entity.setUnitProjId(unitProjId);
                entity.setApplyinstId(applyinstId);
                entity.setIsDeleted("0");
                List<AeaApplyinstUnitProj> list = aeaApplyinstUnitProjMapper.listAeaApplyinstUnitProj(entity);
                if(list.size()>0) continue;
                entity.setApplyinstUnitProjId(UUID.randomUUID().toString());
                entity.setCreater(SecurityContext.getCurrentUserName());
                entity.setCreateTime(new Date());
                paramList.add(entity);
            }
            if(paramList.size()==0) return;
            aeaApplyinstUnitProjMapper.batchInsertAeaApplyinstUnitProjMapper(paramList);
        }
    }

    @Override
    public Map<String, Object> getStringObjectMap(@RequestBody SmsInfoVo smsInfoVo, Map<String, Object> resultMap, AeaProjInfo aeaProjInfo) throws Exception {
        String smsId;//保存或修改领件人信息
        if (StringUtils.isBlank(smsInfoVo.getId())) {
            AeaHiSmsInfo aeaHiSmsInfo = aeaHiSmsInfoService.createAeaHiSmsInfo(smsInfoVo.toSmsInfo());
            smsId = aeaHiSmsInfo.getId();
        } else {
            AeaHiSmsInfo aeaHiSmsInfo = aeaHiSmsInfoService.getAeaHiSmsInfoById(smsInfoVo.getId());
            aeaHiSmsInfoService.updateAeaHiSmsInfo(smsInfoVo.merge(aeaHiSmsInfo));
            smsId = smsInfoVo.getId();
        }
        //修改项目信息
        if (StringUtils.isBlank(aeaProjInfo.getGcbm()))
            aeaProjInfo.setGcbm(aeaProjInfo.getLocalCode());
        aeaProjInfoService.updateAeaProjInfo(aeaProjInfo);
        //当前企业用户的人员设置
        if (smsInfoVo.getLinkmanTypeVos() != null && smsInfoVo.getLinkmanTypeVos().size() > 0) {
            restApplyCommonService.saveOrUpdateLinkmanTypes(smsInfoVo.getLinkmanTypeVos());
        }

        //项目单位关联
        //List<String> projUnitIds = new ArrayList<>();
        Map<String, Object> map = new HashMap<>(2);
        if (smsInfoVo.getAeaUnitInfos() != null && smsInfoVo.getAeaUnitInfos().size() > 0) {
            map = restApplyCommonService.saveOrUpdateUnitInfo(aeaProjInfo.getProjInfoId(), smsInfoVo.getAeaUnitInfos());
        }
        resultMap.put("smsId", smsId);
        resultMap.put("unitProjIds", map.get("unitProjIds"));
        resultMap.put("unitReturnJson", map.get("unitReturnJson"));
        resultMap.put("regionalism", aeaProjInfo.getRegionalism());
        return map;
    }

    @Override
    public void deleteReInsertAeaApplyinstUnitProj(String applyinstId, List<String> unitProjIds){
        List<AeaApplyinstUnitProj> applyinstUnitProjList = aeaApplyinstUnitProjMapper.getAeaApplyinstUnitProjByApplyinstId(applyinstId);
        if(applyinstUnitProjList.size()>0){
            applyinstUnitProjList.stream().forEach(entity->{
                entity.setIsDeleted("1");
                aeaApplyinstUnitProjMapper.updateAeaApplyinstUnitProj(entity);//逻辑删除
            });
        }
        insertAeaApplyinstUnitProj(applyinstId,unitProjIds);
    }

    @Override
    public void deleteReInsertAeaApplyinstUnitProjCurrentLogin(String applyinstId, String unitInfoId,String linkmanInfoId , String projInfoId){
        List<AeaUnitProj> unitProjList = aeaUnitProjMapper.findUnitProjByProjIdAndUnitIdAndunitType(projInfoId, unitInfoId, "1");
        List<String> unitProjIds=new ArrayList<>();
        if(unitProjList.size()==0){
            String unitProjId=UUID.randomUUID().toString();
            AeaUnitProj entity=new AeaUnitProj();
            entity.setIsDeleted("0");
            entity.setUnitInfoId(unitInfoId);
            entity.setIsOwner("1");
            entity.setProjInfoId(projInfoId);
            entity.setCreater(SecurityContext.getCurrentUserName());
            entity.setCreateTime(new Date());
            entity.setUnitType("1");
            entity.setLinkmanInfoId(linkmanInfoId);
            aeaUnitProjMapper.insertAeaUnitProj(entity);
            unitProjIds.add(unitProjId);
        }else{
            unitProjIds=unitProjList.stream().map(AeaUnitProj::getUnitProjId).collect(Collectors.toList());
        }
        deleteReInsertAeaApplyinstUnitProj(applyinstId,unitProjIds);
    }

    @Override
    public void deleteReInsertAeaProjLinkmanCurrentLogin(String applyinstId, String userId, String projInfoId){
        AeaProjLinkman query=new AeaProjLinkman();
        query.setApplyinstId(applyinstId);
        query.setProjInfoId(projInfoId);
        query.setLinkmanInfoId(userId);
        query.setType("apply");
        List<AeaProjLinkman> projLinkmans = aeaProjLinkmanMapper.listAeaProjLinkman(query);
        if(projLinkmans.size()==0){
            String[] types={"apply","link"};
            for (int i=0;i<2;i++){
                AeaProjLinkman entity=new AeaProjLinkman();
                entity.setLinkmanInfoId(userId);
                entity.setProjInfoId(projInfoId);
                entity.setApplyinstId(applyinstId);
                entity.setCreater(SecurityContext.getCurrentUserName());
                entity.setCreateTime(new Date());
                entity.setType(types[i]);
                entity.setProjLinkmanId(UUID.randomUUID().toString());
                aeaProjLinkmanMapper.insertAeaProjLinkman(entity);
            }
        }
    }

    @Override
    public void saveOrUpdateAeaApplyinstProj(String applyinstId, String projInfoId) throws Exception {
        List<AeaApplyinstProj> list = aeaApplyinstProjMapper.getAeaApplyinstProjByApplyinstId(applyinstId);
        if(list.size()==0||!projInfoId.equals(list.get(0).getProjInfoId())){
            AeaApplyinstProj entity=new AeaApplyinstProj();
            entity.setCreateTime(new Date());
            entity.setCreater(SecurityContext.getCurrentUserName());
            entity.setProjInfoId(projInfoId);
            entity.setApplyinstId(applyinstId);
            entity.setApplyinstProjId(UUID.randomUUID().toString());
            aeaApplyinstProjMapper.insertAeaApplyinstProj(entity);
        }
    }

    @Override
    public Map<String,Object>  submitItemList(ItemListTemporaryParamVo itemListTemporaryParamVo) throws Exception {
        Map<String,Object> map=new HashMap<>();
        String applyinstId=itemListTemporaryParamVo.getApplyinstId();
        String stageId=itemListTemporaryParamVo.getStageId();
        String stageinstId=itemListTemporaryParamVo.getStageinstId();
        String themeVerId=itemListTemporaryParamVo.getThemeVerId();
        String[] stateIds=itemListTemporaryParamVo.getStateIds();
        List<String> itemVerIds=itemListTemporaryParamVo.getItemVerIds();
        List<ParallelItemStateVo> parallelItemStateVoList=itemListTemporaryParamVo.getParallelItemStateIds();
        String appinstId=UUID.randomUUID().toString();
        AeaParStage aeaParStage = aeaParStageMapper.getAeaParStageById(stageId);
        if(StringUtils.isNotBlank(applyinstId) && StringUtils.isNotBlank(stageinstId)){//说明此前已经实例化过阶段，则需判断是否切换了阶段
            AeaHiParStageinst stageinst = aeaHiParStageinstService.getAeaHiParStageinstById(stageinstId);
            if(stageinst==null){
                AeaHiParStageinst newStageinst=aeaHiParStageinstService.createAeaHiParStageinst(applyinstId,stageId,themeVerId,appinstId,null);
                stageinstId=newStageinst.getStageinstId();
            }if(!stageId.equals(stageinst.getStageId())){//说明阶段变了
                aeaHiParStageinstService.deleteAeaHiParStageinstById(stageinstId);
                AeaHiParStageinst newStageinst=aeaHiParStageinstService.createAeaHiParStageinst(applyinstId,stageId,themeVerId,appinstId,null);
                stageinstId=newStageinst.getStageinstId();
            }else{
                appinstId=stageinst.getAppinstId();
            }
            //变更,1.删除该阶段的所有情形，重新实例化 2.删除该阶段下的所有事项，重新实例化  3.删除所有事项的情形重新实例化
            deleteReInsertParStateinstUnderStageinst(applyinstId,stageinstId, stateIds);
            deleteReInsertIteminstUnderStageinst(themeVerId,stageinstId,itemVerIds,appinstId,null);
            // 多事项直接合并办理 handWay=0 时才处理
            if (aeaParStage!= null && "0".equals(aeaParStage.getHandWay())) {
                deleteItemStates(applyinstId);
                // 简单合并申报的情况下，可能存在事项自己的情形列表
                saveItemStateBySimpleMerge(parallelItemStateVoList, itemVerIds, applyinstId,stageinstId);
            }
        }else{//第一次暂存阶段信息
            //2、实例化并联实例
            AeaHiParStageinst aeaHiParStageinst = aeaHiParStageinstService.createAeaHiParStageinst(applyinstId, stageId, themeVerId, appinstId, null);
            stageinstId=aeaHiParStageinst.getStageinstId();
            //3、实例化事项----此处已经做了事项实例表中的分局承办字段，
            aeaHiIteminstService.batchInsertAeaHiIteminstAndTriggerAeaLogItemStateHist(themeVerId,stageinstId,itemVerIds,null,null,appinstId);
            //4、情形实例
            aeaHiParStateinstService.batchInsertAeaHiParStateinst(applyinstId, stageinstId, stateIds, SecurityContext.getCurrentUserName());
            // 多事项直接合并办理 handWay=0 时才处理
            if (aeaParStage!= null && "0".equals(aeaParStage.getHandWay())) {
                // 简单合并申报的情况下，可能存在事项自己的情形列表
                saveItemStateBySimpleMerge(parallelItemStateVoList, itemVerIds, applyinstId,stageinstId);
            }
        }

        map.put("stageinstId",stageinstId);
        return map;
    }

    @Override
    public void deleteItemStates(String applyinstId) throws Exception {
        if(StringUtils .isNotBlank(applyinstId)){
            List<AeaItemState> itemStates = aeaHiItemStateinstService.listAeaItemStateByApplyinstIdOrSeriesinstId(applyinstId, null);
            if(itemStates.size()>0) {
                String[] itemStateIds=itemStates.stream().map(AeaItemState::getItemStateId).toArray(String[]::new);
                aeaHiItemStateinstService.batchDeleteAeaItemState(itemStateIds);
            }
        }
    }

    @Override
    public void insertSeriesIteminst(String seriesApplyinstId ,String itemVerId) throws Exception {
        String appinstId=UUID.randomUUID().toString();
        //1、保存单项实例
        AeaHiSeriesinst aeaHiSeriesinst = aeaHiSeriesinstService.createAeaHiSeriesinst(seriesApplyinstId, appinstId,"0",null);
        //2、事项实例
        aeaHiIteminstService.insertAeaHiIteminstAndTriggerAeaLogItemStateHist(aeaHiSeriesinst.getSeriesinstId(), itemVerId, null,null,appinstId);
    }

    @Override
    public   List<AeaHiIteminst> deleteReInsertIteminstUnderStageinst(String themeVerId, String stageinstId, List<String> itemVerIds, String appinstId, String branchOrgMap) throws Exception {
        List<AeaHiIteminst> iteminstList = aeaHiIteminstService.getAeaHiIteminstListByStageinstId(stageinstId);
        if(iteminstList.size()>0){
            String[] iteminstIds = iteminstList.stream().map(AeaHiIteminst::getIteminstId).toArray(String[]::new);
            aeaHiIteminstService.batchDeleteAeaHiIteminst(iteminstIds);
        }
        return aeaHiIteminstService.batchInsertAeaHiIteminstAndTriggerAeaLogItemStateHist(themeVerId,stageinstId,itemVerIds,branchOrgMap,null,appinstId);
    }

    @Override
    public void deleteReInsertParStateinstUnderStageinst(String applyinstId, String stageinstId, String[] stateIds) throws Exception {
        List<AeaHiParStateinst> stateList = aeaHiParStateinstMapper.listAeaHiParStateinstByApplyinstIdOrStageinstId(applyinstId, stageinstId);
        if(stateList.size()>0){
            String[] stateinstIds = stateList.stream().map(AeaHiParStateinst::getStageStateinstId).toArray(String[]::new);
            aeaHiParStateinstMapper.batchDeleteAeaHiParStateinst(stateinstIds);
        }
        aeaHiParStateinstService.batchInsertAeaHiParStateinst(applyinstId, stageinstId, stateIds, SecurityContext.getCurrentUserName());
    }

    /**
     * 简单合并时即多事项直接合并办理，判断并联事项下是否有选择情形，保存对应的情形实例
     *
     * @param itemVerIds       并联申报事项
     * @param applyinstId      并联申报实例id
     * @param stageinstId      阶段实例id
     */
    @Override
    public void saveItemStateBySimpleMerge(List<ParallelItemStateVo> parallelItemStateVoList, List<String> itemVerIds, String applyinstId, String stageinstId) {
        if (parallelItemStateVoList != null && parallelItemStateVoList.size() > 0) {
            Map<String, List<String>> parallelItemStateIdMap = new HashMap<>();
            parallelItemStateVoList.forEach(p -> parallelItemStateIdMap.put(p.getItemVerId(), p.getStateIds()));
            itemVerIds.forEach(itemVerId -> {
                if (CollectionUtils.isNotEmpty(parallelItemStateIdMap.get(itemVerId))) {
                    String[] itemStateIds = parallelItemStateIdMap.get(itemVerId).toArray(new String[0]);
                    try {
                        aeaHiItemStateinstService.batchInsertAeaHiItemStateinst(applyinstId, null, stageinstId, itemStateIds, SecurityContext.getCurrentUserName());
                    } catch (Exception e) {
                        throw new RuntimeException("简单合并申报保存事项的情形实例时失败", e);
                    }
                }
            });
        }
    }
}
