package com.augurit.aplanmis.common.service.itemFill.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.common.domain.AeaHiItemFillRealIninst;
import com.augurit.aplanmis.common.mapper.AeaHiItemFillRealIninstMapper;
import com.augurit.aplanmis.common.service.itemFill.AeaHiItemFillRealIninstService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 事项容缺实际补齐材料实例表-Service服务接口实现类
 */
@Service
@Transactional
public class AeaHiItemFillRealIninstServiceImpl implements AeaHiItemFillRealIninstService {

    private static Logger logger = LoggerFactory.getLogger(AeaHiItemFillRealIninstServiceImpl.class);

    @Autowired
    private AeaHiItemFillRealIninstMapper aeaHiItemFillRealIninstMapper;

    public void saveAeaHiItemFillRealIninst(AeaHiItemFillRealIninst aeaHiItemFillRealIninst) throws Exception {
        aeaHiItemFillRealIninstMapper.insertAeaHiItemFillRealIninst(aeaHiItemFillRealIninst);
    }

    public void updateAeaHiItemFillRealIninst(AeaHiItemFillRealIninst aeaHiItemFillRealIninst) throws Exception {
        aeaHiItemFillRealIninstMapper.updateAeaHiItemFillRealIninst(aeaHiItemFillRealIninst);
    }

    public void deleteAeaHiItemFillRealIninstById(String id) throws Exception {
        if (id == null)
            throw new InvalidParameterException(id);
        aeaHiItemFillRealIninstMapper.deleteAeaHiItemFillRealIninst(id);
    }

    public PageInfo<AeaHiItemFillRealIninst> listAeaHiItemFillRealIninst(AeaHiItemFillRealIninst aeaHiItemFillRealIninst, Page page) throws Exception {
        PageHelper.startPage(page);
        List<AeaHiItemFillRealIninst> list = aeaHiItemFillRealIninstMapper.listAeaHiItemFillRealIninst(aeaHiItemFillRealIninst);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaHiItemFillRealIninst>(list);
    }

    public AeaHiItemFillRealIninst getAeaHiItemFillRealIninstById(String id) throws Exception {
        if (id == null)
            throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaHiItemFillRealIninstMapper.getAeaHiItemFillRealIninstById(id);
    }

    public List<AeaHiItemFillRealIninst> listAeaHiItemFillRealIninst(AeaHiItemFillRealIninst aeaHiItemFillRealIninst) throws Exception {
        List<AeaHiItemFillRealIninst> list = aeaHiItemFillRealIninstMapper.listAeaHiItemFillRealIninst(aeaHiItemFillRealIninst);
        logger.debug("成功执行查询list！！");
        return list;
    }
}

