package com.augurit.aplanmis.common.service.instance;

import com.augurit.aplanmis.common.domain.AeaHiSmsSendApply;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;
/**
* -Service服务调用接口类
<ul>
    <li>项目名：奥格erp3.0--第一期建设项目</li>
    <li>版本信息：v1.0</li>
    <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
    <li>创建人:Administrator</li>
    <li>创建时间：2019-08-03 10:36:39</li>
</ul>
*/
public interface AeaHiSmsSendApplyService {
    public void saveAeaHiSmsSendApply(AeaHiSmsSendApply aeaHiSmsSendApply) throws Exception;
    public void updateAeaHiSmsSendApply(AeaHiSmsSendApply aeaHiSmsSendApply) throws Exception;
    public void deleteAeaHiSmsSendApplyById(String id) throws Exception;
    public PageInfo<AeaHiSmsSendApply> listAeaHiSmsSendApply(AeaHiSmsSendApply aeaHiSmsSendApply, Page page) throws Exception;
    public AeaHiSmsSendApply getAeaHiSmsSendApplyById(String id) throws Exception;
    public List<AeaHiSmsSendApply> listAeaHiSmsSendApply(AeaHiSmsSendApply aeaHiSmsSendApply) throws Exception;

}
