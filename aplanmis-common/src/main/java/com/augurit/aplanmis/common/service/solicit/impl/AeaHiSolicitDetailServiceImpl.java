package com.augurit.aplanmis.common.service.solicit.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.common.domain.AeaHiSolicitDetail;
import com.augurit.aplanmis.common.mapper.AeaHiSolicitDetailMapper;
import com.augurit.aplanmis.common.service.solicit.AeaHiSolicitDetailService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
* 征求意见详情表-Service服务接口实现类
*/
@Service
@Transactional
public class AeaHiSolicitDetailServiceImpl implements AeaHiSolicitDetailService {

    private static Logger logger = LoggerFactory.getLogger(AeaHiSolicitDetailServiceImpl.class);

    @Autowired
    private AeaHiSolicitDetailMapper aeaHiSolicitDetailMapper;

    public void saveAeaHiSolicitDetail(AeaHiSolicitDetail aeaHiSolicitDetail) throws Exception{
        aeaHiSolicitDetailMapper.insertAeaHiSolicitDetail(aeaHiSolicitDetail);
    }
    public void updateAeaHiSolicitDetail(AeaHiSolicitDetail aeaHiSolicitDetail) throws Exception{
        aeaHiSolicitDetailMapper.updateAeaHiSolicitDetail(aeaHiSolicitDetail);
    }
    public void deleteAeaHiSolicitDetailById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        aeaHiSolicitDetailMapper.deleteAeaHiSolicitDetail(id);
    }
    public PageInfo<AeaHiSolicitDetail> listAeaHiSolicitDetail(AeaHiSolicitDetail aeaHiSolicitDetail,Page page) throws Exception{
        PageHelper.startPage(page);
        List<AeaHiSolicitDetail> list = aeaHiSolicitDetailMapper.listAeaHiSolicitDetail(aeaHiSolicitDetail);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaHiSolicitDetail>(list);
    }
    public AeaHiSolicitDetail getAeaHiSolicitDetailById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaHiSolicitDetailMapper.getAeaHiSolicitDetailById(id);
    }
    public List<AeaHiSolicitDetail> listAeaHiSolicitDetail(AeaHiSolicitDetail aeaHiSolicitDetail) throws Exception{
        List<AeaHiSolicitDetail> list = aeaHiSolicitDetailMapper.listAeaHiSolicitDetail(aeaHiSolicitDetail);
        logger.debug("成功执行查询list！！");
        return list;
    }
}

