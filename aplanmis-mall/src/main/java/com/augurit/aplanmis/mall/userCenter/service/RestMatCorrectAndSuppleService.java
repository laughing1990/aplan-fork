package com.augurit.aplanmis.mall.userCenter.service;

import com.augurit.aplanmis.common.dto.MatCorrectAndSuppleDto;
import com.augurit.aplanmis.common.dto.SupplementInfoDto;
import com.github.pagehelper.PageInfo;

public interface RestMatCorrectAndSuppleService {

    PageInfo<MatCorrectAndSuppleDto> getCorrectAndSuppleList(String unitInfoId, String userId, String keyword, int pageNum, int pageSize) throws Exception;

}
