package com.augurit.aplanmis.common.service.applyinst;

import com.augurit.aplanmis.common.domain.AeaHiApplyinstCorrectStateHist;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;
/**
* 事项输入输出实例表-Service服务调用接口类
<ul>
    <li>项目名：奥格erp3.0--第一期建设项目</li>
    <li>版本信息：v1.0</li>
    <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
    <li>创建人:tiantian</li>
    <li>创建时间：2019-08-28 17:34:26</li>
</ul>
*/
public interface AeaHiApplyinstCorrectStateHistService {
    public void saveAeaHiApplyinstCorrectStateHist(AeaHiApplyinstCorrectStateHist aeaHiApplyinstCorrectStateHist) throws Exception;
    public void updateAeaHiApplyinstCorrectStateHist(AeaHiApplyinstCorrectStateHist aeaHiApplyinstCorrectStateHist) throws Exception;
    public void deleteAeaHiApplyinstCorrectStateHistById(String id) throws Exception;
    public PageInfo<AeaHiApplyinstCorrectStateHist> listAeaHiApplyinstCorrectStateHist(AeaHiApplyinstCorrectStateHist aeaHiApplyinstCorrectStateHist, Page page) throws Exception;
    public AeaHiApplyinstCorrectStateHist getAeaHiApplyinstCorrectStateHistById(String id) throws Exception;
    public List<AeaHiApplyinstCorrectStateHist> listAeaHiApplyinstCorrectStateHist(AeaHiApplyinstCorrectStateHist aeaHiApplyinstCorrectStateHist) throws Exception;

}
