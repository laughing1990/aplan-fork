package com.augurit.aplanmis.mall.userCenter.service.impl;

import com.augurit.agcloud.bsc.mapper.BscDicRegionMapper;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.mapper.OpuOmOrgMapper;
import com.augurit.aplanmis.common.apply.item.ComputedItem;
import com.augurit.aplanmis.common.apply.item.GuideComputedItem;
import com.augurit.aplanmis.common.constants.AeaHiApplyinstConstants;
import com.augurit.aplanmis.common.constants.ApplyState;
import com.augurit.aplanmis.common.constants.GuideApplyState;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.apply.AeaHiGuideDetailService;
import com.augurit.aplanmis.common.service.apply.AeaHiGuideService;
import com.augurit.aplanmis.common.service.area.RegionService;
import com.augurit.aplanmis.common.service.instance.*;
import com.augurit.aplanmis.common.service.item.AeaItemBasicService;
import com.augurit.aplanmis.common.service.item.AeaItemPrivService;
import com.augurit.aplanmis.common.service.mat.AeaItemMatService;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.service.state.AeaItemStateService;
import com.augurit.aplanmis.common.service.state.AeaParStateService;
import com.augurit.aplanmis.common.service.theme.AeaParThemeService;
import com.augurit.aplanmis.common.utils.CommonTools;
import com.augurit.aplanmis.common.vo.guide.GuideDetailVo;
import com.augurit.aplanmis.mall.main.vo.ItemListVo;
import com.augurit.aplanmis.mall.main.vo.ParallelApproveItemVo;
import com.augurit.aplanmis.mall.userCenter.service.AeaParStageService;
import com.augurit.aplanmis.mall.userCenter.service.RestAeaHiGuideService;
import com.augurit.aplanmis.mall.userCenter.service.RestParallerApplyService;
import com.augurit.aplanmis.mall.userCenter.vo.AeaGuideApplyVo;
import com.augurit.aplanmis.mall.userCenter.vo.AeaGuideItemVo;
import com.augurit.aplanmis.mall.userCenter.vo.ApplyIteminstConfirmVo;
import com.augurit.aplanmis.mall.userCenter.vo.StageStateParamVo;
import com.augurit.aplanmis.supermarket.linkmanInfo.service.AeaLinkmanInfoService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
@Transactional
@Service
public class RestParallerApplyServiceImpl implements RestParallerApplyService {

    @Autowired
    private AeaItemBasicService aeaItemBasicService;
    @Autowired
    private AeaParStageMapper aeaParStageMapper;
    @Autowired
    private AeaItemPrivService aeaItemPrivService;
    @Autowired
    private OpuOmOrgMapper opuOmOrgMapper;
    @Autowired
    AeaParStateService aeaParStateService;
    @Autowired
    AeaItemStateService aeaItemStateService;
    @Autowired
    AeaItemMatService aeaItemMatService;
    @Autowired
    private AeaParStateMapper aeaParStateMapper;
    @Autowired
    private AeaParNavMapper aeaParNavMapper;
    @Autowired
    private AeaParFactorMapper aeaParFactorMapper;
    @Autowired
    private AeaParFactorThemeMapper aeaParFactorThemeMapper;
    @Autowired
    private AeaParThemeService aeaParThemeService;
    @Autowired
    private AeaHiItemInoutService aeaHiItemInoutService;
    @Autowired
    private AeaProjInfoService aeaProjInfoService;
    @Autowired
    private AeaHiGuideService aeaHiGuideService;
    @Autowired
    private AeaHiGuideDetailService aeaHiGuideDetailService;
    @Autowired
    private AeaHiApplyinstService aeaHiApplyinstService;
    @Autowired
    private AeaHiParStageinstService aeaHiParStageinstService;
    @Autowired
    private AeaHiParStateinstService aeaHiParStateinstService;
    @Autowired
    private AeaParStageService aeaParStageService;
    @Autowired
    private RestAeaHiGuideService restAeaHiGuideService;
    @Autowired
    private AeaUnitInfoMapper aeaUnitInfoMapper;
    @Autowired
    private AeaLinkmanInfoService aeaLinkmanInfoService;
    @Autowired
    private AeaParThemeMapper aeaParThemeMapper;
    @Autowired
    private AeaHiIteminstService aeaHiIteminstService;
    @Autowired
    private AeaApplyinstForminstMapper aeaApplyinstForminstMapper;
    @Autowired
    private RegionService regionService;

    @Override
    public ItemListVo listItemAndStateByStageId(String stageId, String projInfoId, String regionalism, String projectAddress,String isSelectItemState,String isFilterStateItem,String rootOrgId) throws Exception {
        if(StringUtils.isBlank(rootOrgId)) rootOrgId=SecurityContext.getCurrentOrgId();
        ItemListVo vo = new ItemListVo();
        AeaParStage aeaParStage = aeaParStageMapper.getAeaParStageById(stageId);

        //情形
        List<AeaParState> stateList = null;
        //并联
        List<ParallelApproveItemVo> paraItemList = this.getRequiredItems(stageId, projInfoId, regionalism, projectAddress,isFilterStateItem,rootOrgId);
        //并行
        List<ParallelApproveItemVo> coreItemList = this.getOptionalItems(stageId, projInfoId, regionalism, projectAddress,isFilterStateItem,rootOrgId);
        if("1".equals(isSelectItemState)){
            for (ParallelApproveItemVo item:coreItemList){
                if ("1".equals(item.getIsCatalog())) {//标准事项
                    List<AeaItemBasic> carryOutItems = item.getCarryOutItems();//实施事项列表
                    AeaItemBasic currentCarryOutItem = item.getCurrentCarryOutItem();//默认实施事项
                    if (carryOutItems.size() > 0) {
                        for (AeaItemBasic basic : carryOutItems) {
                            List<AeaItemState> coreStateList = aeaItemStateService.listAeaItemStateByParentId(basic.getItemVerId(), "", "ROOT", rootOrgId);
                            basic.setCoreStateList(coreStateList.size() > 0 ? coreStateList : new ArrayList<>());
                            if (basic.getItemVerId().equals(currentCarryOutItem.getItemVerId())) {
                                currentCarryOutItem.setCoreStateList(coreStateList);
                            }
                        }
                    }
                } else {
                    item.setCoreStateList(aeaItemStateService.listAeaItemStateByParentId(item.getItemVerId(), "", "ROOT", rootOrgId));
                }
                if (item.getCarryOutItems()==null||item.getCarryOutItems().size()==0){
                    item.setCarryOutItems(new ArrayList<>());
                }
                if (item.getCurrentCarryOutItem()==null) item.setCurrentCarryOutItem(new AeaItemBasic());
            }
            //getIsNeedState 为1时为分情形
            if ("1".equals(aeaParStage.getIsNeedState())){
                stateList= aeaParStateMapper.listParStateByParentStateId(stageId, "ROOT", rootOrgId);
                //stateList = aeaParStateService.listRootAeaParStateByStageId(stageId, SecurityContext.getCurrentOrgId());
            }
            //hand_way为0时，需展示并联事项情形
            if ("0".equals(aeaParStage.getHandWay())){
                for (ParallelApproveItemVo item:paraItemList){
                    if ("1".equals(item.getIsCatalog())) {//标准事项
                        List<AeaItemBasic> carryOutItems = item.getCarryOutItems();//实施事项列表
                        AeaItemBasic currentCarryOutItem = item.getCurrentCarryOutItem();//默认实施事项
                        if (carryOutItems.size() > 0) {
                            for (AeaItemBasic basic : carryOutItems) {
                                List<AeaItemState> paraStateList = aeaItemStateService.listAeaItemStateByParentId(basic.getItemVerId(), "", "ROOT", rootOrgId);
                                basic.setCoreStateList(paraStateList.size() > 0 ? paraStateList : new ArrayList<>());
                                if (basic.getItemVerId().equals(currentCarryOutItem.getItemVerId())) {
                                    currentCarryOutItem.setParaStateList(paraStateList);
                                }
                            }
                        }
                    } else {
                        item.setParaStateList(aeaItemStateService.listAeaItemStateByParentId(item.getItemVerId(), "", "ROOT", rootOrgId));
                    }
                    if (item.getCarryOutItems()==null||item.getCarryOutItems().size()==0){
                        item.setCarryOutItems(new ArrayList<>());
                    }
                    if (item.getCurrentCarryOutItem()==null) item.setCurrentCarryOutItem(new AeaItemBasic());
                }
            }
        }

        vo.setParallelItemList(paraItemList==null?new ArrayList<>():paraItemList);
        vo.setCoreItemList(coreItemList==null?new ArrayList<>():coreItemList);
        vo.setStateList(stateList==null?new ArrayList<>():stateList);
        return vo;
    }

    @Override
    public ApplyIteminstConfirmVo listGuideItemsByApplyinstId(String guideId,String applyinstId,String projInfoId, String isSelectItemState,String isQueryIteminstState) throws Exception {
        String rootOrgId=SecurityContext.getCurrentOrgId();
        GuideDetailVo detail = aeaHiGuideService.detail(guideId);
        AeaProjInfo projInfo = aeaProjInfoService.getAeaProjInfoByProjInfoId(projInfoId);
        ApplyIteminstConfirmVo vo=ApplyIteminstConfirmVo.formatGuide(detail);
        vo.setProjInfoId(projInfo!=null?projInfo.getProjInfoId():"");
        vo.setGcbm(projInfo!=null?projInfo.getGcbm():"");
        vo.setProjName(projInfo!=null?projInfo.getProjName():"");
        List<AeaHiGuideDetail> details = aeaHiGuideDetailService.queryGuideDetailByGuideIdAndDetailType(guideId, "s");
        vo.setItThemeName(details.size()>0?details.get(0).getThemeName():"");
        vo.setIsItSel(details.size()>0?"1":"0");
        List<GuideComputedItem> parallelItems = detail.getParallelItems();
        List<GuideComputedItem> optionItems = detail.getOptionItems();
        if("1".equals(isSelectItemState) && parallelItems.size()>0)
            parallelItems.stream().forEach(v->{
                try {
                    setMatsAndStatesForCarryItems(rootOrgId, v);
                } catch (Exception e) {}
            });
        if("1".equals(isSelectItemState) && optionItems.size()>0)
            optionItems.stream().forEach(v->{
                try {
                    setMatsAndStatesForCarryItems(rootOrgId, v);
                } catch (Exception e) {}
            });
        setIteminstState(parallelItems,optionItems,isQueryIteminstState,applyinstId);
        vo.setCoreIteminstList(optionItems);
        vo.setParallelIteminstList(parallelItems);
        //实例的阶段情形ID集合
        List<AeaParState> aeaParStates =  aeaParStateService.listAeaParStateByStageinstIdORApplyinstId(applyinstId,"");
        if (aeaParStates.size()>0){
            vo.setStateIds(aeaParStates.stream().map(AeaParState::getStageId).distinct().collect(Collectors.toList()));
        }else {
            vo.setStateIds(new ArrayList<>());
        }
        AeaHiApplyinst applyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);
        vo.setLinkmanInfoId(applyinst.getLinkmanInfoId());
        vo.setApplyinstId(applyinstId);
        vo.setApplySubject(applyinst.getApplySubject());
        List<AeaUnitInfo> unitList = aeaUnitInfoMapper.getApplyJSUnitByProjInfoIdAndApplyinstId(projInfoId, applyinstId);
        String unitInfoId= "";
        if(unitList.size()>0){
            for (AeaUnitInfo v:unitList){
                if(StringUtils.isNotBlank(v.getLinkmanInfoId())&&v.getLinkmanInfoId().equals(applyinst.getLinkmanInfoId())){
                    unitInfoId=v.getUnitInfoId();
                    break;
                }
            }
        }
        vo.setUnitInfoId((StringUtils.isNotBlank(unitInfoId))?unitInfoId:(unitList.size()>0?unitList.get(0).getUnitInfoId():null));
        vo.setSmsInfoVo(ApplyIteminstConfirmVo.formatLinkmanInfo(aeaLinkmanInfoService.getOneById(applyinst.getLinkmanInfoId()),applyinstId));
        AeaParStage stage = aeaParStageMapper.getAeaParStageById(vo.getStageId());
        vo.setDybzspjdxh(stage!=null?stage.getDybzspjdxh():"");
        vo.setUseOneForm(stage!=null?stage.getUseOneForm(): Status.OFF);
        if("1".equals(vo.getUseOneForm())){
            List<AeaApplyinstForminst> forminstList = aeaApplyinstForminstMapper.listAeaApplyinstForminstByApplyinstId(vo.getApplyinstId());
            vo.setForminsts(forminstList);
        }
        vo.setGuideId(guideId);
        return vo;
    }

    private void setIteminstState(List<GuideComputedItem> parallelItems, List<GuideComputedItem> optionItems, String isQueryIteminstState,String applyinstId) throws Exception {
        if("1".equals(isQueryIteminstState)){
            List<AeaHiIteminst> iteminstList = aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(applyinstId);
            if(iteminstList.size()==0) return;
            Map<String,String> map=new HashMap<>();
            iteminstList.stream().forEach(v->{
                map.put(v.getItemVerId(),v.getIteminstState());
            });
            if(parallelItems.size()>0) parallelItems.stream().forEach(v-> {
               v.setIteminstState(map.get(v.getItemVerId()));
            });
            List<AeaHiApplyinst> seriesApplyinsts = aeaHiApplyinstService.getSeriesAeaHiApplyinstListByParentApplyinstId(applyinstId,null);
            if(seriesApplyinsts.size()==0) return;
            List<String> seriesApplyinstIds=seriesApplyinsts.stream().map(AeaHiApplyinst::getApplyinstId).collect(Collectors.toList());
            List<AeaHiIteminst> seriesIteminstList = aeaHiIteminstService.getAeaHiIteminstListByApplyinstIds(seriesApplyinstIds, "1", "0");
            if(seriesIteminstList.size()==0) return;
            Map<String,String> seriesMap=new HashMap<>();
            seriesIteminstList.stream().forEach(v->{
                seriesMap.put(v.getItemVerId(),v.getIteminstState());
            });
            if(optionItems.size()>0) optionItems.stream().forEach(v-> {
                v.setIteminstState(seriesMap.get(v.getItemVerId()));
            });
        }
    }

    private void setMatsAndStatesForCarryItems(String rootOrgId, GuideComputedItem v) throws Exception {
        if ("1".equals(v.getIsCatalog())) {//标准事项
            v.setBaseItemVerId(v.getItemVerId());
            List<ComputedItem.CarryOutItem> carryOutItems = v.getCarryOutItems();
            ComputedItem.CarryOutItem currentCarryOutItem = v.getCurrentCarryOutItem();
            if (carryOutItems.size() > 0) {
                for (ComputedItem.CarryOutItem basic : carryOutItems) {
                    List<AeaItemState> paraStateList = aeaItemStateService.listAeaItemStateByParentId(basic.getItemVerId(), "", "ROOT", rootOrgId);
                    basic.setItemStateList(paraStateList.size() > 0 ? paraStateList : new ArrayList<>());
                    List<AeaItemInout> resultMats = aeaHiItemInoutService.getAeaItemInoutMatCertByItemVerId(basic.getItemVerId(), rootOrgId);
                    basic.setResultMats(resultMats);
                    if (basic.getItemVerId().equals(currentCarryOutItem.getItemVerId())) {
                        currentCarryOutItem.setItemStateList(paraStateList);
                    }
                }
            }
        } else {
            List<AeaItemInout> resultMats = aeaHiItemInoutService.getAeaItemInoutMatCertByItemVerId(v.getItemVerId(), rootOrgId);
            v.setItemStateList(aeaItemStateService.listAeaItemStateByParentId(v.getItemVerId(), "", "ROOT", rootOrgId));
            v.setResultMats(resultMats);
        }
    }

    @Override
    public String initGuideApply(AeaGuideApplyVo aeaGuideApplyVo) throws Exception {
        String applyinstId=aeaGuideApplyVo.getApplyinstId();
        String[] stateIds=aeaGuideApplyVo.getStateIds();
        List<String> unitProjIds=aeaGuideApplyVo.getUnitProjIds();
        String stageinstId="";
        if(StringUtils.isBlank(aeaGuideApplyVo.getApplyinstId())){
            AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstService.createAeaHiApplyinst(aeaGuideApplyVo.getApplySource(), aeaGuideApplyVo.getApplySubject(), aeaGuideApplyVo.getLinkmanInfoId(), AeaHiApplyinstConstants.STAGEINST_APPLY, null, ApplyState.RECEIVE_UNAPPROVAL_APPLY.getValue(),"1",null);
            applyinstId=aeaHiApplyinst.getApplyinstId();
            String appinstId = UUID.randomUUID().toString();//预先生成流程模板实例ID
            //2、实例化并联实例
            AeaHiParStageinst aeaHiParStageinst = aeaHiParStageinstService.createAeaHiParStageinst(applyinstId, aeaGuideApplyVo.getStageId(), aeaGuideApplyVo.getThemeVerId(), appinstId, null);
            stageinstId=aeaHiParStageinst.getStageinstId();
        }
        //4、情形实例
        if(stateIds.length>0)
            aeaHiParStateinstService.batchInsertAeaHiParStateinst(applyinstId, stageinstId, stateIds, SecurityContext.getCurrentUserName());
        //7.1、单位本身的申报主体
        aeaParStageService.insertApplySubject(aeaGuideApplyVo.getApplySubject(), applyinstId, new String[]{aeaGuideApplyVo.getProjInfoId()}, aeaGuideApplyVo.getUnitInfoId(), aeaGuideApplyVo.getLinkmanInfoId());
        //7.2、新增单位的申报主体
        if (unitProjIds!=null&&unitProjIds.size()>0){
            aeaParStageService.insertApplySubject(aeaGuideApplyVo.getApplySubject(), applyinstId,  new String[]{aeaGuideApplyVo.getProjInfoId()}, aeaGuideApplyVo.getLinkmanInfoId(), aeaGuideApplyVo.getLinkmanInfoId(), unitProjIds);
        }
        aeaGuideApplyVo.setApplyinstId(applyinstId);
        restAeaHiGuideService.initAeaHiGuide(aeaGuideApplyVo);
        return applyinstId;
    }

    @Override
    public List<AeaParFactor> listFactorByRootOrgId(String rootOrgId) throws Exception {
        AeaParNav aeaParNav = new AeaParNav();
        aeaParNav.setRootOrgId(rootOrgId);
        aeaParNav.setIsActive("1");
        List<AeaParNav> factors = aeaParNavMapper.listAeaParNav(aeaParNav);
        if (factors==null||factors.size()==0) return new ArrayList<>();
        List<String> navIds = factors.stream().map(AeaParNav::getNavId).collect(Collectors.toList());
        List<AeaParFactor>  aeaParFactors= new ArrayList<>();
        navIds.stream().forEach(navId ->{
            AeaParFactor aeaParFactor = new AeaParFactor();
            aeaParFactor.setNavId(navId);
            aeaParFactor.setIsQuestion("1");
            aeaParFactor.setParentFactorId("ROOT");
            aeaParFactor.setIsActive("1");
            aeaParFactors.addAll(aeaParFactorMapper.listAeaParFactor(aeaParFactor));
        });
        aeaParFactors.stream().forEach(aeaParFactor -> {
            AeaParFactor factor = new AeaParFactor();
            factor.setParentFactorId(aeaParFactor.getFactorId());
            factor.setIsActive("1");
            List<AeaParFactor> answerfactors  = aeaParFactorMapper.listAeaParFactor(factor);
            answerfactors.stream().forEach(answerfactor->{
                AeaParFactorTheme aeaParFactorTheme = new AeaParFactorTheme();
                aeaParFactorTheme.setFactorId(answerfactor.getFactorId());
                List<AeaParFactorTheme> aeaParFactorThemes = aeaParFactorThemeMapper.listAeaParFactorTheme(aeaParFactorTheme);
                if (aeaParFactorThemes.size()>0) answerfactor.setThemeId(aeaParFactorThemes.get(0).getThemeId());
                if(StringUtils.isBlank(answerfactor.getThemeId())) return;
                AeaParTheme theme = aeaParThemeMapper.selectOneById(answerfactor.getThemeId());
                answerfactor.setThemeName(theme!=null?theme.getThemeName():"");
            });

            aeaParFactor.setAnswerFactors(answerfactors);
        });
        return aeaParFactors;
    }

    @Override
    public List<AeaParFactor> listChildFactorByParentFactorId(String parentFactorId) throws Exception {
        AeaParFactor factor = new AeaParFactor();

        AeaParFactor param = aeaParFactorMapper.getAeaParFactorByFactorId(parentFactorId);
        factor.setParentFactorId(parentFactorId);
        factor.setIsActive("1");

        List<AeaParFactor>  aeaParFactors = aeaParFactorMapper.listAeaParFactor(factor);
        aeaParFactors.stream().forEach(aeaParFactor -> {
            AeaParFactor parFactor = new AeaParFactor();
            parFactor.setParentFactorId(aeaParFactor.getFactorId());
            parFactor.setIsActive("1");
            List<AeaParFactor> answerfactors  = aeaParFactorMapper.listAeaParFactor(parFactor);
            answerfactors.stream().forEach(answerfactor->{
                AeaParFactorTheme aeaParFactorTheme = new AeaParFactorTheme();
                aeaParFactorTheme.setFactorId(answerfactor.getFactorId());
                answerfactor.setParentFactorId(aeaParFactor.getFactorId());
                if (param!=null) answerfactor.setParentQuestionFactorId(param.getParentFactorId());
                List<AeaParFactorTheme> aeaParFactorThemes = aeaParFactorThemeMapper.listAeaParFactorTheme(aeaParFactorTheme);
                if (aeaParFactorThemes.size()>0) answerfactor.setThemeId(aeaParFactorThemes.get(0).getThemeId());
                if(StringUtils.isBlank(answerfactor.getThemeId())) return;
                AeaParTheme theme = aeaParThemeMapper.selectOneById(answerfactor.getThemeId());
                answerfactor.setThemeName(theme!=null?theme.getThemeName():"");
            });
            aeaParFactor.setAnswerFactors(answerfactors);
        });
        return aeaParFactors;
    }

    @Override
    public Map<String,Object> getThemeByFactorIds(String[] factorIds) throws Exception{
        if (factorIds==null||factorIds.length==0) return  null;
        Map<String,Object> resultMap = new HashMap<String,Object>();
        Set<Map<String,Object>> set = new HashSet<Map<String,Object>>();
        for (String factorId:factorIds){
            AeaParFactorTheme aeaParFactorTheme = new AeaParFactorTheme();
            aeaParFactorTheme.setFactorId(factorId);
            List<AeaParFactorTheme> aeaParFactorThemes = aeaParFactorThemeMapper.listAeaParFactorTheme(aeaParFactorTheme);
            if(aeaParFactorThemes.size()>0){
                String themeId=aeaParFactorThemes.get(0).getThemeId();
                AeaParTheme aeaParTheme = aeaParThemeService.getAeaParThemeByThemeId(themeId);
                if (aeaParTheme==null) continue;
                resultMap.put("themeId",themeId);
                if(!StringUtils.isBlank(themeId)){
                    String themeType="";
                    try {
                        themeType=aeaParThemeService.getAeaParThemeByThemeId(themeId).getThemeType();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    resultMap.put("themeType",themeType);
                }else{
                    resultMap.put("themeType","");
                }
                set.add(resultMap);
            }
        }
        if (set.size()!=1) return null;
        Iterator it = set.iterator();
        while(it.hasNext()){
            return (Map<String, Object>) it.next();
        }
        return null;
    }

    @Override
    public List<AeaItemMat> listMatByStateIdAndStageIdAndItemVerId(String stageId, String[] stateIds, String[] itemVerIds) throws Exception {
        List<AeaItemMat> list = new ArrayList<>();
        //阶段下的材料
        List<AeaItemMat> stageItemMat = aeaItemMatService.getMatListByStageId(stageId,null);
        list.addAll(stageItemMat);
        //事项下的材料
        List<AeaItemMat> itemMatList =  aeaItemMatService.getMatListByItemVerIds(itemVerIds, "1", null);
        list.addAll(itemMatList);
        //情形材料
        List<AeaItemMat> stateMatList =  aeaItemMatService.getMatListByStageStateIds(stateIds);
        list.addAll(stateMatList);
        return list.stream().filter(CommonTools.distinctByKey(AeaItemMat::getMatId)).collect(Collectors.toList());

    }

    /**
     * 查询阶段必选事项
     *
     * @param stageId    阶段id
     * @param projInfoId 项目id
     */
    public List<ParallelApproveItemVo> getRequiredItems(String stageId, String projInfoId, String regionalism, String projectAddress,String isFilterStateItem,String rootOrgId) throws Exception {
        List<AeaItemBasic> coreItems = aeaItemBasicService.getAeaItemBasicListByStageId(stageId, "0", projInfoId,rootOrgId);
        if (coreItems.size() == 0) return new ArrayList<>();

        return postHandle(coreItems, stageId, "0", regionalism, projectAddress,isFilterStateItem,rootOrgId);
    }

    /**
     * 查询阶段可选事项
     *
     * @param stageId    阶段id
     * @param projInfoId 项目id
     */
    public List<ParallelApproveItemVo> getOptionalItems(String stageId, String projInfoId, String regionalism, String projectAddress,String isFilterStateItem,String rootOrgId) throws Exception {
        List<AeaItemBasic> optionItems = aeaItemBasicService.getAeaItemBasicListByStageId(stageId, "1", projInfoId,rootOrgId);
        if (optionItems.size() == 0) return new ArrayList<>();
        return postHandle(optionItems, stageId, "1", regionalism, projectAddress,isFilterStateItem,rootOrgId);
    }

    /**
     * 判断是否需要过滤情形事项，在不分情形时，不用过滤
     *
     * @param aeaItemBasics 阶段下的所有事项
     * @param stageId       阶段
     * @param isOptionItem  是并联审批还是并行推进
     * @return 返回事项vo
     * @throws Exception 查询事项异常
     */
    private List<ParallelApproveItemVo> postHandle(List<AeaItemBasic> aeaItemBasics, String stageId, String isOptionItem, String regionalism, String projectAddress,String isFilterStateItem,String rootOrgId) throws Exception {
        AeaParStage aeaParStage = aeaParStageMapper.getAeaParStageById(stageId);

        // 设置事项的审批组织信息
        List<String> itemVerIds = aeaItemBasics.stream().map(AeaItemBasic::getItemVerId).collect(Collectors.toList());
        List<String> orgIds = new ArrayList<>();
        final Map<String, String> orgNameMap = new HashMap<>();
        final Map<String, AeaItemPriv> privMap = new HashMap<>();

        if (itemVerIds.size() > 0) {
            List<AeaItemPriv> currentUserItemPriv = aeaItemPrivService.findCurrentUserItemPriv(itemVerIds.toArray(new String[0]));
            if (currentUserItemPriv != null) {
                currentUserItemPriv.forEach(priv -> {
                    orgIds.add(priv.getOrgId());
                    privMap.put(priv.getItemVerId(), priv);
                });

                if (orgIds.size() > 0) {
                    List<OpuOmOrg> orgNameByOrgIds = opuOmOrgMapper.getOrgNameByOrgIds(orgIds.toArray(new String[0]));
                    orgNameByOrgIds.forEach(org -> orgNameMap.put(org.getOrgId(), org.getOrgName()));
                }
            }
        }

        List<AeaItemBasic> resultItems = new ArrayList<>(aeaItemBasics.size());
        // 分情形时要过滤情形事项
        // 情形事项
        Set<String> stateItemVerIds = aeaItemBasicService.getAeaItemBasicListByStageIdAndStateId(stageId, null, isOptionItem,rootOrgId)
                .stream().map(AeaItemBasic::getItemVerId).collect(Collectors.toSet());
        if ("1".equals(aeaParStage.getIsNeedState()) && "1".equals(isFilterStateItem)) {
            // 过滤情形事项
            aeaItemBasics.forEach(item -> {
                if (!stateItemVerIds.contains(item.getItemVerId())) {
                    item.setIsStateItem("0");
                    resultItems.add(item);
                }else{
                    item.setIsStateItem("1");
                }
            });
        } else {
            aeaItemBasics.forEach(item -> {
                if (!stateItemVerIds.contains(item.getItemVerId())) {
                    item.setIsStateItem("0");
                }else{
                    item.setIsStateItem("1");
                }
            });
            resultItems.addAll(aeaItemBasics);
        }
        return  resultItems.stream().filter(CommonTools.distinctByKey(AeaItemBasic::getItemVerId)).map(ParallelApproveItemVo::build).peek(vo -> {
            String flag=vo.getIsCatalog();
            if("1".equals(flag)){//标准事项
                List<String> arrRegionIdList = new ArrayList<>();
                if ("0".equals(vo.getItemExchangeWay()) && StringUtils.isNotBlank(projectAddress)) {//itemExchangeWay 实施事项换算方式 0 按照审批行政区划和属地行政区划换算 1 仅按照审批行政区划换算
                    String[] arrRegionIds = projectAddress.split(",");
                    for (String id : arrRegionIds) {
                        arrRegionIdList.add(id);
                    }
                }
                arrRegionIdList.addAll(regionService.getSeqBscDicRegionIds(regionalism));
                List<AeaItemBasic> sssxList = aeaItemBasicService.getSssxByItemIdAndRegionalism(vo.getItemId(), regionalism, arrRegionIdList.size() == 0 ? null : CommonTools.ListToArr(arrRegionIdList), rootOrgId);
                if(sssxList.size()>0){
                    sssxList.stream().forEach(ss->{
                        List<AeaItemInout> resultMats=aeaHiItemInoutService.getAeaItemInoutMatCertByItemVerId(vo.getItemVerId(),rootOrgId);
                        ss.setResultMats(resultMats);
                    });
                }
                vo.setCarryOutItems(sssxList);
                AeaItemBasic sssx=sssxList.size()>0?this.getAeaItemBasicByMatchingRules(regionalism,sssxList):null;
                if(sssx!=null){
                    vo.setCurrentCarryOutItem(sssx);
                    vo.setBaseItemVerId(vo.getItemVerId());
                    vo.setBaseItemName(vo.getItemName());
                    //BeanUtils.copyProperties(sssx,vo);
                    //vo.setIsCatalog(flag);
                }
            }else{
                List<AeaItemInout> resultMats=aeaHiItemInoutService.getAeaItemInoutMatCertByItemVerId(vo.getItemVerId(),rootOrgId);
                vo.setResultMats(resultMats.size()>0?resultMats:new ArrayList<>());
            }
            AeaItemPriv aeaItemPriv = privMap.get(vo.getItemVerId());
            if (aeaItemPriv != null) {
                vo.setAllowManual(aeaItemPriv.getAllowManual());
                vo.setOrgId(aeaItemPriv.getOrgId());
                vo.setOrgName(orgNameMap.get(aeaItemPriv.getOrgId()));
            }
        })
                .collect(Collectors.toList());
    }

    private AeaItemBasic getAeaItemBasicByMatchingRules(String regionId,List<AeaItemBasic> sssxList){
        if(sssxList.size()>0){
            for (AeaItemBasic basic:sssxList){
                if(regionId.equals(basic.getRegionId())){
                    return basic;
                }
            }
            return sssxList.get(0);
        }
        return null;
    }


    @Override
    public List<AeaGuideItemVo>  listItemByStageIdAndStateList(StageStateParamVo stageStateParamVo,String isOptionItem,String rootOrgId) throws Exception {
        if(StringUtils.isBlank(rootOrgId)) rootOrgId=SecurityContext.getCurrentOrgId();
        String stageId=stageStateParamVo.getStageId();
        String regionalism=stageStateParamVo.getRegionalism();
        String projectAddress=stageStateParamVo.getProjectAddress();
        List<String> stateIds=stageStateParamVo.getStateIds();
        List<AeaItemBasic> itemList =aeaItemBasicService.getAeaItemBasicListByStageId(stageId,isOptionItem,null,rootOrgId);
        Set<String> stateItemVerIds = aeaItemBasicService.getAeaItemBasicListByStageIdAndStateId(stageId, null, isOptionItem,rootOrgId)
                .stream().map(AeaItemBasic::getItemVerId).collect(Collectors.toSet());
        if(itemList.size()>0) itemList.stream().filter(v->stateItemVerIds.contains(v.getItemVerId())).collect(Collectors.toList());
        if(stateIds==null||stateIds.size()==0){
            List<AeaItemBasic> coreStateItemList = aeaItemBasicService.getAeaItemBasicListByStageId(stageId,  isOptionItem,null, rootOrgId);
            itemList.addAll(coreStateItemList);
        }else{
            List<AeaItemBasic> coreStateItemList = aeaItemBasicService.getAeaItemBasicListByStageIdAndStateIds(stageId, stateIds, isOptionItem, rootOrgId);
            itemList.addAll(coreStateItemList);
        }


        String finalRootOrgId = rootOrgId;
        return itemList.size()>0?itemList.stream().map(AeaGuideItemVo::format).peek(vo->{
            String flag=vo.getIsCatalog();
            if("1".equals(flag)) {//标准事项
                List<String> arrRegionIdList = new ArrayList<>();
                if ("0".equals(vo.getItemExchangeWay()) && StringUtils.isNotBlank(projectAddress)) {//itemExchangeWay 实施事项换算方式 0 按照审批行政区划和属地行政区划换算 1 仅按照审批行政区划换算
                    String[] arrRegionIds = projectAddress.split(",");
                    for (String id : arrRegionIds) {
                        arrRegionIdList.add(id);
                    }
                }
                arrRegionIdList.addAll(regionService.getSeqBscDicRegionIds(regionalism));
                vo.setBaseItemVerId(vo.getItemVerId());
                List<AeaItemBasic> sssxList = aeaItemBasicService.getSssxByItemIdAndRegionalism(vo.getItemId(), regionalism, arrRegionIdList.size() == 0 ? null : CommonTools.ListToArr(arrRegionIdList), finalRootOrgId);
                if(sssxList.size()>0){
                    vo.setItemVerId(this.getAeaItemBasicByMatchingRules(regionalism,sssxList).getItemVerId());
                }
            }
        }).collect(Collectors.toList()):new ArrayList<>();
    }


    @Override
    public List<AeaHiGuide> searchGuideApplyListByUnitIdAndUserId(String keyword, String applyState, String unitInfoId, String linkmanInfoId, int pageNum, int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        AeaHiGuide query=new AeaHiGuide();
        query.setKeyword(keyword);
        query.setApplyState(applyState);
        query.setApplyUnitInfoId(unitInfoId);
        query.setApplyLinkmanInfoId(linkmanInfoId);
        List<AeaHiGuide> list = aeaHiGuideService.listAeaHiGuideListUnitIdOrLinkmanInfoId(query);
        return list.size()>0?list.stream().filter(v-> !GuideApplyState.FINISHED.getValue().equals(v.getApplyState())).collect(Collectors.toList()):list;
    }


}
