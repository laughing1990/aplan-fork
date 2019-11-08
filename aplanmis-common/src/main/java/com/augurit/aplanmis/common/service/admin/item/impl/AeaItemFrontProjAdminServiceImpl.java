package com.augurit.aplanmis.common.service.admin.item.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaItemFrontProj;
import com.augurit.aplanmis.common.mapper.AeaItemFrontProjMapper;
import com.augurit.aplanmis.common.service.admin.item.AeaItemFrontProjAdminService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* 项目信息前置检测-Service服务接口实现类
*/
@Service
@Transactional
public class AeaItemFrontProjAdminServiceImpl implements AeaItemFrontProjAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaItemFrontProjAdminServiceImpl.class);

    @Autowired
    private AeaItemFrontProjMapper aeaItemFrontProjMapper;

    @Override
    public void saveAeaItemFrontProj(AeaItemFrontProj aeaItemFrontProj) {
        aeaItemFrontProjMapper.insertAeaItemFrontProj(aeaItemFrontProj);
    }

    @Override
    public void updateAeaItemFrontProj(AeaItemFrontProj aeaItemFrontProj) {
        aeaItemFrontProjMapper.updateAeaItemFrontProj(aeaItemFrontProj);
    }

    @Override
    public void deleteAeaItemFrontProjById(String id) {

        if(StringUtils.isBlank(id)) {
            throw new InvalidParameterException(id);
        }
        aeaItemFrontProjMapper.deleteAeaItemFrontProj(id);
    }

    @Override
    public PageInfo<AeaItemFrontProj> listAeaItemFrontProj(AeaItemFrontProj aeaItemFrontProj,Page page) {

        PageHelper.startPage(page);
        List<AeaItemFrontProj> list = aeaItemFrontProjMapper.listAeaItemFrontProj(aeaItemFrontProj);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaItemFrontProj>(list);
    }

    @Override
    public AeaItemFrontProj getAeaItemFrontProjById(String id) {

        if(StringUtils.isBlank(id)) {
            throw new InvalidParameterException(id);
        }
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaItemFrontProjMapper.getAeaItemFrontProjById(id);
    }

    @Override
    public List<AeaItemFrontProj> listAeaItemFrontProj(AeaItemFrontProj aeaItemFrontProj) {

        List<AeaItemFrontProj> list = aeaItemFrontProjMapper.listAeaItemFrontProj(aeaItemFrontProj);
        logger.debug("成功执行查询list！！");
        return list;
    }
}

