package com.augurit.aplanmis.common.service.admin.item;

import com.augurit.aplanmis.common.domain.AeaItemStateForm;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * AeaItemStateFormAdminService class
 *
 * @author jjt
 * @date 2019/4/21
 */
public interface AeaItemStateFormAdminService {

     /**
      * 保存
      *
      * @param aeaItemStateForm
      */
     void saveAeaItemStateForm(AeaItemStateForm aeaItemStateForm);

     /**
      * 更新
      *
      * @param aeaItemStateForm
      */
     void updateAeaItemStateForm(AeaItemStateForm aeaItemStateForm);

     /**
      * 通过id获取
      *
      * @param id
      * @return
      */
     AeaItemStateForm getAeaItemStateFormById(String id);

     /**
      *通过id删除
      *
      * @param id
      */
     void deleteAeaItemStateFormById(String id);

     /**
      * 批量删除
      *
      * @param ids
      */
     void batchDelAeaItemStateFormByIds(String[] ids);

     /**
      * 获取list列表
      *
      * @param aeaItemStateForm
      * @return
      */
     List<AeaItemStateForm> listAeaItemStateForm(AeaItemStateForm aeaItemStateForm);

     /**
      * 分页获取list列表
      *
      * @param aeaItemStateForm
      * @param page
      * @return
      */
     PageInfo<AeaItemStateForm> listAeaItemStateForm(AeaItemStateForm aeaItemStateForm, Page page);

     /**
      * 分页获取当前机构表单数据
      *
      * @param form
      * @param page
      * @return
      */
     PageInfo<AeaItemStateForm> listCurOrgFormByPage(AeaItemStateForm form, Page page);

     /**
      * 保存情形表单数据
      *
      * @param itemVerId
      * @param itemStateVerId
      * @param isStateForm
      * @param itemStateId
      * @param formIds
      */
     void saveAeaItemStateForms(String itemVerId, String itemStateVerId, String isStateForm, String itemStateId, String[] formIds);

     void saveAeaItemStateFormsAndNotDelOld(String itemVerId, String itemStateVerId, String isStateForm, String itemStateId, String[] formIds);

     /**
      * 获取最大编号
      *
      * @return
      */
     Long getMaxSortNo(String itemVerId, String itemStateVerId);

     List<AeaItemStateForm> listItemNoSelectForm(AeaItemStateForm form);

     PageInfo<AeaItemStateForm> listItemNoSelectFormByPage(AeaItemStateForm form, Page page);
}
