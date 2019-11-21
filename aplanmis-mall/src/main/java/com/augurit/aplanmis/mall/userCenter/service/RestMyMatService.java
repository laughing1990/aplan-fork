package com.augurit.aplanmis.mall.userCenter.service;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.aplanmis.common.vo.MyMatFilesVo;
import com.github.pagehelper.PageInfo;

public interface RestMyMatService {

    PageInfo<BscAttFileAndDir> getMyMatListByUser(String unitInfoId, String userInfoId, String keyword, int pageNum, int pageSize)throws Exception;

    PageInfo<MyMatFilesVo> getMyMatListByUser1(String unitInfoId, String userInfoId, String keyword, int pageNum, int pageSize)throws Exception;

}
