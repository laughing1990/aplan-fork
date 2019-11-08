package com.augurit.aplanmis.rest.userCenter.service;

import com.augurit.aplanmis.common.dto.MatCorrectinstDto;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

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

    void uploadFile(String attRealIninstId, HttpServletRequest request) throws Exception;
}