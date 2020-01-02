package com.augurit.aplanmis.common.service.apply.impl;

import com.augurit.aplanmis.common.domain.AeaHiGuideDetail;
import com.augurit.aplanmis.common.mapper.AeaHiGuideDetailMapper;
import com.augurit.aplanmis.common.service.apply.AeaHiGuideDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AeaHiGuideDetailServiceImpl implements AeaHiGuideDetailService {

    @Autowired
    private AeaHiGuideDetailMapper aeaHiGuideDetailMapper;

    @Override
    public void deleteByGuideDetailId(String guideDetailId) {
        aeaHiGuideDetailMapper.deleteByGuideDetailId(guideDetailId);
    }

    @Override
    public void insertAeaHiGuideDetail(AeaHiGuideDetail record) {
        aeaHiGuideDetailMapper.insertAeaHiGuideDetail(record);
    }

    @Override
    public AeaHiGuideDetail getAeaHiGuideDetailByGuideDetailId(String guideDetailId) {
        return aeaHiGuideDetailMapper.getAeaHiGuideDetailByGuideDetailId(guideDetailId);
    }

    @Override
    public void updateAeaHiGuideDetail(AeaHiGuideDetail record) {
        aeaHiGuideDetailMapper.updateAeaHiGuideDetail(record);
    }

    @Override
    public void batchInsertAeaHiGuideDetail(List<AeaHiGuideDetail> list) {
        aeaHiGuideDetailMapper.batchInsertAeaHiGuideDetail(list);
    }

}
