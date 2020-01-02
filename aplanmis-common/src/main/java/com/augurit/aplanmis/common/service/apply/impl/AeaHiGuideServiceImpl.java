package com.augurit.aplanmis.common.service.apply.impl;

import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.common.constants.ApplySource;
import com.augurit.aplanmis.common.constants.GuideApplyState;
import com.augurit.aplanmis.common.domain.AeaHiGuide;
import com.augurit.aplanmis.common.mapper.AeaHiGuideMapper;
import com.augurit.aplanmis.common.service.apply.AeaHiGuideService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@Service
@Transactional
public class AeaHiGuideServiceImpl implements AeaHiGuideService {

    @Resource
    private AeaHiGuideMapper aeaHiGuideMapper;

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

}
