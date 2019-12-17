package com.augurit.aplanmis.common.service.admin.std;

import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.aplanmis.common.domain.AeaStdmatType;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
* 材料类型表-Service服务调用接口类
*/
public interface AeaStdmatTypeAdminService {

     void saveAeaStdmatType(AeaStdmatType aeaStdmatType) ;

     void updateAeaStdmatType(AeaStdmatType aeaStdmatType) ;

     void deleteAeaStdmatTypeById(String id) ;

     void deleteSelfAndAllChildById(String id, String rootOrgId);

     PageInfo<AeaStdmatType> listAeaStdmatType(AeaStdmatType aeaStdmatType, Page page) ;

     AeaStdmatType getAeaStdmatTypeById(String id) ;

     List<AeaStdmatType> listAeaStdmatType(AeaStdmatType aeaStdmatType) ;

     /**
      * 验证材料类型存在性
      *
      * @param matTypeId 类型id
      * @param typeCode 类型编号
      * @return
      */
     boolean checkUniqueTypeCode(String matTypeId, String typeCode);

     /**
      * 获取最大排序号
      * @return
      */
     Long getMaxSortNo(String rootOrgId);

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
     AeaStdmatType setParentMatType(String curTypeId, String targetTypeId);

     /**
      * 获取ztree树结构，分类与材料数据
      *
      * @param rootOrgId
      * @return
      */
     List<ZtreeNode> gtrStdTypeMatZtree(String rootOrgId);
}
