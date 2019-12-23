package com.augurit.aplanmis.common.service.admin.solicit.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaSolicitItemUser;
import com.augurit.aplanmis.common.mapper.AeaSolicitItemUserMapper;
import com.augurit.aplanmis.common.service.admin.solicit.AeaSolicitItemUserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
* 按事项征求的人员名单表-Service服务接口实现类
*/
@Service
@Transactional
public class AeaSolicitItemUserServiceImpl implements AeaSolicitItemUserService {

    private static Logger logger = LoggerFactory.getLogger(AeaSolicitItemUserServiceImpl.class);

    @Autowired
    private AeaSolicitItemUserMapper aeaSolicitItemUserMapper;

    @Override
    public void saveAeaSolicitItemUser(AeaSolicitItemUser aeaSolicitItemUser) {

        aeaSolicitItemUserMapper.insertAeaSolicitItemUser(aeaSolicitItemUser);
    }

    @Override
    public void updateAeaSolicitItemUser(AeaSolicitItemUser aeaSolicitItemUser) {

        aeaSolicitItemUserMapper.updateAeaSolicitItemUser(aeaSolicitItemUser);
    }

    @Override
    public void deleteAeaSolicitItemUserById(String id) {

        if(StringUtils.isBlank(id)){
            throw new InvalidParameterException("参数id为空!");
        }
        aeaSolicitItemUserMapper.deleteAeaSolicitItemUser(id);
    }

    @Override
    public PageInfo<AeaSolicitItemUser> listAeaSolicitItemUser(AeaSolicitItemUser aeaSolicitItemUser,Page page) {

        PageHelper.startPage(page);
        List<AeaSolicitItemUser> list = aeaSolicitItemUserMapper.listAeaSolicitItemUser(aeaSolicitItemUser);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaSolicitItemUser>(list);
    }

    @Override
    public AeaSolicitItemUser getAeaSolicitItemUserById(String id) {

        if(StringUtils.isBlank(id)){
            throw new InvalidParameterException("参数id为空!");
        }
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaSolicitItemUserMapper.getAeaSolicitItemUserById(id);
    }

    @Override
    public List<AeaSolicitItemUser> listAeaSolicitItemUser(AeaSolicitItemUser aeaSolicitItemUser) {

        List<AeaSolicitItemUser> list = aeaSolicitItemUserMapper.listAeaSolicitItemUser(aeaSolicitItemUser);
        logger.debug("成功执行查询list！！");
        return list;
    }
}

