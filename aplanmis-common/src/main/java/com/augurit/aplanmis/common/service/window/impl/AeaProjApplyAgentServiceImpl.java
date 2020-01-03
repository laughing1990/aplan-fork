package com.augurit.aplanmis.common.service.window.impl;

import com.augurit.aplanmis.common.domain.AeaProjApplyAgent;
import com.augurit.aplanmis.common.mapper.AeaProjApplyAgentMapper;
import com.augurit.aplanmis.common.service.window.AeaProjApplyAgentService;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
/**
* 项目代办申请表-Service服务接口实现类
*/
@Service
@Transactional
public class AeaProjApplyAgentServiceImpl implements AeaProjApplyAgentService {

    private static Logger logger = LoggerFactory.getLogger(AeaProjApplyAgentServiceImpl.class);

    @Autowired
    private AeaProjApplyAgentMapper aeaProjApplyAgentMapper;

    public void saveAeaProjApplyAgent(AeaProjApplyAgent aeaProjApplyAgent) throws Exception{
        aeaProjApplyAgentMapper.insertAeaProjApplyAgent(aeaProjApplyAgent);
    }
    public void updateAeaProjApplyAgent(AeaProjApplyAgent aeaProjApplyAgent) throws Exception{
        aeaProjApplyAgentMapper.updateAeaProjApplyAgent(aeaProjApplyAgent);
    }
    public void deleteAeaProjApplyAgentById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        aeaProjApplyAgentMapper.deleteAeaProjApplyAgent(id);
    }
    public PageInfo<AeaProjApplyAgent> listAeaProjApplyAgent(AeaProjApplyAgent aeaProjApplyAgent,Page page) throws Exception{
        PageHelper.startPage(page);
        List<AeaProjApplyAgent> list = aeaProjApplyAgentMapper.listAeaProjApplyAgent(aeaProjApplyAgent);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaProjApplyAgent>(list);
    }
    public AeaProjApplyAgent getAeaProjApplyAgentById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaProjApplyAgentMapper.getAeaProjApplyAgentById(id);
    }
    public List<AeaProjApplyAgent> listAeaProjApplyAgent(AeaProjApplyAgent aeaProjApplyAgent) throws Exception{
        List<AeaProjApplyAgent> list = aeaProjApplyAgentMapper.listAeaProjApplyAgent(aeaProjApplyAgent);
        logger.debug("成功执行查询list！！");
        return list;
    }

    public PageInfo<AeaProjApplyAgent> listAeaProjApplyAgentByConditional(AeaProjApplyAgent aeaProjApplyAgent, Page page) throws Exception{
        PageHelper.startPage(page);
        List<AeaProjApplyAgent> list = aeaProjApplyAgentMapper.listAeaProjApplyAgentByConditional(aeaProjApplyAgent);
        logger.debug("成功执行查询list！！");
        return new PageInfo<>(list);
    }
}

