package com.augurit.aplanmis.common.service.instance.impl;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.aplanmis.common.domain.AeaHiParStageinst;
import com.augurit.aplanmis.common.mapper.AeaHiParStageinstMapper;
import com.augurit.aplanmis.common.service.instance.AeaHiParStageinstService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Transactional
@Service
public class AeaHiParStageinstServiceImpl implements AeaHiParStageinstService {
    @Autowired
    private AeaHiParStageinstMapper aeaHiParStageinstMapper;

    @Override
    public AeaHiParStageinst createAeaHiParStageinst(String applyinstId,String stageId,String themeVerId,String appinstId,String stageinstId) throws Exception {
        AeaHiParStageinst aeaHiParStageinst = new AeaHiParStageinst();

        if(applyinstId==null)
            throw new NullPointerException("申请实例ID不能为空！");
        if(themeVerId==null)
            throw new NullPointerException("主题版本ID不能为空！");
        if(stageId==null)
            throw new NullPointerException("阶段ID不能为空！");

        if(stageinstId==null){
            aeaHiParStageinst.setStageinstId(UUID.randomUUID().toString());
        }
        aeaHiParStageinst.setStartTime(new Date());
        aeaHiParStageinst.setApplyinstId(applyinstId);
        aeaHiParStageinst.setStageId(stageId);
        aeaHiParStageinst.setThemeVerId(themeVerId);
        aeaHiParStageinst.setStartTime(new Date());
        aeaHiParStageinst.setAppinstId(appinstId);
        //aeaHiParStageinst.setBusinessState();
        //aeaHiParStageinst.setStageinstState();
        aeaHiParStageinst.setCreater(SecurityContext.getCurrentUserName());
        aeaHiParStageinst.setCreateTime(new Date());
        aeaHiParStageinst.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaHiParStageinstMapper.insertAeaHiParStageinst(aeaHiParStageinst);
        return aeaHiParStageinst;
    }

    @Override
    public void updateAeaHiParStageinst(AeaHiParStageinst aeaHiParStageinst) throws Exception {
        if(aeaHiParStageinst==null)
            throw new NullPointerException("需要更新的并联申报对象为空！");

        if(aeaHiParStageinst.getStageinstId()==null)
            throw new InvalidParameterException("更新对象的主键ID为空!");

        aeaHiParStageinstMapper.updateAeaHiParStageinst(aeaHiParStageinst);
    }

    @Override
    public List<AeaHiParStageinst> listAeaHiParStageinst(AeaHiParStageinst aeaHiParStageinst) throws Exception {
        aeaHiParStageinst.setRootOrgId(SecurityContext.getCurrentOrgId());
        return aeaHiParStageinstMapper.listAeaHiParStageinst(aeaHiParStageinst);
    }

    @Override
    public PageInfo<AeaHiParStageinst> listAeaHiParStageinst(AeaHiParStageinst aeaHiParStageinst, Page page) throws Exception {
        aeaHiParStageinst.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaHiParStageinst> list = aeaHiParStageinstMapper.listAeaHiParStageinst(aeaHiParStageinst);
        return new PageInfo<AeaHiParStageinst>(list);
    }

    @Override
    public AeaHiParStageinst getAeaHiParStageinstById(String stageinstId) throws Exception {
        if(stageinstId==null)
            throw new InvalidParameterException("参数stageinstId为空！");
        return aeaHiParStageinstMapper.getAeaHiParStageinstById(stageinstId);
    }

    @Override
    public void deleteAeaHiParStageinstById(String stageinstId) throws Exception {
        if(stageinstId==null)
            throw new InvalidParameterException("参数stageinstId为空！");
        aeaHiParStageinstMapper.deleteAeaHiParStageinst(stageinstId);
    }

    @Override
    public List<AeaHiParStageinst> getAeaHiParStageinstByStageId(String stageId) throws Exception {
        if(stageId==null)
            throw new InvalidParameterException("参数stageId为空！");
        return aeaHiParStageinstMapper.listAeaHiParStageinstByStageIdOrThemeVerId(null,stageId);
    }

    @Override
    public List<AeaHiParStageinst> listAeaHiParStageinstByThemeVerId(String themeVerId) throws Exception {
        if(themeVerId==null)
            throw new InvalidParameterException("参数themeVerId为空！");
        return aeaHiParStageinstMapper.listAeaHiParStageinstByStageIdOrThemeVerId(themeVerId,null);
    }

    @Override
    public AeaHiParStageinst getAeaHiParStageinstByAppinstId(String appinstId) throws Exception {
        if(appinstId==null)
            throw new InvalidParameterException("参数appinstId为空！");
        return aeaHiParStageinstMapper.getAeaHiParStageinstByAppinstId(appinstId);
    }

    @Override
    public AeaHiParStageinst getAeaHiParStageinstByApplyinstId(String applyinstId) throws Exception {
        if(applyinstId==null)
            throw new InvalidParameterException("参数applyinstId为空！");
        return aeaHiParStageinstMapper.getAeaHiParStageinstByApplyinstId(applyinstId);
    }

   // @Override
/*    public void updateStageinstStateByApplyinstId(String applyinstId, String stageinstState) throws Exception {
        if(applyinstId==null)
            throw new InvalidParameterException("参数applyinstId为空！");
        if(stageinstState==null)
            throw new InvalidParameterException("参数stageinstState为空！");

        AeaHiParStageinst conditionObj = new AeaHiParStageinst();
        conditionObj.setApplyinstId(applyinstId);
        conditionObj.setStageinstState(stageinstState);
        aeaHiParStageinstMapper.updateAeaHiParStageinst(conditionObj);
    }*/

   /* @Override
    public void updateBusinessStateByApplyinstId(String applyinstId, String businessState) throws Exception {
        if(applyinstId==null)
            throw new InvalidParameterException("参数applyinstId为空！");
        if(businessState==null)
            throw new InvalidParameterException("参数businessState为空！");

        AeaHiParStageinst conditionObj = new AeaHiParStageinst();
        conditionObj.setApplyinstId(applyinstId);
        conditionObj.setBusinessState(businessState);
        aeaHiParStageinstMapper.updateAeaHiParStageinst(conditionObj);
    }*/
}
