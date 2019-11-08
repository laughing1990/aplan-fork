package com.augurit.aplanmis.common.service.applyinst.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaHiApplyinstCorrectRealIninst;
import com.augurit.aplanmis.common.mapper.AeaHiApplyinstCorrectRealIninstMapper;
import com.augurit.aplanmis.common.service.applyinst.AeaHiApplyinstCorrectRealIninstService;
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
* 材料补全已收实例表-Service服务接口实现类
<ul>
    <li>项目名：奥格erp3.0--第一期建设项目</li>
    <li>版本信息：v1.0</li>
    <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
    <li>创建人:tiantian</li>
    <li>创建时间：2019-08-28 17:34:16</li>
</ul>
*/
@Service
@Transactional
public class AeaHiApplyinstCorrectRealIninstServiceImpl implements AeaHiApplyinstCorrectRealIninstService {

    private static Logger logger = LoggerFactory.getLogger(AeaHiApplyinstCorrectRealIninstServiceImpl.class);

    @Autowired
    private AeaHiApplyinstCorrectRealIninstMapper aeaHiApplyinstCorrectRealIninstMapper;

    public void saveAeaHiApplyinstCorrectRealIninst(AeaHiApplyinstCorrectRealIninst aeaHiApplyinstCorrectRealIninst) throws Exception{
        aeaHiApplyinstCorrectRealIninst.setCreater(SecurityContext.getCurrentUserName());
        aeaHiApplyinstCorrectRealIninst.setCreateTime(new Date());
        aeaHiApplyinstCorrectRealIninst.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaHiApplyinstCorrectRealIninstMapper.insertAeaHiApplyinstCorrectRealIninst(aeaHiApplyinstCorrectRealIninst);
    }
    public void updateAeaHiApplyinstCorrectRealIninst(AeaHiApplyinstCorrectRealIninst aeaHiApplyinstCorrectRealIninst) throws Exception{
        aeaHiApplyinstCorrectRealIninst.setModifier(SecurityContext.getCurrentUserName());
        aeaHiApplyinstCorrectRealIninst.setModifyTime(new Date());
        aeaHiApplyinstCorrectRealIninstMapper.updateAeaHiApplyinstCorrectRealIninst(aeaHiApplyinstCorrectRealIninst);
    }
    public void deleteAeaHiApplyinstCorrectRealIninstById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        aeaHiApplyinstCorrectRealIninstMapper.deleteAeaHiApplyinstCorrectRealIninst(id);
    }
    public PageInfo<AeaHiApplyinstCorrectRealIninst> listAeaHiApplyinstCorrectRealIninst(AeaHiApplyinstCorrectRealIninst aeaHiApplyinstCorrectRealIninst,Page page) throws Exception{
        PageHelper.startPage(page);
        List<AeaHiApplyinstCorrectRealIninst> list = aeaHiApplyinstCorrectRealIninstMapper.listAeaHiApplyinstCorrectRealIninst(aeaHiApplyinstCorrectRealIninst);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaHiApplyinstCorrectRealIninst>(list);
    }
    public AeaHiApplyinstCorrectRealIninst getAeaHiApplyinstCorrectRealIninstById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaHiApplyinstCorrectRealIninstMapper.getAeaHiApplyinstCorrectRealIninstById(id);
    }
    public List<AeaHiApplyinstCorrectRealIninst> listAeaHiApplyinstCorrectRealIninst(AeaHiApplyinstCorrectRealIninst aeaHiApplyinstCorrectRealIninst) throws Exception{
        List<AeaHiApplyinstCorrectRealIninst> list = aeaHiApplyinstCorrectRealIninstMapper.listAeaHiApplyinstCorrectRealIninst(aeaHiApplyinstCorrectRealIninst);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public List<AeaHiApplyinstCorrectRealIninst> getCorrectRealIninstByApplyinstCorrectId(String applyinstCorrectId) throws Exception {
        if (StringUtils.isBlank(applyinstCorrectId)) throw new Exception("材料补全实例ID为空!");
        return aeaHiApplyinstCorrectRealIninstMapper.getCorrectRealIninstByApplyinstCorrectId(applyinstCorrectId, SecurityContext.getCurrentOrgId());
    }
}

