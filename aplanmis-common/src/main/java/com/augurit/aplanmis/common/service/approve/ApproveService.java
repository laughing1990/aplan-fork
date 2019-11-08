package com.augurit.aplanmis.common.service.approve;

import com.augurit.aplanmis.common.vo.MatinstVo;

import java.util.List;

public interface ApproveService {
    /**
     * 获取并联申报材料列表
     *
     * @param applyinstId 申请实例ID
     * @param iteminstId  材料实例ID
     * @return
     * @throws Exception
     */
    List<MatinstVo> getParMatinstList(String applyinstId, String iteminstId) throws Exception;
}
