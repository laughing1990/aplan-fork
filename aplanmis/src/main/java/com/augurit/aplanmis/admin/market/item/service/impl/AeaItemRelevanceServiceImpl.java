package com.augurit.aplanmis.admin.market.item.service.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.admin.market.item.service.AeaItemRelevanceService;
import com.augurit.aplanmis.common.domain.AeaItemRelevance;
import com.augurit.aplanmis.common.mapper.AeaItemRelevanceMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* -Service服务接口实现类
*/
@Service
@Transactional
public class AeaItemRelevanceServiceImpl implements AeaItemRelevanceService {

    private static Logger logger = LoggerFactory.getLogger(AeaItemRelevanceServiceImpl.class);

    @Autowired
    private AeaItemRelevanceMapper aeaItemRelevanceMapper;

    @Override
    public void saveAeaItemRelevance(AeaItemRelevance aeaItemRelevance) throws Exception{
        aeaItemRelevanceMapper.insertAeaItemRelevance(aeaItemRelevance);
    }
    @Override
    public void updateAeaItemRelevance(AeaItemRelevance aeaItemRelevance) throws Exception{
        aeaItemRelevanceMapper.updateAeaItemRelevance(aeaItemRelevance);
    }
    @Override
    public void deleteAeaItemRelevanceById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        aeaItemRelevanceMapper.deleteAeaItemRelevance(id);
    }
    @Override
    public PageInfo<AeaItemRelevance> listAeaItemRelevance(AeaItemRelevance aeaItemRelevance, Page page) throws Exception{
        PageHelper.startPage(page);
        List<AeaItemRelevance> list = aeaItemRelevanceMapper.listAeaItemRelevance(aeaItemRelevance);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaItemRelevance>(list);
    }
    @Override
    public AeaItemRelevance getAeaItemRelevanceById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaItemRelevanceMapper.getAeaItemRelevanceById(id);
    }
    @Override
    public List<AeaItemRelevance> listAeaItemRelevance(AeaItemRelevance aeaItemRelevance) throws Exception{
        List<AeaItemRelevance> list = aeaItemRelevanceMapper.listAeaItemRelevance(aeaItemRelevance);
        logger.debug("成功执行查询list！！");
        return list;
    }
}

