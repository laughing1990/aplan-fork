package com.augurit.aplanmis.mall.userCenter.service;


import com.augurit.aplanmis.mall.userCenter.vo.AutoImportParamVo;
import com.augurit.aplanmis.mall.userCenter.vo.UploadMatReturnVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface RestApplyMatService {

    /**
     * 一件分拣功能
     */
    List<UploadMatReturnVo> saveFilesAuto(AutoImportParamVo autoImportVo, HttpServletRequest request) throws Exception;


}
