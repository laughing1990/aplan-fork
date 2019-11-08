package com.augurit.aplanmis.common.service.form;
import com.augurit.aplanmis.common.domain.AeaExProjCertLand;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import java.util.List;
/**
* 建设项目用地规划许可证-Service服务调用接口类
<ul>
    <li>项目名：奥格erp3.0--第一期建设项目</li>
    <li>版本信息：v1.0</li>
    <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
    <li>创建人:Administrator</li>
    <li>创建时间：2019-10-29 19:48:12</li>
</ul>
*/
public interface AeaExProjCertLandService {
    public void saveAeaExProjCertLand(AeaExProjCertLand aeaExProjCertLand) throws Exception;
    public void updateAeaExProjCertLand(AeaExProjCertLand aeaExProjCertLand) throws Exception;
    public void deleteAeaExProjCertLandById(String id) throws Exception;
    public PageInfo<AeaExProjCertLand> listAeaExProjCertLand(AeaExProjCertLand aeaExProjCertLand, Page page) throws Exception;
    public AeaExProjCertLand getAeaExProjCertLandById(String id) throws Exception;
    public List<AeaExProjCertLand> listAeaExProjCertLand(AeaExProjCertLand aeaExProjCertLand) throws Exception;

}
