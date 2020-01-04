package com.augurit.aplanmis.common.service.window;

import com.augurit.aplanmis.common.domain.AeaProjWindow;
import com.augurit.aplanmis.common.domain.AeaServiceWindow;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 项目与代办中心（本质也是窗口）代办关联表-Service服务调用接口类
 */
public interface AeaProjWindowService {
    public void saveAeaProjWindow(AeaProjWindow aeaProjWindow) throws Exception;

    public void updateAeaProjWindow(AeaProjWindow aeaProjWindow) throws Exception;

    public void deleteAeaProjWindowById(String id) throws Exception;

    public PageInfo<AeaProjWindow> listAeaProjWindow(AeaProjWindow aeaProjWindow, Page page) throws Exception;

    public AeaProjWindow getAeaProjWindowById(String id) throws Exception;

    public List<AeaProjWindow> listAeaProjWindow(AeaProjWindow aeaProjWindow) throws Exception;

    public List <AeaServiceWindow> listAeaServiceWindowByProjInfoId(String projInfoId);

}
