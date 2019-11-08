package com.augurit.aplanmis.common.service.admin.cert;

import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.aplanmis.common.domain.AeaCertType;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author ZhangXinhui
 * @date 2019/7/29 029 11:55
 * @desc
 **/
public interface AeaCertTypeAdminService {
    /**
     * 分页查询电子证照分类
     * @param aeaCertType
     * @param page
     * @return
     */
    PageInfo<AeaCertType> listAeaCertType(AeaCertType aeaCertType, Page page);

    /**
     * 根据父节点获取最大排序号
     * @param parentId
     * @return
     */
    Long getMaxSortNo(String parentId, String rootOrgId);

    /**
     * 验证电子证照类型存在性
     * @param certTypeId 类型id
     * @param typeCode 类型编号
     * @return
     */
    boolean checkUniqueCertTypeCode(String certTypeId, String typeCode, String rootOrgId);

    /**
     * 更新证件类型对象
     * @param aeaCertType
     */
    void updateAeaCertType(AeaCertType aeaCertType);

    /**
     * 添加证件类型
     * @param aeaCertType
     */
    void saveAeaCertType(AeaCertType aeaCertType);

    /**
     * 获取类型树节点id排序
     * @param certTypeId 目标节点id
     * @param parentCertTypeId 父节点id
     * @return
     */
    String getTypeSeq(String certTypeId, String parentCertTypeId);

    /**
     * 删除证照类型
     * @param id 类型id
     */
    void deleteAeaCertTypeById(String id);

    /**
     * 批量删除证照类型
     * @param ids 类型id集
     */
    void batchDeleteCertType(String[] ids);

    /**
     * 更改证照类型可用性
     * @param id 类型id
     */
    void changIsActiveState(String id);

    AeaCertType getAeaCertTypeById(String id);

    List<ElementUiRsTreeNode> gtreeCertType(String rootOrgId);

    List<ElementUiRsTreeNode> listOtherCertTypesByCertTypeId(String certTypeId);

    AeaCertType setParentCertType(String curTypeId, String targetTypeId);
}
