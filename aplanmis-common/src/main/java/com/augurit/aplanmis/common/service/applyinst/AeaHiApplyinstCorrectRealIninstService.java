package com.augurit.aplanmis.common.service.applyinst;

import com.augurit.aplanmis.common.domain.AeaHiApplyinstCorrectRealIninst;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;
/**
* 材料补全已收实例表-Service服务调用接口类
<ul>
    <li>项目名：奥格erp3.0--第一期建设项目</li>
    <li>版本信息：v1.0</li>
    <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
    <li>创建人:tiantian</li>
    <li>创建时间：2019-08-28 17:34:16</li>
</ul>
*/
public interface AeaHiApplyinstCorrectRealIninstService {
    public void saveAeaHiApplyinstCorrectRealIninst(AeaHiApplyinstCorrectRealIninst aeaHiApplyinstCorrectRealIninst) throws Exception;
    public void updateAeaHiApplyinstCorrectRealIninst(AeaHiApplyinstCorrectRealIninst aeaHiApplyinstCorrectRealIninst) throws Exception;
    public void deleteAeaHiApplyinstCorrectRealIninstById(String id) throws Exception;
    public PageInfo<AeaHiApplyinstCorrectRealIninst> listAeaHiApplyinstCorrectRealIninst(AeaHiApplyinstCorrectRealIninst aeaHiApplyinstCorrectRealIninst, Page page) throws Exception;
    public AeaHiApplyinstCorrectRealIninst getAeaHiApplyinstCorrectRealIninstById(String id) throws Exception;
    public List<AeaHiApplyinstCorrectRealIninst> listAeaHiApplyinstCorrectRealIninst(AeaHiApplyinstCorrectRealIninst aeaHiApplyinstCorrectRealIninst) throws Exception;
    public List<AeaHiApplyinstCorrectRealIninst> getCorrectRealIninstByApplyinstCorrectId(String applyinstCorrectId) throws Exception;
}
