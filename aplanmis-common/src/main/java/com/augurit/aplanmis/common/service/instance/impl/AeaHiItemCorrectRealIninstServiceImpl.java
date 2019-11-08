package com.augurit.aplanmis.common.service.instance.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaHiItemCorrectRealIninst;
import com.augurit.aplanmis.common.mapper.AeaHiItemCorrectRealIninstMapper;
import com.augurit.aplanmis.common.service.instance.AeaHiItemCorrectRealIninstService;
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
 * <li>创建时间：2019-08-03 10:31:32</li>
 * </ul>
 */
@Service
@Transactional
public class AeaHiItemCorrectRealIninstServiceImpl implements AeaHiItemCorrectRealIninstService {

    private static Logger logger = LoggerFactory.getLogger(AeaHiItemCorrectRealIninstServiceImpl.class);

    @Autowired
    private AeaHiItemCorrectRealIninstMapper aeaHiItemCorrectRealIninstMapper;

    public void saveAeaHiItemCorrectRealIninst(AeaHiItemCorrectRealIninst aeaHiItemCorrectRealIninst) throws Exception {
        aeaHiItemCorrectRealIninst.setCreater(SecurityContext.getCurrentUserName());
        aeaHiItemCorrectRealIninst.setCreateTime(new Date());
        aeaHiItemCorrectRealIninst.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaHiItemCorrectRealIninstMapper.insertAeaHiItemCorrectRealIninst(aeaHiItemCorrectRealIninst);
    }

    public void updateAeaHiItemCorrectRealIninst(AeaHiItemCorrectRealIninst aeaHiItemCorrectRealIninst) throws Exception {
        aeaHiItemCorrectRealIninst.setModifier(SecurityContext.getCurrentUserName());
        aeaHiItemCorrectRealIninst.setModifyTime(new Date());
        aeaHiItemCorrectRealIninstMapper.updateAeaHiItemCorrectRealIninst(aeaHiItemCorrectRealIninst);
    }

    public void deleteAeaHiItemCorrectRealIninstById(String id) throws Exception {
        if (id == null)
            throw new InvalidParameterException(id);
        aeaHiItemCorrectRealIninstMapper.deleteAeaHiItemCorrectRealIninst(id);
    }

    public PageInfo<AeaHiItemCorrectRealIninst> listAeaHiItemCorrectRealIninst(AeaHiItemCorrectRealIninst aeaHiItemCorrectRealIninst, Page page) throws Exception {
        aeaHiItemCorrectRealIninst.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaHiItemCorrectRealIninst> list = aeaHiItemCorrectRealIninstMapper.listAeaHiItemCorrectRealIninst(aeaHiItemCorrectRealIninst);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaHiItemCorrectRealIninst>(list);
    }

    public AeaHiItemCorrectRealIninst getAeaHiItemCorrectRealIninstById(String id) throws Exception {
        if (id == null)
            throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaHiItemCorrectRealIninstMapper.getAeaHiItemCorrectRealIninstById(id);
    }

    public List<AeaHiItemCorrectRealIninst> listAeaHiItemCorrectRealIninst(AeaHiItemCorrectRealIninst aeaHiItemCorrectRealIninst) throws Exception {
        aeaHiItemCorrectRealIninst.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaHiItemCorrectRealIninst> list = aeaHiItemCorrectRealIninstMapper.listAeaHiItemCorrectRealIninst(aeaHiItemCorrectRealIninst);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public List<AeaHiItemCorrectRealIninst> getCorrectRealIninstByCorrectId(String correctId) throws Exception {
        if (StringUtils.isBlank(correctId)) throw new Exception("材料补正实例ID为空!");
        return aeaHiItemCorrectRealIninstMapper.getCorrectRealIninstByCorrectId(correctId, SecurityContext.getCurrentOrgId());
    }
}

