package com.augurit.aplanmis.common.service.admin.par.impl;

import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.common.domain.AeaParThemeSample;
import com.augurit.aplanmis.common.mapper.AeaParThemeSampleMapper;
import com.augurit.aplanmis.common.service.admin.par.AeaParThemeSampleAdminService;

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
 * @author ZhangXinhui
 * @date 2019/9/11 011 14:34
 * @desc
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class AeaParThemeSampleAdminServiceImpl implements AeaParThemeSampleAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaParThemeSampleAdminServiceImpl.class);

    @Autowired
    private AeaParThemeSampleMapper aeaParThemeSampleMapper;

    @Override
    public PageInfo<AeaParThemeSample> listAeaParThemeSamplePage(AeaParThemeSample aeaParThemeSample, Page page) {

        PageHelper.startPage(page);
        List<AeaParThemeSample> list = aeaParThemeSampleMapper.listAeaParThemeSample(aeaParThemeSample);
        return new PageInfo(list);
    }

    @Override
    public void saveThemeSample(AeaParThemeSample aeaParThemeSample){

        aeaParThemeSample.setCreater(SecurityContext.getCurrentUserId());
        aeaParThemeSample.setCreateTime(new Date());
        aeaParThemeSample.setThemeSampleId(UuidUtil.generateUuid());
        aeaParThemeSampleMapper.insertAeaParThemeSample(aeaParThemeSample);
    }

    @Override
    public void updateThemeSample(AeaParThemeSample aeaParThemeSample){

        aeaParThemeSample.setModifyTime(new Date());
        aeaParThemeSample.setModifier(SecurityContext.getCurrentUserId());
        aeaParThemeSampleMapper.updateAeaParThemeSample(aeaParThemeSample);
    }

    @Override
    public AeaParThemeSample getAeaParThemeSampleById(String id){

        return aeaParThemeSampleMapper.getAeaParThemeSampleById(id);
    }

    @Override
    public void deleteAeaParThemeSampleById(String id){

        aeaParThemeSampleMapper.deleteAeaParThemeSample(id);
    }

    @Override
    public void batchDelThemeSampleByIds(String[] ids){

        aeaParThemeSampleMapper.batchDelThemeSampleByIds(ids);
    }
}
