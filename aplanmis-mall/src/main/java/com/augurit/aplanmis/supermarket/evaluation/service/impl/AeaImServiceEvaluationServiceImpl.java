package com.augurit.aplanmis.supermarket.evaluation.service.impl;

import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.domain.AeaImServiceEvaluation;
import com.augurit.aplanmis.common.domain.AeaImUnitBidding;
import com.augurit.aplanmis.common.mapper.AeaImServiceEvaluationMapper;
import com.augurit.aplanmis.common.mapper.AeaImUnitBiddingMapper;
import com.augurit.aplanmis.supermarket.evaluation.service.AeaImServiceEvaluationService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
* -Service服务接口实现类
<ul>
    <li>项目名：奥格erp3.0--第一期建设项目</li>
    <li>版本信息：v1.0</li>
    <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
    <li>创建人:thinkpad</li>
    <li>创建时间：2019-06-03 13:59:35</li>
</ul>
*/
@Service
@Transactional
public class AeaImServiceEvaluationServiceImpl implements AeaImServiceEvaluationService {

    private static Logger logger = LoggerFactory.getLogger(AeaImServiceEvaluationServiceImpl.class);


    @Autowired
    private AeaImServiceEvaluationMapper aeaImServiceEvaluationMapper;

    @Autowired
    private AeaImUnitBiddingMapper aeaImUnitBiddingMapper;
    @Value("${dg.sso.access.platform.org.top-org-id}")
    private String topOrgId;

    @Override
    public void saveAeaImServiceEvaluation(AeaImServiceEvaluation aeaImServiceEvaluation) throws Exception{
        aeaImServiceEvaluation.setServiceEvaluationId(UuidUtil.generateUuid());
        aeaImServiceEvaluation.setCreateTime(new Date());
        aeaImServiceEvaluation.setCreater(SecurityContext.getCurrentUserName());
        aeaImServiceEvaluation.setIsDelete(DeletedStatus.NOT_DELETED.getValue());
        aeaImServiceEvaluation.setAuditFlag("2");
        aeaImServiceEvaluation.setRootOrgId(topOrgId);
        aeaImServiceEvaluationMapper.insertAeaImServiceEvaluation(aeaImServiceEvaluation);

        AeaImUnitBidding aeaImUnitBidding = new AeaImUnitBidding();
        aeaImUnitBidding.setUnitBiddingId(aeaImServiceEvaluation.getUnitBiddingId());
        aeaImUnitBidding.setIsEvaluate("1");
        aeaImUnitBiddingMapper.updateAeaImUnitBidding(aeaImUnitBidding);
    }

    @Override
    public void updateAeaImServiceEvaluation(AeaImServiceEvaluation aeaImServiceEvaluation) throws Exception{
        aeaImServiceEvaluationMapper.updateAeaImServiceEvaluation(aeaImServiceEvaluation);
    }

    @Override
    public void deleteAeaImServiceEvaluationById(String id) throws Exception{
        if(StringUtils.isBlank(id))
        throw new InvalidParameterException(id);

        AeaImServiceEvaluation aeaImServiceEvaluation = new AeaImServiceEvaluation();
        aeaImServiceEvaluation.setIsDelete(DeletedStatus.DELETED.getValue());
        aeaImServiceEvaluation.setServiceEvaluationId(id);

        aeaImServiceEvaluationMapper.updateAeaImServiceEvaluation(aeaImServiceEvaluation);
    }

    public PageInfo<AeaImServiceEvaluation> listAeaImServiceEvaluation(AeaImServiceEvaluation aeaImServiceEvaluation,Page page) throws Exception{
        aeaImServiceEvaluation.setRootOrgId(topOrgId);
        PageHelper.startPage(page);
        List<AeaImServiceEvaluation> list = aeaImServiceEvaluationMapper.listAeaImServiceEvaluation(aeaImServiceEvaluation);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaImServiceEvaluation>(list);
    }

    @Override
    public AeaImServiceEvaluation getAeaImServiceEvaluationById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaImServiceEvaluationMapper.getAeaImServiceEvaluationById(id);
    }


    public List<AeaImServiceEvaluation> listAeaImServiceEvaluation(AeaImServiceEvaluation aeaImServiceEvaluation) throws Exception{
        aeaImServiceEvaluation.setRootOrgId(topOrgId);
        List<AeaImServiceEvaluation> list = aeaImServiceEvaluationMapper.listAeaImServiceEvaluation(aeaImServiceEvaluation);
        logger.debug("成功执行查询list！！");
        return list;
    }
}

