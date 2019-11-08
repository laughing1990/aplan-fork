package com.augurit.aplanmis.common.service.instance.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.common.domain.AeaHiSmsSendApply;
import com.augurit.aplanmis.common.mapper.AeaHiSmsSendApplyMapper;
import com.augurit.aplanmis.common.service.instance.AeaHiSmsSendApplyService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
* -Service服务接口实现类
<ul>
    <li>项目名：奥格erp3.0--第一期建设项目</li>
    <li>版本信息：v1.0</li>
    <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
    <li>创建人:Administrator</li>
    <li>创建时间：2019-08-03 10:36:39</li>
</ul>
*/
@Service
@Transactional
public class AeaHiSmsSendApplyServiceImpl implements AeaHiSmsSendApplyService {

    private static Logger logger = LoggerFactory.getLogger(AeaHiSmsSendApplyServiceImpl.class);

    @Autowired
    private AeaHiSmsSendApplyMapper aeaHiSmsSendApplyMapper;

    public void saveAeaHiSmsSendApply(AeaHiSmsSendApply aeaHiSmsSendApply) throws Exception{
        aeaHiSmsSendApplyMapper.insertAeaHiSmsSendApply(aeaHiSmsSendApply);
    }
    public void updateAeaHiSmsSendApply(AeaHiSmsSendApply aeaHiSmsSendApply) throws Exception{
        aeaHiSmsSendApplyMapper.updateAeaHiSmsSendApply(aeaHiSmsSendApply);
    }
    public void deleteAeaHiSmsSendApplyById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        aeaHiSmsSendApplyMapper.deleteAeaHiSmsSendApply(id);
    }
    public PageInfo<AeaHiSmsSendApply> listAeaHiSmsSendApply(AeaHiSmsSendApply aeaHiSmsSendApply,Page page) throws Exception{
        aeaHiSmsSendApply.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaHiSmsSendApply> list = aeaHiSmsSendApplyMapper.listAeaHiSmsSendApply(aeaHiSmsSendApply);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaHiSmsSendApply>(list);
    }
    public AeaHiSmsSendApply getAeaHiSmsSendApplyById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaHiSmsSendApplyMapper.getAeaHiSmsSendApplyById(id);
    }
    public List<AeaHiSmsSendApply> listAeaHiSmsSendApply(AeaHiSmsSendApply aeaHiSmsSendApply) throws Exception{
        aeaHiSmsSendApply.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaHiSmsSendApply> list = aeaHiSmsSendApplyMapper.listAeaHiSmsSendApply(aeaHiSmsSendApply);
        logger.debug("成功执行查询list！！");
        return list;
    }
}

