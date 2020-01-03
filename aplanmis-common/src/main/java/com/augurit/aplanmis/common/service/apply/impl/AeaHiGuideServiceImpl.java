package com.augurit.aplanmis.common.service.apply.impl;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.common.apply.item.ComputedItem;
import com.augurit.aplanmis.common.apply.item.GuideComputedItem;
import com.augurit.aplanmis.common.apply.item.GuideItemPrivilegeComputationHandler;
import com.augurit.aplanmis.common.constants.ApplySource;
import com.augurit.aplanmis.common.constants.GuideApplyState;
import com.augurit.aplanmis.common.domain.AeaHiGuide;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.mapper.AeaHiGuideMapper;
import com.augurit.aplanmis.common.mapper.AeaParStageMapper;
import com.augurit.aplanmis.common.service.apply.AeaHiGuideService;
import com.augurit.aplanmis.common.service.item.AeaItemBasicService;
import com.augurit.aplanmis.common.vo.guide.GuideDetailVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Transactional
public class AeaHiGuideServiceImpl implements AeaHiGuideService {

    @Autowired
    private AeaHiGuideMapper aeaHiGuideMapper;
    @Autowired
    private AeaParStageMapper aeaParStageMapper;
    @Autowired
    private AeaItemBasicService aeaItemBasicService;

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

        List<AeaItemBasic> originItems = aeaItemBasicService.getAeaItemBasicListByStageId(aeaHiGuide.getStageId(), null, aeaHiGuide.getProjInfoId(), rootOrgId);

        List<GuideComputedItem> guideComputedItems = new GuideItemPrivilegeComputationHandler(aeaHiGuide, rootOrgId, aeaParStage, originItems, false).compute();
        Map<String, List<GuideComputedItem>> result = guideComputedItems.stream().collect(Collectors.groupingBy(ComputedItem::getIsOptionItem));

        GuideDetailVo guideDetailVo = new GuideDetailVo();
        guideDetailVo.setAeaHiGuide(aeaHiGuide);
        guideDetailVo.setParallelItems(result.get("1"));
        guideDetailVo.setOptionItems(result.get("0"));
        return guideDetailVo;
    }
}
