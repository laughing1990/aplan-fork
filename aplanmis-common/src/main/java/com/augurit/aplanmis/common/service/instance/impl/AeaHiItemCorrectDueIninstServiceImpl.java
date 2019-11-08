package com.augurit.aplanmis.common.service.instance.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaHiItemCorrectDueIninst;
import com.augurit.aplanmis.common.mapper.AeaHiItemCorrectDueIninstMapper;
import com.augurit.aplanmis.common.service.instance.AeaHiItemCorrectDueIninstService;
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
 * 事项输入输出实例表-Service服务接口实现类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-08-03 10:29:47</li>
 * </ul>
 */
@Service
@Transactional
public class AeaHiItemCorrectDueIninstServiceImpl implements AeaHiItemCorrectDueIninstService {

    private static Logger logger = LoggerFactory.getLogger(AeaHiItemCorrectDueIninstServiceImpl.class);

    @Autowired
    private AeaHiItemCorrectDueIninstMapper aeaHiItemCorrectDueIninstMapper;

    public void saveAeaHiItemCorrectDueIninst(AeaHiItemCorrectDueIninst aeaHiItemCorrectDueIninst) throws Exception {
        aeaHiItemCorrectDueIninstMapper.insertAeaHiItemCorrectDueIninst(aeaHiItemCorrectDueIninst);
    }

    public void batchSaveAeaHiItemCorrectDueIninst(List<AeaHiItemCorrectDueIninst> aeaHiItemCorrectDueIninsts) throws Exception {
        if (aeaHiItemCorrectDueIninsts.size() < 1) throw new Exception("材料补正实例数组长度为0！");
        for (AeaHiItemCorrectDueIninst aeaHiItemCorrectDueIninst : aeaHiItemCorrectDueIninsts) {
            aeaHiItemCorrectDueIninst.setRootOrgId(SecurityContext.getCurrentOrgId());
            aeaHiItemCorrectDueIninst.setCreater(SecurityContext.getCurrentUserName());
            aeaHiItemCorrectDueIninst.setCreateTime(new Date());
        }

        aeaHiItemCorrectDueIninstMapper.batchInsertAeaHiItemCorrectDueIninst(aeaHiItemCorrectDueIninsts);
    }


    public void updateAeaHiItemCorrectDueIninst(AeaHiItemCorrectDueIninst aeaHiItemCorrectDueIninst) throws Exception {
        aeaHiItemCorrectDueIninstMapper.updateAeaHiItemCorrectDueIninst(aeaHiItemCorrectDueIninst);
    }

    public void deleteAeaHiItemCorrectDueIninstById(String id) throws Exception {
        if (id == null)
            throw new InvalidParameterException(id);
        aeaHiItemCorrectDueIninstMapper.deleteAeaHiItemCorrectDueIninst(id);
    }

    public PageInfo<AeaHiItemCorrectDueIninst> listAeaHiItemCorrectDueIninst(AeaHiItemCorrectDueIninst aeaHiItemCorrectDueIninst, Page page) throws Exception {
        aeaHiItemCorrectDueIninst.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaHiItemCorrectDueIninst> list = aeaHiItemCorrectDueIninstMapper.listAeaHiItemCorrectDueIninst(aeaHiItemCorrectDueIninst);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaHiItemCorrectDueIninst>(list);
    }

    public AeaHiItemCorrectDueIninst getAeaHiItemCorrectDueIninstById(String id) throws Exception {
        if (id == null)
            throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaHiItemCorrectDueIninstMapper.getAeaHiItemCorrectDueIninstById(id);
    }

    public List<AeaHiItemCorrectDueIninst> listAeaHiItemCorrectDueIninst(AeaHiItemCorrectDueIninst aeaHiItemCorrectDueIninst) throws Exception {
        aeaHiItemCorrectDueIninst.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaHiItemCorrectDueIninst> list = aeaHiItemCorrectDueIninstMapper.listAeaHiItemCorrectDueIninst(aeaHiItemCorrectDueIninst);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public List<AeaHiItemCorrectDueIninst> getCorrectDueIninstByCorrectId(String correctId) throws Exception {
        if (StringUtils.isBlank(correctId)) throw new Exception("材料补正实例ID为空！");
        return aeaHiItemCorrectDueIninstMapper.getCorrectDueIninstByCorrectId(correctId, SecurityContext.getCurrentOrgId());
    }
}

