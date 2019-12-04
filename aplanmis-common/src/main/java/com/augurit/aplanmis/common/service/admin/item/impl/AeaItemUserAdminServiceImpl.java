package com.augurit.aplanmis.common.service.admin.item.impl;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaItemUser;
import com.augurit.aplanmis.common.mapper.AeaItemUserMapper;
import com.augurit.aplanmis.common.service.admin.item.AeaItemUserAdminService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
* 用户事项管理-Service服务接口实现类
*/
@Service
@Transactional
public class AeaItemUserAdminServiceImpl implements AeaItemUserAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaItemUserAdminServiceImpl.class);

    @Autowired
    private AeaItemUserMapper aeaItemUserMapper;

    @Override
    public void saveAeaItemUser(AeaItemUser aeaItemUser) {

        aeaItemUser.setIsActive(Status.ON);
        aeaItemUser.setCreateTime(new Date());
        aeaItemUser.setCreater(SecurityContext.getCurrentUserId());
        aeaItemUser.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaItemUserMapper.insertAeaItemUser(aeaItemUser);
    }

    @Override
    public void updateAeaItemUser(AeaItemUser aeaItemUser) {

        aeaItemUserMapper.updateAeaItemUser(aeaItemUser);
    }

    @Override
    public void deleteAeaItemUserById(String id) {

        if(StringUtils.isBlank(id)) {
            throw new InvalidParameterException("参数id为空!");
        }
        aeaItemUserMapper.deleteAeaItemUser(id);
    }

    @Override
    public PageInfo<AeaItemUser> listAeaItemUser(AeaItemUser aeaItemUser,Page page) {

        PageHelper.startPage(page);
        List<AeaItemUser> list = aeaItemUserMapper.listAeaItemUser(aeaItemUser);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaItemUser>(list);
    }

    @Override
    public AeaItemUser getAeaItemUserById(String id) {

        if(StringUtils.isBlank(id)) {
            throw new InvalidParameterException("参数id为空!");
        }
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaItemUserMapper.getAeaItemUserById(id);
    }

    @Override
    public List<AeaItemUser> listAeaItemUser(AeaItemUser aeaItemUser) {

        List<AeaItemUser> list = aeaItemUserMapper.listAeaItemUser(aeaItemUser);
        logger.debug("成功执行查询list！！");
        return list;
    }
}

