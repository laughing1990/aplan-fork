package com.augurit.aplanmis.common.service.applyinst;

import com.augurit.aplanmis.common.domain.AeaHiApplyinstCorrectDueIninst;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;
/**
* 材料补全应收实例表-Service服务调用接口类
<ul>
    <li>项目名：奥格erp3.0--第一期建设项目</li>
    <li>版本信息：v1.0</li>
    <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
    <li>创建人:tiantian</li>
    <li>创建时间：2019-08-28 17:34:02</li>
</ul>
*/
public interface AeaHiApplyinstCorrectDueIninstService {
    public void saveAeaHiApplyinstCorrectDueIninst(AeaHiApplyinstCorrectDueIninst aeaHiApplyinstCorrectDueIninst) throws Exception;
    public void updateAeaHiApplyinstCorrectDueIninst(AeaHiApplyinstCorrectDueIninst aeaHiApplyinstCorrectDueIninst) throws Exception;
    public void deleteAeaHiApplyinstCorrectDueIninstById(String id) throws Exception;
    public PageInfo<AeaHiApplyinstCorrectDueIninst> listAeaHiApplyinstCorrectDueIninst(AeaHiApplyinstCorrectDueIninst aeaHiApplyinstCorrectDueIninst, Page page) throws Exception;
    public AeaHiApplyinstCorrectDueIninst getAeaHiApplyinstCorrectDueIninstById(String id) throws Exception;
    public List<AeaHiApplyinstCorrectDueIninst> listAeaHiApplyinstCorrectDueIninst(AeaHiApplyinstCorrectDueIninst aeaHiApplyinstCorrectDueIninst) throws Exception;
    public void batchSaveAeaHiApplyinstCorrectDueIninst(List<AeaHiApplyinstCorrectDueIninst> aeaHiApplyinstCorrectDueIninsts) throws Exception;

    public List<AeaHiApplyinstCorrectDueIninst> getCorrectDueIninstByApplyinstCorrectId(String applyinstCorrectId) throws Exception;
}
