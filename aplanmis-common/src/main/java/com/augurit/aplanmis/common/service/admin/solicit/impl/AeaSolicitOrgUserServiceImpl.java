package com.augurit.aplanmis.common.service.admin.solicit.impl;

import ch.qos.logback.core.util.StringCollectionUtil;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaSolicitOrgUser;
import com.augurit.aplanmis.common.mapper.AeaSolicitOrgUserMapper;
import com.augurit.aplanmis.common.service.admin.solicit.AeaSolicitOrgUserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.swing.StringUIClientPropertyKey;

import java.util.List;

/**
* 按组织征求的人员名单表-Service服务接口实现类
*/
@Service
@Transactional
public class AeaSolicitOrgUserServiceImpl implements AeaSolicitOrgUserService {

    private static Logger logger = LoggerFactory.getLogger(AeaSolicitOrgUserServiceImpl.class);

    @Autowired
    private AeaSolicitOrgUserMapper aeaSolicitOrgUserMapper;

    @Override
    public void saveAeaSolicitOrgUser(AeaSolicitOrgUser aeaSolicitOrgUser) {

        aeaSolicitOrgUserMapper.insertAeaSolicitOrgUser(aeaSolicitOrgUser);
    }

    @Override
    public void updateAeaSolicitOrgUser(AeaSolicitOrgUser aeaSolicitOrgUser) {

        aeaSolicitOrgUserMapper.updateAeaSolicitOrgUser(aeaSolicitOrgUser);
    }

    @Override
    public void deleteAeaSolicitOrgUserById(String id) {

        if(StringUtils.isBlank(id)){
            throw new InvalidParameterException("参数id为空!");
        }
        aeaSolicitOrgUserMapper.deleteAeaSolicitOrgUser(id);
    }

    @Override
    public PageInfo<AeaSolicitOrgUser> listAeaSolicitOrgUser(AeaSolicitOrgUser aeaSolicitOrgUser,Page page) {

        PageHelper.startPage(page);
        List<AeaSolicitOrgUser> list = aeaSolicitOrgUserMapper.listAeaSolicitOrgUser(aeaSolicitOrgUser);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaSolicitOrgUser>(list);
    }

    @Override
    public AeaSolicitOrgUser getAeaSolicitOrgUserById(String id) {

        if(StringUtils.isBlank(id)){
            throw new InvalidParameterException("参数id为空!");
        }
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaSolicitOrgUserMapper.getAeaSolicitOrgUserById(id);
    }

    @Override
    public List<AeaSolicitOrgUser> listAeaSolicitOrgUser(AeaSolicitOrgUser aeaSolicitOrgUser) {

        List<AeaSolicitOrgUser> list = aeaSolicitOrgUserMapper.listAeaSolicitOrgUser(aeaSolicitOrgUser);
        logger.debug("成功执行查询list！！");
        return list;
    }
}

