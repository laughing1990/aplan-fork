package com.augurit.aplanmis.common.service.instance.impl;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaHiSeriesinst;
import com.augurit.aplanmis.common.mapper.AeaHiSeriesinstMapper;
import com.augurit.aplanmis.common.service.instance.AeaHiSeriesinstService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AeaHiSeriesinstServiceImpl implements AeaHiSeriesinstService {
    @Autowired
    private AeaHiSeriesinstMapper aeaHiSeriesinstMapper;

    @Override
    public AeaHiSeriesinst createAeaHiSeriesinst(String applyinstId,String appinstId,String isParallel,String stageId) throws Exception {
        AeaHiSeriesinst aeaHiSeriesinst = new AeaHiSeriesinst();

        if(appinstId==null)
            throw new NullPointerException("流程实例ID不能为空！");
        if(applyinstId==null)
            throw new NullPointerException("申请实例ID不能为空！");

        aeaHiSeriesinst.setSeriesinstId(UUID.randomUUID().toString());
        aeaHiSeriesinst.setStartTime(new Date());
        aeaHiSeriesinst.setApplyinstId(applyinstId);
        aeaHiSeriesinst.setAppinstId(appinstId);
        aeaHiSeriesinst.setCreater(SecurityContext.getCurrentUserName());
        aeaHiSeriesinst.setCreateTime(new Date());
        aeaHiSeriesinst.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaHiSeriesinst.setIsParallel("0");
        if(isParallel!=null)
            aeaHiSeriesinst.setIsParallel(isParallel);
        if(stageId!=null)
            aeaHiSeriesinst.setStageId(stageId);
        aeaHiSeriesinstMapper.insertAeaHiSeriesinst(aeaHiSeriesinst);
        return aeaHiSeriesinst;
    }

    @Override
    public void updateAeaHiSeriesinst(AeaHiSeriesinst aeaHiSeriesinst) throws Exception {
        if(aeaHiSeriesinst==null)
            throw new NullPointerException("需要更新的单项申报对象为空！");

        if(aeaHiSeriesinst.getSeriesinstId()==null)
            throw new InvalidParameterException("更新对象的主键ID为空!");

        aeaHiSeriesinstMapper.updateAeaHiSeriesinst(aeaHiSeriesinst);
    }

    @Override
    public List<AeaHiSeriesinst> listAeaHiSeriesinst(AeaHiSeriesinst aeaHiSeriesinst) throws Exception {
        aeaHiSeriesinst.setRootOrgId(SecurityContext.getCurrentOrgId());
        return aeaHiSeriesinstMapper.listAeaHiSeriesinst(aeaHiSeriesinst);
    }

    @Override
    public PageInfo<AeaHiSeriesinst> listAeaHiSeriesinst(AeaHiSeriesinst aeaHiSeriesinst, Page page) throws Exception {
        aeaHiSeriesinst.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaHiSeriesinst> list = aeaHiSeriesinstMapper.listAeaHiSeriesinst(aeaHiSeriesinst);
        return new PageInfo<AeaHiSeriesinst>(list);
    }

    @Override
    public void deleteAeaHiSeriesinst(String seriesinstId) throws Exception {
        if(seriesinstId==null)
            throw new InvalidParameterException("参数seriesinstId为空！");

        aeaHiSeriesinstMapper.deleteAeaHiSeriesinst(seriesinstId);
    }

    @Override
    public AeaHiSeriesinst getAeaHiSeriesinstById(String seriesinstId) throws Exception {
        if(seriesinstId==null)
            throw new InvalidParameterException("参数seriesinstId为空！");

        return aeaHiSeriesinstMapper.getAeaHiSeriesinstById(seriesinstId);
    }

    @Override
    public AeaHiSeriesinst getAeaHiSeriesinstByApplyinstId(String applyinstId) throws Exception {
        if(applyinstId==null)
            throw new InvalidParameterException("参数applyinstId为空！");
        return aeaHiSeriesinstMapper.getAeaHiSeriesinstByApplyinstId(applyinstId);
    }

    @Override
    public AeaHiSeriesinst getAeaHiSeriesinstByAppinstId(String appinstId) throws Exception {
        if(appinstId==null)
            throw new InvalidParameterException("参数appinstId为空！");
        return aeaHiSeriesinstMapper.getAeaHiSeriesinstByAppinstId(appinstId);
    }

    @Override
    public void updateSeriesinstStateById(String seriesinstId, String state) throws Exception {
        if(seriesinstId==null)
            throw new InvalidParameterException("参数seriesinstId为空！");
        if(state==null)
            throw new InvalidParameterException("参数state为空！");

        AeaHiSeriesinst conditionObj = new AeaHiSeriesinst();
        conditionObj.setSeriesinstId(seriesinstId);
        conditionObj.setSeriesinstState(state);
        aeaHiSeriesinstMapper.updateAeaHiSeriesinst(conditionObj);
    }

    @Override
    public void updateEndTimeById(String seriesinstId, Date endTime) throws Exception {
        if(seriesinstId==null)
            throw new InvalidParameterException("参数seriesinstId为空！");
        if(endTime==null)
            throw new InvalidParameterException("参数endTime为空！");

        AeaHiSeriesinst conditionObj = new AeaHiSeriesinst();
        conditionObj.setSeriesinstId(seriesinstId);
        conditionObj.setEndTime(endTime);
        aeaHiSeriesinstMapper.updateAeaHiSeriesinst(conditionObj);
    }
}
