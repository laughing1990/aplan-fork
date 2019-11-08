package com.augurit.aplanmis.admin.market.serviceLinkman.service;

import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.aplanmis.common.domain.AeaImServiceLinkman;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * -Service服务调用接口类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:thinkpad</li>
 * <li>创建时间：2019-06-12 16:11:39</li>
 * </ul>
 */
public interface AeaImServiceLinkmanService {
    void saveAeaImServiceLinkman(AeaImServiceLinkman aeaImServiceLinkman) throws Exception;

    void updateAeaImServiceLinkman(AeaImServiceLinkman aeaImServiceLinkman) throws Exception;

    void deleteAeaImServiceLinkmanById(String id) throws Exception;

    EasyuiPageInfo<AeaImServiceLinkman> listAeaImServiceLinkman(AeaImServiceLinkman aeaImServiceLinkman, Page page) throws Exception;

    List<AeaImServiceLinkman> getAeaImServiceLinkmanList(AeaImServiceLinkman aeaImServiceLinkman, Page page) throws Exception;

    AeaImServiceLinkman getAeaImServiceLinkmanById(String id) throws Exception;

    List<AeaImServiceLinkman> listAeaImServiceLinkman(AeaImServiceLinkman aeaImServiceLinkman) throws Exception;

    PageInfo<AeaLinkmanInfo> listAeaImServiceLinkmanByServiceId(String serviceId, Page page) throws Exception;

    void updateServiceLinkmanAudit(String serviceLinkmanId, String auditFlag, String auditComment) throws Exception;
}
