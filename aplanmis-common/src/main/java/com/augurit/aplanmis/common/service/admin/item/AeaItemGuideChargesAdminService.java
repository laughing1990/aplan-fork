package com.augurit.aplanmis.common.service.admin.item;

import com.augurit.aplanmis.common.domain.AeaItemGuideCharges;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface AeaItemGuideChargesAdminService {

     /**
      * 保存
      * @param aeaItemGuideCharges
      */
     void saveAeaItemGuideCharges(AeaItemGuideCharges aeaItemGuideCharges) ;

     /**
      * 更新
      * @param aeaItemGuideCharges
      */
     void updateAeaItemGuideCharges(AeaItemGuideCharges aeaItemGuideCharges) ;

     /**
      * 删除
      * @param id
      */
     void deleteAeaItemGuideChargesById(String id) ;

     /**
      * 获取
      *
      * @param id
      * @return
      */
     AeaItemGuideCharges getAeaItemGuideChargesById(String id) ;

     /**
      * 获取列表数据
      *
      * @param aeaItemGuideCharges
      * @return
      */
     List<AeaItemGuideCharges> listAeaItemGuideCharges(AeaItemGuideCharges aeaItemGuideCharges) ;

     /**
      * 分页获取数据
      *
      * @param aeaItemGuideCharges
      * @param page
      * @return
      */
     PageInfo<AeaItemGuideCharges> listAeaItemGuideCharges(AeaItemGuideCharges aeaItemGuideCharges, Page page) ;

     /**
      * 通过事项版本id删除
      *
      * @param itemVerId
      */
     void batchDeleteGuideChargesByItemVerId(String itemVerId, String rootOrgId) ;

     /**
      * 批量删除
      *
      * @param ids
      */
     void batchDelAeaItemGuideChargesByIds(String[] ids);

     /**
      * 获取某事项版本下最大编号
      *
      * @param itemVerId
      * @return
      */
     Long getMaxSortNoByItemVerId(String itemVerId, String rootOrgId);
}
