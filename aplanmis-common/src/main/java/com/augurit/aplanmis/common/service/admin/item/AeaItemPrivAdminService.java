package com.augurit.aplanmis.common.service.admin.item;

import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.aplanmis.common.domain.AeaItemPriv;
import com.github.pagehelper.Page;

/**
 * @author ZhangXinhui
 * @date 2019/8/5 005 16:47
 * @desc
 **/
public interface AeaItemPrivAdminService {

    EasyuiPageInfo<AeaItemPriv> getPrivListByItemId(String itemVerId, String keyword, Page page);

    void saveAeaItemPriv(AeaItemPriv aeaItemPriv)throws Exception;

    void deleteAeaItemPrivById(String id);
}
