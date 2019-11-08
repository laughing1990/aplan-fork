package com.augurit.aplanmis.common.service.admin.item.impl;

import com.augurit.agcloud.bsc.util.CommonConstant;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaItemServiceBasic;
import com.augurit.aplanmis.common.domain.AeaServiceLegalClause;
import com.augurit.aplanmis.common.mapper.AeaItemServiceBasicMapper;
import com.augurit.aplanmis.common.mapper.AeaServiceLegalClauseMapper;
import com.augurit.aplanmis.common.service.admin.item.AeaItemServiceBasicAdminService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 设立依据-Service服务接口实现类
 */
@Service
@Transactional
public class AeaItemServiceBasicAdminServiceImpl implements AeaItemServiceBasicAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaItemServiceBasicAdminServiceImpl.class);

    @Autowired
    private AeaItemServiceBasicMapper aeaItemServiceBasicMapper;

    @Autowired
    private AeaServiceLegalClauseMapper aeaServiceLegalClauseMapper;

    @Override
    public void saveAeaItemServiceBasic(AeaItemServiceBasic aeaItemServiceBasic) throws Exception {

        aeaItemServiceBasic.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaItemServiceBasic.setCreater(SecurityContext.getCurrentUserId());
        aeaItemServiceBasic.setCreateTime(new Date());
        aeaItemServiceBasicMapper.insertAeaItemServiceBasic(aeaItemServiceBasic);
    }

    @Override
    public void updateAeaItemServiceBasic(AeaItemServiceBasic aeaItemServiceBasic) throws Exception {

        aeaItemServiceBasic.setModifier(SecurityContext.getCurrentUserId());
        aeaItemServiceBasic.setModifyTime(new Date());
        aeaItemServiceBasicMapper.updateAeaItemServiceBasic(aeaItemServiceBasic);
    }

    @Override
    public void deleteAeaItemServiceBasicById(String id) throws Exception {

        Assert.notNull(id, "id is null.");
        aeaItemServiceBasicMapper.deleteAeaItemServiceBasic(id);
    }

    @Override
    public PageInfo<AeaItemServiceBasic> listAeaItemServiceBasic(AeaItemServiceBasic aeaItemServiceBasic, Page page) throws Exception {

        aeaItemServiceBasic.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaItemServiceBasic> list = aeaItemServiceBasicMapper.listAeaItemServiceBasicWithLegal(aeaItemServiceBasic);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaItemServiceBasic>(list);
    }

    @Override
    public AeaItemServiceBasic getAeaItemServiceBasicById(String id) throws Exception {

        Assert.notNull(id, "id is null.");
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        AeaItemServiceBasic basic = aeaItemServiceBasicMapper.getAeaItemServiceBasicById(id);
        if (basic!=null&&StringUtils.isNotBlank(basic.getLegalClauseId())) {
            AeaServiceLegalClause clause = aeaServiceLegalClauseMapper.getLegalAndClauseByClauseId(basic.getLegalClauseId());
            if(clause!=null){
                BeanUtils.copyProperties(basic, clause);
            }
        }
        return basic;
    }

    @Override
    public List<AeaItemServiceBasic> listAeaItemServiceBasic(AeaItemServiceBasic aeaItemServiceBasic) throws Exception {

        aeaItemServiceBasic.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaItemServiceBasic> list = aeaItemServiceBasicMapper.listAeaItemServiceBasic(aeaItemServiceBasic);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public void batchDeleteServiceBasic(String[] ids) throws Exception {

        for (String id : ids) {
            aeaItemServiceBasicMapper.deleteAeaItemServiceBasic(id);
        }
    }

    @Override
    public void batchSaveServiceBasic(AeaItemServiceBasic aeaItemServiceBasic) throws Exception {

        //先删除
        String rootOrgId = SecurityContext.getCurrentOrgId();
        aeaItemServiceBasic.setRootOrgId(rootOrgId);
        aeaItemServiceBasicMapper.deleteAeaItemServiceBasicByCondition(aeaItemServiceBasic, null);
        if (StringUtils.isNotBlank(aeaItemServiceBasic.getLegalClauseId())) {
            String[] clauseIds = aeaItemServiceBasic.getLegalClauseId().split(CommonConstant.COMMA_SEPARATOR);
            if(clauseIds!=null&&clauseIds.length>0){
                String userId = SecurityContext.getCurrentUserId();
                for (String clauseId : clauseIds) {
                    AeaItemServiceBasic aisb = new AeaItemServiceBasic();
                    aisb.setBasicId(UUID.randomUUID().toString());
                    aisb.setTableName(aeaItemServiceBasic.getTableName());
                    aisb.setPkName(aeaItemServiceBasic.getPkName());
                    aisb.setRecordId(aeaItemServiceBasic.getRecordId());
                    aisb.setRootOrgId(rootOrgId);
                    aisb.setLegalClauseId(clauseId);
                    aisb.setCreater(userId);
                    aisb.setCreateTime(new Date());
                    aeaItemServiceBasicMapper.insertAeaItemServiceBasic(aisb);
                }
            }
        }
    }
}

