package com.augurit.aplanmis.common.service.form;

import com.augurit.aplanmis.common.vo.AeaProjDrawing;
import com.augurit.aplanmis.common.vo.AeaProjDrawingVo;

import java.util.List;

public interface AeaProjDrawingSerivce {

    List<AeaProjDrawing> getAeaProjDrawing(String projInfoId);

    void saveAeaProjDrawing(AeaProjDrawingVo aeaProjDrawingVo);
}
