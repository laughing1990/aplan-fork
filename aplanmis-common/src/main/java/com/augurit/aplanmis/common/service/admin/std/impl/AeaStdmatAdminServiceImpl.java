package com.augurit.aplanmis.common.service.admin.std.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ActiveStatus;
import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.domain.AeaStdmat;
import com.augurit.aplanmis.common.mapper.AeaStdmatMapper;
import com.augurit.aplanmis.common.service.admin.std.AeaStdmatAdminService;
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
* 标准材料定义表-Service服务接口实现类
*/
@Service
@Transactional
public class AeaStdmatAdminServiceImpl implements AeaStdmatAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaStdmatAdminServiceImpl.class);

    @Autowired
    private AeaStdmatMapper aeaStdmatMapper;

    @Override
    public void saveAeaStdmat(AeaStdmat aeaStdmat) {

        String rootOrgId = SecurityContext.getCurrentOrgId();
        String userId = SecurityContext.getCurrentUserId();
        aeaStdmat.setIsActive(ActiveStatus.ACTIVE.getValue());
        aeaStdmat.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
        aeaStdmat.setCreater(userId);
        aeaStdmat.setRootOrgId(rootOrgId);
        aeaStdmat.setCreateTime(new Date());
        aeaStdmatMapper.insertAeaStdmat(aeaStdmat);
    }

    @Override
    public void updateAeaStdmat(AeaStdmat aeaStdmat) {

        aeaStdmat.setModifier(SecurityContext.getCurrentUserId());
        aeaStdmat.setModifyTime(new Date());
        aeaStdmatMapper.updateAeaStdmat(aeaStdmat);
    }

    @Override
    public void deleteAeaStdmatById(String id) {
        
        if(StringUtils.isBlank(id)) {
            throw new InvalidParameterException("参数id为空!");
        }
        aeaStdmatMapper.deleteAeaStdmat(id);
    }

    @Override
    public PageInfo<AeaStdmat> listAeaStdmat(AeaStdmat aeaStdmat, Page page) {

        aeaStdmat.setIsActive(ActiveStatus.ACTIVE.getValue());
        aeaStdmat.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaStdmat> list = aeaStdmatMapper.listAeaStdmat(aeaStdmat);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaStdmat>(list);
    }

    @Override
    public AeaStdmat getAeaStdmatById(String id) {

        if(StringUtils.isBlank(id)) {
            throw new InvalidParameterException("参数id为空!");
        }
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaStdmatMapper.getAeaStdmatById(id);
    }

    @Override
    public List<AeaStdmat> listAeaStdmat(AeaStdmat aeaStdmat) {

        aeaStdmat.setIsActive(ActiveStatus.ACTIVE.getValue());
        aeaStdmat.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaStdmat> list = aeaStdmatMapper.listAeaStdmat(aeaStdmat);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public boolean checkUniqueCode(String stdmatId, String stdmatCode) {

        String rootOrgId = SecurityContext.getCurrentOrgId();
        Long count = aeaStdmatMapper.checkUniqueCode(stdmatId, stdmatCode, rootOrgId);
        return count == null || count <= 0;
    }

    @Override
    public Long getMaxSortNo(String rootOrgId) {

        Long sortNo = aeaStdmatMapper.getMaxSortNo(rootOrgId);
        return sortNo == null ? 1L : sortNo + 1;
    }
}

