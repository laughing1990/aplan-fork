package com.augurit.aplanmis.common.service.form;
import com.augurit.aplanmis.common.domain.AeaExProjSite;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import java.util.List;
/**
* 建设项目选址意见书-Service服务调用接口类
<ul>
    <li>项目名：奥格erp3.0--第一期建设项目</li>
    <li>版本信息：v1.0</li>
    <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
    <li>创建人:Administrator</li>
    <li>创建时间：2019-10-29 18:00:43</li>
</ul>
*/
public interface AeaExProjSiteService {
    public void saveAeaExProjSite(AeaExProjSite aeaExProjSite) throws Exception;
    public void updateAeaExProjSite(AeaExProjSite aeaExProjSite) throws Exception;
    public void deleteAeaExProjSiteById(String id) throws Exception;
    public PageInfo<AeaExProjSite> listAeaExProjSite(AeaExProjSite aeaExProjSite, Page page) throws Exception;
    public AeaExProjSite getAeaExProjSiteById(String id) throws Exception;
    public List<AeaExProjSite> listAeaExProjSite(AeaExProjSite aeaExProjSite) throws Exception;

}
