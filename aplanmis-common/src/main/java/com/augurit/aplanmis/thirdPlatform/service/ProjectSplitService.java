package com.augurit.aplanmis.thirdPlatform.service;

import com.augurit.aplanmis.common.domain.AeaProjApplySplit;
import com.augurit.aplanmis.common.vo.SplitProjGetGcbmVo;

public interface ProjectSplitService {


    String getGCBM(String applySplitId) throws Exception;

    String getGCBM(SplitProjGetGcbmVo splitProjGetGcbmVo) throws Exception;

    String getGCBMFromThirdPlat(AeaProjApplySplit aeaProjApplySplit) throws Exception;
}
