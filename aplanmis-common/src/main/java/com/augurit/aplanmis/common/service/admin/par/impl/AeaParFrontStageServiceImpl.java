package com.augurit.aplanmis.common.service.admin.par.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaParFrontStage;
import com.augurit.aplanmis.common.mapper.AeaParFrontStageMapper;
import com.augurit.aplanmis.common.service.admin.par.AeaParFrontStageService;
import com.augurit.aplanmis.common.vo.AeaParFrontStageVo;
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

/**
 * 阶段的前置阶段检测表-Service服务接口实现类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-11-01 10:48:41</li>
 * </ul>
 */
@Service
@Transactional
public class AeaParFrontStageServiceImpl implements AeaParFrontStageService {

    private static Logger logger = LoggerFactory.getLogger(AeaParFrontStageServiceImpl.class);

    @Autowired
    private AeaParFrontStageMapper aeaParFrontStageMapper;

    @Override
    public void saveAeaParFrontStage(AeaParFrontStage aeaParFrontStage) throws Exception {
        checkSame(aeaParFrontStage);

        aeaParFrontStage.setCreateTime(new Date());
        aeaParFrontStage.setCreater(SecurityContext.getCurrentUserId());
        aeaParFrontStage.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaParFrontStageMapper.insertAeaParFrontStage(aeaParFrontStage);
    }

    @Override
    public void updateAeaParFrontStage(AeaParFrontStage aeaParFrontStage) throws Exception {
        checkSame(aeaParFrontStage);

        aeaParFrontStage.setModifyTime(new Date());
        aeaParFrontStage.setModifier(SecurityContext.getCurrentUserId());
        aeaParFrontStageMapper.updateAeaParFrontStage(aeaParFrontStage);
    }

    @Override
    public void deleteAeaParFrontStageById(String id) throws Exception {
        if (StringUtils.isBlank(id)) {
            throw new InvalidParameterException(id);
        }
        String[] ids = id.split(",");
        for (String frontStageId : ids) {
            aeaParFrontStageMapper.deleteAeaParFrontStage(frontStageId);
        }
    }

    @Override
    public PageInfo<AeaParFrontStage> listAeaParFrontStage(AeaParFrontStage aeaParFrontStage, Page page) throws Exception {
        PageHelper.startPage(page);
        List<AeaParFrontStage> list = aeaParFrontStageMapper.listAeaParFrontStage(aeaParFrontStage);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public AeaParFrontStage getAeaParFrontStageById(String id) throws Exception {
        if (id == null)
            throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaParFrontStageMapper.getAeaParFrontStageById(id);
    }

    @Override
    public List<AeaParFrontStage> listAeaParFrontStage(AeaParFrontStage aeaParFrontStage) throws Exception {
        List<AeaParFrontStage> list = aeaParFrontStageMapper.listAeaParFrontStage(aeaParFrontStage);
        logger.debug("成功执行查询list！！");
        return list;
    }

    private void checkSame(AeaParFrontStage aeaParFrontStage) throws Exception {
        AeaParFrontStage queryParFrontStage = new AeaParFrontStage();
        queryParFrontStage.setStageId(aeaParFrontStage.getStageId());
        queryParFrontStage.setHistStageId(aeaParFrontStage.getHistStageId());
        List<AeaParFrontStage> list = aeaParFrontStageMapper.listAeaParFrontStage(queryParFrontStage);
        if (list.size() > 0) {
            throw new RuntimeException("已有配置相同的前置阶段检测!");
        }

    }

    @Override
    public PageInfo<AeaParFrontStageVo> listAeaParFrontStageVoByPage(AeaParFrontStage aeaParFrontStage, Page page) throws Exception {
        PageHelper.startPage(page);
        List<AeaParFrontStageVo> list = aeaParFrontStageMapper.listAeaParFrontStageVo(aeaParFrontStage);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public Long getMaxSortNo(AeaParFrontStage aeaParFrontStage) throws Exception {
        Long sortNo = aeaParFrontStageMapper.getMaxSortNo(aeaParFrontStage);
        if (sortNo == null) {
            sortNo = 1l;
        } else {
            sortNo = sortNo + 1;
        }

        return sortNo;
    }

    @Override
    public AeaParFrontStageVo getAeaParFrontStageVoById(String frontStageId) throws Exception {
        if (StringUtils.isBlank(frontStageId)) {
            throw new InvalidParameterException(frontStageId);
        }
        return aeaParFrontStageMapper.getAeaParFrontStageVoById(frontStageId);
    }

    @Override
    public List<AeaParFrontStageVo> listSelectParFrontStage(AeaParFrontStage aeaParFrontStage) throws Exception {
        return aeaParFrontStageMapper.listSelectParFrontStage(aeaParFrontStage);
    }

    @Override
    public List<AeaParFrontStageVo> getHistStageByStageId(String stageId) {
        List<AeaParFrontStageVo> aeaParFrontStageVos = new ArrayList();
        if (StringUtils.isBlank(stageId)) return aeaParFrontStageVos;
        return aeaParFrontStageMapper.getHistStageByStageId(stageId, SecurityContext.getCurrentOrgId());
    }
}

