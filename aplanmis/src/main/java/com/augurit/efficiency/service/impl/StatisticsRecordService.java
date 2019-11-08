package com.augurit.efficiency.service.impl;

import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.common.domain.AeaAnaStatisticsRecord;
import com.augurit.aplanmis.common.mapper.AeaAnaStatisticsRecordMapper;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zhangwn on 2019/10/15.
 */
@Service
@Transactional
public class StatisticsRecordService {

    @Autowired
    private AeaAnaStatisticsRecordMapper aeaAnaStatisticsRecordMapper;


    public List<AeaAnaStatisticsRecord> statisticsRecordService(String keyword, Page page){
        PageHelper.startPage(page);
        List<AeaAnaStatisticsRecord> list = aeaAnaStatisticsRecordMapper.listStatisticsRecord(keyword);
        return list;
    }
}
