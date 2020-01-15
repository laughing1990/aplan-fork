package com.augurit.aplanmis.common.service.itemFill;

import com.augurit.aplanmis.common.domain.AeaHiItemFillDueIninst;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;
/**
* 事项容缺要求补齐材料实例表-Service服务调用接口类
*/
public interface AeaHiItemFillDueIninstService {
    public void saveAeaHiItemFillDueIninst(AeaHiItemFillDueIninst aeaHiItemFillDueIninst) throws Exception;
    public void updateAeaHiItemFillDueIninst(AeaHiItemFillDueIninst aeaHiItemFillDueIninst) throws Exception;
    public void deleteAeaHiItemFillDueIninstById(String id) throws Exception;
    public PageInfo<AeaHiItemFillDueIninst> listAeaHiItemFillDueIninst(AeaHiItemFillDueIninst aeaHiItemFillDueIninst, Page page) throws Exception;
    public AeaHiItemFillDueIninst getAeaHiItemFillDueIninstById(String id) throws Exception;
    public List<AeaHiItemFillDueIninst> listAeaHiItemFillDueIninst(AeaHiItemFillDueIninst aeaHiItemFillDueIninst) throws Exception;

}
