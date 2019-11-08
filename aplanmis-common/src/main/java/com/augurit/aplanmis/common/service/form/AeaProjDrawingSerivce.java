package com.augurit.aplanmis.common.service.form;

import com.augurit.aplanmis.common.vo.AeaProjDrawing;

import java.util.List;

public interface AeaProjDrawingSerivce {

    List<AeaProjDrawing> getAeaProjDrawing(String projInfoId);

    void saveAeaProjDrawing(List<AeaProjDrawing> drawings);
}
