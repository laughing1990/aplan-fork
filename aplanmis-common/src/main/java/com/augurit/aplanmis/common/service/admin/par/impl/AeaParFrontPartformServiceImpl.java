package com.augurit.aplanmis.common.service.admin.par.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaParFrontPartform;
import com.augurit.aplanmis.common.mapper.AeaParFrontPartformMapper;
import com.augurit.aplanmis.common.service.admin.par.AeaParFrontPartformService;
import com.augurit.aplanmis.common.vo.AeaParFrontPartformVo;
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
 * 阶段的扩展表单前置检测表-Service服务接口实现类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-11-01 10:48:23</li>
 * </ul>
 */
@Service
@Transactional
public class AeaParFrontPartformServiceImpl implements AeaParFrontPartformService {

    private static Logger logger = LoggerFactory.getLogger(AeaParFrontPartformServiceImpl.class);

    @Autowired
    private AeaParFrontPartformMapper aeaParFrontPartformMapper;

    @Override
    public void saveAeaParFrontPartform(AeaParFrontPartform aeaParFrontPartform) throws Exception {
        checkSame(aeaParFrontPartform);

        aeaParFrontPartform.setCreateTime(new Date());
        aeaParFrontPartform.setCreater(SecurityContext.getCurrentUserId());
        aeaParFrontPartform.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaParFrontPartformMapper.insertAeaParFrontPartform(aeaParFrontPartform);
    }

    @Override
    public void updateAeaParFrontPartform(AeaParFrontPartform aeaParFrontPartform) throws Exception {
//        checkSame(aeaParFrontPartform);

        aeaParFrontPartform.setModifyTime(new Date());
        aeaParFrontPartform.setModifier(SecurityContext.getCurrentUserId());
        aeaParFrontPartformMapper.updateAeaParFrontPartform(aeaParFrontPartform);
    }

    @Override
    public void deleteAeaParFrontPartformById(String id) throws Exception {
        if (StringUtils.isBlank(id)) {
            throw new InvalidParameterException(id);
        }
        String[] ids = id.split(",");
        for (String frontPartformId : ids) {
            aeaParFrontPartformMapper.deleteAeaParFrontPartform(frontPartformId);
        }
    }

    @Override
    public PageInfo<AeaParFrontPartform> listAeaParFrontPartform(AeaParFrontPartform aeaParFrontPartform, Page page) throws Exception {
        PageHelper.startPage(page);
        List<AeaParFrontPartform> list = aeaParFrontPartformMapper.listAeaParFrontPartform(aeaParFrontPartform);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public AeaParFrontPartform getAeaParFrontPartformById(String id) throws Exception {
        if (id == null)
            throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaParFrontPartformMapper.getAeaParFrontPartformById(id);
    }

    @Override
    public List<AeaParFrontPartform> listAeaParFrontPartform(AeaParFrontPartform aeaParFrontPartform) throws Exception {
        List<AeaParFrontPartform> list = aeaParFrontPartformMapper.listAeaParFrontPartform(aeaParFrontPartform);
        logger.debug("成功执行查询list！！");
        return list;
    }

    private void checkSame(AeaParFrontPartform aeaParFrontPartform) throws Exception {
        AeaParFrontPartform queryParFrontPartform = new AeaParFrontPartform();
        queryParFrontPartform.setStageId(aeaParFrontPartform.getStageId());
        queryParFrontPartform.setStagePartformId(aeaParFrontPartform.getStagePartformId());
        List<AeaParFrontPartform> list = aeaParFrontPartformMapper.listAeaParFrontPartform(queryParFrontPartform);
        if (list.size() > 0) {
            throw new RuntimeException("已有配置相同的前置阶段扩展表单检测!");
        }

    }

    @Override
    public PageInfo<AeaParFrontPartformVo> listAeaParFrontPartformVoByPage(AeaParFrontPartform aeaParFrontPartform, Page page) throws Exception {
        PageHelper.startPage(page);
        List<AeaParFrontPartformVo> list = aeaParFrontPartformMapper.listAeaParFrontPartformVo(aeaParFrontPartform);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public Long getMaxSortNo(AeaParFrontPartform aeaParFrontPartform) throws Exception {
        Long sortNo = aeaParFrontPartformMapper.getMaxSortNo(aeaParFrontPartform);
        if (sortNo == null) {
            sortNo = 1l;
        } else {
            sortNo = sortNo + 1;
        }

        return sortNo;
    }

    @Override
    public PageInfo<AeaParFrontPartformVo> listSelectParFrontPartformByPage(AeaParFrontPartform aeaParFrontPartform, Page page) throws Exception {
        PageHelper.startPage(page);
        List<AeaParFrontPartformVo> list = aeaParFrontPartformMapper.listSelectParFrontPartform(aeaParFrontPartform);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public AeaParFrontPartformVo getAeaParFrontPartformVoById(String frontPartformId) throws Exception {
        if (StringUtils.isBlank(frontPartformId)) {
            throw new InvalidParameterException(frontPartformId);
        }
        return aeaParFrontPartformMapper.getAeaParFrontPartformVoById(frontPartformId);
    }

    @Override
    public List<AeaParFrontPartformVo> getAeaParFrontPartformVoByStageId(String stageId) {

        List<AeaParFrontPartformVo> aeaParFrontPartformVos = new ArrayList();
        if (StringUtils.isBlank(stageId)) return aeaParFrontPartformVos;
        aeaParFrontPartformVos.addAll(aeaParFrontPartformMapper.getAeaParFrontPartformVoByStageId(stageId, SecurityContext.getCurrentOrgId()));
        return aeaParFrontPartformVos;
    }

    @Override
    public void batchSaveAeaParFrontPartform(String stageId,String stagePartformIds)throws Exception{
        if(StringUtils.isBlank(stageId)  || StringUtils.isBlank(stagePartformIds)){
            throw new InvalidParameterException(stageId,stagePartformIds);
        }

        String[] stagePartformIdArr = stagePartformIds.split(",");
        if(stagePartformIdArr!=null && stagePartformIdArr.length>0){
            AeaParFrontPartform query = new AeaParFrontPartform();
            query.setStageId(stageId);
            Long maxSortNo = getMaxSortNo(query);
            for(String stagePartformId:stagePartformIdArr){
                AeaParFrontPartform aeaParFrontPartform = new AeaParFrontPartform();
                aeaParFrontPartform.setStageId(stageId);
                aeaParFrontPartform.setStagePartformId(stagePartformId);
                List<AeaParFrontPartform> list = aeaParFrontPartformMapper.listAeaParFrontPartform(aeaParFrontPartform);
                if (list.size() > 0) {
                    continue;
                }
                aeaParFrontPartform.setFrontPartformId(UUID.randomUUID().toString());
                aeaParFrontPartform.setCreateTime(new Date());
                aeaParFrontPartform.setCreater(SecurityContext.getCurrentUserId());
                aeaParFrontPartform.setRootOrgId(SecurityContext.getCurrentOrgId());
                aeaParFrontPartform.setSortNo(maxSortNo);
                aeaParFrontPartform.setIsActive("1");
                aeaParFrontPartformMapper.insertAeaParFrontPartform(aeaParFrontPartform);
                maxSortNo++;
            }
        }
    }

}

