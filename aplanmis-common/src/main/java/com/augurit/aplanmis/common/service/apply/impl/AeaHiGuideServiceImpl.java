package com.augurit.aplanmis.common.service.apply.impl;

import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.mapper.OpuOmOrgMapper;
import com.augurit.aplanmis.common.apply.item.ComputedItem;
import com.augurit.aplanmis.common.apply.item.GuideComputedItem;
import com.augurit.aplanmis.common.apply.item.GuideItemPrivilegeComputationHandler;
import com.augurit.aplanmis.common.constants.ApplySource;
import com.augurit.aplanmis.common.constants.GuideApplyState;
import com.augurit.aplanmis.common.constants.GuideDetailType;
import com.augurit.aplanmis.common.domain.AeaHiGuide;
import com.augurit.aplanmis.common.domain.AeaHiGuideDetail;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.domain.AeaParTheme;
import com.augurit.aplanmis.common.domain.AeaParThemeVer;
import com.augurit.aplanmis.common.domain.AeaSolicitItem;
import com.augurit.aplanmis.common.mapper.AeaHiGuideDetailMapper;
import com.augurit.aplanmis.common.mapper.AeaHiGuideMapper;
import com.augurit.aplanmis.common.mapper.AeaParStageMapper;
import com.augurit.aplanmis.common.mapper.AeaParThemeMapper;
import com.augurit.aplanmis.common.mapper.AeaParThemeVerMapper;
import com.augurit.aplanmis.common.mapper.AeaSolicitItemMapper;
import com.augurit.aplanmis.common.mapper.AeaSolicitOrgMapper;
import com.augurit.aplanmis.common.service.apply.AeaHiGuideService;
import com.augurit.aplanmis.common.service.item.AeaItemBasicService;
import com.augurit.aplanmis.common.vo.guide.GuideDetailVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Transactional
@Slf4j
public class AeaHiGuideServiceImpl implements AeaHiGuideService {

    @Autowired
    private AeaHiGuideMapper aeaHiGuideMapper;
    @Autowired
    private AeaHiGuideDetailMapper aeaHiGuideDetailMapper;
    @Autowired
    private AeaParStageMapper aeaParStageMapper;
    @Autowired
    private AeaItemBasicService aeaItemBasicService;
    @Autowired
    private AeaParThemeVerMapper aeaParThemeVerMapper;
    @Autowired
    private AeaParThemeMapper aeaParThemeMapper;
    @Autowired
    private AeaSolicitItemMapper aeaSolicitItemMapper;
    @Autowired
    private AeaSolicitOrgMapper aeaSolicitOrgMapper;
    @Autowired
    private OpuOmOrgMapper opuOmOrgMapper;

    @Override
    public void deleteAeaHiGuideByGuideId(String guideId) {
        aeaHiGuideMapper.deleteAeaHiGuideByGuideId(guideId);
    }

    @Override
    public void insertAeaHiGuide(AeaHiGuide record) {
        aeaHiGuideMapper.insertAeaHiGuide(record);
    }

    @Override
    public AeaHiGuide getAeaHiGuideByGuideId(String guideId) {
        return aeaHiGuideMapper.getAeaHiGuideByGuideId(guideId);
    }

    @Override
    public void updateAeaHiGuide(AeaHiGuide record) {
        aeaHiGuideMapper.updateAeaHiGuide(record);
    }

    @Override
    public void batchInsertAeaHiGuide(List<AeaHiGuide> list) {
        aeaHiGuideMapper.batchInsertAeaHiGuide(list);
    }

    @Override
    @SuppressWarnings("unchecked")
    public PageInfo<AeaHiGuide> list(AeaHiGuide aeaHiGuide, Page page) {
        PageHelper.startPage(page);
        List<AeaHiGuide> aeaHiGuides = aeaHiGuideMapper.listAeaHiGuide(aeaHiGuide);
        aeaHiGuides.forEach(ahg -> {
            ahg.setApplyStateName(GuideApplyState.fromValue(ahg.getApplyState()).getName());
            ahg.setApplySource(ApplySource.NET.getName());
            computeTimeLimit(ahg);
        });
        return new PageInfo<>(aeaHiGuides);
    }

    private void computeTimeLimit(AeaHiGuide ahg) {
        if (!GuideApplyState.DEPT_FINISHED.getValue().equals(ahg.getApplyState())) {
            Date currentTime = new Date();
            long duration = (currentTime.getTime() - ahg.getGuideStartTime().getTime()) / (1000 * 3600);
            long delt = duration - ahg.getDueTimeLimit().longValue();
            if (delt > 0) {
                ahg.setOverdue("1");
                ahg.setTimeLimitText("逾期" + delt + "小时");
            } else {
                ahg.setOverdue("0");
                ahg.setTimeLimitText("剩余" + Math.abs(delt) + "小时");
            }
        } else {
            ahg.setOverdue("2");
            ahg.setTimeLimitText("共计" + ahg.getRealTimeLimit() + "小时");
        }

    }

    @Override
    public GuideDetailVo detail(String guideId) throws Exception {
        String rootOrgId = SecurityContext.getCurrentOrgId();
        String currentUserId = SecurityContext.getCurrentUserId();
        AeaHiGuide aeaHiGuide = aeaHiGuideMapper.getAeaHiGuideByGuideId(guideId);

        AeaParStage aeaParStage = aeaParStageMapper.getAeaParStageById(aeaHiGuide.getStageId());
        AeaParThemeVer aeaParThemeVer = aeaParThemeVerMapper.getAeaParThemeVerById(aeaParStage.getThemeVerId());
        AeaParTheme aeaParTheme = aeaParThemeMapper.getAeaParThemeByThemeVerId(aeaParThemeVer.getThemeVerId());
        aeaHiGuide.setThemeId(aeaParThemeVer.getThemeId());
        aeaHiGuide.setStageName(aeaParStage.getStageName());
        aeaHiGuide.setThemeName(aeaParTheme.getThemeName());

        List<AeaItemBasic> originItems = aeaItemBasicService.getAeaItemBasicListByStageId(aeaHiGuide.getStageId(), null, aeaHiGuide.getProjInfoId(), rootOrgId);

        List<GuideComputedItem> guideComputedItems = new GuideItemPrivilegeComputationHandler(aeaHiGuide, rootOrgId, aeaParStage, originItems, false).compute();
        Map<String, List<GuideComputedItem>> result = guideComputedItems.stream().collect(Collectors.groupingBy(ComputedItem::getIsOptionItem));

        GuideDetailVo guideDetailVo = new GuideDetailVo();

        AeaHiGuideDetail aeaHiGuideDetail = themeChangedBefore(guideId);
        if (aeaHiGuideDetail != null) {
            guideDetailVo.setThemeChanged(true);
            guideDetailVo.setNewThemeId(aeaHiGuideDetail.getThemeId());
            guideDetailVo.setNewStageId(aeaHiGuideDetail.getStageId());
            guideDetailVo.setNewThemeName(aeaHiGuideDetail.getThemeName());
        }
        int leaderDeptCnt = aeaSolicitOrgMapper.countLeaderDeptByUserId(currentUserId);
        boolean isDeptOrg = leaderDeptCnt > 0;
        guideDetailVo.setLeaderDept(isDeptOrg);
        // 如果是审批部门
        List<OpuOmOrg> opuOmOrgs = opuOmOrgMapper.listOpuOmUserOrgByUserId(currentUserId);
        if (CollectionUtils.isNotEmpty(opuOmOrgs)) {
            guideDetailVo.setApproveOrgId(opuOmOrgs.get(0).getOrgId());
        }
        guideDetailVo.setApproveItemVerIds(aeaSolicitItemMapper.listAeaSolicitItemForDeptConfirmByUserId(currentUserId).stream().map(AeaSolicitItem::getItemVerId).collect(Collectors.toSet()));
        guideDetailVo.setAeaHiGuide(aeaHiGuide);
        guideDetailVo.setParallelItems(result.get("0"));
        guideDetailVo.setOptionItems(result.get("1"));
        return guideDetailVo;
    }

    @Override
    public AeaHiGuideDetail themeChangedBefore(String guideId) {
        List<AeaHiGuideDetail> aeaHiGuideDetails = aeaHiGuideDetailMapper.listAeaHiGuideDetailIfThemeChangedBefore(guideId);
        if (CollectionUtils.isNotEmpty(aeaHiGuideDetails)) {
            return aeaHiGuideDetails.get(0);
        }
        return null;
    }

    @Override
    public void guide(List<AeaHiGuideDetail> aeaHiGuideDetails) {
        String currentUserId = SecurityContext.getCurrentUserId();
        if (CollectionUtils.isNotEmpty(aeaHiGuideDetails)) {
            aeaHiGuideDetails.forEach(detail -> {
                AeaHiGuideDetail param = new AeaHiGuideDetail();
                param.setDetailType(detail.getDetailType());
                param.setGuideId(detail.getGuideId());
                param.setStageId(detail.getStageId());
                param.setThemeId(detail.getThemeId());
                param.setItemId(detail.getItemId());
                param.setItemVerId(detail.getItemVerId());
                AeaHiGuideDetail alreadExists = aeaHiGuideDetailMapper.getAeaHiGuideDetail(param);
                if (alreadExists != null) {
                    alreadExists.setGuideChangeAction(detail.getGuideChangeAction());
                    alreadExists.setGuideOpinion(detail.getGuideOpinion());
                    alreadExists.setGuideOrgId(detail.getGuideOrgId());
                    alreadExists.setGuideUserId(detail.getGuideUserId());
                    alreadExists.setCatalogItemVerId(detail.getCatalogItemVerId());
                    alreadExists.setDetailState(detail.getDetailState());
                    if ("2".equals(detail.getDetailState())) {
                        alreadExists.setDetailEndTime(new Date());
                    }
                    alreadExists.setModifier(currentUserId);
                    alreadExists.setModifyTime(new Date());
                    aeaHiGuideDetailMapper.updateAeaHiGuideDetail(alreadExists);
                } else {
                    detail.setGuideDetailId(UuidUtil.generateUuid());
                    detail.setCreater(currentUserId);
                    detail.setCreateTime(new Date());
                    detail.setDetailStartTime(new Date());
                    if ("2".equals(detail.getDetailState())) {
                        detail.setDetailEndTime(new Date());
                    }
                    aeaHiGuideDetailMapper.insertAeaHiGuideDetail(detail);
                }
            });
            AeaHiGuideDetail aeaHiGuideDetail = aeaHiGuideDetails.get(0);
            if (GuideDetailType.ITEM_DEPT.getValue().equals(aeaHiGuideDetail.getDetailType()) && allDeptGuideFinished(aeaHiGuideDetail.getGuideId())) {
                AeaHiGuide updateAeaHiGuide = new AeaHiGuide();
                updateAeaHiGuide.setGuideId(aeaHiGuideDetail.getGuideId());
                updateAeaHiGuide.setModifier(SecurityContext.getCurrentUserId());
                updateAeaHiGuide.setModifyTime(new Date());
                updateAeaHiGuide.setApplyState(GuideApplyState.DEPT_FINISHED.getValue());
                aeaHiGuideMapper.updateAeaHiGuide(updateAeaHiGuide);
            }
        }
    }

    /**
     * 判单所有部门辅导是否结束
     */
    private boolean allDeptGuideFinished(String guideId) {
        AeaHiGuideDetail param = new AeaHiGuideDetail();
        param.setGuideId(guideId);
        param.setDetailType(GuideDetailType.ITEM_DEPT.getValue());
        List<AeaHiGuideDetail> deptDetails = aeaHiGuideDetailMapper.listAeaHiGuideDetail(param);
        if (CollectionUtils.isEmpty(deptDetails)) {
            return false;
        }
        for (AeaHiGuideDetail detail : deptDetails) {
            if (!"2".equals(detail.getDetailState())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void solicitDept(List<AeaHiGuideDetail> aeaHiGuideDetails) {
        if (CollectionUtils.isNotEmpty(aeaHiGuideDetails)) {
            Map<String, Set<String>> solicitItemUserMap = new HashMap<>();
            List<AeaSolicitItem> aeaSolicitItems = aeaSolicitItemMapper.listAeaSolicitItemWithUserIdByItemVerIds(aeaHiGuideDetails
                    .stream().map(AeaHiGuideDetail::getItemVerId).collect(Collectors.toList()));
            aeaSolicitItems.forEach(aeaSolicitItem -> {
                Set<String> userIds = solicitItemUserMap.get(aeaSolicitItem.getItemVerId());
                if (CollectionUtils.isEmpty(userIds)) {
                    userIds = new HashSet<>();
                    userIds.add(aeaSolicitItem.getUserId());
                    solicitItemUserMap.put(aeaSolicitItem.getItemVerId(), userIds);
                } else {
                    userIds.add(aeaSolicitItem.getUserId());
                }
            });

            Assert.isTrue(aeaHiGuideDetails.size() == solicitItemUserMap.size(), "请配置实施事项对应审批部门的人员");

            aeaHiGuideDetails.forEach(detail -> {
                Set<String> userIds = solicitItemUserMap.get(detail.getItemVerId());
                if (CollectionUtils.isNotEmpty(userIds)) {
                    // 如果部门确认配置的是多人模式，这里默认先取第一个， 在事项部门打开部门辅导页面的时候通过数据库查询验证
                    detail.setGuideUserId(userIds.toArray(new String[0])[0]);
                } else {
                    log.warn("事项itemVerId: {} 没有找到对应的审批人员", detail.getItemVerId());
                }
            });

            guide(aeaHiGuideDetails);

            AeaHiGuide aeaHiGuide = new AeaHiGuide();
            aeaHiGuide.setGuideId(aeaHiGuideDetails.get(0).getGuideId());
            aeaHiGuide.setApplyState(GuideApplyState.DEPT_HANDLING.getValue());
            aeaHiGuide.setModifier(SecurityContext.getCurrentUserId());
            aeaHiGuide.setModifyTime(new Date());
            aeaHiGuideMapper.updateAeaHiGuide(aeaHiGuide);
        }
    }

    @Override
    public List<AeaHiGuide> listAeaHiGuideListUnitIdOrLinkmanInfoId(AeaHiGuide query){
        return  aeaHiGuideMapper.listAeaHiGuideListUnitIdOrLinkmanInfoId(query);
    }

    @Override
    public void finish(AeaHiGuide aeaHiGuide) {
        Assert.notNull(aeaHiGuide, "部门辅导记录不能为空");
        Assert.hasText(aeaHiGuide.getGuideId(), "guideId不能为空");
        Assert.hasText(aeaHiGuide.getLeaderOrgOpinion(), "牵头部门汇总意见不能为空");
        Assert.notNull(aeaHiGuide.getGuideEndTime(), "部门辅导结束时间不能为空");

        AeaHiGuide aeaHiGuideFromDb = aeaHiGuideMapper.getAeaHiGuideByGuideId(aeaHiGuide.getGuideId());
        aeaHiGuide.setRealTimeLimit((double) ((aeaHiGuide.getGuideEndTime().getTime() - aeaHiGuideFromDb.getGuideStartTime().getTime()) / (1000 * 3600)));

        aeaHiGuideMapper.updateAeaHiGuide(aeaHiGuide);
    }

    @Override
    public List<AeaHiGuide> getAeaHiGuideByApplyinstId(String applyinstId){
        AeaHiGuide query=new AeaHiGuide();
        query.setApplyinstId(applyinstId);
        return aeaHiGuideMapper.listAeaHiGuide(query);
    }
}
