package com.augurit.aplanmis.common.service.admin.item.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaItemPartform;
import com.augurit.aplanmis.common.mapper.AeaItemPartformMapper;
import com.augurit.aplanmis.common.service.admin.item.AeaItemPartformAdminService;
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
* 事项与扩展表单关联表-Service服务接口实现类
*/
@Service
@Transactional
public class AeaItemPartformAdminServiceImpl implements AeaItemPartformAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaItemPartformAdminServiceImpl.class);

    @Autowired
    private AeaItemPartformMapper aeaItemPartformMapper;

    @Override
    public PageInfo<AeaItemPartform> listPartFormNoSelectFormByPage(AeaItemPartform partform, Page page){

        partform.setFormOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaItemPartform> list = aeaItemPartformMapper.listItemPartFormNoSelectForm(partform);
        return new PageInfo<>(list);
    }

    @Override
    public void batchDelItemPartform(String[] ids) {

        if (ids != null && ids.length > 0) {
            aeaItemPartformMapper.batchDelItemPartformByIds(ids);
        } else {
            throw new InvalidParameterException("参数ids为空!");
        }
    }

    @Override
    public Long getMaxSortNo(String itemVerId) {
        Long sortNo = aeaItemPartformMapper.getMaxSortNo(itemVerId);
        return sortNo == null ? 1L : (sortNo + 1L);
    }

    @Override
    public void saveAeaItemPartform(AeaItemPartform aeaItemPartform) {

        aeaItemPartform.setCreater(SecurityContext.getCurrentUserId());
        aeaItemPartform.setCreateTime(new Date());
        aeaItemPartformMapper.insertAeaItemPartform(aeaItemPartform);
    }

    @Override
    public void updateAeaItemPartform(AeaItemPartform aeaItemPartform) {
        aeaItemPartformMapper.updateAeaItemPartform(aeaItemPartform);
    }

    @Override
    public void deleteAeaItemPartformById(String id) {
        if(id == null) {
            throw new InvalidParameterException(id);
        }
        aeaItemPartformMapper.deleteAeaItemPartform(id);
    }

    @Override
    public PageInfo<AeaItemPartform> listAeaItemPartform(AeaItemPartform aeaItemPartform,Page page) {
        PageHelper.startPage(page);
        List<AeaItemPartform> list = aeaItemPartformMapper.listAeaItemPartformWithFormInfo(aeaItemPartform);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public List<AeaItemPartform> listAeaItemPartformNoPage(AeaItemPartform aeaItemPartform) {

        List<AeaItemPartform> list = aeaItemPartformMapper.listAeaItemPartformWithFormInfo(aeaItemPartform);
        logger.debug("成功执行分页查询！！");
        return list;
    }

    @Override
    public AeaItemPartform getAeaItemPartformById(String id) {
        if(id == null) {
            throw new InvalidParameterException(id);
        }
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaItemPartformMapper.getAeaItemPartformById(id);
    }

    @Override
    public List<AeaItemPartform> listAeaItemPartform(AeaItemPartform aeaItemPartform) {
        List<AeaItemPartform> list = aeaItemPartformMapper.listAeaItemPartform(aeaItemPartform);
        logger.debug("成功执行查询list！！");
        return list;
    }
}

