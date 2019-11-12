package com.augurit.aplanmis.common.service.admin.item.impl;

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
        checkSame(aeaItemFrontPartform);

        aeaItemFrontPartform.setCreateTime(new Date());
        aeaItemFrontPartform.setCreater(SecurityContext.getCurrentUserId());
        aeaItemFrontPartform.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaItemFrontPartformMapper.insertAeaItemFrontPartform(aeaItemFrontPartform);
    }

    @Override
    public void updateAeaItemFrontPartform(AeaItemFrontPartform aeaItemFrontPartform) {

        checkSame(aeaItemFrontPartform);

        aeaItemFrontPartform.setModifyTime(new Date());
        aeaItemFrontPartform.setModifier(SecurityContext.getCurrentUserId());
        aeaItemFrontPartformMapper.updateAeaItemFrontPartform(aeaItemFrontPartform);
    }

    @Override
    public void deleteAeaItemFrontPartformById(String id) {

        if (StringUtils.isBlank(id)) {
            throw new InvalidParameterException(id);
        }
        String[] ids = id.split(",");
        for (String frontPartformId : ids) {
            aeaItemFrontPartformMapper.deleteAeaItemFrontPartform(frontPartformId);
        }
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

        if (id == null)
            throw new InvalidParameterException(id);
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
        PageHelper.startPage(page);
        List<AeaItemFrontPartformVo> list = aeaItemFrontPartformMapper.listAeaItemFrontPartformVo(aeaItemFrontPartform);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public Long getMaxSortNo(AeaItemFrontPartform aeaItemFrontPartform) {
        Long sortNo = aeaItemFrontPartformMapper.getMaxSortNo(aeaItemFrontPartform);
        if (sortNo == null) {
            sortNo = 1l;
        } else {
            sortNo = sortNo + 1;
        }

        return sortNo;
    }

    @Override
    public PageInfo<AeaItemFrontPartformVo> listSelectItemFrontPartformByPage(AeaItemFrontPartform aeaItemFrontPartform, Page page) {
        PageHelper.startPage(page);
        List<AeaItemFrontPartformVo> list = aeaItemFrontPartformMapper.listSelectItemFrontPartform(aeaItemFrontPartform);
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
        if (StringUtils.isBlank(itemVerId)) return aeaItemFrontPartformVos;
        aeaItemFrontPartformVos.addAll(aeaItemFrontPartformMapper.getAeaItemFrontPartformVoByItemVerId(itemVerId, SecurityContext.getCurrentOrgId()));
        return aeaItemFrontPartformVos;
    }
}

