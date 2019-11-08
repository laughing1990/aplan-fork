package com.augurit.aplanmis.common.service.instance;

import com.augurit.aplanmis.common.domain.AeaHiItemCorrectDueIninst;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 事项输入输出实例表-Service服务调用接口类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-08-03 10:29:47</li>
 * </ul>
 */
public interface AeaHiItemCorrectDueIninstService {
    public void saveAeaHiItemCorrectDueIninst(AeaHiItemCorrectDueIninst aeaHiItemCorrectDueIninst) throws Exception;

    public void batchSaveAeaHiItemCorrectDueIninst(List<AeaHiItemCorrectDueIninst> aeaHiItemCorrectDueIninsts) throws Exception;

    public void updateAeaHiItemCorrectDueIninst(AeaHiItemCorrectDueIninst aeaHiItemCorrectDueIninst) throws Exception;

    public void deleteAeaHiItemCorrectDueIninstById(String id) throws Exception;

    public PageInfo<AeaHiItemCorrectDueIninst> listAeaHiItemCorrectDueIninst(AeaHiItemCorrectDueIninst aeaHiItemCorrectDueIninst, Page page) throws Exception;

    public AeaHiItemCorrectDueIninst getAeaHiItemCorrectDueIninstById(String id) throws Exception;

    public List<AeaHiItemCorrectDueIninst> listAeaHiItemCorrectDueIninst(AeaHiItemCorrectDueIninst aeaHiItemCorrectDueIninst) throws Exception;

    public List<AeaHiItemCorrectDueIninst> getCorrectDueIninstByCorrectId(String correctId) throws Exception;

}
