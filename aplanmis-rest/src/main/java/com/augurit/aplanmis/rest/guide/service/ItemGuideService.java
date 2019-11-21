package com.augurit.aplanmis.rest.guide.service;

import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.sc.dic.code.service.BscDicCodeService;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.aplanmis.common.constants.ActiveStatus;
import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.domain.AeaItemAcceptRange;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaItemMat;
import com.augurit.aplanmis.common.domain.AeaItemServiceBasic;
import com.augurit.aplanmis.common.domain.AeaItemState;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.domain.AeaParStageItem;
import com.augurit.aplanmis.common.domain.AeaParTheme;
import com.augurit.aplanmis.common.domain.AeaParThemeSeq;
import com.augurit.aplanmis.common.domain.AeaParThemeVer;
import com.augurit.aplanmis.common.domain.AeaServiceLegal;
import com.augurit.aplanmis.common.domain.AeaServiceLegalClause;
import com.augurit.aplanmis.common.mapper.AeaItemAcceptRangeMapper;
import com.augurit.aplanmis.common.mapper.AeaItemBasicMapper;
import com.augurit.aplanmis.common.mapper.AeaItemMatMapper;
import com.augurit.aplanmis.common.mapper.AeaItemServiceBasicMapper;
import com.augurit.aplanmis.common.mapper.AeaItemStateMapper;
import com.augurit.aplanmis.common.mapper.AeaParStageItemMapper;
import com.augurit.aplanmis.common.mapper.AeaParStageMapper;
import com.augurit.aplanmis.common.mapper.AeaParThemeMapper;
import com.augurit.aplanmis.common.mapper.AeaParThemeSeqMapper;
import com.augurit.aplanmis.common.mapper.AeaParThemeVerMapper;
import com.augurit.aplanmis.common.mapper.AeaServiceLegalClauseMapper;
import com.augurit.aplanmis.common.mapper.AeaServiceLegalMapper;
import com.augurit.aplanmis.common.service.state.AeaItemStateService;
import com.augurit.aplanmis.common.service.theme.AeaParThemeService;
import com.augurit.aplanmis.rest.guide.service.vo.AcceptRangeVo;
import com.augurit.aplanmis.rest.guide.service.vo.BasicInfoVo;
import com.augurit.aplanmis.rest.guide.service.vo.SeriesApplyHandleVo;
import com.augurit.aplanmis.rest.index.service.vo.ThemeTypeVo;
import com.google.common.collect.Lists;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class ItemGuideService {

    @Value("${dg.sso.access.platform.org.top-org-id:0368948a-1cdf-4bf8-a828-71d796ba89f6}")
    private String topOrgId;
    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;
    @Autowired
    private AeaParThemeSeqMapper aeaParThemeSeqMapper;
    @Autowired
    private AeaParThemeVerMapper aeaParThemeVerMapper;
    @Autowired
    private AeaParStageMapper aeaParStageMapper;
    @Autowired
    private AeaParThemeMapper aeaParThemeMapper;
    @Autowired
    private AeaParStageItemMapper aeaParStageItemMapper;
    @Autowired
    private AeaItemAcceptRangeMapper aeaItemAcceptRangeMapper;
    @Autowired
    private AeaItemServiceBasicMapper aeaItemServiceBasicMapper;
    @Autowired
    private AeaServiceLegalClauseMapper aeaServiceLegalClauseMapper;
    @Autowired
    private AeaServiceLegalMapper aeaServiceLegalMapper;
    @Autowired
    private AeaParThemeService aeaParThemeService;
    @Autowired
    private BscDicCodeService bscDicCodeService;
    @Autowired
    private AeaItemMatMapper aeaItemMatService;
    @Autowired
    private AeaItemBasicMapper aeaItemBasicService;
    @Autowired
    private AeaItemStateMapper aeaItemStateMapper;
    @Autowired
    private AeaItemStateService aeaItemStateService;

    /**
     * 查询所有事项
     */
    public List<AeaItemBasic> listAll4Published(AeaItemBasic aeaItemBasic) {
        return aeaItemBasicMapper.listLatestOkAeaItemBasic(aeaItemBasic);
    }

    /**
     * 根据主题id或者阶段id查询包含的事项
     *
     * @param stageId 阶段id
     * @param themeId 主题id
     */
    public List<String> getItemVerIdsByThemeIdAndStageId(String stageId, String themeId) {
        Assert.notNull(themeId, "themeId is null.");

        if (StringUtils.isNotBlank(stageId)) {
            return aeaParStageItemMapper.listAeaStageItemByStageId(stageId, "", topOrgId)
                    .stream().map(AeaParStageItem::getItemVerId).collect(Collectors.toList());
        } else {
            List<String> stageIds = listAllStages(themeId).stream().map(AeaParStage::getStageId).collect(Collectors.toList());
            Set<String> itemVerIds = new HashSet<>();
            AeaParStageItem param = new AeaParStageItem();
            if (CollectionUtils.isNotEmpty(stageIds)) {
                for (String id : stageIds) {
                    param.setStageId(id);
                    itemVerIds.addAll(aeaParStageItemMapper.listStageAllItem(param).stream().map(AeaParStageItem::getItemVerId).collect(Collectors.toSet()));
                }
                return Lists.newArrayList(itemVerIds);
            }
        }
        return new ArrayList<>();
    }

    /**
     * 获取所有主题
     *
     * @return 主题列表
     */
    public List<AeaParTheme> listAllThemes() {
        try {
            AeaParTheme aeaParTheme = new AeaParTheme();
            return aeaParThemeMapper.listAeaParTheme(aeaParTheme);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("查询主题失败");
        }
    }

    /**
     * @param themeId 主题id
     * @return 主题下的所有阶段
     */
    public List<AeaParStage> listAllStages(String themeId) {
        Assert.notNull(themeId, "themeId is null.");

        try {
            // 获取最大编号
            AeaParThemeSeq themeSeq = aeaParThemeSeqMapper.getAeaParThemeSeqByThemeId(themeId, topOrgId);
            if (themeSeq != null && themeSeq.getMaxNum() != null) {
                AeaParThemeVer themeVer = new AeaParThemeVer();
                themeVer.setThemeId(themeId);
                themeVer.setVerNum(themeSeq.getMaxNum());
                themeVer.setIsActive(ActiveStatus.ACTIVE.getValue());
                themeVer.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
                List<AeaParThemeVer> themeVerList = aeaParThemeVerMapper.listAeaParThemeVer(themeVer);
                if (themeVerList != null && themeVerList.size() > 0) {
                    String themeVerId = themeVerList.get(0).getThemeVerId();
                    AeaParStage stage = new AeaParStage();
                    stage.setThemeVerId(themeVerId);
                    stage.setIsDeleted(Status.OFF);
                    return aeaParStageMapper.listAeaParStage(stage);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("根据themeId查询阶段失败, themeId: " + themeId, e);
        }
        return null;
    }

    //事项的办事指南
    public Map<String, Object> guideDetails(String itemVerId) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (StringUtils.isNotBlank(itemVerId)) {
                AeaItemBasic aeaItemBasic = aeaItemBasicMapper.getOneByItemVerId(itemVerId, topOrgId);
                if (aeaItemBasic == null) {
                    return null;
                }

                //办事指南基本信息
                result.put("itemBasicInfo", getItemBasicInfo(itemVerId));
                // 范围与条件
                result.put("acceptRange", getItemAcceptRange(aeaItemBasic.getItemBasicId()));
                // 设立依据
                result.put("setupBasis", getItemServiceBasic(itemVerId));

                result.put("statesAndMaterials", getStatesAndMaterials(itemVerId, "ROOT"));
                /*AeaItemGuide aeaItemGuide = getAeaItemGuide(itemVerId);

                result.put("guide", aeaItemGuide);
                //设立依据
                result.put("aeaItemGuideAccordings", aeaItemGuideAccordingsMapper.findByItemVerId(itemVerId));
                //窗口信息
                AeaItemGuideDepartments aeaItemGuideDepartments = aeaItemGuideDepartmentsMapper.findOneByItemVerId(itemVerId);
                result.put("aeaItemGuideDepartment", aeaItemGuideDepartments == null ? new AeaItemGuideDepartments() : aeaItemGuideDepartments);
                //常见问题
                result.put("questions", aeaItemGuideQuestionsMapper.findByItemVerId(itemVerId));
                //中介服务
                result.put("service", listAeaitemGuideMaterials(itemVerId));
                //扩展字段
                AeaItemGuideExtend aeaItemGuideExtend = aeaItemGuideExtendMapper.findOneByItemVerId(itemVerId);
                result.put("extend", aeaItemGuideExtend == null ? new AeaItemGuideExtend() : aeaItemGuideExtend);*/
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("查询事项办事指南失败, itemVerId: " + itemVerId);
        }
        return result;
    }

    private List<AeaItemServiceBasic> getItemServiceBasic(String itemVerId) {
        List<AeaItemServiceBasic> basicsList = new ArrayList<>();
        try {
            AeaItemServiceBasic queryParam = new AeaItemServiceBasic();
            queryParam.setRecordId(itemVerId);
            basicsList = aeaItemServiceBasicMapper.listAeaItemServiceBasic(queryParam);
            if (basicsList.size() > 0) {
                for (AeaItemServiceBasic basic : basicsList) {
                    if (com.augurit.agcloud.framework.util.StringUtils.isNotBlank(basic.getLegalClauseId())) {
                        AeaServiceLegalClause clause = aeaServiceLegalClauseMapper.getAeaServiceLegalClauseById(basic.getLegalClauseId());
                        AeaServiceLegal legal = aeaServiceLegalMapper.getAeaServiceLegalById(clause.getLegalId());
                        basic.setLegalClauseId(clause.getLegalClauseId());
                        basic.setClauseTitle(clause.getClauseTitle());
                        basic.setClauseContent(clause.getClauseContent());
                        basic.setLegalName(legal.getLegalName());
                        basic.setIssueOrg(legal.getIssueOrg());
                        basic.setBasicNo(legal.getBasicNo());
                        basic.setExeDate(legal.getExeDate());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询事项的设立依据错误, itemVerId: {}", itemVerId);
            //ignore
        }
        return basicsList;
    }

    public SeriesApplyHandleVo getStatesAndMaterials(String itemVerId, String itemStateId) {
        Assert.notNull(itemVerId, "itemVerId不能为空");

        AeaItemBasic aeaItemBasic = aeaItemBasicService.getAeaItemBasicByItemVerId(itemVerId, topOrgId);
        if (aeaItemBasic == null) {
            return new SeriesApplyHandleVo();
        }
        SeriesApplyHandleVo applyStates;
        // 分情形
        if ("1".equals(aeaItemBasic.getIsNeedState())) {
            try {
                applyStates = listSeriesNeedStateApplyMats(itemVerId, itemStateId);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("查询阶段下的材料失败， itemVerId:" + itemVerId + ", parentId: " + itemStateId, e);
            }
        } else {
            // 不分情形，获取所有材料和情形
            applyStates = listSeriesNostateApplyMats(itemVerId);
        }
        return applyStates;
    }

    public SeriesApplyHandleVo listSeriesNeedStateApplyMats(String itemVerId, String parentId) throws Exception {
        SeriesApplyHandleVo vo = new SeriesApplyHandleVo();
        String[] itemVerIds = {itemVerId};
        //vo属性问答列表--questionStates
        List<SeriesApplyHandleVo.QuestionStateVo> questionStates = new ArrayList<>();
        //vo属性及QuestionStateVo属性：情形列表--stateMats---
        List<SeriesApplyHandleVo.MatVo> stateMats = new ArrayList<>();

        List<AeaItemMat> matList;

        // 父情形
        AeaItemState aeaItemStateById = aeaItemStateMapper.getAeaItemStateById(parentId);
        List<AeaItemState> aeaItemStates = aeaItemStateService.listAeaItemStateByParentId(itemVerId, null, parentId, topOrgId);

        //先获取parentId材料
        if ("root".equalsIgnoreCase(parentId)) {
            //root材料
            matList = aeaItemMatService.getMatListByItemVerIds(itemVerIds, "1", "1");
        } else {
            //选择的情形材料
            String[] stateIds = {parentId};
            matList = aeaItemMatService.getMatListByItemStateIds(stateIds);
        }
        //处理材料
        for (AeaItemMat mat : matList) {
            addReplyIdentifier(mat);
            SeriesApplyHandleVo.MatVo matVo = new SeriesApplyHandleVo.MatVo();
            BeanUtils.copyProperties(mat, matVo);
            stateMats.add(matVo);
        }
        //赋值材料
        vo.setStateMats(stateMats);
        //处理情形
        SeriesApplyHandleVo.QuestionStateVo questionStateVo;
        SeriesApplyHandleVo.AnswerStateVo answerStateVo;
        for (AeaItemState itemState : aeaItemStates) {
            //QuestionStateVo属性----List<SeriesApplyHandleVo.AnswerStateVo> answerStates;
            List<SeriesApplyHandleVo.AnswerStateVo> answerStates = new ArrayList<>();
            questionStateVo = new SeriesApplyHandleVo.QuestionStateVo();
            BeanUtils.copyProperties(itemState, questionStateVo);
            if (null != aeaItemStateById) {
                questionStateVo.setParentQuestionStateId(aeaItemStateById.getParentStateId());
            }
            List<AeaItemState> answerStates1 = itemState.getAnswerStates();
            for (AeaItemState state : answerStates1) {
                answerStateVo = new SeriesApplyHandleVo.AnswerStateVo();
                BeanUtils.copyProperties(state, answerStateVo);
                answerStates.add(answerStateVo);
            }
            questionStateVo.setAnswerStates(answerStates);
            questionStates.add(questionStateVo);
        }
        //赋值
        vo.setQuestionStates(questionStates);

        return vo;
    }

    public SeriesApplyHandleVo listSeriesNostateApplyMats(String itemVerId) {
        SeriesApplyHandleVo vo = new SeriesApplyHandleVo();
        List<SeriesApplyHandleVo.MatVo> stateMats = new ArrayList<>();
        String[] itemVerIds = {itemVerId};
        List<AeaItemMat> matList = aeaItemMatService.getMatListByItemVerIds(itemVerIds, "1", null);
        for (AeaItemMat mat : matList) {
            addReplyIdentifier(mat);
            SeriesApplyHandleVo.MatVo matVo = new SeriesApplyHandleVo.MatVo();
            BeanUtils.copyProperties(mat, matVo);
            stateMats.add(matVo);
        }
        vo.setStateMats(stateMats);
        return vo;
    }

    private AcceptRangeVo getItemAcceptRange(String itemBasicId) {
        AeaItemAcceptRange acceptRange = aeaItemAcceptRangeMapper.getAeaItemAcceptRangeByItemBasicId(itemBasicId);
        if (acceptRange == null) {
            return new AcceptRangeVo();
        }
        return AcceptRangeVo.from(acceptRange);
    }

    private BasicInfoVo getItemBasicInfo(String itemVerId) {
        AeaItemBasic aeaItemBasic = aeaItemBasicMapper.getOneByItemVerId(itemVerId, topOrgId);
        return BasicInfoVo.from(aeaItemBasic);
    }

    /*private AeaItemGuide getAeaItemGuide(String itemVerId) {

        AeaItemGuide aeaItemGuide = aeaItemGuideMapper.getOneByItemVerId(itemVerId, topOrgId);
        if (aeaItemGuide == null) {
            return new AeaItemGuide();
        }
        if (StringUtils.isNotBlank(aeaItemGuide.getWsbllc())) {
            aeaItemGuide.setWsbllc(aeaItemGuide.getWsbllc().replaceAll("\\n", "<br>"));
        }

        if (StringUtils.isNotBlank(aeaItemGuide.getAcceptCondition())) {
            aeaItemGuide.setAcceptCondition(aeaItemGuide.getAcceptCondition().replaceAll("\\n", "<br>"));
        }

        if (StringUtils.isNotBlank(aeaItemGuide.getCkbllc())) {
            aeaItemGuide.setCkbllc(aeaItemGuide.getCkbllc().replaceAll("\\n", "<br>"));
        }
        return aeaItemGuide;
    }*/

    /*public List<AeaItemGuideMaterials> listAeaitemGuideMaterials(String itemId) {
        AeaItemGuideMaterials materials = new AeaItemGuideMaterials();
        materials.setItemVerId(itemId);
        List<AeaItemGuideMaterials> materialsList = aeaItemGuideMaterialsMapper.listAeaItemGuideMaterials(materials);
        if (!materialsList.isEmpty()) {
            Map<String, AeaItemGuideMaterials> map = new HashMap<>();
            for (AeaItemGuideMaterials aeaItemGuideMaterials : materialsList) {
                String serviceCode = aeaItemGuideMaterials.getIntermediaryservicescode();
                if (!map.containsKey(serviceCode)) {
                    map.put(serviceCode, aeaItemGuideMaterials);
                }
            }
            materialsList.clear();
            for (String key : map.keySet()) {
                materialsList.add(map.get(key));
            }
        }
        return materialsList;
    }*/

    public List<AeaItemMat> findAllMats(String itemVerId, List<String> itemStateIds) {
        Assert.notNull(itemVerId, "itemVerId不能为空");

        List<AeaItemMat> allMats = new ArrayList<>();
        AeaItemBasic aeaItemBasic = aeaItemBasicMapper.getOneByItemVerId(itemVerId, topOrgId);
        if (aeaItemBasic == null || itemStateIds == null || itemStateIds.size() == 0) {
            return allMats;
        }
        allMats = aeaItemMatService.getMatListByItemStateIds(itemStateIds.toArray(new String[0]));
        return allMats;
    }

    /**
     * 给文件类型是批复文件的在其文件名后加上批复标识
     */
    private void addReplyIdentifier(AeaItemMat itemMat) {
        if ("1".equals(itemMat.getIsOfficialDoc())) {
            itemMat.setMatName(itemMat.getMatName() + "(批复)");
        }
    }

    public List<ThemeTypeVo> getMainThemeTypeCategory(String category) throws Exception {
        List<ThemeTypeVo> voList = new ArrayList<>();
        List<AeaParTheme> aeaParThemes = new ArrayList<>();
        ThemeTypeVo vo;
        //默认查询并联主线
        if (org.springframework.util.StringUtils.isEmpty(category)) {
            category = "0";
        }
        AeaParTheme aeaParTheme = new AeaParTheme();
        //主线
        if ("0".equals(category)) {
            //先查询所有的可用的主题
            aeaParTheme.setIsMainline("1");
            aeaParThemes = aeaParThemeService.listAeaParTheme(aeaParTheme);

        }
        //辅线
        else if ("1".equals(category)) {
            aeaParTheme.setIsMainline("0");
            aeaParTheme.setIsAuxiline(("1"));
            aeaParThemes = aeaParThemeService.listAeaParTheme(aeaParTheme);
        }
        String currentOrgId = topOrgId;
        if (aeaParThemes.size() > 0) {
            Set<String> themeTypes = aeaParThemes.stream().map(AeaParTheme::getThemeType).collect(Collectors.toSet());
            for (String themeType : themeTypes) {
                BscDicCodeItem itemByTypeCodeAndItemCode = bscDicCodeService.getItemByTypeCodeAndItemCode("THEME_TYPE", themeType, topOrgId);
                vo = new ThemeTypeVo();
                List<AeaParTheme> themeList = aeaParThemeService.getTestRunOrPublishedVerAeaParTheme(themeType, currentOrgId);
                if (itemByTypeCodeAndItemCode != null) {
                    vo.buildThemeTypeVo(itemByTypeCodeAndItemCode);
                }
                vo.setThemeList(themeList);
                voList.add(vo);
            }
        }
        return voList;
    }
}
