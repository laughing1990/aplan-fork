package com.augurit.aplanmis.admin.market.service.service;
import com.augurit.aplanmis.common.domain.AeaImServiceQual;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* -Service服务调用接口类
<ul>
    <li>项目名：奥格erp3.0--第一期建设项目</li>
    <li>版本信息：v1.0</li>
    <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
    <li>创建人:thinkpad</li>
    <li>创建时间：2019-06-10 13:49:01</li>
</ul>
*/
@Repository
public interface AeaImServiceQualService {
    public void saveAeaImServiceQual(AeaImServiceQual aeaImServiceQual) throws Exception;
    public void updateAeaImServiceQual(AeaImServiceQual aeaImServiceQual) throws Exception;
    public void deleteAeaImServiceQualById(String id) throws Exception;
    public void deleteServiceQualByServiceId(String serviceId, String qualId) throws Exception;
    public PageInfo<AeaImServiceQual> listAeaImServiceQual(AeaImServiceQual aeaImServiceQual, Page page) throws Exception;
    public AeaImServiceQual getAeaImServiceQualById(String id) throws Exception;
    public List<AeaImServiceQual> listAeaImServiceQual(AeaImServiceQual aeaImServiceQual) throws Exception;
    public List<AeaImServiceQual> getAeaImServiceQualListByServiceId(String serviceId) throws Exception;
}
