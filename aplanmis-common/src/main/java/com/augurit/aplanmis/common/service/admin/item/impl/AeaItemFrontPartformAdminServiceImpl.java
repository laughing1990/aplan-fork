package com.augurit.aplanmis.common.service.admin.item.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.common.domain.AeaItemFrontPartform;
import com.augurit.aplanmis.common.mapper.AeaItemFrontPartformMapper;
import com.augurit.aplanmis.common.service.admin.item.AeaItemFrontPartformAdminService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        aeaItemFrontPartformMapper.insertAeaItemFrontPartform(aeaItemFrontPartform);
    }

    @Override
    public void updateAeaItemFrontPartform(AeaItemFrontPartform aeaItemFrontPartform) {

        aeaItemFrontPartformMapper.updateAeaItemFrontPartform(aeaItemFrontPartform);
    }

    @Override
    public void deleteAeaItemFrontPartformById(String id) {

        if(id == null) {
            throw new InvalidParameterException(id);
        }
        aeaItemFrontPartformMapper.deleteAeaItemFrontPartform(id);
    }

    @Override
    public PageInfo<AeaItemFrontPartform> listAeaItemFrontPartform(AeaItemFrontPartform aeaItemFrontPartform,Page page) {

        PageHelper.startPage(page);
        List<AeaItemFrontPartform> list = aeaItemFrontPartformMapper.listAeaItemFrontPartform(aeaItemFrontPartform);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaItemFrontPartform>(list);
    }

    @Override
    public AeaItemFrontPartform getAeaItemFrontPartformById(String id) {

        if(id == null)
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
}

