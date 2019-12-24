package com.augurit.aplanmis.mall.userCenter.service.impl;

import com.augurit.aplanmis.common.dto.MatCorrectAndSuppleDto;
import com.augurit.aplanmis.common.mapper.AeaHiItemCorrectMapper;
import com.augurit.aplanmis.mall.userCenter.service.RestMatCorrectAndSuppleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestCorrectAndSuppleServiceImpl implements RestMatCorrectAndSuppleService {


    @Autowired
    AeaHiItemCorrectMapper aeaHiItemCorrectMappera;

    @Override
    public PageInfo<MatCorrectAndSuppleDto> getCorrectAndSuppleList(String unitInfoId, String userId, String keyword, int pageNum, int pageSize)  {
        PageHelper.startPage(pageNum,pageSize);
        List<MatCorrectAndSuppleDto> list = aeaHiItemCorrectMappera.searchMatSupportAndCompletByUser(unitInfoId,userId,keyword);
        return new PageInfo<>(list);
    }
}
