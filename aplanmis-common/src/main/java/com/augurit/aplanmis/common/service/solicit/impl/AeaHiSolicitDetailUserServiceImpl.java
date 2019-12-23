package com.augurit.aplanmis.common.service.solicit.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.common.domain.AeaHiSolicitDetailUser;
import com.augurit.aplanmis.common.mapper.AeaHiSolicitDetailUserMapper;
import com.augurit.aplanmis.common.service.solicit.AeaHiSolicitDetailUserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
* 征求意见详情用户任务表-Service服务接口实现类
*/
@Service
@Transactional
public class AeaHiSolicitDetailUserServiceImpl implements AeaHiSolicitDetailUserService {

    private static Logger logger = LoggerFactory.getLogger(AeaHiSolicitDetailUserServiceImpl.class);

    @Autowired
    private AeaHiSolicitDetailUserMapper aeaHiSolicitDetailUserMapper;

    public void saveAeaHiSolicitDetailUser(AeaHiSolicitDetailUser aeaHiSolicitDetailUser) throws Exception{
        aeaHiSolicitDetailUserMapper.insertAeaHiSolicitDetailUser(aeaHiSolicitDetailUser);
    }
    public void updateAeaHiSolicitDetailUser(AeaHiSolicitDetailUser aeaHiSolicitDetailUser) throws Exception{
        aeaHiSolicitDetailUserMapper.updateAeaHiSolicitDetailUser(aeaHiSolicitDetailUser);
    }
    public void deleteAeaHiSolicitDetailUserById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        aeaHiSolicitDetailUserMapper.deleteAeaHiSolicitDetailUser(id);
    }
    public PageInfo<AeaHiSolicitDetailUser> listAeaHiSolicitDetailUser(AeaHiSolicitDetailUser aeaHiSolicitDetailUser,Page page) throws Exception{
        PageHelper.startPage(page);
        List<AeaHiSolicitDetailUser> list = aeaHiSolicitDetailUserMapper.listAeaHiSolicitDetailUser(aeaHiSolicitDetailUser);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaHiSolicitDetailUser>(list);
    }
    public AeaHiSolicitDetailUser getAeaHiSolicitDetailUserById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaHiSolicitDetailUserMapper.getAeaHiSolicitDetailUserById(id);
    }
    public List<AeaHiSolicitDetailUser> listAeaHiSolicitDetailUser(AeaHiSolicitDetailUser aeaHiSolicitDetailUser) throws Exception{
        List<AeaHiSolicitDetailUser> list = aeaHiSolicitDetailUserMapper.listAeaHiSolicitDetailUser(aeaHiSolicitDetailUser);
        logger.debug("成功执行查询list！！");
        return list;
    }
}

