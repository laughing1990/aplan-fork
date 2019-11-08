package com.augurit.aplanmis.mall.userCenter.service.impl;

import com.augurit.agcloud.bsc.mapper.BscDicRegionMapper;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.mapper.OpuOmOrgMapper;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.item.AeaItemBasicService;
import com.augurit.aplanmis.common.service.item.AeaItemPrivService;
import com.augurit.aplanmis.common.service.mat.AeaItemMatService;
import com.augurit.aplanmis.common.service.state.AeaItemStateService;
import com.augurit.aplanmis.common.service.state.AeaParStateService;
import com.augurit.aplanmis.common.service.theme.AeaParThemeService;
import com.augurit.aplanmis.common.utils.CommonTools;
import com.augurit.aplanmis.mall.main.vo.ItemListVo;
import com.augurit.aplanmis.mall.main.vo.ParallelApproveItemVo;
import com.augurit.aplanmis.mall.userCenter.service.RestParallerApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
    private BscDicRegionMapper bscDicRegionMapper;

    @Override
    public ItemListVo listItemAndStateByStageId(String stageId, String projInfoId, String regionalism, String projectAddress) throws Exception {
        ItemListVo vo = new ItemListVo();
        AeaParStage aeaParStage = aeaParStageMapper.getAeaParStageById(stageId);

        //情形
        List<AeaParState> stateList = null;
        //并联
        List<ParallelApproveItemVo> paraItemList = this.getRequiredItems(stageId, projInfoId, regionalism, projectAddress);
        //并行
        List<ParallelApproveItemVo> coreItemList = this.getOptionalItems(stageId, projInfoId, regionalism, projectAddress);
        for (ParallelApproveItemVo item:coreItemList){
            if ("1".equals(item.getIsCatalog())) {//标准事项
                List<AeaItemBasic> carryOutItems = item.getCarryOutItems();//实施事项列表
                AeaItemBasic currentCarryOutItem = item.getCurrentCarryOutItem();//默认实施事项
                if (carryOutItems.size() > 0) {
                    for (AeaItemBasic basic : carryOutItems) {
                        List<AeaItemState> coreStateList = aeaItemStateService.listAeaItemStateByParentId(basic.getItemVerId(), "", "ROOT", SecurityContext.getCurrentOrgId());
                        basic.setCoreStateList(coreStateList.size() > 0 ? coreStateList : new ArrayList<>());
                        if (basic.getItemVerId().equals(currentCarryOutItem.getItemVerId())) {
                            currentCarryOutItem.setCoreStateList(coreStateList);
                        }
                    }
                }
            } else {
                item.setCoreStateList(aeaItemStateService.listAeaItemStateByParentId(item.getItemVerId(), "", "ROOT", SecurityContext.getCurrentOrgId()));
            }
        }
        //getIsNeedState 为1时为分情形
        if ("1".equals(aeaParStage.getIsNeedState())){
            stateList= aeaParStateMapper.listParStateByParentStateId(stageId, "ROOT", SecurityContext.getCurrentOrgId());
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
                            List<AeaItemState> paraStateList = aeaItemStateService.listAeaItemStateByParentId(basic.getItemVerId(), "", "ROOT", SecurityContext.getCurrentOrgId());
                            basic.setCoreStateList(paraStateList.size() > 0 ? paraStateList : new ArrayList<>());
                            if (basic.getItemVerId().equals(currentCarryOutItem.getItemVerId())) {
                                currentCarryOutItem.setParaStateList(paraStateList);
                            }
                        }
                    }
                } else {
                    item.setParaStateList(aeaItemStateService.listAeaItemStateByParentId(item.getItemVerId(), "", "ROOT", SecurityContext.getCurrentOrgId()));
                }

            }
        }

        vo.setParallelItemList(paraItemList==null?new ArrayList<>():paraItemList);
        vo.setCoreItemList(coreItemList==null?new ArrayList<>():coreItemList);
        vo.setStateList(stateList==null?new ArrayList<>():stateList);
        return vo;
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
            aeaParFactor.setIsActive("1");
            aeaParFactor.setAnswerFactors(aeaParFactorMapper.listAeaParFactor(factor));
        });
        return aeaParFactors;
    }

    @Override
    public List<AeaParFactor> listChildFactorByParentFactorId(String parentFactorId) throws Exception {
        AeaParFactor factor = new AeaParFactor();
        factor.setParentFactorId(parentFactorId);
        factor.setIsActive("1");
        List<AeaParFactor>  aeaParFactors = aeaParFactorMapper.listAeaParFactor(factor);
        aeaParFactors.stream().forEach(aeaParFactor -> {
            AeaParFactor parFactor = new AeaParFactor();
            parFactor.setParentFactorId(aeaParFactor.getFactorId());
            parFactor.setIsActive("1");
            aeaParFactor.setAnswerFactors(aeaParFactorMapper.listAeaParFactor(parFactor));
        });
        return aeaParFactors;
    }

    @Override
    public Map<String,Object> getThemeByFactorIds(String[] factorIds) {
        if (factorIds==null||factorIds.length==0) return  null;
        Map<String,Object> resultMap = new HashMap<String,Object>();
        Set<Map<String,Object>> set = new HashSet<Map<String,Object>>();
        for (String factorId:factorIds){
            AeaParFactorTheme aeaParFactorTheme = new AeaParFactorTheme();
            aeaParFactorTheme.setFactorId(factorId);
            List<AeaParFactorTheme> aeaParFactorThemes = aeaParFactorThemeMapper.listAeaParFactorTheme(aeaParFactorTheme);
            String themeId=aeaParFactorThemes.size()>0?aeaParFactorThemes.get(0).getThemeId():"";
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
    public List<ParallelApproveItemVo> getRequiredItems(String stageId, String projInfoId, String regionalism, String projectAddress) throws Exception {
        List<AeaItemBasic> coreItems = aeaItemBasicService.getAeaItemBasicListByStageId(stageId, "0", projInfoId,SecurityContext.getCurrentOrgId());
        if (coreItems.size() == 0) return new ArrayList<>();

        return postHandle(coreItems, stageId, "0", regionalism, projectAddress);
    }

    /**
     * 查询阶段可选事项
     *
     * @param stageId    阶段id
     * @param projInfoId 项目id
     */
    public List<ParallelApproveItemVo> getOptionalItems(String stageId, String projInfoId, String regionalism, String projectAddress) throws Exception {
        List<AeaItemBasic> optionItems = aeaItemBasicService.getAeaItemBasicListByStageId(stageId, "1", projInfoId,SecurityContext.getCurrentOrgId());
        if (optionItems.size() == 0) return new ArrayList<>();
        return postHandle(optionItems, stageId, "1", regionalism, projectAddress);
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
    private List<ParallelApproveItemVo> postHandle(List<AeaItemBasic> aeaItemBasics, String stageId, String isOptionItem, String regionalism, String projectAddress) throws Exception {
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
        if ("1".equals(aeaParStage.getIsNeedState())) {
            // 情形事项
            Set<String> stateItemVerIds = aeaItemBasicService.getAeaItemBasicListByStageIdAndStateId(stageId, null, isOptionItem,SecurityContext.getCurrentOrgId())
                    .stream().map(AeaItemBasic::getItemVerId).collect(Collectors.toSet());
            // 过滤情形事项
            aeaItemBasics.forEach(item -> {
                if (!stateItemVerIds.contains(item.getItemVerId())) {
                    resultItems.add(item);
                }
            });
        } else {
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

                List<AeaItemBasic> sssxList = aeaItemBasicService.getSssxByItemIdAndRegionalism(vo.getItemId(), regionalism, arrRegionIdList.size() == 0 ? null : CommonTools.ListToArr(arrRegionIdList), SecurityContext.getCurrentOrgId());
                vo.setCarryOutItems(sssxList);
                AeaItemBasic sssx=sssxList.size()>0?sssxList.get(0):null;
                if(sssx!=null){
                    vo.setCurrentCarryOutItem(sssx);
                    vo.setBaseItemVerId(vo.getItemVerId());
                    vo.setBaseItemName(vo.getItemName());
                    //BeanUtils.copyProperties(sssx,vo);
                    //vo.setIsCatalog(flag);
                }
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
}
