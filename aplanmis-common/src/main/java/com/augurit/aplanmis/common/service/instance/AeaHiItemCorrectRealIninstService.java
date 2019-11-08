package com.augurit.aplanmis.common.service.instance;

import com.augurit.aplanmis.common.domain.AeaHiItemCorrectRealIninst;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;
/**
* 事项输入输出实例表-Service服务调用接口类
<ul>
    <li>项目名：奥格erp3.0--第一期建设项目</li>
    <li>版本信息：v1.0</li>
    <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
    <li>创建人:Administrator</li>
    <li>创建时间：2019-08-03 10:31:32</li>
</ul>
*/
public interface AeaHiItemCorrectRealIninstService {
    public void saveAeaHiItemCorrectRealIninst(AeaHiItemCorrectRealIninst aeaHiItemCorrectRealIninst) throws Exception;
    public void updateAeaHiItemCorrectRealIninst(AeaHiItemCorrectRealIninst aeaHiItemCorrectRealIninst) throws Exception;
    public void deleteAeaHiItemCorrectRealIninstById(String id) throws Exception;
    public PageInfo<AeaHiItemCorrectRealIninst> listAeaHiItemCorrectRealIninst(AeaHiItemCorrectRealIninst aeaHiItemCorrectRealIninst, Page page) throws Exception;
    public AeaHiItemCorrectRealIninst getAeaHiItemCorrectRealIninstById(String id) throws Exception;
    public List<AeaHiItemCorrectRealIninst> listAeaHiItemCorrectRealIninst(AeaHiItemCorrectRealIninst aeaHiItemCorrectRealIninst) throws Exception;
    public List<AeaHiItemCorrectRealIninst> getCorrectRealIninstByCorrectId(String correctId) throws Exception;

}
