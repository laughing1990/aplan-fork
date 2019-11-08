package com.augurit.aplanmis.common.service.admin.item;

import com.augurit.aplanmis.common.domain.AeaItemGuideExtend;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public interface AeaItemGuideExtendAdminService {

    void saveAeaItemGuideExtend(AeaItemGuideExtend aeaItemGuideExtend);

    void updateAeaItemGuideExtend(AeaItemGuideExtend aeaItemGuideExtend);

    void deleteAeaItemGuideExtendById(String id);

    void deleteGuideExtendByItemVerId(String itemVerId, String rootOrgId);

    PageInfo<AeaItemGuideExtend> listAeaItemGuideExtend(AeaItemGuideExtend aeaItemGuideExtend, Page page);

    AeaItemGuideExtend getAeaItemGuideExtendById(String id);

    List<AeaItemGuideExtend> listAeaItemGuideExtend(AeaItemGuideExtend aeaItemGuideExtend);

    void saveSampleFile(AeaItemGuideExtend aeaItemGuideExtend,HttpServletRequest request) throws Exception;

    void delItemGuideExtendAtt(String itemGuideExtendId, String type, String detailId) throws Exception;
}
