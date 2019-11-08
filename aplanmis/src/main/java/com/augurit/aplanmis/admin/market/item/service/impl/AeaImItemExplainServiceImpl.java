package com.augurit.aplanmis.admin.market.item.service.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.admin.market.item.service.AeaImItemExplainService;
import com.augurit.aplanmis.common.domain.AeaImItemExplain;
import com.augurit.aplanmis.common.mapper.AeaImItemExplainMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
* -Service服务接口实现类
*/
@Service
@Transactional
public class AeaImItemExplainServiceImpl implements AeaImItemExplainService {

    private static Logger logger = LoggerFactory.getLogger(AeaImItemExplainServiceImpl.class);

    @Autowired
    private AeaImItemExplainMapper aeaImItemExplainMapper;

    @Override
    public void saveAeaImItemExplain(AeaImItemExplain aeaImItemExplain) throws Exception{
        aeaImItemExplain.setItemExplainId(UUID.randomUUID().toString());
        aeaImItemExplainMapper.insertAeaImItemExplain(aeaImItemExplain);
    }
    @Override
    public void updateAeaImItemExplain(AeaImItemExplain aeaImItemExplain) throws Exception{
        aeaImItemExplainMapper.updateAeaImItemExplain(aeaImItemExplain);
    }
    @Override
    public void deleteAeaImItemExplainById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        aeaImItemExplainMapper.deleteAeaImItemExplain(id);
    }
    @Override
    public PageInfo<AeaImItemExplain> listAeaImItemExplain(AeaImItemExplain aeaImItemExplain, Page page) throws Exception{
        PageHelper.startPage(page);
        List<AeaImItemExplain> list = aeaImItemExplainMapper.listAeaImItemExplain(aeaImItemExplain);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaImItemExplain>(list);
    }
    @Override
    public AeaImItemExplain getAeaImItemExplainById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaImItemExplainMapper.getAeaImItemExplainById(id);
    }
    @Override
    public List<AeaImItemExplain> listAeaImItemExplain(AeaImItemExplain aeaImItemExplain) throws Exception{
        List<AeaImItemExplain> list = aeaImItemExplainMapper.listAeaImItemExplain(aeaImItemExplain);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public AeaImItemExplain getAeaImItemExplainByItemVerId(String itemVerId) throws Exception {

        return aeaImItemExplainMapper.getAeaImItemExplainByItemVerId(itemVerId);
    }
}

