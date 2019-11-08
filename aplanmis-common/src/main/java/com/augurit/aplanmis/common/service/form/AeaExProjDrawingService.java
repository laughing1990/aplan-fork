package com.augurit.aplanmis.common.service.form;
import com.augurit.aplanmis.common.domain.AeaExProjDrawing;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import java.util.List;
/**
* 施工图审查信息-Service服务调用接口类

*/
public interface AeaExProjDrawingService {
    public void saveAeaExProjDrawing(AeaExProjDrawing aeaExProjDrawing) throws Exception;
    public void updateAeaExProjDrawing(AeaExProjDrawing aeaExProjDrawing) throws Exception;
    public void deleteAeaExProjDrawingById(String id) throws Exception;
    public PageInfo<AeaExProjDrawing> listAeaExProjDrawing(AeaExProjDrawing aeaExProjDrawing,Page page) throws Exception;
    public AeaExProjDrawing getAeaExProjDrawingById(String id) throws Exception;
    public List<AeaExProjDrawing> listAeaExProjDrawing(AeaExProjDrawing aeaExProjDrawing) throws Exception;

}
