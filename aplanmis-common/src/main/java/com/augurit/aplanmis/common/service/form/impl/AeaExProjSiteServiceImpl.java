package com.augurit.aplanmis.common.service.form.impl;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.aplanmis.common.domain.AeaExProjSite;
import com.augurit.aplanmis.common.mapper.AeaExProjSiteMapper;
import com.augurit.aplanmis.common.service.form.AeaExProjSiteService;
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
* 建设项目选址意见书-Service服务接口实现类

*/
@Service
@Transactional
public class AeaExProjSiteServiceImpl implements AeaExProjSiteService {

    private static Logger logger = LoggerFactory.getLogger(AeaExProjSiteServiceImpl.class);

    @Autowired
    private AeaExProjSiteMapper aeaExProjSiteMapper;

    public void saveAeaExProjSite(AeaExProjSite aeaExProjSite) throws Exception{
        aeaExProjSite.setCreater(SecurityContext.getCurrentUser().getUserName());
        aeaExProjSite.setCreateTime(new Date());
        aeaExProjSite.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaExProjSiteMapper.insertAeaExProjSite(aeaExProjSite);
    }
    public void updateAeaExProjSite(AeaExProjSite aeaExProjSite) throws Exception{
        aeaExProjSite.setModifier(SecurityContext.getCurrentUser().getUserName());
        aeaExProjSite.setModifyTime(new Date());
        aeaExProjSiteMapper.updateAeaExProjSite(aeaExProjSite);
    }
    public void deleteAeaExProjSiteById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        aeaExProjSiteMapper.deleteAeaExProjSite(id);
    }
    public PageInfo<AeaExProjSite> listAeaExProjSite(AeaExProjSite aeaExProjSite,Page page) throws Exception{
        PageHelper.startPage(page);
        List<AeaExProjSite> list = aeaExProjSiteMapper.listAeaExProjSite(aeaExProjSite);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaExProjSite>(list);
    }
    public AeaExProjSite getAeaExProjSiteById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaExProjSiteMapper.getAeaExProjSiteById(id);
    }
    public List<AeaExProjSite> listAeaExProjSite(AeaExProjSite aeaExProjSite) throws Exception{
        List<AeaExProjSite> list = aeaExProjSiteMapper.listAeaExProjSite(aeaExProjSite);
        logger.debug("成功执行查询list！！");
        return list;
    }
}

