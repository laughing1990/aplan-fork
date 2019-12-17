package com.augurit.aplanmis.common.service.admin.std;

import com.augurit.aplanmis.common.domain.AeaStdmat;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
* 标准材料定义表-Service服务调用接口类
*/
public interface AeaStdmatAdminService {

     void saveAeaStdmat(AeaStdmat aeaStdmat);

     void updateAeaStdmat(AeaStdmat aeaStdmat);

     void deleteAeaStdmatById(String id);

     PageInfo<AeaStdmat> listAeaStdmat(AeaStdmat aeaStdmat, Page page);

     AeaStdmat getAeaStdmatById(String id);

     List<AeaStdmat> listAeaStdmat(AeaStdmat aeaStdmat);

     /**
      * 验证材料存在性
      *
      * @param stdmatId 类型id
      * @param stdmatCode 类型编号
      * @return
      */
     boolean checkUniqueCode(String stdmatId, String stdmatCode);

     /**
      * 获取最大排序号
      * @return
      */
     Long getMaxSortNo(String rootOrgId);
}
