package com.augurit.aplanmis.common.service.apply.impl;

import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.apply.item.ComputedItem;
import com.augurit.aplanmis.common.apply.item.GuideComputedItem;
import com.augurit.aplanmis.common.apply.item.GuideItemPrivilegeComputationHandler;
import com.augurit.aplanmis.common.constants.ApplySource;
import com.augurit.aplanmis.common.constants.GuideApplyState;
import com.augurit.aplanmis.common.domain.AeaHiGuide;
import com.augurit.aplanmis.common.domain.AeaHiGuideDetail;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.domain.AeaParThemeVer;
import com.augurit.aplanmis.common.domain.AeaSolicitItem;
import com.augurit.aplanmis.common.mapper.AeaHiGuideDetailMapper;
import com.augurit.aplanmis.common.mapper.AeaHiGuideMapper;
import com.augurit.aplanmis.common.mapper.AeaParStageMapper;
import com.augurit.aplanmis.common.mapper.AeaParThemeVerMapper;
import com.augurit.aplanmis.common.mapper.AeaSolicitItemMapper;
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
import java.util.List;
import java.util.Map;
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
    private AeaSolicitItemMapper aeaSolicitItemMapper;

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
        });
        return new PageInfo<>(aeaHiGuides);
    }

    @Override
    public GuideDetailVo detail(String guideId) throws Exception {
        String rootOrgId = SecurityContext.getCurrentOrgId();
        AeaHiGuide aeaHiGuide = aeaHiGuideMapper.getAeaHiGuideByGuideId(guideId);

        AeaParStage aeaParStage = aeaParStageMapper.getAeaParStageById(aeaHiGuide.getStageId());
        AeaParThemeVer aeaParThemeVer = aeaParThemeVerMapper.getAeaParThemeVerById(aeaParStage.getThemeVerId());
        aeaHiGuide.setThemeId(aeaParThemeVer.getThemeId());

        List<AeaItemBasic> originItems = aeaItemBasicService.getAeaItemBasicListByStageId(aeaHiGuide.getStageId(), null, aeaHiGuide.getProjInfoId(), rootOrgId);

        List<GuideComputedItem> guideComputedItems = new GuideItemPrivilegeComputationHandler(aeaHiGuide, rootOrgId, aeaParStage, originItems, false).compute();
        Map<String, List<GuideComputedItem>> result = guideComputedItems.stream().collect(Collectors.groupingBy(ComputedItem::getIsOptionItem));

        GuideDetailVo guideDetailVo = new GuideDetailVo();

        AeaHiGuideDetail aeaHiGuideDetail = themeChangedBefore(guideId);
        if (aeaHiGuideDetail != null) {
            guideDetailVo.setThemeChanged(true);
            guideDetailVo.setNewThemeId(aeaHiGuideDetail.getThemeId());
            guideDetailVo.setNewStageId(aeaHiGuideDetail.getStageId());
        }
        guideDetailVo.setLeaderDept(SecurityContext.getCurrentUserId().equals(aeaHiGuide.getLeaderUserId()));
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
        }
    }

    @Override
    public void solicitDept(List<AeaHiGuideDetail> aeaHiGuideDetails) {
        if (CollectionUtils.isNotEmpty(aeaHiGuideDetails)) {
            Map<String, String> solicitItemUserMap = aeaSolicitItemMapper.listAeaSolicitItemWithUserIdByItemVerIds(aeaHiGuideDetails
                    .stream().map(AeaHiGuideDetail::getItemVerId).collect(Collectors.toList())
            )
                    .stream().collect(Collectors.toMap(AeaSolicitItem::getItemVerId, AeaSolicitItem::getUserId));
            Assert.isTrue(aeaHiGuideDetails.size() == solicitItemUserMap.size(), "请配置实施事项对应审批部门的人员");

            aeaHiGuideDetails.forEach(detail -> {
                String userId = solicitItemUserMap.get(detail.getItemVerId());
                if (StringUtils.isNotBlank(userId)) {
                    detail.setGuideUserId(userId);
                } else {
                    log.warn("事项itemVerId: {} 没有找到对应的审批人员", detail.getItemVerId());
                }
            });

            guide(aeaHiGuideDetails);

            AeaHiGuide aeaHiGuide = aeaHiGuideMapper.getAeaHiGuideByGuideId(aeaHiGuideDetails.get(0).getGuideId());
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
}
