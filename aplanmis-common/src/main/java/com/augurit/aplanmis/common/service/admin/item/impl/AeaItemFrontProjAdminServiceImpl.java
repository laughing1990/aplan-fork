package com.augurit.aplanmis.common.service.admin.item.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaItemFrontItem;
import com.augurit.aplanmis.common.domain.AeaItemFrontPartform;
import com.augurit.aplanmis.common.domain.AeaItemFrontProj;
import com.augurit.aplanmis.common.mapper.AeaItemFrontItemMapper;
import com.augurit.aplanmis.common.mapper.AeaItemFrontPartformMapper;
import com.augurit.aplanmis.common.mapper.AeaItemFrontProjMapper;
import com.augurit.aplanmis.common.service.admin.item.AeaItemFrontProjAdminService;
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
* 项目信息前置检测-Service服务接口实现类
*/
@Service
@Transactional
public class AeaItemFrontProjAdminServiceImpl implements AeaItemFrontProjAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaItemFrontProjAdminServiceImpl.class);

    @Autowired
    private AeaItemFrontProjMapper aeaItemFrontProjMapper;

    @Autowired
    private AeaItemFrontItemMapper frontItemMapper;

    @Autowired
    private AeaItemFrontPartformMapper frontPartformMapper;

    @Override
    public void saveAeaItemFrontProj(AeaItemFrontProj aeaItemFrontProj) {

//        checkSame(aeaItemFrontProj);

        aeaItemFrontProj.setCreateTime(new Date());
        aeaItemFrontProj.setCreater(SecurityContext.getCurrentUserId());
        aeaItemFrontProj.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaItemFrontProjMapper.insertAeaItemFrontProj(aeaItemFrontProj);
    }

    @Override
    public void updateAeaItemFrontProj(AeaItemFrontProj aeaItemFrontProj) {

        aeaItemFrontProj.setModifyTime(new Date());
        aeaItemFrontProj.setModifier(SecurityContext.getCurrentUserId());
        aeaItemFrontProjMapper.updateAeaItemFrontProj(aeaItemFrontProj);
    }

    @Override
    public void deleteAeaItemFrontProjById(String id) {

        if (StringUtils.isBlank(id)) {
            throw new InvalidParameterException(id);
        }
        String[] ids = id.split(",");
        for(String frontProjId :ids) {
            aeaItemFrontProjMapper.deleteAeaItemFrontProj(frontProjId);
        }
    }

    @Override
    public PageInfo<AeaItemFrontProj> listAeaItemFrontProj(AeaItemFrontProj aeaItemFrontProj,Page page) {

        aeaItemFrontProj.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaItemFrontProj> list = aeaItemFrontProjMapper.listAeaItemFrontProj(aeaItemFrontProj);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public AeaItemFrontProj getAeaItemFrontProjById(String id) {

        if(StringUtils.isBlank(id)) {
            throw new InvalidParameterException(id);
        }
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaItemFrontProjMapper.getAeaItemFrontProjById(id);
    }

    @Override
    public List<AeaItemFrontProj> listAeaItemFrontProj(AeaItemFrontProj aeaItemFrontProj) {

        aeaItemFrontProj.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaItemFrontProj> list = aeaItemFrontProjMapper.listAeaItemFrontProj(aeaItemFrontProj);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public Long getMaxSortNo(AeaItemFrontProj aeaItemFrontProj){

        Long sortNo = aeaItemFrontProjMapper.getMaxSortNo(aeaItemFrontProj);
        return sortNo==null?1L:(sortNo+1L);
    }

    private void checkSame(AeaItemFrontProj aeaItemFrontProj){

        AeaItemFrontProj queryItemFrontProj = new AeaItemFrontProj();
        queryItemFrontProj.setItemVerId(aeaItemFrontProj.getItemVerId());
        queryItemFrontProj.setRuleName(aeaItemFrontProj.getRuleName());
        List<AeaItemFrontProj> list = aeaItemFrontProjMapper.listAeaItemFrontProj(queryItemFrontProj);
        if(list.size()>0){
            throw new RuntimeException("已有相同规则名称的项目前置检测!");
        }

        queryItemFrontProj.setRuleName(null);
        queryItemFrontProj.setRuleEl(aeaItemFrontProj.getRuleEl());
        list = aeaItemFrontProjMapper.listAeaItemFrontProj(queryItemFrontProj);
        if(list.size()>0){
            throw new RuntimeException("已有相同规则的项目前置检测!");
        }
    }

    @Override
    public void changIsActive(String id, String rootOrgId){

        aeaItemFrontProjMapper.changIsActive(id, rootOrgId);
    }

    @Override
    public void updateFrontCkSort(String[] ids, Long[] sorts, String type){

        for(int i=0;i<ids.length;i++) {

            if("proj".equals(type)){

                AeaItemFrontProj proj = new AeaItemFrontProj();
                proj.setFrontProjId(ids[i]);
                proj.setSortNo(sorts[i]);
                aeaItemFrontProjMapper.updateAeaItemFrontProj(proj);

            }else if("item".equals(type)){

                AeaItemFrontItem item = new AeaItemFrontItem();
                item.setFrontItemId(ids[i]);
                item.setSortNo(sorts[i]);
                frontItemMapper.updateAeaItemFront(item);

            }else if("partform".equals(type)){

                AeaItemFrontPartform partform = new AeaItemFrontPartform();
                partform.setFrontPartformId(ids[i]);
                partform.setSortNo(sorts[i]);
                frontPartformMapper.updateAeaItemFrontPartform(partform);
            }
        }
    }
}

