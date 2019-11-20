package com.augurit.aplanmis.common.service.admin.par.impl;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaParFrontItemform;
import com.augurit.aplanmis.common.mapper.AeaParFrontItemformMapper;
import com.augurit.aplanmis.common.service.admin.par.AeaParFrontItemformService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
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
 * 阶段事项表单前置检测表-Service服务接口实现类
 */
@Service
@Transactional
public class AeaParFrontItemformServiceImpl implements AeaParFrontItemformService {

    private static Logger logger = LoggerFactory.getLogger(AeaParFrontItemformServiceImpl.class);

    @Autowired
    private AeaParFrontItemformMapper aeaParFrontItemformMapper;

    @Override
    public void saveAeaParFrontItemform(AeaParFrontItemform aeaParFrontItemform) throws Exception {

        aeaParFrontItemform.setCreateTime(new Date());
        aeaParFrontItemform.setCreater(SecurityContext.getCurrentUserId());
        aeaParFrontItemform.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaParFrontItemformMapper.insertAeaParFrontItemform(aeaParFrontItemform);
    }

    @Override
    public void updateAeaParFrontItemform(AeaParFrontItemform aeaParFrontItemform) throws Exception {

        aeaParFrontItemform.setModifyTime(new Date());
        aeaParFrontItemform.setModifier(SecurityContext.getCurrentUserId());
        aeaParFrontItemformMapper.updateAeaParFrontItemform(aeaParFrontItemform);
    }

    @Override
    public void deleteAeaParFrontItemformById(String id) throws Exception {

        if (StringUtils.isBlank(id)) {
            throw new InvalidParameterException(id);
        }
        String[] ids = id.split(",");
        for (String frontItemformId : ids) {
            aeaParFrontItemformMapper.deleteAeaParFrontItemform(frontItemformId);
        }
    }

    @Override
    public PageInfo<AeaParFrontItemform> listAeaParFrontItemform(AeaParFrontItemform aeaParFrontItemform, Page page) throws Exception {

        aeaParFrontItemform.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaParFrontItemform> list = aeaParFrontItemformMapper.listAeaParFrontItemform(aeaParFrontItemform);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public AeaParFrontItemform getAeaParFrontItemformById(String id) throws Exception {

        if (StringUtils.isBlank(id)) {
            throw new InvalidParameterException(id);
        }
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaParFrontItemformMapper.getAeaParFrontItemformById(id);
    }

    @Override
    public List<AeaParFrontItemform> listAeaParFrontItemform(AeaParFrontItemform aeaParFrontItemform) throws Exception {

        aeaParFrontItemform.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaParFrontItemform> list = aeaParFrontItemformMapper.listAeaParFrontItemform(aeaParFrontItemform);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public PageInfo<AeaParFrontItemform> listAeaParFrontItemformVoByPage(AeaParFrontItemform aeaParFrontItemform, Page page) throws Exception {

        aeaParFrontItemform.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaParFrontItemform> list = aeaParFrontItemformMapper.listAeaParFrontItemformVo(aeaParFrontItemform);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public Long getMaxSortNo(String stageId, String rootOrgId) throws Exception {

        Long sortNo = aeaParFrontItemformMapper.getMaxSortNo(stageId, rootOrgId);
        return sortNo==null?1L:(sortNo+1L);
    }

    @Override
    public PageInfo<AeaParFrontItemform> listSelectParFrontItemformByPage(AeaParFrontItemform aeaParFrontItemform, Page page) throws Exception {

        aeaParFrontItemform.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaParFrontItemform> list = aeaParFrontItemformMapper.listSelectParFrontItemform(aeaParFrontItemform);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public AeaParFrontItemform getAeaParFrontItemformVoById(String frontItemformId) throws Exception {

        if (StringUtils.isBlank(frontItemformId)) {
            throw new InvalidParameterException(frontItemformId);
        }
        return aeaParFrontItemformMapper.getAeaParFrontItemformVoById(frontItemformId);
    }

    private void checkSame(AeaParFrontItemform aeaParFrontItemform) throws Exception {

        AeaParFrontItemform queryParFrontItemform = new AeaParFrontItemform();
        queryParFrontItemform.setStageId(aeaParFrontItemform.getStageId());
        queryParFrontItemform.setStageItemId(aeaParFrontItemform.getStageItemId());
        List<AeaParFrontItemform> list = aeaParFrontItemformMapper.listAeaParFrontItemform(queryParFrontItemform);
        if (list.size() > 0) {
            throw new RuntimeException("已有配置相同的前置事项表单检测!");
        }

    }

    @Override
    public List<AeaParFrontItemform> getAeaParFrontItemformVoByStageId(String stageId) {

        List<AeaParFrontItemform> aeaParFrontItemformVos = new ArrayList();
        if (StringUtils.isBlank(stageId)){
            return aeaParFrontItemformVos;
        }
        aeaParFrontItemformVos.addAll(aeaParFrontItemformMapper.getAeaParFrontItemformVoByStageId(stageId, SecurityContext.getCurrentOrgId()));
        return aeaParFrontItemformVos;
    }

    @Override
    public void batchSaveAeaParFrontItemform(String stageId, String[] stageItemIds)throws Exception{

        if(StringUtils.isBlank(stageId)){
            throw new InvalidParameterException("参数stageId为空!");
        }
        if(stageItemIds==null||(stageItemIds!=null&&stageItemIds.length==0)){
            throw new InvalidParameterException("参数stageItemIds为空!");
        }
        if(stageItemIds!=null && stageItemIds.length>0){
            String userId = SecurityContext.getCurrentUserId();
            String rootOrgId = SecurityContext.getCurrentOrgId();
            for(String stageItemId:stageItemIds){
                AeaParFrontItemform aeaParFrontItemform = new AeaParFrontItemform();
                aeaParFrontItemform.setFrontItemformId(UUID.randomUUID().toString());
                aeaParFrontItemform.setStageId(stageId);
                aeaParFrontItemform.setStageItemId(stageItemId);
                aeaParFrontItemform.setCreater(userId);
                aeaParFrontItemform.setCreateTime(new Date());
                aeaParFrontItemform.setRootOrgId(rootOrgId);
                aeaParFrontItemform.setSortNo(getMaxSortNo(stageId, rootOrgId));
                aeaParFrontItemform.setIsActive(Status.ON);
                aeaParFrontItemformMapper.insertAeaParFrontItemform(aeaParFrontItemform);
            }
        }
    }

    @Override
    public List<AeaParFrontItemform> listAeaParFrontItemformVoByNoPage(AeaParFrontItemform aeaParFrontItemform) throws Exception{

        aeaParFrontItemform.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaParFrontItemform> list = aeaParFrontItemformMapper.listAeaParFrontItemformVo(aeaParFrontItemform);
        return list;
    }

    @Override
    public void changIsActive(String id, String rootOrgId){

        aeaParFrontItemformMapper.changIsActive(id, rootOrgId);
    }
}

