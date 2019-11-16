package com.augurit.aplanmis.common.service.admin.cert;

import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.aplanmis.common.domain.AeaCert;
import com.augurit.aplanmis.common.domain.AeaItemInout;
import com.augurit.aplanmis.common.domain.AeaParIn;
import com.augurit.aplanmis.integration.license.dto.LicenseAuthResDTO;
import com.augurit.aplanmis.integration.license.dto.LicenseUserInfoDTO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author ZhangXinhui
 * @date 2019/7/29 029 14:58
 * @desc
 **/
public interface AeaCertAdminService {
    /**
     * 根据id删除证照
     *
     * @param id
     */
    void deleteAeaCertById(String id);

    /**
     * 批量删除证照
     *
     * @param ids id集
     */
    void batchDeleteCertByIds(String[] ids);

    /**
     * 保存电子证照
     *
     * @param aeaCert
     */
    void saveAeaCert(AeaCert aeaCert);

    /**
     * 更新电子证照
     *
     * @param aeaCert
     */
    void updateAeaCert(AeaCert aeaCert);

    /**
     * 电子证照分页查询
     *
     * @param aeaCert
     * @param page
     * @return
     */
    PageInfo<AeaCert> listAeaCert(AeaCert aeaCert, Page page);

    /**
     * 验证电子证照存在性
     *
     * @param certId   证照id
     * @param certCode 证照编号
     * @return
     */
    boolean checkUniqueCertCode(String certId, String certCode, String rootOrgId);

    /**
     * 获取附件树
     *
     * @param orgId
     * @return
     * @
     */
    List<ZtreeNode> gtreeBscAttDir(String orgId) throws Exception;

    /**
     * 获取附件文件目录Eui格式
     *
     * @param orgId
     * @return
     */
    List<ElementUiRsTreeNode> gtreeAttDirForEui(String orgId) throws Exception;

    /**
     * 获取树结构
     *
     * @param rootOrgId
     * @return
     */
    List<ZtreeNode> gtreeTypeCert(String rootOrgId);

    /**
     * 获取排序
     *
     * @param rootOrgId
     * @return
     */
    Long getMaxCertSortNo(String rootOrgId);

    AeaCert getAeaCertById(String id);

    PageInfo<AeaCert> listStageNoSelectCertByPage(AeaParIn aeaParIn, Page page);

    /**
     * 获取阶段未选择的证照
     *
     * @param aeaParIn
     * @return
     */
    List<AeaCert> listStageNoSelectCert(AeaParIn aeaParIn);

    /**
     * 事项分页
     *
     * @param inout
     * @param page
     * @return
     */
    PageInfo<AeaCert> listItemNoSelectCertByPage(AeaItemInout inout, Page page);

    /**
     * 获取事项未选择的证照
     *
     * @param inout
     * @return
     */
    List<AeaCert> listItemNoSelectCert(AeaItemInout inout);

    /**
     * 根据事项实例ID获取输出证照定义
     *
     * @param iteminstId
     * @return
     * @throws Exception
     */
    List<AeaCert> getOutputCertsByIteminstId(String iteminstId) throws Exception;

    /**
     * 获取电子证照列表
     *
     * @param itemVerIds
     * @param identityNumber
     * @return
     * @throws Exception
     */
    LicenseAuthResDTO getLicenseAuthRes(String itemVerIds, String identityNumber, LicenseUserInfoDTO operator) throws Exception;

    /**
     * 获取电子证照
     *
     * @param authCode
     * @return
     * @throws Exception
     */
    String getViewLicenseURL(String authCode);
}
