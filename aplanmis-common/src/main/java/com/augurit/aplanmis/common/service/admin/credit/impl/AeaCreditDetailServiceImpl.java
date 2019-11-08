package com.augurit.aplanmis.common.service.admin.credit.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaCreditDetail;
import com.augurit.aplanmis.common.mapper.AeaCreditDetailMapper;
import com.augurit.aplanmis.common.service.admin.credit.AeaCreditDetailService;
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
* 信用管理-信用汇总子表（字段列表）-Service服务接口实现类
*/
@Service
@Transactional
public class AeaCreditDetailServiceImpl implements AeaCreditDetailService {

    private static Logger logger = LoggerFactory.getLogger(AeaCreditDetailServiceImpl.class);

    @Autowired
    private AeaCreditDetailMapper aeaCreditDetailMapper;

    @Override
    public void saveAeaCreditDetail(AeaCreditDetail aeaCreditDetail) {

        aeaCreditDetail.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaCreditDetail.setCreater(SecurityContext.getCurrentUserName());
        aeaCreditDetail.setCreateTime(new Date());
        aeaCreditDetailMapper.insertAeaCreditDetail(aeaCreditDetail);
    }

    @Override
    public void updateAeaCreditDetail(AeaCreditDetail aeaCreditDetail) {
        aeaCreditDetail.setModifier(SecurityContext.getCurrentUserName());
        aeaCreditDetail.setModifyTime(new Date());
        aeaCreditDetailMapper.updateAeaCreditDetail(aeaCreditDetail);
    }

    @Override
    public void deleteAeaCreditDetailById(String ids) {

        if (StringUtils.isBlank(ids)) {
            throw new InvalidParameterException("参数id为空!");
        }
        String[] idArr = ids.split(",");
        for (String id : idArr) {
            aeaCreditDetailMapper.deleteAeaCreditDetail(id);
        }
    }

    @Override
    public PageInfo<AeaCreditDetail> listAeaCreditDetail(AeaCreditDetail aeaCreditDetail, Page page) {

        aeaCreditDetail.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaCreditDetail> list = aeaCreditDetailMapper.listAeaCreditDetail(aeaCreditDetail);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaCreditDetail>(list);
    }

    @Override
    public AeaCreditDetail getAeaCreditDetailById(String id) {

        if(StringUtils.isBlank(id)) {
            throw new InvalidParameterException("参数id为空!");
        }
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaCreditDetailMapper.getAeaCreditDetailById(id);
    }

    @Override
    public List<AeaCreditDetail> listAeaCreditDetail(AeaCreditDetail aeaCreditDetail) {
        
        List<AeaCreditDetail> list = aeaCreditDetailMapper.listAeaCreditDetail(aeaCreditDetail);
        logger.debug("成功执行查询list！！");
        return list;
    }
}

