package com.augurit.aplanmis.common.service.admin.solicit.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaSolicitOrg;
import com.augurit.aplanmis.common.mapper.AeaSolicitOrgMapper;
import com.augurit.aplanmis.common.service.admin.solicit.AeaSolicitOrgService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
/**
* 按组织征求配置表-Service服务接口实现类
*/
@Service
@Transactional
public class AeaSolicitOrgServiceImpl implements AeaSolicitOrgService {

    private static Logger logger = LoggerFactory.getLogger(AeaSolicitOrgServiceImpl.class);

    @Autowired
    private AeaSolicitOrgMapper aeaSolicitOrgMapper;

    @Override
    public void saveAeaSolicitOrg(AeaSolicitOrg aeaSolicitOrg) {

        aeaSolicitOrg.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaSolicitOrg.setCreater(SecurityContext.getCurrentUserId());
        aeaSolicitOrg.setCreateTime(new Date());
        aeaSolicitOrgMapper.insertAeaSolicitOrg(aeaSolicitOrg);
    }

    @Override
    public void updateAeaSolicitOrg(AeaSolicitOrg aeaSolicitOrg) {

        aeaSolicitOrg.setModifier(SecurityContext.getCurrentUserId());
        aeaSolicitOrg.setModifyTime(new Date());
        aeaSolicitOrgMapper.updateAeaSolicitOrg(aeaSolicitOrg);
    }

    @Override
    public void deleteAeaSolicitOrgById(String id) {

        if(StringUtils.isBlank(id)){
            throw new InvalidParameterException("参数id为空!");
        }
        aeaSolicitOrgMapper.deleteAeaSolicitOrg(id);
    }

    @Override
    public void batchDelSolicitOrgByIds(String[] ids){

        if(ids!=null&&ids.length>0){
            aeaSolicitOrgMapper.batchDelSolicitOrgByIds(ids);
        }else{
            throw new InvalidParameterException("参数ids为空!");
        }
    }

    @Override
    public PageInfo<AeaSolicitOrg> listAeaSolicitOrg(AeaSolicitOrg aeaSolicitOrg,Page page) {

        aeaSolicitOrg.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaSolicitOrg> list = aeaSolicitOrgMapper.listAeaSolicitOrg(aeaSolicitOrg);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaSolicitOrg>(list);
    }

    @Override
    public AeaSolicitOrg getAeaSolicitOrgById(String id) {

        if(StringUtils.isBlank(id)){
            throw new InvalidParameterException("参数id为空!");
        }
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaSolicitOrgMapper.getAeaSolicitOrgById(id);
    }

    @Override
    public AeaSolicitOrg getSolicitOrgRelOrgInfoById(String id){

        if(StringUtils.isBlank(id)){
            throw new InvalidParameterException("参数id为空!");
        }
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaSolicitOrgMapper.getSolicitOrgRelOrgInfoById(id);
    }

    @Override
    public List<AeaSolicitOrg> listAeaSolicitOrg(AeaSolicitOrg aeaSolicitOrg) {

        aeaSolicitOrg.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaSolicitOrg> list = aeaSolicitOrgMapper.listAeaSolicitOrg(aeaSolicitOrg);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public PageInfo<AeaSolicitOrg> listAeaSolicitOrgRelOrgInfo(AeaSolicitOrg aeaSolicitOrg,Page page) {

        aeaSolicitOrg.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaSolicitOrg> list = aeaSolicitOrgMapper.listAeaSolicitOrgRelOrgInfo(aeaSolicitOrg);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaSolicitOrg>(list);
    }

    @Override
    public List<AeaSolicitOrg> listAeaSolicitOrgRelOrgInfo(AeaSolicitOrg aeaSolicitOrg) {

        aeaSolicitOrg.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaSolicitOrg> list = aeaSolicitOrgMapper.listAeaSolicitOrgRelOrgInfo(aeaSolicitOrg);
        logger.debug("成功执行查询list！！");
        return list;
    }
}

