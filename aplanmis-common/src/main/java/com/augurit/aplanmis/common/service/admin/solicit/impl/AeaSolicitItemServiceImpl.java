package com.augurit.aplanmis.common.service.admin.solicit.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaSolicitItem;
import com.augurit.aplanmis.common.mapper.AeaSolicitItemMapper;
import com.augurit.aplanmis.common.service.admin.solicit.AeaSolicitItemService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* 按事项征求配置表-Service服务接口实现类
*/
@Service
@Transactional
public class AeaSolicitItemServiceImpl implements AeaSolicitItemService {

    private static Logger logger = LoggerFactory.getLogger(AeaSolicitItemServiceImpl.class);

    @Autowired
    private AeaSolicitItemMapper aeaSolicitItemMapper;

    @Override
    public void saveAeaSolicitItem(AeaSolicitItem aeaSolicitItem) {

        aeaSolicitItemMapper.insertAeaSolicitItem(aeaSolicitItem);
    }

    @Override
    public void updateAeaSolicitItem(AeaSolicitItem aeaSolicitItem) {

        aeaSolicitItemMapper.updateAeaSolicitItem(aeaSolicitItem);
    }

    @Override
    public void deleteAeaSolicitItemById(String id) {

        if(StringUtils.isBlank(id)){
            throw new InvalidParameterException("参数id为空!");
        }
        aeaSolicitItemMapper.deleteAeaSolicitItem(id);
    }

    @Override
    public PageInfo<AeaSolicitItem> listAeaSolicitItem(AeaSolicitItem aeaSolicitItem,Page page) {

        PageHelper.startPage(page);
        List<AeaSolicitItem> list = aeaSolicitItemMapper.listAeaSolicitItem(aeaSolicitItem);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaSolicitItem>(list);
    }

    @Override
    public AeaSolicitItem getAeaSolicitItemById(String id) {

        if(StringUtils.isBlank(id)){
            throw new InvalidParameterException("参数id为空!");
        }
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaSolicitItemMapper.getAeaSolicitItemById(id);
    }

    @Override
    public List<AeaSolicitItem> listAeaSolicitItem(AeaSolicitItem aeaSolicitItem) {

        List<AeaSolicitItem> list = aeaSolicitItemMapper.listAeaSolicitItem(aeaSolicitItem);
        logger.debug("成功执行查询list！！");
        return list;
    }
}

