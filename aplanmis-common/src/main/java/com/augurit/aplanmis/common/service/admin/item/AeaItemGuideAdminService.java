package com.augurit.aplanmis.common.service.admin.item;

import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaItemGuide;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public interface AeaItemGuideAdminService {

    void saveAeaItemGuide(AeaItemGuide aeaItemGuide);

    void updateAeaItemGuide(AeaItemGuide aeaItemGuide);

    void deleteAeaItemGuideById(String id);

    void deleteAeaItemGuideByItemVerId(String itemVerId, String rootOrgId);

    PageInfo<AeaItemGuide> listAeaItemGuide(AeaItemGuide aeaItemGuide, Page page);

    AeaItemGuide getAeaItemGuideById(String id);

    List<AeaItemGuide> listAeaItemGuide(AeaItemGuide aeaItemGuide);

    void syncLocalAeaItemGuide();

    void copyItemBasicToGuide(List<AeaItemBasic> itemBasicList, String rootOrgId);

    String getBscDicCodeItemItemName(String itemCode, List<BscDicCodeItem> bscDicCodeItemList);

    void saveSampleFile(AeaItemGuide aeaItemGuide, HttpServletRequest request) throws Exception;

    void delItemGuideAtt(String itemGuideId, String type, String detailId) throws Exception;
}
