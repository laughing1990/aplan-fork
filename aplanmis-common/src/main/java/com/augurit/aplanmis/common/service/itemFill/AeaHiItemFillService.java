package com.augurit.aplanmis.common.service.itemFill;

import com.augurit.aplanmis.common.domain.AeaHiItemFill;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;
/**
* 事项容缺补齐实例表-Service服务调用接口类
*/
public interface AeaHiItemFillService {
    public void saveAeaHiItemFill(AeaHiItemFill aeaHiItemFill) throws Exception;
    public void updateAeaHiItemFill(AeaHiItemFill aeaHiItemFill) throws Exception;
    public void deleteAeaHiItemFillById(String id) throws Exception;
    public PageInfo<AeaHiItemFill> listAeaHiItemFill(AeaHiItemFill aeaHiItemFill, Page page) throws Exception;
    public AeaHiItemFill getAeaHiItemFillById(String id) throws Exception;
    public List<AeaHiItemFill> listAeaHiItemFill(AeaHiItemFill aeaHiItemFill) throws Exception;

    /**
     * 创建容缺补齐实例，包括补齐的详细信息
     * @param applyinstId
     * @throws Exception
     */
    public void createAeaHiItemFill(String applyinstId) throws Exception;



}
