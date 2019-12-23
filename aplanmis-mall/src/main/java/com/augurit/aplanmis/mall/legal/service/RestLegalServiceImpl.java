package com.augurit.aplanmis.mall.legal.service;


import com.augurit.aplanmis.common.domain.AeaServiceLegal;
import com.augurit.aplanmis.common.dto.ApproveProjInfoDto;
import com.augurit.aplanmis.common.mapper.AeaServiceLegalMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestLegalServiceImpl {

    @Autowired
    AeaServiceLegalMapper aeaServiceLegalMapper;

    public PageInfo<AeaServiceLegal> getLegalListByKeyword(String keyword, int pageNum, int pageSize) throws Exception {
        AeaServiceLegal aeaServiceLegal = new AeaServiceLegal();
        aeaServiceLegal.setLegalLevel(keyword);
        PageHelper.startPage(pageNum,pageSize);
        List<AeaServiceLegal> list = aeaServiceLegalMapper.listAeaServiceLegal(aeaServiceLegal);
        return new PageInfo(list);
    }
}
