package com.augurit.aplanmis.common.service.applyinstCancel.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.common.domain.AeaHiItemCancel;
import com.augurit.aplanmis.common.mapper.AeaHiItemCancelMapper;
import com.augurit.aplanmis.common.service.applyinstCancel.AeaHiItemCancelService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 办件撤件实例表-Service服务接口实现类
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-12-12 14:18:59</li>
 * </ul>
 */
@Service
@Transactional
public class AeaHiItemCancelServiceImpl implements AeaHiItemCancelService {

    private static Logger logger = LoggerFactory.getLogger(AeaHiItemCancelServiceImpl.class);

    @Autowired
    private AeaHiItemCancelMapper aeaHiItemCancelMapper;

    public void saveAeaHiItemCancel(AeaHiItemCancel aeaHiItemCancel) throws Exception {
        aeaHiItemCancelMapper.insertAeaHiItemCancel(aeaHiItemCancel);
    }

    public void updateAeaHiItemCancel(AeaHiItemCancel aeaHiItemCancel) throws Exception {
        aeaHiItemCancelMapper.updateAeaHiItemCancel(aeaHiItemCancel);
    }

    public void deleteAeaHiItemCancelById(String id) throws Exception {
        if (id == null)
            throw new InvalidParameterException(id);
        aeaHiItemCancelMapper.deleteAeaHiItemCancel(id);
    }

    public PageInfo<AeaHiItemCancel> listAeaHiItemCancel(AeaHiItemCancel aeaHiItemCancel, Page page) throws Exception {
        PageHelper.startPage(page);
        List<AeaHiItemCancel> list = aeaHiItemCancelMapper.listAeaHiItemCancel(aeaHiItemCancel);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaHiItemCancel>(list);
    }

    public AeaHiItemCancel getAeaHiItemCancelById(String id) throws Exception {
        if (id == null)
            throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaHiItemCancelMapper.getAeaHiItemCancelById(id);
    }

    public List<AeaHiItemCancel> listAeaHiItemCancel(AeaHiItemCancel aeaHiItemCancel) throws Exception {
        List<AeaHiItemCancel> list = aeaHiItemCancelMapper.listAeaHiItemCancel(aeaHiItemCancel);
        logger.debug("成功执行查询list！！");
        return list;
    }
}

