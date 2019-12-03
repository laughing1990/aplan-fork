package com.augurit.aplanmis.mall.userCenter.service;

import com.augurit.agcloud.bsc.domain.BscAttLink;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.domain.AeaHiItemCorrectRealIninst;
import com.augurit.aplanmis.common.dto.MatCorrectinstDto;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * Description: 材料补正接口
 */
@Service

public interface RestMatSupplyService {
    void saveMatCorrectInfo(MatCorrectinstDto matCorrectinstDto) throws Exception;
    /**
     * 材料删除
     *
     * @param detailIds
     * @throws Exception
     */
    void delelteAttFile(String detailIds, String attRealIninstId) throws Exception;

    /**
     * 材料补正材料上传
     * @param attRealIninstId
     * @param request
     * @throws Exception
     */
    void uploadFile(String attRealIninstId, HttpServletRequest request) throws Exception ;

    /**
     * 材料补正材料上传(从材料库或云盘)
     * @param attRealIninstId
     * @param detailIds
     * @throws Exception
     */
    void uploadFileByCloud(String attRealIninstId, String  detailIds) throws Exception;


}