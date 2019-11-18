package com.augurit.aplanmis.common.service.admin.par.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaParFrontItemform;
import com.augurit.aplanmis.common.mapper.AeaParFrontItemformMapper;
import com.augurit.aplanmis.common.service.admin.par.AeaParFrontItemformService;
import com.augurit.aplanmis.common.vo.AeaParFrontItemformVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 阶段事项表单前置检测表-Service服务接口实现类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-11-01 10:48:12</li>
 * </ul>
 */
@Service
@Transactional
public class AeaParFrontItemformServiceImpl implements AeaParFrontItemformService {

    private static Logger logger = LoggerFactory.getLogger(AeaParFrontItemformServiceImpl.class);

    @Autowired
    private AeaParFrontItemformMapper aeaParFrontItemformMapper;

    @Override
    public void saveAeaParFrontItemform(AeaParFrontItemform aeaParFrontItemform) throws Exception {
        checkSame(aeaParFrontItemform);

        aeaParFrontItemform.setCreateTime(new Date());
        aeaParFrontItemform.setCreater(SecurityContext.getCurrentUserId());
        aeaParFrontItemform.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaParFrontItemformMapper.insertAeaParFrontItemform(aeaParFrontItemform);
    }

    @Override
    public void updateAeaParFrontItemform(AeaParFrontItemform aeaParFrontItemform) throws Exception {
//        checkSame(aeaParFrontItemform);

        aeaParFrontItemform.setModifyTime(new Date());
        aeaParFrontItemform.setModifier(SecurityContext.getCurrentUserId());
        aeaParFrontItemformMapper.updateAeaParFrontItemform(aeaParFrontItemform);
    }

    @Override
    public void deleteAeaParFrontItemformById(String id) throws Exception {
        if (StringUtils.isBlank(id)) {
            throw new InvalidParameterException(id);
        }
        String[] ids = id.split(",");
        for (String frontItemformId : ids) {
            aeaParFrontItemformMapper.deleteAeaParFrontItemform(frontItemformId);
        }
    }

    @Override
    public PageInfo<AeaParFrontItemform> listAeaParFrontItemform(AeaParFrontItemform aeaParFrontItemform, Page page) throws Exception {
        PageHelper.startPage(page);
        List<AeaParFrontItemform> list = aeaParFrontItemformMapper.listAeaParFrontItemform(aeaParFrontItemform);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public AeaParFrontItemform getAeaParFrontItemformById(String id) throws Exception {
        if (id == null)
            throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaParFrontItemformMapper.getAeaParFrontItemformById(id);
    }

    @Override
    public List<AeaParFrontItemform> listAeaParFrontItemform(AeaParFrontItemform aeaParFrontItemform) throws Exception {
        List<AeaParFrontItemform> list = aeaParFrontItemformMapper.listAeaParFrontItemform(aeaParFrontItemform);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public PageInfo<AeaParFrontItemformVo> listAeaParFrontItemformVoByPage(AeaParFrontItemform aeaParFrontItemform, Page page) throws Exception {
        PageHelper.startPage(page);
        List<AeaParFrontItemformVo> list = aeaParFrontItemformMapper.listAeaParFrontItemformVo(aeaParFrontItemform);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public Long getMaxSortNo(AeaParFrontItemform aeaParFrontItemform) throws Exception {
        Long sortNo = aeaParFrontItemformMapper.getMaxSortNo(aeaParFrontItemform);
        if (sortNo == null) {
            sortNo = 1l;
        } else {
            sortNo = sortNo + 1;
        }

        return sortNo;
    }

    @Override
    public PageInfo<AeaParFrontItemformVo> listSelectParFrontItemformByPage(AeaParFrontItemform aeaParFrontItemform, Page page) throws Exception {
        PageHelper.startPage(page);
        List<AeaParFrontItemformVo> list = aeaParFrontItemformMapper.listSelectParFrontItemform(aeaParFrontItemform);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public AeaParFrontItemformVo getAeaParFrontItemformVoById(String frontItemformId) throws Exception {
        if (StringUtils.isBlank(frontItemformId)) {
            throw new InvalidParameterException(frontItemformId);
        }
        return aeaParFrontItemformMapper.getAeaParFrontItemformVoById(frontItemformId);
    }

    private void checkSame(AeaParFrontItemform aeaParFrontItemform) throws Exception {
        AeaParFrontItemform queryParFrontItemform = new AeaParFrontItemform();
        queryParFrontItemform.setStageId(aeaParFrontItemform.getStageId());
        queryParFrontItemform.setStageItemId(aeaParFrontItemform.getStageItemId());
        List<AeaParFrontItemform> list = aeaParFrontItemformMapper.listAeaParFrontItemform(queryParFrontItemform);
        if (list.size() > 0) {
            throw new RuntimeException("已有配置相同的前置事项表单检测!");
        }

    }

    @Override
    public List<AeaParFrontItemformVo> getAeaParFrontItemformVoByStageId(String stageId) {
        List<AeaParFrontItemformVo> aeaParFrontItemformVos = new ArrayList();
        if (StringUtils.isBlank(stageId)) return aeaParFrontItemformVos;
        aeaParFrontItemformVos.addAll(aeaParFrontItemformMapper.getAeaParFrontItemformVoByStageId(stageId, SecurityContext.getCurrentOrgId()));
        return aeaParFrontItemformVos;
    }

    @Override
    public void batchSaveAeaParFrontItemform(String stageId,String stageItemIds)throws Exception{
        if(StringUtils.isBlank(stageId)  || StringUtils.isBlank(stageItemIds)){
            throw new InvalidParameterException(stageId,stageItemIds);
        }

        String[] stageItemIdArr = stageItemIds.split(",");
        if(stageItemIdArr!=null && stageItemIdArr.length>0){
            AeaParFrontItemform query = new AeaParFrontItemform();
            query.setStageId(stageId);
            Long maxSortNo = getMaxSortNo(query);
            for(String stageItemId:stageItemIdArr){
                AeaParFrontItemform aeaParFrontItemform = new AeaParFrontItemform();
                aeaParFrontItemform.setStageId(stageId);
                aeaParFrontItemform.setStageItemId(stageItemId);
                List<AeaParFrontItemform> list = aeaParFrontItemformMapper.listAeaParFrontItemform(aeaParFrontItemform);
                if (list.size() > 0) {
                    continue;
                }
                aeaParFrontItemform.setFrontItemformId(UUID.randomUUID().toString());
                aeaParFrontItemform.setCreateTime(new Date());
                aeaParFrontItemform.setCreater(SecurityContext.getCurrentUserId());
                aeaParFrontItemform.setRootOrgId(SecurityContext.getCurrentOrgId());
                aeaParFrontItemform.setSortNo(maxSortNo);
                aeaParFrontItemform.setIsActive("1");
                aeaParFrontItemformMapper.insertAeaParFrontItemform(aeaParFrontItemform);
                maxSortNo++;
            }
        }
    }

}

