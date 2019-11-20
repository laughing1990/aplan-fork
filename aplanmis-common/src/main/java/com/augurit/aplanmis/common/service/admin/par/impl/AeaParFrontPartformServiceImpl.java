package com.augurit.aplanmis.common.service.admin.par.impl;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaParFrontPartform;
import com.augurit.aplanmis.common.mapper.AeaParFrontPartformMapper;
import com.augurit.aplanmis.common.service.admin.par.AeaParFrontPartformService;
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
 * 阶段的扩展表单前置检测表-Service服务接口实现类
 */
@Service
@Transactional
public class AeaParFrontPartformServiceImpl implements AeaParFrontPartformService {

    private static Logger logger = LoggerFactory.getLogger(AeaParFrontPartformServiceImpl.class);

    @Autowired
    private AeaParFrontPartformMapper aeaParFrontPartformMapper;

    @Override
    public void saveAeaParFrontPartform(AeaParFrontPartform aeaParFrontPartform) throws Exception {

//        checkSame(aeaParFrontPartform);

        aeaParFrontPartform.setCreateTime(new Date());
        aeaParFrontPartform.setCreater(SecurityContext.getCurrentUserId());
        aeaParFrontPartform.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaParFrontPartformMapper.insertAeaParFrontPartform(aeaParFrontPartform);
    }

    @Override
    public void updateAeaParFrontPartform(AeaParFrontPartform aeaParFrontPartform) throws Exception {

//      checkSame(aeaParFrontPartform);

        aeaParFrontPartform.setModifyTime(new Date());
        aeaParFrontPartform.setModifier(SecurityContext.getCurrentUserId());
        aeaParFrontPartformMapper.updateAeaParFrontPartform(aeaParFrontPartform);
    }

    @Override
    public void deleteAeaParFrontPartformById(String id) throws Exception {

        if (StringUtils.isBlank(id)) {
            throw new InvalidParameterException(id);
        }
        String[] ids = id.split(",");
        for (String frontPartformId : ids) {
            aeaParFrontPartformMapper.deleteAeaParFrontPartform(frontPartformId);
        }
    }

    @Override
    public PageInfo<AeaParFrontPartform> listAeaParFrontPartform(AeaParFrontPartform aeaParFrontPartform, Page page) throws Exception {

        aeaParFrontPartform.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaParFrontPartform> list = aeaParFrontPartformMapper.listAeaParFrontPartform(aeaParFrontPartform);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public AeaParFrontPartform getAeaParFrontPartformById(String id) throws Exception {

        if (id == null) {
            throw new InvalidParameterException(id);
        }
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaParFrontPartformMapper.getAeaParFrontPartformById(id);
    }

    @Override
    public List<AeaParFrontPartform> listAeaParFrontPartform(AeaParFrontPartform aeaParFrontPartform) throws Exception {

        aeaParFrontPartform.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaParFrontPartform> list = aeaParFrontPartformMapper.listAeaParFrontPartform(aeaParFrontPartform);
        logger.debug("成功执行查询list！！");
        return list;
    }

    private void checkSame(AeaParFrontPartform aeaParFrontPartform) throws Exception {

        AeaParFrontPartform queryParFrontPartform = new AeaParFrontPartform();
        queryParFrontPartform.setStageId(aeaParFrontPartform.getStageId());
        queryParFrontPartform.setStagePartformId(aeaParFrontPartform.getStagePartformId());
        List<AeaParFrontPartform> list = aeaParFrontPartformMapper.listAeaParFrontPartform(queryParFrontPartform);
        if (list.size() > 0) {
            throw new RuntimeException("已有配置相同的前置阶段扩展表单检测!");
        }

    }

    @Override
    public PageInfo<AeaParFrontPartform> listAeaParFrontPartformVoByPage(AeaParFrontPartform aeaParFrontPartform, Page page) throws Exception {

        aeaParFrontPartform.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaParFrontPartform> list = aeaParFrontPartformMapper.listAeaParFrontPartformVo(aeaParFrontPartform);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public Long getMaxSortNo(String stageId, String rootOrgId) throws Exception {

        Long sortNo = aeaParFrontPartformMapper.getMaxSortNo(stageId, rootOrgId);
        return sortNo==null?1L:(sortNo+1L);
    }

    @Override
    public PageInfo<AeaParFrontPartform> listSelectParFrontPartformByPage(AeaParFrontPartform aeaParFrontPartform, Page page) throws Exception {

        aeaParFrontPartform.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaParFrontPartform> list = aeaParFrontPartformMapper.listSelectParFrontPartform(aeaParFrontPartform);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public AeaParFrontPartform getAeaParFrontPartformVoById(String frontPartformId) throws Exception {

        if (StringUtils.isBlank(frontPartformId)) {
            throw new InvalidParameterException(frontPartformId);
        }
        return aeaParFrontPartformMapper.getAeaParFrontPartformVoById(frontPartformId);
    }

    @Override
    public List<AeaParFrontPartform> getAeaParFrontPartformVoByStageId(String stageId) {

        List<AeaParFrontPartform> aeaParFrontPartformVos = new ArrayList();
        if (StringUtils.isBlank(stageId)){
            return aeaParFrontPartformVos;
        }
        aeaParFrontPartformVos.addAll(aeaParFrontPartformMapper.getAeaParFrontPartformVoByStageId(stageId, SecurityContext.getCurrentOrgId()));
        return aeaParFrontPartformVos;
    }

    @Override
    public void batchSaveAeaParFrontPartform(String stageId, String[] stagePartformIds)throws Exception{

        if(StringUtils.isBlank(stageId)){
            throw new InvalidParameterException("参数stageId为空!");
        }
        if(stagePartformIds==null||(stagePartformIds!=null&&stagePartformIds.length==0)){
            throw new InvalidParameterException("参数stagePartformIds为空!");
        }
        if(stagePartformIds!=null && stagePartformIds.length>0){
            String userId = SecurityContext.getCurrentUserId();
            String rootOrgId = SecurityContext.getCurrentOrgId();
            for(String stagePartformId:stagePartformIds){

                AeaParFrontPartform aeaParFrontPartform = new AeaParFrontPartform();
                aeaParFrontPartform.setFrontPartformId(UUID.randomUUID().toString());
                aeaParFrontPartform.setStageId(stageId);
                aeaParFrontPartform.setStagePartformId(stagePartformId);
                aeaParFrontPartform.setCreater(userId);
                aeaParFrontPartform.setCreateTime(new Date());
                aeaParFrontPartform.setRootOrgId(rootOrgId);
                aeaParFrontPartform.setIsActive(Status.ON);
                aeaParFrontPartform.setSortNo(getMaxSortNo(stageId, rootOrgId));
                aeaParFrontPartformMapper.insertAeaParFrontPartform(aeaParFrontPartform);
            }
        }
    }

    @Override
    public List<AeaParFrontPartform> listAeaParFrontPartformVoByNoPage(AeaParFrontPartform aeaParFrontPartform) throws Exception{

        List<AeaParFrontPartform> list = aeaParFrontPartformMapper.listAeaParFrontPartformVo(aeaParFrontPartform);
        return list;
    }

    @Override
    public void changIsActive(String id, String rootOrgId){

        aeaParFrontPartformMapper.changIsActive(id, rootOrgId);
    }
}

