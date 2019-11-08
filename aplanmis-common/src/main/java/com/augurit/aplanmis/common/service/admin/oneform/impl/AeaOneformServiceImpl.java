package com.augurit.aplanmis.common.service.admin.oneform.impl;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaOneform;
import com.augurit.aplanmis.common.mapper.AeaOneformMapper;
import com.augurit.aplanmis.common.service.admin.oneform.AeaOneformService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class AeaOneformServiceImpl implements AeaOneformService {

    private static Logger logger = LoggerFactory.getLogger(AeaOneformServiceImpl.class);

    @Autowired
    private AeaOneformMapper aeaOneformMapper;

    @Override
    public void saveAeaOneform(AeaOneform aeaOneform){

        aeaOneform.setCreater(SecurityContext.getCurrentUserId());
        aeaOneform.setCreateTime(new Date());
        aeaOneformMapper.insertAeaOneform(aeaOneform);
    }

    @Override
    public void updateAeaOneform(AeaOneform aeaOneform){

        aeaOneform.setModifier(SecurityContext.getCurrentUserId());
        aeaOneform.setModifyTime(new Date());
        aeaOneformMapper.updateAeaOneform(aeaOneform);
    }

    @Override
    public void deleteAeaOneformById(String id){

        if(StringUtils.isBlank(id)) {
            throw new InvalidParameterException("参数id为空!");
        }
        aeaOneformMapper.deleteAeaOneform(id);
    }

    @Override
    public void batchDelAeaOneformByIds(String[] ids){

        if(ids!=null&&ids.length>0){
            aeaOneformMapper.batchDelAeaOneformByIds(ids);
        }else{
            throw new InvalidParameterException("参数ids为空!");
        }
    }

    @Override
    public EasyuiPageInfo<AeaOneform> getAeaOneformList(AeaOneform AeaOneform, Page page){

        PageHelper.startPage(page);
        List<AeaOneform> list = aeaOneformMapper.listAeaOneform(AeaOneform);
        logger.debug("成功执行分页查询！！");
        return PageHelper.toEasyuiPageInfo(new PageInfo<>(list));
    }

    @Override
    public AeaOneform getAeaOneformById(String id){

        if(StringUtils.isBlank(id)) {
            throw new InvalidParameterException("参数id为空!");
        }
        AeaOneform AeaOneform = aeaOneformMapper.getParOneformById(id);
        return AeaOneform;
    }

    @Override
    public Long getMaxSortNo(String rootOrgId){

        Long sortNo = aeaOneformMapper.getMaxSortNo(rootOrgId);
        return sortNo==null?1L:(sortNo+1L);
    }

    @Override
    public List<AeaOneform> findAllStageOneFormsByStageId(String stageId, String rootOrgId) {

        return aeaOneformMapper.listAeaOneformByStageId(stageId, rootOrgId);
    }

    @Override
    public void enOrDisableIsActive(String id){

        if(StringUtils.isBlank(id)){
          throw new InvalidParameterException("参数id为空!");
        }
        aeaOneformMapper.enOrDisableIsActive(id);
    }
}
