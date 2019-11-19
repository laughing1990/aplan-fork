package com.augurit.aplanmis.common.service.admin.item.impl;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaItemFrontPartform;
import com.augurit.aplanmis.common.mapper.AeaItemFrontPartformMapper;
import com.augurit.aplanmis.common.service.admin.item.AeaItemFrontPartformAdminService;
import com.augurit.aplanmis.common.vo.AeaItemFrontPartformVo;
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
 * 事项的前置检查事项-Service服务接口实现类
 */
@Service
@Transactional
public class AeaItemFrontPartformAdminServiceImpl implements AeaItemFrontPartformAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaItemFrontPartformAdminServiceImpl.class);

    @Autowired
    private AeaItemFrontPartformMapper aeaItemFrontPartformMapper;

    @Override
    public void saveAeaItemFrontPartform(AeaItemFrontPartform aeaItemFrontPartform) {

        aeaItemFrontPartform.setCreateTime(new Date());
        aeaItemFrontPartform.setCreater(SecurityContext.getCurrentUserId());
        aeaItemFrontPartform.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaItemFrontPartformMapper.insertAeaItemFrontPartform(aeaItemFrontPartform);
    }

    @Override
    public void updateAeaItemFrontPartform(AeaItemFrontPartform aeaItemFrontPartform) {

        aeaItemFrontPartform.setModifyTime(new Date());
        aeaItemFrontPartform.setModifier(SecurityContext.getCurrentUserId());
        aeaItemFrontPartformMapper.updateAeaItemFrontPartform(aeaItemFrontPartform);
    }

    @Override
    public void deleteAeaItemFrontPartformById(String id) {

        if(StringUtils.isBlank(id)) {
            throw new InvalidParameterException("参数id为空!");
        }
        aeaItemFrontPartformMapper.deleteAeaItemFrontPartform(id);
    }

    @Override
    public void deleteAeaItemFrontPartformByIds(String[] ids){

        aeaItemFrontPartformMapper.deleteAeaItemFrontPartformByIds(ids);
    }

    @Override
    public PageInfo<AeaItemFrontPartform> listAeaItemFrontPartform(AeaItemFrontPartform aeaItemFrontPartform, Page page) {

        PageHelper.startPage(page);
        List<AeaItemFrontPartform> list = aeaItemFrontPartformMapper.listAeaItemFrontPartform(aeaItemFrontPartform);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public AeaItemFrontPartform getAeaItemFrontPartformById(String id) {

        if(StringUtils.isNotBlank(id)) {
            throw new InvalidParameterException("参数id为空!");
        }
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaItemFrontPartformMapper.getAeaItemFrontPartformById(id);
    }

    @Override
    public List<AeaItemFrontPartform> listAeaItemFrontPartform(AeaItemFrontPartform aeaItemFrontPartform) {

        List<AeaItemFrontPartform> list = aeaItemFrontPartformMapper.listAeaItemFrontPartform(aeaItemFrontPartform);
        logger.debug("成功执行查询list！！");
        return list;
    }

    private void checkSame(AeaItemFrontPartform aeaItemFrontPartform) {

        AeaItemFrontPartform queryItemFrontPartform = new AeaItemFrontPartform();
        queryItemFrontPartform.setItemVerId(aeaItemFrontPartform.getItemVerId());
        queryItemFrontPartform.setItemPartformId(aeaItemFrontPartform.getItemPartformId());
        List<AeaItemFrontPartform> list = aeaItemFrontPartformMapper.listAeaItemFrontPartform(queryItemFrontPartform);
        if (list.size() > 0) {
            throw new RuntimeException("已有配置相同的前置扩事项展表单检测!");
        }

    }

    @Override
    public PageInfo<AeaItemFrontPartformVo> listAeaItemFrontPartformVoByPage(AeaItemFrontPartform aeaItemFrontPartform, Page page) {

        aeaItemFrontPartform.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaItemFrontPartformVo> list = aeaItemFrontPartformMapper.listAeaItemFrontPartformVo(aeaItemFrontPartform);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public List<AeaItemFrontPartformVo> listAeaItemFrontPartformVo(AeaItemFrontPartform aeaItemFrontPartform) {

        aeaItemFrontPartform.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaItemFrontPartformVo> list = aeaItemFrontPartformMapper.listAeaItemFrontPartformVo(aeaItemFrontPartform);
        logger.debug("成功执行分页查询！！");
        return list;
    }

    @Override
    public Long getMaxSortNo(String itemVerId, String rootOrgId){

        Long sortNo = aeaItemFrontPartformMapper.getMaxSortNo(itemVerId, rootOrgId);
        return sortNo==null?1L:(sortNo+1L);
    }

    @Override
    public PageInfo<AeaItemFrontPartformVo> listItemNoSelectPartform(AeaItemFrontPartform frontPartform, Page page) {

        frontPartform.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaItemFrontPartformVo> list = aeaItemFrontPartformMapper.listItemNoSelectPartform(frontPartform);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public AeaItemFrontPartformVo getAeaItemFrontPartformVoById(String frontPartformId) {

        if (StringUtils.isBlank(frontPartformId)) {
            throw new InvalidParameterException(frontPartformId);
        }
        return aeaItemFrontPartformMapper.getAeaItemFrontPartformVoById(frontPartformId);
    }

    @Override
    public List<AeaItemFrontPartformVo> getAeaItemFrontPartformVoByItemVerId(String itemVerId) {

        List<AeaItemFrontPartformVo> aeaItemFrontPartformVos = new ArrayList();
        if (StringUtils.isBlank(itemVerId)) {
            return aeaItemFrontPartformVos;
        }
        aeaItemFrontPartformVos.addAll(aeaItemFrontPartformMapper.getAeaItemFrontPartformVoByItemVerId(itemVerId, SecurityContext.getCurrentOrgId()));
        return aeaItemFrontPartformVos;
    }

    @Override
    public void batchSaveAeaItemFrontPartform(String itemVerId, String[] itemPartformIds)throws Exception{

        if(StringUtils.isBlank(itemVerId)){
            throw new InvalidParameterException("参数itemVerId为空!");
        }
        if(itemPartformIds==null||(itemPartformIds!=null&&itemPartformIds.length==0)){
            throw new InvalidParameterException("参数itemPartformIds为空!");
        }
        if(itemPartformIds!=null && itemPartformIds.length>0){
            String userId = SecurityContext.getCurrentUserId();
            String rootOrgId = SecurityContext.getCurrentOrgId();
            AeaItemFrontPartform frontPartform;
            for(String itemPartformId:itemPartformIds){
                frontPartform = new AeaItemFrontPartform();
                frontPartform.setFrontPartformId(UUID.randomUUID().toString());
                frontPartform.setItemVerId(itemVerId);
                frontPartform.setItemPartformId(itemPartformId);
                frontPartform.setSortNo(getMaxSortNo(itemVerId, rootOrgId));
                frontPartform.setIsActive(Status.ON);
                frontPartform.setCreater(userId);
                frontPartform.setRootOrgId(rootOrgId);
                frontPartform.setCreateTime(new Date());
                aeaItemFrontPartformMapper.insertAeaItemFrontPartform(frontPartform);
            }
        }
    }

    @Override
    public void changIsActive(String id, String rootOrgId){

        aeaItemFrontPartformMapper.changIsActive(id, rootOrgId);
    }
}

