package com.augurit.aplanmis.common.service.itemFill.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.common.domain.AeaHiItemFillDueIninst;
import com.augurit.aplanmis.common.mapper.AeaHiItemFillDueIninstMapper;
import com.augurit.aplanmis.common.service.itemFill.AeaHiItemFillDueIninstService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 事项容缺要求补齐材料实例表-Service服务接口实现类
 */
@Service
@Transactional
public class AeaHiItemFillDueIninstServiceImpl implements AeaHiItemFillDueIninstService {

    private static Logger logger = LoggerFactory.getLogger(AeaHiItemFillDueIninstServiceImpl.class);

    @Autowired
    private AeaHiItemFillDueIninstMapper aeaHiItemFillDueIninstMapper;

    public void saveAeaHiItemFillDueIninst(AeaHiItemFillDueIninst aeaHiItemFillDueIninst) throws Exception {
        aeaHiItemFillDueIninstMapper.insertAeaHiItemFillDueIninst(aeaHiItemFillDueIninst);
    }

    public void updateAeaHiItemFillDueIninst(AeaHiItemFillDueIninst aeaHiItemFillDueIninst) throws Exception {
        aeaHiItemFillDueIninstMapper.updateAeaHiItemFillDueIninst(aeaHiItemFillDueIninst);
    }

    public void deleteAeaHiItemFillDueIninstById(String id) throws Exception {
        if (id == null)
            throw new InvalidParameterException(id);
        aeaHiItemFillDueIninstMapper.deleteAeaHiItemFillDueIninst(id);
    }

    public PageInfo<AeaHiItemFillDueIninst> listAeaHiItemFillDueIninst(AeaHiItemFillDueIninst aeaHiItemFillDueIninst, Page page) throws Exception {
        PageHelper.startPage(page);
        List<AeaHiItemFillDueIninst> list = aeaHiItemFillDueIninstMapper.listAeaHiItemFillDueIninst(aeaHiItemFillDueIninst);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaHiItemFillDueIninst>(list);
    }

    public AeaHiItemFillDueIninst getAeaHiItemFillDueIninstById(String id) throws Exception {
        if (id == null)
            throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaHiItemFillDueIninstMapper.getAeaHiItemFillDueIninstById(id);
    }

    public List<AeaHiItemFillDueIninst> listAeaHiItemFillDueIninst(AeaHiItemFillDueIninst aeaHiItemFillDueIninst) throws Exception {
        List<AeaHiItemFillDueIninst> list = aeaHiItemFillDueIninstMapper.listAeaHiItemFillDueIninst(aeaHiItemFillDueIninst);
        logger.debug("成功执行查询list！！");
        return list;
    }
}

