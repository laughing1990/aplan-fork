package com.augurit.aplanmis.common.service.applyinst.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaHiApplyinstCorrectDueIninst;
import com.augurit.aplanmis.common.mapper.AeaHiApplyinstCorrectDueIninstMapper;
import com.augurit.aplanmis.common.service.applyinst.AeaHiApplyinstCorrectDueIninstService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 材料补全应收实例表-Service服务接口实现类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:tiantian</li>
 * <li>创建时间：2019-08-28 17:34:02</li>
 * </ul>
 */
@Service
@Transactional
public class AeaHiApplyinstCorrectDueIninstServiceImpl implements AeaHiApplyinstCorrectDueIninstService {

    private static Logger logger = LoggerFactory.getLogger(AeaHiApplyinstCorrectDueIninstServiceImpl.class);

    @Autowired
    private AeaHiApplyinstCorrectDueIninstMapper aeaHiApplyinstCorrectDueIninstMapper;

    public void saveAeaHiApplyinstCorrectDueIninst(AeaHiApplyinstCorrectDueIninst aeaHiApplyinstCorrectDueIninst) throws Exception {
        aeaHiApplyinstCorrectDueIninstMapper.insertAeaHiApplyinstCorrectDueIninst(aeaHiApplyinstCorrectDueIninst);
    }

    public void updateAeaHiApplyinstCorrectDueIninst(AeaHiApplyinstCorrectDueIninst aeaHiApplyinstCorrectDueIninst) throws Exception {
        aeaHiApplyinstCorrectDueIninstMapper.updateAeaHiApplyinstCorrectDueIninst(aeaHiApplyinstCorrectDueIninst);
    }

    public void deleteAeaHiApplyinstCorrectDueIninstById(String id) throws Exception {
        if (id == null)
            throw new InvalidParameterException(id);
        aeaHiApplyinstCorrectDueIninstMapper.deleteAeaHiApplyinstCorrectDueIninst(id);
    }

    public PageInfo<AeaHiApplyinstCorrectDueIninst> listAeaHiApplyinstCorrectDueIninst(AeaHiApplyinstCorrectDueIninst aeaHiApplyinstCorrectDueIninst, Page page) throws Exception {
        PageHelper.startPage(page);
        List<AeaHiApplyinstCorrectDueIninst> list = aeaHiApplyinstCorrectDueIninstMapper.listAeaHiApplyinstCorrectDueIninst(aeaHiApplyinstCorrectDueIninst);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaHiApplyinstCorrectDueIninst>(list);
    }

    public AeaHiApplyinstCorrectDueIninst getAeaHiApplyinstCorrectDueIninstById(String id) throws Exception {
        if (id == null)
            throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaHiApplyinstCorrectDueIninstMapper.getAeaHiApplyinstCorrectDueIninstById(id);
    }

    public List<AeaHiApplyinstCorrectDueIninst> listAeaHiApplyinstCorrectDueIninst(AeaHiApplyinstCorrectDueIninst aeaHiApplyinstCorrectDueIninst) throws Exception {
        List<AeaHiApplyinstCorrectDueIninst> list = aeaHiApplyinstCorrectDueIninstMapper.listAeaHiApplyinstCorrectDueIninst(aeaHiApplyinstCorrectDueIninst);
        logger.debug("成功执行查询list！！");
        return list;
    }

    public void batchSaveAeaHiApplyinstCorrectDueIninst(List<AeaHiApplyinstCorrectDueIninst> aeaHiApplyinstCorrectDueIninsts) throws Exception {
        if (aeaHiApplyinstCorrectDueIninsts.size() < 1) throw new Exception("材料补全实例数组长度为0！");
        for (AeaHiApplyinstCorrectDueIninst aeaHiApplyinstCorrectDueIninst : aeaHiApplyinstCorrectDueIninsts) {
            aeaHiApplyinstCorrectDueIninst.setApplyinstDueIninstId(UUID.randomUUID().toString());
            aeaHiApplyinstCorrectDueIninst.setRootOrgId(SecurityContext.getCurrentOrgId());
            aeaHiApplyinstCorrectDueIninst.setCreater(SecurityContext.getCurrentUserName());
            aeaHiApplyinstCorrectDueIninst.setCreateTime(new Date());
        }

        aeaHiApplyinstCorrectDueIninstMapper.batchInsertAeaHiApplyinstCorrectDueIninst(aeaHiApplyinstCorrectDueIninsts);
    }

    @Override
    public List<AeaHiApplyinstCorrectDueIninst> getCorrectDueIninstByApplyinstCorrectId(String applyinstCorrectId) throws Exception {
        if (StringUtils.isBlank(applyinstCorrectId)) throw new Exception("材料补全实例ID为空！");
        return aeaHiApplyinstCorrectDueIninstMapper.getCorrectDueIninstByApplyinstCorrectId(applyinstCorrectId, SecurityContext.getCurrentOrgId());
    }
}

