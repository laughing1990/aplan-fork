package com.augurit.aplanmis.rest.userCenter.service.impl;

import com.augurit.agcloud.bpm.common.domain.ActStoTimeruleInst;
import com.augurit.agcloud.bpm.common.service.ActStoTimeruleInstService;
import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.domain.BscDicRegion;
import com.augurit.agcloud.bsc.sc.dic.code.service.BscDicCodeService;
import com.augurit.agcloud.bsc.sc.dic.region.service.BscDicRegionService;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ApplyState;
import com.augurit.aplanmis.common.constants.ApplyType;
import com.augurit.aplanmis.common.constants.DicConstants;
import com.augurit.aplanmis.common.constants.ItemStatus;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaHiItemStateinst;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaHiParStageinst;
import com.augurit.aplanmis.common.domain.AeaHiSmsInfo;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaItemMat;
import com.augurit.aplanmis.common.domain.AeaItemState;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.domain.AeaParTheme;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.dto.ApproveProjInfoDto;
import com.augurit.aplanmis.common.dto.SupplementInfoDto;
import com.augurit.aplanmis.common.mapper.AeaHiItemCorrectMapper;
import com.augurit.aplanmis.common.mapper.AeaHiItemStateinstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiIteminstMapper;
import com.augurit.aplanmis.common.mapper.AeaItemStateMapper;
import com.augurit.aplanmis.common.mapper.AeaParStateItemMapper;
import com.augurit.aplanmis.common.service.file.impl.FileAbstractService;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiItemStateinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiIteminstService;
import com.augurit.aplanmis.common.service.instance.AeaHiParStageinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiParStateinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiSmsInfoService;
import com.augurit.aplanmis.common.service.item.AeaItemBasicService;
import com.augurit.aplanmis.common.service.linkman.AeaLinkmanInfoService;
import com.augurit.aplanmis.common.service.mat.AeaItemMatService;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.service.search.ApproveDataService;
import com.augurit.aplanmis.common.service.stage.AeaParStageService;
import com.augurit.aplanmis.common.service.theme.AeaParThemeService;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.augurit.aplanmis.common.vo.MatCorrectConfirmVo;
import com.augurit.aplanmis.rest.auth.AuthContext;
import com.augurit.aplanmis.rest.auth.AuthUser;
import com.augurit.aplanmis.rest.userCenter.constant.LoginUserRoleEnum;
import com.augurit.aplanmis.rest.userCenter.service.RestApproveService;
import com.augurit.aplanmis.rest.userCenter.vo.AeaItemBasicVo;
import com.augurit.aplanmis.rest.userCenter.vo.AeaParStageVo;
import com.augurit.aplanmis.rest.userCenter.vo.ApplyDetailVo;
import com.augurit.aplanmis.rest.userCenter.vo.LifeCycleDiagramVo;
import com.augurit.aplanmis.rest.userCenter.vo.StatisticsNumVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.security.InvalidParameterException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class RestApproveServiceImpl implements RestApproveService {
    private static final Logger logger = LoggerFactory.getLogger(RestApproveServiceImpl.class);

    @Autowired
    private AeaProjInfoService aeaProjInfoService;
    @Autowired
    private ApproveDataService approveDataService;
    @Autowired
    private AeaHiIteminstService aeaHiIteminstService;
    @Autowired
    private AeaItemMatService aeaItemMatService;
    @Autowired
    private AeaHiParStateinstService aeaHiParStateinstService;
    @Autowired
    private AeaHiItemStateinstService aeaHiItemStateinstService;
    @Autowired
    private AeaHiSmsInfoService aeaHiSmsInfoService;
    @Autowired
    private AeaUnitInfoService aeaUnitInfoService;
    @Autowired
    private AeaLinkmanInfoService aeaLinkmanInfoService;
    @Autowired
    private AeaHiApplyinstService aeaHiApplyinstService;
    @Autowired
    private AeaHiParStageinstService aeaHiParStageinstService;
    @Autowired
    private AeaParStageService aeaParStageService;
    @Autowired
    private AeaParThemeService aeaParThemeService;
    @Autowired
    private BscDicCodeService bscDicCodeService;
    @Autowired
    private FileAbstractService fileAbstractService;
    @Autowired
    private AeaItemBasicService aeaItemBasicService;
    @Autowired
    private AeaHiItemCorrectMapper itemCorrectMapper;
    @Autowired
    AeaParStateItemMapper aeaParStateItemMapper;
    @Autowired
    ActStoTimeruleInstService actStoTimeruleInstService;
    @Autowired
    BscDicRegionService bscDicRegionService;
    @Autowired
    private AeaItemStateMapper aeaItemStateMapper;
    @Autowired
    private AeaHiItemStateinstMapper aeaHiItemStateinstMapper;
    @Autowired
    private AeaHiIteminstMapper aeaHiIteminstMapper;

    @Override
    public PageInfo<ApproveProjInfoDto> searchApproveProjInfoListByUnitOrLinkman(String unitInfoId, String userInfoId, String state, String keyword, int pageNum, int pageSize) throws Exception {
        return approveDataService.searchApproveProjInfoListByUnitOrLinkman(unitInfoId, userInfoId, state, keyword, pageNum, pageSize);
    }

    @Override
    public PageInfo<ApproveProjInfoDto> searchScheduleInquireListByUnitInfoIdOrLinkman(String unitInfoId, String userInfoId, String keyword, int pageNum, int pageSize) {
        return approveDataService.searchScheduleInquireListByUnitInfoIdOrLinkman(unitInfoId, userInfoId, keyword, pageNum, pageSize);
    }

    @Override
    public PageInfo<ApproveProjInfoDto> searchIteminstApproveInfoListByUnitIdAndUserId(String keyword, String unitInfoId, String userId, int pageNum, int pageSize) throws Exception {
        return approveDataService.searchIteminstApproveInfoListByUnitIdAndUserId(keyword, unitInfoId, userId, pageNum, pageSize);
    }

    @Override
    public PageInfo<SupplementInfoDto> searchMatComplet(String unitInfoId, String userId, String keyword, int pageNum, int pageSize) throws Exception {
        return MergeopinionList(approveDataService.searchMatCompletByUser(unitInfoId, userId, keyword, pageNum, pageSize));
    }

    public PageInfo<SupplementInfoDto> searchSupplementInfo(String unitInfoId, String userId, int pageNum, int pageSize) throws Exception {
        return MergeopinionList(approveDataService.searchSupplementInfo(unitInfoId, userId, ItemStatus.CORRECT_MATERIAL_START.getValue(), pageNum, pageSize));
    }

    @Override
    public PageInfo<MatCorrectConfirmVo> searchSupplyInfoList(String unitInfoId, String userId, int pageNum, int pageSize) throws Exception {
        PageHelper.startPage(pageNum, pageSize);
        List<MatCorrectConfirmVo> list = itemCorrectMapper.searchStayMatCorrectListByCurrentUser(unitInfoId, userId, null);
        return new PageInfo<MatCorrectConfirmVo>(list);
    }

    /*
     * 对补正、补全列表，不同意见的相同数据，进行合并
     * */
    public PageInfo<SupplementInfoDto> MergeopinionList(PageInfo<SupplementInfoDto> pageInfo) {
        List<SupplementInfoDto> list = Optional.ofNullable(pageInfo).orElse(new PageInfo<>()).getList();
        if (list == null)
            return new PageInfo<>();
        HashMap<String, SupplementInfoDto> map = new HashMap<>();
        for (SupplementInfoDto entity : list) {
            String key = entity.getIsSeriesApprove() + "," + entity.getApplyinstId() + "," + entity.getProjInfoId() + "," + entity.getIteminstId();//将进入详情页所用的传参作为键值标识，进行意见的合并
            if (map.containsKey(key)) {
                SupplementInfoDto value = map.get(key);
                List<String> opinionList = value.getOpsUserOpinionList() == null ? new ArrayList<>() : value.getOpsUserOpinionList();
                opinionList.add(entity.getOpsUserOpinion());
                value.setOpsUserOpinionList(opinionList);
            } else {
                List<String> opinionList = new ArrayList<>();
                opinionList.add(entity.getOpsUserOpinion());
                entity.setOpsUserOpinionList(opinionList);
                map.put(key, entity);
            }
        }
        list.clear();
        list.addAll(map.values());
        return pageInfo;
    }

    @Override
    public PageInfo<AeaProjInfo> findAeaProjInfoByKeyword(String keyWord, int pageNum, int pageSize) throws Exception {
        PageHelper.startPage(pageNum, pageSize);
        List<AeaProjInfo> list = aeaProjInfoService.findAeaProjInfoByKeyword(keyWord);
        return new PageInfo<AeaProjInfo>(list);
    }

    @Override
    public ApplyDetailVo getApplyDetailByApplyinstIdAndProjInfoId(String applyinstId, String projInfoId, String isSeriesApprove, String iteminstId, HttpServletRequest request) throws Exception {
        ApplyDetailVo applyDetailVo = new ApplyDetailVo();
        String isNeedState = "1";
        applyDetailVo.setIsSeriesApprove(isSeriesApprove);
        //项目基本信息
        AeaProjInfo aeaProjInfo = aeaProjInfoService.getAeaProjInfoByProjInfoId(projInfoId);
        if (aeaProjInfo != null) {
            //建设性质
            BscDicCodeItem projNatrueItem = bscDicCodeService.getItemByTypeCodeAndItemCode(DicConstants.XM_NATURE, aeaProjInfo.getProjNature(), SecurityContext.getCurrentOrgId());
            if (projNatrueItem != null) aeaProjInfo.setProjNature(projNatrueItem.getItemName());
            //项目级别
            BscDicCodeItem projLevelItem = bscDicCodeService.getItemByTypeCodeAndItemCode(DicConstants.XM_PROJECT_LEVEL, aeaProjInfo.getProjLevel(), SecurityContext.getCurrentOrgId());
            if (projLevelItem != null) aeaProjInfo.setProjLevel(projLevelItem.getItemName());
            //项目类别
            BscDicCodeItem projClassItem = bscDicCodeService.getItemByTypeCodeAndItemCode(DicConstants.PROJECT_CLASS, aeaProjInfo.getProjType(), SecurityContext.getCurrentOrgId());
            if (projClassItem != null) aeaProjInfo.setProjType(projClassItem.getItemName());
            //土地来源
            BscDicCodeItem landSourceItem = bscDicCodeService.getItemByTypeCodeAndItemCode(DicConstants.XM_TDLY, aeaProjInfo.getLandSource(), SecurityContext.getCurrentOrgId());
            if (landSourceItem != null) aeaProjInfo.setLandSource(landSourceItem.getItemName());
            //工程分类
            BscDicCodeItem projCategoryItem = bscDicCodeService.getItemByTypeCodeAndItemCode(DicConstants.XM_GCFL, aeaProjInfo.getProjCategory(), SecurityContext.getCurrentOrgId());
            if (projCategoryItem != null) aeaProjInfo.setProjCategory(projCategoryItem.getItemName());
            //投资类型
            BscDicCodeItem investTypeItem = bscDicCodeService.getItemByTypeCodeAndItemCode(DicConstants.XM_TZLX, aeaProjInfo.getInvestType(), SecurityContext.getCurrentOrgId());
            if (investTypeItem != null) aeaProjInfo.setInvestType(investTypeItem.getItemName());
            //资金来源
            BscDicCodeItem financialSourceItem = bscDicCodeService.getItemByTypeCodeAndItemCode(DicConstants.XM_ZJLY, aeaProjInfo.getIsFinancialFund(), SecurityContext.getCurrentOrgId());
            if (financialSourceItem != null) aeaProjInfo.setFinancialSource(financialSourceItem.getItemName());
            AeaParTheme aeaParTheme = aeaParThemeService.getAeaParThemeByThemeId(aeaProjInfo.getThemeId());
            if (aeaParTheme != null) aeaProjInfo.setThemeType(aeaParTheme.getThemeName());
            //行政区划
            BscDicRegion region = bscDicRegionService.getBscDicRegionById(aeaProjInfo.getRegionalism());
            if (region != null) aeaProjInfo.setRegionalism(region.getRegionName());
            applyDetailVo.setAeaProjInfo(aeaProjInfo);
        } else {
            applyDetailVo.setAeaProjInfo(new AeaProjInfo());
        }

        //事项实例列表/主题阶段信息
        List<AeaHiIteminst> aeaHiIteminstList = aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(applyinstId);
        if (aeaHiIteminstList != null && aeaHiIteminstList.size() > 0) {
            if ("1".equals(isSeriesApprove)) {
                applyDetailVo.setIteminstName(aeaHiIteminstList.get(0).getIteminstName());
                applyDetailVo.setApproveOrgName(aeaHiIteminstList.get(0).getApproveOrgName());
            } else {
                AeaHiParStageinst stageinst = aeaHiParStageinstService.getAeaHiParStageinstByApplyinstId(applyinstId);
                if (stageinst != null) {
                    AeaParStage stage = aeaParStageService.getAeaParStageById(stageinst.getStageId());
                    isNeedState = (stage != null && StringUtils.isNotBlank(stage.getIsNeedState())) ? stage.getIsNeedState() : "1";
                    //办理方式 0 多事项直接合并办理  1 按阶段多级情形组织事项办理
                    if (stage != null) {
                        if ("0".equals(stage.getHandWay())) {
                            setItemStateinstList(aeaHiIteminstList, stageinst.getStageinstId());
                        }
                    }
                    AeaParTheme theme = aeaParThemeService.getAeaParThemeByThemeVerId(stageinst.getThemeVerId());
                    applyDetailVo.setThemeStageName((stage == null || theme == null) ? "" : "【" + theme.getThemeName() + "】" + stage.getStageName());
                }
                applyDetailVo.setAeaHiIteminstList(aeaHiIteminstList);
            }
        } else {
            applyDetailVo.setAeaHiIteminstList(new ArrayList<>());
        }

        //情形、前置条件
        List<Map<String, String>> stateList = new ArrayList<>();
        if ("1".equals(isSeriesApprove)) {//单项，取值事项情形实例表
            stateList = aeaHiItemStateinstService.listSelectedAeaItemStateinstBySeriesinstIdOrApplyinstId(applyinstId, "");
        } else {//并联，取值情形实例表
            stateList = aeaHiParStateinstService.listSelectedParStateinstByStageinstIdOrApplyinstId(applyinstId, "");
        }
        applyDetailVo.setStateList(stateList);

        //材料列表
        List<AeaItemMat> matList = aeaItemMatService.getMatListByApplyinstIdContainsMatinst(applyinstId, iteminstId);
        if (matList != null) {
            applyDetailVo.setMatList(matList);
        } else {
            applyDetailVo.setMatList(new ArrayList<>());
        }
        //办件结果取件方式
        AeaHiSmsInfo aeaHiSmsInfo = aeaHiSmsInfoService.getAeaHiSmsInfoByApplyinstId(applyinstId);
        applyDetailVo.setAeaHiSmsInfo(aeaHiSmsInfo == null ? new AeaHiSmsInfo() : aeaHiSmsInfo);

        //申报主体
        AeaHiApplyinst applyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);
        if (applyinst != null) {
            //是否启动情形
//            if("0".equals(applyinst.getIsSeriesApprove())){
//                AeaParStage stage = aeaParStageService.getAeaParStageByApplyinstId(applyinstId);
//                applyDetailVo.setIsNeedState(stage==null?"1":stage.getIsNeedState());
//            }else{
//                applyDetailVo.setIsNeedState("1");
//            }
            applyDetailVo.setIsNeedState(isNeedState);
            //申报主体：1 单位，0 个人
            if ("0".equals(applyinst.getApplySubject())) {
                applyDetailVo.setRole(LoginUserRoleEnum.PERSONAL.getValue());
            } else {
                AeaUnitInfo aeaUnitInfo;
                applyDetailVo.setRole(LoginUserRoleEnum.UNIT.getValue());
                List<AeaUnitInfo> unitInfoList = aeaUnitInfoService.findApplyOwnerUnitProj(applyinstId, projInfoId);//建设单位
                if (unitInfoList.size()>0){
                    aeaUnitInfo = unitInfoList.get(0);
                    String unitInfoId = aeaUnitInfo.getUnitInfoId();
                    aeaUnitInfo.setAeaLinkmanInfoList(aeaLinkmanInfoService.findAllUnitLinkman(unitInfoId));
                }else {
                    List<AeaLinkmanInfo> aeaLinkmanInfoList = new ArrayList<>();
                    aeaUnitInfo = new AeaUnitInfo();
                    aeaUnitInfo.setAeaLinkmanInfoList(aeaLinkmanInfoList);
                }
                applyDetailVo.setAeaUnitInfo(aeaUnitInfo);
            }
            AeaLinkmanInfo aeaLinkmanInfo = aeaLinkmanInfoService.getAeaLinkmanInfoByLinkmanInfoId(applyinst.getLinkmanInfoId());
            applyDetailVo.setAeaLinkmanInfo(aeaLinkmanInfo == null ? new AeaLinkmanInfo() : aeaLinkmanInfo);
            applyDetailVo.setApplyinstCode(applyinst.getApplyinstCode());
        } else {
            applyDetailVo.setRole(LoginUserRoleEnum.UNIT.getValue());
            applyDetailVo.setAeaLinkmanInfo(new AeaLinkmanInfo());
            applyDetailVo.setAeaUnitInfo(new AeaUnitInfo());
        }
        return applyDetailVo;
    }

    private void setItemStateinstList(List<AeaHiIteminst> aeaHiIteminstList, String stageinstId) throws Exception {
        List<Map<String, String>> list = new ArrayList<>();
        for (AeaHiIteminst iteminst : aeaHiIteminstList) {
            AeaHiItemStateinst query = new AeaHiItemStateinst();
            query.setRootOrgId(SecurityContext.getCurrentOrgId());
            query.setStageinstId(stageinstId);
            List<AeaHiItemStateinst> stateinstList = aeaHiItemStateinstMapper.listAeaHiItemStateinst(query);
            String[] parentStateIds = stateinstList.size() > 0 ? stateinstList.stream().map(AeaHiItemStateinst::getParentStateinstId).toArray(String[]::new) : new String[0];
            String[] stateIds = stateinstList.size() > 0 ? stateinstList.stream().map(AeaHiItemStateinst::getExecStateId).toArray(String[]::new) : new String[0];
            if (parentStateIds.length == 0 || stateIds.length == 0) {
                iteminst.setItemStateinsts(list);
                continue;
            }
            List<AeaItemState> parentStateList = aeaItemStateMapper.listAeaItemStateByIds(parentStateIds);
            List<AeaItemState> stateList = aeaItemStateMapper.listAeaItemStateByIds(stateIds);
            for (AeaItemState parentState : parentStateList) {
                for (AeaItemState state : stateList) {
                    if (StringUtils.isNotBlank(state.getParentStateId()) && state.getParentStateId().equals(parentState.getItemStateId())) {
                        Map<String, String> map = new HashMap<>(4);
                        map.put("question", parentState.getStateName());
                        map.put("answer", state.getStateName());
                        map.put("questionId",parentState.getItemStateId());
                        map.put("answerId",state.getItemStateId());
                        list.add(map);
                    }
                }
            }
            iteminst.setItemStateinsts(list);
        }
    }

    @Override
    public StatisticsNumVo getApprovalAndMatCompletionNum() throws Exception {
        StatisticsNumVo retVo = new StatisticsNumVo();
        AuthUser loginInfo = AuthContext.getCurrentUser();
        // 已申报，已撤件，草稿箱，材料补全，材料补正
        long matCompletionNum = 0;//材料补全
        long approvalNum = 0;//已审批
        long draftNum = 0;//草稿箱
        long supplyNum = 0;//材料补正
        long applyNum = 0;//已申报
        long withdrawalNum = 0;//已撤件
        if (loginInfo == null) return retVo;
        if (loginInfo.isPersonalAccount()) {//个人
            matCompletionNum = this.searchMatComplet("", loginInfo.getLinkmanInfoId(), null, 1, 1).getTotal();
            approvalNum = this.searchIteminstApproveInfoListByUnitIdAndUserId("", "", loginInfo.getLinkmanInfoId(), 1, 1).getTotal();
            draftNum = this.searchApproveProjInfoListByUnitOrLinkman("", loginInfo.getLinkmanInfoId(), "2", null, 1, 1).getTotal();
            supplyNum = this.searchSupplyInfoList("", loginInfo.getLinkmanInfoId(), 1, 1).getTotal();
            applyNum = aeaHiIteminstService.countApproveProjInfoListByUnitOrLinkman("", loginInfo.getLinkmanInfoId(),"all");
        } else if (StringUtils.isNotBlank(loginInfo.getLinkmanInfoId())) {//委托人
            matCompletionNum = this.searchMatComplet(loginInfo.getUnitInfoId(), loginInfo.getLinkmanInfoId(), null, 1, 1).getTotal();
            approvalNum = this.searchIteminstApproveInfoListByUnitIdAndUserId("", loginInfo.getUnitInfoId(), loginInfo.getLinkmanInfoId(), 1, 1).getTotal();
            draftNum = this.searchApproveProjInfoListByUnitOrLinkman(loginInfo.getUnitInfoId(), loginInfo.getLinkmanInfoId(), "2", null, 1, 1).getTotal();
            supplyNum = this.searchSupplyInfoList(loginInfo.getUnitInfoId(), loginInfo.getLinkmanInfoId(), 1, 1).getTotal();
            applyNum = aeaHiIteminstService.countApproveProjInfoListByUnitOrLinkman(loginInfo.getUnitInfoId(), loginInfo.getLinkmanInfoId(),"all");
        } else {//企业
            matCompletionNum = this.searchMatComplet(loginInfo.getUnitInfoId(), "", null, 1, 1).getTotal();
            approvalNum = this.searchIteminstApproveInfoListByUnitIdAndUserId("", loginInfo.getUnitInfoId(), "", 1, 1).getTotal();
            draftNum = this.searchApproveProjInfoListByUnitOrLinkman(loginInfo.getUnitInfoId(), "", "2", null, 1, 1).getTotal();
            supplyNum = this.searchSupplyInfoList(loginInfo.getUnitInfoId(), "", 1, 1).getTotal();
            applyNum = aeaHiIteminstService.countApproveProjInfoListByUnitOrLinkman(loginInfo.getUnitInfoId(), "","all");
        }
        retVo.setApplyNum(applyNum);
        retVo.setApprovalNum(approvalNum);
        retVo.setDraftNum(draftNum);
        retVo.setMatCompletionNum(matCompletionNum);
        retVo.setSupplyNum(supplyNum);
        retVo.setWithdrawalNum(withdrawalNum);
        return retVo;
    }

    @Override
    public List<BscAttFileAndDir> getMatAttDetailByMatinstId(String matinstId) throws Exception {
        return fileAbstractService.getMatAttDetailByMatinstId(matinstId);
    }

    @Override
    public LifeCycleDiagramVo getLiftCycleDiagramInfo(String projInfoId, String unitInfoId, String userInfoId) throws Exception {
        LifeCycleDiagramVo lifeCycleDiagramVo = new LifeCycleDiagramVo();
        AeaProjInfo aeaProjInfo = aeaProjInfoService.getAeaProjInfoByProjInfoId(projInfoId);
        if (aeaProjInfo == null) throw new InvalidParameterException("查询项目不存在！");
        lifeCycleDiagramVo.setProjName(aeaProjInfo.getProjName());
        lifeCycleDiagramVo.setProjInfoId(aeaProjInfo.getProjInfoId());
        lifeCycleDiagramVo.setRegionId(aeaProjInfo.getRegionalism());
        String themeId = aeaProjInfo.getThemeId();
        AeaParTheme aeaParTheme = aeaParThemeService.getAeaParThemeByThemeId(themeId);
        if (aeaParTheme == null) throw new InvalidParameterException("当前项目下无相关主题信息！");
        lifeCycleDiagramVo.setThemeName(aeaParTheme.getThemeName());
        lifeCycleDiagramVo.setThemeId(aeaParTheme.getThemeId());
        lifeCycleDiagramVo.setThemeType(aeaParTheme.getThemeType());
        setLifeCycleDiagramVoStageInfo(lifeCycleDiagramVo, unitInfoId, userInfoId);
        return lifeCycleDiagramVo;
    }

    private void setLifeCycleDiagramVoStageInfo(LifeCycleDiagramVo lifeCycleDiagramVo, String unitInfoId, String userInfoId) throws Exception {
        List<AeaParStage> stages = aeaParStageService.listAeaParStageByThemeIdOrThemeVerId(lifeCycleDiagramVo.getThemeId(), "",null);
        //根据项目ID查询当前登录用户的所有并联申请实例ID
        List<AeaHiApplyinst> parallelApplyinsts = aeaHiApplyinstService.listApplyInstIdAndStateByProjInfoIdAndUser(lifeCycleDiagramVo.getProjInfoId(), ApplyType.UNIT.getValue(), unitInfoId, userInfoId);

        //最新的项目状态
        List<String> parallelApplyinstIds = parallelApplyinsts.size() > 0 ? parallelApplyinsts.stream().map(AeaHiApplyinst::getApplyinstId).collect(Collectors.toList()) : new ArrayList<>();
        List<AeaHiIteminst> parallellAeaHiIteminstList = parallelApplyinstIds.size() > 0 ? aeaHiIteminstService.getAeaHiIteminstListByApplyinstIds(parallelApplyinstIds, ApplyType.UNIT.getValue(),"0") : new ArrayList<>();
        List<AeaParStageVo> paralleStages = new ArrayList<>();
        AtomicReference<Boolean> isCurrentStage = new AtomicReference<>(false);//是否是处于办理中的阶段
        for (AeaParStage stage : stages) {
            AeaParStageVo aeaParStageVo = new AeaParStageVo();
            if (!"1".equals(stage.getIsNode())) continue;//只查询主线的阶段
            aeaParStageVo.setStageName(stage.getStageName());
            aeaParStageVo.setStageId(stage.getStageId());
            AtomicReference<Boolean> hadStageApply = new AtomicReference<>(false);//阶段是否已申报过

            parallelApplyinsts.stream().forEach(applyinst -> {
                if (stage.getStageId().equals(applyinst.getStageId())) {
                    if (ApplyState.COMPLETED.equals(applyinst.getApplyinstState())) {
                        aeaParStageVo.setStateType("0");
                    } else {
                        aeaParStageVo.setStateType("1");
                        if (!isCurrentStage.get()) {
                            isCurrentStage.set(true);
                            lifeCycleDiagramVo.setCurrentStageId(stage.getStageId());
                        }
                    }
                    aeaParStageVo.setStageInstId(applyinst.getStageInstId());
                    hadStageApply.set(true);
                }
            });
            if (!hadStageApply.get()) {
                aeaParStageVo.setStateType("2");
            }
            //历时相关VO
            if (StringUtils.isNotBlank(aeaParStageVo.getStageInstId())) {
                ActStoTimeruleInst actStoTimeruleInst = actStoTimeruleInstService.getProcessinstTimeruleInstByStageinstId(aeaParStageVo.getStageInstId());
                if (actStoTimeruleInst != null) aeaParStageVo.setStageRunTime(actStoTimeruleInst.getUseLimitTime());
            }

            if (aeaParStageVo.getStageRunTime() == null) aeaParStageVo.setStageRunTime(0D);
            //并联事项定义
            List<AeaItemBasic> parallelItems = aeaItemBasicService.getAeaItemBasicListByStageId(stage.getStageId(), "0", "", SecurityContext.getCurrentOrgId());
            aeaItemBasicService.conversionBasicItemToSssx(parallelItems, lifeCycleDiagramVo.getRegionId(), null, SecurityContext.getCurrentOrgId());

            List<AeaItemBasicVo> parallelItemVos = new ArrayList<>();
            AtomicInteger paraEndItem = new AtomicInteger();//并联办结事项数

            //if (parallellAeaHiIteminstList.size()>0){
            for (AeaItemBasic paraItem : parallelItems) {
                AeaItemBasicVo aeaItemBasicVo = new AeaItemBasicVo();
                aeaItemBasicVo.setItemName(paraItem.getItemName());
                aeaItemBasicVo.setItemVerId(paraItem.getItemVerId());
                aeaItemBasicVo.setOrgName(paraItem.getOrgName());
                aeaItemBasicVo.setDueNum(paraItem.getDueNum());
                Boolean haveInst = false;
                aeaItemBasicVo.setItemStateType("2");
                for (AeaHiIteminst itemInst : parallellAeaHiIteminstList) {
                    if (paraItem.getItemVerId().equals(itemInst.getItemVerId())) {
                        //历时相关VO
                        if (StringUtils.isNotBlank(itemInst.getIteminstId())) {
                            ActStoTimeruleInst itemActStoTimeruleInst = actStoTimeruleInstService.getProcessinstTimeruleInstByIteminstId(itemInst.getIteminstId());
                            if (itemActStoTimeruleInst != null) {
                                aeaItemBasicVo.setIteminstRunTime(itemActStoTimeruleInst.getUseLimitTime());
                            }
                        }
                        if (aeaItemBasicVo.getIteminstRunTime() == null) aeaItemBasicVo.setIteminstRunTime(0D);

                        aeaItemBasicVo.setIteminstState(itemInst.getIteminstState());
                        aeaItemBasicVo.setIteminstStartTime(itemInst.getStartTime());
                        aeaItemBasicVo.setIteminstEndTime(itemInst.getEndTime());
                        aeaItemBasicVo.setOrgName(itemInst.getApproveOrgName());
                        //事项状态类型
                        if (ItemStatus.isEnd(itemInst.getIteminstState())) {
                            aeaItemBasicVo.setItemStateType("0");
                        } else {
                            aeaItemBasicVo.setItemStateType("1");
                        }
                        haveInst = true;
                    } else {
                        if (!haveInst) aeaItemBasicVo.setItemStateType("2");
                    }
                }
                parallelItemVos.add(aeaItemBasicVo);
            }
            //}
            Collections.sort(parallelItemVos, Comparator.comparing(AeaItemBasicVo::getItemStateType));
            aeaParStageVo.setParallelItemList(parallelItemVos);


            //并行事项定义
            List<AeaItemBasic> seriItems = aeaItemBasicService.getAeaItemBasicListByStageId(stage.getStageId(), "1", "", SecurityContext.getCurrentOrgId());
            aeaItemBasicService.conversionBasicItemToSssx(seriItems, lifeCycleDiagramVo.getRegionId(), null, SecurityContext.getCurrentOrgId());
            List<AeaHiApplyinst> coreApplyinsts = aeaHiApplyinstService.listApplyInstIdAndStateByProjInfoIdAndUser(lifeCycleDiagramVo.getProjInfoId(), ApplyType.SERIES.getValue(), unitInfoId, userInfoId);

            //最新的项目状态
            List<String> coreApplyinstIds = coreApplyinsts.size() > 0 ? coreApplyinsts.stream().map(AeaHiApplyinst::getApplyinstId).collect(Collectors.toList()) : new ArrayList<>();

            List<AeaHiIteminst> coreAeaHiIteminstList = coreApplyinstIds.size() > 0 ? aeaHiIteminstService.getAeaHiIteminstListByApplyinstIds(coreApplyinstIds, ApplyType.SERIES.getValue(),"0") : new ArrayList<>();
            List<AeaItemBasicVo> corellelItemVos = new ArrayList<>();
            AtomicInteger coreEndItem = new AtomicInteger();//并行办结事项数
            //if (coreAeaHiIteminstList.size()>0){
            for (AeaItemBasic seriItem : seriItems) {
                AeaItemBasicVo aeaItemBasicVo = new AeaItemBasicVo();
                aeaItemBasicVo.setItemName(seriItem.getItemName());
                aeaItemBasicVo.setItemVerId(seriItem.getItemVerId());
                aeaItemBasicVo.setOrgName(seriItem.getOrgName());
                aeaItemBasicVo.setDueNum(seriItem.getDueNum());
                aeaItemBasicVo.setItemStateType("2");
                Boolean haveInst = false;
                for (AeaHiIteminst itemInst : coreAeaHiIteminstList) {

                    if (seriItem.getItemVerId().equals(itemInst.getItemVerId())) {
                        if (StringUtils.isNotBlank(itemInst.getIteminstId())) {
                            ActStoTimeruleInst itemActStoTimeruleInst = actStoTimeruleInstService.getProcessinstTimeruleInstByIteminstId(itemInst.getIteminstId());
                            if (itemActStoTimeruleInst != null) {
                                aeaItemBasicVo.setIteminstRunTime(itemActStoTimeruleInst.getUseLimitTime());
                            }
                        }
                        if (aeaItemBasicVo.getIteminstRunTime() == null) aeaItemBasicVo.setIteminstRunTime(0D);

                        aeaItemBasicVo.setIteminstState(itemInst.getIteminstState());
                        aeaItemBasicVo.setIteminstStartTime(itemInst.getStartTime());
                        aeaItemBasicVo.setIteminstEndTime(itemInst.getEndTime());
                        aeaItemBasicVo.setOrgName(itemInst.getApproveOrgName());
                        //事项状态类型
                        if (ItemStatus.isEnd(itemInst.getIteminstState())) {
                            aeaItemBasicVo.setItemStateType("0");
                        } else {
                            aeaItemBasicVo.setItemStateType("1");
                        }
                        haveInst = true;
                    } else {
                        if (!haveInst) aeaItemBasicVo.setItemStateType("2");
                    }
                }
                corellelItemVos.add(aeaItemBasicVo);
            }
            //}
            if (corellelItemVos.size() > 0)
                Collections.sort(corellelItemVos, Comparator.comparing(AeaItemBasicVo::getItemStateType));
            aeaParStageVo.setCoreItemList(corellelItemVos);
            //并行事项办结数
            if (corellelItemVos.size() > 0) {
                corellelItemVos.stream().forEach(itemInst -> {
                    if (StringUtils.isNotBlank(itemInst.getIteminstState()) && ItemStatus.isEnd(itemInst.getIteminstState())) {
                        coreEndItem.getAndIncrement();
                    }
                });
            }
            //并联事项办结数
            if (parallelItemVos.size() > 0) {
                parallelItemVos.stream().forEach(itemInst -> {
                    if (StringUtils.isNotBlank(itemInst.getIteminstState()) && ItemStatus.isEnd(itemInst.getIteminstState())) {
                        paraEndItem.getAndIncrement();
                    }
                });
            }
            int doneNumber = paraEndItem.get() + coreEndItem.get();//并联并行办结数
            int itemNumver = (aeaParStageVo.getParallelItemList() != null ? aeaParStageVo.getParallelItemList().size() : 0) +
                    (aeaParStageVo.getCoreItemList() != null ? aeaParStageVo.getCoreItemList().size() : 0);
            if (doneNumber != 0 && itemNumver != 0) {
                NumberFormat numberFormat = NumberFormat.getInstance();
                // 设置精确到小数点后2位
                numberFormat.setMaximumFractionDigits(0);
                aeaParStageVo.setEndProp(numberFormat.format((float) doneNumber / (float) itemNumver * 100) + "%");
            } else {
                aeaParStageVo.setEndProp("0%");
            }
            aeaParStageVo.setParallelDoneNumber(paraEndItem.get());
            aeaParStageVo.setCoreDoneNumber(coreEndItem.get());
            aeaParStageVo.setStageItemNum(aeaParStageVo.getParallelItemList().size() + aeaParStageVo.getCoreItemList().size());
            paralleStages.add(aeaParStageVo);
        }
        //当前阶段ID

        if (StringUtils.isBlank(lifeCycleDiagramVo.getCurrentStageId())) {//说明所有阶段全部办结
            lifeCycleDiagramVo.setCurrentStageId(paralleStages.get(paralleStages.size() - 1).getStageId());
        }

        lifeCycleDiagramVo.setAeaParStages(paralleStages);
    }


    public LifeCycleDiagramVo getLiftCycleDiagramInfo1(String projInfoId, String unitInfoId, String userInfoId) throws Exception {
        LifeCycleDiagramVo lifeCycleDiagramVo = new LifeCycleDiagramVo();
//        AeaProjInfo aeaProjInfo = aeaProjInfoService.getAeaProjInfoByProjInfoId(projInfoId);
//        if (aeaProjInfo == null) throw new InvalidParameterException("查询项目不存在！");
//        lifeCycleDiagramVo.setProjName(aeaProjInfo.getProjName());
//        lifeCycleDiagramVo.setProjInfoId(aeaProjInfo.getProjInfoId());
//        String themeId = aeaProjInfo.getThemeId();
//        AeaParTheme aeaParTheme = aeaParThemeService.getAeaParThemeByThemeId(themeId);
//        if (aeaParTheme == null) throw new InvalidParameterException("当前项目下无相关主题信息！");
//        lifeCycleDiagramVo.setThemeName(aeaParTheme.getThemeName());
//        //根据主题查询阶段
//        List<AeaParStage> stages = aeaParStageService.listAeaParStageByThemeIdOrThemeVerId(themeId,"");
//        //根据项目ID查询当前登录用户的所有申请实例ID
//        List<AeaHiApplyinst> applyinsts = aeaHiApplyinstService.listApplyInstIdAndStateByProjInfoIdAndUser(projInfoId,unitInfoId,userInfoId);
//        List<AeaHiIteminst> aeaHiIteminstList = new ArrayList<>();
//        //List<ProjStateDto> projStateDtos = new ArrayList<>();
//        //最新的项目状态
//        logger.error("--getLiftCycleDiagramInfo---start----");
//        long start=System.currentTimeMillis();
//        //for (String applyinstId : applyinstIds) {
//            //根据申请实例ID查询所有项目实例
//            //aeaHiIteminstList.addAll(aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(applyinstId));
//           // projStateDtos.add(approveDataService.searchProjStateDtoByApplyinstId(applyinstId));
//        //}
//        List<String> applyinstIds = applyinsts.size()>0?applyinsts.stream().map(AeaHiApplyinst::getApplyinstId).collect(Collectors.toList()):new ArrayList<>();
//        aeaHiIteminstList.addAll(aeaHiIteminstService.getAeaHiIteminstListByApplyinstIds(applyinstIds));
//        logger.error("--getLiftCycleDiagramInfo---end----耗时："+(System.currentTimeMillis()-start));
//        List<AeaParStageVo> paralleStages=new ArrayList<>();
//        for (AeaParStage stage : stages){
//            AeaParStageVo aeaParStageVo = new AeaParStageVo();
//            if(!"1".equals(stage.getIsNode())) continue;//只查询主线的阶段
//            aeaParStageVo.setStageName(stage.getStageName());
//            aeaParStageVo.setStageId(stage.getStageId());
//            AtomicReference<Boolean> hadStageApply = new AtomicReference<>(false);//阶段是否已申报过
//            applyinsts.stream().forEach(applyinst -> {
//                if (stage.getStageId().equals(applyinst.getStageId())){
//                    if (ApplyState.COMPLETED .equals(applyinst.getApplyinstState())){
//                        aeaParStageVo.setStateType("0");
//                    }else {
//                        aeaParStageVo.setStateType("1");
//                    }
//                    //aeaParStageVo.setStageInstId(applyinst.getStageInstId());
//                    hadStageApply.set(true);
//                }
//            });
//            if (!hadStageApply.get()){
//                aeaParStageVo.setStateType("2");
//            }
//            //历时相关VO
//            if (StringUtils.isNotBlank(aeaParStageVo.getStageInstId())){
//                ActStoTimeruleInst actStoTimeruleInst = actStoTimeruleInstService.getProcessinstTimeruleInstByStageinstId(aeaParStageVo.getStageInstId());
//                if (actStoTimeruleInst!=null) aeaParStageVo.setStageRunTime(actStoTimeruleInst.getUseLimitTime());
//            }
//
//            if (aeaParStageVo.getStageRunTime()==null) aeaParStageVo.setStageRunTime(0D);
//            //并联事项定义
//            List<AeaItemBasic> parallelItems = aeaItemBasicService.getAeaItemBasicListByStageId(stage.getStageId(),"0","",SecurityContext.getCurrentOrgId());
//            aeaItemBasicService.conversionBasicItemToSssx(parallelItems,aeaProjInfo.getRegionalism());
//
//            List<AeaItemBasicVo> parallelItemVos = new ArrayList<>();
//            AtomicInteger paraEndItem= new AtomicInteger();//并联办结事项数
//
//            if (aeaHiIteminstList.size()>0){
//                for (AeaItemBasic paraItem:parallelItems){
//                    AeaItemBasicVo aeaItemBasicVo = new AeaItemBasicVo();
//                    aeaItemBasicVo.setItemName(paraItem.getItemName());
//                    aeaItemBasicVo.setItemVerId(paraItem.getItemVerId());
//                    aeaItemBasicVo.setOrgName(paraItem.getOrgName());
//                    aeaItemBasicVo.setDueNum(paraItem.getDueNum());
//                    Boolean haveInst = false;
//                    for (AeaHiIteminst itemInst:aeaHiIteminstList){
//                        if (paraItem.getItemVerId().equals(itemInst.getItemVerId())){
//                            //历时相关VO
//                            if (StringUtils.isNotBlank(itemInst.getIteminstId())){
//                                ActStoTimeruleInst itemActStoTimeruleInst = actStoTimeruleInstService.getProcessinstTimeruleInstByIteminstId(itemInst.getIteminstId());
//                                if (itemActStoTimeruleInst!=null) {
//                                    aeaItemBasicVo.setIteminstRunTime(itemActStoTimeruleInst.getUseLimitTime());
//                                }
//                            }
//                            if (aeaItemBasicVo.getIteminstRunTime()==null)  aeaItemBasicVo.setIteminstRunTime(0D);
//
//                            aeaItemBasicVo.setIteminstState(itemInst.getIteminstState());
//                            aeaItemBasicVo.setIteminstStartTime(itemInst.getStartTime());
//                            aeaItemBasicVo.setIteminstEndTime(itemInst.getEndTime());
//                            aeaItemBasicVo.setOrgName(itemInst.getApproveOrgName());
//                            //事项状态类型
//                            if (ItemStatus.isEnd(itemInst.getIteminstState())){
//                                aeaItemBasicVo.setItemStateType("0");
//                            }else {
//                                aeaItemBasicVo.setItemStateType("1");
//                            }
//                            haveInst = true;
//                        }else{
//                            if (!haveInst) aeaItemBasicVo.setItemStateType("2");
//                        }
//                    }
//                    parallelItemVos.add(aeaItemBasicVo);
//                }
//            }
//            Collections.sort(parallelItemVos, Comparator.comparing(AeaItemBasicVo::getItemStateType));
//            aeaParStageVo.setParallelItemList(parallelItemVos);
//
//
//            //并行事项定义
//            List<AeaItemBasic> seriItems = aeaItemBasicService.getAeaItemBasicListByStageId(stage.getStageId(),"1","",SecurityContext.getCurrentOrgId());
//            aeaItemBasicService.conversionBasicItemToSssx(seriItems,aeaProjInfo.getRegionalism());
//
//            List<AeaItemBasicVo> corellelItemVos = new ArrayList<>();
//            AtomicInteger coreEndItem= new AtomicInteger();//并行办结事项数
//            if (aeaHiIteminstList.size()>0){
//                for (AeaItemBasic seriItem:seriItems){
//                    AeaItemBasicVo aeaItemBasicVo = new AeaItemBasicVo();
//                    aeaItemBasicVo.setItemName(seriItem.getItemName());
//                    aeaItemBasicVo.setItemVerId(seriItem.getItemVerId());
//                    aeaItemBasicVo.setOrgName(seriItem.getOrgName());
//                    aeaItemBasicVo.setDueNum(seriItem.getDueNum());
//                    Boolean haveInst = false;
//                    for(AeaHiIteminst itemInst:aeaHiIteminstList){
//
//                        if (seriItem.getItemVerId().equals(itemInst.getItemVerId())){
//                            if (StringUtils.isNotBlank(itemInst.getIteminstId())){
//                                ActStoTimeruleInst itemActStoTimeruleInst = actStoTimeruleInstService.getProcessinstTimeruleInstByIteminstId(itemInst.getIteminstId());
//                                if (itemActStoTimeruleInst!=null) {
//                                    aeaItemBasicVo.setIteminstRunTime(itemActStoTimeruleInst.getUseLimitTime());
//                                }
//                            }
//                            if (aeaItemBasicVo.getIteminstRunTime()==null)  aeaItemBasicVo.setIteminstRunTime(0D);
//
//                            aeaItemBasicVo.setIteminstState(itemInst.getIteminstState());
//                            aeaItemBasicVo.setIteminstStartTime(itemInst.getStartTime());
//                            aeaItemBasicVo.setIteminstEndTime(itemInst.getEndTime());
//                            aeaItemBasicVo.setOrgName(itemInst.getApproveOrgName());
//                            //事项状态类型
//                            if (ItemStatus.isEnd(itemInst.getIteminstState())){
//                                aeaItemBasicVo.setItemStateType("0");
//                            }else {
//                                aeaItemBasicVo.setItemStateType("1");
//                            }
//                            haveInst = true;
//                        }else{
//                            if(!haveInst) aeaItemBasicVo.setItemStateType("2");
//                        }
//                    }
//                    corellelItemVos.add(aeaItemBasicVo);
//                }
//            }
//            Collections.sort(corellelItemVos, Comparator.comparing(AeaItemBasicVo::getItemStateType));
//            aeaParStageVo.setCoreItemList(corellelItemVos);
//            //并行事项办结数
//            if (corellelItemVos.size() > 0) {
//                corellelItemVos.stream().forEach(itemInst -> {
//                    if (StringUtils.isNotBlank(itemInst.getIteminstState())&&ItemStatus.isEnd(itemInst.getIteminstState())) {
//                        coreEndItem.getAndIncrement();
//                    }
//                });
//            }
//            //并联事项办结数
//            if (parallelItemVos.size() > 0) {
//                parallelItemVos.stream().forEach(itemInst -> {
//                    if (StringUtils.isNotBlank(itemInst.getIteminstState())&&ItemStatus.isEnd(itemInst.getIteminstState())) {
//                        paraEndItem.getAndIncrement();
//                    }
//                });
//            }
//            int doneNumber = paraEndItem.get()+coreEndItem.get();//并联并行办结数
//            int itemNumver = (aeaParStageVo.getParallelItemList()!=null?aeaParStageVo.getParallelItemList().size():0)+
//                    (aeaParStageVo.getCoreItemList()!=null?aeaParStageVo.getCoreItemList().size():0);
//            if (doneNumber!=0&&itemNumver!=0){
//                NumberFormat numberFormat = NumberFormat.getInstance();
//                // 设置精确到小数点后2位
//                numberFormat.setMaximumFractionDigits(0);
//                aeaParStageVo.setEndProp(numberFormat.format((float)doneNumber/(float)itemNumver*100)+"%");
//            }else {
//                aeaParStageVo.setEndProp("0%");
//            }
//            aeaParStageVo.setParallelDoneNumber(paraEndItem.get());
//            aeaParStageVo.setCoreDoneNumber(coreEndItem.get());
//            aeaParStageVo.setStageItemNum(aeaParStageVo.getParallelItemList().size()+aeaParStageVo.getCoreItemList().size());
//            paralleStages.add(aeaParStageVo);
//        }
//        //当前阶段ID
//        for(int i=0;i<paralleStages.size();i++){
//            //if (i==paralleStages.size()-1) break;
//            if ("1".equals(paralleStages.get(i).getStateType())){
//                lifeCycleDiagramVo.setCurrentStageId(paralleStages.get(i).getStageId());
//            }
//        }
//        lifeCycleDiagramVo.setAeaParStages(paralleStages);

        return lifeCycleDiagramVo;
    }

    @Override
    public PageInfo<ApproveProjInfoDto> searchWithdrawApplyListByUnitOrLinkman(String unitInfoId, String userInfoId, String keyword, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ApproveProjInfoDto> list = aeaHiIteminstMapper.getWithdrawApplyListByUnitOrLinkman(unitInfoId, userInfoId, keyword);
        return new PageInfo<>(list);
    }
}
