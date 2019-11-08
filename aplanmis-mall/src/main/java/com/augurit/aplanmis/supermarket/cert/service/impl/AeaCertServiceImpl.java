package com.augurit.aplanmis.supermarket.cert.service.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.common.domain.AeaCert;
import com.augurit.aplanmis.common.mapper.AeaCertMapper;
import com.augurit.aplanmis.supermarket.cert.service.AeaCertService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* 电子证照定义表-Service服务接口实现类
*/
@Service
@Transactional
public class AeaCertServiceImpl implements AeaCertService {

    private static Logger logger = LoggerFactory.getLogger(AeaCertServiceImpl.class);

    @Autowired
    private AeaCertMapper aeaCertMapper;

    public void saveAeaCert(AeaCert aeaCert) throws Exception{
        aeaCertMapper.insertOne(aeaCert);
    }
    public void updateAeaCert(AeaCert aeaCert) throws Exception{
        aeaCertMapper.updateOne(aeaCert);
    }
    public void deleteAeaCertById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        aeaCertMapper.deleteById(id);
    }
    public PageInfo<AeaCert> listAeaCert(AeaCert aeaCert, Page page) throws Exception{
        PageHelper.startPage(page);
        List<AeaCert> list = aeaCertMapper.listAeaCert(aeaCert);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaCert>(list);
    }
    public List<AeaCert> listAeaCert(AeaCert aeaCert) throws Exception{
        List<AeaCert> list = aeaCertMapper.listAeaCert(aeaCert);
        logger.debug("成功执行查询list！！");
        return list;
    }
}

