package com.augurit.aplanmis.supermarket.linkmanInfo.service;

import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaUnitLinkman;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 联系人表-Service服务调用接口类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:thinkpad</li>
 * <li>创建时间：2019-06-12 20:19:45</li>
 * </ul>
 */
public interface AeaLinkmanInfoService {
    public AeaLinkmanInfo insertLinkmanInfo(AeaLinkmanInfo aeaLinkmanInfo) throws Exception;

    public AeaLinkmanInfo updateLinkmanInfo(AeaLinkmanInfo aeaLinkmanInfo) throws Exception;

    public boolean saveAeaLinkmanInfo(AeaLinkmanInfo aeaLinkmanInfo) throws Exception;

    public void updateAeaLinkmanInfo(AeaLinkmanInfo aeaLinkmanInfo) throws Exception;

    public void deleteAeaLinkmanInfoById(String serviceLinkmanId, String unitServiceId, String unitServiceLinkmanId) throws Exception;

    public PageInfo<AeaLinkmanInfo> listAeaLinkmanInfo(AeaLinkmanInfo aeaLinkmanInfo, Page page) throws Exception;

    public List<AeaLinkmanInfo> getAeaLinkmanInfoById(String id) throws Exception;

    public List<AeaLinkmanInfo> getAeaLinkmanInfoByUnitInfoId(String unitInfoId, Integer isAll) throws Exception;

    public List<AeaLinkmanInfo> listAeaLinkmanInfo(AeaLinkmanInfo aeaLinkmanInfo) throws Exception;

    public AeaLinkmanInfo getOneById(String id);

    public int updateAeaUnitLinkman(AeaUnitLinkman aeaUnitLinkman);

    public void deleteUnitLinkmanInfo(AeaUnitLinkman aeaUnitLinkman);

    int batchDeleteAeaLinkmanInfo(List<String> ids) throws Exception;

    int batchInsertAeaLinkmanInfo(List<AeaLinkmanInfo> linkmanInfos) throws Exception;

    int batchInsertAeaUnintLinkman(List<AeaUnitLinkman> unitLinkmen) throws Exception;

    public AeaLinkmanInfo insertOnlyLinkmanInfo(AeaLinkmanInfo aeaLinkmanInfo) throws Exception;
}
