package com.augurit.aplanmis.common.service.form.impl;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.aplanmis.common.domain.AeaExProjCertProject;
import com.augurit.aplanmis.common.mapper.AeaExProjCertProjectMapper;
import com.augurit.aplanmis.common.service.form.AeaExProjCertProjectService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
/**
* 建设工程规划许可证-Service服务接口实现类

*/
@Service
@Transactional
public class AeaExProjCertProjectServiceImpl implements AeaExProjCertProjectService {

    private static Logger logger = LoggerFactory.getLogger(AeaExProjCertProjectServiceImpl.class);

    @Autowired
    private AeaExProjCertProjectMapper aeaExProjCertProjectMapper;

    public void saveAeaExProjCertProject(AeaExProjCertProject aeaExProjCertProject) throws Exception{

        aeaExProjCertProject.setCreater(SecurityContext.getCurrentUser().getUserName());
        aeaExProjCertProject.setCreateTime(new Date());
        aeaExProjCertProject.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaExProjCertProjectMapper.insertAeaExProjCertProject(aeaExProjCertProject);
    }
    public void updateAeaExProjCertProject(AeaExProjCertProject aeaExProjCertProject) throws Exception{
        aeaExProjCertProjectMapper.updateAeaExProjCertProject(aeaExProjCertProject);
    }
    public void deleteAeaExProjCertProjectById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        aeaExProjCertProjectMapper.deleteAeaExProjCertProject(id);
    }
    public PageInfo<AeaExProjCertProject> listAeaExProjCertProject(AeaExProjCertProject aeaExProjCertProject,Page page) throws Exception{
        PageHelper.startPage(page);
        List<AeaExProjCertProject> list = aeaExProjCertProjectMapper.listAeaExProjCertProject(aeaExProjCertProject);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaExProjCertProject>(list);
    }
    public AeaExProjCertProject getAeaExProjCertProjectById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaExProjCertProjectMapper.getAeaExProjCertProjectById(id);
    }
    public List<AeaExProjCertProject> listAeaExProjCertProject(AeaExProjCertProject aeaExProjCertProject) throws Exception{
        List<AeaExProjCertProject> list = aeaExProjCertProjectMapper.listAeaExProjCertProject(aeaExProjCertProject);
        logger.debug("成功执行查询list！！");
        return list;
    }
}

