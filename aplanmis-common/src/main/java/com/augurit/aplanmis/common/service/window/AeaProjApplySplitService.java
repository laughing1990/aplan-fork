package com.augurit.aplanmis.common.service.window;

import com.augurit.aplanmis.common.domain.AeaProjApplySplit;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;
/**
* 项目拆分申请表-Service服务调用接口类
*/
public interface AeaProjApplySplitService {

    void saveAeaProjApplySplit(AeaProjApplySplit aeaProjApplySplit) throws Exception;

    void updateAeaProjApplySplit(AeaProjApplySplit aeaProjApplySplit) throws Exception;

    void deleteAeaProjApplySplitById(String id) throws Exception;

    PageInfo<AeaProjApplySplit> listAeaProjApplySplit(AeaProjApplySplit aeaProjApplySplit, Page page) throws Exception;

    AeaProjApplySplit getAeaProjApplySplitById(String id) throws Exception;

    List<AeaProjApplySplit> listAeaProjApplySplit(AeaProjApplySplit aeaProjApplySplit) throws Exception;

    List<AeaProjApplySplit> listSplitedProjInfo(String projInfoId);

    void passed(String applySplitId, String reason) throws Exception;

    void rejected(String applySplitId, String reason) throws Exception;
}
