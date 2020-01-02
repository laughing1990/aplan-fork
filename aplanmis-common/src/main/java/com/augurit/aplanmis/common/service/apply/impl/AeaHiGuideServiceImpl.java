package com.augurit.aplanmis.common.service.apply.impl;

import com.augurit.aplanmis.common.domain.AeaHiGuide;
import com.augurit.aplanmis.common.mapper.AeaHiGuideMapper;
import com.augurit.aplanmis.common.service.apply.AeaHiGuideService;
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

}
