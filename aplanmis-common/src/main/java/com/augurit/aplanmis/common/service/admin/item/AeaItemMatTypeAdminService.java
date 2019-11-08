package com.augurit.aplanmis.common.service.admin.item;

import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.aplanmis.common.domain.AeaItemMatType;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author ZhangXinhui
 * @date 2019/7/26 026 14:16
 * @desc
 **/
public interface AeaItemMatTypeAdminService {
    /**
     * 获取材料类型列表
     * @param aeaItemMatType
     * @return
     */
    List<ZtreeNode> getListMatTypeZtreeNode(AeaItemMatType aeaItemMatType) ;

    /**
     * 获取材料分类
     * @param id 分类id
     * @return
     */
    AeaItemMatType getAeaItemMatTypeById(String id);

    /**
     * 材料分类定义分页查询
     * @param aeaItemMatType
     * @param page
     * @return
     * @throws Exception
     */
    PageInfo<AeaItemMatType> listAeaItemMatType(AeaItemMatType aeaItemMatType, Page page) throws Exception;

    /**
     * 获取材料类型树节点列表
     * @return
     * @throws Exception
     */
    List<ZtreeNode> gtreeMatType() ;

    /**
     * 获取材料类型EUi树节点列表
     *
     * @return
     */
    List<ElementUiRsTreeNode> gtreeMatTypeForEUi();

    /**
     * 验证材料类型存在性
     * @param matTypeId 类型id
     * @param typeCode 类型编号
     * @return
     */
    boolean checkUniqueTypeCode(String matTypeId, String typeCode);

    /**
     * 更新材料类型
     * @param aeaItemMatType
     */
    void updateAeaItemMatType(AeaItemMatType aeaItemMatType);

    /**
     * 新增材料类型
     * @param aeaItemMatType
     */
    void saveAeaItemMatType(AeaItemMatType aeaItemMatType);

    /**
     * 获取最大排序号
     * @return
     */
    Long getMaxSortNo();

    /**
     * 删除材料类型
     * @param id 材料id
     */
    void deleteAeaItemMatTypeById(String id);

    /**
     * 批量删除材料类型
     * @param ids
     */
    void batchDeleteAeaItemMatType(String[] ids);

    /**
     * 获取某组织下的所有材料类型（不包括自己以及包含的自己子类型）
     *
     * @param matTypeId
     * @return
     */
    List<ElementUiRsTreeNode> listOtherMatTypesByMatTypeId(String matTypeId);

    /**
     * 设置父级类型
     *
     * @param curTypeId
     * @param targetTypeId
     * @return
     */
    AeaItemMatType setParentMatType(String curTypeId, String targetTypeId);
}
