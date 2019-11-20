package com.augurit.aplanmis.common.service.admin.par.impl;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaParFrontStage;
import com.augurit.aplanmis.common.mapper.AeaParFrontStageMapper;
import com.augurit.aplanmis.common.service.admin.par.AeaParFrontStageService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 阶段的前置阶段检测表-Service服务接口实现类
 */
@Service
@Transactional
public class AeaParFrontStageServiceImpl implements AeaParFrontStageService {

    private static Logger logger = LoggerFactory.getLogger(AeaParFrontStageServiceImpl.class);

    @Autowired
    private AeaParFrontStageMapper aeaParFrontStageMapper;

    @Override
    public void saveAeaParFrontStage(AeaParFrontStage aeaParFrontStage) throws Exception {
//        checkSame(aeaParFrontStage);

        aeaParFrontStage.setCreateTime(new Date());
        aeaParFrontStage.setCreater(SecurityContext.getCurrentUserId());
        aeaParFrontStage.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaParFrontStageMapper.insertAeaParFrontStage(aeaParFrontStage);
    }

    @Override
    public void updateAeaParFrontStage(AeaParFrontStage aeaParFrontStage) throws Exception {
//        checkSame(aeaParFrontStage);

        aeaParFrontStage.setModifyTime(new Date());
        aeaParFrontStage.setModifier(SecurityContext.getCurrentUserId());
        aeaParFrontStageMapper.updateAeaParFrontStage(aeaParFrontStage);
    }

    @Override
    public void deleteAeaParFrontStageById(String id) throws Exception {

        if (StringUtils.isBlank(id)) {
            throw new InvalidParameterException(id);
        }
        String[] ids = id.split(",");
        for (String frontStageId : ids) {
            aeaParFrontStageMapper.deleteAeaParFrontStage(frontStageId);
        }
    }

    @Override
    public PageInfo<AeaParFrontStage> listAeaParFrontStage(AeaParFrontStage aeaParFrontStage, Page page) throws Exception {

        aeaParFrontStage.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaParFrontStage> list = aeaParFrontStageMapper.listAeaParFrontStage(aeaParFrontStage);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public AeaParFrontStage getAeaParFrontStageById(String id) throws Exception {

        if (StringUtils.isBlank(id)) {
            throw new InvalidParameterException(id);
        }
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaParFrontStageMapper.getAeaParFrontStageById(id);
    }

    @Override
    public List<AeaParFrontStage> listAeaParFrontStage(AeaParFrontStage aeaParFrontStage) throws Exception {

        aeaParFrontStage.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaParFrontStage> list = aeaParFrontStageMapper.listAeaParFrontStage(aeaParFrontStage);
        logger.debug("成功执行查询list！！");
        return list;
    }

    private void checkSame(AeaParFrontStage aeaParFrontStage) throws Exception {

        AeaParFrontStage queryParFrontStage = new AeaParFrontStage();
        queryParFrontStage.setStageId(aeaParFrontStage.getStageId());
        queryParFrontStage.setHistStageId(aeaParFrontStage.getHistStageId());
        List<AeaParFrontStage> list = aeaParFrontStageMapper.listAeaParFrontStage(queryParFrontStage);
        if (list.size() > 0) {
            throw new RuntimeException("已有配置相同的前置阶段检测!");
        }
    }

    @Override
    public PageInfo<AeaParFrontStage> listAeaParFrontStageVoByPage(AeaParFrontStage aeaParFrontStage, Page page) throws Exception {

        aeaParFrontStage.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaParFrontStage> list = aeaParFrontStageMapper.listAeaParFrontStageVo(aeaParFrontStage);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public Long getMaxSortNo(String stageId, String rootOrgId) throws Exception {

        Long sortNo = aeaParFrontStageMapper.getMaxSortNo(stageId, rootOrgId);
        return sortNo==null?1L:(sortNo+1L);
    }

    @Override
    public AeaParFrontStage getAeaParFrontStageVoById(String frontStageId) throws Exception {

        if (StringUtils.isBlank(frontStageId)) {
            throw new InvalidParameterException(frontStageId);
        }
        return aeaParFrontStageMapper.getAeaParFrontStageVoById(frontStageId);
    }

    @Override
    public List<AeaParFrontStage> listSelectParFrontStage(AeaParFrontStage aeaParFrontStage) throws Exception {

        aeaParFrontStage.setRootOrgId(SecurityContext.getCurrentOrgId());
        return aeaParFrontStageMapper.listSelectParFrontStage(aeaParFrontStage);
    }

    @Override
    public List<AeaParFrontStage> getHistStageByStageId(String stageId) {

        List<AeaParFrontStage> aeaParFrontStageVos = new ArrayList();
        if(StringUtils.isBlank(stageId)){
            return aeaParFrontStageVos;
        }
        return aeaParFrontStageMapper.getHistStageByStageId(stageId, SecurityContext.getCurrentOrgId());
    }

    @Override
    public PageInfo<AeaParFrontStage> listSelectParFrontStageByPage(AeaParFrontStage aeaParFrontStage,Page page) throws Exception{

        aeaParFrontStage.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaParFrontStage> list = aeaParFrontStageMapper.listSelectParFrontStage(aeaParFrontStage);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public void batchSaveAeaParFrontStage(String stageId, String[] histStageIds)throws Exception{

        if(StringUtils.isBlank(stageId)){
            throw new InvalidParameterException("参数stageId为空!");
        }
        if(histStageIds==null||(histStageIds!=null&&histStageIds.length==0)){
            throw new InvalidParameterException("参数histStageIds为空!");
        }
        if(histStageIds!=null && histStageIds.length>0){
            String userId = SecurityContext.getCurrentUserId();
            String rootOrgId = SecurityContext.getCurrentOrgId();
            for(String histStageId : histStageIds){
                AeaParFrontStage frontStage = new AeaParFrontStage();
                frontStage.setFrontStageId(UUID.randomUUID().toString());
                frontStage.setStageId(stageId);
                frontStage.setHistStageId(histStageId);
                frontStage.setSortNo(getMaxSortNo(stageId, rootOrgId));
                frontStage.setIsActive(Status.ON);
                frontStage.setCreater(userId);
                frontStage.setCreateTime(new Date());
                frontStage.setRootOrgId(rootOrgId);
                aeaParFrontStageMapper.insertAeaParFrontStage(frontStage);
            }
        }
    }

    @Override
    public List<AeaParFrontStage> listAeaParFrontStageVoByNoPage(AeaParFrontStage aeaParFrontStage) throws Exception{

        aeaParFrontStage.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaParFrontStage> list = aeaParFrontStageMapper.listAeaParFrontStageVo(aeaParFrontStage);
        return list;
    }

    @Override
    public void changIsActive(String id, String rootOrgId){

        aeaParFrontStageMapper.changIsActive(id, rootOrgId);
    }
}

