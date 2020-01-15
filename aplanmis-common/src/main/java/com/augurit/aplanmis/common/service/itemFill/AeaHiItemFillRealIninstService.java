package com.augurit.aplanmis.common.service.itemFill;

import com.augurit.aplanmis.common.domain.AeaHiItemFillRealIninst;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;
/**
* 事项容缺实际补齐材料实例表-Service服务调用接口类
*/
public interface AeaHiItemFillRealIninstService {
    public void saveAeaHiItemFillRealIninst(AeaHiItemFillRealIninst aeaHiItemFillRealIninst) throws Exception;
    public void updateAeaHiItemFillRealIninst(AeaHiItemFillRealIninst aeaHiItemFillRealIninst) throws Exception;
    public void deleteAeaHiItemFillRealIninstById(String id) throws Exception;
    public PageInfo<AeaHiItemFillRealIninst> listAeaHiItemFillRealIninst(AeaHiItemFillRealIninst aeaHiItemFillRealIninst, Page page) throws Exception;
    public AeaHiItemFillRealIninst getAeaHiItemFillRealIninstById(String id) throws Exception;
    public List<AeaHiItemFillRealIninst> listAeaHiItemFillRealIninst(AeaHiItemFillRealIninst aeaHiItemFillRealIninst) throws Exception;

}
