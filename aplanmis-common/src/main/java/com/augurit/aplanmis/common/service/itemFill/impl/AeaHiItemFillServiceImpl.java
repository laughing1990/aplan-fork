package com.augurit.aplanmis.common.service.itemFill.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.common.domain.AeaHiItemFill;
import com.augurit.aplanmis.common.mapper.AeaHiItemFillMapper;
import com.augurit.aplanmis.common.service.itemFill.AeaHiItemFillService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 事项容缺补齐实例表-Service服务接口实现类
 */
@Service
@Transactional
public class AeaHiItemFillServiceImpl implements AeaHiItemFillService {

    private static Logger logger = LoggerFactory.getLogger(AeaHiItemFillServiceImpl.class);

    @Autowired
    private AeaHiItemFillMapper aeaHiItemFillMapper;

    public void saveAeaHiItemFill(AeaHiItemFill aeaHiItemFill) throws Exception {
        aeaHiItemFillMapper.insertAeaHiItemFill(aeaHiItemFill);
    }

    public void updateAeaHiItemFill(AeaHiItemFill aeaHiItemFill) throws Exception {
        aeaHiItemFillMapper.updateAeaHiItemFill(aeaHiItemFill);
    }

    public void deleteAeaHiItemFillById(String id) throws Exception {
        if (id == null)
            throw new InvalidParameterException(id);
        aeaHiItemFillMapper.deleteAeaHiItemFill(id);
    }

    public PageInfo<AeaHiItemFill> listAeaHiItemFill(AeaHiItemFill aeaHiItemFill, Page page) throws Exception {
        PageHelper.startPage(page);
        List<AeaHiItemFill> list = aeaHiItemFillMapper.listAeaHiItemFill(aeaHiItemFill);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaHiItemFill>(list);
    }

    public AeaHiItemFill getAeaHiItemFillById(String id) throws Exception {
        if (id == null)
            throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaHiItemFillMapper.getAeaHiItemFillById(id);
    }

    public List<AeaHiItemFill> listAeaHiItemFill(AeaHiItemFill aeaHiItemFill) throws Exception {
        List<AeaHiItemFill> list = aeaHiItemFillMapper.listAeaHiItemFill(aeaHiItemFill);
        logger.debug("成功执行查询list！！");
        return list;
    }
}

