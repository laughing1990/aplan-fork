package com.augurit.aplanmis.supermarket.serviceLinkman.service;

import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.aplanmis.common.domain.AeaImServiceLinkman;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.github.pagehelper.Page;

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
    public void saveAeaImServiceLinkman(AeaImServiceLinkman aeaImServiceLinkman) throws Exception;

    public void updateAeaImServiceLinkman(AeaImServiceLinkman aeaImServiceLinkman) throws Exception;

    public void deleteAeaImServiceLinkmanById(String id) throws Exception;

    public EasyuiPageInfo<AeaImServiceLinkman> listAeaImServiceLinkman(AeaImServiceLinkman aeaImServiceLinkman, Page page) throws Exception;

    public AeaImServiceLinkman getAeaImServiceLinkmanById(String id) throws Exception;

    public List<AeaImServiceLinkman> listAeaImServiceLinkman(AeaImServiceLinkman aeaImServiceLinkman) throws Exception;

    public EasyuiPageInfo<AeaLinkmanInfo> listAeaImServiceLinkmanByServiceId(String serviceId, Page page) throws Exception;

    public EasyuiPageInfo<AeaLinkmanInfo> listAeaImServiceLinkmanByUnitInfoId(String unitInfoId, String cardNo, String auditFlag, Page page) throws Exception;
}
