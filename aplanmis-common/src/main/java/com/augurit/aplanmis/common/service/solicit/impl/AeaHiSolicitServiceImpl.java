package com.augurit.aplanmis.common.service.solicit.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.common.domain.AeaHiSolicit;
import com.augurit.aplanmis.common.mapper.AeaHiSolicitMapper;
import com.augurit.aplanmis.common.service.solicit.AeaHiSolicitService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
* 征求意见主表-Service服务接口实现类
*/
@Service
@Transactional
public class AeaHiSolicitServiceImpl implements AeaHiSolicitService {

    private static Logger logger = LoggerFactory.getLogger(AeaHiSolicitServiceImpl.class);

    @Autowired
    private AeaHiSolicitMapper aeaHiSolicitMapper;

    public void saveAeaHiSolicit(AeaHiSolicit aeaHiSolicit) throws Exception{
        aeaHiSolicitMapper.insertAeaHiSolicit(aeaHiSolicit);
    }
    public void updateAeaHiSolicit(AeaHiSolicit aeaHiSolicit) throws Exception{
        aeaHiSolicitMapper.updateAeaHiSolicit(aeaHiSolicit);
    }
    public void deleteAeaHiSolicitById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        aeaHiSolicitMapper.deleteAeaHiSolicit(id);
    }
    public PageInfo<AeaHiSolicit> listAeaHiSolicit(AeaHiSolicit aeaHiSolicit,Page page) throws Exception{
        PageHelper.startPage(page);
        List<AeaHiSolicit> list = aeaHiSolicitMapper.listAeaHiSolicit(aeaHiSolicit);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaHiSolicit>(list);
    }
    public AeaHiSolicit getAeaHiSolicitById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaHiSolicitMapper.getAeaHiSolicitById(id);
    }
    public List<AeaHiSolicit> listAeaHiSolicit(AeaHiSolicit aeaHiSolicit) throws Exception{
        List<AeaHiSolicit> list = aeaHiSolicitMapper.listAeaHiSolicit(aeaHiSolicit);
        logger.debug("成功执行查询list！！");
        return list;
    }
}

