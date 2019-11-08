package com.augurit.aplanmis.common.service.form;
import com.augurit.aplanmis.common.domain.AeaExProjCertProject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import java.util.List;
/**
* 建设工程规划许可证-Service服务调用接口类
<ul>
    <li>项目名：奥格erp3.0--第一期建设项目</li>
    <li>版本信息：v1.0</li>
    <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
    <li>创建人:Administrator</li>
    <li>创建时间：2019-10-29 16:05:47</li>
</ul>
*/
public interface AeaExProjCertProjectService {
    public void saveAeaExProjCertProject(AeaExProjCertProject aeaExProjCertProject) throws Exception;
    public void updateAeaExProjCertProject(AeaExProjCertProject aeaExProjCertProject) throws Exception;
    public void deleteAeaExProjCertProjectById(String id) throws Exception;
    public PageInfo<AeaExProjCertProject> listAeaExProjCertProject(AeaExProjCertProject aeaExProjCertProject, Page page) throws Exception;
    public AeaExProjCertProject getAeaExProjCertProjectById(String id) throws Exception;
    public List<AeaExProjCertProject> listAeaExProjCertProject(AeaExProjCertProject aeaExProjCertProject) throws Exception;

}
