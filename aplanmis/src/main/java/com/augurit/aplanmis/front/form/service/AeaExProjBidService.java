package com.augurit.aplanmis.front.form.service;

import com.augurit.aplanmis.common.domain.AeaExProjBid;
import com.augurit.aplanmis.common.domain.AeaProjInfo;

/**
* 招投标信息-Service服务调用接口类
<ul>
    <li>项目名：奥格erp3.0--第一期建设项目</li>
    <li>版本信息：v1.0</li>
    <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
    <li>创建人:刘赵雄</li>
    <li>创建时间：2019-10-31 15:56:12</li>
</ul>
*/
public interface AeaExProjBidService {

    public void saveAeaExProjBid(AeaExProjBid aeaExProjBid) throws Exception;

    public void updateAeaExProjBid(AeaExProjBid aeaExProjBid) throws Exception;

    public AeaExProjBid getAeaExProjBidByProjId(String projId) throws Exception;

    public AeaProjInfo getProjInfoByProjId(String projId) throws Exception;

}
