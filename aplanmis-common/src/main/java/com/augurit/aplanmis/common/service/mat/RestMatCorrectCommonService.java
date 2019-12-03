package com.augurit.aplanmis.common.service.mat;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.aplanmis.common.domain.AeaHiItemCorrect;
import com.augurit.aplanmis.common.dto.CorrectinstDto;
import com.augurit.aplanmis.common.dto.MatCorrectInfoDto;
import com.augurit.aplanmis.common.dto.MatCorrectinstDto;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Description: 材料补正公共接口/**
 * @Date 2019/8/28
 */
@Service

public interface RestMatCorrectCommonService {

    MatCorrectInfoDto getLackMatsByApplyinstIdAndIteminstId(String applyinstId, String iteminstId) throws Exception;

    void createMatCorrectinst(CorrectinstDto correctinstDto) throws Exception;

    AeaHiItemCorrect getMatCorrectInfo(String correctId) throws Exception;

    void saveMatCorrectInfo(MatCorrectinstDto matCorrectinstDto) throws Exception;

    AeaHiItemCorrect getItemCorrectRealIninst(String correctId) throws Exception;

    void matCorrectConfirm(String correctId, String correctState, String correctMemo, String matCorrectDtosJson) throws Exception;

    void delelteAttFile(String detailIds, String attRealIninstId) throws Exception;

    void uploadFile(String attRealIninstId, HttpServletRequest request) throws Exception;

    List<BscAttFileAndDir> getAttFiles(String attRealIninstId) throws Exception;



}