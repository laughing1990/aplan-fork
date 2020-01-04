package com.augurit.aplanmis.common.service.window.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.common.constants.SplitApplyState;
import com.augurit.aplanmis.common.domain.AeaProjApplySplit;
import com.augurit.aplanmis.common.mapper.AeaProjApplySplitMapper;
import com.augurit.aplanmis.common.service.window.AeaProjApplySplitService;
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
* 项目拆分申请表-Service服务接口实现类
*/
@Service
@Transactional
public class AeaProjApplySplitServiceImpl implements AeaProjApplySplitService {

    private static Logger logger = LoggerFactory.getLogger(AeaProjApplySplitServiceImpl.class);

    @Autowired
    private AeaProjApplySplitMapper aeaProjApplySplitMapper;

    public void saveAeaProjApplySplit(AeaProjApplySplit aeaProjApplySplit) throws Exception{
        aeaProjApplySplitMapper.insertAeaProjApplySplit(aeaProjApplySplit);
    }
    public void updateAeaProjApplySplit(AeaProjApplySplit aeaProjApplySplit) throws Exception{
        aeaProjApplySplitMapper.updateAeaProjApplySplit(aeaProjApplySplit);
    }
    public void deleteAeaProjApplySplitById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        aeaProjApplySplitMapper.deleteAeaProjApplySplit(id);
    }
    public PageInfo<AeaProjApplySplit> listAeaProjApplySplit(AeaProjApplySplit aeaProjApplySplit,Page page) throws Exception{
        PageHelper.startPage(page);
        List<AeaProjApplySplit> list = aeaProjApplySplitMapper.listAeaProjApplySplit(aeaProjApplySplit);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaProjApplySplit>(list);
    }
    public AeaProjApplySplit getAeaProjApplySplitById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaProjApplySplitMapper.getAeaProjApplySplitById(id);
    }
    public List<AeaProjApplySplit> listAeaProjApplySplit(AeaProjApplySplit aeaProjApplySplit) throws Exception{
        List<AeaProjApplySplit> list = aeaProjApplySplitMapper.listAeaProjApplySplit(aeaProjApplySplit);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public List<AeaProjApplySplit> listSplitedProjInfo(String projInfoId) {
        return aeaProjApplySplitMapper.listSplitedProjInfo(projInfoId);
    }

    @Override
    public void passed(String applySplitId) throws Exception {
        AeaProjApplySplit aeaProjApplySplit = aeaProjApplySplitMapper.getAeaProjApplySplitById(applySplitId);
        aeaProjApplySplit.setApplyState(SplitApplyState.PASSED.getValue());
        aeaProjApplySplit.setModifier(SecurityContext.getCurrentUserId());
        aeaProjApplySplit.setModifyTime(new Date());
        aeaProjApplySplitMapper.updateAeaProjApplySplit(aeaProjApplySplit);
    }

    @Override
    public void rejected(String applySplitId, String reason) throws Exception {
        AeaProjApplySplit aeaProjApplySplit = aeaProjApplySplitMapper.getAeaProjApplySplitById(applySplitId);
        aeaProjApplySplit.setApplyState(SplitApplyState.REJECTED.getValue());
        aeaProjApplySplit.setSplitMemo(reason);
        aeaProjApplySplit.setModifier(SecurityContext.getCurrentUserId());
        aeaProjApplySplit.setModifyTime(new Date());
        aeaProjApplySplitMapper.updateAeaProjApplySplit(aeaProjApplySplit);
    }
}

