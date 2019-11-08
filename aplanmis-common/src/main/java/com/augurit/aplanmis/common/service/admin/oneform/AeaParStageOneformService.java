package com.augurit.aplanmis.common.service.admin.oneform;

import com.augurit.agcloud.bpm.common.domain.ActStoForm;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.aplanmis.common.domain.AeaOneform;
import com.augurit.aplanmis.common.domain.AeaParStageItem;
import com.augurit.aplanmis.common.domain.AeaParStageOneform;
import com.augurit.aplanmis.common.vo.ActStoFormVo;
import com.github.pagehelper.Page;

import java.util.List;

public interface AeaParStageOneformService {

    void saveAeaParStageOneform(AeaParStageOneform aeaParStageOneform);
    void updateAeaParStageOneform(AeaParStageOneform aeaParStageOneform);
    void deleteAeaParStageOneformById(String id);
    EasyuiPageInfo<AeaParStageOneform> getAeaParStageOneformList(String parStageId, Page page);
    EasyuiPageInfo<AeaParStageItem> getItemFormlist(String parStageId, Page page);
    EasyuiPageInfo<AeaOneform> getAeaParOneformList(AeaOneform aeaParOneform, Page page);
    void addParStageOneform(String parStageId, String oneformId);
    AeaParStageOneform getAeaParStageOneformById(String id);
    EasyuiPageInfo<ActStoFormVo> getActStoFormList(ActStoForm actStoForm, Page page);
    List<AeaParStageItem> getAeaParStageItemListNoPage(AeaParStageItem aeaParStageItem);
}
