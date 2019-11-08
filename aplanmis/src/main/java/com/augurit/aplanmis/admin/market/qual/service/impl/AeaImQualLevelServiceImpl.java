package com.augurit.aplanmis.admin.market.qual.service.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.admin.market.qual.service.AeaImQualLevelService;
import com.augurit.aplanmis.common.domain.AeaImQualLevel;
import com.augurit.aplanmis.common.mapper.AeaImQualLevelMapper;
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
public class AeaImQualLevelServiceImpl implements AeaImQualLevelService {

    private static Logger logger = LoggerFactory.getLogger(AeaImQualLevelServiceImpl.class);

    @Autowired
    private AeaImQualLevelMapper aeaImQualLevelMapper;

    @Override
    public void saveAeaImQualLevel(AeaImQualLevel aeaImQualLevel) throws Exception{
        aeaImQualLevelMapper.insertAeaImQualLevel(aeaImQualLevel);
    }
    @Override
    public void updateAeaImQualLevel(AeaImQualLevel aeaImQualLevel) throws Exception{
        aeaImQualLevelMapper.updateAeaImQualLevel(aeaImQualLevel);
    }
    @Override
    public void deleteAeaImQualLevelById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        aeaImQualLevelMapper.deleteAeaImQualLevel(id);
    }
    @Override
    public PageInfo<AeaImQualLevel> listAeaImQualLevel(AeaImQualLevel aeaImQualLevel, Page page) throws Exception{
        PageHelper.startPage(page);
        List<AeaImQualLevel> list = aeaImQualLevelMapper.listAeaImQualLevel(aeaImQualLevel);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaImQualLevel>(list);
    }
    @Override
    public AeaImQualLevel getAeaImQualLevelById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaImQualLevelMapper.getAeaImQualLevelById(id);
    }
    @Override
    public List<AeaImQualLevel> listAeaImQualLevel(AeaImQualLevel aeaImQualLevel) throws Exception{
        List<AeaImQualLevel> list = aeaImQualLevelMapper.listAeaImQualLevel(aeaImQualLevel);
        logger.debug("成功执行查询list！！");
        return list;
    }
}

