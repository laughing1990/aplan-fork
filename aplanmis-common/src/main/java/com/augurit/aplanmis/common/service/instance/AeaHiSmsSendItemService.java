package com.augurit.aplanmis.common.service.instance;

import com.augurit.aplanmis.common.domain.AeaHiSmsSendItem;
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
    <li>创建时间：2019-08-03 10:37:41</li>
</ul>
*/
public interface AeaHiSmsSendItemService {
    public void saveAeaHiSmsSendItem(AeaHiSmsSendItem aeaHiSmsSendItem) throws Exception;
    public void updateAeaHiSmsSendItem(AeaHiSmsSendItem aeaHiSmsSendItem) throws Exception;
    public void deleteAeaHiSmsSendItemById(String id) throws Exception;
    public PageInfo<AeaHiSmsSendItem> listAeaHiSmsSendItem(AeaHiSmsSendItem aeaHiSmsSendItem, Page page) throws Exception;
    public AeaHiSmsSendItem getAeaHiSmsSendItemById(String id) throws Exception;
    public List<AeaHiSmsSendItem> listAeaHiSmsSendItem(AeaHiSmsSendItem aeaHiSmsSendItem) throws Exception;

}
