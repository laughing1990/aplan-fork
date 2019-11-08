package com.augurit.aplanmis.common.service.admin.item.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.common.domain.AeaItemOneform;
import com.augurit.aplanmis.common.mapper.AeaItemOneformMapper;
import com.augurit.aplanmis.common.service.admin.item.AeaItemOneformAdminService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* 事项与总表单关联表-Service服务接口实现类
*/
@Service
@Transactional
public class AeaItemOneformAdminServiceImpl implements AeaItemOneformAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaItemOneformAdminServiceImpl.class);

    @Autowired
    private AeaItemOneformMapper aeaItemOneformMapper;

    @Override
    public void saveAeaItemOneform(AeaItemOneform aeaItemOneform) {

        aeaItemOneformMapper.insertAeaItemOneform(aeaItemOneform);
    }

    @Override
    public void updateAeaItemOneform(AeaItemOneform aeaItemOneform) {

        aeaItemOneformMapper.updateAeaItemOneform(aeaItemOneform);
    }

    @Override
    public void deleteAeaItemOneformById(String id) {

        if(id == null)
        throw new InvalidParameterException(id);
        aeaItemOneformMapper.deleteAeaItemOneform(id);
    }

    @Override
    public PageInfo<AeaItemOneform> listAeaItemOneform(AeaItemOneform aeaItemOneform,Page page) {

        PageHelper.startPage(page);
        List<AeaItemOneform> list = aeaItemOneformMapper.listAeaItemOneform(aeaItemOneform);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaItemOneform>(list);
    }

    @Override
    public AeaItemOneform getAeaItemOneformById(String id) {

        if(id == null)
        throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaItemOneformMapper.getAeaItemOneformById(id);
    }

    @Override
    public List<AeaItemOneform> listAeaItemOneform(AeaItemOneform aeaItemOneform) {

        List<AeaItemOneform> list = aeaItemOneformMapper.listAeaItemOneform(aeaItemOneform);
        logger.debug("成功执行查询list！！");
        return list;
    }
}

