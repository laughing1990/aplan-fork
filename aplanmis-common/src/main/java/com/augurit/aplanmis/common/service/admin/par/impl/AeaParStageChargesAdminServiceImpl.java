package com.augurit.aplanmis.common.service.admin.par.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;

import com.augurit.aplanmis.common.domain.AeaParStageCharges;
import com.augurit.aplanmis.common.mapper.AeaParStageChargesMapper;
import com.augurit.aplanmis.common.service.admin.par.AeaParStageChargesAdminService;
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
* 阶段办事指南收费项目信息-Service服务接口实现类
*/
@Service
@Transactional
public class AeaParStageChargesAdminServiceImpl implements AeaParStageChargesAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaParStageChargesAdminServiceImpl.class);

    @Autowired
    private AeaParStageChargesMapper aeaParStageChargesMapper;

    @Override
    public void saveAeaParStageCharges(AeaParStageCharges aeaParStageCharges){

        aeaParStageCharges.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaParStageCharges.setCreater(SecurityContext.getCurrentUserId());
        aeaParStageCharges.setCreateTime(new Date());
        aeaParStageChargesMapper.insertAeaParStageCharges(aeaParStageCharges);
    }

    @Override
    public void updateAeaParStageCharges(AeaParStageCharges aeaParStageCharges){

        aeaParStageCharges.setModifier(SecurityContext.getCurrentUserId());
        aeaParStageCharges.setModifyTime(new Date());
        aeaParStageChargesMapper.updateAeaParStageCharges(aeaParStageCharges);
    }

    @Override
    public void deleteAeaParStageChargesById(String id){

        if(StringUtils.isBlank(id)){
            throw new InvalidParameterException("参数id为空!");
        }
        aeaParStageChargesMapper.deleteAeaParStageCharges(id);
    }

    @Override
    public void batchDelChargesByIds(String[] ids){

        if(ids!=null&&ids.length>0){
            aeaParStageChargesMapper.batchDelChargesByIds(ids);
        }else{
            throw new InvalidParameterException("参数ids为空!");
        }
    }

    @Override
    public PageInfo<AeaParStageCharges> listAeaParStageCharges(AeaParStageCharges aeaParStageCharges,Page page){

        aeaParStageCharges.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaParStageCharges> list = aeaParStageChargesMapper.listAeaParStageCharges(aeaParStageCharges);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaParStageCharges>(list);
    }

    @Override
    public AeaParStageCharges getAeaParStageChargesById(String id){

        if(StringUtils.isBlank(id)){
            throw new InvalidParameterException("参数id为空!");
        }
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaParStageChargesMapper.getAeaParStageChargesById(id);
    }

    @Override
    public List<AeaParStageCharges> listAeaParStageCharges(AeaParStageCharges aeaParStageCharges){

        aeaParStageCharges.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaParStageCharges> list = aeaParStageChargesMapper.listAeaParStageCharges(aeaParStageCharges);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public Long getMaxSortNo(String stageId, String rootOrgId){

        Long sortNo = aeaParStageChargesMapper.getMaxSortNo(stageId, rootOrgId);
        return sortNo==null?1L:(sortNo+1);
    }
}

